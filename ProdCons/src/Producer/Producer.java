package Producer;
import Buffer.Buffer;
import Tools.SleepTools;

public class Producer implements Runnable
{
   public Producer(Buffer b) {
      buffer = b;
   }
   
   public void run()
   {
   String message;
     
      while (true) {
         System.out.println("PRODUCTEUR: En train de dormir...");
	 SleepTools.nap();

         message = new String("Produit...");      
         System.out.println("PRODUCTEUR: Message produit " + message);
           
         buffer.insert(message);
      }
   }
   
   private  Buffer buffer;
}
