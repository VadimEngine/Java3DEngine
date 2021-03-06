package engine;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.text.DecimalFormat;
import entities.Mesh;
import entities.Object3D;

/**
 * Camera object that is used as a reference for rendering
 * 
 * When all angles are 0, camera points directly up and top vector points to the
 * -y axis BUt for some reason, when doing unadjusted 3d math, top vetctor seems
 * to point in -x direction
 * 
 * @author Vadim
 *
 */
public class Camera {
    private DecimalFormat df = new DecimalFormat("#.00");

    private static final double MAX_ANGLE = 2 * Math.PI;

    private Coordinate position;

    private double speed = 5;

    private double rotSpeed = 0.05;

    // Replace angles with directional Angle
    private double xyAngle = 0;
    private double zyAngle = Math.PI / 2;// 0 points directly up
    private double xzAngle = 0;
    private double near = 620;

    private double cosXY;
    private double sinXY;

    private double cosZY;
    private double sinZY;

    private double cosXZ;
    private double sinXZ;

    private double cachedXyAngle = xyAngle;
    private double cachedZyAngle = zyAngle;
    private double cachedXzAngle = xzAngle;

    private Object3D cameraBody;

    public Camera() {
        this(0, 0, 0);
    }

    public Camera(double x, double y, double z) {
        position = new Coordinate(x, y, z);

        cosXY = Math.cos((xyAngle));
        sinXY = Math.sin((xyAngle));

        cosZY = Math.cos((zyAngle));
        sinZY = Math.sin((zyAngle));

        cosXZ = Math.cos(-(xzAngle));
        sinXZ = Math.sin(-(xzAngle));

        cameraBody = new Object3D(Mesh.createCubeMesh(x, y, z, 1, 1, 1, Color.GRAY));
        cameraBody.setXScale(10);
        cameraBody.setYScale(10);
        cameraBody.setZScale(10);
    }

    /**
     * Moves straight into looking direction by converting the angles(XY, ZY) from
     * Spherical to Cartesian by using (P=ro) x=P*sin(zy)cos(xy) y=P*sin(zy)sin(xy)
     * z=pcos(zy)
     * 
     * Needs to be updated to take xz angle into account. Add mouse controls. Clean
     * up Turns right by subtracting 90 degrees from xy and left by adding 90
     * degrees to xy
     * 
     * @param handler
     */
    public void tick(Handler handler, boolean focus) {
        if (focus) {
            // only do trig calculations when needed(W||A||S||D||RIGHT||etc.)

            double zy = (zyAngle);
            double xy = (xyAngle + Math.PI / 2);
            double sinZY = Math.sin(zy);
            double sinXY = Math.sin(xy);
            double cosZY = Math.cos(zy);
            double cosXY = Math.cos(xy);

            if (handler.keys[KeyEvent.VK_W]) {
                position.addX(sinZY * cosXY * (speed));
                position.addY(sinZY * sinXY * (speed));
                position.addZ(cosZY * (speed));
            }

            if (handler.keys[KeyEvent.VK_D]) {
                double xyRight = (xy - (Math.PI / 2));
                double sinXYRight = Math.sin(xyRight);
                double cosXYRight = Math.cos(xyRight);

                double zyRight = Math.PI / 2;
                double sinZYRight = Math.sin(zyRight);
                double cosZYRight = Math.cos(zyRight);

                position.addX(sinZYRight * cosXYRight * (speed));
                position.addY(sinZYRight * sinXYRight * (speed));
                position.addZ(cosZYRight * (speed));
            }

            if (handler.keys[KeyEvent.VK_S]) {
                position.addX(-sinZY * cosXY * (speed));
                position.addY(-sinZY * sinXY * (speed));
                position.addZ(-cosZY * (speed));
            }

            if (handler.keys[KeyEvent.VK_A]) {
                double xyLeft = (xy + (Math.PI / 2));
                double sinXYLeft = Math.sin(xyLeft);
                double cosXYLeft = Math.cos(xyLeft);

                double zyLeft = Math.PI / 2;
                double sinZYLeft = Math.sin(zyLeft);
                double cosZYLeft = Math.cos(zyLeft);

                position.addX(sinZYLeft * cosXYLeft * (speed));
                position.addY(sinZYLeft * sinXYLeft * (speed));
                position.addZ(cosZYLeft * (speed));
            }

            if (handler.keys[KeyEvent.VK_RIGHT]) {
                xyAngle = (xyAngle - rotSpeed) % MAX_ANGLE;
            }

            if (handler.keys[KeyEvent.VK_LEFT]) {
                xyAngle = (xyAngle + rotSpeed) % MAX_ANGLE;
            }

            if (handler.keys[KeyEvent.VK_UP]) {
                zyAngle = (zyAngle - rotSpeed) % MAX_ANGLE;
            }

            if (handler.keys[KeyEvent.VK_DOWN]) {
                zyAngle = (zyAngle + rotSpeed) % MAX_ANGLE;
            }

            if (handler.keys[KeyEvent.VK_Q]) {
                xzAngle = (xzAngle - rotSpeed) % MAX_ANGLE;
            }

            if (handler.keys[KeyEvent.VK_E]) {
                xzAngle = (xzAngle + rotSpeed) % MAX_ANGLE;
            }

            if (handler.keys[KeyEvent.VK_SPACE]) {
                position.addZ(5);
            }

            if (handler.keys[KeyEvent.VK_SHIFT]) {
                position.addZ(-5);
            }

            if (handler.keys[KeyEvent.VK_PERIOD]) {
                near++;
            }

            if (handler.keys[KeyEvent.VK_COMMA]) {
                near--;
            }
        }
    }

    public void render(Graphics g, Camera c) {

    }

    public void render(RenderHandler renderer, Camera cam) {
        cameraBody.setPosition(position);
        cameraBody.setXAngle(xyAngle);
        cameraBody.setYAngle(zyAngle);
        cameraBody.setZAngle(xzAngle);

        cameraBody.render(renderer, cam);
    }

    public void move(double angle) {
        double xMov = Math.cos((angle + xyAngle - Math.PI / 2)) * 5;
        double yMov = Math.sin((angle + xyAngle - Math.PI / 2)) * 5;
        position.addX(-xMov);
        position.addY(-yMov);
    }

    // Needs to take mouse x and y into account to zoom into where the mouse is
    // pointing (Fine the way it is?)
    public void scrollCamera(double dir) {
        double zy = (zyAngle);
        double xy = (xyAngle + Math.PI / 2);
        double sinZY = Math.sin(zy);
        double sinXY = Math.sin(xy);
        double cosZY = Math.cos(zy);
        double cosXY = Math.cos(xy);
        position.addX(-sinZY * cosXY * (speed * dir * 8));
        position.addY(-sinZY * sinXY * (speed * dir * 8));
        position.addZ(-cosZY * (speed * dir * 4));
    }

    // Need to take drag speed into account, maybe use mouse click and mouse move
    // together instead of drag?
    public void rotateCamera(double xdir, double ydir) {
        if (ydir > 0) {
            zyAngle = (zyAngle + rotSpeed) % MAX_ANGLE;
        }
        if (ydir < 0) {
            zyAngle = (zyAngle - rotSpeed) % MAX_ANGLE;
        }
        if (xdir > 0) {
            xyAngle = (xyAngle - rotSpeed) % MAX_ANGLE;
        }
        if (xdir < 0) {
            xyAngle = (xyAngle + rotSpeed) % MAX_ANGLE;
        }
    }

    public double getX() {
        return position.getX();
    }

    public double getY() {
        return position.getY();
    }

    public double getZ() {
        return position.getZ();
    }

    public double getCenterX() {
        return position.getX() + 640 / 2;
    }

    public double getCenterY() {
        return position.getY() + 640 / 2;
    }

    public double getXYAngle() {
        return xyAngle;
    }

    public double getZYAngle() {
        return zyAngle;
    }

    public double getXZAngle() {
        return xzAngle;
    }

    public double getCosXY() {
        if (cachedXyAngle != xyAngle) {
            cachedXyAngle = xyAngle;
            cosXY = Math.cos((xyAngle));
            sinXY = Math.sin((xyAngle));
        }
        return cosXY;
    }

    public double getSinXY() {
        if (cachedXyAngle != xyAngle) {
            cachedXyAngle = xyAngle;
            cosXY = Math.cos((xyAngle));
            sinXY = Math.sin((xyAngle));
        }
        return sinXY;
    }

    public double getCosZY() {
        if (cachedZyAngle != zyAngle) {
            cachedZyAngle = zyAngle;
            cosZY = Math.cos((zyAngle));
            sinZY = Math.sin((zyAngle));
        }
        return cosZY;
    }

    public double getSinZY() {
        if (cachedZyAngle != zyAngle) {
            cachedZyAngle = zyAngle;
            cosZY = Math.cos((zyAngle));
            sinZY = Math.sin((zyAngle));
        }
        return sinZY;
    }

    public double getCosXZ() {
        if (cachedXzAngle != xzAngle) {
            cachedZyAngle = zyAngle;
            cosXZ = Math.cos(-(xzAngle));
            sinXZ = Math.sin(-(xzAngle));
        }
        return cosXZ;
    }

    public double getSinXZ() {
        if (cachedXzAngle != xzAngle) {
            cachedZyAngle = zyAngle;
            cosXZ = Math.cos(-(xzAngle));
            sinXZ = Math.sin(-(xzAngle));
        }
        return sinXZ;
    }

    public double[] getUnitComponent() {// take in unit size? take all 3 angles into account.
        double zy = (zyAngle);
        double xy = (xyAngle + Math.PI / 2);
        double sinZY = Math.sin(zy);
        double sinXY = Math.sin(xy);
        double cosZY = Math.cos(zy);
        double cosXY = Math.cos(xy);

        double x = sinZY * cosXY;
        double y = sinZY * sinXY;
        double z = cosZY;

        return new double[] { x, y, z };
    }

    public void setXZAngle(double xzAngle) {
        this.xzAngle = xzAngle;
    }

    public void setXYAngle(double xyAngle) {
        this.xyAngle = xyAngle;
    }

    public void setZYAngle(double zyAngle) {
        this.zyAngle = zyAngle;
    }

    public double getNear() {
        return near;
    }

    @Override
    public String toString() {
        String pos = "Position: " + df.format(position.getX()) + ", " + df.format(position.getY()) + ", "
                + df.format(position.getZ());
        String angles = "Angle: " + df.format(xyAngle) + ", " + df.format(zyAngle) + ", " + df.format(xzAngle);
        return pos + "    \n\r   " + angles;
    }

    public void moveForward() {
        double zy = (zyAngle);
        double xy = (xyAngle + Math.PI / 2);
        double sinZY = Math.sin(zy);
        double sinXY = Math.sin(xy);
        double cosZY = Math.cos(zy);
        double cosXY = Math.cos(xy);

        position.addX(sinZY * cosXY * (speed));
        position.addY(sinZY * sinXY * (speed));
        position.addZ(cosZY * (speed));
    }

}
