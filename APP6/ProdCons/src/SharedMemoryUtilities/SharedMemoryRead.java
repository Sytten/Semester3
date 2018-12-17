package SharedMemoryUtilities;

import java.io.*;

public class SharedMemoryRead
{

  public static void main(String[] args)
  {
    System.out.println("Starting Shared Memory Example Read");

    if (args.length == 0)
    {
    	System.out.println("USAGE: java SharedMemoryUtilities.SharedMemoryRead <handle>");
    	System.exit(-1);
    }
    try
    {

      SharedMemory sm = new SharedMemory();

      String memory_addres = args[0];

      /* Il faut convertir ce qu'on a lu en entier */ 
      int handle = Integer.parseInt(memory_addres);


      System.out.println("On commence la phase de lecture de la memoire partagee...");
	  String buffer_1 = new String();
	  for(int i = 0; i < 5; i++)
	  {
        buffer_1 = sm.read(handle, i);
		System.out.println ("SharedMemoryRead a lu :"+buffer_1);
      }
    }
    catch(Throwable t)
    {
      System.out.println(t);
    }

    System.out.println("Exiting Shared Memory Example Read");
  }
}