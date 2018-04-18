package reslearn.model.paket;

/**
 * @author Lukas Willburger
 * @version 1.0
 * @created 18-Apr-2018 10:53:18
 */
public abstract class Paket {

	private Vektor2i position;
	private int vorgangsdauer;
	private int mitarbeitersanzahl;
	/**
	 * Produkt aus Vorgansdauer und Mitarbeiteranzahl
	 */
	private int aufwand;
	public Vektor2i m_Vektor2i;

	public Paket(){

	}

	public void finalize() throws Throwable {

	}
}//end Paket