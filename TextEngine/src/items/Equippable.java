package items;

import game.Player;

/**
 * Subclass of item. This type of item can be equipped on the player to induce
 * an effect such as stat, state or flag changes.
 * 
 *
 */
public class Equippable extends Item {

	public Equippable(String name, String descrip) {
		super(name, descrip);
	}
	
	public Equippable(String name, String descrip, int weight) {
		super(name, descrip, weight);
	}

	/**
	 * This method is to be overwritten by the each equippable to specify its
	 * effect.
	 * 
	 * @param The player upon which the item is equipped.
	 * @return String describing what happens when item equipped.
	 */
	public String equip(Player player) {
		return "No effect. You should not see this.";
	}

}