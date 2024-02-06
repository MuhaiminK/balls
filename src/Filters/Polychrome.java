package Filters;

import Interfaces.PixelFilter;
import core.DImage;

import javax.swing.*;

public class Polychrome implements PixelFilter {
    private int n1;
    public Polychrome(){
        n1 = 255/Integer.parseInt(JOptionPane.showInputDialog("Input the number of shades"));
    }
    @Override
    public DImage processImage(DImage img) {
        short[][] arr = img.getBWPixelGrid();
        for (int i = 0; i < arr.length; i++) {
            for (int j = 0; j < arr[i].length; j++) {
                for(int n = n1; n <= 255; n+=n1){
                    if(arr[i][j] <= n && arr[i][j] > n-n1){
                        arr[i][j] = (short)((n+n1)/2);
                    }else if(arr[i][j] > n && n+n1 >=254){
                        arr[i][j] = (short)((n+n1)/2);
                    }
                }
            }
        }
        img.setPixels(arr);
        return img;
    }
}
