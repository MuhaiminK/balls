package Filters;

import Interfaces.PixelFilter;
import core.DImage;

public class Blur implements PixelFilter {

    @Override
    public DImage processImage(DImage img) {
        short[][] grid = img.getBWPixelGrid();
        double sum = 0;
        for (int r = 3; r < grid.length - 3; r++) {
            for (int c = 3; c < grid[r].length - 3; c++) {
                for (int i = -3; i < 3; i++) {
                    for (int j = -3; j < 3; j++) {
                        sum += grid[r + i][c + j];
                    }
                }
                sum /= 49;
                grid[r][c] = (short) sum;
                sum = 0;
            }
        }

        img.setPixels(grid);
        return img;
    }
}
