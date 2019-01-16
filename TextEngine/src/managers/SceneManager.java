package managers;

import java.awt.Color;
import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JEditorPane;
import javax.swing.JPanel;

import game.Player;
import items.Consumable;
import items.Equippable;
import items.Item;
import states.Game;
import utilities.CustomButton;
import utilities.Parser;
import utilities.Scene;
import utilities.SceneHistory;
import utilities.TokenMapCreator;

/* is it bad to pass the game object into the SceneManager? That way it isn't a static reference, but at the same time it's kinda weird to
* have an entire reference to it in here? Also is it a privacy leak?
* 
*/

// TODO To remedy the oddity of adding text into the box and losing it, try storing a revised copy in temp mem?
// TODO this file may get too large to laod properly, is there a way to segment it or anything to reduce it's size? 
/**
 * Manages the construction and execution of all scenes with their corresponding
 * methods and variables.
 */
public class SceneManager {
	// Logger
	private static final Logger LOGGER = Logger.getLogger(SceneManager.class.getName());

	private Parser parser;

	// Scenes
	private Map<String, Scene> scenes;

	private Map<String, Boolean> flags;

	private Map<String, Item> items;

	// URL
	private static final String FLAGS_PATH = "/Dictionaries/flagDictionary";

	// JComponents
	private JEditorPane textPane;
	private JPanel buttonContainer;

	private CustomButton menuBtn;
	private CustomButton infoBtn;

	// Variables for controlling scene history
	private SceneHistory sceneHistory;
	private JPanel sceneHistoryBtns;

	// Marker Scenes
	private Scene currentScene;
	private Scene priorScene;

	// Text that is added to the bottom of the normal text
	public String status = "";
	// Special text that is added every scene
	// private String appendText;

	// The game object where the components are held. Needed to update those
	// components locally.
	private Game game;

	// Variable to track all gameplay variables stored for the player
	private Player player;

	// Used to hold state when accessing different parts of the game. TODO find a
	// better way of holding 'state'?
	private Scene suspendedScene;

	private boolean menuDisplayed = false;

	/**
	 * Creates a new SceneManager for the creation and handling of all scenes.
	 * 
	 * @param textPane
	 *            where text is displayed
	 * @param buttonContainer
	 *            JPanel that contains the buttons
	 * @param game
	 *            ContentPane that houses the JComponents
	 */
	public SceneManager(JEditorPane textPane, JPanel buttonContainer, Game game) {
		LOGGER.fine("Entered SceneManager Constructor.");

		this.textPane = textPane;
		this.buttonContainer = buttonContainer;
		this.game = game;

		sceneHistoryBtns = new JPanel();

		items = new ItemLoader().getItems();

		flags = loadFlags(FLAGS_PATH);

		parser = new Parser(flags, new TokenMapCreator().createFor(player));
		scenes = initScenes();

		initStatsBtn();
		initInfoBtn();
		initSceneHistoryBtns();

		LOGGER.fine("Finished SceneManager Constructor");
	}

	/**
	 * Defines all the load methods for every scene before adding it to the
	 * <code>scene</code> hashmap.
	 * <p>
	 * All scene coding occurs in here.
	 * 
	 * @return scenes The completed hashmap between the names of the scenes and the
	 *         scenes themselves.
	 */
	public Map<String, Scene> initScenes() {
		LOGGER.fine("Initializing all scenes.");
		Map<String, Scene> scenes = new HashMap<String, Scene>();

		// TODO replace with an actual main menu ***
		Scene scene = new Scene("Beginning", "Misc") {
			@Override
			public void load(Parser p) {
				// TODO recode the parser call phrase so that if an error
				// occurs, it is caught
				// here (parser should throw an error) so that the textpane can
				// be updated
				// also TODO maybe move this so that it's all done within the
				// super...? maybe?
				setContent(p.parse(getContentURL()));

				infoBtn.setVisible(true);
				menuBtn.setVisible(false);
				sceneHistoryBtns.setVisible(false);
				initDefaultValues();
				loadButtons();
			}

			public void loadButtons() {
				clearButtons();

				CustomButton btn = new CustomButton("Begin");
				btn.addMouseListener(new MouseAdapter() {
					public void mouseClicked(MouseEvent e) {
						// Start scene
						changeScene("StartSceneHere");
						menuBtn.setVisible(true);
						infoBtn.setVisible(false);
						menuDisplayed = false;
						sceneHistoryBtns.setVisible(true);
					}
				});
				addButton(btn);
			}
		};
		scenes.put(scene.getSceneID(), scene);

		scene = new Scene("Stats", "Menu") {
			@Override
			public void load(Parser p) {
				setContent(player.getStats(flags));
				loadButtons();
			}

			// TODO DISABLE THIS DURING INOPPERTUNE TIMES (can only use when traversing)
			public void loadButtons() {
				clearButtons();

				CustomButton btn;

				// TODO WHAT TO DO IF INVENTORY HAS TOO MANY ITEMS TO DISPLAY
				for (Item item : player.getInv().accessItems()) {
					btn = new CustomButton(item.getName());
					btn.addMouseListener(new MouseAdapter() {
						public void mouseClicked(MouseEvent e) {
							// Depending on the type of item being used, execute the respective use methods.
							if (item instanceof Consumable)
								updateScene(((Consumable) item).applyEffect(player));
							else if (item instanceof Equippable)
								updateScene(((Equippable) item).equip(player));
							else
								appendScene("<br>This item cannot be used.");
							player.getInv().remove(item);

							buttonContainer.removeAll();
							CustomButton btn = new CustomButton("Next");
							btn.addMouseListener(new MouseAdapter() {
								public void mouseClicked(MouseEvent e) {
									loadScene("StatsMenu");
								}
							});
							buttonContainer.add(btn);
							refreshGUI();
						}
					});
					btn.setToolTipText(item.getDescrip());
					addButton(btn);
				}

			}
		};
		scenes.put(scene.getSceneID(), scene);

		scene = new Scene("SceneName", "Group") {
			public void loadButtons() {
				clearButtons();

				CustomButton btn = new CustomButton("Button text");
				btn.addMouseListener(new MouseAdapter() {
					public void mouseClicked(MouseEvent e) {
						
					}
				});
				btn.setToolTipText("Description");
				addButton(btn);

			}
		};
		scenes.put(scene.getSceneID(), scene);


		/*
		 * Template SCENE
		 * 
		 * 
		 * 
		 * 
		 * scene = new Scene("", "") {
		 * 
		 * public void loadButtons() { clearButtons();
		 * 
		 * CustomButton btn = new CustomButton(""); btn.addMouseListener(new
		 * MouseAdapter() { public void mouseClicked(MouseEvent e) { changeScene(""); }
		 * }); addButton(btn); } }; scenes.put(scene.getSceneID(), scene);
		 * 
		 */

		return scenes;
	}

	/**
	 * Changes the scene to the one given, disposing of the previous scene.
	 * 
	 * @param sceneTitle
	 *            the title of the scene to be loaded
	 */
	public void changeScene(String sceneTitle) {
		// Clear away the previous scene (maybe call end of scene method that
		// does stuff like setting booleans?)
		// scene.end();
		// OR
		// scene.dispose();

		Scene nextScene = scenes.get(sceneTitle);

		try {
			loadScene(sceneTitle);
			priorScene = getCurrentScene();
			setCurrentScene(nextScene);

		} catch (NullPointerException npe) {
			// If the connecting scene hasn't been added to the hashmap, alert the player
			// with an error message.
			appendScene(
					"<br><br><br><p style=\"font-family:courier;color:red;\"><b>Uh oh! That button leads to no scene. Try something else and report this error.</b></p>"
							+ "<p style=\"font-family:courier;\"><b>Details</b>: Attempted to reach scene titled: <i>"
							+ sceneTitle + "</i> from scene: <i>" + getCurrentScene().getSceneID()
							+ "</i>. But no such scene exists!</p>");

			// Create a button to allow the player to restart
			CustomButton btn = new CustomButton("RESTART");
			btn.addMouseListener(new MouseAdapter() {
				public void mouseClicked(MouseEvent e) {
					changeScene("BeginningMisc");
				}
			});
			buttonContainer.add(btn);
			refreshGUI();

			LOGGER.log(Level.SEVERE,
					"NullPointerException occured attempting to reach scene titled:" + sceneTitle + ", from scene:"
							+ getCurrentScene().getSceneID() + ". But no such scene exists!\n" + npe.toString(),
					npe);
			System.out.println("No such scene " + sceneTitle + " from scene: " + getCurrentScene().getSceneID());
			// npe.printStackTrace();
		}

		// Attempt to update the scene history
		sceneHistory.addSceneText(currentScene.getContent());
		if (priorScene != null && !currentScene.getTitle().equals("Beginning"))
			sceneHistory.updatePrev(priorScene.getContent());

		// Set Scene history button style
		sceneHistoryBtns.getComponent(1).setForeground(Color.LIGHT_GRAY);
		sceneHistoryBtns.getComponent(0).setForeground(Color.BLACK);


		 System.out.println(status);
	}

	/**
	 * Takes a specified scene, loads from file and parses it. The textpane is then
	 * updated with that content.
	 * <p>
	 * This also updates the available buttons.
	 * <p>
	 * <strong>Note</strong>: It is recommended that this not be used directly and
	 * that changeScene be called instead, as this bypasses certain code that must
	 * be executed upon changing a scene.
	 * 
	 * @throws NullPointer
	 *             if no such scene exists.
	 * @param sceneTitle
	 *            the title of the scene to be loaded
	 */
	private void loadScene(String sceneTitle) throws NullPointerException {
		Scene sceneToLoad = scenes.get(sceneTitle);
		if (sceneToLoad != null) {
			sceneToLoad.load(parser);

			updateScene(sceneToLoad.getContent());

			// Set buttons onto the ButtonContainer
			reloadButtons(sceneToLoad);

			// TODO add extra method to add extra buttons depending on certain
			// circumstances, probably given certain conditions

			refreshGUI();
		} else
			throw new NullPointerException();
	}

	// Removes all buttons from the button continer and adds the buttons from the
	// given scene
	private void reloadButtons(Scene sceneToLoad) {
		buttonContainer.removeAll();
		for (CustomButton button : sceneToLoad.getButtons())
			buttonContainer.add(button);
	}

	/**
	 * Changes the text within the textPane.
	 * 
	 * @param content
	 *            To display on the textPane.
	 */
	public void updateScene(String content) {
		// Set text into pane
		getTextPane().setText(content);

		// Ensures text begins at the beginning
		getTextPane().setCaretPosition(0);
	}

	/**
	 * Adds the given text to the end of the scene without changing cursor position.
	 * Disappears after scene change.
	 * 
	 * @param content
	 *            String to add to the end of the present scene's text.
	 */
	public void appendScene(String content) {
		getTextPane().setText(getCurrentScene().getContent() + content);
	}

	/**
	 * Adds the given text to the scene's content. Persists.
	 * 
	 * @param text
	 *            String to add to the end of the present scene's content.
	 */
	public void appendContent(String text) {
		getTextPane().setText(getCurrentScene().append(text));
	}

	// Fixes the JComponents after things have changed
	private void refreshGUI() {
		game.repaint();
		game.revalidate();
	}

	/**
	 * Attempts to go back to the previous scene. As with all time travel, is prone
	 * to tearing space-time continuity.
	 */
	public void rewind() {
		updateScene(priorScene.getContent());

		// Set buttons onto the ButtonContainer
		buttonContainer.removeAll();
		for (CustomButton button : priorScene.getButtons()) {
			buttonContainer.add(button);
		}

		refreshGUI();
	}

	/**
	 * Displays the set content for the scene, removing any appended text that
	 * wasn't originally a part of the scene's content, or wasn't added.
	 */
	public void refreshScene() {
		updateScene(getCurrentScene().getContent());
	}

	/**
	 * Short hand for handling and updating a flag's value
	 * 
	 * @param flag
	 *            the String name of the flag to set
	 * @param boolean
	 *            value to set the flag to
	 */
	private void setFlag(String flag, boolean bool) {
		if (flags.put(flag, bool) == null) {
			LOGGER.severe("Tried setting value of '" + flag + "', but no such flag exists.");
		}
	}

	// Convenience method to create the stats button
	private void initStatsBtn() {
		menuBtn = new CustomButton("Menu", 50, 40);

		menuBtn.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				// If not in player menu, save current scene and load up menu
				// Otherwise, display scene text and reload buttons.
				// NOTE: This may change the original buttons shown, but not change text for the
				// scene!!!
				
				if (!menuDisplayed) {
					suspendedScene = currentScene;
					loadScene("StatsMenu");
					sceneHistoryBtns.setVisible(false);
					menuBtn.setText("Back");
					menuBtn.setToolTipText("Close player menu.");
				} else {	// Returning from menu to game
					updateScene(suspendedScene.getContent());
					currentScene = suspendedScene;
					reloadButtons(currentScene);
					sceneHistoryBtns.setVisible(true);
					menuBtn.setText("Menu");
					menuBtn.setToolTipText("Open player menu.");
				}
				menuDisplayed = !menuDisplayed;
				refreshGUI();
			}
		});

		GridBagConstraints c = new GridBagConstraints();
		c.gridx = 0;
		c.gridy = 0;
		c.gridwidth = 1;
		c.gridheight = 1;
		c.insets = new Insets(0, 10, 2, 0);
		menuBtn.setVisible(false);
		game.buttonContainer.add(menuBtn, c);
	}

	// Convenience method to create the info button
	private void initInfoBtn() {
		infoBtn = new CustomButton("Info", 40, 40);

		infoBtn.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				if (!menuDisplayed) {
					updateScene("Info");
					menuDisplayed = true;
				} else {
					updateScene(currentScene.getContent());
					menuDisplayed = false;
				}
			}
		});
		infoBtn.setToolTipText("Info");
		GridBagConstraints c = new GridBagConstraints();
		c.gridx = 0;
		c.gridy = 0;
		c.gridwidth = 1;
		c.gridheight = 1;
		c.insets = new Insets(0, 10, 2, 0);

		infoBtn.setVisible(false);
		game.buttonContainer.add(infoBtn, c);
	}

	// TODO add toggle method to custom button...?
	// Instantiate the scene history buttons
	private void initSceneHistoryBtns() {
		// TODO replace with icons
		CustomButton backBtn = new CustomButton("Back", 60, 40);
		CustomButton forwardBtn = new CustomButton("Forward", 60, 40);

		backBtn.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				updateScene(sceneHistory.getPrevScene(currentScene.getContent()));
				sceneHistoryBtns.getComponent(sceneHistory.atLimit() == 2 ? 0 : 1)
						.setForeground(sceneHistory.atLimit() == 2 ? Color.LIGHT_GRAY : Color.BLACK);
				refreshGUI();
			}
		});

		forwardBtn.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				updateScene(sceneHistory.getNextScene());
				sceneHistoryBtns.getComponent(sceneHistory.atLimit() == 1 ? 1 : 0)
						.setForeground(sceneHistory.atLimit() == 1 ? Color.LIGHT_GRAY : Color.BLACK);
				refreshGUI();
			}
		});

		sceneHistoryBtns.add(backBtn);
		sceneHistoryBtns.add(forwardBtn);

		GridBagConstraints c = new GridBagConstraints();
		c.gridx = 2;
		c.gridy = 0;
		c.gridwidth = 1;
		c.gridheight = 1;
		c.insets = new Insets(0, 0, 0, 5);
		sceneHistoryBtns.setVisible(false);
		sceneHistoryBtns.setOpaque(false);
		game.buttonContainer.add(sceneHistoryBtns, c);
	}

	// Initializes the values to the beginning of the game
	private void initDefaultValues() {
		// Reloading the parser with the original flag values
		flags = loadFlags(FLAGS_PATH);
		parser = new Parser(flags, new TokenMapCreator().createFor(player));

		player = new Player();

		sceneHistory = new SceneHistory();
	}

	/**
	 * Deletes the button with the given text Convenience function that removes
	 * finds and removes the button with the given text. Returns whether or not the
	 * operation was successful.
	 */
	private Boolean deleteButton(String buttonText) {
		CustomButton toDelete = null;
		for (Component butn : buttonContainer.getComponents())
			if (((CustomButton) butn).getText().equals(buttonText)) {
				toDelete = (CustomButton) butn;
				break;
			}
		if (toDelete != null) {
			buttonContainer.remove(toDelete);
			refreshGUI();
			return true;
		} else
			return false;
	}

	// Loads in the flags from the specified flagDictionary
	private Map<String, Boolean> loadFlags(String flagPath) {
		Map<String, Boolean> flags = new HashMap<String, Boolean>();

		BufferedReader in;
		InputStream is;
		try {
			is = this.getClass().getResourceAsStream(flagPath);
			in = new BufferedReader(new InputStreamReader(is));
			String line = "";

			// Cycle until no more lines left in file, and split string into
			// each piece of the hashmap
			while ((line = in.readLine()) != null) {
				String[] parts = line.split("\t");

				if (parts[1].equals("true"))
					flags.put(parts[0], true);
				else if (parts[1].equals("false"))
					flags.put(parts[0], false);
				else {
					flags.put(parts[0], false);
					System.out
							.println("Flag Dictionary entry not in valid form.\nSetting '" + parts[0] + "' to false.");
					LOGGER.warning("Flag Dictionary entry not in valid form.\nSetting '" + parts[0] + "' to false.");
				}
			}
			in.close();
		} catch (IOException ioe) {
			LOGGER.log(Level.SEVERE, "IOExcetion occured while trying to read in flagDictionary:\n" + ioe.toString(),
					ioe);
			ioe.printStackTrace();
		} catch (ArrayIndexOutOfBoundsException aiobe) {
			LOGGER.log(Level.SEVERE, "ArrayIndexOutOfBoundsException occured while trying to read in flagDictionary:\n"
					+ aiobe.toString(), aiobe);
			aiobe.printStackTrace();
		} catch (NullPointerException npe) {
			LOGGER.log(Level.SEVERE,
					"NullPointerException occured while trying to read in flagDictionary:\n" + npe.toString(), npe);
			npe.printStackTrace();
		}
		return flags;
	}

	// Getters and Setters

	public Scene getCurrentScene() {
		return currentScene;
	}

	public void setCurrentScene(Scene currentScene) {
		this.currentScene = currentScene;
	}

	public JPanel getButtonContainer() {
		return buttonContainer;
	}

	public JEditorPane getTextPane() {
		return textPane;
	}
}