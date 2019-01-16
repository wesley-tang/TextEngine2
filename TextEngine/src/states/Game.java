package states;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

import javax.swing.BorderFactory;
import javax.swing.JEditorPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ToolTipManager;
import javax.swing.text.html.HTMLEditorKit;
import javax.swing.text.html.HTMLDocument;

import managers.SceneManager;

/*
 * Does creating instance variables within a method instead of the class retain those variables properly when placed into a hashmap?
 * 
 * Bad to read from file every time a scene is clicked? **IF it is, I could technically preload all the text and THEN token replace it and stuff**
 * 
 * How exactly is the code executed by a program? When does the thread begin running? Where do the arguments for events enter from?
 * 
 * 
 * Notes:
 * Instead of a one time boolean to change text, set text itself as a variable, and then change that so the one displayed is changed once and never again
 *
 *					>>> MAKE IT WORK AND WORK WELL *BEFORE* OPTIMIZING <<<
 *
 */

/**
 * Contains all the SWING Components composing the GUI.
 * 
 * <p>
 * Manages and displays the correct GUI.
 */
@SuppressWarnings("serial")
public class Game extends State implements KeyListener {
	// Logger
	private static final Logger LOGGER = Logger.getLogger(Game.class.getName());
	private static FileHandler fh = null;

	private SceneManager sm;

	// Components
	private JEditorPane textPane;
	public JPanel buttonContainer;

	private JPanel choiceContainer;

	private JScrollPane editorScrollPane;

	// TextPane Properties
	private int fontSize = 20;
	//private static double TEXTPANE_WIDTH_RATIO = 0.80;
	//private static double TEXTPANE_HEIGHT_RATIO = 0.75;

	/**
	 * Creates a new JPanel and initializes and populates it with all the
	 * JComponents for normal text gameplay.
	 */
	public Game() {
		super();
		setPreferredSize(new Dimension(WIDTH, HEIGHT));
		setFocusable(true);
		requestFocusInWindow();

		LOGGER.log(Level.FINE, "Finished Game Constructor");

	}

	public static void initLogger() {
		try {
			fh = new FileHandler("Game.log", false);
		} catch (Exception e) {
			e.printStackTrace();
		}
		Logger l = Logger.getLogger("");
		fh.setFormatter(new SimpleFormatter());
		l.addHandler(fh);
		l.setLevel(Level.INFO);
	}

	// Initializes the game's components: text boxes, etc.
	private void initComponents() {
		LOGGER.log(Level.FINE, "Initializing components.");

		// image = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
		// g = (Graphics2D) image.getGraphics();

		setBackground(new Color(45, 45, 45));// TODO test
		setOpaque(true);
		// Setting as BorderLayout
		setLayout(new BorderLayout());

		add(initScrollPane(), BorderLayout.CENTER);

		// Container for the CustomButtons at the bottom of the screen
		buttonContainer = new JPanel(new GridBagLayout());
		buttonContainer.setOpaque(false);

		choiceContainer = new JPanel();
		choiceContainer.setOpaque(false);

		GridBagConstraints c = new GridBagConstraints();
		c.gridx = 1;
		c.gridy = 0;
		c.gridwidth = 1;
		c.gridheight = 1;
		c.weightx = 1;
		c.insets = new Insets(0, 0, 0, 55);
		// c.fill = GridBagConstraints.BOTH;

		buttonContainer.add(choiceContainer, c);

		add(buttonContainer, BorderLayout.SOUTH);

		ToolTipManager.sharedInstance().setInitialDelay(150);

		LOGGER.log(Level.FINE, "finished initailizing components.");
	}

	// Initializes the scroll pane where the text box is in
	private JScrollPane initScrollPane() {
		LOGGER.log(Level.FINER, "Initializing Text/Scroll Pane");

		textPane = new JEditorPane(new HTMLEditorKit().getContentType(),
				"Editor Pane initializing. If you're seeing this, something has gone wrong.");

		// Setting font (Cannot use to set font on the editor pane because it
		// uses style sheet once an HTMLEditorKit is created)
		Font font = new Font("Arial", Font.PLAIN, fontSize);
		// Rule dictating the font, defined by the Font font.
		String bodyRule = "body { font-family: " + font.getFamily() + "; " + "font-size: " + font.getSize() + "pt; }";
		// Retrieves the document from the JeditorPane, gets its style sheet,
		// then adds the above rule
		((HTMLDocument) textPane.getDocument()).getStyleSheet().addRule(bodyRule);

		textPane.setEditable(false);

		textPane.setMargin(new Insets(11, 15, 7, 9));

		textPane.setBackground(new Color(221, 221, 221));

		// Put the editor pane in a scroll pane.
		editorScrollPane = new JScrollPane(textPane);
		editorScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		// editorScrollPane.setPreferredSize(
		// new Dimension((int) (WIDTH * TEXTPANE_WIDTH_RATIO), (int) (HEIGHT *
		// TEXTPANE_HEIGHT_RATIO)));

		// Border between text and edges of editor pane
		editorScrollPane.setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 10));

		editorScrollPane.setOpaque(false);

		return editorScrollPane;
	}

	// Prepares the game's content.
	public void init() {
		initComponents();
		// initLogger();

		LOGGER.log(Level.FINE, "Initializing SceneManager");

		// Builds all the scenes
		sm = new SceneManager(textPane, choiceContainer, this);

		// TODO along with
		// https://stackoverflow.com/questions/4362888/getting-the-number-of-files-in-a-folder-in-java
		// it is possible to override the 'put' method inside of the scenes
		// hashmap to increment scenes loaded
		// and thus track how many scenes have been loaded for the loading scene

		getSceneManager().changeScene("BeginningScene");

		LOGGER.log(Level.FINE, "Finished SceneManager and set starting scene");
		System.out.println("DONE");
	}

//	/**
//	 * Slightly customized validate method to ensure component sizes remain
//	 * after changes to the window. [3]
//	 */
//	@Override
//	public void validate() {
//		super.validate();
//
////		editorScrollPane.setPreferredSize(
////				new Dimension((int) (getWidth() * TEXTPANE_WIDTH_RATIO), (int) (getHeight() * TEXTPANE_HEIGHT_RATIO)));
//	}

	// EventListeners ~~~~~~~~~~~~~~~~~
	public void keyTyped(KeyEvent e) {

	}

	// *** Disabled since the thread has been removed ***
	public void keyPressed(KeyEvent e) {

		if (e.getKeyCode() == KeyEvent.VK_ENTER) {
			repaint();
			revalidate();
			System.out.println("Trying to fix GUI");
			LOGGER.log(Level.INFO, "Request to revalidate GUI.");
		} else if (e.getKeyCode() == KeyEvent.VK_Z) {
			// Debug rewind -> bad use
			// Rewinds only to one scene back. Holding it breaks it lol
			LOGGER.log(Level.INFO, "Rewind requested. Rewinding.");
			System.out.println("Attempting to rewind...");
			getSceneManager().rewind();
		} else if (e.getKeyCode() == KeyEvent.VK_P) {
			getSceneManager().appendScene(getSceneManager().status);
		}
	}

	public void keyReleased(KeyEvent e) {}

	public SceneManager getSceneManager() {
		return sm;
	}
}

/*
 * The following links to various sources from whence pieces of code were copied
 * or derived.
 * 
 * http://stackoverflow.com/questions/12542733/setting-default-font-in-
 * jeditorpane
 * 
 * http://stackoverflow.com/questions/959731/how-to-replace-a-set-of-tokens-in-
 * a- java-string
 * 
 * [3]
 * https://stackoverflow.com/questions/2106367/listen-to-jframe-resize-events-as
 * -the-user-drags-their-mouse
 */