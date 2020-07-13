package entities;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import engine.Calculator;
import engine.Camera;
import engine.Coordinate;
import engine.MeshLoader;
import engine.RenderHandler;

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
	
	
	public final static Map<String, Mesh> MESHES;
	
	
	static {
		MESHES = new HashMap<>();
		try {
		
		//load all meshes from file
		
		Mesh teapot = MeshLoader.loadMesh("teapot.txt");
		teapot.setName("Teapot");
		MESHES.put("Teapot", teapot);
		
		Mesh sphere = MeshLoader.loadMesh("sphere.txt");
		sphere.setName("Sphere");
		MESHES.put("Sphere", sphere);
		
		Mesh cone = MeshLoader.loadMesh("cone.txt");
		cone.name = "Cone";
		MESHES.put("Cone", cone);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	
	private static int MeshCount = 0;
	//middle coordinate defaults to 0,0,0
	
	//Use arrays instead?
	private List<Coordinate> vertices;
	private List<Integer> indices;
	private Color color;
	private String name;
	
	
	public Mesh(List<Coordinate> vertices, List<Integer> indices, Color color) {
		this.vertices = vertices;
		this.indices = indices;
		this.color = color;
		this.name = "Mesh " + MeshCount++;
	}
	
	public Mesh(List<Coordinate> vertices, List<Integer> indices) {
		this(vertices, indices, Color.DARK_GRAY);
	}
	
	public void render(RenderHandler renderer, Camera cam) {
		for (int i = 0; i < indices.size(); i+=3) {
			Coordinate c1 = vertices.get(indices.get(i)).clone();
			Coordinate c2 = vertices.get(indices.get(i + 1)).clone();
			Coordinate c3 = vertices.get(indices.get(i + 2)).clone();
			
			c1 = Calculator.rotateAroundCamera(c1, cam);
			c2 = Calculator.rotateAroundCamera(c2, cam);
			c3 = Calculator.rotateAroundCamera(c3, cam);
			
			renderer.drawTriangleScanLineOp(c1, c2, c3, color);
		}
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
	
	public static Mesh createCubeMesh(double x, double y, double z,
			double width, double depth, double height, Color theColor) {
		
		List<Coordinate> coords = new ArrayList<>();
		List<Integer> indices = new ArrayList<>();
				
		coords.add(new Coordinate(x, y, z));
		coords.add(new Coordinate(x + width , y, z));
		coords.add(new Coordinate(x + width, y + depth, z));
		coords.add(new Coordinate(x, y + depth, z));
		
		coords.add(new Coordinate(x, y, z + height));
		coords.add(new Coordinate(x + width, y, z + height));
		coords.add(new Coordinate(x + width, y + depth, z + height));
		coords.add(new Coordinate(x, y + depth, z+ height));
		
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
		
		return new Mesh(coords, indices, theColor);
	}
	
	@Override
	public String toString() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}

}
