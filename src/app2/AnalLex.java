package app2;

/** @author Ahmed Khoumsi */

/**
 * Cette classe effectue l'analyse lexicale
 */
public class AnalLex {

	// Attributs
	private String m_expression;

	private int m_etat = 0;
	private int m_ptrLect = 0;

	/**
	 * Constructeur pour l'initialisation d'attribut(s)
	 */
	public AnalLex(String s) { // arguments possibles
		m_expression = s;
	}

	/**
	 * resteTerminal() retourne : false si tous les terminaux de l'expression
	 * arithmetique ont ete retournes true s'il reste encore au moins un
	 * terminal qui n'a pas ete retourne
	 */
	public boolean resteTerminal() {
		if (m_ptrLect < m_expression.length())
			return true;
		return false;
	}

/** prochainTerminal() retourne le prochain terminal
      Cette methode est une implementation d'un AEF
 */
  public Terminal prochainTerminal( ) throws Exception {
	  char c;
	  String result = "";
	  while (m_ptrLect < m_expression.length()) {
		  c = m_expression.charAt(m_ptrLect);
		  m_ptrLect++;
		  switch (m_etat) {
		  	case 0 :
		  		if (Character.isDigit(c)) {
		  			result += String.valueOf(c);
			  		m_etat = 1;
		  		} else if (Character.isUpperCase(c)) {
		  			result += String.valueOf(c);
		  			m_etat = 2;
		  		} else if (c == '+') {
		  			return new Terminal("+", Terminal.Type.OPERATEUR);
		  		} else if (c == '-') {
		  			return new Terminal("-", Terminal.Type.OPERATEUR);
		  		} else if (c == '*') {
		  			return new Terminal("*", Terminal.Type.OPERATEUR);
		  		} else if (c == '/') {
		  			return new Terminal("/", Terminal.Type.OPERATEUR);
		  		} else if (c == '(') {
		  			return new Terminal("(", Terminal.Type.OPERATEUR);
		  		} else if (c == ')') {
		  			return new Terminal(")", Terminal.Type.OPERATEUR);
		  		} else if (Character.isLowerCase(c)) {
		  			throw new ExceptionLexicale("Lettre minuscule debut invalide", m_ptrLect);
		  		} else {
		  			throw new ExceptionLexicale("Caractere invalide", m_ptrLect);
		  		}
		  		break;
		  	case 1 :
		  		if (Character.isDigit(c)) {
		  			result += String.valueOf(c);
		  		} else {
		  			m_ptrLect--;
		  			m_etat = 0;
		  			return new Terminal(result, Terminal.Type.CONSTANTE);
		  		}
		  		break;
		  	case 2:
		  		if (Character.isLetter(c)) {
		  			result += String.valueOf(c);
		  		} else if (c == '_') {
		  			result += String.valueOf(c);
		  			m_etat = 3;
		  		} else {
		  			m_ptrLect--;
		  			m_etat = 0;
		  			return new Terminal(result, Terminal.Type.VARIABLE);
		  		}
		  		break;
		  	case 3:
		  		if (Character.isLetter(c)) {
		  			result += String.valueOf(c);
		  			m_etat = 2;
		  		} else if(c == '_') {
		  			throw new ExceptionLexicale("Double tiret", m_ptrLect);
		  		} else {
		  			throw new ExceptionLexicale("Caractere invalide", m_ptrLect);
		  		}
		  		break;
		  	default:
		  		break;
		  }
	  }

	  /**
	   * Etats non finaux
	   */
	  if(m_etat == 3) {
		  throw new ExceptionLexicale("Tiret de fin", m_ptrLect);
	  }
	
	  /**
	   * Etats finaux
	   */
	  if (m_etat == 0) {
		  return new Terminal(result, Terminal.Type.OPERATEUR);
	  } else if (m_etat == 1) {
		  return new Terminal(result, Terminal.Type.CONSTANTE);
	  } else {
		  return new Terminal(result, Terminal.Type.VARIABLE);
	  }  
  }

	// Methode principale a lancer pour tester l'analyseur lexical
	public static void main(String[] args) {
		String toWrite = "";
		System.out.println("Debut d'analyse lexicale");
		if (args.length == 0) {
			args = new String[2];
			args[0] = "ExpArith.txt";
			args[1] = "ResultatLexical.txt";
		}
		Reader r = new Reader(args[0]);

		AnalLex lexical = new AnalLex(r.toString()); // Creation de l'analyseur
														// lexical

		// Execution de l'analyseur lexical
		try {
			Terminal t = null;
			while (lexical.resteTerminal()) {
				t = lexical.prochainTerminal();
				toWrite += t.getChaine() + "\n"; // toWrite contient le resultat
			} // d'analyse lexicale
			System.out.println(toWrite); // Ecriture de toWrite sur la console
			Writer w = new Writer(args[1], toWrite); // Ecriture de toWrite dans
														// fichier args[1]
			System.out.println("Fin d'analyse lexicale");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public int getPosition() {
		// TODO Auto-generated method stub
		return m_ptrLect;
	}
}
