package Filters;

import Interfaces.PixelFilter;
import core.DImage;

import javax.swing.*;
import java.util.ArrayList;

public class ColorReduction implements PixelFilter{
    int k;
    public ColorReduction(){
        k = Integer.parseInt(JOptionPane.showInputDialog("Input # of colors"));
    }

    public DImage processImage(DImage img) {
        short[][] red = img.getRedChannel();
        short[][] green = img.getGreenChannel();
        short[][] blue = img.getBlueChannel();
        Cluster[] clusters = new Cluster[k];
        Point[] previousClusters = clusters;
        ArrayList<Point> points = new ArrayList<>();

        clusters = initClusters(clusters);
        points = initPoints(red,green,blue,points);

        do {
            previousClusters = clusters;
            recalculateClusters(points,clusters);
        }while(!stabilityCheck(previousClusters,clusters));

        int counter = 0;
        for (int i = 0; i < red.length; i++) {
            for (int j = 0; j < red[i].length; j++) {
                Cluster c = findCluster(points.get(counter),clusters);
                red[i][j] = c.getR();
                green[i][j] = c.getG();
                blue[i][j] = c.getB();
                counter++;
            }
        }


        img.setColorChannels(red,green,blue);
        return img;
    }

    public void recalculateClusters(ArrayList<Point> points, Cluster[] clusters){
        clearClusters(clusters);
        assignPointsToClusters(points,clusters);
        reCalcCenters(clusters);
    }

    public boolean stabilityCheck(Point[] previousClusters, Cluster[] clusters){
        int numStable = 0;
        for(int i = 0; i < clusters.length; i++){
            if(isEqualColors(previousClusters[i],clusters[i])){
                numStable++;
            }
        }
        return numStable >= clusters.length-1;
    }

    public boolean isEqualColors(Point p, Cluster c){
        return p.getR() == c.getR() && p.getG() == c.getG() && p.getB() == c.getB();
    }

    public ArrayList<Point> initPoints(short[][] red, short[][] green, short[][] blue, ArrayList<Point> points){
        for (int i = 0; i < red.length; i++) {
            for (int j = 0; j < red[i].length; j++) {
                points.add(new Point(red[i][j], green[i][j], blue[i][j]));
            }
        }
        return points;
    }

    public Cluster[] initClusters(Cluster[] clusters){
        for (int i = 0; i < k; i++) {
            clusters[i] = new Cluster((short) (Math.random()*256), (short) (Math.random()*256), (short) (Math.random()*256));
        }
        return clusters;
    }

    public void clearClusters(Cluster[] clusters){
        for (int index = 0; index < clusters.length; index++) {
            clusters[index].clearPoints();
        }
    }
    public void assignPointsToClusters(ArrayList<Point> points, Cluster[] clusters){
        for(Point p : points){
            Cluster c = findCluster(p,clusters);
            c.addPoint(p);
        }
    }

    public Cluster findCluster(Point point, Cluster[] clusters){
        double smallest = 10000;
        Cluster closest = null;
        for(int i = 0; i < clusters.length; i++){
            Cluster c = clusters[i];
            double dist = findDistance(point.getR(),point.getG(),point.getB(),c.getR(),c.getG(),c.getB());
            if(dist < smallest){
                smallest = dist;
                closest = c;
            }
        }
        return closest;
    }

    public double findDistance(short r1, short g1, short b1, short r2, short g2, short b2){
        int r = Math.abs(r1-r2);
        int g = Math.abs(g1-g2);
        int b = Math.abs(b1-b2);
        return Math.sqrt((r*r)+(g*g)+(b*b));
    }

    public void reCalcCenters(Cluster[] clusters){
        for(Cluster c : clusters){
            Point averageP = getAveragePoint(c.getPoints());
            c.changeCenter(averageP.getR(),averageP.getG(),averageP.getB());
        }
    }

    public Point getAveragePoint(ArrayList<Point> points){
        double r = 0, g = 0, b = 0;
        for(Point p : points){
            r += p.getR();
            g += p.getG();
            b += p.getB();
        }
        r /= points.size();
        g /= points.size();
        b /= points.size();
        return new Point((short)r,(short)g,(short)b);
    }
}
