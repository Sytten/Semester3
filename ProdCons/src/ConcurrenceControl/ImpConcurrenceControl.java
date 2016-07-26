package ConcurrenceControl;

import SharedMemoryUtilities.SharedMemory;
/**
 * Implementation of concurrence Control.
 * This is an implementation of a mutex to control access to critical sections in code. 
 * it allows only one process at the time to acquire a lock on a resource. 
 * 
 * It writes and read the its token at (sharedMemorySize - 2) of a given sharedMemoryAddr.
 * 
 * @author Emile Fugulin
 * @author Philippe Girard
 * @version 1.2 - Return a boolean when acquire instead of waiting.
 * @see ConcurrenceControl
 *
 */
public class ImpConcurrenceControl implements ConcurrenceControl {

	private SharedMemory sharedMemory;
	private int sharedMemoryAddr;
	private int sharedMemorySize;
	
	/**
	 * Create a new instance of ImpConcurrenceContro
	 * 
	 * @param sharedMemoryAddr the first memory address of the buffer in memory
	 * @param sharedMemorySize the size of the buffer in memory
	 */
	public ImpConcurrenceControl(int sharedMemoryAddr, int sharedMemorySize) {
		sharedMemory = new SharedMemory();
		this.sharedMemoryAddr = sharedMemoryAddr;
		this.sharedMemorySize = sharedMemorySize;
	}
	
	/**
	 * Wait until a token is available then take it.
	 */
	@Override
	public synchronized boolean acquire() {
		if(Boolean.parseBoolean(SharedMemory.read(sharedMemoryAddr, sharedMemorySize - 2))) {
			SharedMemory.write(sharedMemoryAddr, sharedMemorySize - 2, "false");
			return true;
		} else {
			return false;
		}
	}

	/**
	 * release the token.
	 */
	@Override
	public synchronized void release() {
		SharedMemory.write(sharedMemoryAddr, sharedMemorySize - 2, "true");
	}

}
