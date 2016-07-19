package SharedMemoryUtilities;

import java.io.*;


public class SharedMemoryCreateBuffer
{

  public static void main(String[] args)
  {
    System.out.println("Starting Shared Memory Example Write");

    try
    {
      SharedMemory sm = new SharedMemory();

      System.out.println("Allocation de la memoire");
	  int handle = sm.alloc(5,5);

	  sm.displaySharedMemory(handle);

	  InputStreamReader converter = new InputStreamReader (System.in);
	  BufferedReader		in = new BufferedReader (converter);
      String anyKey = null;
	  try {
	    anyKey = in.readLine();
	  } catch( Exception e ) {
	       System.out.println("Erreur de lecture de string");
	  }

      System.out.println("On doit liberer la memoire partage...");
      sm.free(handle);
    }
    catch(Throwable t)
    {
      System.out.println(t);
    }
    System.out.println("Exiting Shared Memory Example");
  }
}
