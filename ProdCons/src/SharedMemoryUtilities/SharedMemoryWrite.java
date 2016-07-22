package SharedMemoryUtilities;

import java.io.*;

public class SharedMemoryWrite
{

  public static void main(String[] args)
  {
    System.out.println("Starting Shared Memory Example Write");
    if (args.length == 0)
    {
    	System.out.println("USAGE: java SharedMemoryUtilities.SharedMemoryWrite <handle>");
    	System.exit(-1);
    }
    try
    {
      SharedMemory sm = new SharedMemory();

      String memory_addres = args[0];

      /* Il faut convertir ce qu'on a lu en entier */ 
      int handle = Integer.parseInt(memory_addres);


      String buffer[]= {"ABCD","BCDEF","CD","D","EFGHI"};
      System.out.println("On commence a envoyer des strings a stocker...");
	  for (int i=0; i<5; i++)
	  {
		 System.out.println ("SharedMemoryWrite envoie : " + buffer[i]);
		 SharedMemory.write(handle, i, buffer[i]);
	  }

    }
    catch(Throwable t)
    {
      System.out.println(t.getMessage());
      System.out.println(t.getStackTrace());
    }

    System.out.println("Exiting Shared Memory Example Write");
  }
}