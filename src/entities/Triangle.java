package entities;

import java.awt.Color;
import engine.Calculator;
import engine.Camera;
import engine.Coordinate;
import engine.Screen;

/**
 * Triangle class that is rendered
 * 
 * May attempt to normalize the coordinate system.
 * 
 * @author Vadim
 *
 */
public class Triangle {
	
	private Coordinate[] coords = new Coordinate[3];
	private Color color;
	
	
	public Triangle(Coordinate p1, Coordinate p2, Coordinate p3, Color color) {
		coords[0] = p1;
		coords[1] = p2;
		coords[2] = p3;
		this.color = color;
	}
	
	
	public void tick() {}
	

		
	public void renderTriangles(Screen theScreen, Camera cam) {
		//theScreen.renderTriangle(this, cam);
	}
	
	public void renderTriangles(Screen theScreen, Camera cam, Color theColor) {}
	
	
	public Coordinate[] getTriCoords(Camera cam) {
		Coordinate[] ret = new Coordinate[3];
		ret[0] = Calculator.rotateAroundCamera(coords[0], cam);
		ret[1] = Calculator.rotateAroundCamera(coords[1], cam);
		ret[2] = Calculator.rotateAroundCamera(coords[2], cam);
		
		return ret;
	}
	
	public Coordinate[] getCoordinate() {
		return coords;
	}
	
	public Color getColor() {
		return this.color;
	}
	
}
