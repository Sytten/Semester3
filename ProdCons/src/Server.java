import Buffer.BoundedBuffer;
import Buffer.Buffer;
import Consumer.Consumer;
import Producer.Producer;
import SharedMemoryUtilities.SharedMemory;

/**
 * This <code>Server</code> class contains the <code>main</code> method to strat
 * the program.
 * 
 * @author Departement GEGI Sherbrooke
 * @version 1.2
 */
public class Server {
	/**
	 * Start <code>main</code> program code.
	 * 
	 * @param args
	 *            The arguments passed to the program.
	 */
	public static void main(String args[]) {
		
		if(args.length != 3) {
			System.out.println("FONCTIONNEMENT: java server {p|c} {sharedMemoryAdrr} {sharedMemorySize}");
			System.exit(1);
		}
		
		Buffer server = new BoundedBuffer(Integer.parseInt(args[1]), Integer.parseInt(args[2]));

		// now create the producer and consumer threads
		if (args[0].equals("p")) {
			SharedMemory sharedMemory = new SharedMemory();
			Thread producerThread = new Thread(new Producer(server));
			producerThread.start();
		} else if (args[0].equals("c")) {
			Thread consumerThread = new Thread(new Consumer(server));
			consumerThread.start();
		}

	}
}
