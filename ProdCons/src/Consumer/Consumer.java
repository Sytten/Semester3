package Consumer;
import Buffer.Buffer;
import Tools.SleepTools;

public class Consumer implements Runnable
{
   public Consumer(Buffer b) { 
      buffer = b;
   }
   
   public void run()
   {
   String message;
   
     while (true)
      {
         System.out.println("CONSOMMATEUR: En train de dormir...");
	 SleepTools.nap(); 
         
         System.out.println("CONSOMMATEUR: Pret a consommer...");
           
         message = (String)buffer.remove();
      }
   }
   
   private  Buffer buffer;
}


