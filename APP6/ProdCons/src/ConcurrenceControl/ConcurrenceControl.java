package ConcurrenceControl;

/**
 * Interface that define basic behavior of a Semaphore.
 * @author Departement GEGI Sherbrooke
 * @version 1.1
 */
public interface ConcurrenceControl {

	/**
	 * Acquire a token if there is one.
	 * @return	boolean		Token was acquired.
	 */
	public abstract boolean acquire();

	/**
	 * Release a token. 
	 */
	public abstract void release();
}
