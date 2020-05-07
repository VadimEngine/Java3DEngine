package engine;

import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.List;

/**
 * A Collection of Polygons
 * @author Vadim
 *
 */
public class Shape {

	private List<Polygon> polygons;
	//Polygon movement logic fields
	//Default shapes (cube/rotated cube)
	
	public Shape(Polygon...polygons) {
		this.polygons = new ArrayList<>(polygons.length);
		
		for (int i = 0; i < polygons.length; i++) {
			this.polygons.add(polygons[i]);//add poly.copy?
		}
	}
	
	public Shape (Color color, Coordinate btmLeft, int width, int depth, int height) {
		polygons = new ArrayList<>(6);
		//top
		polygons.add(new Polygon(color,
				new Coordinate(btmLeft.getX() ,btmLeft.getY() ,btmLeft.getZ() + height),
				new Coordinate(btmLeft.getX() ,btmLeft.getY() + depth ,btmLeft.getZ() + height),
				new Coordinate(btmLeft.getX() + width ,btmLeft.getY() + depth ,btmLeft.getZ() + height),
				new Coordinate(btmLeft.getX() + width, btmLeft.getY() ,btmLeft.getZ() + height)));
		//bottom
		polygons.add(new Polygon(color,
				new Coordinate(btmLeft.getX() ,btmLeft.getY() ,btmLeft.getZ()),
				new Coordinate(btmLeft.getX() ,btmLeft.getY() + depth ,btmLeft.getZ()),
				new Coordinate(btmLeft.getX() + width ,btmLeft.getY() + depth ,btmLeft.getZ()),
				new Coordinate(btmLeft.getX() + width, btmLeft.getY() ,btmLeft.getZ())));
		//left
		polygons.add(new Polygon(color,
				new Coordinate(btmLeft.getX() ,btmLeft.getY() ,btmLeft.getZ()),
				new Coordinate(btmLeft.getX() ,btmLeft.getY() + depth ,btmLeft.getZ()),
				new Coordinate(btmLeft.getX() ,btmLeft.getY() + depth,btmLeft.getZ() + height),
				new Coordinate(btmLeft.getX() ,btmLeft.getY() ,btmLeft.getZ() + height)));
		//right
		polygons.add(new Polygon(color,
				new Coordinate(btmLeft.getX() + width,btmLeft.getY() ,btmLeft.getZ()),
				new Coordinate(btmLeft.getX() + width,btmLeft.getY() + depth ,btmLeft.getZ()),
				new Coordinate(btmLeft.getX() + width,btmLeft.getY() + depth,btmLeft.getZ() + height),
				new Coordinate(btmLeft.getX() + width,btmLeft.getY() ,btmLeft.getZ() + height)));
		//front
		polygons.add(new Polygon(color,
				new Coordinate(btmLeft.getX() ,btmLeft.getY() + depth,btmLeft.getZ()),
				new Coordinate(btmLeft.getX() + width ,btmLeft.getY() + depth,btmLeft.getZ()),
				new Coordinate(btmLeft.getX() + width,btmLeft.getY() + depth,btmLeft.getZ() + height),
				new Coordinate(btmLeft.getX() ,btmLeft.getY() + depth,btmLeft.getZ() + height)));
		//back
		polygons.add(new Polygon(color,
				new Coordinate(btmLeft.getX(), btmLeft.getY(),btmLeft.getZ()),
				new Coordinate(btmLeft.getX() + width ,btmLeft.getY(),btmLeft.getZ()),
				new Coordinate(btmLeft.getX() + width,btmLeft.getY(),btmLeft.getZ() + height),
				new Coordinate(btmLeft.getX() ,btmLeft.getY(),btmLeft.getZ() + height)));
	}
	
	
	
	//Not Dynamic
	public Shape (Color color, Coordinate btmLeft, int width, int depth, int height, 
			double XYangle, double XZangle, double ZYangle) {
		//rotate around center
		polygons = new ArrayList<>(6);
		//top
		polygons.add(new Polygon(color,
				new Coordinate(btmLeft.getX(), btmLeft.getY() ,btmLeft.getZ() + height),
				new Coordinate(btmLeft.getX(), btmLeft.getY() + depth ,btmLeft.getZ() + height),
				new Coordinate(btmLeft.getX() + width, btmLeft.getY() + depth ,btmLeft.getZ() + height),
				new Coordinate(btmLeft.getX() + width, btmLeft.getY() ,btmLeft.getZ() + height)));
		//bottom
		polygons.add(new Polygon(color,
				new Coordinate(btmLeft.getX(), btmLeft.getY(), btmLeft.getZ()),
				new Coordinate(btmLeft.getX(), btmLeft.getY() + depth, btmLeft.getZ()),
				new Coordinate(btmLeft.getX() + width, btmLeft.getY() + depth, btmLeft.getZ()),
				new Coordinate(btmLeft.getX() + width, btmLeft.getY() ,btmLeft.getZ())));
		//left
		polygons.add(new Polygon(color,
				new Coordinate(btmLeft.getX() ,btmLeft.getY() ,btmLeft.getZ()),
				new Coordinate(btmLeft.getX() ,btmLeft.getY() + depth ,btmLeft.getZ()),
				new Coordinate(btmLeft.getX() ,btmLeft.getY() + depth,btmLeft.getZ() + height),
				new Coordinate(btmLeft.getX() ,btmLeft.getY() ,btmLeft.getZ() + height)));
		//right
		polygons.add(new Polygon(color,
				new Coordinate(btmLeft.getX() + width,btmLeft.getY() ,btmLeft.getZ()),
				new Coordinate(btmLeft.getX() + width,btmLeft.getY() + depth ,btmLeft.getZ()),
				new Coordinate(btmLeft.getX() + width,btmLeft.getY() + depth,btmLeft.getZ() + height),
				new Coordinate(btmLeft.getX() + width,btmLeft.getY() ,btmLeft.getZ() + height)));
		//front
		polygons.add(new Polygon(color,
				new Coordinate(btmLeft.getX() ,btmLeft.getY() + depth,btmLeft.getZ()),
				new Coordinate(btmLeft.getX() + width ,btmLeft.getY() + depth,btmLeft.getZ()),
				new Coordinate(btmLeft.getX() + width,btmLeft.getY() + depth,btmLeft.getZ() + height),
				new Coordinate(btmLeft.getX() ,btmLeft.getY() + depth,btmLeft.getZ() + height)));
		//back
		polygons.add(new Polygon(color,
				new Coordinate(btmLeft.getX(), btmLeft.getY(),btmLeft.getZ()),
				new Coordinate(btmLeft.getX() + width ,btmLeft.getY(),btmLeft.getZ()),
				new Coordinate(btmLeft.getX() + width,btmLeft.getY(),btmLeft.getZ() + height),
				new Coordinate(btmLeft.getX() ,btmLeft.getY(),btmLeft.getZ() + height)));
	}
	
	
	public void rotateAroundCenter(Coordinate center, double XYAngle, double XZAngle, double ZYAngle) {
		for (int i = 0; i < polygons.size(); i++) {
			List<Coordinate> newCords = new ArrayList<>();
			for (int j = 0; j < polygons.get(i).getCoords().size(); j++) {
				Coordinate rotated = Calculator.rotateAroundCenter(polygons.get(i).getCoords().get(j), center, XYAngle, XZAngle, ZYAngle);
				newCords.add(rotated);
			}
			polygons.set(i, new Polygon(polygons.get(i).getColor(), newCords));
		}
	}
	
	
	public void render(Graphics g, Camera c) {//Find a workaround to not pass in camera
		for (int i = 0; i < polygons.size(); i++) {
			polygons.get(i).render(g, c);
		}
	}
	
	public void tick() {
		//implement a better moving mechanism
		for (int i = 0; i < polygons.size(); i++) {
			polygons.get(i).tick();
		}	
	}
	
	public void addPoly(Polygon poly) {
		polygons.add(poly);
	}
	
	public int polygonCount() {
		return polygons.size();
	}
	
	public Polygon getPolygon(int i) {//revise to return clone/cloneable
		return polygons.get(i);
	}
	
	public List<Polygon> getPolygons() {
		return polygons;
	}
	
}
