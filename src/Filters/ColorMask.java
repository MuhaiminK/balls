package Filters;

import Interfaces.Interactive;
import Interfaces.PixelFilter;
import core.DImage;

import javax.swing.*;
import java.util.ArrayList;

public class ColorMask implements PixelFilter, Interactive {
    private short threshold;
    private ArrayList<Point> colors;
    private short[][] red;
    private short[][] green;
    private short[][] blue;

    public ColorMask(){
        colors = new ArrayList<>();
        threshold = 80;
    }

    @Override
    public DImage processImage(DImage img) {
        red = img.getRedChannel();
        green = img.getGreenChannel();
        blue = img.getBlueChannel();

        findColor();

        img.setColorChannels(red, green, blue);
        return img;
    }

    private void findColor(){
        for (Point color : colors){
            loopThroughPixels(color);
        }
    }

    private void loopThroughPixels(Point color){
        for (int i = 0; i < red.length; i++) {
            for (int j = 0; j < red[i].length; j++) {
                if(checkColor(red[i][j],green[i][j],blue[i][j], color)){
                    red[i][j] = 255;
                    green[i][j] = 255;
                    blue[i][j] = 255;
                }else{
                    red[i][j] = 0;
                    green[i][j] = 0;
                    blue[i][j] = 0;
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
        colors.add(new Point(r,g,b));
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, DImage img) {
        addColor(red[mouseX][mouseY],green[mouseX][mouseY], blue[mouseX][mouseY]);
    }

    @Override
    public void keyPressed(char key) {
        if(key == '=' || key == '+'){
            threshold += 10;
        }else if(key == '-' || key == '_'){
            threshold -= 10;
        }else if(key == 'r' || key == 'R'){
            if(colors.size() >= 1){
                colors.remove(colors.size()-1);
            }
        }
    }
}

