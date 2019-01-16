package managers;

import java.util.HashMap;
import java.util.Map;

import game.Player;
import items.Consumable;
import items.Equippable;
import items.Item;

/**
 * The class that creates all the items. Items are specified within this class
 * by the user.
 */
public class ItemLoader {
	private Map<String, Item> items;

	/**
	 * Automatically generates the list of all items in the game.
	 */
	public ItemLoader() {
		items = initItems();
	}

	// Initializes all the items into the game.
	private Map<String, Item> initItems() {
		Map<String, Item> items = new HashMap<String, Item>();

		Item itm = new Consumable("item name", "item description") {
			public String applyEffect(Player player) {
				return "effect message";
			}
		};
		items.put("itemCode", itm);

		return items;
	}

	/**
	 * @return items The map of all items in the game.
	 */
	public Map<String, Item> getItems() {
		return items;
	}
}