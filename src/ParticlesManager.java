import shiffman.box2d.Box2DProcessing;

import java.util.ArrayList;
import java.util.Random;

public class ParticlesManager extends Manager{

    private Random rnd = new Random();

    private int maxNumberOfParticles;
    private int particlesBaseIndex;
    private ArrayList<ParticleBase> particlesBases;


    public ParticlesManager(int maxNumberOfParticles, Box2DProcessing box2d){
        super(box2d);

        this.maxNumberOfParticles = maxNumberOfParticles;

        this.particlesBases = new ArrayList<>();
        this.setParticlesBaseIndex(0);
    }

    public void addParticleBase(int xLoc, int yLoc, int sectorDirection){
        getParticlesBasesList().add(new ParticleBase(xLoc, yLoc, sectorDirection));
    }

    public void makeRandomParticle(int minRadius, int maxRadius) {
        makeParticle(rnd.nextInt(maxRadius - minRadius + 1) + minRadius);
    }

    public void makeParticle(int radius){

        if(getMaxNumberOfParticles() <= getNumberOfWorldEntity()){return;}

        ParticleBase pb = getParticlesBasesList().get(getParticlesBaseIndex());

        Particle particle = new Particle(radius, pb, getBox2d());
        particle.initEntity();

        addWorldEntity(particle);
        setParticlesBaseIndex((getParticlesBaseIndex() + 1 < getParticlesBasesList().size()) ? getParticlesBaseIndex() + 1 : 0);
    }



    public int getMaxNumberOfParticles() {
        return this.maxNumberOfParticles;
    }

    public ArrayList<ParticleBase> getParticlesBasesList() {
        return this.particlesBases;
    }

    private int getParticlesBaseIndex() {
        return this.particlesBaseIndex;
    }

    private void setParticlesBaseIndex(int newParticlesBaseIndex) {
        this.particlesBaseIndex = newParticlesBaseIndex;
    }
}
