package app2;

/** @author Ahmed Khoumsi */

/** Classe representant une feuille d'AST
 */
public class FeuilleAST extends ElemAST {

  // Attribut(s)
  private String m_chaine;
  private int m_valeur;


/**Constructeur pour l'initialisation d'attribut(s)
 */
  public FeuilleAST(String c, int v) {
    m_chaine = c;
    m_valeur = v;
  }


  /** Evaluation de feuille d'AST
   */
  public int EvalAST( ) {
	  return m_valeur;
  }


 /** Lecture de chaine de caracteres correspondant a la feuille d'AST
  */
  public String LectAST( ) {
	  return m_chaine;
  }

}
