package engine;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import entities.Polygon;

/**
 * Side GUI object for controlling the cameras. Work in progress to manage the polygons and drawing polygons
 * @author user
 *
 */
public class SideGUI extends JPanel {

	private static final long serialVersionUID = 1L;
	
	private Polygon selectedPolygon = null;
	//click selected, when clicking within Screen so polygon can be edited with mouse
	private Polygon clickSelected = null;//Muli-Select, CTRL+A to select all
	
	private JComboBox<Camera> testdrop2 = new JComboBox<>();
	private JSlider angleSlider1 = new JSlider();
	private JSlider angleSlider2 = new JSlider();
	private JSlider angleSlider3 = new JSlider();
	
	private JButton switchB = new JButton("View");
	private JButton forward = new JButton("Move Forward");
	
	private boolean drawing;
	
	/**
	 * Constructor to build the GUI with the camera angle sliders, and camera select drop down. Components
	 * are set to non-focusable the cameras can still be moved with the keys when the components are clicked
	 * @param handler The application Graphics object
	 */
	public SideGUI(Handler handler) {
		//List<Camera> cams = handler.getScreen().getCameras();
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		add(testdrop2);
		add(switchB);
		switchB.setFocusable(false);
		switchB.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				Camera cam = (Camera) testdrop2.getSelectedItem();
				//handler.getScreen().setCamera(cam);
				angleSlider1.setValue((int)cam.getXYAngle());
				angleSlider2.setValue((int)cam.getZYAngle());
				angleSlider3.setValue((int)cam.getXZAngle());
			}
		});
		
		add(forward);
		
		forward.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				Camera cam = (Camera) testdrop2.getSelectedItem();
				cam.moveForward();
				
			}
		});
		
		forward.setFocusable(false);
		
		//for (int i = 0; i < cams.size(); i++) {
		//	testdrop2.addItem(cams.get(i));
		//}
		testdrop2.setFocusable(false);
		testdrop2.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				Camera cam = (Camera) testdrop2.getSelectedItem();				
				angleSlider1.setValue((int)cam.getXYAngle());
				angleSlider2.setValue((int)cam.getZYAngle());
				angleSlider3.setValue((int)cam.getXZAngle());
				
			}
		});
		
		angleSlider1.setMinimum(0);
		angleSlider1.setMaximum(360);
		angleSlider1.setBorder(new EmptyBorder(50, 0, 0, 0));

		angleSlider2.setMinimum(0);
		angleSlider2.setMaximum(360);
		
		angleSlider3.setMinimum(0);
		angleSlider3.setMaximum(360);
		
		angleSlider1.addChangeListener(new ChangeListener() {
			
			@Override
			public void stateChanged(ChangeEvent e) {
				Camera cam = (Camera) testdrop2.getSelectedItem();
				cam.setXYAngle(angleSlider1.getValue());
				
			}
		});
		
		add(new JLabel("XYangle"));
		add(angleSlider1);
		angleSlider1.setFocusable(false);

		angleSlider2.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				Camera cam = (Camera) testdrop2.getSelectedItem();
				cam.setZYAngle(angleSlider2.getValue());
			}
		});
		
		add(new JLabel("ZYangle"));
		add(angleSlider2);
		angleSlider2.setFocusable(false);

		angleSlider3.addChangeListener(new ChangeListener() {
			
			@Override
			public void stateChanged(ChangeEvent e) {
				Camera cam = (Camera) testdrop2.getSelectedItem();
				cam.setXZAngle(angleSlider3.getValue());
			}
		});
		add(new JLabel("XZangle"));
		add(angleSlider3);
		angleSlider3.setFocusable(false);
	}

	/**
	 * Work in progress, not being used. in progress for rendering information when user is drawing polygons
	 * @param g The Application Graphics object
	 * @param x Top Left Coordinate
	 * @param y Top Right Coordinate
	 */
	public void render(Graphics g, int x, int y) {
		g.setColor(Color.gray);
		g.fillRect(x, y, 256, 640);		
		g.setColor(Color.BLACK);
		g.drawRect(x, y, 256, 640);
		
		g.drawString("Drawing: " + drawing, x + 16, y + 16);
		
		if (clickSelected!= null) {
			Color color = clickSelected.getColor();
			g.drawString("Color: [" + color.getRed() + ", " + color.getGreen() + ", " 
					+ color.getBlue() + "]", x + 16, y + 32);
			for (int i = 0; i < clickSelected.getCoords().size(); i++) {
				Coordinate coord = clickSelected.getCoords().get(i);
				g.drawString(i + ") " + coord.getX() + ", " + coord.getY() 
					+ ", " + coord.getZ(), x + 16, y + 48 + 16*i);
			}
		}
	
		System.out.println(angleSlider2.getValue());
	}

	/**
	 * Updated the sliders based on the selected camera
	 */
	public void tick() {
		Camera cam = (Camera) testdrop2.getSelectedItem();
		angleSlider1.setValue((int)cam.getXYAngle());
		angleSlider2.setValue((int)cam.getZYAngle());
		angleSlider3.setValue((int)cam.getXZAngle());
	}

	public void click (int x, int y) {
		if (x < 640 && x > 0) {
			clickSelected = selectedPolygon;
		}
	}
	
	//Getters and Setter------------------------------------------------------------------------------------------------
	
	public void setSelected(Polygon selected) {
		selectedPolygon = selected;
	}
	
	public void setDraw(boolean draw) {
		drawing = draw;
	}

	public Polygon getSelected() {
		return selectedPolygon;
	}

	public Polygon getClickSelected() {
		return clickSelected;
	}
	
	public boolean isDrawing() {
		return drawing;
	}
	
	//End of Getters and Setters----------------------------------------------------------------------------------------

}
