package engine;

import java.awt.Color;
import java.awt.Graphics;
import java.util.List;

import entities.Mesh;
import entities.Triangle;

public class RenderHandler {
	
	private Screen theScreen;
	
	
	
	private int x = 200;
	private int y = 200;
	
	private int count = 640;
	private int increment = 1;
	
	
	public RenderHandler(Screen theScreen) {
		this.theScreen = theScreen;
	}
	
	
	public void render(Graphics g) {
		
		count += increment;
		if (count > 640 || count < 0) {
			increment *= -1;
		}
		
		/*
		double rad = 100;
		//x = (this.x + 1) % 640;
		//y = (this.y + 1) % 640;
		
		int n = 100;
		
		
		count = (count + 1) % n;
		
		Coordinate center = new Coordinate(x , y , 0);
		
		for (int i = 0; i < n && i < count; i++) {
			double x2 = x + Math.cos(2 * Math.PI / n * i) * rad;
			double y2 = y + Math.sin((2 * Math.PI / n) * i) * rad;
			Coordinate c2 = new Coordinate(x2, y2, 0);
			
			drawLine3D(c2, center, Color.YELLOW);
			
			//drawLine3DCustom(c2, center, Color.YELLOW);
			//drawLine3DCustom(center, c2, Color.BLUE);
		}
		
		
		drawLine3DCustom(new Coordinate(0, 0, 0), 
						new Coordinate(640, 640, 0),
						Color.BLUE);
		
		*/
		
		//draw the screen
		theScreen.render(g);
		//rest screen
		theScreen.reset();
	}
	

	
	private double sign(Coordinate c1, Coordinate c2, Coordinate c3) {
		return (c1.getX() - c3.getX()) * (c2.getY() - c3.getY()) - (c2.getX() - c3.getX()) * (c1.getY() - c3.getY());
	}
	
	
	private boolean inbound(final Coordinate theCoord, final Coordinate c1, final Coordinate c2, final Coordinate c3) {
		double d1, d2, d3;
		boolean has_neg, has_pos;

		d1 = sign(theCoord, c1, c2);
		d2 = sign(theCoord, c2, c3);
		d3 = sign(theCoord, c3, c1);

		has_neg = (d1 < 0) || (d2 < 0) || (d3 < 0);
		has_pos = (d1 > 0) || (d2 > 0) || (d3 > 0);

		return !(has_neg && has_pos);
	}
	
	
	
	public void setScreenColor(int xPos, int yPos, Color color) {
		theScreen.setScreenColor(xPos, yPos, color);
	}
	
	
	public void setScreenColor3D(int xPos, int yPos, int zPos, Color color) {
		theScreen.setScreenColor3D(xPos, yPos, zPos, color);
	}
	
	
	
	/**
	 * Used to avoid drawing too large shapes
	 *
	 * @param c1
	 * @return
	 */
	private boolean coordInbond(Coordinate c1) {
		if (c1.getX() >= -1000 && c1.getX() <= 1000 && c1.getY() >= -1000 && c1.getY() <= 1000) {
			return true;
		} else {
			return false;
		}
	}
	
	public void renderTriangle(Triangle triangle, Camera cam) {
		renderTriangle(triangle, cam, triangle.getColor());
	}
	
	public void renderTriangle(Triangle triangle, Camera cam, Color color) {
		Coordinate[] coords = triangle.getCoordinate();
		
		Coordinate c1 = Calculator.rotateAroundCamera(coords[0], cam);
		Coordinate c2 = Calculator.rotateAroundCamera(coords[1], cam);
		Coordinate c3 = Calculator.rotateAroundCamera(coords[2], cam);
		
		//drawTriangle3DCorner(c1, c2, c3, color);
		drawTriangleScanLine(c1, c2, c3, color);
		
		drawLine3D(c1, c2, Color.BLACK);
		drawLine3D(c2, c3, Color.BLACK);
		drawLine3D(c3, c1, Color.BLACK);
		
		
	}
	
	/**
	 * Current approach is to draw a line from c3 to all points between c1 and c2. Because these lines are angles, some pixels are skipped
	 * 
	 * Better approach would be to only draw horizontal/straight lines. Maybe split the triangle to make drawing easier
	 * 
	 * @param c1
	 * @param c2
	 * @param c3
	 * @param color
	 */
	public void drawTriangle3D(final Coordinate c1, final Coordinate c2, final Coordinate c3, final Color color) {
		if (!coordInbond(c1) || !coordInbond(c2) || !coordInbond(c3)) {
			return;
		}
		
		Coordinate left;
		Coordinate right;
		Coordinate top;
		Coordinate bottom;
		
		if (c1.getX() < c2.getX()) {
			left = c1;
			right = c2;
		} else {
			left = c2;
			right = c1;
		}
		
		if (c1.getY() < c2.getY()) {
			top = c1;
			bottom = c2;
		} else {
			top = c2;
			bottom = c1;
		}
		
		double slope = ( (right.getY() - left.getY()) / (right.getX() - left.getX()) );
		double iSlope =  ( (top.getX() - bottom.getX()) / (top.getY() - bottom.getY()) );
		
		double b = left.getY() - (slope * left.getX());
		double ib = top.getX() - (iSlope * top.getY());
		
		
		if (slope >= 1 || slope <= -1) {
			//iterate along y values		

			
			
			double vx = top.getX() - bottom.getX();
			double vy = top.getY() - bottom.getY();
			double vz = top.getZ() - bottom.getZ();
			
			
			for (int y = (int)top.getY(); y <= bottom.getY(); y++) {
				int theX = (int)(y * iSlope + ib);//(y-b)/m
				int theY = y;
				int theZ = (int)(left.getZ() + vz * (y - left.getY())/vy);
				
				drawLine3D(c3, new Coordinate(theX, theY, theZ), color);
				
			}
		} else {
			//iterate along x values
			double vx = left.getX() - right.getX();
			double vy = left.getY() - right.getY();
			double vz = left.getZ() - right.getZ();
			
			
			for (int x = (int)left.getX(); x <= right.getX(); x++) {
				int theX = x;
				int theY = (int)(x * slope + b);
				//int theZ = (int)(left.getX() + v1x * (x - left.getX())/v1x);
				int theZ = (int)(left.getZ() + vz * (x - left.getX())/vx);
				
				drawLine3D(c3, new Coordinate(theX, theY, theZ), color);
			}
		}

	}	
	
	
	
	public void drawMesh(Mesh theMesh, Camera cam, Color color) {
		List<Integer> indicies = theMesh.getIndices();
		
		for (int i = 0; i < indicies.size(); i+=3) {
			Coordinate c1 = theMesh.getCoordinate(indicies.get(i));
			Coordinate c2 = theMesh.getCoordinate(indicies.get(i + 1));
			Coordinate c3 = theMesh.getCoordinate(indicies.get(i + 2));
			
			
			
			
			
			//rotate around center
			//c1 = Calculator.rotateAroundCenter(c1, new Coordinate(0,0,0), count, count, count);
			//c2 = Calculator.rotateAroundCenter(c2, new Coordinate(0,0,0), count, count, count);
			//c3 = Calculator.rotateAroundCenter(c3, new Coordinate(0,0,0), count, count, count);
		
			//translate
			/*
			c1.addX(count);
			c1.addY(count);
			c1.addZ(count);
			
			c2.addX(count);
			c2.addY(count);
			c2.addZ(count);
			
			c3.addX(count);
			c3.addY(count);
			c3.addZ(count);
			*/
			
			//rotate around camera
			//c1 = Calculator.rotateAroundCamera(c1, cam);
			//c2 = Calculator.rotateAroundCamera(c2, cam);
			//c3 = Calculator.rotateAroundCamera(c3, cam);
						
			renderTriangle(new Triangle(c1, c2, c3, color), cam);
			
			//drawTriangleScanLine(c1, c2, c3, Color.WHITE);
			//draw(c1, c2, c3, Color.WHITE);
			//drawTriangle3DCorner(c1, c2, c3, Color.RED);
		}
	}
		
	
	/**
	 * 
	 * x_start = math.max(left.x, 0) and x_end = math.min(rigth.x, screen.width) (same with y)
	 * 
	 * 
	 * @param c1
	 * @param c2
	 * @param color
	 */
	public void drawLine3DCustom(final Coordinate c1, final Coordinate c2, final Color color) {
		Coordinate left, right, top, bottom;

		if (c1.getX() < c2.getX()) {
			left = c1;
			right = c2;
		} else {
			left = c2;
			right = c1;
		}
		
		if (c1.getY() < c2.getY()) {
			top = c1;
			bottom = c2;
		} else {
			top = c2;
			bottom = c1;
		}
		
		double y_start = Math.max(top.getY(), 0);
		double y_end = Math.min(bottom.getY(), theScreen.getHeight());
		
		double x_start = Math.max(left.getX(), 0);
		double x_end = Math.min(right.getX(), theScreen.getWidth());
		
		if (!(y_start < y_end && x_start < x_end)) {
			return;
		}
       
		double vx = (c1.getX() - c2.getX());
		double vy = (c1.getY() - c2.getY());
		double vz = (c1.getZ() - c2.getZ());
		
		if (vx <= vy) {
			//iterate along x
			for (int x = (int)x_start; x < x_end; x++) {
            	int theY = (int)(c1.getY() + vy * (x - c1.getX())/vx);
            	int theZ = (int)(c1.getZ() + vz * (x - c1.getX())/vx);
            	setScreenColor3D(x, theY, theZ, color);
			}
		} else {
			//iterate along y
			for (int y = (int)y_start; y < y_end; y++) {
            	int theX = (int)(c1.getX() + vx * (y - c1.getY())/vy);
            	int theZ = (int)(c1.getZ() + vz * (y - c1.getY())/vy);
            	setScreenColor3D(theX, y, theZ, color);
			}
		}
		
		
		
	}
	
	
	/**
	 * 
	 * x_start = math.max(left.x, 0) and x_end = math.min(rigth.x, screen.width) (same with y)
	 * 
	 * draw if x_start, x_end, y_start, y_end is within bounds (or x_start <= x_end and y_start <= y_end)
	 * 
	 * 
	 * @param c1
	 * @param c2
	 * @param color
	 */
	public void drawLine3D(final Coordinate c1, final Coordinate c2, final Color color) {			
		if (!coordInbond(c1) || !coordInbond(c2)) {
			return;
		}
		
 		int x1 = (int)c1.getX();
		int x2 = (int)c2.getX();
		
		int y1 = (int)c1.getY();
		int y2 = (int)c2.getY(); 
		
        // delta of exact value and rounded value of the dependent variable
        int d = 0;
 
        int dx = Math.abs(x2 - x1);
        int dy = Math.abs(y2 - y1);
 
        int dx2 = 2 * dx; // slope scaling factors to
        int dy2 = 2 * dy; // avoid floating point
 
        int ix = x1 < x2 ? 1 : -1; // increment direction
        int iy = y1 < y2 ? 1 : -1;
 
        int x = x1;
        int y = y1;
 
        if (dx >= dy) {
            while (true) {
            	double theZ = getZatX(c1, c2, x);
            	setScreenColor3D(x, y, (int)Math.floor(theZ), color);
            	
                if (x == x2)
                    break;
                x += ix;
                d += dy2;
                if (d > dx) {
                    y += iy;
                    d -= dx2;
                }
            }
        } else {
            while (true) {
            	double theZ = getZatY(c1, c2, y);
            	setScreenColor3D(x, y, (int)Math.floor(theZ), color);
            	
                if (y == y2)
                    break;
                y += iy;
                d += dx2;
                if (d > dy) {
                    x += ix;
                    d -= dy2;
                }
            }
        }
	}
	
	
	/**
	 * Iterate on x coords or y coords depending on the slope of the line
	 * 
	 * update to only draw inbound and check if the line would cross
	 * 
	 * @param c1
	 * @param c2
	 * @param color
	 */
	public void drawLine(final Coordinate c1, final Coordinate c2, final Color color) {	
		if (!coordInbond(c1) || !coordInbond(c2)) {
			return;
		}		
		
		int x1 = (int)c1.getX();
		int x2 = (int)c2.getX();
		
		int y1 = (int)c1.getY();
		int y2 = (int)c2.getY();
		
		
        // delta of exact value and rounded value of the dependent variable
        int d = 0;
 
        int dx = Math.abs(x2 - x1);
        int dy = Math.abs(y2 - y1);
 
        int dx2 = 2 * dx; // slope scaling factors to
        int dy2 = 2 * dy; // avoid floating point
 
        int ix = x1 < x2 ? 1 : -1; // increment direction
        int iy = y1 < y2 ? 1 : -1;
 
        int x = x1;
        int y = y1;
 
        if (dx >= dy) {
            while (true) {
            	double vz = c1.getZ() - c2.getZ();
            	double vx = c1.getX() - c2.getX();
            	int theZ = (int)(c1.getZ() + vz * (x - c1.getX())/vx);
      	
            	setScreenColor3D(x, y, theZ, color);
            	//setScreenColor(x, y, color);
                if (x == x2)
                    break;
                x += ix;
                d += dy2;
                if (d > dx) {
                    y += iy;
                    d -= dx2;
                }
            }
        } else {
            while (true) {
            	double vz = c1.getZ() - c2.getZ();
            	double vy = c1.getY() - c2.getY();
            	int theZ = (int)(c1.getZ() + vz * (y - c1.getY())/vy);

            	setScreenColor3D(x, y, theZ, color);
            	//setScreenColor(x, y, color);
                if (y == y2)
                    break;
                y += iy;
                d += dx2;
                if (d > dy) {
                    x += ix;
                    d -= dy2;
                }
            }
        }
		
		
	}
	
	
	/**
	 * Used proper double comparison
	 * 
	 * 
	 * http://www.sunshine2k.de/coding/java/TriangleRasterization/TriangleRasterization.html
	 * @param c1
	 * @param c2
	 * @param c3
	 * @param color
	 */
	public void drawTriangleScanLine(Coordinate c1, Coordinate c2, Coordinate c3, Color color) {
		if (!coordInbond(c1) || !coordInbond(c2) || !coordInbond(c3)) {
			return;
		}
		Coordinate v1 = c1;
		Coordinate v2 = c2;
		Coordinate v3 = c3;	
		
		//at first sort the three vertices by y-coordinate ascending so v1 is the topmost vertex		
		if (v1.getY() > v2.getY()) {
			Coordinate temp = v1;
			v1 = v2;
			v2 = temp;
		}
		
		if (v2.getY() > v3.getY()) {
			//swap c1, c3
			Coordinate temp = v2;
			v2 = v3;
			v3 = temp;
		}
		
		if (v1.getY() > v2.getY()) {
			//swap v1, v2
			Coordinate temp = v1;
			v1 = v2;
			v2 = temp;
		}
		
		// check for trivial case of bottom-flat triangle
		if (v2.getY() == v3.getY()) {
			//make v1 one the left?
			renderTriangleFlatBottom(v1, v2, v3, color);
		} else if (v1.getY() == v2.getY()) {
			// check for trivial case of top-flat triangle
			//make v2 on the left?
			renderTriangleFlatTop(v1, v2, v3, color);
		} else {
			//general case - split the triangle in a topflat and bottom-flat one
        	double theZ = getZatY(v1, v3, v2.getY());
			Coordinate v4 = new Coordinate((v1.getX() + ((v2.getY() - v1.getY()) / (v3.getY() - v1.getY())) * (v3.getX() - v1.getX())), v2.getY(), theZ);

			renderTriangleFlatBottom(v1, v2, v4, color);
			renderTriangleFlatTop(v2, v4, v3, color);
		}
		  
	}
	
	/**
	 * This might not be working as it should... causes weird clipping
	 * @param c1
	 * @param c2
	 * @param c3
	 * @param color
	 */
	private void renderTriangleFlatBottom(Coordinate c1, Coordinate c2, Coordinate c3, Color color) {
		double invslope1 = (c2.getX() - c1.getX()) / (c2.getY() - c1.getY());
		double invslope2 = (c3.getX() - c1.getX()) / (c3.getY() - c1.getY());

		double curx1 = c1.getX();
		double curx2 = c1.getX();

		for (int scanlineY = (int) c1.getY(); scanlineY <= c2.getY(); scanlineY++) {	
			double theZ1 = getZatY(c2, c1, scanlineY);
        	double theZ2 = getZatY(c3, c1, scanlineY);
 
			drawLine3D( new Coordinate(curx1, scanlineY, theZ1), new Coordinate (curx2, scanlineY, theZ2), color );
			curx1 += invslope1;
			curx2 += invslope2;
		}
	}
	
	private void renderTriangleFlatTop(Coordinate c1, Coordinate c2, Coordinate c3, Color color) {
		double invslope1 = (c3.getX() - c1.getX()) / (c3.getY() - c1.getY());
		double invslope2 = (c3.getX() - c2.getX()) / (c3.getY() - c2.getY());

		double curx1 = c3.getX();
		double curx2 = c3.getX();

		for (int scanlineY = (int) c3.getY(); scanlineY > c1.getY(); scanlineY--) {
			double theZ1 = getZatY(c1, c3, scanlineY);
        	double theZ2 = getZatY(c2, c3, scanlineY);
			
        	drawLine3D( new Coordinate(curx1, scanlineY, theZ1),  new Coordinate (curx2, scanlineY, theZ2), color );
			curx1 -= invslope1;
			curx2 -= invslope2;
		} 
	}
	
	private double getZatY(Coordinate c1, Coordinate c2, double theY) {
		double vz = c1.getZ() - c2.getZ();
    	double vy = c1.getY() - c2.getY();
    	double theZ = (c1.getZ() + vz * (theY - c1.getY())/vy);
    	
		return theZ;
	}
	
	private double getZatX(Coordinate c1, Coordinate c2, double theX) {
    	double vz = c1.getZ() - c2.getZ();
    	double vx = c1.getX() - c2.getX();
    	
    	
    	
    	double theZ = (c1.getZ() + vz * (theX - c1.getX())/vx); 
    	
    	return theZ;
	}
	
	
	
	/**
	 * Custom draw triangle algorithm that iterate a line from c1 to c2 using Bresenham's line algorithm
	 * and draws line to c3
	 * 
	 * @param c1
	 * @param c2
	 * @param c3
	 * @param color
	 */
	public void drawTriangle3DCorner(Coordinate c1, Coordinate c2, Coordinate c3, Color color) {	
		if (!coordInbond(c1) || !coordInbond(c2) || !coordInbond(c3)) {
			return;
		}
		int x1 = (int)c1.getX();
		int x2 = (int)c2.getX();
		
		int y1 = (int)c1.getY();
		int y2 = (int)c2.getY();
		
        int d = 0;
        
        int dx = Math.abs(x2 - x1);
        int dy = Math.abs(y2 - y1);
 
        int dx2 = 2 * dx; // slope scaling factors to
        int dy2 = 2 * dy; // avoid floating point
 
        int ix = x1 < x2 ? 1 : -1; // increment direction
        int iy = y1 < y2 ? 1 : -1;
 
        int x = x1;
        int y = y1;
 
        if (dx >= dy) {
            while (true) {
            	double theZ = getZatX(c1, c2, x);
            	
            	//drawLine3DCustom(c3, new Coordinate(x, y, theZ), color);
            	drawLine3D(c3, new Coordinate(x, y, theZ), color);
                if (x == x2)
                    break;
                x += ix;
                d += dy2;
                if (d > dx) {
                    y += iy;
                    d -= dx2;
                }
            }
        } else {
            while (true) {
            	double theZ = getZatY(c1, c2, y);
            	
            	//drawLine3DCustom(c3, new Coordinate(x, y, theZ), color);
            	drawLine3D(c3, new Coordinate(x, y, theZ), color);
                if (y == y2)
                    break;
                y += iy;
                d += dx2;
                if (d > dy) {
                    x += ix;
                    d -= dy2;
                }
            }
        }
	}
	
}
