package app2;

public class ExceptionLexicale extends Exception {

	/**
	 * Unique ID of class
	 */
	private static final long serialVersionUID = 8417942520423733784L;

	public ExceptionLexicale() {
		super("Exception Lexicale");
	}

	public ExceptionLexicale(String message, int position) {
		super(message + ": Position " + position);
		// TODO Auto-generated constructor stub
	}

	public ExceptionLexicale(Throwable cause) {
		super(cause);
		// TODO Auto-generated constructor stub
	}

	public ExceptionLexicale(String message, int position, Throwable cause) {
		super(message + ": Position " + position, cause);
		// TODO Auto-generated constructor stub
	}

	public ExceptionLexicale(String message, int position, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message + ": Position " + position, cause, enableSuppression, writableStackTrace);
		// TODO Auto-generated constructor stub
	}

}
