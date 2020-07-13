package engine;

import java.awt.Color;
import java.awt.Graphics;

import entities.Texture;
import entities.VertexTex;

public class RenderHandler {
	
	private Screen theScreen;
			
	public RenderHandler(Screen theScreen) {
		this.theScreen = theScreen;
	}
	
	
	public void render(Graphics g) {			
		//draw the screen
		theScreen.render(g);
		//rest screen
		theScreen.reset();
	}
	
	
	public void drawTriangleScanLineTex(VertexTex c0, VertexTex c1, VertexTex c2, Texture theTex) {
		if (!coordInbond(c0) || !coordInbond(c1) || !coordInbond(c2)) {
			return;
		}
		
		VertexTex pv0 = c0;
		VertexTex pv1 = c1;
		VertexTex pv2 = c2;	
		
		if( pv1.getY() < pv0.getY() ) {
			VertexTex temp = pv0;
			pv0 = pv1;
			pv1 = temp;
		}
		if( pv2.getY() < pv1.getY() ) {
			VertexTex temp = pv1;
			pv1 = pv2;
			pv2 = temp;
		}
		if( pv1.getY() < pv0.getY() ) {
			VertexTex temp = pv0;
			pv0 = pv1;
			pv1 = temp;
		}
		
		if( pv0.getY() == pv1.getY() )  { // natural flat top
			
			// sorting top vertices by x
			if( pv1.getX() < pv0.getX() ) {
				VertexTex temp = pv0;
				pv0 = pv1;
				pv1 = temp;
			}
			drawFlatTopTriangleTex( pv0, pv1, pv2, theTex);
		} else if ( pv1.getY() == pv2.getY()) { // natural flat bottom
			// sorting bottom vertices by x
			if( pv2.getX() < pv1.getX() ) {
				VertexTex temp = pv1;
				pv1 = pv2;
				pv2 = temp;
			}
			drawFlatBottomTriangleTex( pv0, pv1, pv2, theTex);
		} else {
			
			double alphaSplit = (pv1.getY() - pv0.getY()) /
								(pv2.getY() - pv0.getY());
									
			VertexTex vi = new VertexTex(pv0.getX() + (pv2.getX() - pv0.getX())  * alphaSplit,
									     pv0.getY() + (pv2.getY() - pv0.getY())  * alphaSplit,
									     pv0.getZ() + (pv2.getZ() - pv0.getZ())  * alphaSplit,
									     pv0.getTexX() + (pv2.getTexX() - pv0.getTexX()) * alphaSplit,
									     pv0.getTexY() + (pv2.getTexY() - pv0.getTexY()) * alphaSplit,
									     pv0.getTexture());
							
			if( pv1.getX() < vi.getX() ) { // major right
				drawFlatBottomTriangleTex( pv0, pv1, vi, theTex );
				drawFlatTopTriangleTex( pv1, vi, pv2, theTex );
			} else { // major left
				drawFlatBottomTriangleTex( pv0, vi, pv1, theTex );
				drawFlatTopTriangleTex( vi, pv1, pv2, theTex );
			}
		}
	}
	
	private void drawFlatTopTriangleTex(VertexTex v0, VertexTex v1, VertexTex v2, Texture theTex) {		
		double delta_y = v2.getY() - v0.getY();
		VertexTex dv0 = new VertexTex((v2.getX() - v0.getX()) / delta_y,
									  (v2.getY() - v0.getY()) / delta_y,
									  (v2.getZ() - v0.getZ()) / delta_y,
									  (v2.getTexX() - v0.getTexX()) / delta_y,
									  (v2.getTexY() - v0.getTexY()) / delta_y);
		
		VertexTex dv1 = new VertexTex((v2.getX() - v1.getX()) / delta_y,
									  (v2.getY() - v1.getY()) / delta_y,
									  (v2.getZ() - v1.getZ()) / delta_y,
									  (v2.getTexX() - v1.getTexX()) / delta_y,
									  (v2.getTexY() - v1.getTexY()) / delta_y);
		
		VertexTex itEdge1 = v1.clone();
		
		drawFlatTriangleTex(v0, v1, v2, theTex, dv0, dv1, itEdge1);
	}
	
	
	private void drawFlatBottomTriangleTex(VertexTex v0, VertexTex v1, VertexTex v2, Texture theTex) {		
		double delta_y = v2.getY() - v0.getY();
		VertexTex dv0 = new VertexTex((v1.getX()    - v0.getX()) / delta_y,
									  (v1.getY()    - v0.getY()) / delta_y,
									  (v1.getZ()    - v0.getZ()) / delta_y,
									  (v1.getTexX() - v0.getTexX()) / delta_y,
									  (v1.getTexY() - v0.getTexY()) / delta_y);
		
		VertexTex dv1 = new VertexTex((v2.getX()    - v0.getX()) / delta_y,
									  (v2.getY()    - v0.getY()) / delta_y,
									  (v2.getZ()    - v0.getZ()) / delta_y,
									  (v2.getTexX() - v0.getTexX()) / delta_y,
									  (v2.getTexY() - v0.getTexY()) / delta_y);
		
		VertexTex itEdge1 = v0.clone();
		
		drawFlatTriangleTex(v0, v1, v2, theTex, dv0, dv1, itEdge1);
	}
	
	
	
	private void drawFlatTriangleTex(VertexTex v0, VertexTex v1, VertexTex v2,
								Texture theTex, VertexTex dv0, VertexTex dv1, VertexTex itEdge1)  {
				
		VertexTex itEdge0 = v0.clone();
			
		// calculate start and end scanlines
		final int yStart = (int)Math.ceil( v0.getY() - 0.5f );
		final int yEnd = (int)Math.ceil( v2.getY() - 0.5f ); // the scanline AFTER the last line drawn
		
		itEdge0.add(new VertexTex(dv0.getX()    * ((double)yStart + 0.5f - v0.getY()),
								  dv0.getY()    * ((double)yStart + 0.5f - v0.getY()),
								  dv0.getZ()    * ((double)yStart + 0.5f - v0.getY()),
								  dv0.getTexX() * ((double)yStart + 0.5f - v0.getY()),
								  dv0.getTexY() * ((double)yStart + 0.5f - v0.getY())
								  ));
		
		itEdge1.add(new VertexTex(dv1.getX()    * ((double)yStart + 0.5f - v0.getY()),
								  dv1.getY()    * ((double)yStart + 0.5f - v0.getY()),
								  dv1.getZ()    * ((double)yStart + 0.5f - v0.getY()),
								  dv1.getTexX() * ((double)yStart + 0.5f - v0.getY()),
								  dv1.getTexY() * ((double)yStart + 0.5f - v0.getY())
								  ));
				
		for( int y = yStart; y < yEnd; y++, itEdge0.add(dv0), itEdge1.add(dv1) ) {
						
			// calculate start and end pixels
			final int xStart = (int)Math.ceil( itEdge0.getX() - 0.5f );
			final int xEnd = (int)Math.ceil( itEdge1.getX() - 0.5f ); // the pixel AFTER the last pixel drawn
			
			VertexTex dtcLine = new VertexTex(0,0, 0,
										      (itEdge1.getTexX() - itEdge0.getTexX()) / (itEdge1.getX() - itEdge0.getX()),
										      (itEdge1.getTexY() - itEdge0.getTexY()) / (itEdge1.getX() - itEdge0.getX()));
					
			VertexTex itcLine = new VertexTex(0,0,0,
											 itEdge0.getTexX() + dtcLine.getTexX() * ((float)xStart + 0.5f - itEdge0.getX()),
											 itEdge0.getTexY() + dtcLine.getTexY() * ((float)xStart + 0.5f - itEdge0.getX()));
			
					
			for( int x = xStart; x < xEnd; x++, itcLine.add(dtcLine)) {
				double theZ = Calculator.zOnPlane(v0, v1, v2, x, y);
				
				//System.out.println(theZ);
				
				VertexTex temp = itcLine.clone();
			
				//temp.multiply(theZ); //x,y *= z/(camera.getNear)
				//temp.multiply(theZ/1000.0f); //x,y *= z/(camera.getNear)
				if (temp.getTexX() > 1 || temp.getTexY() > 1) {
					//System.out.println(theZ + ": " + temp.getTexX() + ", " + temp.getTexY());					
				}
				//System.out.println(theTex);
				Color theColor = theTex.getColor(temp.getTexX(), temp.getTexY());
				theScreen.setScreenColor3D(x, y, (int) theZ, theColor);
			}
		}
		
	}
	
	
	
	public void drawTriangleScanLineOp(Coordinate c0, Coordinate c1, Coordinate c2, Color theColor) {
		if (!coordInbond(c0) || !coordInbond(c1) || !coordInbond(c2)) {
			return;
		}
		
		Coordinate pv0 = c0;
		Coordinate pv1 = c1;
		Coordinate pv2 = c2;	
		
		if( pv1.getY() < pv0.getY() ) {
			Coordinate temp = pv0;
			pv0 = pv1;
			pv1 = temp;
		}
		if( pv2.getY() < pv1.getY() ) {
			Coordinate temp = pv1;
			pv1 = pv2;
			pv2 = temp;
		}
		if( pv1.getY() < pv0.getY() ) {
			Coordinate temp = pv0;
			pv0 = pv1;
			pv1 = temp;
		}
		
		if( pv0.getY() == pv1.getY() )  { // natural flat top
			
			// sorting top vertices by x
			if( pv1.getX() < pv0.getX() ) {
				Coordinate temp = pv0;
				pv0 = pv1;
				pv1 = temp;
			}
			drawFlatTopTriangle( pv0, pv1, pv2, theColor);
		} else if ( pv1.getY() == pv2.getY()) { // natural flat bottom
			// sorting bottom vertices by x
			if( pv2.getX() < pv1.getX() ) {
				Coordinate temp = pv1;
				pv1 = pv2;
				pv2 = temp;
			}
			drawFlatBottomTriangle( pv0, pv1, pv2, theColor);
		} else {
			
			double alphaSplit = (pv1.getY() - pv0.getY()) /
								(pv2.getY() - pv0.getY());
									
			Coordinate vi = new Coordinate(  pv0.getX() + (pv2.getX() - pv0.getX())  * alphaSplit,
										     pv0.getY() + (pv2.getY() - pv0.getY())  * alphaSplit,
										     pv0.getZ() + (pv2.getZ() - pv0.getZ())  * alphaSplit);
							
			if( pv1.getX() < vi.getX() ) { // major right
				drawFlatBottomTriangle( pv0, pv1, vi, theColor );
				drawFlatTopTriangle( pv1, vi, pv2, theColor );
			} else { // major left
				drawFlatBottomTriangle( pv0, vi, pv1, theColor );
				drawFlatTopTriangle( vi, pv1, pv2, theColor );
			}
		}
	}
	
	
	private void drawFlatTopTriangle(Coordinate v0, Coordinate v1, Coordinate v2, Color theColor) {
		final double m0 = (v2.getX() - v0.getX()) / (v2.getY() - v0.getY());
		final double m1 = (v2.getX() - v1.getX()) / (v2.getY() - v1.getY());
		
		// calculate start and end scanlines
		final int yStart = (int)Math.ceil( v0.getY() - 0.5f );
		final int yEnd = (int)Math.ceil( v2.getY() - 0.5f ); // the scanline AFTER the last line drawn

		for( int y = yStart; y < yEnd; y++ ) {
			// calculate start and end points (x-coords)
			// add 0.5 to y value because we're calculating based on pixel CENTERS
			final double px0 = m0 * ((double)( y ) + 0.5f - v0.getY()) + v0.getX();
			final double px1 = m1 * ((double)( y ) + 0.5f - v1.getY()) + v1.getX();

			// calculate start and end pixels
			final int xStart = (int)Math.ceil( px0 - 0.5f );
			final int xEnd = (int)Math.ceil( px1 - 0.5f ); // the pixel AFTER the last pixel drawn

			for( int x = xStart; x < xEnd; x++ ) {
				double theZ = Calculator.zOnPlane(v0, v1, v2, x, y);
				theScreen.setScreenColor3D(x, y, (int) theZ, theColor);
			}
		}
	}
	
	private void drawFlatBottomTriangle(Coordinate v0, Coordinate v1, Coordinate v2, Color theColor) {
		final double m0 = (v1.getX() - v0.getX()) / (v1.getY() - v0.getY());
		final double m1 = (v2.getX() - v0.getX()) / (v2.getY() - v0.getY());
			
		// calculate start and end scanlines
		final int yStart = (int)Math.ceil( v0.getY() - 0.5f );
		final int yEnd = (int)Math.ceil( v2.getY() - 0.5f ); // the scanline AFTER the last line drawn
		
		for( int y = yStart; y < yEnd; y++ ) {
			// calculate start and end points (x-coords)
			// add 0.5 to y value because we're calculating based on pixel CENTERS
			final double px0 = m0 * ((double)( y ) + 0.5f - v0.getY()) + v0.getX();
			final double px1 = m1 * ((double)( y ) + 0.5f - v0.getY()) + v0.getX();

			// calculate start and end pixels
			final int xStart = (int)Math.ceil( px0 - 0.5f );
			final int xEnd = (int)Math.ceil( px1 - 0.5f ); // the pixel AFTER the last pixel drawn

			for( int x = xStart; x < xEnd; x++ ) {
				double theZ = Calculator.zOnPlane(v0, v1, v2, x, y);
				theScreen.setScreenColor3D(x, y, (int) theZ, theColor);
			}
		}
	}
	

	
	public void setScreenColor(int xPos, int yPos, Color color) {
		theScreen.setScreenColor(xPos, yPos, color);
	}
	
	
	public void setScreenColor3D(int xPos, int yPos, int zPos, Color color) {
		theScreen.setScreenColor3D(xPos, yPos, zPos, color);
	}
			
	
	/**
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
            	double theZ = Calculator.getZatX(c1, c2, x);
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
            	double theZ = Calculator.getZatY(c1, c2, y);
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
            	double theZ = Calculator.getZatX(c1, c2, x);
            	
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
            	double theZ = Calculator.getZatY(c1, c2, y);
            	
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
	
	/**
	 * Used to avoid drawing too large shapes
	 *
	 * @param c1
	 * @return
	 */
	private static boolean coordInbond(Coordinate c1) {
		if (c1.getX() >= -1000 && c1.getX() <= 1500 && c1.getY() >= -1000 && c1.getY() <= 1500) {
			return true;
		} else {
			return false;
		}
	}
	
	private static boolean coordInbond(VertexTex c1) {
		if (c1.getX() >= -1000 && c1.getX() <= 1500 && c1.getY() >= -1000 && c1.getY() <= 1500) {
			return true;
		} else {
			return false;
		}
	}
	
}
