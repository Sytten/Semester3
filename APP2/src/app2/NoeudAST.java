package app2;

/** @author Ahmed Khoumsi */

/** Classe representant une feuille d'AST
 */
public class NoeudAST extends ElemAST {

  // Attributs
 private ElemAST mEnfantGauche;
 private ElemAST mEnfantDroit;
 private Terminal mTerminal;

  /** Constructeur pour l'initialisation d'attributs
   */
  public NoeudAST(Terminal pTerminal, ElemAST pEnfantGauche, ElemAST pEnfantDroit) {
	  mEnfantGauche = pEnfantGauche;
	  mEnfantDroit = pEnfantDroit;
	  mTerminal = pTerminal;
  }


  /** Evaluation de noeud d'AST
   */
  public int EvalAST() throws Exception {
	  if(mTerminal.getChaine().equals("+")) {
		  return mEnfantGauche.EvalAST() + mEnfantDroit.EvalAST();
	  } else if (mTerminal.getChaine().equals("-")) {
		  return mEnfantGauche.EvalAST() - mEnfantDroit.EvalAST();
	  } else if (mTerminal.getChaine().equals("*")) {
		  return mEnfantGauche.EvalAST() * mEnfantDroit.EvalAST();
	  } else if (mTerminal.getChaine().equals("/")) {
		  return mEnfantGauche.EvalAST() / mEnfantDroit.EvalAST();
	  }

	  throw new Exception("Noeud Invalide");
  }


  /** Lecture de noeud d'AST
   */
  public String LectAST( ) {
     return "(" + mEnfantGauche.LectAST() + mTerminal.getChaine() + mEnfantDroit.LectAST() + ")";
  }

}


