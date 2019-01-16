package items;

/**
 * The abstract superclass for all items.
 */
public abstract class Item {
	// Universal item properties
	private String name;
	private String descrip;
	private int weight;

	/**
	 * Creates a new item with the given name and description. Weight is set to 1 by
	 * default. Useful if weight mechanic unused.
	 * 
	 * @param name
	 * @param descrip
	 */
	public Item(String name, String descrip) {
		this(name, descrip, 1);
	}

	/**
	 * Creates a new item with the given name, weight and description.
	 * 
	 * @param name
	 * @param weight
	 */
	public Item(String name, String descrip, int weight) {
		this.name = name;
		this.descrip = descrip;
		this.weight = weight;
	}

	/**
	 * @return name of item.
	 */
	public String getName() {
		return name;
	}

	/**
	 * @return descrip The description for the item.
	 */
	public String getDescrip() {
		return descrip;
	}

	/**
	 * @return weight of item.
	 */
	public int getWeight() {
		return weight;
	}

	/**
	 * Sets weight of item. Weight cannot be below 0, if it is, set to 0.
	 * 
	 * @param weight
	 */
	public void setWeight(int weight) {
		this.weight = (weight >= 0) ? weight : 0;
	}
}