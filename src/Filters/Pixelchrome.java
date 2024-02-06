package Filters;

import Interfaces.PixelFilter;
import core.DImage;

import javax.swing.*;

public class Pixelchrome implements PixelFilter {
    @Override
    public DImage processImage(DImage img) {
        short[][] arr = img.getBWPixelGrid();
        //int n1 = 255 / Integer.parseInt(JOptionPane.showInputDialog("Input the number of shades"));
        int n2 = Integer.parseInt(JOptionPane.showInputDialog("Enter pixelation value"));
        arr = pixelate(n2,arr);
        //arr = polychrome(n1, arr);
        img.setPixels(arr);
        return img;
    }

    public short[][] pixelate(int r, short[][] arr){
        int p = 2*r+1;
        for (int i = 0; i < arr.length-p; i += p) {
            for (int j = 0; j < arr[i].length-p; j += p) {
                arr = loopThroughSquare(findAverage(p,i,j,arr), p,i,j,arr);
            }
        }
        return arr;
    }

    public short[][] loopThroughSquare(int avg, int p, int row1, int col1, short[][] arr){
        for (int i = row1; i < p; i++) {
            for (int j = col1; j < p; j++) {
                arr[i][j] = (short)avg;
            }
        }
        return arr;
    }
    public short findAverage(int p, int row1, int col1, short[][] arr){
        short avg = 0;
        for (int i = row1; i < p; i++) {
            for (int j = col1; j < p; j++) {
                avg += arr[i][j];
            }
        }
        return (short)(avg/p*p);
    }

    public short[][] polychrome(int n1, short[][] arr){
        for (int i = 0; i < arr.length; i++) {
            for (int j = 0; j < arr[i].length; j++) {
                for (int n = n1; n <= 255; n += n1) {
                    if (arr[i][j] <= n && arr[i][j] > n - n1) {
                        arr[i][j] = (short) ((n + n1) / 2);
                    } else if (arr[i][j] > n && n + n1 >= 254) {
                        arr[i][j] = (short) ((n + n1) / 2);
                    }
                }
            }
        }
        return arr;
    }
}
