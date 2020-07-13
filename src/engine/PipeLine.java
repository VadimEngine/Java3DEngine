package engine;

import java.util.List;

public class PipeLine {

	
	public PipeLine() {}
	
	public void draw(List<Coordinate> vertices, List<Integer> indices) {
		//callprocessVertices
	}
	
	public void processVertices(List<Coordinate> vertices, List<Integer> indices) {
		
		//do rotations, scales and translates
		
		//call AssembleTriangles with new vertices
		
	}
	
	public void assembleTriangles(List<Coordinate> vertices, List<Integer> indices) {
		//call processTriangle for every 3 coordinates based on indices
	}
	
	
	public void processTriangle(Coordinate c1, Coordinate c2, Coordinate c3) {
		//Will replace with geometry shader?
		
		//call postProcessTriangle
	}
	
	public void postProcessTriangle(Coordinate c1, Coordinate c2, Coordinate c3) {
		
		//Transform around camera and set perspective
		
		//Call draw Triangle
		
	}
	
	public void drawTriangle(Coordinate c1, Coordinate c2, Coordinate c3) {
		//draw triangle from RenderHandler
	}
	
	public void drawFlatTopTriangle(Coordinate c1, Coordinate c2, Coordinate c3) {
		//call draw flat triangle
	}
	
	public void drawFlatBottomTriangle(Coordinate c1, Coordinate c2, Coordinate c3) {
		//call draw flat triangle
	}
	
	public void drawFlatTriangle() {
		
		//Screen.setPixel
	}
	
	
}
