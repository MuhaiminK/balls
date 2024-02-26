package Filters;

import Interfaces.Drawable;
import Interfaces.Interactive;
import Interfaces.PixelFilter;
import core.DImage;
import processing.core.PApplet;

import java.util.ArrayList;

public class ColorMask implements PixelFilter, Interactive, Drawable {
    private short threshold;
    private ArrayList<colorPoint> colors;
    private short[][] red, blue, green;
    private boolean[][] masked;
    private ArrayList<PixelFilter> filters = new ArrayList<>();
    private ArrayList<Ball> balls = new ArrayList<>();

    public ColorMask() {
        colors = new ArrayList<>();
        threshold = 80;
        PixelFilter blur = new Blur();
        PixelFilter threshold = new Threshold();

        filters.add(blur);
        filters.add(threshold);
    }

    @Override
    public DImage processImage(DImage img) {
        red = img.getRedChannel();
        green = img.getGreenChannel();
        blue = img.getBlueChannel();
        masked = new boolean[red.length][red[0].length];

        maskColors();

        for (int i = 0; i < masked.length; i++) {
            for (int j = 0; j < masked[i].length; j++) {
                if (masked[i][j]) {
                    red[i][j] = 255;
                    blue[i][j] = 255;
                    green[i][j] = 255;
                } else {
                    red[i][j] = 0;
                    blue[i][j] = 0;
                    green[i][j] = 0;
                }
            }
        }

        img.setColorChannels(red, green, blue);
        for (PixelFilter filter : filters) {
            img = filter.processImage(img);
        }
        return img;
    }

    private void maskColors() {
        for (colorPoint color : colors) {
            checkPixels(color);
        }
    }

    private void checkPixels(colorPoint color) {
        for (int i = 0; i < red.length; i++) {
            for (int j = 0; j < red[i].length; j++) {
                if (checkColor(red[i][j], green[i][j], blue[i][j], color)) {
                    masked[i][j] = true;
                }
            }
        }
    }

    private boolean checkPixels(DImage img) {
        short[][] grid = img.getBWPixelGrid();
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[i].length; j++) {
                if (grid[i][j] != 0) {
                    return true;
                }
            }
        }
        return false;
    }

    public void initiateBalls(DImage img) {
        short[][] grid = img.getBWPixelGrid();
        int r, c;
        do {
            r = (int) (Math.random() * grid.length);
            c = (int) (Math.random() * grid[r].length);
        } while (grid[r][c] != 255);
        balls.add(new Ball(r, c));
    }


    private boolean checkColor(short r, short g, short b, colorPoint color) {
        double distR = Math.abs(color.getR() - r) * Math.abs(color.getR() - r);
        double distG = Math.abs(color.getG() - g) * Math.abs(color.getG() - g);
        double distB = Math.abs(color.getB() - b) * Math.abs(color.getB() - b);
        double dist = Math.sqrt(distR + distG + distB);
        if (dist < threshold) {
            return true;
        }
        return false;
    }

    public void addColor(short r, short g, short b) {
        System.out.println(r + ", " + g + ", " + b);
        colors.add(new colorPoint(r, g, b));
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, DImage img) {
        short[][] r1 = img.getRedChannel();
        short[][] g1 = img.getGreenChannel();
        short[][] b1 = img.getBlueChannel();
        addColor(r1[mouseY][mouseX], g1[mouseY][mouseX], b1[mouseY][mouseX]);
    }

    @Override
    public void keyPressed(char key) {
        if (key == '=') {
            threshold += 10;
        } else if (key == '-') {
            threshold -= 10;
        } else if (key == 'r') {
            if (colors.size() >= 1) {
                colors.remove(colors.size() - 1);
            }
        }
    }

    public int findRowCenter(DImage img, Ball ball) {
        short[][] grid = img.getBWPixelGrid();
        int row = ball.getRow();
        imgPoint point2 = new imgPoint(row, ball.getCol());
        while (row < grid.length && grid[row][ball.getCol()] == 255) {
            point2 = new imgPoint(row, ball.getCol());
            row++;
        }
        row = ball.getRow() - 1;
        imgPoint point1 = new imgPoint(row, ball.getCol());
        while (row > 1 && grid[row][ball.getCol()] == 255) {
            point1 = new imgPoint(row, ball.getCol());
            row--;
        }
        int dist1 = point1.getRow() - ball.getRow();
        int dist2 = point2.getRow() - ball.getRow();
        int avg = dist1 + dist2;
        avg /= 2;
        return avg + ball.getRow();
    }

    public int findColCenter(DImage img, Ball ball) {
        short[][] grid = img.getBWPixelGrid();
        int col = ball.getCol();
        imgPoint point2 = new imgPoint(ball.getRow(), col);
        while (col < grid[ball.getRow()].length && grid[ball.getRow()][col] == 255) {
            point2 = new imgPoint(ball.getRow(), col);
            col++;
        }
        col = ball.getCol() - 1;
        imgPoint point1 = new imgPoint(ball.getRow(), col);
        while (col > 0 && grid[ball.getRow()][col] == 255) {
            point2 = new imgPoint(ball.getRow(), col);
            col--;
        }
        int dist1 = point1.getCol() - ball.getCol();
        int dist2 = point2.getCol() - ball.getCol();
        int avg = dist1 + dist2;
        avg /= 2;
        return avg + ball.getCol();
    }

    @Override
    public void drawOverlay(PApplet window, DImage original, DImage filtered) {
        short[][] grid = filtered.getBWPixelGrid();
        if (checkPixels(filtered)) {
            if (balls.size() < 3) {
                for (int i = 0; i < 4; i++) {
                    initiateBalls(filtered);
                }
            }
            window.fill(255, 0, 0);
            if (balls.size() != 0) {
                for (Ball ball : balls) {
                    int row, col;
                    row = findRowCenter(filtered, ball);
                    col = findColCenter(filtered, ball);
                    ball.setRow(row);
                    ball.setCol(col);
                    window.ellipse(col, row, 10, 10);
                }
                balls.remove(balls.get(0));
            }
        }
    }
}

