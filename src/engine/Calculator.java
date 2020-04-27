package engine;


/**
 * 
 * Currently using right hand coordinate system
 * 
 * 
 * 
 * @author user
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
	 * @param coord
	 * @param cam
	 * @return
	 */
	public static Coordinate rotateAroundCamera(Coordinate coord, Camera cam) {
		double cosXY = Math.cos((cam.getXYAngle()) * Math.PI/180);
		double sinXY = Math.sin((cam.getXYAngle()) * Math.PI/180);
		
		double cosZY = Math.cos((cam.getZYAngle()) * Math.PI/180);
		double sinZY = Math.sin((cam.getZYAngle()) * Math.PI/180);
		
		double cosXZ = Math.cos(-(cam.getXZAngle()) * Math.PI/180);
		double sinXZ = Math.sin(-(cam.getXZAngle()) * Math.PI/180);
		
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
	
	
	private static double[] matrixMult(double[] m1, double[][] m2) {
		double[] result = new double[m1.length];
		result[0] = m1[0] * m2[0][0] + m1[1] * m2[1][0] + m1[2] * m2[2][0];
		result[1] = m1[0] * m2[0][1] + m1[1] * m2[1][1] + m1[2] * m2[2][1];
		result[2] = m1[0] * m2[0][2] + m1[1] * m2[1][2] + m1[2] * m2[2][2];
		return result;
	}	
	
}
