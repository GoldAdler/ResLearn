package reslearn.model.utils;

import java.util.Comparator;

import reslearn.model.paket.Arbeitspaket;

/**
 * Sortiert die Arbeitspaketet nach den Vektor2i-Positionen. Sortiert die Pakete
 * von Links nach Rechts.
 *
 */
public class ComperatorArbeitspaketRL implements Comparator<Arbeitspaket> {

	@Override
	public int compare(Arbeitspaket ap1, Arbeitspaket ap2) {
		int result;

		Vektor2i vek1 = ap1.getTeilpaketListe().get(0).getResEinheitListe().get(0).getPosition();
		Vektor2i vek2 = ap2.getTeilpaketListe().get(0).getResEinheitListe().get(0).getPosition();

		result = vek1.getxKoordinate() - vek2.getxKoordinate();

		if (result == 0) {
			result = vek2.getyKoordinate() - vek1.getyKoordinate();
		}
		return result;
	}
}
