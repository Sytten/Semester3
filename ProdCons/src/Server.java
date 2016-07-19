import Buffer.BoundedBuffer;
import Buffer.Buffer;
import Consumer.Consumer;
import Producer.Producer;
/**
 * This <code>Server</code> class contains the <code>main</code> method to strat the program.
 * @author Departement GEGI Sherbrooke
 * @version 1.1
 */
public class Server
{
	/**
	 * Start <code>main</code> program code.
	 * @param args		The arguments passed to the program.
	 */
	public static void main(String args[]) {
		Buffer server = new BoundedBuffer();

      		// now create the producer and consumer threads
      		Thread producerThread = new Thread(new Producer(server));
      		Thread consumerThread = new Thread(new Consumer(server));
      
      		producerThread.start();
      		consumerThread.start();               
	}
}
