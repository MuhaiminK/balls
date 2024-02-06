package Filters;

import Interfaces.PixelFilter;
import core.DImage;

public class SwapRG implements PixelFilter {
    public DImage processImage(DImage img) {
        short[][] red = img.getRedChannel();
        short[][] blue = img.getBlueChannel();
        short[][] green = img.getGreenChannel();

        img.setColorChannels(green,red,blue);
        return img;
    }
}
