package items;

import game.Player;

/**
 * Subclass of item. This type of item can be consumed to induce an effect.
 */
public class Consumable extends Item {

	public Consumable(String name, String descrip) {
		super(name, descrip);
	}
	
	public Consumable(String name, String descrip, int weight) {
		super(name, descrip, weight);
	}
	
	/**
	 * This method is to be overwritten by each consumable to specify its effect.
	 * 
	 * @return String describing what happens when item consumed.
	 */
	public String applyEffect(Player player) {
		return "No effect. You should not see this.";
	}
}