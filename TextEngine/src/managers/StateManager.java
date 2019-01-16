package managers;

import javax.swing.JFrame;

import states.LoadingScreen;
import states.State;

public class StateManager {

	private State currentState;

	// Different States
	private LoadingScreen loadingScreen;
	private State game;

	private JFrame window;

	/**
	 * Manager to handle the various States that exist within the game.
	 * Automatically starts from loading state.
	 * 
	 * @param window
	 *            JFrame with which the State is displayed upon.
	 */
	public StateManager(JFrame window) {
		this.window = window;

		loadStart();
		currentState = loadingScreen;
		window.setContentPane(currentState);
		
		loadGame();
	}

	// Activates the loading screen.
	private void loadStart() {
		loadingScreen = new LoadingScreen();
	}

	/**
	 * Tells the loading screen to load everything up. Once it completes this task,
	 * it changes scenes.
	 */
	public void loadGame() {
		game = loadingScreen.loadGame();
		currentState = game;

		window.setContentPane(currentState);
		// TODO NEED CONCURENCY???

		// http://docs.oracle.com/javase/tutorial/uiswing/concurrency/index.html
		// https://stackoverflow.com/questions/27474752/is-there-a-way-to-know-if-a-jpanel-is-finished-loading-its-content
	}

	/**
	 * @return Current State of the program.
	 */
	public State getCurrentState() {
		return currentState;
	}
}