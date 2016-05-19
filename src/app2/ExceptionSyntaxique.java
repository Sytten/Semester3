package app2;

public class ExceptionSyntaxique extends Exception {

	/**
	 * Unique ID of class
	 */
	private static final long serialVersionUID = -5588519030341713480L;

	public ExceptionSyntaxique() {
		super("Erreur Syntaxique");
	}

	public ExceptionSyntaxique(String message, int position) {
		super("Erreur Syntaxique: " + message + ": Position " + position);
	}

	public ExceptionSyntaxique(Throwable cause) {
		super(cause);
	}

	public ExceptionSyntaxique(String message, int position, Throwable cause) {
		super("Erreur Syntaxique: " + message + ": Position " + position, cause);
	}

	public ExceptionSyntaxique(String message, int position, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super("Erreur Syntaxique: " + message + ": Position " + position, cause, enableSuppression, writableStackTrace);
	}

}
