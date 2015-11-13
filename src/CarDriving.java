import processing.core.PApplet;
import resources.ResourcesClass;
import shiffman.box2d.Box2DProcessing;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;

public class CarDriving extends PApplet {

    static int[][] boundariesMap = new int[][]{
          //{  x,   y, width, height}
            { 15, 270,    30,    180},
            {300, 345,   540,     30},
            {585, 270,    30,    180},
            {300,  30,    28,     60},
            {  3,  90,     6,    180},
            {597,  90,     6,    180},
            {146,   3,   280,      6},
            {454,   3,   280,      6}
    };


    private Box2DProcessing box2d;

    private BoundariesManager boundariesManager;
    private ParticlesManager particleManager;
    private Car car;

    private boolean[] currentControlInputImage;
    private delayWrapper FPS, UPS;

    private long recordedMilliTime;
    private long currentMilliTime;
    private long currentMilliDelay;

    @Override
    public void setup(){
        setApplicationIcon(ResourcesClass.class.getResourceAsStream("appIco.png"));
        size(600,360);

        FPS = new delayWrapper(1000);
        UPS = new delayWrapper(1000);

        box2d = new Box2DProcessing(this);
        box2d.createWorld();
        box2d.setGravity(0, 0);
        box2d.listenForCollisions();
        box2d.world.setContactListener(new WorldEntityCollisionListener());

        boundariesManager = new BoundariesManager(box2d);
        boundariesManager.addMappedBoundaries(boundariesMap, color(30, 200, 20), color(0));

        particleManager = new ParticlesManager(250, box2d);
        particleManager.addParticleBase( 10, 10, 4);
        particleManager.addParticleBase(590, 10, 3);
        particleManager.addParticleBase(590,170, 2);
        particleManager.addParticleBase( 10,170, 1);

        car = new Car(width/2, height/2, box2d);

        currentControlInputImage = new boolean[MonitoredKeys.getCountOfMonitoredKeys()];
    }

    @Override
    public void draw(){

        currentMilliTime = System.currentTimeMillis();
        currentMilliDelay = currentMilliTime - recordedMilliTime;
        recordedMilliTime = currentMilliTime;


        if(UPS.isElapsed()){ updateGameState(currentControlInputImage); }
        //updateGameState(currentControlInputImage);
        if(FPS.isElapsed()){ drawView(); }
        //drawView();


        drawUPS(44,  26, "UPS: ", String.valueOf(UPS.getRealUPS()));
        drawUPS(44,  72, "FPS: ", String.valueOf(FPS.getRealUPS()));
        drawUPS(44, 118, "DRW: ", String.valueOf(Math.round(1000.0/currentMilliDelay)));
    }


    @Override
    public void keyPressed(){

        if(MonitoredKeys.isMonitored(keyCode)){
            currentControlInputImage[MonitoredKeys.getIndex(keyCode)] = true;
        }

    }

    @Override
    public void keyReleased(){

        if(MonitoredKeys.isMonitored(keyCode)){
            currentControlInputImage[MonitoredKeys.getIndex(keyCode)] = false;
        }

    }



    private void updateGameState(boolean[] inputControlImage){
        box2d.step();
        car.update(inputControlImage);
        particleManager.makeRandomParticle(2, 6);
    }

    private void drawView(){

        background(150, 150, 150);

        car.paintWorldEntities(g);
        particleManager.paintWorldEntities(g);
        boundariesManager.paintWorldEntities(g);
    }

    private void drawUPS(int x, int y, String note, String upsString){

        int w = 75, h = 40;

        fill(color(90, 90, 90));
        rect(x, y, w, h);

        fill(255, 200, 0);
        text(note + upsString, x - 20, y + 3);
    }

    private void setApplicationIcon(InputStream input) {

        BufferedImage image;

        try{
            image = ImageIO.read(input);
        } catch (IOException exception){
            System.out.println("Can't load image from resources.");
            return;
        }

        frame.setIconImage(image);
    }


    // +++++++++++++++++++++++++++++++++++++++
    // +    Statics members                  +
    // +++++++++++++++++++++++++++++++++++++++

    public static void main(String[] passedArgs) {
        PApplet.main("CarDriving");
    }
}
