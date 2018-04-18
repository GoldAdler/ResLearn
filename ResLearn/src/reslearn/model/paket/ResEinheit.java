package reslearn.model.paket;

/**
 * @author Lukas Willburger
 * @version 1.0
 * @created 18-Apr-2018 10:53:19
 */
public class ResEinheit extends Paket {

	/**
	 * Referenz auf das Teilpaket
	 */
	private Teilpaket teilpaket;
	private static final int BREITE = 1;
	private static final int LAENGE = 1;
	private static final int FLAECHE = 1;

	public ResEinheit() {

	}

	public ResEinheit(Teilpaket teilpaket) {
		super(BREITE, LAENGE, FLAECHE);
		this.teilpaket = teilpaket;
	}
}