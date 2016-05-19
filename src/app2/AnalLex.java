package app2;

/** @author Ahmed Khoumsi */

/**
 * Cette classe effectue l'analyse lexicale
 */
public class AnalLex {

	// Attributs
	private String mExpression;

	private int mEtat = 0;
	private int mPtrLect = 0;

	/**
	 * Constructeur pour l'initialisation d'attribut(s)
	 */
	public AnalLex(String s) { // arguments possibles
		mExpression = s;
	}

	/**
	 * resteTerminal() retourne : false si tous les terminaux de l'expression
	 * arithmetique ont ete retournes true s'il reste encore au moins un
	 * terminal qui n'a pas ete retourne
	 */
	public boolean resteTerminal() {
		if (mPtrLect < mExpression.length())
			return true;
		return false;
	}

/** prochainTerminal() retourne le prochain terminal
      Cette methode est une implementation d'un AEF
 */
  public Terminal prochainTerminal( ) throws Exception {
	  char c;
	  String result = "";
	  while (mPtrLect < mExpression.length()) {
		  c = mExpression.charAt(mPtrLect);
		  mPtrLect++;
		  switch (mEtat) {
		  	case 0 :
		  		if (Character.isDigit(c)) {
		  			result += String.valueOf(c);
			  		mEtat = 1;
		  		} else if (Character.isUpperCase(c)) {
		  			result += String.valueOf(c);
		  			mEtat = 2;
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
		  			throw new ExceptionLexicale("Lettre minuscule debut invalide", mPtrLect);
		  		} else {
		  			throw new ExceptionLexicale("Caractere invalide", mPtrLect);
		  		}
		  		break;
		  	case 1 :
		  		if (Character.isDigit(c)) {
		  			result += String.valueOf(c);
		  		} else {
		  			mPtrLect--;
		  			mEtat = 0;
		  			return new Terminal(result, Terminal.Type.CONSTANTE);
		  		}
		  		break;
		  	case 2:
		  		if (Character.isLetter(c)) {
		  			result += String.valueOf(c);
		  		} else if (c == '_') {
		  			result += String.valueOf(c);
		  			mEtat = 3;
		  		} else {
		  			mPtrLect--;
		  			mEtat = 0;
		  			return new Terminal(result, Terminal.Type.VARIABLE);
		  		}
		  		break;
		  	case 3:
		  		if (Character.isLetter(c)) {
		  			result += String.valueOf(c);
		  			mEtat = 2;
		  		} else if(c == '_') {
		  			throw new ExceptionLexicale("Double tiret", mPtrLect);
		  		} else {
		  			throw new ExceptionLexicale("Caractere invalide", mPtrLect);
		  		}
		  		break;
		  	default:
		  		break;
		  }
	  }

	  /**
	   * Etats non finaux
	   */
	  if(mEtat == 3) {
		  throw new ExceptionLexicale("Tiret de fin", mPtrLect);
	  }
	
	  /**
	   * Etats finaux
	   */
	  if (mEtat == 0) {
		  return new Terminal(result, Terminal.Type.OPERATEUR);
	  } else if (mEtat == 1) {
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
		return mPtrLect;
	}
}
