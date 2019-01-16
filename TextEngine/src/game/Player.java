package game;

import java.util.Map;

import items.Inventory;
import items.Item;

/**
 * Maintains collection of variables relating to gameplay.
 *
 */
public class Player {
	// Game variables (TEMP?)
	private Inventory inv;


	/**
	 * Creates a new player with all stats set 0 with a nonweighted inventory
	 * size of 1.
	 */
	public Player() {
		inv = new Inventory(1);
	}

	/**
	 * @return Player's inventory.
	 */
	public Inventory getInv() {
		return inv;
	}

	/**
	 * Attempts to add the given item to the player's inventory.
	 * 
	 * @param item
	 * @return Whether or not the item could be added.
	 */
	public boolean addItem(Item item) {
		return inv.add(item);
	}

	/**
	 * Removes the given item from the inventory.
	 * 
	 * @param item
	 *            to remove
	 */
	public void removeItem(Item item) {
		inv.remove(item);
	}
	/**
	 * Prints out the current in-game status of the player, based off player stats, inventory and the flag map.
	 * 
	 * @param flags Map of flags containing boolean values.
	 * @return statInfo An HTML string containing the player's stats, formatted.
	 */
	public String getStats(Map<String, Boolean> flags) {
		String statInfo = "";
		// Starting tags
		statInfo = "<h1 style=\"text-align:center;\"><b>Player Menu</b></h1><p style=\"text-align:right;\"><small>* Click <b>Back</b> to return to game* </small></p>";
		

		statInfo += "<br><i>Click on an item to use it.</i>" + inv.printStatus();
	
		return statInfo;
	}
}