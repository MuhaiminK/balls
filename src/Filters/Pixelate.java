package Filters;

import Interfaces.PixelFilter;
import core.DImage;

import javax.swing.*;

public class Pixelate implements PixelFilter {
    public DImage processImage(DImage img) {
        short[][] arr = img.getBWPixelGrid();
        int n1 = Integer.parseInt(JOptionPane.showInputDialog("Input pixelation radius"));
        for (int i = n1; i < arr.length-n1; i += n1*2){
            for (int j = n1; j < arr[i].length-n1; j += n1*2) {
                arr = loopThroughSquares(arr,n1,i,j);
            }
        }
        img.setPixels(arr);
        return img;
    }
    public short[][] loopThroughSquares(short[][] arr, int n1, int row1, int col1){
        for (int i = row1; i < (n1*2)+1; i++) {
            for (int j = col1; j < (n1*2)+1; j++) {
                arr[i][j] = arr[(n1-1)/2][(n1-1)/2];
            }
        }
        return arr;
    }
}
