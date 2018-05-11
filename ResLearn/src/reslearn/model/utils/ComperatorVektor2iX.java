package reslearn.model.utils;

import java.util.Comparator;

import reslearn.model.paket.ResEinheit;

public class ComperatorVektor2iX implements Comparator<ResEinheit> {

	@Override
	public int compare(ResEinheit re1, ResEinheit re2) {
		int result;

		Vektor2i vek1 = re1.getPosition();
		Vektor2i vek2 = re2.getPosition();

		result = vek1.getxKoordinate() - vek2.getxKoordinate();

		if (result == 0) {
			result = vek2.getyKoordinate() - vek1.getyKoordinate();
		}
		return result;
	}

}
