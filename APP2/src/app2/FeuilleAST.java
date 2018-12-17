package app2;

/** @author Ahmed Khoumsi */

/**
 * Classe representant une feuille d'AST
 */
public class FeuilleAST extends ElemAST {

	// Attribut(s)
	private Terminal mTerminal;

	/**
	 * Constructeur pour l'initialisation d'attribut(s)
	 */
	public FeuilleAST(Terminal pTerminal) {
		mTerminal = pTerminal;
	}

	/**
	 * Evaluation de feuille d'AST
	 */
	public int EvalAST() throws Exception{
		if (mTerminal.getType() != Terminal.Type.CONSTANTE) {
			throw new Exception("Impossible d'évaluer une expression contenant des variables");
		}
		
		int value = 0;
		try {
			value = Integer.parseInt(mTerminal.getChaine());
		} catch (Exception e) {
			// do nothing
		}
		return value;
	}

	/**
	 * Lecture de chaine de caracteres correspondant a la feuille d'AST
	 */
	public String LectAST() {
		return mTerminal.getChaine();
	}

}
