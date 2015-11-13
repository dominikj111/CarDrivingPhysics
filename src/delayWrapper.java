
public class delayWrapper {

    private long delayUPS;
    private long realUPS;
    private long lastElapsedTime;

    public delayWrapper(int UPS){
        this.delayUPS = Math.round(1000.0 / ((UPS < 1000) ? (UPS < 1) ? 1 : UPS : 1000) );
    }

    public boolean isElapsed(){

        long currentMilliTime = System.currentTimeMillis();
        long currentDelay = currentMilliTime - getLastElapsedTime();

        boolean elapsed = currentDelay > getDelayUPS();

        if(elapsed) {
            setRealUPS(Math.round(1000.0 / currentDelay));
            setLastElapsedTime(currentMilliTime);
        }

        return elapsed;
    }


    public long getRealUPS() {
        return realUPS;
    }


    private long getDelayUPS() {
        return delayUPS;
    }

    private void setRealUPS(long realUPS) {
        this.realUPS = realUPS;
    }


    private long getLastElapsedTime() {
        return lastElapsedTime;
    }

    private void setLastElapsedTime(long lastElapsedTime) {
        this.lastElapsedTime = lastElapsedTime;
    }
}
