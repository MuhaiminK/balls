package Filters;

import java.util.ArrayList;

public class Ball{
    private ArrayList<imgPoint> points;
    private int row, col;
    public Ball(int row, int col){
        points = new ArrayList<>();
        this.row = row;
        this.col = col;
    }
    public void addPoint(imgPoint point){
        points.add(point);
    }

    public int getRow() {
        return row;
    }

    public void setRow(int row) {
        this.row = row;
    }

    public int getCol() {
        return col;
    }

    public void setCol(int col) {
        this.col = col;
    }
    public void clearPoints(){
        points = new ArrayList<>();
    }
    public ArrayList<imgPoint> getPoints(){
        return points;
    }
}
