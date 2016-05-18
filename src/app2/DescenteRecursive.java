package app2;

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
				return T_E();
			}
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

		return null;
	}

	// Methode pour chaque symbole non-terminal de la grammaire retenue
	private ElemAST T_E() throws Exception {
		ElemAST n1 = T_T();
		if (m_courant != null) {
			if (m_courant.getChaine().equals("+")) {
				if (m_analLex.resteTerminal()) {
					m_courant = m_analLex.prochainTerminal();
					ElemAST n2 = T_E();
					return new NoeudAST("+", n1, n2);
				} else {
					throw new Exception("Syntaxe invalide");
				}
			}
		}

		return n1;
	}

	private ElemAST T_T() throws Exception {
		try {
			int valeur = Integer.valueOf(m_courant.getChaine()); // Throws if not a
															// number

			ElemAST n = new FeuilleAST(m_courant.getChaine(), valeur);
			if (m_analLex.resteTerminal()) {
				m_courant = m_analLex.prochainTerminal();
			} else {
				m_courant = null;
			}

			return n;

		} catch (Exception e) {
			throw new Exception("Syntaxe invalide");
		}
	}

	/**
	 * ErreurSynt() envoie un message d'erreur syntaxique
	 */
	public void ErreurSynt(String s) {
		//
	}

	// Methode principale a lancer pour tester l'analyseur syntaxique
	/*public static void main(String[] args) {
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
	}*/

}
