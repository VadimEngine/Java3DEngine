package engine;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseEvent;
import java.awt.geom.Path2D;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class Screen {

	private Handler handler;
	private List<Camera> cameras = new ArrayList<>();
	private Camera camera = new Camera();
	private Axis axis = new Axis();	
	private List<Shape> shapes = new ArrayList<>();
	private Polygon ghost = null;
	
	private TestShape testshape = new TestShape(new Coordinate(0, 0, 10), 6, 20, 0, 0, 0);
	
	private Polygon selected = null;
	

	public Screen(Handler handler) {
		this.handler = handler;
		populateShapes();
		cameras.add(camera);
		cameras.add(new Camera(500,500,500));
	}

	public void render(Graphics g) {	
		if (ghost != null) {
			ghost.render(g, camera);
		}

		List<Polygon> polys = new ArrayList<>();//Only do this if camera moves or poly list changes
		for (int i = 0; i < shapes.size(); i++) {
			for (int j = 0; j < shapes.get(i).polygonCount(); j++) {
				polys.add(shapes.get(i).getPolygon(j));
			}
		}

		Collections.sort(polys, new Comparator<Polygon>() {//Save this compare method?
			@Override
			public int compare(Polygon o1, Polygon o2) {
				if (o1.closestDist(camera) < o2.closestDist(camera)) {
					return 1;
				} else if (o1.closestDist(camera) > o2.closestDist(camera)) {
					return -1;
				} else {
					return 0;
				}
			}
		});

		for (int i = 0; i < polys.size(); i++) {			
			//if (polys.get(i) == side.getSelected()) {
				//polys.get(i).render(g, camera, Color.YELLOW);
			//} else {
			
			if (polys.get(i) == selected) {
				polys.get(i).render(g, camera, Color.YELLOW);
			} else {
				polys.get(i).render(g, camera);
			}
				
				
				//}

			//if (polys.get(i) == side.getClickSelected()) {
			//	polys.get(i).render(g, camera, Color.BLUE);
			//}
		}
		//camera.render(g, camera);
		
		for (int i = 0; i < cameras.size(); i++) {
			if (camera != cameras.get(i)) {
				cameras.get(i).render(g, camera);
			} else {
				g.setColor(Color.WHITE);
				g.drawString(cameras.get(i).toString(), 20, 20 + 20);
			}
		}
		
		axis.render(g, camera);
		//side.render(g, 640, 0);
		
		drawOnZZero(g);
		
		testshape.render(g, camera);

		//renderCircle(0,0, 100,
			//		100, 20, g);
		
		renderCylinder(0,0, 0, 10,
				100, 100, g);
				
		//new TempImage().render(g);		
	}

	// Draws a rectagnle at the same x, y of the screen but at z=0, works pretty well only if camera moves and
	// only XY angles chagnes, but readjusting the moust once the other angles change keeps the shape drawn
	// at the same location on screen, Need to take the other angles more into account.
	//Also need to rorate the other angles so the shaoe is always drawn parallel/perpendicular to the camera direction
	private void drawOnZZero(Graphics g) {//does near need to be taken into account?
		int width = 10;
		int height = 10;
		double component[] = camera.getUnitComponent();
		Coordinate draw = new Coordinate(camera.getX() + component[0] * ((0-camera.getZ()) / component[2]),
										 camera.getY() + component[1] * ((0-camera.getZ()) / component[2]),
										 camera.getZ() + component[2] * ((0-camera.getZ()) / component[2]));
		
		Coordinate c2 = new Coordinate(draw.getX(), draw.getY() + height, draw.getZ());
		Coordinate c3 = new Coordinate(draw.getX() + width, draw.getY() + height, draw.getZ());
		Coordinate c4 = new Coordinate(draw.getX() + width, draw.getY(), draw.getZ());
		
		Polygon test = new Polygon(Color.YELLOW, draw, c2, c3, c4);
		test.render(g, camera);
	}

	public void tick() {	
		
		//camera.tick(handler);
		
		for (int i = 0; i < cameras.size(); i++) {
			cameras.get(i).tick(handler, cameras.get(i) == camera);
		}
		
		
		for (int i = 0; i < shapes.size(); i++) {
			shapes.get(i).tick();
		}
		
		selectPoly(handler.mouseX, handler.mouseY);
		testshape.tick();
		
//		if (handler.keys[KeyEvent.VK_ESCAPE]) {
//			side.setDraw(!side.isDrawing());
//		}
	}

	/**
	 * Sometimes works weird because of the simple sorting technique
	 * @param x
	 * @param y
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
				if (o1.closestDist(camera) < o2.closestDist(camera)) {
					return 1;
				} else if (o1.closestDist(camera) > o2.closestDist(camera)) {
					return -1;
				} else {
					return 0;
				}
			}
		});

		for (int i = polys.size() - 1; i >= 0; i--) {
			if (polys.get(i).coordInside(x, y, camera)) {
				//side.setSelected(polys.get(i));
				selected = polys.get(i);
				//System.out.println("Selected: " + x + ", " + y  + ": "+ selected.getCoords().toString());
				return;
			}
		}
		selected = null;		
	}

	public void moveCamera(double angle) {
		camera.move(angle);
	}

	public void scollCamera(double dir) {
		camera.scrollCamera(dir);
	}

	public void rotateCamera(double xdir, double ydir) {
		camera.rotateCamera(xdir, ydir);
	}

	public void click(int x, int y, MouseEvent e) {
//		if (!side.isDrawing() && SwingUtilities.isLeftMouseButton(e)) {
//			side.click(x, y);			
//		}
	}

	public List<Camera> getCameras() {
		return cameras;
	}
	
	public void setCamera(Camera cam) {
		this.camera = cam;
	}
	
	
	private void renderCircle(double x, double y, double z, double rad , double n, Graphics g) {
		//Temp render Circle method
		//x = 0;
		//y = 0;
		List<Coordinate> tempCords = new ArrayList<>();
		
		for (Integer i = 0; i < n; i++) {
			
			double x2 = x + Math.cos((2 * Math.PI / n) * (float)i) * rad;
			double y2 = y + Math.sin((2 * Math.PI / (float)n) * (float)i) * rad;
			tempCords.add(new Coordinate(x2, y2, z));
		}
		
		Polygon pp = new Polygon(Color.GREEN, tempCords);
		
		pp.render(g, camera);
	}
	
	//take in a color
	private void renderCylinder(double x, double y, double z, double rad,  double height , double n ,Graphics g) {
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
			pp.render(g, camera, false);
		}
		
		
		renderCircle(x, y, z + height, rad, n, g);
		
		
		
		
		
	}
	
	
	
	private void populateShapes() {
		
		shapes.add(new Shape(new Polygon(Color.RED, new Coordinate(0+100,0,10),
													 new Coordinate(0+100,100,10),
													 new Coordinate(100+100,100,10),
													 new Coordinate(100+100,0,10))));
		
		//populate the floor
		Shape floorShape = new Shape();
		boolean flag = true;
		for (int i = 0; i < 10; i++) {//Lags at 50x50
			for (int j = 0; j < 10; j++) {
				if (flag) {
					floorShape.addPoly(new Polygon(i*100, j* 100, 0, 100, 100, new Color(60, 60, 60)));
				} else {
					floorShape.addPoly(new Polygon(i*100, j* 100, 0, 100, 100, new Color(37, 37, 37)));
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
