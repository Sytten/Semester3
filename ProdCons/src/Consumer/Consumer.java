package Consumer;

import Buffer.Buffer;
import Tools.SleepTools;

/**
 * Consume buffer messages.
 * 
 * @author Departement GEGI Sherbrooke
 * @version 1.1
 */
public class Consumer implements Runnable {

	/**
	 * Create Consumer instance. 
	 * @param b			Buffer to consume.
	 */
	public Consumer(Buffer b) {
		buffer = b;
	}

	/**
	 * Sleep than consume a message, an infinite number of time. 
	 */
	public void run() {
		String message;

		while (true) {
			System.out.println("CONSOMMATEUR: En train de dormir...");

			System.out.println("CONSOMMATEUR: Pret a consommer...");

			message = (String) buffer.remove();
		}
	}

	/**
	 * Buffer to consume. 
	 */
	private Buffer buffer;
}
