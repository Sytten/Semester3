package Consumer;

import Buffer.Buffer;
import Tools.SleepTools;

/**
 * Consume buffer messages.
 * 
 * @author Departement GEGI Sherbrooke
 * @author Emile Fugulin
 * @author Philippe Girard
 * @version 1.2 - Display product being removed from buffer
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
			System.out.println("CONSUMER: Sleeping");
			
			SleepTools.nap(5);
			
			message = (String) buffer.remove();

			System.out.println("CONSUMER: " + message);

			
		}
	}

	/**
	 * Buffer to consume. 
	 */
	private Buffer buffer;
}
