package reslearn.model.paket;

import java.util.Comparator;

public class ComperatorVektor2i implements Comparator<ResEinheit> {

	@Override
	public int compare(ResEinheit re1, ResEinheit re2) {
		int result;

		Vektor2i vek1 = re1.getPosition();
		Vektor2i vek2 = re2.getPosition();

		result = vek2.getyKoordinate() - vek1.getyKoordinate();

		if (result == 0) {
			result = vek1.getxKoordinate() - vek2.getxKoordinate();
		}
		return result;
	}

}
