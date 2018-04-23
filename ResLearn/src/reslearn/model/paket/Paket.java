package reslearn.model.paket;

public abstract class Paket {

	protected Vektor2i position;
	protected int vorgangsdauer;
	protected int mitarbeiteranzahl;
	/**
	 * Produkt aus Vorgansdauer und Mitarbeiteranzahl
	 */
	protected int aufwand;

	public Paket() {

	}

	public Paket(int vorgangsdauer, int mitarbeiteranzahl, int aufwand) {
		this.vorgangsdauer = vorgangsdauer;
		this.mitarbeiteranzahl = mitarbeiteranzahl;
		this.aufwand = aufwand;
	}

	public Vektor2i getPosition() {
		return position;
	}

	public void setPosition(Vektor2i position) {
		this.position = position;
	}

	public int getVorgangsdauer() {
		return vorgangsdauer;
	}

	public void setVorgangsdauer(int vorgangsdauer) {
		this.vorgangsdauer = vorgangsdauer;
	}

	public int getMitarbeiteranzahl() {
		return mitarbeiteranzahl;
	}

	public void setMitarbeiteranzahl(int mitarbeiteranzahl) {
		this.mitarbeiteranzahl = mitarbeiteranzahl;
	}

	public int getAufwand() {
		return aufwand;
	}

	public void setAufwand(int aufwand) {
		this.aufwand = aufwand;
	}
}