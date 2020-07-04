package entities;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class Texture {
	
	private BufferedImage image;
	
	public Texture() {
		try {
			image = ImageIO.read(new File("./res/Sprites.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
	public Color getColor(double x, double y) {	
		int theX = (int) Math.floorMod((int) (image.getWidth()*x), image.getWidth());
		int theY = (int) Math.floorMod((int) (image.getHeight()*y), image.getHeight());
		
		
		return new Color( image.getRGB(theX, theY));
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

}
