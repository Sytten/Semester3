package Buffer;

import ConcurrenceControl.ImpConcurrenceControl;
import SharedMemoryUtilities.SharedMemory;
import Tools.SleepTools;

/**
 * Implementation for the <code>Buffer</code> interface using a shared memory.
 * 
 * @author Departement GEGI Sherbrooke
 * @author Emile Fugulin
 * @author Philippe Girard
 * @version 1.3 - Use the return value of the acquire in the waiting while and display where the item is stored/removed.
 * @see Buffer
 */
public class BoundedBuffer implements Buffer {
	private int bufferSize;
	private int count;
	private int in; // points to the next free position in the buffer
	private int out; // points to the next full position in the buffer
	private SharedMemory sharedMemory;
	private int sharedMemoryAddr;
	private int sharedMemorySize;

	private ImpConcurrenceControl mutex;

	/**
	 * Create a BoundedBuffer instance.
	 */
	public BoundedBuffer(int sharedMemoryAddr, int sharedMemorySize) {
		// buffer is initially empty
		count = 0;
		in = 0;
		out = 0;

		this.sharedMemoryAddr = sharedMemoryAddr;
		this.sharedMemorySize = sharedMemorySize;

		bufferSize = sharedMemorySize - 2;
		if (this.bufferSize <= 0) {
			System.out.println("BOUNDEDBUFFER: shared memory size must be at least of size 3");
			System.exit(1);
		}

		sharedMemory = new SharedMemory();
		mutex = new ImpConcurrenceControl(sharedMemoryAddr, sharedMemorySize);

		// write to the memory the count and the available if it is not already
		// written
		String memoryValueOfCount = SharedMemory.read(sharedMemoryAddr, sharedMemorySize - 1);
		String memoryValueOfAvailable = SharedMemory.read(sharedMemoryAddr, sharedMemorySize - 2);
		if (memoryValueOfAvailable.isEmpty() || memoryValueOfCount.isEmpty()) {
			System.out.println("ECRITURE");
			SharedMemory.write(sharedMemoryAddr, sharedMemorySize - 1, String.valueOf(0));
			SharedMemory.write(sharedMemoryAddr, sharedMemorySize - 2, "true");
		}

	}

	/**
	 * Producer calls this method. Insert a new item into the shared memory.
	 * @param 	item	Object to insert in the buffer.
	 */
	public synchronized void insert(Object item) {
		while (Integer.parseInt(SharedMemory.read(sharedMemoryAddr, sharedMemorySize - 1)) == bufferSize || !mutex.acquire()) {
			SleepTools.nap(1);
		}

		// add an item to the buffer
		count = Integer.parseInt(SharedMemory.read(sharedMemoryAddr, sharedMemorySize - 1));
		++count;
		SharedMemory.write(sharedMemoryAddr, sharedMemorySize - 1, String.valueOf(count));

		SharedMemory.write(sharedMemoryAddr, in, (String) item);
		mutex.release();

		System.out.println("BOUNDEDBUFFER: " + item + " stored at position " + in);
		
		if(count == bufferSize)
			System.out.println("BOUNDEDBUFFER: Buffer full");
		
		in = (in + 1) % bufferSize;
	}

	/**
	 * Consumer calls this method. Remove an item from the shared memory.
	 * @return	item	Object removed from the buffer.	
	 */
	public synchronized Object remove() {
		Object item;

		while (Integer.parseInt(SharedMemory.read(sharedMemoryAddr, sharedMemorySize - 1)) == 0 || !mutex.acquire()) {
			SleepTools.nap(1);
		}

		// remove an item from the buffer
		count = Integer.parseInt(SharedMemory.read(sharedMemoryAddr, sharedMemorySize - 1));
		--count;
		SharedMemory.write(sharedMemoryAddr, sharedMemorySize - 1, String.valueOf(count));

		item = SharedMemory.read(sharedMemoryAddr, out);
		mutex.release();
		
		System.out.println("BOUNDEDBUFFER: " + item + " removed at position " + out);
		
		if(count == 0)
			System.out.println("BOUNDEDBUFFER: Buffer empty");

		out = (out + 1) % bufferSize;

		return item;
	}

}
