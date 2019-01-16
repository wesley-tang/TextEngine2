package misc.tests;

import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.function.Supplier;

import org.junit.Before;
import org.junit.Test;

import game.Player;
import utilities.Parser;

/**
 * Performs tests on the parser.
 *
 */
public class ParserTest {
	// Player class to be used within each test
	Player p;

	@Before
	public void setUp() {
		p = new Player();
	}

	// Testing TOKENS -> [token_name]
	@Test
	public void testNormalTokenReplacement() {
		HashMap<String, Supplier<String>> tokens = new HashMap<String, Supplier<String>>();
		tokens.put("[test]", () -> "test");

		Parser testP = new Parser(null, tokens);

		assertEquals("test", testP.tokenReplace("[test]"));
	}

	@Test
	public void testBrokenTokenReplacement() {
		HashMap<String, Supplier<String>> tokens = new HashMap<String, Supplier<String>>();
		tokens.put("[test]", () -> "test");

		Parser testP = new Parser(null, tokens);

		assertEquals("[test[", testP.tokenReplace("[test["));
	}

	// TODO nested token replacement doesn't relaly work either, but should it even?
//	@Test
//	public void testNestedTokenReplacement() {
//		HashMap<String, Supplier<String>> tokens = new HashMap<String, Supplier<String>>();
//		tokens.put("[one]", () -> "one");
//		tokens.put("[onetest]", () -> "test");
//
//		Parser testP = new Parser(null, tokens);
//
//		assertEquals("test", testP.tokenReplace("[[one]test]"));
//	}

	@Test
	public void testMultipleTokenReplacements() {
		HashMap<String, Supplier<String>> tokens = new HashMap<String, Supplier<String>>();
		tokens.put("[test]", () -> "test");

		Parser testP = new Parser(null, tokens);

		assertEquals("testtest", testP.tokenReplace("[test][test]"));
	}

	// Testing CONDITIONAL -> {flag@text_if_true|text_if_false}
	@Test
	public void testNormalConditionalReplacement() {
		HashMap<String, Boolean> flags = new HashMap<String, Boolean>();
		flags.put("bool", true);

		Parser testP = new Parser(flags, null);

		assertEquals("test", testP.conditionalReplace("{bool@test|fail}"));
	}

	@Test
	public void testMissingFlagConditionalReplacement() {
		HashMap<String, Boolean> flags = new HashMap<String, Boolean>();

		Parser testP = new Parser(flags, null);

		assertEquals("{bool@test|test1}", testP.conditionalReplace("{bool@test|test1}"));
	}
	
	
	// TODO Nested conditional replacement
//	@Test
//	public void testNestedConditionalReplacement() {
//		HashMap<String, Boolean> flags = new HashMap<String, Boolean>();
//		flags.put("bool", true);
//		
//		Parser testP = new Parser(flags, null);
//
//		assertEquals("testyes", testP.conditionalReplace("{bool@test{bool@yes|no}|test1}"));
//	}

	@Test
	public void testMultipleConditionalReplacements() {
		HashMap<String, Boolean> flags = new HashMap<String, Boolean>();
		flags.put("bool", true);
		flags.put("bool2", false);

		Parser testP = new Parser(flags, null);

		assertEquals("testtest", testP.conditionalReplace("{bool@test|fail}{bool2@fail|test}"));
	}
	
	@Test
	public void testQuoteReplacement() {
		Parser testP = new Parser(null, null);

		assertEquals("\"<i>TEST</i>\"", testP.italicizeQuotes("\"TEST\""));
	}
	
	@Test
	public void testQuoteReplacementMultiple() {
		Parser testP = new Parser(null, null);

		assertEquals("\"<i>TEST</i>\"\"<i></i>\"", testP.italicizeQuotes("\"TEST\"\"\""));
	}

	//
}
