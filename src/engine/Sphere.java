package engine;

import java.awt.Color;
import java.awt.Graphics;


/** 
 * Could be done with rendering a circle/polygon with n sides
 * that always faces the camera and has a light gradient to 
 * simulate curves
 * 
 * 
 * @author user
 *
 */
public class Sphere {
	
	private Coordinate coord;
	private Color color;
	private double radius;
	
	public Sphere(Color color, Coordinate coord, double radius) {
		this.coord = coord;
		this.color = color;
		this.radius = radius;
	}
	
	public void tick() {
		
	}
	
	public void render(Graphics g, Camera c) {
		Coordinate a = Calculator.rotateAroundCamera(coord, c);
		if (a.getZ() < 0) {
			return;
		}
		g.setColor(color);
		g.fillOval((int)(a.getX() - radius/2), (int)(a.getY() - radius/2), (int)radius, (int)radius);
		g.setColor(Color.BLACK);
		g.drawOval((int)(a.getX() - radius/2), (int)(a.getY() - radius/2), (int)radius, (int)radius);
	}

}

/*
 Graphics2D g2 = (Graphics2D) g;
    int x = 50;
    int y = 75;
    int width = 200;
    int height = 100;
    Shape r1 = new Ellipse2D.Float(x, y, width, height);
    for (int angle = 0; angle <= 360; angle += 45) {
      g2.rotate(Math.toRadians(angle), x + width / 2, y + height / 2);
      g2.setPaint(Color.YELLOW);
      g2.fill(r1);
      g2.setStroke(new BasicStroke(4));
      g2.setPaint(Color.BLACK);
      g2.draw(r1);
    } 
 */
