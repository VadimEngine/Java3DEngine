package entities;

import engine.Coordinate;

public class VertexTex extends Coordinate{
	
	private double texX;
	private double texY;
	
	private Texture texture;
	
	public VertexTex(double x, double y, double z, double texX, double texY, Texture texture) {
		super(x, y, z);
		this.texX = texX;
		this.texY = texY;
		this.texture = texture;
	}
	
	public VertexTex(double x, double y, double z, double texX, double texY) {
		super(x, y, z);
		this.texX = texX;
		this.texY = texY;
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
	
	@Override
	public VertexTex clone() {
		return new VertexTex(x, y, z, texX, texY, texture);
	}

	
	public void add(VertexTex other) {
		this.x += other.x;
		this.y += other.y;
		this.z += other.z;
		this.texX += other.texX;
		this.texY += other.texY;
	}
	
	@Override
	public void multiply(double scaler) {
		this.x *= scaler;
		this.y *= scaler;
		this.z *= scaler;
		this.texX *= scaler;
		this.texY *= scaler;
	}
	
}
