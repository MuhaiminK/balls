package Filters;
import Interfaces.PixelFilter;
import core.DImage;
import javax.swing.*;

public class LightenBW implements PixelFilter{
    @Override
    public DImage processImage(DImage img) {
        short[][] arr = img.getBWPixelGrid();
        double p = Integer.parseInt(JOptionPane.showInputDialog("Input a percentage (without %)"));
        for (int i = 0; i < arr.length; i++) {
            for (int j = 0; j < arr[i].length; j++) {
                arr[i][j] = (short)(arr[i][j] + (255 - arr[i][j])*(p/100.0));
            }
        }
        img.setPixels(arr);
        return img;
    }
}
