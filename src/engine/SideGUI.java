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
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import entities.Object3D;
import entities.Polygon;

/**
 * Side GUI object for controlling the cameras. Work in progress to manage the polygons and drawing polygons
 * 
 * Update to use Java lambda expression
 * 
 * @author Vadim
 *
 */
public class SideGUI extends JPanel {

	private static final long serialVersionUID = 1L;
	
	private Polygon selectedPolygon = null;
	//click selected, when clicking within Screen so polygon can be edited with mouse
	private Polygon clickSelected = null;//Muli-Select, CTRL+A to select all
	
	private JComboBox<Camera> cameraDropDown = new JComboBox<>();
	private JSlider angleSlider1 = new JSlider();
	private JSlider angleSlider2 = new JSlider();
	private JSlider angleSlider3 = new JSlider();
	
	private JButton switchB = new JButton("View");
	private JButton forward = new JButton("Move Forward");
	
	
	private JComboBox<Object3D> objectDropDown = new JComboBox<>();
	
	private boolean drawing;
	
	
	private JTextField objXPos = new JTextField(10);
	private JTextField objYPos = new JTextField(10);
	private JTextField objZPos = new JTextField(10);
	
	private JTextField objXrot = new JTextField(10);
	private JTextField objYrot = new JTextField(10);
	private JTextField objZrot = new JTextField(10);
	
	private JTextField objXscale = new JTextField(10);
	private JTextField objYscale = new JTextField(10);
	private JTextField objZscale = new JTextField(10);
	
	
	
	/**
	 * Constructor to build the GUI with the camera angle sliders, and camera select drop down. Components
	 * are set to non-focusable the cameras can still be moved with the keys when the components are clicked
	 * @param handler The application Graphics object
	 */
	public SideGUI(Handler handler) {
		List<Camera> cams = handler.getLogicHandler().getCameras();
		
		for (Camera eachCamera: cams) {
			cameraDropDown.addItem(eachCamera);
		}
		
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		add(cameraDropDown);
		add(switchB);
		switchB.setFocusable(false);
		switchB.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				Camera cam = (Camera) cameraDropDown.getSelectedItem();
				//handler.getScreen().setCamera(cam);
				handler.getLogicHandler().setCamera(cam);
				angleSlider1.setValue((int)cam.getXYAngle());
				angleSlider2.setValue((int)cam.getZYAngle());
				angleSlider3.setValue((int)cam.getXZAngle());
			}
		});
		
		add(forward);
		
		forward.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				Camera cam = (Camera) cameraDropDown.getSelectedItem();
				cam.moveForward();
				
			}
		});
		
		forward.setFocusable(false);
		
		//for (int i = 0; i < cams.size(); i++) {
		//	testdrop2.addItem(cams.get(i));
		//}
		cameraDropDown.setFocusable(false);
		cameraDropDown.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				Camera cam = (Camera) cameraDropDown.getSelectedItem();				
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
				Camera cam = (Camera) cameraDropDown.getSelectedItem();
				cam.setXYAngle(angleSlider1.getValue());
				
			}
		});
		
		add(new JLabel("XYangle"));
		add(angleSlider1);
		angleSlider1.setFocusable(false);

		angleSlider2.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				Camera cam = (Camera) cameraDropDown.getSelectedItem();
				cam.setZYAngle(angleSlider2.getValue());
			}
		});
		
		add(new JLabel("ZYangle"));
		add(angleSlider2);
		angleSlider2.setFocusable(false);

		angleSlider3.addChangeListener(new ChangeListener() {
			
			@Override
			public void stateChanged(ChangeEvent e) {
				Camera cam = (Camera) cameraDropDown.getSelectedItem();
				cam.setXZAngle(angleSlider3.getValue());
			}
		});
		add(new JLabel("XZangle"));
		add(angleSlider3);
		angleSlider3.setFocusable(false);
		
		add(new JLabel("Objects"));
		
		for (Object3D eachObj: handler.getLogicHandler().getObjects()) {
			objectDropDown.addItem(eachObj);
		}
		
		objectDropDown.setFocusable(false);
		
		JButton objButton = new JButton("Select objet");
		objButton.setFocusable(false);
		
		objectDropDown.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				Object3D obj = (Object3D) objectDropDown.getSelectedItem();	
				if (obj != null) {
					handler.getLogicHandler().setSelectedObject(obj);
					//set the text fields
					Coordinate pos = obj.getPosition();
					double xAngle = obj.getXAngle();
					double yAngle = obj.getYAngle();
					double zAngle = obj.getZAngle();
					
					double xScale = obj.getXScale();
					double yScale = obj.getYScale();
					double zScale = obj.getZScale();
					
					objXPos.setText("" + pos.getX());
					objYPos.setText("" + pos.getY());
					objZPos.setText("" + pos.getZ());
					
					objXscale.setText("" + xScale);
					objYscale.setText("" + yScale);
					objZscale.setText("" + zScale);
					
					objXrot.setText("" + xAngle);
					objYrot.setText("" + yAngle);
					objZrot.setText("" + zAngle);
				}
			}
		});
		
		objButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Object3D obj = (Object3D) objectDropDown.getSelectedItem();	
				if (obj != null) {
					handler.getLogicHandler().setSelectedObject(obj);
					//set the text fields
					Coordinate pos = obj.getPosition();
					double xAngle = obj.getXAngle();
					double yAngle = obj.getYAngle();
					double zAngle = obj.getZAngle();
					
					double xScale = obj.getXScale();
					double yScale = obj.getYScale();
					double zScale = obj.getZScale();
					
					objXPos.setText("" + pos.getX());
					objYPos.setText("" + pos.getY());
					objZPos.setText("" + pos.getZ());
					
					objXscale.setText("" + xScale);
					objYscale.setText("" + yScale);
					objZscale.setText("" + zScale);
					
					objXrot.setText("" + xAngle);
					objYrot.setText("" + yAngle);
					objZrot.setText("" + zAngle);
				}
			}
		});
		
		
		add(objectDropDown);
		add(objButton);		
	
		//Postion (Group into 1 row)
		JPanel positionPanel = new JPanel();
		positionPanel.add(new JLabel("Position"));		
		positionPanel.add(new JLabel("X"));
		positionPanel.add(objXPos);	
		positionPanel.add(new JLabel("Y"));
		positionPanel.add(objYPos);	
		positionPanel.add(new JLabel("Z"));
		positionPanel.add(objZPos);	
		
		//rotation
		JPanel rotationPanel = new JPanel();
		rotationPanel.add(new JLabel("Rotation"));
		rotationPanel.add(new JLabel("X"));
		rotationPanel.add(objXrot);	
		rotationPanel.add(new JLabel("Y"));
		rotationPanel.add(objYrot);	
		rotationPanel.add(new JLabel("Z"));
		rotationPanel.add(objZrot);
		
		//Scale
		JPanel scalePanel = new JPanel();
		scalePanel.add(new JLabel("Scale"));
		scalePanel.add(new JLabel("X"));
		scalePanel.add(objXscale);	
		scalePanel.add(new JLabel("Y"));
		scalePanel.add(objYscale);
		scalePanel.add(new JLabel("Z"));
		scalePanel.add(objZscale);	
		
		add(positionPanel);
		add(rotationPanel);
		add(scalePanel);
		setObjectUIListeners();
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
	
	
	private void setObjectUIListeners() {
		objXPos.getDocument().addDocumentListener(new DocumentListener() {
			@Override
			public void removeUpdate(DocumentEvent e) {
				try {
					double newX = Double.parseDouble(objXPos.getText());
					Object3D obj = (Object3D) objectDropDown.getSelectedItem();	
					if (obj != null) {
						obj.setXPostion(newX);
					}
				} catch (Exception ex) {}	
			}
			@Override
			public void insertUpdate(DocumentEvent e) {
				try {
					double newX = Double.parseDouble(objXPos.getText());
					Object3D obj = (Object3D) objectDropDown.getSelectedItem();	
					if (obj != null) {
						obj.setXPostion(newX);
					}
				} catch (Exception ex) {}	
			}
			@Override
			public void changedUpdate(DocumentEvent e) {
				try {
					double newX = Double.parseDouble(objXPos.getText());
					Object3D obj = (Object3D) objectDropDown.getSelectedItem();	
					if (obj != null) {
						obj.setXPostion(newX);
					}
				} catch (Exception ex) {}	
			}
		});
		
		objYPos.getDocument().addDocumentListener(new DocumentListener() {
			@Override
			public void removeUpdate(DocumentEvent e) {
				try {
					double newY = Double.parseDouble(objYPos.getText());
					Object3D obj = (Object3D) objectDropDown.getSelectedItem();	
					if (obj != null) {
						obj.setYPostion(newY);
					}
				} catch (Exception ex) {}	
			}
			@Override
			public void insertUpdate(DocumentEvent e) {
				try {
					double newY = Double.parseDouble(objYPos.getText());
					Object3D obj = (Object3D) objectDropDown.getSelectedItem();	
					if (obj != null) {
						obj.setYPostion(newY);
					}
				} catch (Exception ex) {}	
			}
			@Override
			public void changedUpdate(DocumentEvent e) {
				try {
					double newY = Double.parseDouble(objYPos.getText());
					Object3D obj = (Object3D) objectDropDown.getSelectedItem();	
					if (obj != null) {
						obj.setYPostion(newY);
					}
				} catch (Exception ex) {}	
			}
		});
		
		objZPos.getDocument().addDocumentListener(new DocumentListener() {
			@Override
			public void removeUpdate(DocumentEvent e) {
				try {
					double newZ = Double.parseDouble(objZPos.getText());
					Object3D obj = (Object3D) objectDropDown.getSelectedItem();	
					if (obj != null) {
						obj.setZPostion(newZ);
					}
				} catch (Exception ex) {}	
			}
			@Override
			public void insertUpdate(DocumentEvent e) {
				try {
					double newZ = Double.parseDouble(objZPos.getText());
					Object3D obj = (Object3D) objectDropDown.getSelectedItem();	
					if (obj != null) {
						obj.setZPostion(newZ);
					}
				} catch (Exception ex) {}	
			}
			@Override
			public void changedUpdate(DocumentEvent e) {
				try {
					double newZ = Double.parseDouble(objZPos.getText());
					Object3D obj = (Object3D) objectDropDown.getSelectedItem();	
					if (obj != null) {
						obj.setZPostion(newZ);
					}
				} catch (Exception ex) {}	
			}
		});
		objXrot.getDocument().addDocumentListener(new DocumentListener() {
			@Override
			public void removeUpdate(DocumentEvent e) {
				try {
					double newXRot = Double.parseDouble(objXrot.getText());
					Object3D obj = (Object3D) objectDropDown.getSelectedItem();	
					if (obj != null) {
						obj.setXAngle(newXRot);
					}
				} catch (Exception ex) {}	
			}
			@Override
			public void insertUpdate(DocumentEvent e) {
				try {
					double newXRot = Double.parseDouble(objXrot.getText());
					Object3D obj = (Object3D) objectDropDown.getSelectedItem();	
					if (obj != null) {
						obj.setXAngle(newXRot);
					}
				} catch (Exception ex) {}	
			}
			@Override
			public void changedUpdate(DocumentEvent e) {
				try {
					double newXRot = Double.parseDouble(objXrot.getText());
					Object3D obj = (Object3D) objectDropDown.getSelectedItem();	
					if (obj != null) {
						obj.setXAngle(newXRot);
					}
				} catch (Exception ex) {}	
			}
		});
		objYrot.getDocument().addDocumentListener(new DocumentListener() {
			@Override
			public void removeUpdate(DocumentEvent e) {
				try {
					double newYRot = Double.parseDouble(objYrot.getText());
					Object3D obj = (Object3D) objectDropDown.getSelectedItem();	
					if (obj != null) {
						obj.setYAngle(newYRot);
					}
				} catch (Exception ex) {}	
			}
			@Override
			public void insertUpdate(DocumentEvent e) {
				try {
					double newYRot = Double.parseDouble(objYrot.getText());
					Object3D obj = (Object3D) objectDropDown.getSelectedItem();	
					if (obj != null) {
						obj.setYAngle(newYRot);
					}
				} catch (Exception ex) {}	
			}
			@Override
			public void changedUpdate(DocumentEvent e) {
				try {
					double newYRot = Double.parseDouble(objYrot.getText());
					Object3D obj = (Object3D) objectDropDown.getSelectedItem();	
					if (obj != null) {
						obj.setYAngle(newYRot);
					}
				} catch (Exception ex) {}	
			}
		});
		objZrot.getDocument().addDocumentListener(new DocumentListener() {
			@Override
			public void removeUpdate(DocumentEvent e) {
				try {
					double newZRot = Double.parseDouble(objZrot.getText());
					Object3D obj = (Object3D) objectDropDown.getSelectedItem();	
					if (obj != null) {
						obj.setZAngle(newZRot);
					}
				} catch (Exception ex) {}	
			}
			@Override
			public void insertUpdate(DocumentEvent e) {
				try {
					double newZRot = Double.parseDouble(objZrot.getText());
					Object3D obj = (Object3D) objectDropDown.getSelectedItem();	
					if (obj != null) {
						obj.setZAngle(newZRot);
					}
				} catch (Exception ex) {}	
			}
			@Override
			public void changedUpdate(DocumentEvent e) {
				try {
					double newZRot = Double.parseDouble(objZrot.getText());
					Object3D obj = (Object3D) objectDropDown.getSelectedItem();	
					if (obj != null) {
						obj.setZAngle(newZRot);
					}
				} catch (Exception ex) {}	
			}
		});
		
		objXscale.getDocument().addDocumentListener(new DocumentListener() {
			@Override
			public void removeUpdate(DocumentEvent e) {
				try {
					double newXscale = Double.parseDouble(objXscale.getText());
					Object3D obj = (Object3D) objectDropDown.getSelectedItem();	
					if (obj != null) {
						obj.setXScale(newXscale);
					}
				} catch (Exception ex) {}	
			}
			@Override
			public void insertUpdate(DocumentEvent e) {
				try {
					double newXscale = Double.parseDouble(objXscale.getText());
					Object3D obj = (Object3D) objectDropDown.getSelectedItem();	
					if (obj != null) {
						obj.setXScale(newXscale);
					}
				} catch (Exception ex) {}	
			}
			@Override
			public void changedUpdate(DocumentEvent e) {
				try {
					double newXscale = Double.parseDouble(objXscale.getText());
					Object3D obj = (Object3D) objectDropDown.getSelectedItem();	
					if (obj != null) {
						obj.setXScale(newXscale);
					}
				} catch (Exception ex) {}	
			}
		});
		objYscale.getDocument().addDocumentListener(new DocumentListener() {
			@Override
			public void removeUpdate(DocumentEvent e) {
				try {
					double newYScale = Double.parseDouble(objYscale.getText());
					Object3D obj = (Object3D) objectDropDown.getSelectedItem();	
					if (obj != null) {
						obj.setYScale(newYScale);
					}
				} catch (Exception ex) {}	
			}
			@Override
			public void insertUpdate(DocumentEvent e) {
				try {
					double newYScale = Double.parseDouble(objYscale.getText());
					Object3D obj = (Object3D) objectDropDown.getSelectedItem();	
					if (obj != null) {
						obj.setYScale(newYScale);
					}
				} catch (Exception ex) {}	
			}
			@Override
			public void changedUpdate(DocumentEvent e) {
				try {
					double newYScale = Double.parseDouble(objYscale.getText());
					Object3D obj = (Object3D) objectDropDown.getSelectedItem();	
					if (obj != null) {
						obj.setYScale(newYScale);
					}
				} catch (Exception ex) {}	
			}
		});
		objZscale.getDocument().addDocumentListener(new DocumentListener() {
			@Override
			public void removeUpdate(DocumentEvent e) {
				try {
					double newZScale = Double.parseDouble(objZscale.getText());
					Object3D obj = (Object3D) objectDropDown.getSelectedItem();	
					if (obj != null) {
						obj.setZScale(newZScale);
					}
				} catch (Exception ex) {}	
			}
			@Override
			public void insertUpdate(DocumentEvent e) {
				try {
					double newZScale = Double.parseDouble(objZscale.getText());
					Object3D obj = (Object3D) objectDropDown.getSelectedItem();	
					if (obj != null) {
						obj.setZScale(newZScale);
					}
				} catch (Exception ex) {}	
			}
			@Override
			public void changedUpdate(DocumentEvent e) {
				try {
					double newZScale = Double.parseDouble(objZscale.getText());
					Object3D obj = (Object3D) objectDropDown.getSelectedItem();	
					if (obj != null) {
						obj.setZScale(newZScale);
					}
				} catch (Exception ex) {}	
			}
		});
	}
	

	/**
	 * Updated the sliders based on the selected camera
	 */
	public void tick() {
		Camera cam = (Camera) cameraDropDown.getSelectedItem();
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

	public void setSelectedObject(Object3D theSelected) {
		if (theSelected != null) {
			objectDropDown.setSelectedItem(theSelected);
			
			Coordinate pos = theSelected.getPosition();
			double xAngle = theSelected.getXAngle();
			double yAngle = theSelected.getYAngle();
			double zAngle = theSelected.getZAngle();
			
			double xScale = theSelected.getXScale();
			double yScale = theSelected.getYScale();
			double zScale = theSelected.getZScale();
			
			objXPos.setText("" + pos.getX());
			objYPos.setText("" + pos.getY());
			objZPos.setText("" + pos.getZ());
			
			objXscale.setText("" + xScale);
			objYscale.setText("" + yScale);
			objZscale.setText("" + zScale);
			
			objXrot.setText("" + xAngle);
			objYrot.setText("" + yAngle);
			objZrot.setText("" + zAngle);
		}
	}
	
}
