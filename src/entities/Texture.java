package entities;

import java.awt.Color;
import java.awt.image.BufferedImage;

public class Texture {

    private BufferedImage image;

    public Texture(BufferedImage theImage) {
        this.image = theImage;
    }

    public Color getColor(double x, double y) {
        // System.out.println("Tex X:Y" + x + ", " + y);
        int theX = (int) Math.floorMod((int) (image.getWidth() * x), image.getWidth());
        int theY = (int) Math.floorMod((int) (image.getHeight() * y), image.getHeight());

        return new Color(image.getRGB(theX, theY));
    }

    public int getWidth() {
        return image.getWidth();
    }

    public int getHeight() {
        return image.getHeight();
    }

    public BufferedImage getImage() {
        return image;
    }

    public BufferedImage resizeImage(BufferedImage src, int width, int height) {
        // check that width and height are > 0 and not too large

        BufferedImage localImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

        int x, y;
        int ww = src.getWidth();
        int hh = src.getHeight();
        int[] ys = new int[height];

        for (y = 0; y < height; y++) {
            ys[y] = y * hh / height;
        }

        for (x = 0; x < width; x++) {
            int newX = x * ww / width;
            for (y = 0; y < height; y++) {
                int col = src.getRGB(newX, ys[y]);
                localImage.setRGB(x, y, col);
            }
        }

        return localImage;
    }

}
