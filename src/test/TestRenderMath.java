package test;

import engine.Calculator;
import engine.Camera;
import engine.Coordinate;
import org.junit.Test;
import org.junit.Assert;

/**
 * Test class to validate that Calculator class is accurate
 * @author user
 *
 */
public class TestRenderMath {
	
	private static final double ERROR_RANGE = 0.001;

	private Camera testCam;
	private Coordinate testCoord;

	/**
	 * Divide by zero needs to be checked
	 */
	@Test
	public void testRotate() {
		testCam = new Camera(0, 0, 0);
		testCam.setXYAngle(0);
		testCam.setXZAngle(0);
		testCam.setZYAngle(0);
		testCoord = new Coordinate(1, 1, 1);
		

		//double[] rotated = Calculator.rotate(testCoord.getX(), testCoord.getY(), testCoord.getZ(), testCam);
		//assertEquals(String message, double expected, double actual, double epsilon) 
		//Assert.assertEquals("X is not rotated corrected", 1, rotated[0], ERROR_RANGE);

	}
	
	@Test
	public void testRotateAroundCenter() {
		//Coordinate test1 = new Coordinate(1, 0, 0);
		//Coordinate center = new Coordinate(0, 0, 0);
		
//		//double[] rotated = Calculator.rotateAroundCenter(test1, center, 90.0, 0, 0);
//		System.out.println(rotated[0] + ", " + rotated[1] + ", " + rotated[2]);
//		Assert.assertEquals( "(XY rotate)X not rotated Correct. Expected 0.0 but was " + rotated[0],0.0, rotated[0], ERROR_RANGE);
//		Assert.assertEquals( "(XY rotate)Y not rotated Correct. Expected 1.0 but was " + rotated[1],1.0, rotated[1], ERROR_RANGE);
//		Assert.assertEquals( "(XY rotate)Z not rotated Correct. Expected 0.0 but was " + rotated[2],0.0, rotated[2], ERROR_RANGE);
//		
//		//rotated = Calculator.rotateAroundCenter(test1, center, 0.0, 90.0, 0.0);
//		System.out.println(rotated[0] + ", " + rotated[1] + ", " + rotated[2]);
//		Assert.assertEquals( "(XZ rotate)X not rotated Correct. Expected 0.0 but was " + rotated[0],0.0, rotated[0], ERROR_RANGE);
//		Assert.assertEquals( "(XZ rotate)Y not rotated Correct. Expected 0.0 but was " + rotated[1],0.0, rotated[1], ERROR_RANGE);
//		Assert.assertEquals( "(XZ rotate)Z not rotated Correct. Expected -1.0 but was " + rotated[2],-1.0, rotated[2], ERROR_RANGE);
//		
//		//rotated = Calculator.rotateAroundCenter(test1, center, 0.0, 0.0, 90.0);
//		System.out.println(rotated[0] + ", " + rotated[1] + ", " + rotated[2]);
//		Assert.assertEquals( "(ZY rotate)X not rotated Correct. Expected 1.0 but was " + rotated[0],1.0, rotated[0], ERROR_RANGE);
//		Assert.assertEquals( "(ZY rotate)Y not rotated Correct. Expected 0.0 but was " + rotated[1],0.0, rotated[1], ERROR_RANGE);
//		Assert.assertEquals( "(ZY rotate)Z not rotated Correct. Expected 0.0 but was " + rotated[2],0.0, rotated[2], ERROR_RANGE);
//		
//		//rotated = Calculator.rotateAroundCenter(test1, center, 0.0, 0.0, 0.0);
//		System.out.println(rotated[0] + ", " + rotated[1] + ", " + rotated[2]);
//		Assert.assertEquals( "(ZY rotate)X not rotated Correct. Expected 1.0 but was " + rotated[0],1.0, rotated[0], ERROR_RANGE);
//		Assert.assertEquals( "(ZY rotate)Y not rotated Correct. Expected 0.0 but was " + rotated[1],0.0, rotated[1], ERROR_RANGE);
//		Assert.assertEquals( "(ZY rotate)Z not rotated Correct. Expected 0.0 but was " + rotated[2],0.0, rotated[2], ERROR_RANGE);
		
		
		Coordinate center = new Coordinate(0, 0, 0);
		Coordinate top = new Coordinate(0, 0, 1);
		
		Coordinate rot = Calculator.rotateAroundCenter(top, center, 120, 109.5, 0);
		System.out.println(rot);
		
		
	}
	
	
	@Test
	public void testCalcZ() {
		
		//Coordinate c1 = new Coordinate(454.66368, 423.45489, 361.64834);
		//Coordinate c2 = new Coordinate(129.5121,178.49775,700.89771);
		//Coordinate c3 = new Coordinate(443.79735,110.55942,947.3768);
		
		//double x = 291;
		//double y = 208;
		//z should be ~708
		
		Coordinate c1 = new Coordinate(1, 1, 6);
		Coordinate c2 = new Coordinate(-1, -1, 1);
		Coordinate c3 = new Coordinate(1, -1, 8);
		
		double x = 0;
		double y = 0;
		
		
		double z = Calculator.zOnPlane2(c1, c2, c3,  x,  y);
		//z should be ~708
	}

	
	@Test
	public void testInbound() {
		
		Coordinate c1 = new Coordinate(331.09996600641125, 497.83201476794875, 349.1913649912884);
		Coordinate c2 = new Coordinate(627.5885138701215, 176.59868848484706, 646.8555376245224);
		Coordinate c3 = new Coordinate(272.75374384952573, 74.29539401576503, 887.8992318771176);
		
		int x = 353;
		int y = 223;
		
		boolean inbound = Calculator.xyInbound(c1, c2, c3, x, y);
		
		System.out.println(inbound);
		
		
		
		
	}



}
