package SharedMemoryUtilities;

import java.io.*;


public class SharedMemory
{

  /* Les methodes natives sont implementes en C */
  
  /** 
   * Permet de reserver une zone de memoire partagee divisee en strings
   * @param    size        le nombre de positions (strings) a reserver
   * @param    maxString   la taille maximale de chaque string
   * @return   l'adresse de la memoire partagee reservee   
   */
  public static native int alloc(int size, int maxString); 

  /* Permet d'ecrire une string dans la zone de memoire partagee
   * @param    handle      l'adresse de la memoire partagee 
   * @param    pos         la position dans la memoire partagee ou ecrire
   * @param    data        le string a ecrire
   */
  public static native void write(int handle, int pos, String data); 

  /* Permet de lire une string dans la zone de memoire partagee
   * @param    handle      l'adresse de la memoire partagee 
   * @param    pos         la position dans la memoire partagee ou ecrire
   * @return   data        le string lu
   */
  public static native String read(int handle, int pos);

  /* Permet de liberer la memoire partagee
   * @param    handle      l'adresse de la memoire partagee 
   */
  public static native void free(int handle);

  /* Permet d'afficher le contenu de la memoire partagee
   * @param    handle      l'adresse de la memoire partagee 
   */
  public static native void displaySharedMemory(int handle);

  SharedMemory(){
    try
    {
      // Pour chercher la libraire native, elle doit se nommer
	  // libSharedMemory.so
	  // Il faut definir la variable d'environement LD_LIBRARY_PATH 
	  // Elle doit contenir le repertoire ou la librairie se trouve
      System.loadLibrary("SharedMemory");
    }
    catch(Throwable t)
    {
      System.out.println(t);
    }

  }
}
