package engine;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferStrategy;

import javax.swing.JPanel;

import ui.SideGUI;

/**
 * Engine class that runs the the engine class in a thread which repeatedly calls tick() and render() 60 times a second.
 * 
 * @author user
 *
 */
public class Engine extends JPanel implements Runnable {

	private static final long serialVersionUID = 1L;
	private static final int WIDTH = 640 + 256;
	private static final int HEIGHT = 640;
	
	private boolean running;
	private Thread thread;
	private int frames;
	private Handler handler;
	private Canvas canvas = new Canvas();
	
	private boolean antiAliasing = false;
	
	
	/**
	 * Builds the canvas which the application is rendered on and creates a Handler object to handler the rendering
	 * and backend logic.
	 */
	public Engine() {
		handler = new Handler();
		Dimension size = new Dimension(WIDTH, HEIGHT);
		canvas.setSize(size);
		canvas.setPreferredSize(size);
		canvas.setFocusable(true);
		canvas.addKeyListener(handler);
		canvas.addMouseListener(handler);
		canvas.addMouseMotionListener(handler);
		canvas.addMouseWheelListener(handler);
		add(canvas);
		SideGUI side = new SideGUI(handler);
		handler.setSideGUI(side);
		add(side);
	}
	
	public synchronized void start() {
		if (running) {
			return;
		}
		running = true;
		thread = new Thread(this);
		thread.start();
	}
	
	public synchronized void stop() {
		if (!running) {
			return;
		}
		running = false;
		try {
			thread.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Calls tick() at 60 calls a second and render() up to 60 times a second depending on how long it takes to
	 * to process tick() and render()
	 */
	@Override
	public void run() {
		int frames = 0;
		double unprocessedSeconds = 0;
		long lastTime = System.nanoTime();
		double secondsPerTick = 1 / 60.0;
		int tickCount = 0;

		while (running) {
			long now = System.nanoTime();
			long passedTime = now - lastTime;
			lastTime = now;
			if (passedTime < 0) {
				passedTime = 0;
			}
			if (passedTime > 100000000) {
				passedTime = 100000000;
			}
			unprocessedSeconds += passedTime / 1000000000.0;
			boolean ticked = false;
			while (unprocessedSeconds > secondsPerTick) {
				tick();
				unprocessedSeconds -= secondsPerTick;
				ticked = true;
				tickCount++;
				if (tickCount % 60 == 0) {
					this.frames = frames;
					lastTime += 1000;
					frames = 0;
				}
			}
			if (ticked)  {//|| this != null)
				render();
				frames++;
			} else {
				try {
					Thread.sleep(1);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	
	/**
	 * Main Tick method, calls handler.tick() to update the backend logic
	 * 
	 */
	public void tick() {
		handler.tick();
	}
	
	/**
	 * Main render method, uses a a triple BufferStrategy as passes the Graphic object to handler to handler
	 * render logic and draw the application
	 */
	public void render() {
		BufferStrategy bs = canvas.getBufferStrategy();
		if (bs == null) {
			canvas.createBufferStrategy(3);
			return;
		}
		Graphics g = bs.getDrawGraphics();
		if (antiAliasing) {
			((Graphics2D) g).setRenderingHint(RenderingHints.KEY_ANTIALIASING,
	              RenderingHints.VALUE_ANTIALIAS_ON);
		}
		g.setColor(Color.BLUE);
		//g.fillRect(0, 0, getWidth(), getHeight());
		handler.render(g);
		g.setColor(Color.WHITE);
		g.drawString(Integer.toString(frames), 20, 20);
		g.dispose();
		bs.show();	
	}
	
}