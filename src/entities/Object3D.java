package entities;

import java.awt.Color;
import java.util.List;

import engine.Calculator;
import engine.Camera;
import engine.Coordinate;
import engine.RenderHandler;

public class Object3D {
	
	private double temp = 1;
	private double increment = 1;
	
	
	private Mesh theMesh;
	
	private Coordinate center = new Coordinate(0,0,0);
	
	//x scale, yscle and zscale	
	private double xScale = 1;
	private double yScale = 1;
	private double zScale = 1;
	
	private Color color = Color.DARK_GRAY;
	
	//rotates around z axis
	private double xyAngle = 0;
	
	//rotates around y axis
	private double zyAngle = 0;
	
	//rotates around x axis
	private double xzAngle = 0;
	
	public Object3D(Mesh theMesh) {
		this.theMesh = theMesh;
	}
	
	public void tick() {
		
	}
	
	public void render(RenderHandler renderer, Camera cam) {
		temp += increment;
		if (temp > 360 || temp < 0) {
			increment *= -1;
		}
		
		//xScale = temp;
		//yScale = temp;
		//zScale = temp;
		
		//xyAngle = temp;
		//zyAngle = temp;
		//xzAngle = temp;
		
		center.setZ(temp);
		
		List<Integer> indicies = theMesh.getIndices();
		
		for (int i = 0; i < indicies.size(); i+=3) {
			Coordinate c1 = theMesh.getCoordinate(indicies.get(i));
			Coordinate c2 = theMesh.getCoordinate(indicies.get(i + 1));
			Coordinate c3 = theMesh.getCoordinate(indicies.get(i + 2));
			
			
			//rotate around mesh center (0,0,0)
			c1 = Calculator.rotateAroundCenter(c1, new Coordinate(0,0,0), xyAngle, zyAngle, xzAngle);
			c2 = Calculator.rotateAroundCenter(c2, new Coordinate(0,0,0), xyAngle, zyAngle, xzAngle);
			c3 = Calculator.rotateAroundCenter(c3, new Coordinate(0,0,0), xyAngle, zyAngle, xzAngle);
			
			//Scale
			c1.setX(c1.getX() * xScale);
			c1.setY(c1.getY() * yScale);
			c1.setZ(c1.getZ() * zScale);
			
			c2.setX(c2.getX() * xScale);
			c2.setY(c2.getY() * yScale);
			c2.setZ(c2.getZ() * zScale);
			
			c3.setX(c3.getX() * xScale);
			c3.setY(c3.getY() * yScale);
			c3.setZ(c3.getZ() * zScale);
			
			//translate
			c1.addX(center.getX());
			c1.addY(center.getY());
			c1.addZ(center.getZ());
			
			c2.addX(center.getX());
			c2.addY(center.getY());
			c2.addZ(center.getZ());
			
			c3.addX(center.getX());
			c3.addY(center.getY());
			c3.addZ(center.getZ());
			
			//rotate around camera
			c1 = Calculator.rotateAroundCamera(c1, cam);
			c2 = Calculator.rotateAroundCamera(c2, cam);
			c3 = Calculator.rotateAroundCamera(c3, cam);
						
			//renderer.renderTriangle(new Triangle(c1, c2, c3, color), cam);			
			renderer.drawTriangleScanLine(c1, c2, c3, color);
			renderer.drawLine3D(c1, c2, Color.BLACK);
			renderer.drawLine3D(c2, c3, Color.BLACK);
			renderer.drawLine3D(c3, c1, Color.BLACK);
			
			//draw(c1, c2, c3, Color.WHITE);
			//drawTriangle3DCorner(c1, c2, c3, Color.RED);
		}
	}

}
