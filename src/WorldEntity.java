import org.jbox2d.dynamics.Body;
import processing.core.PGraphics;
import shiffman.box2d.Box2DProcessing;

import java.util.HashMap;

public abstract class WorldEntity {

    private Box2DProcessing box2d;
    private Body entityBody;



    public WorldEntity(Box2DProcessing box2d){
        this.box2d = box2d;
    }

    public void initEntity(){
        this.entityBody = makeBody();
        entities.put(getBody().hashCode(), this);
    }

    public void destroyEntity(){
        getBox2d().destroyBody(getBody());
    }

    protected Box2DProcessing getBox2d(){return this.box2d;}

    protected Body getBody(){return this.entityBody;}

    protected int getProcessingColor(int r, int g, int b, int a){
        r = ( r < 255 ) ? ( r > 0 ) ? r : 0 : 255;
        g = ( g < 255 ) ? ( g > 0 ) ? g : 0 : 255;
        b = ( b < 255 ) ? ( b > 0 ) ? b : 0 : 255;
        a = ( a < 255 ) ? ( a > 0 ) ? a : 0 : 255;

        return a << 24 | r << 16 | g << 8 | b;
    }

    public abstract void paint(PGraphics g);
    protected abstract Body makeBody();
    public abstract void entityCollision(WorldEntity ent);



    // +++++++++++++++++++++++++++++++++++++++
    // +    Statics members                  +
    // +++++++++++++++++++++++++++++++++++++++

    public static void collidedEntities(int bodyHashA, int bodyHashB){
        getCollidedObject(bodyHashA).entityCollision(getCollidedObject(bodyHashB));
        getCollidedObject(bodyHashB).entityCollision(getCollidedObject(bodyHashA));
    }

    private static HashMap<Integer, WorldEntity> entities = new HashMap<>();
    private static WorldEntity getCollidedObject(int bodyHash){return entities.get(bodyHash);}
}
