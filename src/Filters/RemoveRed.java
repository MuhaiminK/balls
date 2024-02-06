package Filters;

import Interfaces.PixelFilter;
import core.DImage;

public class RemoveRed implements PixelFilter {
    public DImage processImage(DImage img) {
        short[][] red = img.getRedChannel();
        short[][] blue = img.getBlueChannel();
        short[][] green = img.getGreenChannel();

        for (int i = 0; i < red.length; i++) {
            for (int j = 0; j < red[i].length; j++) {
                red[i][j] = 0;
            }
        }
        img.setColorChannels(red,green,blue);
        return img;
    }
}
