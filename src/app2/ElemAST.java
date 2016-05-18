package app2;

/** @author Ahmed Khoumsi */

/** Classe Abstraite dont heriteront les classes FeuilleAST et NoeudAST
 */
public abstract class ElemAST {


  /** Evaluation d'AST
 * @throws Exception
   */
  public abstract int EvalAST() throws Exception;


  /** Lecture d'AST
   */
  public abstract String LectAST();


/** ErreurEvalAST() envoie un message d'erreur lors de la construction d'AST
 */
  public void ErreurEvalAST(String s) {
    //
  }

}
