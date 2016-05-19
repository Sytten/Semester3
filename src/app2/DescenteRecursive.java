package app2;

import app2.Terminal.Type;

/** @author Ahmed Khoumsi */

/**
 * Cette classe effectue l'analyse syntaxique
 */
public class DescenteRecursive {

	// Attributs
	private AnalLex m_analLex;
	private Terminal m_courant;

	/**
	 * Constructeur de DescenteRecursive : - recoit en argument le nom du
	 * fichier contenant l'expression a analyser - pour l'initalisation
	 * d'attribut(s)
	 */
	public DescenteRecursive(String in) {
		Reader r = new Reader(in);
		m_analLex = new AnalLex(r.toString());
	}

	/**
	 * AnalSynt() effectue l'analyse syntaxique et construit l'AST. Elle
	 * retourne une reference sur la racine de l'AST construit
	 */
	public ElemAST AnalSynt() {
		try {
			if (m_analLex.resteTerminal()) {
				m_courant = m_analLex.prochainTerminal();
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
		if (!isInSetPR(m_courant)) {
			throw new ExceptionLexicale("Erreur Syntaxique : l'unité lexicale suivante n'est pas dans le SET premier",
					m_analLex.getPosition());
		}
		ElemAST n1 = T_U();

		if (m_courant.getChaine().equals("+") || m_courant.getChaine().equals("-")) {
			String opt = m_courant.getChaine();
			m_courant = m_analLex.prochainTerminal();
			if (!isInSetPR(m_courant)) {
				throw new ExceptionLexicale(
						"Erreur Syntaxique : l'unité lexicale suivante n'est pas dans le SET premier",
						m_analLex.getPosition());
			}
			ElemAST n2 = T_EXP();
			return new NoeudAST(opt, n1, n2);
		} else if (m_courant.getChaine().equals("(")) {
			throw new ExceptionLexicale("Erreur Syntaxique : parenthèse après une opérande", m_analLex.getPosition());
		} else if (m_courant.getType() == Terminal.Type.CONSTANTE && !m_courant.getChaine().equals("")) {
			throw new ExceptionLexicale("Erreur Syntaxique : constante après une variable", m_analLex.getPosition());
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

		if (m_courant.getChaine().equals("*") || m_courant.getChaine().equals("/")) {
			String opt = m_courant.getChaine();
			m_courant = m_analLex.prochainTerminal();
			if (!isInSetPR(m_courant)) {
				throw new ExceptionLexicale(
						"Erreur Syntaxique : l'unité lexicale suivante n'est pas dans le SET premier",
						m_analLex.getPosition());
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

		if (m_courant.getType() == Type.CONSTANTE || m_courant.getType() == Type.VARIABLE) {
			// la valeur d'une variable est 0
			int value = 0;
			// si c'est une constante on essait d'évaluer sa valeur
			if (m_courant.getType() == Type.CONSTANTE)
				try {
					value = Integer.parseInt(m_courant.getChaine());
				} catch (Exception e) {
					// do nothing
				}
			ElemAST n = new FeuilleAST(m_courant.getChaine(), value);
			m_courant = m_analLex.prochainTerminal();
			return n;
		} else if (m_courant.getChaine().equals("(")) {
			m_courant = m_analLex.prochainTerminal();
			if (!isInSetPR(m_courant)) {
				throw new ExceptionLexicale(
						"Erreur Syntaxique : l'unité lexicale suivante n'est pas dans le SET premier",
						m_analLex.getPosition());
			}
			ElemAST n = T_EXP();
			if (m_courant.getChaine().equals(")")) {
				m_courant = m_analLex.prochainTerminal();
				return n;
			} else {
				throw new ExceptionLexicale("Erreur Syntaxique : il manque la parenthèse fermante",
						m_analLex.getPosition());
			}

		}
		throw new ExceptionLexicale(
				"Erreur Syntaxique : mauvaise concatenation des opérateurs ou expression finissant par un opérateur",
				m_analLex.getPosition());
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
