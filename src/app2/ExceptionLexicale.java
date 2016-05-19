package app2;

public class ExceptionLexicale extends Exception {

	/**
	 * Unique ID of class
	 */
	private static final long serialVersionUID = 8417942520423733784L;

	public ExceptionLexicale() {
		super("Erreur Lexicale");
	}

	public ExceptionLexicale(String message, int position) {
		super("Erreur Lexicale: " + message + ": Position " + position);
	}

	public ExceptionLexicale(Throwable cause) {
		super(cause);
	}

	public ExceptionLexicale(String message, int position, Throwable cause) {
		super("Erreur Lexicale" + message + ": Position " + position, cause);
	}

	public ExceptionLexicale(String message, int position, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super("Erreur Lexicale: " + message + ": Position " + position, cause, enableSuppression, writableStackTrace);
	}

}
