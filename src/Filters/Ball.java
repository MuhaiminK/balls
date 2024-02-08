package Filters;

import java.util.ArrayList;

public class Ball{
    private ArrayList<Short> points;
    private short row, col;
    public Ball(short row, short col){
        points = new ArrayList<>();
        this.row = row;
        this.col = col;
    }

}
