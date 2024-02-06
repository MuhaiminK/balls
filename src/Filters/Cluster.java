package Filters;

import java.util.ArrayList;

public class Cluster extends Point {
    private ArrayList<Point> points;

    public Cluster(short r, short g, short b){
        super(r, g, b);
    }

    public void clearPoints(){
        points = new ArrayList<>();
    }

    public void addPoint(Point point){
        points.add(point);
    }

    public void changeCenter(short r, short g, short b){
        this.r = r;
        this.g = g;
        this.b = b;
    }

    public ArrayList<Point> getPoints() {
        return points;
    }
}
