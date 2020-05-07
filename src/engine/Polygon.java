package engine;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Path2D;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;

/**
 * A polygon that build by 3 or more coordinates and can be rendered.
 * @author Vadim
 *
 */
public class Polygon {
		
	private List<Coordinate> coords = new ArrayList<>();
	private Color color;
	
	private int testX = 0;//used as a test way to move shape
	private boolean testMove;
	
	/**
	 * Constructor that sets the color and polygon vertices 
	 * 
	 * @param color The color of the polygon
	 * @param coords Vertexes of the polygon
	 */
	public Polygon(Color color, List<Coordinate> coords) {
		this.color = color;
		for (int i = 0; i < coords.size(); i++) {
			this.coords.add(coords.get(i));
		}
	}
	
	/**
	 * Constructor that sets the color and polygon vertices 
	 * 
	 * @param color The color of the polygon
	 * @param coords Vertexes of the polygon
	 */
	public Polygon(Color color, Coordinate... coord) {//check for empty input
		this.color = color;
		for (int i = 0; i < coord.length; i++) {
			coords.add(coord[i]);
		}
	}
	
	/**
	 * Constructor that sets the color and polygon vertices 
	 * 
	 * @param color The color of the polygon
	 * @param move If the polgon moves
	 * @param coords Vertexes of the polygon
	 */
	public Polygon(Color color, boolean move, Coordinate... coord) {//check for empty input
		this.color = color;
		testMove = move;
		for (int i = 0; i < coord.length; i++) {
			coords.add(coord[i]);
		}
	}
	
	/**
	 * Rectangle polygon with Red Color
	 * 
	 * @param x Top left x coordinate
	 * @param y Top left y coordinate
	 * @param z Top left z coordinate
	 * @param width width of rectangle
	 * @param height radius of rectangle
	 */
	public Polygon(int x, int y, int z, int width, int height) {
		this(Color.RED, new Coordinate(x,y, z), new Coordinate(x + width, y, z),
				new Coordinate(x + width, y + height, z), new Coordinate(x, y + height, z));
	}
	
	/**
	 * Rectangle polygon
	 * 
	 * @param x Top left x coordinate
	 * @param y Top left y coordinate
	 * @param z Top left z coordinate
	 * @param width width of rectangle
	 * @param height radius of rectangle
	 * @param color Color of polygon
	 */
	public Polygon(int x, int y, int z, int width, int height, Color color) {
		this(color, new Coordinate(x,y, z), new Coordinate(x + width, y, z),
				new Coordinate(x + width, y + height, z), new Coordinate(x, y + height, z));
	}

	/**
	 * Moves the polygon if it is enabled
	 */
	public void tick() {
		if (testMove) {			
			testX = (testX + 1) % 100;
		}
	}

	/**
	 * Renders the polygon relative to the Camera that is viewing
	 * @param g the Application Graphics object
	 * @param c The Viewing Camera
	 */
	public void render(Graphics g, Camera c) {
		boolean oneInbound = false;
		Coordinate coords = this.coords.get(0);
		Coordinate rot = Calculator.rotateAroundCamera(coords, c);
				
		Path2D shape = new Path2D.Double();
		shape.moveTo(rot.getX(), rot.getY());
		
		if (rot.getX() >= 0 && rot.getX() <= 640 && rot.getY() >= 0 && rot.getY() <= 640) {
			oneInbound = true;
		}

		for (int i = 1; i <= this.coords.size(); i++) {
			coords = this.coords.get(i % this.coords.size());
			rot = Calculator.rotateAroundCamera(coords, c);
			if (rot.getX() >= 0 && rot.getX() <= 640 + 256 && rot.getY() >= 0 && rot.getY() <= 640 + 256) {
				oneInbound = true;
			}
			if (rot.getZ() < 0) {
				return;
			}
			shape.lineTo(rot.getX(), rot.getY());
		}

		if (oneInbound) {
			g.setColor(color);
			((Graphics2D) g).fill(shape);
			g.setColor(Color.BLACK);
			((Graphics2D) g).draw(shape);
		}

	}
	
	/**
	 * Renders the polygon relative to the Camera that is viewing with a set color
	 * @param g the Application Graphics object
	 * @param c The Viewing Camera
	 */
	public void render(Graphics g, Camera c, Color color) {
		Coordinate coords = this.coords.get(0);
		Coordinate rot = Calculator.rotateAroundCamera(coords, c);
				
		Path2D shape = new Path2D.Double();
		shape.moveTo(rot.getX(), rot.getY());

		for (int i = 1; i <= this.coords.size(); i++) {
			coords = this.coords.get(i % this.coords.size());
			rot = Calculator.rotateAroundCamera(coords, c);
			if (rot.getZ() < 0) {
				return;
			}
			shape.lineTo(rot.getX(), rot.getY());
		}
		g.setColor(color);
		((Graphics2D) g).fill(shape);
		g.setColor(Color.BLACK);
		((Graphics2D) g).draw(shape);
	}
	
	/**
	 * Renders the polygon relative to the Camera that is viewing with a boarder
	 * @param g the Application Graphics object
	 * @param c The Viewing Camera
	 */
	public void render(Graphics g, Camera c, boolean line) {
		Coordinate coords = this.coords.get(0);
		Coordinate rot = Calculator.rotateAroundCamera(coords, c);
				
		Path2D shape = new Path2D.Double();
		shape.moveTo(rot.getX(), rot.getY());

		for (int i = 1; i <= this.coords.size(); i++) {
			coords = this.coords.get(i % this.coords.size());
			rot = Calculator.rotateAroundCamera(coords, c);
			if (rot.getZ() < 0) {
				return;
			}
			shape.lineTo(rot.getX(), rot.getY());
		}
		g.setColor(color);
		((Graphics2D) g).fill(shape);
		if (line) {
			g.setColor(Color.BLACK);
			((Graphics2D) g).draw(shape);
		}

	}
	
	/**
	 * Used in a quick method to order the polygons by distance to camera so the closest ones get drawn last
	 * and therefore, on top of the further polygons. //redo to work with the rotated coords
	 * @param c
	 * @return
	 */
	public double closestDist(Camera c) {
		double dist = Double.MAX_VALUE;
		for (int i = 0; i < coords.size(); i++) {
			Coordinate coord = coords.get(i);
			double xdist = c.getX() - coord.getX();
			double ydist = c.getY() - coord.getY();
			double zdist = c.getZ() - coord.getZ();
			double dist2 = Math.sqrt( xdist*xdist + ydist*ydist + zdist*zdist );
			if (dist2 < dist) {
				dist = dist2;
			}
		}
		return dist;
	}
	
	/**
	 * Returns if the a 2d coordinate is within the polygon based on the Camera view
	 * 
	 * @param x The 2d X coordinate
	 * @param y The 2d Y coordinate
	 * @param c The Camera that is viewing
	 * @return
	 */
	public boolean coordInside(int x, int y, Camera c) {
		Path2D shape = new Path2D.Double();
		Coordinate coord = coords.get(0);
		Coordinate rotated = Calculator.rotateAroundCamera(coord, c);
		shape.moveTo(rotated.getX(), rotated.getY());
		for (int i = 1; i < coords.size(); i++) {
			coord = coords.get(i);
			rotated = Calculator.rotateAroundCamera(coord, c);
			shape.lineTo(rotated.getX(), rotated.getY());
			if (rotated.getZ() <= 0.1) {
				return false;
			}
		}
		return shape.contains(new Point2D.Double(x,y));
	}
	
	//Getters and Setters-----------------------------------------------------------------------------------------------
	
	public void setColor(Color color) {
		this.color = color;
	}
	
	public List<Coordinate> getCoords() {
		return coords;
	}

	public Color getColor() {
		return color;
	}

	//End of Getters and Setters----------------------------------------------------------------------------------------
}
