package app2;

/**
 * <p>Title: APP6 Compilation</p>
 * <p>Description: Exemples Compilaton</p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: UdeS</p>
 * @author rgr
 * @version 1.0
 */
import java.io.FileOutputStream;
/** Classe Permettant l'‰criture d'un fichier texte
 */
public class Writer {
  String _str;

  /** Constructeur prenant en parametre le nom du fichier et de la donnee, et 
    * inserer celle-ci dans celui-la
   */

  public Writer(String name, String data) {
    try {
      FileOutputStream fos = new FileOutputStream(name);
      fos.write(data.getBytes());
      fos.close();
    } catch (Exception e) {
      System.out.println(e);
      e.printStackTrace();
      System.exit(1);
    }
  }

}
