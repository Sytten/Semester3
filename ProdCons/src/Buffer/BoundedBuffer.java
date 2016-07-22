package Buffer;

import ConcurrenceControl.ImpConcurrenceControl;

/**
 * Implementation for the <code>Buffer</code> interface.
 * 
 * @author Departement GEGI Sherbrooke
 * @version 1.1
 * @see Buffer
 */
public class BoundedBuffer implements Buffer {
	private static final int BUFFER_SIZE = 3;

	private volatile int count;

	private int in; // points to the next free position in the buffer
	private int out; // points to the next full position in the buffer
	private Object[] buffer;
	
	private ImpConcurrenceControl mutex;

	/**
	 *  Create a BoundedBuffer instance.
	 */
	public BoundedBuffer() {
		// buffer is initially empty
		count = 0;
		in = 0;
		out = 0;

		buffer = new Object[BUFFER_SIZE];
		mutex = new ImpConcurrenceControl();
	}

	/* 
	 * Producer calls this method.
	 * @inheritDoc
	 */
	public void insert(Object item) {
		while (count == BUFFER_SIZE)
			; // do nothing

		// add an item to the buffer
		mutex.acquire();
		++count;
		buffer[in] = item;
		mutex.release();
		
		in = (in + 1) % BUFFER_SIZE;

		if (count == BUFFER_SIZE)
			System.out.println("BOUNDEDBUFFER: Le produit " + item + " a ete enmagasine. Buffer PLEIN!!!");
		else
			System.out.println(
					"BOUNDEDBUFFER: Le produit " + item + "a ete enmagasine. Il y a = " + count + " produits.");
	}

	/*
	 * Consumer calls this method.
	 * @inheritDoc
	 */
	public Object remove() {
		Object item;

		while (count == 0)
			; // do nothing

		// remove an item from the buffer
		mutex.acquire();
		--count;
		item = buffer[out];
		mutex.release();
		out = (out + 1) % BUFFER_SIZE;

		if (count == 0)
			System.out.println("BOUNDEDBUFFER: Le produit " + item + " a ete consomme. Buffer VIDE");
		else
			System.out
					.println("BOUNDEDBUFFER: Le produit " + item + " a ete consomme. Il y a = " + count + " produits.");

		return item;
	}

}
