package app2;

/** @author Ahmed Khoumsi */

/** Cette classe effectue l'analyse lexicale
 */
public class AnalLex {

// Attributs
  private String m_expression;

  private int m_etat = 0;
  private int m_ptrLect = 0;


/** Constructeur pour l'initialisation d'attribut(s)
 */
  public AnalLex(String s) {  // arguments possibles
	  m_expression = s;
  }


/** resteTerminal() retourne :
      false  si tous les terminaux de l'expression arithmetique ont ete retournes
      true s'il reste encore au moins un terminal qui n'a pas ete retourne
 */
  public boolean resteTerminal( ) {
	  if(m_ptrLect < m_expression.length())
		  return true;
	  return false;
  }


/** prochainTerminal() retourne le prochain terminal
      Cette methode est une implementation d'un AEF
 */
  public Terminal prochainTerminal( ) throws Exception{
	  char c;
	  String result = "";
	  while(m_ptrLect < m_expression.length()) {
		  c = m_expression.charAt(m_ptrLect);
		  m_ptrLect++;
		  switch(m_etat) {
		  	case 0 :
		  		if(Character.isDigit(c)) {
		  			result += String.valueOf(c);
			  		m_etat = 1;
		  		} else if(c == '+') {
		  			return new Terminal("+");
		  		} else {
		  			throw new Exception("Expression invalide");
		  		}
		  		break;
		  	case 1 :
		  		if(Character.isDigit(c)) {
		  			result += String.valueOf(c);
		  		} else {
		  			m_ptrLect--;
		  			m_etat = 0;
		  			return new Terminal(result);
		  		}
		  		break;
		  	default:
		  		break;
		  }
	  }

	  return new Terminal(result);
  }


/** ErreurLex() envoie un message d'erreur lexicale
 */
  public void ErreurLex(String s) {
     //
  }


  //Methode principale a lancer pour tester l'analyseur lexical
  /*public static void main(String[] args) {
    String toWrite = "";
    System.out.println("Debut d'analyse lexicale");
    if (args.length == 0){
    args = new String [2];
            args[0] = "ExpArith.txt";
            args[1] = "ResultatLexical.txt";
    }
    Reader r = new Reader(args[0]);

    AnalLex lexical = new AnalLex(r.toString()); // Creation de l'analyseur lexical

    // Execution de l'analyseur lexical
    try {
	    Terminal t = null;
	    while(lexical.resteTerminal()){
	    	t = lexical.prochainTerminal();
	    	toWrite +=t.chaine + "\n" ;  // toWrite contient le resultat
	    }				   //    d'analyse lexicale
	    System.out.println(toWrite); 	// Ecriture de toWrite sur la console
	    Writer w = new Writer(args[1],toWrite); // Ecriture de toWrite dans fichier args[1]
	    System.out.println("Fin d'analyse lexicale");
    } catch (Exception e) {
		e.printStackTrace();
	}
  }*/
}
