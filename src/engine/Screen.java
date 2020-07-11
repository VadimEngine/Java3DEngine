package engine;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;

/**
 * Screen class that handles rendering all renderable objects
 * 
 * Should this hold screen width and height?
 * 
 * @author Vadim
 *
 */
public class Screen {


	private double[] zBuffer;
	private int[] pixels;
	private BufferedImage theScreen;
	
	private Color background = Color.LIGHT_GRAY;
	
	private static final Color ALPHA_COLOR1 = new Color(255,0,255);
	private static final Color ALPHA_COLOR2 = new Color(200,0,200);

	/**
	 * Constructor that initializes cameras and populates all the polygons to render.
	 * @param handler The handler that holds this Screen
	 */
	public Screen(Handler handler) {
		this(handler, 640, 640);
	}
	
	public Screen(Handler handler, int width, int height) {
		zBuffer = new double[width*height];
		theScreen = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);		
		pixels = ((DataBufferInt) theScreen.getRaster().getDataBuffer()).getData();	
	}

	/**
	 * Sorts the polygons by their distance to the so the closest polygon get rendered last on top of the further
	 * polygons and then renders all renderable objects
	 *  
	 * @param g The application rendering object
	 */
	public void render(Graphics g) {			
		g.drawImage(theScreen, 0, 0, theScreen.getWidth(), theScreen.getHeight(), null);
	}
	
	public void setScreenColor(int xPos, int yPos, Color color) {
		if (xPos >= 0 && xPos < theScreen.getWidth() ) {
			int pixelIndex = xPos + yPos * theScreen.getWidth();
			if (pixelIndex >= 0 && pixelIndex < pixels.length & xPos < theScreen.getWidth()) {
				pixels[pixelIndex] = color.getRGB();
			}
		}
	}
	
	public void setScreenColor3D(int xPos, int yPos, int zPos, Color color) {	
		if (color.equals(ALPHA_COLOR1) || color.equals(ALPHA_COLOR2)) {
			return;
		}
		
		
		//Only color the closest positive-z pixels
		if (xPos >= 0 && xPos < theScreen.getWidth() && yPos >= 0 && yPos < theScreen.getHeight()) {
			int pixelIndex = xPos + yPos * theScreen.getWidth();
			if (zPos >= 0 && zPos <= zBuffer[pixelIndex]) {
				zBuffer[pixelIndex] = zPos;
				pixels[pixelIndex] = color.getRGB();
			}
		}
	}
	
	
	public void reset() {
		//reset color
		for (int i = 0; i < pixels.length; i++) {
			pixels[i] = background.getRGB();
		}
		
		//reset z buffer
		for (int i = 0; i < zBuffer.length; i++) {
			zBuffer[i] = Integer.MAX_VALUE;
		}
	}
	
	
	//GETTERS AND SETTERS
	public int getWidth() {
		return theScreen.getWidth();
	}
	
	public int getHeight() {
		return theScreen.getHeight();
	}
	
}
