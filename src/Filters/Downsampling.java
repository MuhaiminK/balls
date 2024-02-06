package Filters;
import Interfaces.PixelFilter;
import core.DImage;

public class Downsampling implements PixelFilter {
    @Override
    public DImage processImage(DImage img) {
        short[][] og =img.getBWPixelGrid();
        short[][] arr = new short[img.getHeight()/2][img.getWidth()/2];
        for (int i = 0; i < arr.length; i++) {
            for (int j = 0; j < arr[i].length; j++){
                arr[i][j] = og[i*2][j*2];
            }
        }

        img.setPixels(arr);
        return img;
    }
}
