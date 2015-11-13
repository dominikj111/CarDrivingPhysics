import shiffman.box2d.Box2DProcessing;

public class BoundariesManager extends Manager {

    public enum boundaryMapMarkers{
        xCord(0), yCord(1), width(2), height(3);

        private int id;
        boundaryMapMarkers(int id){this.id = id;}
        public int getId(){return this.id;}
    }


    public BoundariesManager(Box2DProcessing box2d) {
        super(box2d);
    }

    public void addBoundary(int x, int y, int w, int h, int fillColor, int strokeColor){

        Boundary b = new Boundary(x, y, w, h, getBox2d(), fillColor, strokeColor);
        b.initEntity();

        addWorldEntity(b);
    }

    public void addMappedBoundaries(int[][] bounds, int fillColor, int strokeColor){

        for (int[] bound : bounds) {
            addBoundary(bound[boundaryMapMarkers.xCord.getId()],
                    bound[boundaryMapMarkers.yCord.getId()],
                    bound[boundaryMapMarkers.width.getId()],
                    bound[boundaryMapMarkers.height.getId()], fillColor, strokeColor);
        }
    }
}
