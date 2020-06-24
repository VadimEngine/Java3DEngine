package entities;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import engine.Camera;
import engine.Coordinate;
import engine.RenderHandler;

public class Rectangle {

	private Triangle t1;
	private Triangle t2;	
	private Color color;
	
	
	public Rectangle(Coordinate p1, Coordinate p2, Coordinate p3,  Coordinate p4,
					Color color) {
		this.color = color;
		t1 = new Triangle(p1, p3, p2, color);
		t2 = new Triangle(p1, p3, p4, color);
	}
	
	
	public void render(RenderHandler renderer, Camera cam) {
		
		
	}
	
	public List<Triangle> getTriangles() {
		List<Triangle> ret = new ArrayList<>();
		
		ret.add(t1);
		ret.add(t2);
		
		
		return ret;
	}
	
	
}
