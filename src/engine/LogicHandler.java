package engine;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import entities.Axis;
import entities.Mesh;
import entities.Object3D;
import entities.Texture;
import entities.TexturedCube;
import entities.TexturedMesh;
import entities.VertexTex;

public class LogicHandler {
	
	private static int scale = 1;
	
	private Camera theCamera;
	private List<Camera> cameraList;
	private Handler handler;
	//private List<Mesh> meshList;
	
	private Axis axis = new Axis();	

	private List<Object3D> objectList;
	
	private Object3D selectedObject = null;
	private Object3D hoverObject = null;
	
	private List<Object3D> theFloor;
	
	private boolean displayFloor = true;
	
	//Temporarily public and in this class
	public List<Mesh> meshList = new ArrayList<>();
	
	
	private Texture theTexture = new Texture(Art.SPRITES[1][5]);
	
	private TexturedMesh texCube = TexturedMesh.createTextureCube(500,500,100, 100, 100, 100, theTexture);
	private TexturedMesh teapotTex = TexturedMesh.createTexTeapot();
	
	
	private TexturedCube texturedCube = new TexturedCube(400,400,50,100,100,100,theTexture);
	
	
	public LogicHandler(Handler handler) {
		this.handler = handler;
		theCamera = new Camera();
		theFloor = new ArrayList<>();
		objectList = new ArrayList<>();

		populateMeshIndex();
		
		cameraList = new ArrayList<>();
		cameraList.add(theCamera);
		
		//Mesh teapotMesh = MeshLoader.loadMesh();
		//teapotMesh.setName("TeaPot");
		//meshList.add(teapotMesh);
		
		for (Mesh meshName: Mesh.MESHES.values()) {
			meshList.add(meshName);
		}
		
		//Plane Mesh
		Mesh planeMesh = addRectagleMesh(-.5, -.5, 0, 1, 1, Color.LIGHT_GRAY).getMesh();
		planeMesh.setName("Plane");
		meshList.add(planeMesh);
		
		//Cube Mesh
		Mesh theCubeMesh = Mesh.createCubeMesh(-.5, -.5, -.5, 1,1,1, Color.LIGHT_GRAY);
		theCubeMesh.setName("Cube");
		meshList.add(theCubeMesh);
		
		//Circle mesh
		
		//Cylinder mesh
		
		
	}
	
	public void tick() {			
		for (Camera eachCam: cameraList) {
			if (theCamera == eachCam) {
				eachCam.tick(handler, true);
			} else {
				eachCam.tick(handler, false);
			}
		}
		
		selectMesh(handler.mouseX, handler.mouseY);		
	}
	
	public void render(RenderHandler renderer) {
		axis.render(renderer, theCamera);
		
		//for (Object3D eachObject: objectList) {
		for (int i = 0; i < objectList.size(); i++) {
			Object3D eachObject = objectList.get(i);
			if (eachObject == selectedObject) {
				eachObject.render(renderer, theCamera, Color.BLUE);
			} else if (eachObject == hoverObject) {
				eachObject.render(renderer, theCamera, Color.YELLOW);
			} else {
				eachObject.render(renderer, theCamera);
			}
		}
		
		if (displayFloor) {
			for (Object3D eachFloor: theFloor) {
				eachFloor.render(renderer, theCamera);
			}
		}
		
		for (Camera eachCamera: cameraList) {
			if (eachCamera != theCamera) {
				eachCamera.render(renderer, theCamera);
			}
			
		}
						
		scale = ((scale + 1) % 500);
		
		/*
		Coordinate cc1 = new Coordinate(0,0,scale);
		Coordinate cc2 = new Coordinate(300,300,scale);
		Coordinate cc3 = new Coordinate(0,300,scale);
		Coordinate cc4 = new Coordinate(300,0,scale);
		
		cc1 = Calculator.rotateAroundCamera(cc1, theCamera);
		cc2 = Calculator.rotateAroundCamera(cc2, theCamera);
		cc3 = Calculator.rotateAroundCamera(cc3, theCamera);
		cc4 = Calculator.rotateAroundCamera(cc4, theCamera);
		
		VertexTex v0 = new VertexTex(cc1.getX(), cc1.getY(), cc1.getZ(), 0, 0, theTexture);
		VertexTex v1 = new VertexTex(cc2.getX(), cc2.getY(), cc2.getZ(), 1, 1, theTexture);
		VertexTex v2 = new VertexTex(cc3.getX(), cc3.getY(), cc2.getZ(), 0, 1, theTexture);
		VertexTex v3 = new VertexTex(cc4.getX(), cc4.getY(), cc4.getZ(), 1, 0, theTexture);		
		*/
		
		
		VertexTex v0 = new VertexTex(0,0,scale, 0, 0, theTexture);
		VertexTex v1 = new VertexTex(300,300,scale, 1, 1, theTexture);
		VertexTex v2 = new VertexTex(0,300,scale, 0, 1, theTexture);
		VertexTex v3 = new VertexTex(300,0,scale, 1, 0, theTexture);
		
		v0 = (VertexTex) Calculator.rotateAroundCamera(v0, theCamera);
		v1 = (VertexTex) Calculator.rotateAroundCamera(v1, theCamera);
		v2 = (VertexTex) Calculator.rotateAroundCamera(v2, theCamera);
		v3 = (VertexTex) Calculator.rotateAroundCamera(v3, theCamera);
		
		//renderer.drawTriangleScanLineTex(v0, v1, v2, theTexture);
		//renderer.drawTriangleScanLineTex(v0, v3, v1, theTexture);
		
		
		//texCube.render(renderer, theCamera);
		//teapotTex.render(renderer, theCamera);
		
		texturedCube.render(renderer, theCamera);
	}
					
	
	/**
	 * Do on mouse click
	 * @param xPos
	 * @param yPos
	 */
	public void selectMesh(int xPos, int yPos) {
		Object3D localObj = null;
		double z = Integer.MAX_VALUE;
		
		for (int j = 0; j < objectList.size(); j++) {
			Object3D eachObj = objectList.get(j);
			Mesh eachMesh = eachObj.getMesh();
			
			List<Integer> meshIndices = eachMesh.getIndices();
			for (int i = 0; i < meshIndices.size(); i+=3) {				
				Coordinate c1 = eachMesh.getCoordinate( meshIndices.get(i) );
				Coordinate c2 = eachMesh.getCoordinate( meshIndices.get(i + 1) );
				Coordinate c3 = eachMesh.getCoordinate( meshIndices.get(i + 2) );
				
				//rotate around mesh center (0,0,0)
				c1 = Calculator.rotateAroundCenter(c1, new Coordinate(0, 0, 0), eachObj.getXAngle(), eachObj.getYAngle(), eachObj.getXAngle());
				c2 = Calculator.rotateAroundCenter(c2, new Coordinate(0, 0, 0), eachObj.getXAngle(), eachObj.getYAngle(), eachObj.getXAngle());
				c3 = Calculator.rotateAroundCenter(c3, new Coordinate(0, 0, 0), eachObj.getXAngle(), eachObj.getYAngle(), eachObj.getXAngle());
				
				//Scale
				c1.setX(c1.getX() * eachObj.getXScale());
				c1.setY(c1.getY() * eachObj.getYScale());
				c1.setZ(c1.getZ() * eachObj.getZScale());
				
				c2.setX(c2.getX() * eachObj.getXScale());
				c2.setY(c2.getY() * eachObj.getYScale());
				c2.setZ(c2.getZ() * eachObj.getZScale());
				
				c3.setX(c3.getX() * eachObj.getXScale());
				c3.setY(c3.getY() * eachObj.getYScale());
				c3.setZ(c3.getZ() * eachObj.getZScale());
				
				//translate
				c1.addX(eachObj.getPosition().getX());
				c1.addY(eachObj.getPosition().getY());
				c1.addZ(eachObj.getPosition().getZ());
				
				c2.addX(eachObj.getPosition().getX());
				c2.addY(eachObj.getPosition().getY());
				c2.addZ(eachObj.getPosition().getZ());
				
				c3.addX(eachObj.getPosition().getX());
				c3.addY(eachObj.getPosition().getY());
				c3.addZ(eachObj.getPosition().getZ());
				
				//rotate around camera
				c1 = Calculator.rotateAroundCamera(c1, theCamera);
				c2 = Calculator.rotateAroundCamera(c2, theCamera);
				c3 = Calculator.rotateAroundCamera(c3, theCamera);
					
				double theZ = Calculator.zOnPlane(c1, c2, c3, xPos, yPos);
				
				// closer z value and mouse inbound
				if (theZ <= z && theZ >=0 && Calculator.xyInbound(c1, c2, c3, xPos, yPos)) {
					localObj = eachObj;
					z = theZ;
				}
			}	
		}
		
		hoverObject = localObj;
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

		//Polygon test = new Polygon(Color.YELLOW, draw, c2, c3, c4);
		//test.render(g, theCamera);
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
		
		//Polygon pp = new Polygon(Color.GREEN, tempCords);
		//pp.render(g, theCamera);
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
			
			//Polygon pp = new Polygon(Color.GREEN, coords);
			//pp.render(g, theCamera, false);
		}
		
		renderCircle(x, y, z + height, rad, n, g);
	}
	
	
	private void populateMeshIndex() {
		final int height = 100;
		final int width = 100;
		
		boolean flag = true;
		for (int i = 0; i < 16; i++) {//Lags at 50x50
			for (int j = 0; j < 16; j++) {
				if (flag) {
					theFloor.add(addRectagleMesh(i*height, j* width, 0, height, width, new Color(60, 60, 60)));
				} else {
					theFloor.add(addRectagleMesh(i*height, j* width, 0, height, width, new Color(37, 37, 37)));
				}
				flag = !flag;
			}
			flag = !flag;
		}
		
		Mesh.createCubeMesh(0, 0, 0, 50, 50, 50, Color.LIGHT_GRAY);
		//Load mesh from file
		//objectList.add( new Object3D(MeshLoader.loadMesh()));
		addManMesh();
	}
	
	
	private Object3D addRectagleMesh(double x, double y, double z, double width, double height, Color theColor) {		
		List<Coordinate> coords = new ArrayList<>();
		List<Integer> indices = new ArrayList<>();
		
		coords.add(new Coordinate(x, y, z));
		coords.add(new Coordinate(x + width, y, z));
		coords.add(new Coordinate(x + width, y + height, z));
		coords.add(new Coordinate(x, y + height, z));
		
		Collections.addAll(indices, new Integer[]{0, 1, 2});
		Collections.addAll(indices, new Integer[]{0, 3, 2});	
		
		return new Object3D( new Mesh(coords, indices), theColor);
		
		//objectList.add(new Object3D( new Mesh(coords, indices))); 
	}
	
	private void addManMesh() {
		Color woodColor = new Color(242, 194, 146);
		//Use color and Object3D
		objectList.add(new Object3D(Mesh.createCubeMesh(-10,-10, 450, 80, 80, 50, woodColor), woodColor)); // cranium
		objectList.add(new Object3D(Mesh.createCubeMesh(-10, -10, 425, 80, 80, 25, woodColor), woodColor)); // mandible
		objectList.add(new Object3D(Mesh.createCubeMesh(0, 0, 400, 50, 50, 25, woodColor), woodColor)); // neck
		
		objectList.add(new Object3D(Mesh.createCubeMesh(-25 + 4, -10 + 4, 300, 100 - 4, 80 - 4, 100, woodColor), woodColor)); // chest
		objectList.add(new Object3D(Mesh.createCubeMesh(-25 + 4, -10 + 4, 250, 100 - 4, 80 - 4, 50, woodColor), woodColor)); // waist
		objectList.add(new Object3D(Mesh.createCubeMesh(-25 + 4, -10 + 4, 200, 100 - 4, 80 - 4, 50, woodColor), woodColor)); // hip
		
		objectList.add(new Object3D(Mesh.createCubeMesh(-75 + 4, 0, 375, 50, 50, 25, woodColor), woodColor)); // lShoulder
		objectList.add(new Object3D(Mesh.createCubeMesh(-75, 0, 300, 50, 50, 75, woodColor), woodColor)); // lUpperArm
		objectList.add(new Object3D(Mesh.createCubeMesh(-75 + 4, 4, 275, 42, 42, 25, woodColor), woodColor)); // lElbow
		objectList.add(new Object3D(Mesh.createCubeMesh(-75, 0, 200, 50, 50, 75, woodColor), woodColor)); // lForearm
		objectList.add(new Object3D(Mesh.createCubeMesh(-75 + 4, 4, 175, 42, 42, 25, woodColor), woodColor)); // lWrist
		objectList.add(new Object3D(Mesh.createCubeMesh(-75, 0, 150, 50, 50, 25, woodColor), woodColor)); // lHand
		//fingers
		
		objectList.add(new Object3D(Mesh.createCubeMesh(75 - 4, 0, 375, 50, 50, 25, woodColor), woodColor)); // rShoulder
		objectList.add(new Object3D(Mesh.createCubeMesh(75, 0, 300, 50, 50, 75, woodColor), woodColor)); // rUpperArm
		objectList.add(new Object3D(Mesh.createCubeMesh(75 + 4, 4, 275, 42, 42, 25, woodColor), woodColor)); // rElbow
		objectList.add(new Object3D(Mesh.createCubeMesh(75, 0, 200, 50, 50, 75, woodColor), woodColor)); // rForearm
		objectList.add(new Object3D(Mesh.createCubeMesh(75 + 4, 4, 175, 42, 42, 25, woodColor), woodColor));  // rWrist
		objectList.add(new Object3D(Mesh.createCubeMesh(75, 0, 150, 50, 50, 25, woodColor), woodColor)); // rHand
		//fingers
		
		objectList.add(new Object3D(Mesh.createCubeMesh(-25, 0, 125, 50, 50, 75, woodColor), woodColor)); // lQuad
		objectList.add(new Object3D(Mesh.createCubeMesh(-25 + 4, 4, 100, 50 - 4, 50 - 4, 25, woodColor), woodColor)); // lKnee
		objectList.add(new Object3D(Mesh.createCubeMesh(-25, 0, 25, 50, 50, 75, woodColor), woodColor)); // lShin
		objectList.add(new Object3D(Mesh.createCubeMesh(-25, 0, 0, 50, 50, 25, woodColor), woodColor)); // lAnkle
		objectList.add(new Object3D(Mesh.createCubeMesh(-25, 25, 0, 50, 75, 25, woodColor), woodColor)); // lFoot
		//toes, hip joint
		
		objectList.add(new Object3D(Mesh.createCubeMesh(25, 0, 125, 50, 50, 75, woodColor), woodColor)); //rQuad
		objectList.add(new Object3D(Mesh.createCubeMesh(25 + 4, 4, 100, 50 - 4, 50 - 4, 25, woodColor), woodColor)); // rKnee
		objectList.add(new Object3D(Mesh.createCubeMesh(25, 0, 25, 50, 50, 75, woodColor), woodColor)); // rShin
		objectList.add(new Object3D(Mesh.createCubeMesh(25, 0, 0, 50, 50, 25, woodColor), woodColor)); // rAnkle
		objectList.add(new Object3D(Mesh.createCubeMesh(25, 0 + 25, 0, 50, 75, 25, woodColor), woodColor)); // rFoot
		//toes, hip joint
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
		selectedObject = hoverObject;
		handler.sideSetFocus(selectedObject);		
	}
	
	public List<Camera> getCameras() {
		return cameraList;
	}

	public void setCamera(Camera cam) {
		this.theCamera = cam;
	}
	
	public void addCamera(Camera cam) {
		cameraList.add(cam);
	}
	
	public List<Object3D> getObjects() {
		return objectList;
	}
	
	public void setSelectedObject(Object3D theObject) {
		this.selectedObject = theObject;
	}
	
	public void setFloorDisplay(boolean displayFloor) {
		this.displayFloor = displayFloor;
	}

	public void addObject3D(Mesh theMesh) {
		objectList.add(new Object3D(theMesh));
	}
	
}
