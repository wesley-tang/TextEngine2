package utilities;

import java.util.HashMap;
import java.util.Random;
import java.util.function.Supplier;

import game.Player;

/**
 * Class dedicated to generating randomized descriptions of the player's attributes by using a token map.
 */
public class TokenMapCreator {
	private Player p;

	/**
	 * Creates a new token map linking token strings to player variables.
	 * 
	 * @param Player object whose variables are to be mapped
	 * @return Hashmap with token strings as keys and Suppliers which return the appropriate string based on player's variables.
	 */
	public HashMap<String, Supplier<String>> createFor(Player p) {
		this.p = p;

		HashMap<String, Supplier<String>> tokens = new HashMap<String, Supplier<String>>();

		return tokens;
	}


}