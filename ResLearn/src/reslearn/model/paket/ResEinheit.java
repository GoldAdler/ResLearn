package reslearn.model.paket;

public class ResEinheit extends Paket {

	/**
	 * Referenz auf das Teilpaket
	 */
	private Teilpaket teilpaket;
	private Vektor2i vektor;
	private static final int BREITE = 1;
	private static final int LAENGE = 1;
	private static final int FLAECHE = 1;

	public ResEinheit() {

	}

	public ResEinheit(Teilpaket teilpaket) {
		super(BREITE, LAENGE, FLAECHE);
		this.teilpaket = teilpaket;
	}

	public Teilpaket getTeilpaket() {
		return teilpaket;
	}

	public void setTeilpaket(Teilpaket teilpaket) {
		this.teilpaket = teilpaket;
	}

	public static int getBreite() {
		return BREITE;
	}

	public static int getLaenge() {
		return LAENGE;
	}

	public static int getFlaeche() {
		return FLAECHE;
	}

	public Vektor2i getVektor() {
		return vektor;
	}

	public void setVektor(Vektor2i vektor) {
		this.vektor = vektor;
	}
}