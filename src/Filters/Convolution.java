package Filters;

import Interfaces.PixelFilter;
import core.DImage;

public class Convolution implements PixelFilter {
    private double[][] boxBlurKernel = {{1, 1, 1}, {1, 1, 1}, {1, 1, 1}};
    private double[][] PrewittEdgeKernel = {{-1, -1, -1}, {-1, 8, -1}, {-1, -1, -1}};
    private double[][] gx = {{-1, 0, 1}, {-2, 0, 2}, {-1, 0, 1}};
    private double[][] gy = {{1, 2, 1}, {0, 0, 0}, {-1, -2, -1}};
    private double kernelWeight = 0;


    public DImage processImage(DImage img) {
        short[][] arr = img.getBWPixelGrid();
        double[][] currKernel = PrewittEdgeKernel;
        //arr = outlining(arr);
        //arr = edgeDetection(arr);
        kernelWeight = getKernelWeight(currKernel);
        arr = convolution(arr,currKernel);

        img.setPixels(arr);
        return img;
    }

    //custom thing i wanted to try. idea was adding the prewitt edge detection onto the normal image to create an outline while keeping the regular image
    public short[][] outlining(short[][] arr){
        short[][] newImage = new short[arr.length][arr[0].length];
        newImage = convolution(arr, PrewittEdgeKernel);
        newImage = threshold(newImage);
        for (int i = 0; i < newImage.length; i++) {
            for (int j = 0; j < newImage[i].length; j++) {
                if(newImage[i][j] != 255){
                    newImage[i][j] = arr[i][j];
                }
            }
        }
        return newImage;
    }


    public short[][] edgeDetection(short[][] arr){
        kernelWeight = getKernelWeight(gx);
        arr = convolution(arr,gx);
        kernelWeight = getKernelWeight(gy);
        arr = convolution(arr,gy);
        arr = threshold(arr);
        return arr;
    }

    public short[][] threshold(short[][] arr){
        for (int i = 0; i < arr.length; i++) {
            for (int j = 0; j < arr[i].length; j++) {
                if(arr[i][j] > 255/2){
                    arr[i][j] = 255;
                }else arr[i][j] = 0;
            }
        }
        return arr;
    }

    public short[][] convolution(short[][] arr, double[][] kernel){
        short[][] newImage = new short[arr.length][arr[0].length];
        for (int r = 1; r < arr.length-1; r++) {
            for (int c = 1; c < arr[r].length-1; c++) {
                newImage[r][c] = (short)getWeightedAvg(r, c, kernel, arr);
            }
        }
        return newImage;
    }

    public double getWeightedAvg(int r, int c, double[][] kernels, short[][] img){
        double avg = 0;
        for (int i = r-1; i <= r+1; i++) {
            for (int j = c-1; j <= c+1; j++) {
                double kernelVal = kernels[i-r+1][j-c+1];
                short imgVal = img[i][j];
                avg += (kernelVal * imgVal);
            }
        }

        if(kernelWeight != 0){
            avg /= kernelWeight;
        }
        if(avg < 0) avg = 0;
        if(avg > 255) avg = 255;
        return avg;
    }

    public double getKernelWeight(double[][] kernels){
        double weight = 0;
        for (int i = 0; i < kernels.length; i++) {
            for (int j = 0; j < kernels[i].length; j++) {
                weight += kernels[i][j];
            }
        }
        return weight;
    }
}
