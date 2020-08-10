package entities;

import java.awt.Color;
import java.util.List;

import engine.Calculator;
import engine.Camera;
import engine.Coordinate;
import engine.RenderHandler;

public class Object3D {

    private static int ObjectCount = 0;

    private Mesh theMesh;

    private Coordinate center = new Coordinate(0, 0, 0);

    // x scale, yscle and zscale
    private double xScale = 1;
    private double yScale = 1;
    private double zScale = 1;

    private Color color = Color.DARK_GRAY;

    // rotates around z axis (xyAngle)
    private double zAngle = 0;

    // rotates around y axis (zyAngle)
    private double yAngle = 0;

    // rotates around x axis (xzAngle)
    private double xAngle = 0;

    private String name;

    public Object3D(Mesh theMesh) {
        this(theMesh, Color.DARK_GRAY);
    }

    public Object3D(Mesh theMesh, Color theColor) {
        this.color = theColor;
        this.theMesh = theMesh;
        this.name = "Object " + ObjectCount++;
    }

    public void tick() {

    }

    public void render(RenderHandler renderer, Camera cam, Color theColor) {
        List<Integer> indicies = theMesh.getIndices();

        // xAngle = tempCount++;

        for (int i = 0; i < indicies.size(); i += 3) {
            Coordinate c1 = theMesh.getCoordinate(indicies.get(i)).clone();
            Coordinate c2 = theMesh.getCoordinate(indicies.get(i + 1)).clone();
            Coordinate c3 = theMesh.getCoordinate(indicies.get(i + 2)).clone();

            // rotate around mesh center (0,0,0)
            if (zAngle != 0 || yAngle != 0 || xAngle != 0) {
                c1 = Calculator.rotateAroundCenter(c1, new Coordinate(0, 0, 0), zAngle, yAngle, xAngle);
                c2 = Calculator.rotateAroundCenter(c2, new Coordinate(0, 0, 0), zAngle, yAngle, xAngle);
                c3 = Calculator.rotateAroundCenter(c3, new Coordinate(0, 0, 0), zAngle, yAngle, xAngle);
            }

            // Scale
            c1.setX(c1.getX() * xScale);
            c1.setY(c1.getY() * yScale);
            c1.setZ(c1.getZ() * zScale);

            c2.setX(c2.getX() * xScale);
            c2.setY(c2.getY() * yScale);
            c2.setZ(c2.getZ() * zScale);

            c3.setX(c3.getX() * xScale);
            c3.setY(c3.getY() * yScale);
            c3.setZ(c3.getZ() * zScale);

            // translate

            c1.addX(center.getX());
            c1.addY(center.getY());
            c1.addZ(center.getZ());

            c2.addX(center.getX());
            c2.addY(center.getY());
            c2.addZ(center.getZ());

            c3.addX(center.getX());
            c3.addY(center.getY());
            c3.addZ(center.getZ());

            // rotate around camera
            c1 = Calculator.rotateAroundCamera(c1, cam);
            c2 = Calculator.rotateAroundCamera(c2, cam);
            c3 = Calculator.rotateAroundCamera(c3, cam);

            // translate by camera
            // Set perspective

            renderer.drawTriangleScanLineOp(c1, c2, c3, theColor);
            renderer.drawLine3D(c1, c2, Color.BLACK);
            renderer.drawLine3D(c2, c3, Color.BLACK);
            renderer.drawLine3D(c3, c1, Color.BLACK);
        }
    }

    public void render(RenderHandler renderer, Camera cam) {
        render(renderer, cam, color);
    }

    public void setPosition(Coordinate newPos) {
        this.center = newPos;
    }

    public void setXPostion(double theX) {
        center.setX(theX);
    }

    public void setYPostion(double theY) {
        center.setY(theY);
    }

    public void setZPostion(double theZ) {
        center.setZ(theZ);
    }

    public void setXAngle(double xAngle) {
        this.xAngle = xAngle;
    }

    public void setYAngle(double yAngle) {
        this.yAngle = yAngle;
    }

    public void setZAngle(double zAngle) {
        this.zAngle = zAngle;
    }

    public void setXScale(double xScale) {
        this.xScale = xScale;
    }

    public void setYScale(double yScale) {
        this.yScale = yScale;
    }

    public void setZScale(double zScale) {
        this.zScale = zScale;
    }

    public void setName(String theName) {
        name = theName;
    }

    public Coordinate getPosition() {
        return new Coordinate(center.getX(), center.getY(), center.getZ());
    }

    public double getXAngle() {
        return xAngle;
    }

    public double getYAngle() {
        return yAngle;
    }

    public double getZAngle() {
        return zAngle;
    }

    public double getXScale() {
        return xScale;
    }

    public double getYScale() {
        return yScale;
    }

    public double getZScale() {
        return zScale;
    }

    public Mesh getMesh() {
        return theMesh;
    }

    public Color getColor() {
        return Color.DARK_GRAY;
    }

    @Override
    public String toString() {
        return name;
    }

}
