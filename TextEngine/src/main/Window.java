package main;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.net.URL;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
//import javax.swing.UIManager;
//import javax.swing.UnsupportedLookAndFeelException;

import managers.StateManager;

/**
 * Execution thread which creates the window from which the game is played, taking a Game object as its
 * ContentPane. This creates top-level container before being disposed.
 */
public class Window {
	private static String TITLE = "TITLE";
	private static String ICONURL = "/Images/icon.png";

	private final static double WIDTH_SCALE = 0.8;
	private final static double HEIGHT_SCALE = 0.8;
	
	
	private static StateManager statemng;

	/**
	 * Executes the program, creating the game and determining window properties.
	 * 
	 * @param args
	 *            (Can be used to enter specific mode when running from the console.
	 *            Currently no special modes)
	 */
	public static void main(String[] args) {
		// Set System L&F
//		try {
//			// Set System L&F
//			UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
//		} catch (UnsupportedLookAndFeelException e) {
//			// handle exception
//		} catch (ClassNotFoundException e) {
//			// handle exception
//		} catch (InstantiationException e) {
//			// handle exception
//		} catch (IllegalAccessException e) {
//			// handle exception
//		}

		// Creates the top-level container which contains all the SWING
		// components, with the given name [1]
		JFrame window = new JFrame(TITLE);

		statemng = new StateManager(window);
		
		// TODO MAIN PROBLEM IS TO GET THE WINDOW CREATED AND THE LOADING SCREEN CREATED ON SEPARATE THREADS BEFORE JOINING THEM WHEN THE GAME STARTS

		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		window.setResizable(true);

		// Sets the window sized according to its components (MUST HAVE
		// COMPONENTS EXISTING BEFORE THIS WORKS)
		// window.pack();
		
		// Sets the window size scaled and moves it to the centre of the screen.
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
	    window.setBounds(0,0,(int)(screenSize.width*WIDTH_SCALE), (int)(screenSize.height*HEIGHT_SCALE));
	
		window.setLocationRelativeTo(null);

		window.setVisible(true); // TODO figure out what causes the screen to update in this

		setIcon(window);
	}

	// Retrieve and set the icon (only works on Windows...)
	private static void setIcon(JFrame window) {
		URL iconURL = Window.class.getResource(ICONURL);
		// TODO if null
		ImageIcon icon = new ImageIcon(iconURL);
		window.setIconImage(icon.getImage());
	}
}

/*
 * References: [1]
 * https://docs.oracle.com/javase/tutorial/uiswing/components/toplevel.html
 */