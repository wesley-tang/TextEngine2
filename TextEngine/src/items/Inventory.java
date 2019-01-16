package items;

import java.util.ArrayList;

/**
 * An inventory that manages items stored within it.
 */
public class Inventory {
	// Capacity trackers
	private int maxCapacity;
	private int currentCapacity;

	private ArrayList<Item> items;

	/**
	 * Creates a new, empty inventory of the max specified capacity, with 0 capacity
	 * filled.
	 * 
	 * @param maxCapacity
	 */
	public Inventory(int maxCapacity) {
		this.maxCapacity = maxCapacity;
		currentCapacity = 0;

		items = new ArrayList<Item>();
	}

	/**
	 * Attempt to add a new item into the inventory.
	 * 
	 * @param item
	 * @return Whether or not the item could be successfully added.
	 */
	public boolean add(Item item) {
		if (item.getWeight() + currentCapacity <= maxCapacity) {
			items.add(item);
			currentCapacity += item.getWeight();
			return true;
		} else {
			System.out.println("Can't fit item, already at max capacity!");
			return false;
		}
	}

	/**
	 * Attempts to remove the specified item from the inventory.
	 * 
	 * @param item
	 *            to be removed.
	 */
	public void remove(Item item) {
		if (items.remove(item)) {
			currentCapacity -= item.getWeight();
		}
	}

	/**
	 * Clears the inventory, resetting it so that it is empty of items.
	 */
	public void empty() {
		items.clear();
		currentCapacity = 0;
	}

	/**
	 * Returns an array of all the names of the items in this inventory. Note that
	 * these are names and not references to the actual items themselves.
	 * 
	 * @return contents An array with the names of all the items in the inventory.
	 */
	public String[] getContentNames() {
		String[] contents = new String[items.size()];

		for (int i = 0; i < contents.length; i++)
			contents[i] = items.get(i).getName();

		return contents;
	}

	/**
	 * Prints out the names of all the contents in this inventory, separated by
	 * newlines. Otherwise, prints a message saying inventory is empty.
	 * 
	 * @return A string containing the above information.
	 */
	public String printContents() {
		String info = "\n<br>";
		if (items.isEmpty())
			info += "Inventory is empty.";
		else {
			for (Item item : items)
				info +=  " - " + item.getName() + "\n<br>";
		}
		//System.out.println(info);
		return info;
	}

	/**
	 * Prints out the contents, the current capacity of the inventory as a fraction,
	 * and the number of items currently in the inventory.
	 * 
	 * @return A string containing the above information.
	 */
	public String printStatus() {
		String info = isEmpty() ? ""
				: "\n<br>Your inventory has " + items.size() + " item" + (items.size() == 1 ? "" : "s") + ": ";
		info += printContents();
		return info + "<br><br>\n(Capacity: " + currentCapacity + "/" + maxCapacity + ")\n<br>";
	}

	/**
	 * Gives direct access to the arraylist of items. Should refrain from using.
	 * Note that changes to this can break the inventory.
	 * 
	 * @return items The ArrayList containing all the items stored within the
	 */
	public ArrayList<Item> accessItems() {
		return items;
	}

	/**
	 * Checks to see if a given item is within the inventory.
	 * 
	 * @param item
	 *            to check for
	 * @return True if item is in inventory
	 */
	public boolean checkInvFor(Item item) {
		return items.contains(item);
	}

	/**
	 * @return maxCapacity Inventory's max capacity.
	 */
	public int getMaxCapacity() {
		return maxCapacity;
	}

	/**
	 * Sets the inventory's max capacity, cannot go below 0.
	 * 
	 * @param maxCapacity
	 *            The new maximum capacity of the inventory.
	 */
	public void setMaxCapacity(int maxCapacity) {
		if (maxCapacity >= 0)
			this.maxCapacity = maxCapacity;
		else
			this.maxCapacity = 0;
		if (maxCapacity < currentCapacity)
			System.out.println("WARNING: MAX CAPACITY LOWERED BELOW CURRENT CAPACITY.");
	}

	/**
	 * @return currentCapacity Inventory's current capacity.
	 */
	public int getCurrentCapacity() {
		return currentCapacity;
	}

	/**
	 * Sets the inventory's current capacity, cannot go below 0, or higher than
	 * maxCapacity
	 * 
	 * @param capacity
	 */
	public void setCurrentCapacity(int capacity) {
		if (capacity >= 0)
			currentCapacity = capacity;
		else if (capacity > maxCapacity) {
			currentCapacity = maxCapacity;
			// TODO throw error and try say inventory is full
			System.out.println("MAX CAPACITY EXCEEDED. CAPPING AT MAX CAPACITY.");
		} else
			currentCapacity = 0;
	}

	/**
	 * @return True if inventory is full.
	 */
	public boolean isFull() {
		return currentCapacity == maxCapacity;
	}

	/**
	 * @return True if inventory is empty.
	 */
	public boolean isEmpty() {
		return items.isEmpty();
	}
}