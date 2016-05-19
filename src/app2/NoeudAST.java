package app2;

/** @author Ahmed Khoumsi */

/** Classe representant une feuille d'AST
 */
public class NoeudAST extends ElemAST {

  // Attributs
 private ElemAST m_enfantG;
 private ElemAST m_enfantD;
 private String m_chaine;

  /** Constructeur pour l'initialisation d'attributs
   */
  public NoeudAST(String c, ElemAST g, ElemAST d) {
	  m_enfantG = g;
	  m_enfantD = d;
	  m_chaine = c;
  }


  /** Evaluation de noeud d'AST
   */
  public int EvalAST() throws Exception {
	  if(m_chaine.equals("+")) {
		  return m_enfantG.EvalAST() + m_enfantD.EvalAST();
	  } else if (m_chaine.equals("-")) {
		  return m_enfantG.EvalAST() - m_enfantD.EvalAST();
	  } else if (m_chaine.equals("*")) {
		  return m_enfantG.EvalAST() * m_enfantD.EvalAST();
	  } else if (m_chaine.equals("/")) {
		  return m_enfantG.EvalAST() / m_enfantD.EvalAST();
	  }

	  throw new Exception("Noeud Invalide");
  }


  /** Lecture de noeud d'AST
   */
  public String LectAST( ) {
     return "(" + m_enfantG.LectAST() + m_chaine + m_enfantD.LectAST() + ")";
  }

}


