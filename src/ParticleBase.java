import org.jbox2d.common.Vec2;

import java.util.Random;

public class ParticleBase {

    private Random rnd = new Random();

    private int x;
    private int y;
    private int sector;


    public ParticleBase(int x, int y, int sector){
        this.x = x;
        this.y = y;
        this.sector = sector;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getSector() {
        return sector;
    }

    public Vec2 getSectorDirection(){
        switch(this.getSector()){
            case 1: return new Vec2( 1, 1);
            case 2: return new Vec2(-1, 1);
            case 3: return new Vec2(-1,-1);
            case 4: return new Vec2( 1,-1);
        }

        return null;
    }

    public Vec2 getLinearRandomVelocity(int min, int max){

        Vec2 sectorDirection = getSectorDirection();

        int a = rnd.nextInt(max - min + 1) + min;
        int b = rnd.nextInt(max - min + 1) + min;

        return new Vec2(sectorDirection.x * a, sectorDirection.y * b);
    }
}
