package reslearn.model.paket;

import java.util.Comparator;

public class ComperatorErsteSchrittModus implements Comparator<Arbeitspaket> {

	@Override
	public int compare(Arbeitspaket ap1, Arbeitspaket ap2) {
		int result;
		result = ap1.getFaz() - ap2.getFaz();
		if (result == 0) {
			result = (ap2.getFez() - ap2.getFaz()) - (ap1.getFez() - ap1.getFaz());
		}
		return result;
	}

}
