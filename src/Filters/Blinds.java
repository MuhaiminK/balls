package Filters;
import Interfaces.PixelFilter;
import core.DImage;
import javax.swing.*;

public class Blinds implements PixelFilter {
    @Override
    public DImage processImage(DImage img) {
        short[][] arr = img.getBWPixelGrid();
        int p = Integer.parseInt(JOptionPane.showInputDialog("Input blind width"));
        for (int blinds = 0; blinds < arr.length-p; blinds += 2*p) {
            for (int rows = blinds; rows < blinds + p; rows++) {
                for (int cols = 0; cols < arr[rows].length; cols++) {
                    arr[rows][cols] = 0;
                }
            }
        }
        img.setPixels(arr);
        return img;
    }
}
