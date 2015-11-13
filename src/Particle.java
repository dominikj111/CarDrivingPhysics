import org.jbox2d.collision.shapes.CircleShape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.BodyType;
import org.jbox2d.dynamics.FixtureDef;
import processing.core.PGraphics;
import shiffman.box2d.Box2DProcessing;

import java.util.Random;

public class Particle extends WorldEntity {

    private static Random rnd = new Random();


    private float radius;
    private ParticleBase base;
    private int mainColor, currentColor;



    public Particle(float radius, ParticleBase base, Box2DProcessing box2d) {
        super(box2d);

        this.base = base;
        this.radius = radius;
        this.mainColor = getProcessingColor(0, 0, 0, 255);
        this.currentColor = getProcessingColor(0, 0, 0, 255);
    }

    @Override
    public void paint(PGraphics g) {

        Vec2 pos = getBox2d().getBodyPixelCoord(getBody());
        float a = getBody().getAngle();

        g.pushMatrix();

        g.translate(pos.x, pos.y);
        g.rotate(-a);

        g.fill(getCurrentColor());
        g.stroke(0);
        g.strokeWeight(0);
        g.ellipse(0, 0, 2 * radius, 2 * radius);

        g.line(0, 0, radius, 0);

        g.popMatrix();
    }

    @Override
    protected Body makeBody() {

        Body particleBody;

        BodyDef bd = new BodyDef();
        bd.position = getBox2d().coordPixelsToWorld(getBase().getX(), getBase().getY());
        bd.type = BodyType.DYNAMIC;
        bd.linearDamping = 0.5f;
        bd.angularDamping = 0.9f;
        bd.fixedRotation = true;

        particleBody = getBox2d().world.createBody(bd);

        CircleShape cs = new CircleShape();
        cs.m_radius = getBox2d().scalarPixelsToWorld(getRadius());

        FixtureDef fd = new FixtureDef();
        fd.shape = cs;
        fd.density = 1;
        fd.friction = 0.5f;
        fd.restitution = 0.3f;

        particleBody.createFixture(fd);

        particleBody.setLinearVelocity(getBase().getLinearRandomVelocity(5, 15));
        particleBody.setAngularVelocity(10);

        return particleBody;
    }

    @Override
    public void entityCollision(WorldEntity ent) {

        if(ent instanceof Particle){
            Particle p = (Particle)ent;
            if(p.getCurrentColor() != this.getCurrentColor()){
                this.setCurrentColor(p.getCurrentColor());
            }
        }

        if(ent instanceof Car.Wheel || ent instanceof Car.CarBody){
            this.changeToRandomColor();
        }


    }



    private void changeToRandomColor(){
        setCurrentColor(getProcessingColor(rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256), 255));
    }

    private void changeToDefaultColor(){setCurrentColor(getDefaultColor());}




    public float getRadius() {
        return radius;
    }

    private ParticleBase getBase(){return this.base;}

    private int getCurrentColor(){return currentColor;}

    private int getDefaultColor(){return mainColor;}

    private void setCurrentColor(int value){ currentColor = value;}

}
