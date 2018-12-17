package app2;

/**
 * <p>Title: APP6 Compilation</p>
 * <p>Description: Exemples Compilaton</p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: UdeS</p>
 * @author rgr
 * @version 1.0
 */
import java.io.FileInputStream;
/** Classe permettant d'ouvrir et de faire la Lecture d'un fichier
  *
  */
public class Reader {
  String _str;

  /** Constructeur prenant dans ses paramˆtre le non du fichier a ouvrir
   * @param  String qui est le Nom du Fichier
   */

  public Reader(String name) {
    try {
      FileInputStream fis = new FileInputStream(name);
      int n;
      while ((n= fis.available()) > 0) {
        byte[]b = new byte[n];
        int result = fis.read(b);
        if (result == -1)
          break;
        _str = new String(b);
      }//end while
      fis.close();
    } catch (Exception e) {
      System.out.println(e);
      e.printStackTrace();
      System.exit(1);
    }
  }

  /** Methode renvoyant la String
   * @return La string contenu dans le fichier
   */

public String toString(){
    return _str;
  }

}
