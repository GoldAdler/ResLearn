package reslearn.model.paket;

import reslearn.model.resCanvas.ResCanvas;

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

	@Override
	public void bewegen(ResCanvas resCanvas, int yMove, int xMove) {

		// TODO: Kommentar eventuell weg
		// itereriert rückwärts
		// somit mussen die Reseinheiten nur einmal angefasst werden
		// i.a. Reseinheit von aktueller Position im Koordinatensystem löschen und um
		// eines nach oben verschieben
		// ListIterator<ResEinheit> li =
		// resEinheitListe.listIterator(resEinheitListe.size());

		// while (li.hasPrevious()) {
		// resEinheit = li.previous();
		// Vektor2i vektor = this.getPosition();
		// resCanvas.bewege(resEinheit, resEinheit.get)
		// resCanvas.delete(vektor.getyKoordinate()][vektor.getxKoordinate() );

		Vektor2i altePosition = new Vektor2i(this.position.getyKoordinate(), this.position.getxKoordinate());
		this.position.add(new Vektor2i(yMove, xMove));

		resCanvas.updatePosition(this, altePosition);

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

}