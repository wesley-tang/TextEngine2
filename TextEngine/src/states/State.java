package states;

import java.awt.Dimension;

import javax.swing.JPanel;

/**
 * Abstract class from which States are created.
 */
@SuppressWarnings("serial")
public abstract class State extends JPanel {
	// Dimensions of display
	public final int WIDTH = 1000;
	public final int HEIGHT = 640;

	/**
	 * Creates a new State with preset dimensions.
	 */
	public State() {
		super();
		setPreferredSize(new Dimension(WIDTH, HEIGHT));
		setFocusable(true);
		requestFocusInWindow();
	}

	public abstract void init();
}