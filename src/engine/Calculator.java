package engine;


/**
 * 
 * Utility class that handles graphics calculations. Used 3x3 Matrix multiplication to "rotate" polygons relative 
 * to a point.
 * 
 * Currently using right hand coordinate system
 * 
 * @author Vadim
 *
 */
public class Calculator {
	
	private static final int WIDTH = 640;
	private static final int HEIGHT = 640;
		
	private Calculator(){}	
	
	public static Coordinate rotateAroundCenter(Coordinate coord, Coordinate center, 
												double XYAngle, double XZAngle, double ZYAngle) {
		
		double cosXY = Math.cos(-(XYAngle) * Math.PI/180);
		double sinXY = Math.sin(-(XYAngle) * Math.PI/180);
		
		double cosZY = Math.cos(-(ZYAngle) * Math.PI/180);
		double sinZY = Math.sin((-ZYAngle) * Math.PI/180);
		
		double cosXZ = Math.cos(-(XZAngle) * Math.PI/180);
		double sinXZ = Math.sin(-(XZAngle) * Math.PI/180);
		
		double[] rot = new double[]{coord.getX() - center.getX(), coord.getY()- center.getY(), coord.getZ() - center.getZ()};
		
		double[][] xy = {{cosXY, -sinXY, 0 },
		 				 {sinXY, cosXY, 0},
		 				 {0, 0, 1}};	
		
		double[][] xz = {{cosXZ, 0, -sinXZ},
		 				{0, 1, 0},
		 				{sinXZ, 0, cosXZ}};
		
		double[][] zy = {{1, 0, 0 },
				 		{0, cosZY, sinZY},
				 		{0, -sinZY, cosZY}};
		
		rot = matrixMult(rot, zy);
		rot = matrixMult(rot, xy);
		rot = matrixMult(rot, xz);
		return new Coordinate(rot[0] + center.getX(), rot[1] + center.getY(), rot[2] + center.getZ());
	}
	
	
	/**
	 * Behaves differently than rotate around center. Could be related to the fact that rendering has y point opposite compared to
	 * standard Cartesian coordinates(down instead of up). Also Keeps the rotation radius different? If using rotate around center
	 * for the camera rotations then the shape will always eventually rotate into the camera. (Like the rotation center is between the camera
	 * and the coordinate instead of the just the camera)
	 * 
	 * Right hand rule coordinates and right hand rule rotations?
	 * 
	 * NEW NOTE
	 * Behaves different than rotateAroundCenter because rotateAround Camera rotates everything the opposite direction of the camera
	 * in order to simulate the camera rotating
	 * 
	 * 
	 * @param coord
	 * @param cam
	 * @return
	 */
	public static Coordinate rotateAroundCamera(Coordinate coord, Camera cam) {
		double cosXY = cam.getCosXY();
		double sinXY = cam.getSinXY();
		
		double cosZY = cam.getCosZY();
		double sinZY = cam.getSinZY();
		
		double cosXZ = cam.getCosXZ();
		double sinXZ = cam.getSinXZ();
		
		//Transalte
		double[] rot = new double[]{coord.getX() - cam.getX(), coord.getY()- cam.getY(), coord.getZ() - cam.getZ()};
		
		double[][] xy = {{cosXY, -sinXY, 0 },
		 				 {sinXY, cosXY, 0},
		 				 {0, 0, 1}};	
		
		double[][] xz = {{cosXZ, 0, -sinXZ},
		 				{0, 1, 0},
		 				{sinXZ, 0, cosXZ}};
		
		double[][] zy = {{1, 0, 0 },
				 		{0, cosZY, sinZY},
				 		{0, -sinZY, cosZY}};
		
		rot = matrixMult(rot, xy);
		rot = matrixMult(rot, xz);
		rot = matrixMult(rot, zy);
		
		rot[0] /= rot[2]/cam.getNear();
		rot[1] /= rot[2]/cam.getNear();
		rot[0] += WIDTH/2;//centers the rendering
		rot[1] += HEIGHT/2;//centers the rendering
		
		return new Coordinate(rot[0], rot[1], rot[2]);
	}
	
	//methods to generate rotational matrices
	
	public static double zOnPlane(Coordinate c1, Coordinate c2, Coordinate c3, double xPos, double yPos) {
		double x1 = c1.getX(), y1 = c1.getY(), z1 = c1.getZ();
		double x2 = c2.getX(), y2 = c2.getY(), z2 = c2.getZ();
		double x3 = c3.getX(), y3 = c3.getY(), z3 = c3.getZ();
		
		double a =  y1*(z2-z3) + y2*(z3-z1) + y3*(z1-z2);
		double b =  z1*(x2-x3) + z2*(x3-x1) + z3*(x1-x2);
		double c =  x1*(y2-y3) + x2*(y3-y1) + x3*(y1-y2);
		double d = -x1*((y2*z3)-(y3*z2)) - x2*((y3*z1)-(y1*z3)) - x3*((y1*z2)-(y2*z1));

        return (d-(a*xPos)-(b*yPos))/c;
	}
	
	
	
	public static double zOnPlane2(Coordinate c1, Coordinate c2, Coordinate c3, double x, double y) {
		double ax = c1.getX() - c2.getX();
		double ay = c1.getY() - c2.getY();
		double az = c1.getZ() - c2.getZ();
		
		double bx = c1.getX() - c3.getX();
		double by = c1.getY() - c3.getY();
		double bz = c1.getZ() - c3.getZ();
		
		double nx = (ay*bz) - (az*by);
		double ny = -((ax*bz) - (az*bx));
		double nz = (ax*by) - (ay*bx);
		
		double tempx = nx * (x - c1.getX());
		double tempy = ny * (y - c1.getY());
		double tempz = nz * c1.getZ();
		
		double z = ( (tempx) + (tempy) - (tempz)) / (-nz);
		
		return z;
	}
	
	
	
	private static double sign(Coordinate c1, Coordinate c2, double x, double y) {
		return (x - c2.getX()) * (c1.getY() - c2.getY()) - (c1.getX() - c2.getX()) * (y - c2.getY());
	}
	
	public static double area(double x1, double y1, double x2, double y2, double x3, double y3) { 
		return Math.abs((x1*(y2-y3) + x2*(y3-y1)+ 
                x3*(y1-y2))/2.0); 
	} 
	
	
	public static boolean xyInbound(final Coordinate c1, final Coordinate c2, final Coordinate c3, double x, double y) {        
        double d1, d2, d3;
        boolean has_neg, has_pos;

        d1 = sign(c1, c2, x, y);
        d2 = sign(c2, c3, x, y);
        d3 = sign(c3, c1, x, y);

        has_neg = (d1 < 0) || (d2 < 0) || (d3 < 0);
        has_pos = (d1 > 0) || (d2 > 0) || (d3 > 0);

        return !(has_neg && has_pos);    
	}

	
	private static double[] matrixMult(double[] m1, double[][] m2) {
		double[] result = new double[m1.length];
		result[0] = m1[0] * m2[0][0] + m1[1] * m2[1][0] + m1[2] * m2[2][0];
		result[1] = m1[0] * m2[0][1] + m1[1] * m2[1][1] + m1[2] * m2[2][1];
		result[2] = m1[0] * m2[0][2] + m1[1] * m2[1][2] + m1[2] * m2[2][2];
		return result;
	}	
	
	
}