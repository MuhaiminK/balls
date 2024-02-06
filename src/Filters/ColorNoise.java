package Filters;

import Interfaces.PixelFilter;
import core.DImage;

import javax.swing.*;

public class ColorNoise implements PixelFilter {
    private double n;

    public ColorNoise(){
        n = Double.parseDouble(JOptionPane.showInputDialog("Enter noise probability"));
    }
    @Override
    public DImage processImage(DImage img) {
        short[][] red = img.getRedChannel();
        short[][] blue = img.getBlueChannel();
        short[][] green = img.getGreenChannel();

        for (int i = 0; i < red.length; i++) {
            for (int j = 0; j < red[0].length; j++) {
                if(Math.random() < n){
                    red[i][j] = (short)(Math.random()*256);
                    blue[i][j] = (short)(Math.random()*256);
                    green[i][j] = (short)(Math.random()*256);
                }
            }
        }
        img.setColorChannels(red,green,blue);
        return img;
    }
}
