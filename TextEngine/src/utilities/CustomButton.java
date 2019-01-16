package utilities;

import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Insets;
import java.awt.image.BufferedImage;

import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JButton;

/**
 * Button class derived from JButton so that custom images can be used.
 * TODO may replace with a specified look and feel or CSS/Javascript
 */
@SuppressWarnings("serial")
public class CustomButton extends JButton {
	private BufferedImage buttonImg;
	private final static int DEFAULT_WIDTH = 130;
	private final static int DEFAULT_HEIGHT = 40;


	/**
	 * Creates a new button using the image given by the URL.
	 * 
	 * @param imageURL
	 *            String representing the file path to the
	 */
	public CustomButton(String text, String imageURL) {
		this(text);

		buttonImg = null;
		try {
			// Setting the sprite to the one found in file TODO remove
			// imageIO.read to enable runnable jar
			buttonImg = ImageIO.read(getClass().getResourceAsStream(imageURL));
		} catch (IOException ioe) {
			ioe.printStackTrace();
			System.out.println("Failed to set button image!");
		}
	}

	/**
	 * Calls JButton's default constructor.
	 */
	public CustomButton() {
		super();
		super.setCursor(new Cursor(Cursor.HAND_CURSOR));
	}

	/**
	 * Creates a CustomButton with the specified text and default size of 130 by 40.
	 * 
	 * @param text
	 */
	public CustomButton(String text) {
		this(text, DEFAULT_WIDTH, DEFAULT_HEIGHT);
	}

	/**
	 * Creates a CustomButton of the specified width and height, with the given
	 * text;
	 * 
	 * @param text
	 * @param width
	 * @param height
	 */
	public CustomButton(String text, int width, int height) {
		super(text);
		// Allows hand cursor when hovering over button [3]
		super.setCursor(new Cursor(Cursor.HAND_CURSOR));
		// Ensures text isn't cut off if it is a bit long
		setMargin(new Insets(0, 2, 0, 2));
		setPreferredSize(new Dimension(width, height));
	}

	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);

		if (buttonImg != null) {
			// Create a separate instance of 'g' to prevent errors [1] [2]
			Graphics g2 = g.create();
			g2.drawImage(buttonImg, 0, 0, null);
			g2.dispose();
		}

		// TODO draw graphic in here. The painter is for multiple different button
		// styles
		// Or you could always just create subclasses
	}
}

/*
 * References [1]
 * https://docs.oracle.com/javase/7/docs/api/javax/swing/JComponent.html#
 * paintComponent(java.awt.Graphics) [2]
 * https://stackoverflow.com/questions/9149992/override-jbutton-paintcomponent-
 * doesnt-work-java [3]
 * https://stackoverflow.com/questions/27194858/jbutton-default-cursor
 * 
 * https://stackoverflow.com/questions/2407024/drawing-graphics-on-top-of-a-
 * jbutton
 */