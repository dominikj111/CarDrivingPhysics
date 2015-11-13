import processing.core.PGraphics;
import shiffman.box2d.Box2DProcessing;

import java.util.ArrayList;

public abstract class Manager {

    private Box2DProcessing box2d;
    private ArrayList<WorldEntity> worldEntities;
    private ArrayList<WorldEntity> tempWorldEntities;
    private int entityListSize;

    public Manager(Box2DProcessing box2d){
        this.box2d = box2d;
        this.worldEntities = new ArrayList<>();
        this.tempWorldEntities = new ArrayList<>();
    }


    public Box2DProcessing getBox2d(){return this.box2d;}

    public void addWorldEntity(WorldEntity entity){
        this.tempWorldEntities.add(entity);
    }

    public int getNumberOfWorldEntity(){return this.entityListSize;}

    public void paintWorldEntities(PGraphics g){

        getWorldEntities().addAll(setNewTempWorldEntities());

        getWorldEntities().forEach((p) -> p.paint(g));
    }




    private ArrayList<WorldEntity> getWorldEntities(){return this.worldEntities;}

    private ArrayList<WorldEntity> setNewTempWorldEntities(){

        ArrayList<WorldEntity> tempWorldEntities2 = this.tempWorldEntities;

        this.tempWorldEntities = new ArrayList<>();

        setNumberOfWorldEntities(getNumberOfWorldEntity() + tempWorldEntities2.size());

        return tempWorldEntities2;
    }

    private void setNumberOfWorldEntities(int value){this.entityListSize = value;}
}
