package reslearn.model.paket;

/**
 * @author Lukas Willburger
 * @version 1.0
 * @created 18-Apr-2018 10:53:21
 */
public class Vektor2i {

	private int xKoordinate;
	private int yKoordinate;

	public Vektor2i() {

	}

	public Vektor2i(int xKoordinate, int yKoordinate) {
		this.xKoordinate = xKoordinate;
		this.yKoordinate = yKoordinate;
	}

	public void add(Vektor2i vektor2i) {
		this.xKoordinate += vektor2i.xKoordinate;
		this.yKoordinate += vektor2i.yKoordinate;
	}
}