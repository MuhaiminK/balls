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
        colorPoint[] previousClusters = clusters;
        ArrayList<colorPoint> colorPoints = new ArrayList<>();

        clusters = initClusters(clusters);
        colorPoints = initPoints(red,green,blue, colorPoints);

        do {
            previousClusters = clusters;
            recalculateClusters(colorPoints,clusters);
        }while(!stabilityCheck(previousClusters,clusters));

        int counter = 0;
        for (int i = 0; i < red.length; i++) {
            for (int j = 0; j < red[i].length; j++) {
                Cluster c = findCluster(colorPoints.get(counter),clusters);
                red[i][j] = c.getR();
                green[i][j] = c.getG();
                blue[i][j] = c.getB();
                counter++;
            }
        }


        img.setColorChannels(red,green,blue);
        return img;
    }

    public void recalculateClusters(ArrayList<colorPoint> colorPoints, Cluster[] clusters){
        clearClusters(clusters);
        assignPointsToClusters(colorPoints,clusters);
        reCalcCenters(clusters);
    }

    public boolean stabilityCheck(colorPoint[] previousClusters, Cluster[] clusters){
        int numStable = 0;
        for(int i = 0; i < clusters.length; i++){
            if(isEqualColors(previousClusters[i],clusters[i])){
                numStable++;
            }
        }
        return numStable >= clusters.length-1;
    }

    public boolean isEqualColors(colorPoint p, Cluster c){
        return p.getR() == c.getR() && p.getG() == c.getG() && p.getB() == c.getB();
    }

    public ArrayList<colorPoint> initPoints(short[][] red, short[][] green, short[][] blue, ArrayList<colorPoint> colorPoints){
        for (int i = 0; i < red.length; i++) {
            for (int j = 0; j < red[i].length; j++) {
                colorPoints.add(new colorPoint(red[i][j], green[i][j], blue[i][j]));
            }
        }
        return colorPoints;
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
    public void assignPointsToClusters(ArrayList<colorPoint> colorPoints, Cluster[] clusters){
        for(colorPoint p : colorPoints){
            Cluster c = findCluster(p,clusters);
            c.addPoint(p);
        }
    }

    public Cluster findCluster(colorPoint colorPoint, Cluster[] clusters){
        double smallest = 10000;
        Cluster closest = null;
        for(int i = 0; i < clusters.length; i++){
            Cluster c = clusters[i];
            double dist = findDistance(colorPoint.getR(), colorPoint.getG(), colorPoint.getB(),c.getR(),c.getG(),c.getB());
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
            colorPoint averageP = getAveragePoint(c.getPoints());
            c.changeCenter(averageP.getR(),averageP.getG(),averageP.getB());
        }
    }

    public colorPoint getAveragePoint(ArrayList<colorPoint> colorPoints){
        double r = 0, g = 0, b = 0;
        for(colorPoint p : colorPoints){
            r += p.getR();
            g += p.getG();
            b += p.getB();
        }
        r /= colorPoints.size();
        g /= colorPoints.size();
        b /= colorPoints.size();
        return new colorPoint((short)r,(short)g,(short)b);
    }
}
