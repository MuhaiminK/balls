package Filters;

import Interfaces.Interactive;
import Interfaces.PixelFilter;
import core.DImage;

import java.util.ArrayList;

public class ColorMask implements PixelFilter, Interactive {
    private short threshold;
    private ArrayList<colorPoint> colors;
    private short[][] red, blue, green;
    private boolean[][] masked;
    private ArrayList<PixelFilter> filters = new ArrayList<>();
    private ArrayList<Ball> balls = new ArrayList<>();

    public ColorMask(){
        colors = new ArrayList<>();
        threshold = 80;
        PixelFilter blur = new Blur();
        PixelFilter threshold = new Threshold();

        filters.add(blur);
        filters.add(threshold);
    }

    @Override
    public DImage processImage(DImage img) {
        red = img.getRedChannel();
        green = img.getGreenChannel();
        blue = img.getBlueChannel();
        masked = new boolean[red.length][red[0].length];

        maskColors();

        for (int i = 0; i < masked.length; i++) {
            for (int j = 0; j < masked[i].length; j++) {
                if(masked[i][j]){
                    red[i][j] = 255;
                    blue[i][j] = 255;
                    green[i][j] = 255;
                }else{
                    red[i][j] = 0;
                    blue[i][j] = 0;
                    green[i][j] = 0;
                }
            }
        }

        img.setColorChannels(red, green, blue);
        for(PixelFilter filter : filters){
            img = filter.processImage(img);
        }
        return img;
    }

    private void maskColors(){
        for (Point color : colors){
            loopThroughPixels(color);
        }
    }

    private void loopThroughPixels(Point color){
        for (int i = 0; i < red.length; i++) {
            for (int j = 0; j < red[i].length; j++) {
                if(checkColor(red[i][j],green[i][j],blue[i][j], color)){
                    masked[i][j] = true;
                }
            }
        }
    }


    private boolean checkColor(short r, short g, short b, Point color){
        double distR = Math.abs(color.getR()-r)*Math.abs(color.getR()-r);
        double distG = Math.abs(color.getG()-g)*Math.abs(color.getG()-g);
        double distB = Math.abs(color.getB()-b)*Math.abs(color.getB()-b);
        double dist = Math.sqrt(distR+distG+distB);
        if(dist < threshold){
            return true;
        }
        return false;
    }

    public void addColor(short r, short g, short b){
        System.out.println(r + ", " + g + ", " + b);
        colors.add(new Point(r,g,b));
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, DImage img) {
        short[][] r1 = img.getRedChannel();
        short[][] g1 = img.getGreenChannel();
        short[][] b1 = img.getBlueChannel();
        addColor(r1[mouseY][mouseX],g1[mouseY][mouseX], b1[mouseY][mouseX]);
    }

    @Override
    public void keyPressed(char key){
        if(key == '='){
            threshold += 10;
        }else if(key == '-'){
            threshold -= 10;
        }else if(key == 'r'){
            if(colors.size() >= 1){
                colors.remove(colors.size()-1);
            }
        }
    }
}

