package Filters;

public class Point {
    protected short r, b, g;
    public Point(short r, short g, short b){
        this.r = r;
        this.g = g;
        this.b = b;
    }

    public short getR() {
        return r;
    }
    public short getG() {
        return g;
    }
    public short getB() {
        return b;
    }

    public void setR(short r) {
        this.r = r;
    }
    public void setG(short g) {
        this.g = g;
    }
    public void setB(short b) {
        this.b = b;
    }
}
