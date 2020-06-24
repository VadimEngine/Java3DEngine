package entities;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import engine.Coordinate;

/**
 * 
 * Currently take in a list of triangles and draw the triangles
 * 
 * later take in a list of coordinates(vertices) and a list of indices
 * 
 * @author user
 *
 */
public class Mesh {
	
	//middle coordinate defaults to 0,0,0
	
	//Use arrays instead?
	private List<Coordinate> vertices;
	private List<Integer> indices;
	private Color color;
	
	
	public Mesh(List<Coordinate> vertices, List<Integer> indices, Color color) {
		this.vertices = vertices;
		this.indices = indices;
		this.color = color;
	}
	
	public Mesh(List<Coordinate> vertices, List<Integer> indices) {
		this(vertices, indices, Color.DARK_GRAY);
	}
	
	
	
	public List<Coordinate> getVertices() {
		return vertices;
	}
	
	public List<Integer> getIndices() {
		return indices;
	}
	
	public Coordinate getCoordinate(int index) {
		return vertices.get(index);
	}
	
	public void setColor(Color theColor) {
		this.color = theColor;
	}
	
	public Color getColor() {
		return color;
	}
	
	
	//STATIC
	
	public static Mesh createCubeMesh() {
		List<Coordinate> coordsRect = new ArrayList<>();
		
		coordsRect.add(new Coordinate(-50, -50, -50));	//0
		coordsRect.add(new Coordinate(50, -50, -50));	//1
		coordsRect.add(new Coordinate(50, 50, -50));	//2
		coordsRect.add(new Coordinate(-50, 50, -50));	//3
		coordsRect.add(new Coordinate(-50, -50, 50));	//4
		coordsRect.add(new Coordinate(50, -50, 50));	//5
		coordsRect.add(new Coordinate(50, 50, 50));		//6
		coordsRect.add(new Coordinate(-50, 50, 50));	//7
		
		List<Integer> indices = new ArrayList<>();
		
		Collections.addAll(indices, new Integer[]{0,1,2});
		Collections.addAll(indices, new Integer[]{0,3,2});
		
		Collections.addAll(indices, new Integer[]{0,4,1});
		Collections.addAll(indices, new Integer[]{1,5,4});
		
		Collections.addAll(indices, new Integer[]{0,3,7});
		Collections.addAll(indices, new Integer[]{0,4,7});
		
		Collections.addAll(indices, new Integer[]{2,1,5});
		Collections.addAll(indices, new Integer[]{2,5,6});
		
		Collections.addAll(indices, new Integer[]{2,3,7});
		Collections.addAll(indices, new Integer[]{2,6,7});
		
		Collections.addAll(indices, new Integer[]{4,7,6});
		Collections.addAll(indices, new Integer[]{4,6,5});
		
		return new Mesh(coordsRect, indices);
	}

}
