package engine;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

/**
 * Work in progress code to render polygons with image textures
 * @author Vadim
 *
 */
public class TempImage {
	
	static double tempAngle = 0;
	
	private static BufferedImage img = null;
	
	static {
		try {
			img = ImageIO.read(new File("./res/Sprites.png"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public TempImage() {
		
	}

	
	public void render(Graphics g) {
		if (img == null) {
			System.out.println("Image did not load");
			System.exit(0);
		}
		
		//skew image
		
		AffineTransform trans = AffineTransform.getRotateInstance(tempAngle, img.getWidth(), img.getHeight());
		
		
		
		
				
		((Graphics2D) g).drawImage(img, trans, null);
		//((Graphics2D) g).rotate(0, tempAngle, 0);
		((Graphics2D) g).fill3DRect(100, 100, 100, 100, false);
		incr();
		
		//g.drawImage(img, 0,	0, 100, 100, null);
		
		
		
		
	}
	
	public void incr() {
		tempAngle = (tempAngle + .01) % 360;
		System.out.println("Angle: " + tempAngle);
		
	}
}
