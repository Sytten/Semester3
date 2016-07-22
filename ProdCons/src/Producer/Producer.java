package Producer;

import Buffer.Buffer;
import Tools.SleepTools;

/**
 * Produce message in the buffer.
 * 
 * @author Departement GEGI Sherbrooke
 * @version 1.1
 */
public class Producer implements Runnable {
	/**
	 * Create producer instance. 
	 * @param b The buffer in which the messages will be produced. 
	 */
	public Producer(Buffer b) {
		buffer = b;
	}

	/**
	 * Sleep than produce a message, an infinite number of time. 
	 */
	public void run() {
		String message;

		while (true) {
			System.out.println("PRODUCTEUR: En train de dormir...");
			
			SleepTools.nap(5);
			
			message = new String("Produit...");
			System.out.println("PRODUCTEUR: Message produit " + message);

			buffer.insert(message);
		}
	}

	/**
	 * Buffer to consume.
	 */
	private Buffer buffer;
}
