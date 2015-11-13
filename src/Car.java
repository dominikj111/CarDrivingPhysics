import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.BodyType;
import org.jbox2d.dynamics.joints.PrismaticJoint;
import org.jbox2d.dynamics.joints.PrismaticJointDef;
import org.jbox2d.dynamics.joints.RevoluteJoint;
import org.jbox2d.dynamics.joints.RevoluteJointDef;
import processing.core.PGraphics;
import shiffman.box2d.Box2DProcessing;

import java.lang.reflect.InvocationTargetException;

public class Car extends Manager {

    private Wheel leftFrontWheel;
    private Wheel rightFrontWheel;
    private Wheel leftRearWheel;
    private Wheel rightRearWheel;
    private CarBody car;
    private RevoluteJoint jointLeftFrontWheel;
    private RevoluteJoint jointRightFrontWheel;
    private PrismaticJoint jointLeftRearWheel;
    private PrismaticJoint jointRightRearWheel;
    private int x;
    private int y;
    private int carWidth;
    private int carHeight;
    private int wheelWidth;
    private int wheelHeight;
    private float engineSpeed;
    private float steeringAngle;
    private float horsePower;
    private float maxSteerAngle;
    private float steerSpeed;


    public Car(int x, int y, Box2DProcessing box2d) {
        super(box2d);

        this.x = x;
        this.y = y;

        this.carWidth = 100;
        this.carHeight = 50;
        this.wheelWidth = 20;
        this.wheelHeight = 10;

        this.engineSpeed = 0;
        this.steeringAngle = 0;
        this.horsePower = 40;
        this.maxSteerAngle = (float) (Math.PI / 3);
        this.steerSpeed = 1.5f;

        init();
    }


    private void init() {

        try {
            leftFrontWheel = defineEntity(Wheel.class, getX() + getWheelWidth(), getY() - (getWheelWidth() + getWheelHeight() / 2), getWheelWidth(), getWheelHeight());
            rightFrontWheel = defineEntity(Wheel.class, getX() + getWheelWidth(), getY() + (getWheelWidth() + getWheelHeight() / 2), getWheelWidth(), getWheelHeight());
            leftRearWheel = defineEntity(Wheel.class, getX() - getWheelWidth(), getY() - (getWheelWidth() + getWheelHeight() / 2), getWheelWidth(), getWheelHeight());
            rightRearWheel = defineEntity(Wheel.class, getX() - getWheelWidth(), getY() + (getWheelWidth() + getWheelHeight() / 2), getWheelWidth(), getWheelHeight());

            car = defineEntity(CarBody.class, getX(), getY(), getCarWidth(), getCarHeight());
        } catch (Exception ex){
            /*throw new Exception("Cannot create car parts - wheels, carBody in init method.\n" +
                                "Probably bad constructor in WorldEntity offspring.");*/

            System.out.println("Cannot create car parts - wheels, carBody in init method.\n" +
                               "Probably bad constructor in WorldEntity offspring.");
            System.exit(0);
        }


        jointLeftFrontWheel = createJoints(getCar().getBody(), getLeftFrontWheel().getBody());
        jointRightFrontWheel = createJoints(getCar().getBody(), getRightFrontWheel().getBody());

        jointLeftRearWheel = createStaticJoints(car.getBody(), leftRearWheel.getBody());
        jointRightRearWheel = createStaticJoints(car.getBody(), rightRearWheel.getBody());
    }

    public void update(boolean[] inputControlImage) {

        applyControlImage(inputControlImage);

        getLeftFrontWheel().updateDirectionVelocity(getEngineSpeed());
        getRightFrontWheel().updateDirectionVelocity(getEngineSpeed());
        getLeftRearWheel().updateDirectionVelocity(0);
        getRightRearWheel().updateDirectionVelocity(0);
        
        //Steering
        float motorSpeed;

        motorSpeed = getSteeringAngle() - getJointLeftFrontWheel().getJointAngle();
        getJointLeftFrontWheel().setMotorSpeed(motorSpeed * getSteerSpeed());

        motorSpeed = getSteeringAngle() - getJointRightFrontWheel().getJointAngle();
        getJointRightFrontWheel().setMotorSpeed(motorSpeed * getSteerSpeed());
    }




    private RevoluteJoint createJoints(Body b1, Body b2) {
        RevoluteJointDef rjd = new RevoluteJointDef();
        rjd.initialize(b1, b2, b2.getWorldCenter());
        rjd.enableMotor = true;
        rjd.maxMotorTorque = 1000.0f;


        rjd.enableLimit = true;
        rjd.lowerAngle = (float) (-50 * Math.PI / 180);
        rjd.upperAngle = (float) (50 * Math.PI / 180);

        return (RevoluteJoint) getBox2d().world.createJoint(rjd);
    }

    private PrismaticJoint createStaticJoints(Body b1, Body b2) {
        PrismaticJointDef rjd = new PrismaticJointDef();
        rjd.initialize(b1, b2, b2.getWorldCenter(), new Vec2(1, 0));
        
        rjd.enableLimit = true;
        rjd.lowerTranslation = 0;
        rjd.upperTranslation = 0;
        
        /*
        RevoluteJointDef, RevoluteJoint
        rjd.enableLimit = true;
        rjd.lowerAngle = 0;
        rjd.upperAngle = 0;
        */
        
        return (PrismaticJoint) getBox2d().world.createJoint(rjd);
    }

    /*
    private WorldEntity defineEntity(Class entityType, int x, int y, int width, int height) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        WorldEntity tempEntity = (WorldEntity)entityType.getConstructor(entityType).newInstance(x, y, width, height, getBox2d());
        tempEntity.initEntity();
        getWorldEntities().add(tempEntity);
        return tempEntity;
    }
    */

    private <Type> Type defineEntity(Class<Type> entityType, int x, int y, int width, int height) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {

        WorldEntity tempEntity = (WorldEntity)entityType.getConstructors()[0].newInstance(this, x, y, width, height, getBox2d());
        tempEntity.initEntity();
        addWorldEntity(tempEntity);
        return entityType.cast(tempEntity);
    }

    private void applyControlImage(boolean[] controlImage){

       /* for (int i = 0; i < controlImage.length; i++) {
            System.out.print("["+((controlImage[i]) ? 1 : 0)+"]");
        }
        System.out.println();*/

        if(isUpDownSame(controlImage)){setEngineSpeed(0);}

        if(isOnlyUp(controlImage)){setEngineSpeed(getHorsePower());}

        if(isOnlyDown(controlImage)){setEngineSpeed(-getHorsePower());}


        if(isLeftRightSame(controlImage)){setSteeringAngle(0);}

        if(isOnlyLeft(controlImage)){setSteeringAngle(getMaxSteerAngle());}

        if(isOnlyRight(controlImage)){setSteeringAngle(-getMaxSteerAngle());}
    }


    /* Accessors */
    public void setEngineSpeed(float engineSpeed) {
        this.engineSpeed = engineSpeed;
    }

    public void setSteeringAngle(float steeringAngle) {
        this.steeringAngle = steeringAngle;
    }

    public float getHorsePower() {
        return this.horsePower;
    }

    public float getMaxSteerAngle() {
        return this.maxSteerAngle;
    }

    public float getSteerSpeed() {
        return this.steerSpeed;
    }

    public float getEngineSpeed() {
        return this.engineSpeed;
    }

    public float getSteeringAngle() {
        return this.steeringAngle;
    }

    public int getX() {
        return this.x;
    }

    public int getY() {
        return this.y;
    }

    public int getCarWidth() {
        return this.carWidth;
    }

    public int getCarHeight() {
        return this.carHeight;
    }

    public int getWheelWidth() {
        return this.wheelWidth;
    }

    public int getWheelHeight() {
        return this.wheelHeight;
    }



    private Wheel getLeftFrontWheel() {
        return this.leftFrontWheel;
    }

    private Wheel getRightFrontWheel() {
        return this.rightFrontWheel;
    }

    private Wheel getLeftRearWheel() {
        return this.leftRearWheel;
    }

    private Wheel getRightRearWheel() {
        return this.rightRearWheel;
    }

    private CarBody getCar() {
        return this.car;
    }

    private RevoluteJoint getJointLeftFrontWheel() {
        return this.jointLeftFrontWheel;
    }

    private RevoluteJoint getJointRightFrontWheel() {
        return this.jointRightFrontWheel;
    }




    private boolean isUpDownSame(boolean[] controlImage){
        return controlImage[MonitoredKeys.UP.getIndex()] == controlImage[MonitoredKeys.DOWN.getIndex()];
    }

    private boolean isOnlyUp(boolean[] controlImage){
        return controlImage[MonitoredKeys.UP.getIndex()] && !controlImage[MonitoredKeys.DOWN.getIndex()];
    }

    private boolean isOnlyDown(boolean[] controlImage){
        return !controlImage[MonitoredKeys.UP.getIndex()] && controlImage[MonitoredKeys.DOWN.getIndex()];
    }

    private boolean isLeftRightSame(boolean[] controlImage){
        return controlImage[MonitoredKeys.LEFT.getIndex()] == controlImage[MonitoredKeys.RIGHT.getIndex()];
    }

    private boolean isOnlyLeft(boolean[] controlImage){
        return controlImage[MonitoredKeys.LEFT.getIndex()] && !controlImage[MonitoredKeys.RIGHT.getIndex()];
    }

    private boolean isOnlyRight(boolean[] controlImage){
        return !controlImage[MonitoredKeys.LEFT.getIndex()] && controlImage[MonitoredKeys.RIGHT.getIndex()];
    }




    class Wheel extends WorldEntity {

        private int x;
        private int y;
        private int width;
        private int height;

        public Wheel(int x, int y, int width, int height, Box2DProcessing box2d) {
            super(box2d);

            this.x = x;
            this.y = y;
            this.width = width;
            this.height = height;
        }

        public void updateDirectionVelocity(float engineSpeed){
            killOrthogonalVelocity();

            Vec2 direction = new Vec2();
            getBody().getTransform().q.getXAxis(direction);
            direction = direction.mul(engineSpeed);

            getBody().setLinearVelocity(direction);
        }

        @Override
        public void paint(PGraphics g) {
            Vec2 v = getBox2d().getBodyPixelCoord(getBody());
            float a = getBody().getAngle();

            g.pushMatrix();
            g.translate(v.x, v.y);
            g.rotate(-a);
            g.fill(50, 255, 50);
            g.rect(0, 0, getWidth(), getHeight());
            g.fill(255, 0, 0);
            g.ellipse(0, 0, 2, 2);
            g.popMatrix();
        }

        @Override
        protected Body makeBody() {
            Body tempBody;

            PolygonShape sd = new PolygonShape();
            sd.setAsBox(getBox2d().scalarPixelsToWorld(getWidth() / 2),
                        getBox2d().scalarPixelsToWorld(getHeight() / 2));

            BodyDef bd = new BodyDef();
            bd.type = BodyType.DYNAMIC;
            bd.position = getBox2d().coordPixelsToWorld(getX(), getY());

            /*
            FixtureDef fixture  = new FixtureDef();
            fixture.shape       = sd;
            fixture.density     = 1;
            fixture.friction    = 0.1;
            fixture.restitution = 0.5;
            */

            tempBody = getBox2d().createBody(bd);
            tempBody.createFixture(sd, 1);
            //tempBody.createFixture(fixture);

            return tempBody;
        }

        @Override
        public void entityCollision(WorldEntity ent) {

        }




        public int getX() {
            return x;
        }

        public int getY() {
            return y;
        }

        public int getWidth() {
            return width;
        }

        public int getHeight() {
            return height;
        }


        private void killOrthogonalVelocity() {
            Vec2 velocity = getBody().getLinearVelocityFromLocalPoint(new Vec2(0, 0));

            Vec2 sideWaysAxis = new Vec2();
            getBody().getTransform().q.getXAxis(sideWaysAxis);
            sideWaysAxis.mul(Vec2.dot(velocity, sideWaysAxis));

            getBody().setLinearVelocity(sideWaysAxis);
        }
    }

    class CarBody extends WorldEntity{

        private int x;
        private int y;
        private int width;
        private int height;

        public CarBody(int x, int y, int width, int height, Box2DProcessing box2d){
            super(box2d);

            this.x = x;
            this.y = y;
            this.width = width;
            this.height = height;
        }

        @Override
        public void paint(PGraphics g) {
            Vec2 v = getBox2d().getBodyPixelCoord(getBody());
            float a = getBody().getAngle();

            g.rectMode(PGraphics.CENTER);

            g.pushMatrix();

            g.translate(v.x, v.y);
            g.rotate(-a);

            g.fill(50, 50, 50);
            g.rect(0, 0, getWidth(), getHeight());

            g.fill(255, 0, 0);
            g.ellipse(0, 0, 4, 4);

            g.popMatrix();
        }

        @Override
        protected Body makeBody() {

            Body tempBody;

            PolygonShape sd = new PolygonShape();
            sd.setAsBox(getBox2d().scalarPixelsToWorld(getWidth() / 2),
                        getBox2d().scalarPixelsToWorld(getHeight() / 2));

            BodyDef bd = new BodyDef();
            bd.type = BodyType.DYNAMIC;
            bd.position = getBox2d().coordPixelsToWorld(getX(), getY());

            /*
            FixtureDef fixture  = new FixtureDef();
            fixture.shape       = sd;
            fixture.density     = 0.3;
            fixture.friction    = 0.1;
            fixture.restitution = 0.5;
            */

            tempBody = getBox2d().createBody(bd);
            tempBody.createFixture(sd, 1);
            //tempBody.createFixture(fixture);

            return tempBody;
        }

        @Override
        public void entityCollision(WorldEntity ent) {

        }


        public int getWidth() {
            return width;
        }

        public int getHeight() {
            return height;
        }

        public int getX() {
            return x;
        }

        public int getY() {
            return y;
        }
    }
}
