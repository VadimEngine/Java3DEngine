package entities;

import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import engine.Calculator;
import engine.Camera;
import engine.Coordinate;

/**
 * A test Sphere shape build with N sides
 * @author Vadim
 *
 */
public class TestShape {
	
	private double xyAngle;
	private double zyAngle;//0 points directly up
	private double xzAngle;
	
	private double height;
	private Coordinate center;
	
	private List<Coordinate> vertices = new ArrayList<>();
	
	private List<Polygon> polys = new ArrayList<>();
	
	int faces;
	
	//vertix, faces, vector direction
	
	public TestShape(Coordinate center, final int faces, double height, double xyAngle,
												   double zyAngle,
												   double xzAngle) {
		this.xyAngle = xyAngle;
		this.zyAngle = zyAngle;
		this.xzAngle = xzAngle;
		this.height = height;
		this.center = center;
		
		if (faces < 4) {
			this.faces = 4;
		} else {
			this.faces = faces;
		}
		
		
		fillVertex();
		
	}
	
	public void tick() {
		for (int i = 0; i < vertices.size(); i++) {
			Coordinate rot = Calculator.rotateAroundCenter(vertices.get(i), center, 1, 1, 1);
			vertices.set(i, rot);
		}
		rotateAroundCenter(center, 1,1,1);
	}
	
	public void render(Graphics g, Camera cam) {
		
		Coordinate rotCenter = Calculator.rotateAroundCamera(center, cam);
		g.setColor(Color.YELLOW);
		
		for (int i = 0; i < vertices.size(); i++) {
			Coordinate rot = Calculator.rotateAroundCamera(vertices.get(i), cam);
			g.drawLine((int)rotCenter.getX(), (int)rotCenter.getY(), (int)rot.getX(), (int)rot.getY());
		}
		
		
		
		Collections.sort(polys, new Comparator<Polygon>() {
			@Override
			public int compare(Polygon o1, Polygon o2) {
				if (o1.closestDist(cam) < o2.closestDist(cam)) {
					return 1;
				} else if (o1.closestDist(cam) > o2.closestDist(cam)) {
					return -1;
				} else {
					return 0;
				}
			}

		});

		
		
		
		
		for (int i = 0; i < polys.size(); i++) {
			polys.get(i).render(g, cam);
		}
		
	}
	
	private void fillVertex() {
		/* tetrahedral 4 vertices, 4 sides
		 * for n=4, top = (0,0,1), and the rest of the points are top but rotated down ~109.5 (xz) and around 120(xy) and 240(xy)
		//top
		Coordinate top = new Coordinate(center.getX(),
									center.getY(),
									center.getZ() + (1 * 10.0));
		//+x y=0?
		Coordinate one = new Coordinate(center.getX() + (Math.sqrt(8.0/9) * 10.0),
									center.getY(),
									center.getZ() +  (-1.0/3.0 * 10.0));
		//-x +y
		Coordinate two = new Coordinate(center.getX() - (Math.sqrt(2.0/9.0) * 10.0),
									center.getY() + (Math.sqrt(2.0/3.0)* 10.0),
									center.getZ() + (-1.0/3.0 * 10.0));
		//-x -y
		Coordinate three = new Coordinate(center.getX() - (Math.sqrt(2.0/9.0)* 10.0),
									center.getY() - (Math.sqrt(2.0/3.0)* 10.0),
									center.getZ() + (-1.0/3.0 * 10.0) );
		
		vertices.add(top);
		vertices.add(one);
		vertices.add(two);
		vertices.add(three);
		
		
		polys.add(new Polygon(Color.yellow, top, one, two));
		polys.add(new Polygon(Color.yellow, top, two, three));
		polys.add(new Polygon(Color.yellow, top, one, three));
		polys.add(new Polygon(Color.yellow, one, two, three));
		*/
		
		/*
		//octahedral n = 6 vertices, 8 sides
		Coordinate top = new Coordinate(center.getX(),
				center.getY(),
				center.getZ() + (1 * 10.0));
		Coordinate bottom = new Coordinate(center.getX(),
				center.getY(),
				center.getZ() + (-1 * 10.0));
		
		//redo, not y+10 x + 10 because of trig
		Coordinate one = new Coordinate(center.getX() + 10,
				center.getY(),
				center.getZ() );
		Coordinate two = new Coordinate(center.getX(),
				center.getY() - 10,
				center.getZ() );
		Coordinate three = new Coordinate(center.getX() - 10,
				center.getY(),
				center.getZ() );
		Coordinate four = new Coordinate(center.getX(),
				center.getY() + 10,
				center.getZ() );
		
		vertices.add(top);
		vertices.add(one);
		vertices.add(two);
		vertices.add(three);
		vertices.add(four);
		vertices.add(bottom);
		
		polys.add(new Polygon(Color.yellow, top, one, two));
		polys.add(new Polygon(Color.yellow, top, two, three));
		polys.add(new Polygon(Color.yellow, top, three, four));
		polys.add(new Polygon(Color.yellow, top, four, one));
		
		polys.add(new Polygon(Color.yellow, bottom, one, two));
		polys.add(new Polygon(Color.yellow, bottom, two, three));
		polys.add(new Polygon(Color.yellow, bottom, three, four));
		polys.add(new Polygon(Color.yellow, bottom, four, one));
		*/
		//next...
		//(+-1,0,+-t), (0,+-t,+-1), (+-t,+-1,0), t=(-1+5^.5)/2 (golden ratio)
		//20 sides, 2 vertices
		
		double t = (-1.5 + Math.sqrt(5))/2;
		Coordinate one = new Coordinate(center.getX() + (1.0*10.0),
				center.getY(),
				center.getZ() + (t * 10.0));//upper
		Coordinate two = new Coordinate(center.getX() - (1.0*10.0),
				center.getY(),
				center.getZ() + (t * 10.0));//upper
		Coordinate three = new Coordinate(center.getX() + (1.0*10.0),
				center.getY(),
				center.getZ() - (t * 10.0));//lower
		Coordinate four = new Coordinate(center.getX() - (1.0*10.0),
				center.getY(),
				center.getZ() - (t * 10.0));//lower
		
		Coordinate five = new Coordinate(center.getX(),
				center.getY() + (t * 10.0),
				center.getZ() + (1 * 10.0));//upper
		Coordinate six = new Coordinate(center.getX(),
				center.getY() - (t * 10.0),
				center.getZ() + (1 * 10.0));//upper
		Coordinate seven = new Coordinate(center.getX(),
				center.getY() + (t * 10.0),
				center.getZ() - (1 * 10.0));//lower
		Coordinate eight = new Coordinate(center.getX(),
				center.getY() - (t * 10.0),
				center.getZ() - (1 * 10.0));//lower
		
		Coordinate nine = new Coordinate(center.getX()  + (t * 10.0),//+x
				center.getY() + (1 * 10.0),
				center.getZ());//middle
		Coordinate ten = new Coordinate(center.getX()  - (t * 10.0),//-x
				center.getY() + (1 * 10.0),
				center.getZ());//middle
		Coordinate eleven = new Coordinate(center.getX()  + (t * 10.0),
				center.getY() - (1 * 10.0),
				center.getZ());//middle
		Coordinate twelve = new Coordinate(center.getX()  - (t * 10.0),
				center.getY() - (1 * 10.0),
				center.getZ());//middle
		
		vertices.add(one);
		vertices.add(two);
		vertices.add(three);
		vertices.add(four);
		vertices.add(five);
		vertices.add(six);
		vertices.add(seven);
		vertices.add(eight);
		vertices.add(nine);
		vertices.add(ten);
		vertices.add(eleven);
		vertices.add(twelve);
		
		//1, 2, 5, 6, 9, 10		//CENTER: five
		polys.add(new Polygon(Color.WHITE, five, nine, ten));//up
		polys.add(new Polygon(Color.WHITE, five, ten, two));//right
		polys.add(new Polygon(Color.RED, five, two, six));
		polys.add(new Polygon(Color.RED, five, six, one));
		polys.add(new Polygon(Color.WHITE, five, one, nine));//left
		
		// 3, 4, 7, 8, 11, 12 		//CENTER: Eight
		polys.add(new Polygon(Color.RED, eight, twelve, eleven));
		polys.add(new Polygon(Color.RED, eight, eleven, three));
		polys.add(new Polygon(Color.BLUE, eight, three, seven));
		polys.add(new Polygon(Color.BLUE, eight, seven, four));
		polys.add(new Polygon(Color.CYAN, eight, four, twelve));
		
		//partial Center: Seven
		polys.add(new Polygon(Color.WHITE, nine, ten, seven));
		polys.add(new Polygon(Color.BLUE, four, ten, seven));//btm
		polys.add(new Polygon(Color.BLUE, three, nine, seven));//btm;
		
		//partial Center: SIX
		polys.add(new Polygon(Color.RED, twelve, eleven, six));
		polys.add(new Polygon(Color.CYAN, two, twelve, six));
		polys.add(new Polygon(Color.GREEN, six, eleven, one));//one of these
		
		//FINAL 4 FACES
		polys.add(new Polygon(Color.YELLOW, two , ten, four));//middle right
		polys.add(new Polygon(Color.YELLOW, one, three, nine));//middle left
		// 2 4  , 1 3
		polys.add(new Polygon(Color.ORANGE, twelve, two, four));//middle left
		polys.add(new Polygon(Color.ORANGE, eleven, one, three));
	}

	
	private void rotateAroundCenter(Coordinate center, double XYAngle, double XZAngle, double ZYAngle) {
		for (int i = 0; i < polys.size(); i++) {
			List<Coordinate> newCords = new ArrayList<>();
			for (int j = 0; j < polys.get(i).getCoords().size(); j++) {
				Coordinate rotated = Calculator.rotateAroundCenter(polys.get(i).getCoords().get(j), center, XYAngle, XZAngle, ZYAngle);
				newCords.add(rotated);
			}
			polys.set(i, new Polygon(polys.get(i).getColor(), newCords));
		}
	}
	
}
