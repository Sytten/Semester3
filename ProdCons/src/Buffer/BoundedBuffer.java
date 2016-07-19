package Buffer;

public class BoundedBuffer implements Buffer
{ 
   private static final int   BUFFER_SIZE = 3;

   private volatile int count;

   private int in;   // points to the next free position in the buffer
   private int out;  // points to the next full position in the buffer
   private Object[] buffer;
       
   public BoundedBuffer()
   {
      // buffer is initially empty
      count = 0;
      in = 0;
      out = 0;
      
      buffer = new Object[BUFFER_SIZE];
   }

   // producer calls this method
   public void insert(Object item) {
      while (count == BUFFER_SIZE) 
         ; // do nothing
      
      // add an item to the buffer
      ++count;
      buffer[in] = item;
      in = (in + 1) % BUFFER_SIZE;

	if (count == BUFFER_SIZE)
                System.out.println("BOUNDEDBUFFER: Le produit " + item + " a ete enmagasine. Buffer PLEIN!!!");
        else
                System.out.println("BOUNDEDBUFFER: Le produit " + item + "a ete enmagasine. Il y a = " +  count + " produits.");
   }
   
   // consumer calls this method
   public Object remove() {
      Object item;
      
      while (count == 0) 
         ; // do nothing
      
      // remove an item from the buffer
      --count;
      item = buffer[out];
      out = (out + 1) % BUFFER_SIZE;

	if (count == 0)
                System.out.println("BOUNDEDBUFFER: Le produit " + item + " a ete consomme. Buffer VIDE");
        else
                System.out.println("BOUNDEDBUFFER: Le produit " + item + " a ete consomme. Il y a = " +  count + " produits.");
      
      return item;
   }

}
