package app2;

import app2.Terminal.Type;

/** @author Ahmed Khoumsi */

/**
 * Cette classe effectue l'analyse syntaxique
 */
public class DescenteRecursive {

	// Attributs
	private AnalLex mAnalLex;
	private Terminal mCourant;
	private int mOpennedParentheses = 0;

	/**
	 * Constructeur de DescenteRecursive : - recoit en argument le nom du
	 * fichier contenant l'expression a analyser - pour l'initalisation
	 * d'attribut(s)
	 */
	public DescenteRecursive(String in) {
		Reader r = new Reader(in);
		mAnalLex = new AnalLex(r.toString());
	}

	/**
	 * AnalSynt() effectue l'analyse syntaxique et construit l'AST. Elle
	 * retourne une reference sur la racine de l'AST construit
	 */
	public ElemAST AnalSynt() {
		try {
			if (mAnalLex.resteTerminal()) {
				mCourant = mAnalLex.prochainTerminal();
				return T_EXP();
			}
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

		return null;
	}

	/**
	 * token EXP
	 * 
	 * @return
	 * @throws Exception
	 */
	private ElemAST T_EXP() throws Exception {
		if (!isInSetPR(mCourant)) {
			throw new ExceptionLexicale("L'unité lexicale suivante n'est pas dans le SET premier",
					mAnalLex.getPosition());
		}
		ElemAST n1 = T_U();

		if (mCourant.getChaine().equals("+") || mCourant.getChaine().equals("-")) {
			String opt = mCourant.getChaine();
			mCourant = mAnalLex.prochainTerminal();
			if (!isInSetPR(mCourant)) {
				throw new ExceptionLexicale(
						"L'unité lexicale suivante n'est pas dans le SET premier",
						mAnalLex.getPosition());
			}
			ElemAST n2 = T_EXP();
			return new NoeudAST(opt, n1, n2);
		} else if (mCourant.getChaine().equals("(")) {
			throw new ExceptionLexicale("Parenthèse après une opérande", mAnalLex.getPosition());
		} else if (mCourant.getChaine().equals(")") && mOpennedParentheses == 0) {
			throw new ExceptionLexicale("Parenthèse fermante sans parenthèse ouvrante correspondante", mAnalLex.getPosition());
		} else if (mCourant.getType() == Terminal.Type.CONSTANTE && !mCourant.getChaine().equals("")) {
			throw new ExceptionLexicale("Constante après une variable sans opérateur", mAnalLex.getPosition());
		} else if (mCourant.getType() == Terminal.Type.VARIABLE && !mCourant.getChaine().equals("")) {
			throw new ExceptionLexicale("Variable après une constante sans opérateur", mAnalLex.getPosition());
		}

		return n1;
	}

	/**
	 * token U
	 * 
	 * @return
	 * @throws Exception
	 */
	private ElemAST T_U() throws Exception {
		ElemAST n1 = T_V();

		if (mCourant.getChaine().equals("*") || mCourant.getChaine().equals("/")) {
			String opt = mCourant.getChaine();
			mCourant = mAnalLex.prochainTerminal();
			if (!isInSetPR(mCourant)) {
				throw new ExceptionLexicale(
						"L'unité lexicale suivante n'est pas dans le SET premier",
						mAnalLex.getPosition());
			}
			ElemAST n2 = T_U();
			return new NoeudAST(opt, n1, n2);
		}

		return n1;
	}

	/**
	 * token V
	 * 
	 * @return
	 * @throws Exception
	 */
	private ElemAST T_V() throws Exception {

		if (mCourant.getType() == Type.CONSTANTE || mCourant.getType() == Type.VARIABLE) {
			// la valeur d'une variable est 0
			int value = 0;
			// si c'est une constante on essait d'évaluer sa valeur
			if (mCourant.getType() == Type.CONSTANTE)
				try {
					value = Integer.parseInt(mCourant.getChaine());
				} catch (Exception e) {
					// do nothing
				}
			ElemAST n = new FeuilleAST(mCourant.getChaine(), value);
			mCourant = mAnalLex.prochainTerminal();
			return n;
		} else if (mCourant.getChaine().equals("(")) {
			mCourant = mAnalLex.prochainTerminal();
			mOpennedParentheses++;
			if (!isInSetPR(mCourant)) {
				throw new ExceptionLexicale(
						"L'unité lexicale suivante n'est pas dans le SET premier",
						mAnalLex.getPosition());
			}
			ElemAST n = T_EXP();
			if (mCourant.getChaine().equals(")")) {
				mCourant = mAnalLex.prochainTerminal();
				mOpennedParentheses--;
				return n;
			} else {
				throw new ExceptionLexicale("Il manque la parenthèse fermante",
						mAnalLex.getPosition());
			}

		}
		throw new ExceptionLexicale(
				"Mauvaise concatenation des opérateurs ou expression finissant par un opérateur",
				mAnalLex.getPosition());
	}

	private boolean isInSetPR(Terminal UL) {
		if (UL.getChaine() == "(" || UL.getType() == Type.CONSTANTE || UL.getType() == Type.VARIABLE) {
			return true;
		} else {
			return false;
		}

	}

	// Methode principale a lancer pour tester l'analyseur syntaxique
	public static void main(String[] args) {
		String toWriteLect = "";
		String toWriteEval = "";

		System.out.println("Debut d'analyse syntaxique");
		if (args.length == 0) {
			args = new String[2];
			args[0] = "ExpArith.txt";
			args[1] = "ResultatSyntaxique.txt";
		}
		DescenteRecursive dr = new DescenteRecursive(args[0]);
		try {
			ElemAST RacineAST = dr.AnalSynt();
			toWriteLect += "Lecture de l'AST trouve : " + RacineAST.LectAST() + "\n";
			System.out.println(toWriteLect);
			toWriteEval += "Evaluation de l'AST trouve : " + RacineAST.EvalAST() + "\n";
			System.out.println(toWriteEval);
			Writer w = new Writer(args[1], toWriteLect + toWriteEval); // Ecriture
																		// de
																		// toWrite
																		// dans
																		// fichier
																		// args[1]
		} catch (Exception e) {
			System.out.println(e);
			e.printStackTrace();
			System.exit(51);
		}
		System.out.println("Analyse syntaxique terminee");
	}

}
