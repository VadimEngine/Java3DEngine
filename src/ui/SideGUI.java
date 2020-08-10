package ui;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSlider;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import engine.Camera;
import engine.Coordinate;
import engine.Handler;
import entities.Mesh;
import entities.Object3D;

/**
 * Side GUI object for controlling the cameras. Work in progress to manage the
 * polygons and drawing polygons
 * 
 * Update to use Java lambda expression
 * 
 * @author Vadim
 *
 */
public class SideGUI extends JPanel {

    private static final long serialVersionUID = 1L;

    private JSlider angleSlider1 = new JSlider();
    private JSlider angleSlider2 = new JSlider();
    private JSlider angleSlider3 = new JSlider();

    private JButton swithViewButton = new JButton("View");
    private JButton forwardButton = new JButton("Move Forward");

    private DefaultListModel<Camera> cameramodel = new DefaultListModel<>();
    private JList<Camera> cameraDropDown = new JList<>(cameramodel);

    private DefaultListModel<Object3D> objectmodel = new DefaultListModel<>();
    private JList<Object3D> objectDropDown = new JList<>(objectmodel);

    private DefaultListModel<Mesh> meshmodel = new DefaultListModel<>();
    private JList<Mesh> meshJList = new JList<>(meshmodel);

    private JTextField objXPos = new JTextField(10);
    private JTextField objYPos = new JTextField(10);
    private JTextField objZPos = new JTextField(10);

    private JTextField objXrot = new JTextField(10);
    private JTextField objYrot = new JTextField(10);
    private JTextField objZrot = new JTextField(10);

    private JTextField objXscale = new JTextField(10);
    private JTextField objYscale = new JTextField(10);
    private JTextField objZscale = new JTextField(10);

    private Handler handler;

    /**
     * Constructor to build the GUI with the camera angle sliders, and camera select
     * drop down. Components are set to non-focusable the cameras can still be moved
     * with the keys when the components are clicked
     * 
     * @param handler The application Graphics object
     */
    public SideGUI(Handler handler) {
        this.handler = handler;
        objectDropDown.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        addCameraUI();
        setObjectUIPanel();
        setEditPanel();
    }

    private void addCameraUI() {
        JPanel cameraPanel = new JPanel();
        cameraPanel.setLayout(new BoxLayout(cameraPanel, BoxLayout.Y_AXIS));
        cameraPanel.setBorder(BorderFactory.createLineBorder(Color.black));

        cameraPanel.add(new JLabel("Camera"));

        for (Camera eachCamera : handler.getLogicHandler().getCameras()) {
            cameramodel.addElement(eachCamera);
        }

        JPanel buttonPanel = new JPanel();

        JCheckBox toggleFloor = new JCheckBox("View floor", true);
        toggleFloor.setFocusable(false);
        toggleFloor.addActionListener(e -> {
            handler.getLogicHandler().setFloorDisplay(toggleFloor.isSelected());
        });
        buttonPanel.add(toggleFloor);

        cameraDropDown.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        cameraDropDown.setVisibleRowCount(5);

        JScrollPane camerascrollPane = new JScrollPane();
        camerascrollPane.setViewportView(cameraDropDown);
        cameraDropDown.setVisibleRowCount(5);

        cameraPanel.add(camerascrollPane);
        buttonPanel.add(swithViewButton);
        swithViewButton.setFocusable(false);
        swithViewButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent arg0) {
                Camera cam = (Camera) cameraDropDown.getSelectedValue();
                if (cam != null) {
                    handler.getLogicHandler().setCamera(cam);
                    angleSlider1.setValue((int) cam.getXYAngle());
                    angleSlider2.setValue((int) cam.getZYAngle());
                    angleSlider3.setValue((int) cam.getXZAngle());
                }

            }
        });

        buttonPanel.add(forwardButton);

        forwardButton.addActionListener(e -> {
            Camera cam = (Camera) cameraDropDown.getSelectedValue();
            if (cam != null) {
                cam.moveForward();
            }

        });

        forwardButton.setFocusable(false);

        cameraDropDown.setFocusable(false);
        cameraDropDown.addListSelectionListener(e -> {
            Camera cam = (Camera) cameraDropDown.getSelectedValue();
            angleSlider1.setValue((int) cam.getXYAngle());
            angleSlider2.setValue((int) cam.getZYAngle());
            angleSlider3.setValue((int) cam.getXZAngle());

        });

        angleSlider1.addChangeListener(e -> {
            Camera cam = (Camera) cameraDropDown.getSelectedValue();
            if (cam != null) {
                cam.setXYAngle(angleSlider1.getValue());
            }
        });

        angleSlider2.addChangeListener(e -> {
            Camera cam = (Camera) cameraDropDown.getSelectedValue();
            if (cam != null) {
                cam.setZYAngle(angleSlider2.getValue());
            }
        });

        angleSlider3.addChangeListener(e -> {
            Camera cam = (Camera) cameraDropDown.getSelectedValue();
            if (cam != null) {
                cam.setXZAngle(angleSlider3.getValue());
            }
        });

        angleSlider1.setMinimum(0);
        angleSlider1.setMaximum(360);

        angleSlider2.setMinimum(0);
        angleSlider2.setMaximum(360);

        angleSlider3.setMinimum(0);
        angleSlider3.setMaximum(360);

        JPanel xy = new JPanel();
        xy.add(new JLabel("XYangle"));
        xy.add(angleSlider1);

        JPanel zy = new JPanel();
        zy.add(new JLabel("ZYangle"));
        zy.add(angleSlider2);

        JPanel xz = new JPanel();
        xz.add(new JLabel("XZangle"));
        xz.add(angleSlider3);

        cameraPanel.add(buttonPanel);
        cameraPanel.add(xy);
        cameraPanel.add(zy);
        cameraPanel.add(xz);
        add(cameraPanel);
    }

    private void setEditPanel() {
        JPanel editPanel = new JPanel();
        editPanel.setLayout(new BoxLayout(editPanel, BoxLayout.Y_AXIS));
        editPanel.setBorder(BorderFactory.createLineBorder(Color.black));

        editPanel.add(new JLabel("Edit"));

        for (Mesh eachMesh : handler.getLogicHandler().meshList) {
            meshmodel.addElement(eachMesh);
        }

        meshJList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane meshscrollPane = new JScrollPane();
        meshscrollPane.setViewportView(meshJList);
        meshJList.setVisibleRowCount(5);

        editPanel.add(meshscrollPane);

        JButton addButton = new JButton("Add");
        addButton.addActionListener(e -> {
            Mesh theMesh = meshJList.getSelectedValue();
            if (theMesh != null) {
                handler.getLogicHandler().addObject3D(theMesh);
                // reset object JList
                objectmodel.clear();
                for (Object3D eachObject : handler.getLogicHandler().getObjects()) {
                    objectmodel.addElement(eachObject);
                }
            }

        });
        editPanel.add(addButton);

        add(editPanel);
    }

    // remove handler
    private void setObjectUIPanel() {
        setObjectUIListeners();
        JPanel objectPanel = new JPanel();
        objectPanel.setLayout(new BoxLayout(objectPanel, BoxLayout.Y_AXIS));
        objectPanel.setBorder(BorderFactory.createLineBorder(Color.black));
        objectPanel.add(new JLabel("Objects"));

        for (int i = 0; i < handler.getLogicHandler().getObjects().size(); i++) {
            Object3D eachObj = handler.getLogicHandler().getObjects().get(i);
            objectmodel.addElement(eachObj);
        }

        JScrollPane objectscrollPane = new JScrollPane();
        objectscrollPane.setViewportView(objectDropDown);
        objectDropDown.setVisibleRowCount(5);

        objectPanel.add(objectscrollPane);

        objectDropDown.setFocusable(false);

        objectDropDown.addListSelectionListener(e -> {
            Object3D obj = (Object3D) objectDropDown.getSelectedValue();
            if (obj != null) {
                handler.getLogicHandler().setSelectedObject(obj);
                // set the text fields
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
        });

        JPanel positionPanel = new JPanel();
        positionPanel.add(new JLabel("Position"));
        positionPanel.add(new JLabel("X"));
        positionPanel.add(objXPos);
        positionPanel.add(new JLabel("Y"));
        positionPanel.add(objYPos);
        positionPanel.add(new JLabel("Z"));
        positionPanel.add(objZPos);

        // rotation
        JPanel rotationPanel = new JPanel();
        rotationPanel.add(new JLabel("Rotation"));
        rotationPanel.add(new JLabel("X"));
        rotationPanel.add(objXrot);
        rotationPanel.add(new JLabel("Y"));
        rotationPanel.add(objYrot);
        rotationPanel.add(new JLabel("Z"));
        rotationPanel.add(objZrot);

        // Scale
        JPanel scalePanel = new JPanel();
        scalePanel.add(new JLabel("Scale"));
        scalePanel.add(new JLabel("X"));
        scalePanel.add(objXscale);
        scalePanel.add(new JLabel("Y"));
        scalePanel.add(objYscale);
        scalePanel.add(new JLabel("Z"));
        scalePanel.add(objZscale);

        objectPanel.add(positionPanel);
        objectPanel.add(rotationPanel);
        objectPanel.add(scalePanel);

        add(objectPanel);
        setObjectUIListeners();
    }

    private void setObjectUIListeners() {
        objXPos.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void removeUpdate(DocumentEvent e) {
                try {
                    double newX = Double.parseDouble(objXPos.getText());
                    Object3D obj = (Object3D) objectDropDown.getSelectedValue();
                    if (obj != null) {
                        obj.setXPostion(newX);
                    }
                } catch (Exception ex) {
                }
            }

            @Override
            public void insertUpdate(DocumentEvent e) {
                try {
                    double newX = Double.parseDouble(objXPos.getText());
                    Object3D obj = (Object3D) objectDropDown.getSelectedValue();
                    if (obj != null) {
                        obj.setXPostion(newX);
                    }
                } catch (Exception ex) {
                }
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                try {
                    double newX = Double.parseDouble(objXPos.getText());
                    Object3D obj = (Object3D) objectDropDown.getSelectedValue();
                    if (obj != null) {
                        obj.setXPostion(newX);
                    }
                } catch (Exception ex) {
                }
            }
        });

        objYPos.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void removeUpdate(DocumentEvent e) {
                try {
                    double newY = Double.parseDouble(objYPos.getText());
                    Object3D obj = (Object3D) objectDropDown.getSelectedValue();
                    if (obj != null) {
                        obj.setYPostion(newY);
                    }
                } catch (Exception ex) {
                }
            }

            @Override
            public void insertUpdate(DocumentEvent e) {
                try {
                    double newY = Double.parseDouble(objYPos.getText());
                    Object3D obj = (Object3D) objectDropDown.getSelectedValue();
                    if (obj != null) {
                        obj.setYPostion(newY);
                    }
                } catch (Exception ex) {
                }
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                try {
                    double newY = Double.parseDouble(objYPos.getText());
                    Object3D obj = (Object3D) objectDropDown.getSelectedValue();
                    if (obj != null) {
                        obj.setYPostion(newY);
                    }
                } catch (Exception ex) {
                }
            }
        });

        objZPos.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void removeUpdate(DocumentEvent e) {
                try {
                    double newZ = Double.parseDouble(objZPos.getText());
                    Object3D obj = (Object3D) objectDropDown.getSelectedValue();
                    if (obj != null) {
                        obj.setZPostion(newZ);
                    }
                } catch (Exception ex) {
                }
            }

            @Override
            public void insertUpdate(DocumentEvent e) {
                try {
                    double newZ = Double.parseDouble(objZPos.getText());
                    Object3D obj = (Object3D) objectDropDown.getSelectedValue();
                    if (obj != null) {
                        obj.setZPostion(newZ);
                    }
                } catch (Exception ex) {
                }
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                try {
                    double newZ = Double.parseDouble(objZPos.getText());
                    Object3D obj = (Object3D) objectDropDown.getSelectedValue();
                    if (obj != null) {
                        obj.setZPostion(newZ);
                    }
                } catch (Exception ex) {
                }
            }
        });
        objXrot.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void removeUpdate(DocumentEvent e) {
                try {
                    double newXRot = Double.parseDouble(objXrot.getText());
                    Object3D obj = (Object3D) objectDropDown.getSelectedValue();
                    if (obj != null) {
                        obj.setXAngle(newXRot);
                    }
                } catch (Exception ex) {
                }
            }

            @Override
            public void insertUpdate(DocumentEvent e) {
                try {
                    double newXRot = Double.parseDouble(objXrot.getText());
                    Object3D obj = (Object3D) objectDropDown.getSelectedValue();
                    if (obj != null) {
                        obj.setXAngle(newXRot);
                    }
                } catch (Exception ex) {
                }
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                try {
                    double newXRot = Double.parseDouble(objXrot.getText());
                    Object3D obj = (Object3D) objectDropDown.getSelectedValue();
                    if (obj != null) {
                        obj.setXAngle(newXRot);
                    }
                } catch (Exception ex) {
                }
            }
        });
        objYrot.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void removeUpdate(DocumentEvent e) {
                try {
                    double newYRot = Double.parseDouble(objYrot.getText());
                    Object3D obj = (Object3D) objectDropDown.getSelectedValue();
                    if (obj != null) {
                        obj.setYAngle(newYRot);
                    }
                } catch (Exception ex) {
                }
            }

            @Override
            public void insertUpdate(DocumentEvent e) {
                try {
                    double newYRot = Double.parseDouble(objYrot.getText());
                    Object3D obj = (Object3D) objectDropDown.getSelectedValue();
                    if (obj != null) {
                        obj.setYAngle(newYRot);
                    }
                } catch (Exception ex) {
                }
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                try {
                    double newYRot = Double.parseDouble(objYrot.getText());
                    Object3D obj = (Object3D) objectDropDown.getSelectedValue();
                    if (obj != null) {
                        obj.setYAngle(newYRot);
                    }
                } catch (Exception ex) {
                }
            }
        });
        objZrot.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void removeUpdate(DocumentEvent e) {
                try {
                    double newZRot = Double.parseDouble(objZrot.getText());
                    Object3D obj = (Object3D) objectDropDown.getSelectedValue();
                    if (obj != null) {
                        obj.setZAngle(newZRot);
                    }
                } catch (Exception ex) {
                }
            }

            @Override
            public void insertUpdate(DocumentEvent e) {
                try {
                    double newZRot = Double.parseDouble(objZrot.getText());
                    Object3D obj = (Object3D) objectDropDown.getSelectedValue();
                    if (obj != null) {
                        obj.setZAngle(newZRot);
                    }
                } catch (Exception ex) {
                }
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                try {
                    double newZRot = Double.parseDouble(objZrot.getText());
                    Object3D obj = (Object3D) objectDropDown.getSelectedValue();
                    if (obj != null) {
                        obj.setZAngle(newZRot);
                    }
                } catch (Exception ex) {
                }
            }
        });

        objXscale.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void removeUpdate(DocumentEvent e) {
                try {
                    double newXscale = Double.parseDouble(objXscale.getText());
                    Object3D obj = (Object3D) objectDropDown.getSelectedValue();
                    if (obj != null) {
                        obj.setXScale(newXscale);
                    }
                } catch (Exception ex) {
                }
            }

            @Override
            public void insertUpdate(DocumentEvent e) {
                try {
                    double newXscale = Double.parseDouble(objXscale.getText());
                    Object3D obj = (Object3D) objectDropDown.getSelectedValue();
                    if (obj != null) {
                        obj.setXScale(newXscale);
                    }
                } catch (Exception ex) {
                }
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                try {
                    double newXscale = Double.parseDouble(objXscale.getText());
                    Object3D obj = (Object3D) objectDropDown.getSelectedValue();
                    if (obj != null) {
                        obj.setXScale(newXscale);
                    }
                } catch (Exception ex) {
                }
            }
        });
        objYscale.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void removeUpdate(DocumentEvent e) {
                try {
                    double newYScale = Double.parseDouble(objYscale.getText());
                    Object3D obj = (Object3D) objectDropDown.getSelectedValue();
                    if (obj != null) {
                        obj.setYScale(newYScale);
                    }
                } catch (Exception ex) {
                }
            }

            @Override
            public void insertUpdate(DocumentEvent e) {
                try {
                    double newYScale = Double.parseDouble(objYscale.getText());
                    Object3D obj = (Object3D) objectDropDown.getSelectedValue();
                    if (obj != null) {
                        obj.setYScale(newYScale);
                    }
                } catch (Exception ex) {
                }
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                try {
                    double newYScale = Double.parseDouble(objYscale.getText());
                    Object3D obj = (Object3D) objectDropDown.getSelectedValue();
                    if (obj != null) {
                        obj.setYScale(newYScale);
                    }
                } catch (Exception ex) {
                }
            }
        });
        objZscale.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void removeUpdate(DocumentEvent e) {
                try {
                    double newZScale = Double.parseDouble(objZscale.getText());
                    Object3D obj = (Object3D) objectDropDown.getSelectedValue();
                    if (obj != null) {
                        obj.setZScale(newZScale);
                    }
                } catch (Exception ex) {
                }
            }

            @Override
            public void insertUpdate(DocumentEvent e) {
                try {
                    double newZScale = Double.parseDouble(objZscale.getText());
                    Object3D obj = (Object3D) objectDropDown.getSelectedValue();
                    if (obj != null) {
                        obj.setZScale(newZScale);
                    }
                } catch (Exception ex) {
                }
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                try {
                    double newZScale = Double.parseDouble(objZscale.getText());
                    Object3D obj = (Object3D) objectDropDown.getSelectedValue();
                    if (obj != null) {
                        obj.setZScale(newZScale);
                    }
                } catch (Exception ex) {
                }
            }
        });
    }

    /**
     * Updated the sliders based on the selected camera
     */
    public void tick() {
        Camera cam = (Camera) cameraDropDown.getSelectedValue();
        angleSlider1.setValue((int) cam.getXYAngle());
        angleSlider2.setValue((int) cam.getZYAngle());
        angleSlider3.setValue((int) cam.getXZAngle());
    }

    // Getters and
    // Setter------------------------------------------------------------------------------------------------

    // End of Getters and
    // Setters----------------------------------------------------------------------------------------

    public void setSelectedObject(Object3D theSelected) {
        if (theSelected != null) {
            objectDropDown.setSelectedValue(theSelected, true);

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
