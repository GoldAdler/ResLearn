package reslearn.model.paket;

import reslearn.model.resCanvas.ResCanvas;
import reslearn.model.utils.Vektor2i;

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

		Vektor2i altePosition = new Vektor2i(this.position.getyKoordinate(), this.position.getxKoordinate());
		this.position.add(new Vektor2i(yMove, xMove));

		resCanvas.updatePosition(this, altePosition);

	}

	@Override
	public boolean bewegeX(ResCanvas resCanvas, int xMove) {

		Vektor2i altePosition = new Vektor2i(this.position.getyKoordinate(), this.position.getxKoordinate());
		this.position.add(new Vektor2i(0, xMove));

		resCanvas.updatePosition(this, altePosition);

		return true;

	}

	@Override
	public boolean bewegeY(ResCanvas resCanvas, int yMove) {

		Vektor2i altePosition = new Vektor2i(this.position.getyKoordinate(), this.position.getxKoordinate());
		this.position.add(new Vektor2i(yMove, 0));

		resCanvas.updatePosition(this, altePosition);
		return true;
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