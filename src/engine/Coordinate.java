package engine;

public class Coordinate {
	
	private double x;
	private double y;
	private double z;
	
	public Coordinate(double x, double y, double z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
	public double getX() {
		return x;
	}
	
	public double getY() {
		return y;
	}
	
	public double getZ() {
		return z;
	}
	
	public void setX(double x) {
		this.x = x;
	}
	
	public void setY(double y) {
		this.y = y;
	}
	
	public void setZ(double z) {
		this.z = z;
	}
	
	public void addX(double xAdd) {
		x += xAdd;
	}
	
	public void addY(double yAdd) {
		y += yAdd;
	}
	
	public void addZ(double zAdd) {
		z += zAdd;
	}
	
	public void add(Coordinate other) {
		x += other.x;
		y += other.y;
		z += other.z;
	}
	
	public void mulitply(double scaler) {
		x *= scaler;
		y *= scaler;
		z *= scaler;
	}
	
	//public void add(coordinate) {return new cord(x+x, y+y, z+z)}
	
	@Override
	public String toString() {
		return "[" + x + ", " + y + ", " + z + "]";
	}
	
	@Override
	public Coordinate clone() {
		return new Coordinate(x, y, z);
	}

}
