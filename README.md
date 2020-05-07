# Java3DEngine

A custom 3D game rendering engine built using Java AWT (Abstract Window Toolkit) and Swing and all 3D logic is built from sratch but mostly relies on Java Graphics Object drawing and a shape from a given set of coordinates. The postiions of the polyogns that are rendered are recalcuating on render based on the Camera postion. All polygons are "rotated" around the camera by using 3 Matrix mutiplcations to roate around the axis XY, XZ, and ZY. 

The camera can moved and adjusted with the keyboard and mouse. The camera moves in the dircetion its facing with the "W" key, back with the "S" key, Left with the "A" Key and right with the "D" Key. The camear can rotate left and right with the left and right arrow keys. It ran face up and down with the l

The camea responds to the following user input:
* "W" Key: Move camera in the direction it's facing
* "A" Key: Move camera left of the direction it's facing
* "S" Key: Mouse camera backwards of the direction it's facing
* "D" Key: Move camera right of the direction it's facing
* Left Arrow Key: Rotate the camera left
* Right Arrow Key: Rotate the camera right
* Up Arrow Key: Rotate the camera up
* Down Arrow Key: Rotate the camera down
* "Q" Key: Rotate the camera counter clock wise
* "E" Key: Rotate the camera clock wise
* Space Key: Move the camera up the z axiss
* Shift Key: Move the camera down the z axis
* "." Key: Increase camera field of view
* "," Key Decrease camera field of view
* Mouse Move: Highlights a highlighable polygon
* Mouse Drag (Right and middle mouse button): Moves the camaera relative to draging motion
* Mouse Wheel: moves the camera forward/backwards

The JPanel on the right allows managing the cameras in the applicaiton. The drop down allow the user to select a camera. The "View" button allows the user to switch perspective to the selected camera. The "Move forward" button moves the selected camaera in the direction its facing. The 3 sliders allow adjusting the selected camera's view angle. All cameras are green cubes with a red side represting where the camera is facing.


![Screen1](./Screenshots/view1.png)

![Screen2](./Screenshots/view2.png)

![Screen3](./Screenshots/view3.png)

The prebuilt shapes include a human figure made up of several cubes, a cylinder, and a 20 sided polyhydron (icosahedron). The icosahedron was work in progress for rendering spheres.