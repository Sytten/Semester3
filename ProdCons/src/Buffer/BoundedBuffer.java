package Buffer;

import ConcurrenceControl.ImpConcurrenceControl;
import SharedMemoryUtilities.SharedMemory;
import Tools.SleepTools;

/**
 * Implementation for the <code>Buffer</code> interface.
 * 
 * @author Departement GEGI Sherbrooke
 * @version 1.1
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
			SharedMemory.write(sharedMemoryAddr, sharedMemorySize - 1, String.valueOf(0));
			SharedMemory.write(sharedMemoryAddr, sharedMemorySize - 2, "true");
		}

	}

	/*
	 * Producer calls this method.
	 * 
	 * @inheritDoc
	 */
	public void insert(Object item) {
		while (Integer.parseInt(SharedMemory.read(sharedMemoryAddr, sharedMemorySize - 1)) == bufferSize) {
			SleepTools.nap(1);
		}

		// add an item to the buffer
		mutex.acquire();

		count = Integer.parseInt(SharedMemory.read(sharedMemoryAddr, sharedMemorySize - 1));
		++count;
		SharedMemory.write(sharedMemoryAddr, sharedMemorySize - 1, String.valueOf(count));

		SharedMemory.write(sharedMemoryAddr, in, (String) item);
		mutex.release();

		in = (in + 1) % bufferSize;

		if (count == bufferSize)
			System.out.println("BOUNDEDBUFFER: Le produit " + item + " a ete enmagasine. Buffer PLEIN!!!");
		else
			System.out.println(
					"BOUNDEDBUFFER: Le produit " + item + "a ete enmagasine. Il y a = " + count + " produits.");
	}

	/*
	 * Consumer calls this method.
	 * 
	 * @inheritDoc
	 */
	public Object remove() {
		Object item;

		while (Integer.parseInt(SharedMemory.read(sharedMemoryAddr, sharedMemorySize - 1)) == 0) {
			SleepTools.nap(1);
		}

		// remove an item from the buffer
		mutex.acquire();

		count = Integer.parseInt(SharedMemory.read(sharedMemoryAddr, sharedMemorySize - 1));
		--count;
		SharedMemory.write(sharedMemoryAddr, sharedMemorySize - 1, String.valueOf(count));

		item = SharedMemory.read(sharedMemoryAddr, out);
		mutex.release();

		out = (out + 1) % bufferSize;

		if (count == 0)
			System.out.println("BOUNDEDBUFFER: Le produit " + item + " a ete consomme. Buffer VIDE");
		else
			System.out
					.println("BOUNDEDBUFFER: Le produit " + item + " a ete consomme. Il y a = " + count + " produits.");

		return item;
	}

}
