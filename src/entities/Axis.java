package entities;
import java.awt.Color;
import java.awt.Graphics;

import engine.Calculator;
import engine.Camera;
import engine.Coordinate;
import engine.RenderHandler;

/**
 * Draws the axis and rotates by converting from spherical coordinates to Cartesian 
 * @author user
 *
 */
public class Axis {

	private int xLength;
	private int yLength;
	private int zLength;
	int gridSize = 100;


	public Axis() {
		xLength = 1200;
		yLength = 1200;
		zLength = 1200;
	}

	public void tick() {}

	public void render(Graphics g, Camera c) {
		Coordinate centerCord = Calculator.rotateAroundCamera(new Coordinate(0, 0, 0), c);
		Coordinate xEndCord = Calculator.rotateAroundCamera(new Coordinate(xLength, 0,  0), c);
		Coordinate yEndCord = Calculator.rotateAroundCamera(new Coordinate(0, yLength, 0), c);
		Coordinate zEndCord = Calculator.rotateAroundCamera(new Coordinate(0, 0, zLength), c);

		drawLine(yEndCord, centerCord, Color.BLUE, g);
		drawLine(xEndCord, centerCord, Color.RED, g);
		drawLine(zEndCord, centerCord, Color.MAGENTA, g);
	}
	
	private void drawLine(Coordinate end1, Coordinate end2, Color color, Graphics g) {
		if (end1.getZ() >= 0 && end2.getZ() >= 0) {
			g.setColor(color);
			g.drawLine((int)(end2.getX()), (int)(end2.getY()),
					   (int)(end1.getX()), (int)(end1.getY()));
		}
	}
	
	
	public void render(RenderHandler renderer, Camera c) {
		Coordinate centerCord = Calculator.rotateAroundCamera(new Coordinate(0, 0, 0), c);
		Coordinate xEndCord = Calculator.rotateAroundCamera(new Coordinate(xLength, 0,  0), c);
		Coordinate yEndCord = Calculator.rotateAroundCamera(new Coordinate(0, yLength, 0), c);
		Coordinate zEndCord = Calculator.rotateAroundCamera(new Coordinate(0, 0, zLength), c);
		
		renderer.drawLine3DCustom(yEndCord, centerCord, Color.CYAN);
		renderer.drawLine3DCustom(xEndCord, centerCord, Color.RED);
		renderer.drawLine3DCustom(zEndCord, centerCord, Color.MAGENTA);
	}


}
