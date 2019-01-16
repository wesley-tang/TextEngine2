package utilities;

import java.util.ArrayList;

/**
 * Abstract class from which all scenes are derived.
 */
public abstract class Scene {
	private final String TITLE;
	// This groups the scenes to their respective Blocks. Blocks indicate certain
	// 'plots' or scenes that are linked with one central event. There are scene
	// blocks for a scenes grouped by a 'linear' path and traversal blocks which
	// indicate a group of scenes in a general location that has no set end and can
	// be repeated.
	private final String BLOCK;
	private final String CONTENT_URL;
	public final String SCENE_ID;
	private final String SCENES_URL = "/Scenes";

	private String content;
	private ArrayList<CustomButton> buttons;

	/**
	 * Creates a scene from the given title and block.
	 * 
	 * @param title
	 * @param block
	 */
	public Scene(String title, String block) {
		TITLE = title;
		BLOCK = block;
		SCENE_ID = title + block;
		CONTENT_URL = SCENES_URL + "/" + BLOCK + "/" + TITLE;
		content = "No content to display. You should not be seeing this.";
		buttons = new ArrayList<CustomButton>();
	}

	// /**
	// * Constructor for any scene that sets the title and location, but also
	// gives an extra group for the scene to be with, in the location
	// *
	// * @param title
	// * @param location
	// */
	// public Scene(String title, String location, String group){
	// this.TITLE = title;
	// this.LOCATION = location;
	// CONTENT_URL = scenesLocationURL + "/" + LOCATION + "/" + group + "/" +
	// TITLE;
	// content = "";
	// buttons = new ArrayList<CustomButton>();
	// }

	/**
	 * Calls to this method will read in the content stored in this scene's text
	 * file. Flags will be checked and set to determine content and choices to other
	 * scenes.
	 * 
	 * Override to add further functionality.
	 */
	public void load(Parser p) {
		setContent(p.parse(getContentURL()));
		loadButtons();
	}

	/**
	 * Compliment to load, but only loads the buttons into the scene. Needs ot be
	 * overrided.
	 */
	public abstract void loadButtons();

	// TODO Is it bad to create a static parser for all buttons?
	// TODO Is it bad to pass a reference to the SceneManager for space reasons????
	
	/**
	 * Returns the content contained within the scene
	 * 
	 * @return content
	 */
	public String getContent() {
		return content;
	}

	/**
	 * Sets the content contained within the scene
	 * 
	 * @param content
	 */
	public void setContent(String content) {
		this.content = content;
	}

	/**
	 * Returns the scene's title
	 * 
	 * @return TITLE
	 */
	public String getTitle() {
		return TITLE;
	}

	/**
	 * Returns the scene's block, or in other words, the name of the folder it is
	 * in.
	 * 
	 * @return BLOCK
	 */
	public String getBlock() {
		return BLOCK;
	}

	/**
	 * Returns the url to the scene's content, relative from the rsc folder
	 * 
	 * @return CONTENT_URL
	 */
	public String getContentURL() {
		return CONTENT_URL;
	}

	/**
	 * Returns the scene's ID, composed of the title followed immediately by the
	 * location
	 * 
	 * @return SCENE_ID
	 */
	public String getSceneID() {
		return SCENE_ID;
	}

	/**
	 * Returns the ArrayList of buttons within this scene.
	 * 
	 * @return
	 */
	public ArrayList<CustomButton> getButtons() {
		return buttons;
	}

	/**
	 * Adds the specified button to the scene's buttons.
	 * 
	 * @param button
	 *            to be added
	 */
	public void addButton(CustomButton button) {
		buttons.add(button);
	}

	/**
	 * Removes the current buttons that this scene has so that loading the scene
	 * more than once won't create duplicates.
	 */
	public void clearButtons() {
		buttons.clear();
	}

	/**
	 * Adds the given text to the end of the scene.
	 * 
	 * @param text
	 *            to add.
	 * @return content of the scene with text appended.
	 */
	public String append(String text) {
		return content += text;
	}
}