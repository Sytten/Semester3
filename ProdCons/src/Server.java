import Buffer.BoundedBuffer;
import Buffer.Buffer;
import Consumer.Consumer;
import Producer.Producer;
public class Server
{
	public static void main(String args[]) {
		Buffer server = new BoundedBuffer();

      		// now create the producer and consumer threads
      		Thread producerThread = new Thread(new Producer(server));
      		Thread consumerThread = new Thread(new Consumer(server));
      
      		producerThread.start();
      		consumerThread.start();               
	}
}
