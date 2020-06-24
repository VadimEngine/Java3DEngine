package engine;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import entities.Axis;
import entities.Mesh;
import entities.Object3D;
import entities.Polygon;
import entities.Shape;
import entities.TestShape;
import entities.Triangle;

public class LogicHandler {
	
	private List<Triangle> triangles;
	private Camera theCamera;
	private Handler handler;
	private List<Mesh> meshList;
	
	
	private Mesh selectedMesh = null;
	
	
	private List<Shape> shapes;
	private Polygon ghost = null;
	private TestShape testshape = new TestShape(new Coordinate(0, 0, 10), 6, 20, 0, 0, 0);
	private Polygon selected = null;
	
	private Axis axis = new Axis();	
	
	private Object3D theObj;
	
	
	
	
	public LogicHandler(Handler handler) {
		this.handler = handler;
		triangles = new ArrayList<>();
		shapes = new ArrayList<>();
		theCamera = new Camera();
		meshList = new ArrayList<>();
		
		//populate();
		populateMesh();
		
		populateMeshIndex();
		
		theObj = new Object3D(Mesh.createCubeMesh());
		
	}
	
	public void tick() {
		theCamera.tick(handler, true);
		
		//for (int i = 0; i < shapes.size(); i++) {
			//shapes.get(i).tick();
		//}	
		//selectPoly(handler.mouseX, handler.mouseY);
		//testshape.tick();
	}
	
	public void render(RenderHandler renderer) {
		
		axis.render(renderer, theCamera);
		
		for (Triangle eachTri: triangles) {
			renderer.renderTriangle(eachTri, theCamera);
		}
		
		for (Shape eachShape: shapes) {
			for (Polygon eachPoly: eachShape.getPolygons()) {
				for (Triangle eachTri: eachPoly.getTriangles()) {
					if (selected == eachPoly) {
						//renderer.renderTriangle(eachTri, theCamera, Color.YELLOW);
					} else {
						//renderer.renderTriangle(eachTri, theCamera);
					}
					
					
				}
			}
		}
		
		for (Mesh eachMesh: meshList) {
			renderer.drawMesh(eachMesh, theCamera, eachMesh.getColor());
		}
	
		
		theObj.render(renderer, theCamera);
		
	}
	
	
	
	
	
	public void selectMesh() {
		Mesh localMesh;
		int z;
		
	}
	
	
	/**
	 * Sets the polygon that the mouse is over based on the mouse position. Sometimes works weird because of the
	 * simple sorting technique
	 * @param x The mouse x position
	 * @param y The mouse y Position
	 */
	public void selectPoly(int x, int y) {
		List<Polygon> polys = new ArrayList<>();//Only do this if camera moves or poly list changes
		for (int i = 0; i < shapes.size(); i++) {
			for (int j = 0; j < shapes.get(i).polygonCount(); j++) {
				polys.add(shapes.get(i).getPolygon(j));
			}
		}

		Collections.sort(polys, new Comparator<Polygon>() {
			@Override
			public int compare(Polygon o1, Polygon o2) {
				if (o1.closestDist(theCamera) < o2.closestDist(theCamera)) {
					return 1;
				} else if (o1.closestDist(theCamera) > o2.closestDist(theCamera)) {
					return -1;
				} else {
					return 0;
				}
			}
		});

		for (int i = polys.size() - 1; i >= 0; i--) {
			if (polys.get(i).coordInside(x, y, theCamera)) {
				selected = polys.get(i);
				return;
			}
		}
		selected = null;		
	}
	
	
	/** 
	 * Draws a rectangle at the same x, y of the screen but at z=0, works pretty well only if camera moves and
	 * only XY angles changes, but re-adjusting the mouse once the other angles change keeps the shape drawn
	 * at the same location on screen, Need to take the other angles more into account.
	 * Also need to rotate the other angles so the shape is always drawn parallel/perpendicular to the camera direction
	 * //does near need to be taken into account?
	 * 
	 * @param g The Application Grapics object
	 */
	private void drawOnZZero(Graphics g) {
		int width = 10;
		int height = 10;
		double component[] = theCamera.getUnitComponent();
		Coordinate draw = new Coordinate(theCamera.getX() + component[0] * ((0-theCamera.getZ()) / component[2]),
				theCamera.getY() + component[1] * ((0-theCamera.getZ()) / component[2]),
				theCamera.getZ() + component[2] * ((0-theCamera.getZ()) / component[2]));

		Coordinate c2 = new Coordinate(draw.getX(), draw.getY() + height, draw.getZ());
		Coordinate c3 = new Coordinate(draw.getX() + width, draw.getY() + height, draw.getZ());
		Coordinate c4 = new Coordinate(draw.getX() + width, draw.getY(), draw.getZ());

		Polygon test = new Polygon(Color.YELLOW, draw, c2, c3, c4);
		test.render(g, theCamera);
	}
	
	/**
	 * Temporary circle rendering Method
	 * @param x The x coordinate of the center
	 * @param y The y coordinate of the center
	 * @param z The z coordinate of the center
	 * @param rad The Radius of the circle
	 * @param n The number of edges on the circle, more means more smooth
	 * @param g The Application graphics object
	 */
	private void renderCircle(double x, double y, double z, double rad , double n, Graphics g) {
		List<Coordinate> tempCords = new ArrayList<>();
		
		for (int i = 0; i < n; i++) {
			double x2 = x + Math.cos((2 * Math.PI / n) * (float)i) * rad;
			double y2 = y + Math.sin((2 * Math.PI / (float)n) * (float)i) * rad;
			tempCords.add(new Coordinate(x2, y2, z));
		}
		
		Polygon pp = new Polygon(Color.GREEN, tempCords);
		pp.render(g, theCamera);
	}
	
	/**
	 * Temporary Cylinder rendering
	 * @param x The x coordinate of the bottom center
	 * @param y The y coordinate of the bottom center
	 * @param z The z coordinate of the bottom center
	 * @param rad The Radius of the bottom circle
	 * @param height The Height of the cylinder
	 * @param n The number of edges on the circle, more means more smooth
	 * @param g The Application graphics object
	 */
	private void renderCylinder(double x, double y, double z, double rad,  double height , double n, Graphics g) {
		renderCircle(x, y, z, rad, n, g);
		
		for (int i =  0; i < n; i++) {
			List<Coordinate> coords = new ArrayList<>();
			double x1 = x + Math.cos((2 * Math.PI / n) * (float)i) * rad;
			double y1 = y + Math.sin((2 * Math.PI / (float)n) * (float)(i)) * rad;
			double x2 = x + Math.cos((2 * Math.PI / n) * (float)(i-1)) * rad;
			double y2 = y + Math.sin((2 * Math.PI / (float)n) * (float)(i-1)) * rad;
			
			coords.add(new Coordinate(x2, y2, z));
			coords.add(new Coordinate(x1, y1, z));
			coords.add(new Coordinate(x1, y1, z + height));
			coords.add(new Coordinate(x2, y2, z + height));
			
			Polygon pp = new Polygon(Color.GREEN, coords);
			pp.render(g, theCamera, false);
		}
		
		renderCircle(x, y, z + height, rad, n, g);
	}
	
	public void moveCamera(double angle) {
		theCamera.move(angle);
	}

	public void scollCamera(double dir) {
		theCamera.scrollCamera(dir);
	}

	public void rotateCamera(double xdir, double ydir) {
		theCamera.rotateCamera(xdir, ydir);
	}

	public void click(int x, int y, MouseEvent e) {

	}


	
	public void setCamera(Camera cam) {
		this.theCamera = cam;
	}
	

	
	private void populateMesh() {
		/*
		Mesh floorMesh = new Mesh();
		
		final int height = 50;
		final int width = 50;
		
		boolean flag = true;
		for (int i = 0; i < 16; i++) {//Lags at 50x50
			for (int j = 0; j < 16; j++) {		
				Coordinate c1 = new Coordinate(i*height, j* width, 0.0);
				Coordinate c2 = new Coordinate(i*height + height, j* width, 0.0);
				Coordinate c3 = new Coordinate(i*height + height, j* width + width, 0.0);
				Coordinate c4 = new Coordinate(i*height, j* width + width, 0.0);
				
				if (flag) {	
					Triangle t1 = new Triangle(c1, c3, c2, new Color(60, 60, 60));
					Triangle t2 = new Triangle(c1, c3, c4, new Color(60, 60, 60));
					floorMesh.addTriangle(t1);
					floorMesh.addTriangle(t2);
				} else {
					Triangle t1 = new Triangle(c1, c3, c2, new Color(37, 37, 37));
					Triangle t2 = new Triangle(c1, c3, c4, new Color(37, 37, 37));
					floorMesh.addTriangle(t1);
					floorMesh.addTriangle(t2);
				}
				flag = !flag;
			}
			flag = !flag;
		}
		
		
		Mesh cube = new Mesh();
		{
			Coordinate ac1 = new Coordinate(0,0,100);
			Coordinate ac2 = new Coordinate(0,100,100);
			Coordinate ac3 = new Coordinate(100,100,100);
			Coordinate ac4 = new Coordinate(100,0,100);
			
			cube.addTriangle(new Triangle(ac1, ac3, ac2, Color.GRAY) );
			cube.addTriangle(new Triangle(ac1, ac3, ac4, Color.GRAY) );
			
			Coordinate bc1 = new Coordinate(0,0,0);
			Coordinate bc2 = new Coordinate(0,100,0);
			Coordinate bc3 = new Coordinate(0,100,100);
			Coordinate bc4 = new Coordinate(0,0,100);
			
			cube.addTriangle(new Triangle(bc1, bc3, bc2, Color.RED) );
			cube.addTriangle(new Triangle(bc1, bc3, bc4, Color.RED) );
			
			Coordinate cc1 = new Coordinate(0,0,0);
			Coordinate cc2 = new Coordinate(100,0,0);
			Coordinate cc3 = new Coordinate(100,0,100);
			Coordinate cc4 = new Coordinate(0,0,100);
			
			cube.addTriangle(new Triangle(cc1, cc3, cc2, Color.BLUE) );
			cube.addTriangle(new Triangle(cc1, cc3, cc4, Color.BLUE) );
			
			Coordinate dc1 = new Coordinate(0,100,0);
			Coordinate dc2 = new Coordinate(100,100,0);
			Coordinate dc3 = new Coordinate(100, 100 ,100);
			Coordinate dc4 = new Coordinate(0,100,100);
			
			cube.addTriangle(new Triangle(dc1, dc3, dc2, Color.YELLOW) );
			cube.addTriangle(new Triangle(dc1, dc3, dc4, Color.YELLOW) );
			
			Coordinate ec1 = new Coordinate(100,0,0);
			Coordinate ec2 = new Coordinate(100,100,0);
			Coordinate ec3 = new Coordinate(100,100,100);
			Coordinate ec4 = new Coordinate(100,0,100);
			
			cube.addTriangle(new Triangle(ec1, ec3, ec2, Color.MAGENTA) );
			cube.addTriangle(new Triangle(ec1, ec3, ec4, Color.MAGENTA) );
			
			Coordinate fc1 = new Coordinate(0,0,0);
			Coordinate fc2 = new Coordinate(0,100,0);
			Coordinate fc3 = new Coordinate(100,100,0);
			Coordinate fc4 = new Coordinate(100,0,0);
			
			cube.addTriangle(new Triangle(fc1, fc3, fc2, Color.GREEN) );
			cube.addTriangle(new Triangle(fc1, fc3, fc4, Color.GREEN) );	
		}
		
		meshes.add(floorMesh);
		meshes.add(cube);
		*/
	}
	
	
	private void populateMeshIndex() {
		//CUBE MESH
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
		Collections.addAll(indices, new Integer[]{0,3,4});
		
		Collections.addAll(indices, new Integer[]{2,1,5});
		Collections.addAll(indices, new Integer[]{2,5,6});
		
		Collections.addAll(indices, new Integer[]{2,3,7});
		Collections.addAll(indices, new Integer[]{2,6,7});
		
		Collections.addAll(indices, new Integer[]{4,7,6});
		Collections.addAll(indices, new Integer[]{4,6,5});
		
		Mesh theMesh = new Mesh(coordsRect, indices);
		
		//indexMesh.add(theMesh);
		//END OF CUBE MESH
		
		//Load mesh from file
		//meshList.add(MeshLoader.loadMesh());
		
		
		//FLOOR MESH
		
		final int width = 500;
		final int height = 500;
		
		List<Coordinate> floorCoord = new ArrayList<>();
		List<Integer> floorIndex = new ArrayList<>();
		
		floorCoord.add(new Coordinate(0, 0, 0));
		floorCoord.add(new Coordinate(width, 0, 0));
		floorCoord.add(new Coordinate(width, height, 0));
		floorCoord.add(new Coordinate(0, height, 0));
		
		Collections.addAll(floorIndex, new Integer[]{0, 1, 2});
		Collections.addAll(floorIndex, new Integer[]{0, 3, 2});		
		
		meshList.add(new Mesh(floorCoord, floorIndex)); 
		
	}
	
	
	private void populate() {
		shapes.add(new Shape(new Polygon(Color.RED, new Coordinate(0+100,0,10),
													 new Coordinate(0+100,100,10),
													 new Coordinate(100+100,100,10),
													 new Coordinate(100+100,0,10))));
		//populate the floor
		
		final int height = 50;
		final int width = 50;
		
		Shape floorShape = new Shape();
		boolean flag = true;
		for (int i = 0; i < 16; i++) {//Lags at 50x50
			for (int j = 0; j < 16; j++) {
				if (flag) {
					floorShape.addPoly(new Polygon(i*height, j* width, 0, height, width, new Color(60, 60, 60)));
				} else {
					floorShape.addPoly(new Polygon(i*height, j* width, 0, height, width, new Color(37, 37, 37)));
				}
				flag = !flag;
			}
			flag = !flag;
		}

		//populate the cube
		Shape cubeShape = new Shape();
		//top
		cubeShape.addPoly(new Polygon(Color.GRAY, true, new Coordinate(0,0,100), new Coordinate(0,100,100),
				new Coordinate(100,100,100), new Coordinate(100,0,100)));
		//		//y-axis
		cubeShape.addPoly(new Polygon(Color.RED, true, new Coordinate(0,0,0), new Coordinate(0,100,0),
				new Coordinate(0,100,100), new Coordinate(0,0,100)));
		//		//x-axis
		cubeShape.addPoly(new Polygon(Color.BLUE, true, new Coordinate(0,0,0), new Coordinate(100,0,0),
				new Coordinate(100,0,100), new Coordinate(0,0,100)));
		//		//across x axis
		cubeShape.addPoly(new Polygon(Color.YELLOW, true, new Coordinate(0,100,0), new Coordinate(100,100,0),
				new Coordinate(100, 100 ,100), new Coordinate(0,100,100)));
		//		//across y axis
		cubeShape.addPoly(new Polygon(Color.MAGENTA, true, new Coordinate(100,0,0), new Coordinate(100,100,0),
				new Coordinate(100,100,100), new Coordinate(100,0,100)));
		//bottom
		cubeShape.addPoly(new Polygon(Color.GREEN, true, new Coordinate(0,0,0), new Coordinate(0,100,0),
				new Coordinate(100,100,0), new Coordinate(100,0,0)));
		
		//shapes.add(cubeShape);

		Color woodColor = new Color(242, 194, 146);
		Shape cranium = new Shape(woodColor, new Coordinate(500 - 10, 500 - 10, 450), 80, 80, 50);
		Shape mandible = new Shape(woodColor, new Coordinate(500 - 10, 500 - 10, 425), 80, 80, 25);
		Shape neck = new Shape(woodColor, new Coordinate(500, 500, 400), 50, 50, 25);

		Shape chest = new Shape(woodColor, new Coordinate(500 - 25 + 4, 500 - 10 + 4, 300), 100 - 4, 80 - 4, 100);
		Shape waist = new Shape(woodColor, new Coordinate(500 - 25 + 4, 500 - 10 + 4, 250), 100 - 4, 80 - 4, 50);
		Shape hip = new Shape(woodColor, new Coordinate(500 - 25 + 4, 500 - 10 + 4, 200), 100 - 4, 80 - 4, 50);

		Shape lShoulder = new Shape(woodColor, new Coordinate(500 - 75 + 4, 500, 375), 50, 50, 25);
		Shape lUpperArm = new Shape(woodColor, new Coordinate(500 - 75, 500, 300), 50, 50, 75);
		Shape lElbow = new Shape(woodColor, new Coordinate(500 - 75 + 4, 500 + 4, 275), 42, 24, 25);
		Shape lForearm = new Shape(woodColor, new Coordinate(500 - 75, 500, 200), 50, 50, 75);
		Shape lWrist = new Shape(woodColor, new Coordinate(500 - 75 + 4, 500 + 4, 175), 42, 42, 25); 
		Shape lHand = new  Shape(woodColor, new Coordinate(500 - 75, 500, 150), 50, 50, 25); 
		//fingers

		Shape rShoulder = new Shape(woodColor, new Coordinate(500 + 75 - 4, 500, 375), 50, 50, 25);
		Shape rUpperArm = new Shape(woodColor, new Coordinate(500 + 75, 500, 300), 50, 50, 75);
		Shape rElbow = new Shape(woodColor, new Coordinate(500 + 75 + 4, 500 + 4, 275), 42, 42, 25);
		Shape rForearm = new Shape(woodColor, new Coordinate(500 + 75, 500, 200), 50, 50, 75);
		Shape rWrist = new Shape(woodColor, new Coordinate(500 + 75 + 4, 500 + 4, 175), 42, 42, 25); 
		Shape rHand = new  Shape(woodColor, new Coordinate(500 + 75, 500, 150), 50, 50, 25); 
		//fingers

		Shape lQuad = new Shape(woodColor, new Coordinate(500 - 25, 500, 125), 50, 50, 75);
		Shape lKnee = new Shape(woodColor, new Coordinate(500 - 25 + 4, 500 + 4, 100), 50 - 4, 50 - 4, 25);
		Shape lShin = new Shape(woodColor, new Coordinate(500 - 25, 500, 25), 50, 50, 75);
		Shape lAnkle = new Shape(woodColor, new Coordinate(500 - 25, 500, 0), 50, 50, 25);
		Shape lFoot = new Shape(woodColor, new Coordinate(500 - 25, 500 + 25, 0), 50, 75, 25);
		//toes, hip joint

		Shape rQuad = new Shape(woodColor, new Coordinate(500 + 25, 500, 125), 50, 50, 75);
		Shape rKnee = new Shape(woodColor, new Coordinate(500 + 25 + 4, 500 + 4, 100), 50 - 4, 50 - 4, 25);
		Shape rShin = new Shape(woodColor, new Coordinate(500 + 25, 500, 25), 50, 50, 75);
		Shape rAnkle = new Shape(woodColor, new Coordinate(500 + 25, 500, 0), 50, 50, 25);
		Shape rFoot = new Shape(woodColor, new Coordinate(500 + 25, 500 + 25, 0), 50, 75, 25);
		//toes, hip joint

		shapes.add(floorShape);
		
		//shapes.add(cubeShape);
		shapes.add(cranium);
		shapes.add(mandible);
		shapes.add(neck);
		shapes.add(chest);
		shapes.add(waist);
		shapes.add(hip);
		shapes.add(lShoulder);
		shapes.add(lUpperArm);
		shapes.add(lElbow);
		shapes.add(lForearm);
		shapes.add(lWrist);
		shapes.add(lHand);
		shapes.add(rShoulder);
		shapes.add(rUpperArm);
		shapes.add(rElbow);
		shapes.add(rForearm);
		shapes.add(rWrist);
		shapes.add(rHand);
		shapes.add(lQuad);
		shapes.add(lKnee);
		shapes.add(lShin);
		shapes.add(lAnkle);
		shapes.add(lFoot);
		shapes.add(rQuad);
		shapes.add(rKnee);
		shapes.add(rShin);
		shapes.add(rAnkle);
		shapes.add(rFoot);
	}

}
