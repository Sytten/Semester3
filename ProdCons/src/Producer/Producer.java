package Producer;

import Buffer.Buffer;
import Tools.SleepTools;

/**
 * Produce message in the buffer.
 * 
 * @author Departement GEGI Sherbrooke
 * @author Emile Fugulin
 * @author Philippe Girard
 * @version 1.2 - Use an ID for each message
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
			System.out.println("PRODUCER: Sleeping");
			
			SleepTools.nap(5);
			
			message = new String("P" + String.valueOf(id));
			System.out.println("PRODUCER: " + message);
			
			id++;
			
			if(id >= 10000)
				id = 0;

			buffer.insert(message);
		}
	}

	/**
	 * Buffer to consume.
	 */
	private Buffer buffer;
	
	private int id = 0;
}
