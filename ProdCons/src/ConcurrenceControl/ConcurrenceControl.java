package ConcurrenceControl;

/**
 * Interface that define basic behavior of a Semaphore.
 * @author Departement GEGI Sherbrooke
 * @version 1.1
 */
public interface ConcurrenceControl {

	/**
	 * Acquire a token if there is one. If not wait. 
	 */
	public abstract void acquire();

	/**
	 * Release a token. 
	 */
	public abstract void release();
}
