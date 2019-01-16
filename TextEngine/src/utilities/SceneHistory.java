package utilities;

/**
 * Utility class to store the previous scenes encountered so that the player may
 * go back to read them. Employs a cyclic array.
 */
public class SceneHistory {
	// Array containing the text from the scenes
	private String[] scenes;

	// Integers to keep track of the contents
	private int beginning;
	private int end;
	private int stored;
	private int onSceneIndex;

	/**
	 * Create a new Scene History that stores a max of the specified number of
	 * scenes
	 * 
	 * @param size
	 */
	public SceneHistory(int size) {
		scenes = new String[size];
		beginning = 0;
		end = 0;
		stored = 0;
		onSceneIndex = -1;
	}

	/**
	 * Creates a Scene History with a default max of 10 scenes
	 */
	public SceneHistory() {
		this(10);
	}

	/**
	 * Takes a given scene's text and adds it to the history.
	 * 
	 * @param content
	 */
	public void addSceneText(String content) {
		// If the array isn't filled, just keep adding scenes to fill it up. If
		// it is filled, shift the array so that the oldest entry is lost and
		// replaced with newer entry.
		if (stored != scenes.length) {
			stored++;
			onSceneIndex = stored - 1;
			end = onSceneIndex;
		} else {
			beginning = (beginning + 1) % scenes.length;
			end = (end + 1) % scenes.length;
			onSceneIndex = end;
		}

		scenes[onSceneIndex] = content;
	}

	/**
	 * Updates the previous scene's content, stored in history, in case of any
	 * additions made to the scene content.
	 * 
	 * @param content
	 *            Previous scene's content
	 */
	public void updatePrev(String content) {
		// System.out.println(beginning + " " + onSceneIndex + " " + end + " out of " +
		// stored);
		if (end == 0)
			scenes[stored - 2] = content;
		else
			scenes[onSceneIndex - 1] = content;
	}

	/**
	 * Traverses backwards through the scene history and returns that scene's text.
	 * Updates the current scene before going back. Stops at the oldest entry.
	 * 
	 * @param current
	 *            the text of the current scene to preserve.
	 * @return Text for the scene that came before this one in time.
	 */
	public String getPrevScene(String current) {

		if (onSceneIndex != beginning) {
			scenes[end] = current;
			onSceneIndex = ((onSceneIndex - 1) + scenes.length) % scenes.length;
		}
		return (scenes[onSceneIndex]);
	}

	/**
	 * Traverses forwards through the scene history and returns that scene's text.
	 * Stops at the current scene.
	 * 
	 * @return Text for the scene that came before this one in time.
	 */
	public String getNextScene() {
		if (onSceneIndex != end) {
			if (onSceneIndex == 9)
				onSceneIndex = 0;
			else
				onSceneIndex = (onSceneIndex + 1) % scenes.length;
		}
		return (scenes[onSceneIndex]);

	}

	/**
	 * Checks whether the currently viewed scene in the scene history is at the end
	 * or beginning.
	 * 
	 * @return 1 if at end (most recent), 2 if at beginning (oldest), 0 otherwise.
	 */
	public int atLimit() {
		return onSceneIndex == end ? 1 : onSceneIndex == beginning ? 2 : 0;
	}

	/**
	 * Change how many scenes long the SceneHistory is. Cannot be fewer than 3
	 * scenes. WARNING: increasing the SceneHistory too much may result in great
	 * memory usage. May take a few scenes before effects are noticed.
	 * 
	 * @param stored
	 */
	public void setStored(int stored) {
		if (stored < 3)
			this.stored = 3;
		else
			this.stored = stored;
	}
}