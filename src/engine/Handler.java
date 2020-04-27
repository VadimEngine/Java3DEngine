package engine;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;

import javax.swing.SwingUtilities;

public class Handler implements KeyListener, MouseListener, MouseMotionListener, MouseWheelListener {
	public boolean[] keys = new boolean[65536];//make retrieve only for other classes
	public int mouseX;
	public int mouseY;
	
	
	private Screen screen = new Screen(this);

	/*
	 * Instead of using the listener to do actions in the game (Which may cause the tick to not
	 * be synched), it might be like an extra thread messing with game/data flow, Save the mouse
	 * info like mouse x,y, left/right/middle boolean, isDrag? Too much game logic in the 
	 * mouse action listeners
	 * 
	 */
	private int oldX = 0;
	private int oldY = 0;

	public Handler() {}

	public void tick() {
		screen.tick();
	}

	public void render(Graphics g) {
		screen.render(g);
	}

	@Override
	public void mouseWheelMoved(MouseWheelEvent e) {		
		screen.scollCamera(e.getPreciseWheelRotation());
	}

	@Override
	public void mouseDragged(MouseEvent e) {
		if ((e.getX() != oldX || e.getY() != oldY) && SwingUtilities.isMiddleMouseButton(e)) {
			double angle = Math.atan2((oldX - e.getX()), (oldY - e.getY()));
			angle = angle * 180.0/Math.PI;
			angle = (angle + 90) % 360;
			screen.moveCamera(angle);
			
			oldX = e.getX();
			oldY = e.getY();
		}
		if ((e.getX() != oldX || e.getY() != oldY) && SwingUtilities.isRightMouseButton(e)) {
			int xdir = oldX - e.getX();
			int ydir = oldY - e.getY(); 
			screen.rotateCamera( xdir,  ydir ); 
			
			oldX = e.getX();
			oldY = e.getY();
		}		

	}

	@Override
	public void mouseMoved(MouseEvent e) {
		//screen.selectPoly(e.getX(), e.getY());
		oldX = e.getX();
		oldY = e.getY();
		
		mouseX = e.getX();
		mouseY = e.getY();
	}

	@Override
	public void mouseClicked(MouseEvent arg0) {}

	@Override
	public void mouseEntered(MouseEvent e) {
		oldX = e.getX();
		oldY = e.getY();
	}

	@Override
	public void mouseExited(MouseEvent arg0) {}

	@Override
	public void mousePressed(MouseEvent e) {
		screen.click(e.getX(), e.getY(), e);
	}

	@Override
	public void mouseReleased(MouseEvent arg0) {}

	@Override
	public void keyPressed(KeyEvent e) {
		int code = e.getKeyCode(); 
		if (code>0 && code<keys.length) {
			keys[code] = true;
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
		int code = e.getKeyCode(); 
		if (code>0 && code<keys.length) {
			keys[code] = false;
		}
	}

	@Override
	public void keyTyped(KeyEvent arg0) {}
	
	public Screen getScreen() {
		return screen;
	}

}
