package Buffer;

public interface Buffer
{
	/**
	 * insert an item into the Buffer.
	 */
	public abstract void insert(Object item);

	/**
	 * remove an item from the Buffer.
	 */
	public abstract Object remove();
}
