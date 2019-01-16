package misc;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileFilter;
import java.io.FileWriter;
import java.io.InputStream;
import java.io.InputStreamReader;

import java.net.URL;

/**
 * Generates a document containing all scenes in one file for rapid search of
 * all scenes. Alternatively returns word count of all files.
 */
public class DocGenerator {

	public static DocGenerator dg;

	public DocGenerator() {
		dg = this;
	}

	public static void main(String[] args) {
		generateDoc();

		System.out.println("\nApprox word count: " + wordCount());
	}

	/**
	 * Returns an approximate word count of all scenes. This may include bits and
	 * pieces of the HTML and Parser code.
	 */
	public static long wordCount() {
		long count = 0;

		DocGenerator dg = new DocGenerator();

		URL url = dg.getClass().getResource("/Scenes");

		File file = new File(url.getPath());

		// Getting the folder names
		File[] files = file.listFiles(new FileFilter() {
			@Override
			public boolean accept(File f) {
				return f.isDirectory();
			}
		});

		StringBuilder content = new StringBuilder();

		// Read in all files
		for (File location : files) {
			String[] text = location.toString().split("/");

			// Obtain just the location's name
			String locationName = text[text.length - 1];

			// System.out.println(locationName);

			File[] scenes = location.listFiles();

			// Run for each scene in the folder
			for (File scene : scenes) {
				String[] text1 = scene.toString().split("/");

				String sceneTitle = text1[text1.length - 1];

				// System.out.println(sceneTitle + locationName);

				// Reads in the content and stores it into a string
				BufferedReader in;
				InputStream is;

				try {
					is = dg.getClass().getResourceAsStream("/Scenes/" + locationName + "/" + sceneTitle);
					in = new BufferedReader(new InputStreamReader(is));

					String line = ""; // Contains the current line of text

					// Places new lines automatically
					while ((line = in.readLine()) != null)
						content.append(line).append(" ");

					in.close();

				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}

		String[] contents = content.toString().split(" ");

		// (Try) Counting all the non-word strings
		for (String s : contents) {
			if (!s.equals("") && !s.equals("-")) {
				count++;
				// System.out.println(s);
			}
		}

		return count;
	}

	/**
	 * Generates a text file, 'Content.txt', with all scenes inside.
	 */
	public static void generateDoc() {
		DocGenerator dg = new DocGenerator();

		URL url = dg.getClass().getResource("/Scenes");

		File file = new File(url.getPath());

		File[] files = file.listFiles(new FileFilter() {
			@Override
			public boolean accept(File f) {
				return f.isDirectory();
			}
		});

		StringBuilder content = new StringBuilder();

		// Read in all files
		for (File location : files) {
			String[] text = location.toString().split("/");

			String locationName = text[text.length - 1];

			System.out.println(locationName);

			content.append("\n\n\t\t------" + locationName + "-------\n\n");

			File[] scenes = location.listFiles();

			for (File scene : scenes) {
				String[] text1 = scene.toString().split("/");

				String sceneTitle = text1[text1.length - 1];

				String sceneName = sceneTitle + locationName;

				System.out.println(sceneName);

				content.append("\n\t*" + sceneName + "*\n\n");

				// Reads in the content and stores it into a string
				BufferedReader in;
				InputStream is;

				try {
					is = dg.getClass().getResourceAsStream("/Scenes/" + locationName + "/" + sceneTitle);
					in = new BufferedReader(new InputStreamReader(is));

					String line = ""; // Contains the current line of text

					// Places new lines automatically
					while ((line = in.readLine()) != null)
						content.append(line + "\n");

					content.append("\n");

					in.close();

				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}

		// System.out.println(content);

		// Print out to a file
		try {

			BufferedWriter fileOut = null;

			fileOut = new BufferedWriter(new FileWriter("./rsc/Content.txt"));
			fileOut.write(content.toString());
			fileOut.close();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}