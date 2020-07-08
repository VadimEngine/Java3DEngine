package entities;

import engine.Coordinate;

public class VertexTex {
	
	private double x;
	private double y;
	private double z;
	
	private double texX;
	private double texY;
	
	private Texture texture;
	
	public VertexTex(double x, double y, double z, double texX, double texY, Texture texture) {
		this.x = x;
		this.y = y;
		this.z = z;
		this.texX = texX;
		this.texY = texY;
		this.texture = texture;
	}
	
	public Coordinate getCoordiante() {
		return new Coordinate(x, y, z);
	}

	public double getX() {
		return x;
	}

	public void setX(double x) {
		this.x = x;
	}

	public double getY() {
		return y;
	}

	public void setY(double y) {
		this.y = y;
	}

	public double getZ() {
		return z;
	}

	public void setZ(double z) {
		this.z = z;
	}

	public double getTexX() {
		return texX;
	}

	public void setTexX(double texX) {
		this.texX = texX;
	}

	public double getTexY() {
		return texY;
	}

	public void setTexY(double texY) {
		this.texY = texY;
	}

	public Texture getTexture() {
		return texture;
	}

	public void setTexture(Texture texture) {
		this.texture = texture;
	}

}
