package misc;

import java.awt.event.MouseEvent;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.util.concurrent.ThreadLocalRandom;

import javax.swing.JPanel;

import states.Game;
import states.LoadingScreen;
import utilities.CustomButton;
import utilities.Scene;

/**
 * Creates a test version of the game that executes a given number of random
 * button presses in the possible choices at each scene to attempt to identify
 * exceptions or errors.
 * <p>
 * Also
 *
 */
public class Tester {

	// Variables to determine how the Tester will function
	// Set number of button presses to test by changing ITERATIONS
	final static int ITERATIONS = 100;
	// Set GENDOC to true to create a sample doc of playthroughs
	final static boolean GEN_DOC = true;
	
	// Set ALLBTNS to true to test every button, not just the scene navigation buttons 
	//TODO
	
	public static void main(String[] args) {		
		LoadingScreen ls = new LoadingScreen();
		Game testGame = ls.loadGame();
		
		System.out.println("Initialization complete, beginning tests.\n");

		JPanel btnCont = testGame.getSceneManager().getButtonContainer();

		// Strings for doc generation
		StringBuilder content = new StringBuilder();
		String buffer = "";

		// Holding scenes for the doc generation
		Scene previousScene = null;
		Scene currentScene = null;

		// Repeat button presses for however many iterations
		for (int i = 0; i < ITERATIONS; i++) {
			// System.out.println("Iteration " + i);
			int randomNum = ThreadLocalRandom.current().nextInt(0, btnCont.getComponents().length);
			System.out.println(randomNum);

			CustomButton btnToClick = (CustomButton) btnCont.getComponent(randomNum);

			System.out
					.println(testGame.getSceneManager().getCurrentScene().getSceneID() + " \t " + btnToClick.getText());

			MouseEvent me = new MouseEvent(testGame, 0, 0, 0, 100, 100, 1, false);

			btnToClick.getMouseListeners()[1].mouseClicked(me);

			// Code to print out a random run through
			if (GEN_DOC) {
				currentScene = testGame.getSceneManager().getCurrentScene();
				if (testGame.getSceneManager().getCurrentScene().getSceneID().equals("beginning scene code")) {
					content.append("<br><br>------------------<br><br>");
					printDoc(content.toString());
				} else if (!(previousScene == currentScene)) {
					if (buffer.equals(""))
						content.append(testGame.getSceneManager().getTextPane().getText()).append("<br>");
					else {
						// TODO fix the off by one scene bug
						content.append(buffer);
						buffer = "";
					}

				} else
					buffer = testGame.getSceneManager().getTextPane().getText() + "<br>";
				previousScene = currentScene;
			}
		}
		System.out.println("\n> TESTS COMPLETE <");
		System.exit(0);
	}

	// Prints content to a document
	private static void printDoc(String content) {
		// Print out to a file
		try {

			BufferedWriter fileOut = null;

			fileOut = new BufferedWriter(new FileWriter("./rsc/Sample.html"));
			fileOut.write(content);
			fileOut.close();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}