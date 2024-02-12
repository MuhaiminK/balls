package Filters;

import java.util.ArrayList;

public class Cluster extends colorPoint {
    private ArrayList<colorPoint> colorPoints;

    public Cluster(short r, short g, short b){
        super(r, g, b);
    }

    public void clearPoints(){
        colorPoints = new ArrayList<>();
    }

    public void addPoint(colorPoint colorPoint){
        colorPoints.add(colorPoint);
    }

    public void changeCenter(short r, short g, short b){
        this.r = r;
        this.g = g;
        this.b = b;
    }

    public ArrayList<colorPoint> getPoints() {
        return colorPoints;
    }
}
