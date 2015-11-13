import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.BodyType;
import processing.core.PGraphics;
import shiffman.box2d.Box2DProcessing;

public class Boundary extends WorldEntity{

    private int fillColor;
    private int strokeColor;
    private int x;
    private int y;
    private int width;
    private int height;



    public Boundary(int x, int y, int width, int height, Box2DProcessing box2d, int fillColor, int strokeColor){
        super(box2d);

        this.fillColor = fillColor;
        this.strokeColor = strokeColor;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    @Override
    public void paint(PGraphics g){

        g.strokeWeight(1);

        g.rectMode(PGraphics.CENTER);

        //Vec2  v  = box2d.getBodyPixelCoord(bodies[bounds[i][body_i]]);
        //float a  = bodies[bounds[i][body_i]].getAngle();

        g.pushMatrix();
        //translate(v.x,v.y);
        //rotate(-a);
        g.translate(getX(), getY());

        g.fill(getFillColor());
        g.stroke(getStrokeColor());
        g.rect(0, 0, getWidth(), getHeight());

        g.fill(255, 0, 0);
        g.ellipse(0, 0, 4, 4);

        g.popMatrix();
        
    }

    @Override
    protected Body makeBody() {

        Body returnBody;

        PolygonShape sd = new PolygonShape();
        sd.setAsBox(getBox2d().scalarPixelsToWorld(getWidth() / 2),
                    getBox2d().scalarPixelsToWorld(getHeight()/2));

        BodyDef bd = new BodyDef();
        bd.type = BodyType.STATIC;
        bd.position = getBox2d().coordPixelsToWorld(getX(), getY());

        /*FixtureDef fixture  = new FixtureDef();
        fixture.shape       = sd;
        fixture.density     = 1;
        fixture.friction    = 0.5;
        fixture.restitution = 0.5;*/

        returnBody = getBox2d().createBody(bd);
        returnBody.createFixture(sd, 1);
        //returnBody.createFixture(fixture);

        return returnBody;
    }

    @Override
    public void entityCollision(WorldEntity ent) {

    }



    public int getFillColor() {
        return this.fillColor;
    }

    public int getStrokeColor() {
        return this.strokeColor;
    }

    public int getX() {
        return this.x;
    }

    public int getY() {
        return this.y;
    }

    public int getWidth() {
        return this.width;
    }

    public int getHeight() {
        return this.height;
    }
}