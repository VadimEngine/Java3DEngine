package engine;

public class DirectionVector {

    private double xyAngle;
    private double zyAngle;// 0 points directly up
    private double xzAngle;

    public DirectionVector(double xyAngle, double zyAngle, double xzAngle) {
        this.xyAngle = xyAngle;
        this.zyAngle = zyAngle;
        this.xzAngle = xzAngle;
    }

    // get(i)
    // get unitVector
    // get cross product
    // add vector

    public double getXyAngle() {
        return xyAngle;
    }

    public double getZyAngle() {
        return zyAngle;
    }

    public double getXzAngle() {
        return xzAngle;
    }

}
