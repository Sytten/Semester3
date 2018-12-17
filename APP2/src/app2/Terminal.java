package app2;

/** @author Ahmed Khoumsi */

/**
 * Cette classe identifie les terminaux reconnus et retournes par l'analyseur
 * lexical
 */
public class Terminal {

	/**
	 * Enumeration du type de terminal
	 */
	public enum Type {
		OPERATEUR, CONSTANTE, VARIABLE
	}

	/**
	 * Variables membres
	 */
	private String mChaine;
	private Type mType;

	/**
	 * Un ou deux constructeurs (ou plus, si vous voulez) pour l'initalisation
	 * d'attributs
	 */
	public Terminal(String pChaine, Type pType) { // arguments possibles
		mChaine = pChaine;
		mType = pType;
	}
	
	public Type getType() {
		return mType;
	}
	
	public String getChaine() {
		return mChaine;
	}
}
