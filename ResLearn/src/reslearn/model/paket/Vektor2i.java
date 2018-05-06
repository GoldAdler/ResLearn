package reslearn.model.paket;

public class Vektor2i {

	public enum Methode {
		ADD, SUBTRACT
	}

	private int xKoordinate;
	private int yKoordinate;

	public Vektor2i() {

	}

	public Vektor2i(int yKoordinate, int xKoordinate) {
		this.xKoordinate = xKoordinate;
		this.yKoordinate = yKoordinate;
	}

	public Vektor2i(Vektor2i v1, Vektor2i v2, Methode methode) {
		this.xKoordinate = v1.getxKoordinate();
		this.yKoordinate = v1.getyKoordinate();
		method(v2, methode);
	}

	public void add(Vektor2i vektor2i) {
		this.xKoordinate += vektor2i.xKoordinate;
		this.yKoordinate -= vektor2i.yKoordinate;
	}

	public void subtract(Vektor2i vektor2i) {
		this.xKoordinate -= vektor2i.xKoordinate;
		this.yKoordinate += vektor2i.yKoordinate;
	}

	public void subtract(int yKoordinate, int xKoordinate) {
		this.xKoordinate -= xKoordinate;
		this.yKoordinate += yKoordinate;
	}

	public void method(Vektor2i vektor2i, Methode methode) {
		switch (methode) {
		case ADD:
			add(vektor2i);
			break;
		case SUBTRACT:
			subtract(-vektor2i.getyKoordinate(), vektor2i.getxKoordinate());
			break;
		default:
			break;
		}
	}

	public int getxKoordinate() {
		return xKoordinate;
	}

	public void setxKoordinate(int xKoordinate) {
		this.xKoordinate = xKoordinate;
	}

	public int getyKoordinate() {
		return yKoordinate;
	}

	public void setyKoordinate(int yKoordinate) {
		this.yKoordinate = yKoordinate;
	}

}