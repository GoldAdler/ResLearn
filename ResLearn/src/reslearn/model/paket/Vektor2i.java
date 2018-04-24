package reslearn.model.paket;

public class Vektor2i {

	private int xKoordinate;
	private int yKoordinate;

	public Vektor2i() {

	}

	public Vektor2i(int yKoordinate, int xKoordinate) {
		this.xKoordinate = xKoordinate;
		this.yKoordinate = yKoordinate;
	}

	public void add(Vektor2i vektor2i) {
		this.xKoordinate += vektor2i.xKoordinate;
		this.yKoordinate -= vektor2i.yKoordinate;
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