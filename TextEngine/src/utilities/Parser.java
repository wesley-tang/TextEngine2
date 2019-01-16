package utilities;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import java.util.Map;
import java.util.function.Supplier;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Creates a Parser which loads in and interprets text in a specified file.
 * 
 * Supported features includes adding HTML new lines automatically when a new
 * line is encountered. Ignoring commented lines denoted by anything after "//".
 * 
 * <p>
 * For how it interprets text, refer to Scene_Writing_Info
 */
public class Parser {
	// Hashmap of flags
	public Map<String, Boolean> flags;

	// Hashmap of string vars, such as name.
	private Map<String, Supplier<String>> tokens;

	// NOTE (The hashmap's key and mapped value must be declared within a
	// method)

	// TODO add auto <i> insert for quotations using boolean

	// TODO add integer variable recognition to the parser using == (=), -= (<=) ,
	// += (>=), -- (<), ++(>)?
	// it could retain the flag checking syntax by replacing the 'flag' with the
	// variable condition (is there the above syntax?)

	/**
	 * Creates a parser that can parse through given text
	 * 
	 * @param flags
	 *            A hashmap containing the flags from which to parse
	 */
	public Parser(Map<String, Boolean> flags, Map<String, Supplier<String>> tokens) {
		// Var init
		this.flags = flags;
		this.tokens = tokens;
	}

	/**
	 * Reads in the file specified at the given URL and converts it to text to be
	 * displayed within the text pane. Token replaces and
	 * 
	 * @param url
	 * @return Parsed content
	 */
	public String parse(String url) {
		String content = "";

		// Reads in the content and stores it into a string
		BufferedReader in;
		InputStream is;

		try {
			is = this.getClass().getResourceAsStream(url);
			in = new BufferedReader(new InputStreamReader(is));

			String line = ""; // Contains the current line of text

			// Places new lines automatically
			while ((line = in.readLine()) != null)
				content += (line + "\n<br>");

			in.close();

		} catch (FileNotFoundException fnfe) {
			fnfe.printStackTrace();
			// TODO If error occurs, give button to send them back to previous
			return "File not found, unable to locate " + url;
		} catch (IOException ioe) {
			ioe.printStackTrace();
			return "Input-output error, unable to read in file.";
		} catch (NullPointerException npe) {
			npe.printStackTrace();
			return "File not found, unable to locate " + url;
		}

		content = commentReplace(content);
		content = conditionalReplace(content);
		content = tokenReplace(content);
		content = italicizeQuotes(content);

		return content;
	}

	/**
	 * Replaces the given tokens with the appropriate text based on the player's
	 * variables. Tokens take the form [token].
	 * 
	 * @param content
	 *            To be parsed
	 * @return Parsed content
	 */
	public String tokenReplace(String content) {
		String temp = content;

		// Finds and replaces of text in the form [-anything in here-]
		Pattern pattern = Pattern.compile("\\[(.+?)\\]");
		Matcher matcher = pattern.matcher(temp);
		StringBuffer buffer = new StringBuffer();

		while (matcher.find()) {
			String replacement = tokens.get(matcher.group(0)).get();
			if (replacement != null) {
				matcher.appendReplacement(buffer, "");
				buffer.append(replacement);
			}
		}
		matcher.appendTail(buffer);
		content = buffer.toString();

		return content;
	}

	/**
	 * Replaces the conditional with the appropriate thing depending on the given
	 * flag
	 * 
	 * All conditionals must be of the form {flag@trueText|falseText}
	 * 
	 * TODO to support nested conditionals, specify different regex cases. { _ @ _
	 * }, {! _ @ _ }, {
	 * https://docs.oracle.com/javase/tutorial/essential/regex/quant.html Use above
	 * link to determine how to choose the proper type to allow nested.
	 */
	public String conditionalReplace(String content) {
		String temp = content;

		Pattern pattern = Pattern.compile("\\{(.+?)@(.+?)\\}");
		Matcher matcher = pattern.matcher(temp);
		StringBuffer buffer = new StringBuffer();

		while (matcher.find()) {
			String conditionalText = matcher.group(0);

			// System.out.println(conditionalText);

			// Trims the { }
			conditionalText = conditionalText.substring(1, matcher.group(0).length() - 1);

			// System.out.println(conditionalText);

			// Splits the flag from the conditional text
			String[] conditional = conditionalText.split("@");

			// TODO nested if statements don't seem possible?
			// System.out.println(String.join("@", Arrays.copyOfRange(conditional, 1,
			// conditional.length)));
			// while (conditional.length > 2) {
			// conditionalText = String.join("@", Arrays.copyOfRange(conditional, 1,
			// conditional.length));
			// conditionalReplace(conditionalText, flags);
			// conditional = conditionalText.split("@");
			// }

			// Set the final text as the unparsed conditional text by default
			String replacement = matcher.group(0);

			// System.out.println(conditional[0]);

			try {

				boolean flag = flags.get(conditional[0]);

				String[] possibleText = conditional[1].split("\\|");

				// In the case {flag@text|}, where only display text if true
				if (possibleText.length == 1 && flag)
					replacement = possibleText[0];
				// In the case {flag@text1|text2}, where display text1 if true, text 2 if false
				// Also works for {flag@|text}, where display text if false
				else if (possibleText.length == 2) {
					if (flag)
						replacement = possibleText[0];
					else
						replacement = possibleText[1];
				} else
					replacement = "";

			} catch (NullPointerException npe) {
				// TODO Log this error
				npe.printStackTrace();
			} catch (ArrayIndexOutOfBoundsException aiobe) {
				aiobe.printStackTrace();
			}

			matcher.appendReplacement(buffer, "");
			buffer.append(replacement);
		}
		matcher.appendTail(buffer);
		content = buffer.toString();

		return content;
	}

	/**
	 * Finds all quotations and adds HTML tags to italicize everything said inside of them.
	 * Ignores quotation marks inside of HTML tags.
	 * 
	 * @param content
	 * @return parsed content
	 */
	public String italicizeQuotes(String content) {
		boolean startQuote = true;
		boolean inTag = false;

		// Finds a " or < or >
		Pattern pattern = Pattern.compile("\"|<|>");
		Matcher matcher = pattern.matcher(content);
		StringBuffer buffer = new StringBuffer();

		while (matcher.find()) {
			if (matcher.group(0).equals("<"))
				inTag = true;
			else if (matcher.group(0).equals(">"))
				inTag = false;
			else if (!inTag) {
				matcher.appendReplacement(buffer, "");
				buffer.append(startQuote ? "\"<i>" : "</i>\"");
				startQuote = !startQuote;
			}
		}
		matcher.appendTail(buffer);
		content = buffer.toString();
		return content;
	}

	// Removes comments, given in the inline form of comments in java,
	// "// comment here" terminating with a new line
	private String commentReplace(String content) {
		String temp = content;

		Pattern pattern = Pattern.compile("<br>//(.+?)\n");
		Matcher matcher = pattern.matcher(temp);
		StringBuffer buffer = new StringBuffer();

		while (matcher.find()) {
			matcher.appendReplacement(buffer, "");
			buffer.append("");
		}
		matcher.appendTail(buffer);
		content = buffer.toString();

		return content;
	}
}