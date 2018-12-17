package Buffer;

/**
 * Simple Buffer interface.
 * @author Departement GEGI Sherbrooke
 * @version 1.1
 */
public interface Buffer
{
	/**
	 * Insert a new item into the Buffer.
	 * @param 	item	Object to insert in the buffer.
	 */
	public abstract void insert(Object item);

	/**
	 * Remove an item from the Buffer.
	 * @return	item	Object removed from the buffer.		
	 */
	public abstract Object remove();
}
