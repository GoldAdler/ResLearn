package reslearn.model.utils;

import java.util.Comparator;

import reslearn.model.paket.Teilpaket;

public class ComperatorTeilpaket implements Comparator<Teilpaket> {

	@Override
	public int compare(Teilpaket tp1, Teilpaket tp2) {

		int result;

		Vektor2i vek1 = tp1.getResEinheitListe().get(0).getPosition();
		Vektor2i vek2 = tp2.getResEinheitListe().get(0).getPosition();

		result = vek2.getyKoordinate() - vek1.getyKoordinate();

		if (result == 0) {
			result = vek1.getxKoordinate() - vek2.getxKoordinate();
		}
		return result;
	}

}
