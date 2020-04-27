package engine;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferStrategy;

import javax.swing.JPanel;

public class Engine extends JPanel implements Runnable {

	private static final long serialVersionUID = 1L;
	private static final int WIDTH = 640 + 256;
	private static final int HEIGHT = 640;
	
	private boolean running;
	private Thread thread;
	private int frames;
	private Handler handler;
	
	private Canvas canvas = new Canvas();
	private SideGUI side;
	

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
		side = new SideGUI(handler);
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
			if (ticked) {
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
	 * This is the tick method written
	 * 
	 * @return returns nothing
	 */
	public void tick() {
		handler.tick();
		side.tick();
	}
	
	public void render() {
		BufferStrategy bs = canvas.getBufferStrategy();
		if (bs == null) {
			canvas.createBufferStrategy(3);
			return;
		}
		Graphics g = bs.getDrawGraphics();
		//((Graphics2D) g).setRenderingHint(RenderingHints.KEY_ANTIALIASING,
        //      RenderingHints.VALUE_ANTIALIAS_ON);
		g.setColor(Color.BLUE);
		g.fillRect(0, 0, getWidth(), getHeight());
		handler.render(g);
		g.setColor(Color.WHITE);
		g.drawString(Integer.toString(frames), 20, 20);
		g.dispose();
		bs.show();	
	}
	
	public Handler getHandler() {
		return handler;
	}
	
}
