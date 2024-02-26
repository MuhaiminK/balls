package Filters;

import Interfaces.Interactive;
import Interfaces.PixelFilter;
import core.DImage;
import processing.core.PApplet;

import java.util.ArrayList;

public class Pipeline implements PixelFilter, Interactive {
    ArrayList<PixelFilter> filters = new ArrayList<>();
    ColorMask colorMask;

    public Pipeline() {
        PixelFilter colorMask = new ColorMask();
        PixelFilter blur = new Blur();
        PixelFilter threshold = new Threshold();

        filters.add(colorMask);
        filters.add(blur);
        filters.add(threshold);
    }

    @Override
    public DImage processImage(DImage img) {
        for (PixelFilter filter : filters) {
            img = filter.processImage(img);
        }

        return img;
    }

    public void drawOverlay(PApplet Window, DImage original, DImage filtered) {

    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, DImage img) {
        colorMask.mouseClicked(mouseX, mouseY, img);
    }

    @Override
    public void keyPressed(char key) {
        if (key == '-' || key == '=' || key == 'r') {
            colorMask.keyPressed(key);
        }
    }
}
