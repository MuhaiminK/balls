package Filters;

import Interfaces.PixelFilter;
import core.DImage;
import processing.core.PApplet;

import java.util.ArrayList;

public class Pipeline implements PixelFilter {
    ArrayList<PixelFilter> filters = new ArrayList<>();
    public Pipeline(){
        PixelFilter colorMask = new ColorMask();
        PixelFilter ColorReduction = new ColorReduction();

        filters.add(colorMask);
        filters.add(ColorReduction);
    }

    @Override
    public DImage processImage(DImage img){
        for (PixelFilter filter : filters){
            img = filter.processImage(img);
        }

        return img;
    }

    public void drawOverlay(PApplet Window, DImage original, DImage filtered){

    }
}
