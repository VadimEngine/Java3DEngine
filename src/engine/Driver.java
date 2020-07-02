package engine;
import java.awt.BorderLayout;
import javax.swing.JFrame;
import javax.swing.JPanel;

/**
 * Driver class that builds the Frame to hold the Engine JPanel and starts the game engine.
 * @author Vadim
 *
 */
public class Driver {
	
	public static void main(String args[]) {
		Engine engine = new Engine();
		
		JFrame frame = new JFrame("3d Draw 2.0");
		JPanel panel = new JPanel(new BorderLayout());
		panel.add(engine, BorderLayout.WEST);
		frame.setContentPane(panel);
		frame.setLocationRelativeTo(null);
		frame.setResizable(false);
		frame.pack();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
			
		engine.start();
	}
}
