package reslearn.model.utils;

import java.util.Comparator;

import reslearn.model.paket.Arbeitspaket;

/**
 * Sortiert die Arbeitspaketet nach dem FAZ Sind die FAZ der Pakete gleich, wird
 * nach der Vorgangsdauer sortiert. Dadurch befinden sich die längeren Pakete
 * vorne in der Liste
 *
 * @param index
 *            index at which the specified element is to be inserted
 * @param element
 *            element to be inserted
 * @throws IndexOutOfBoundsException
 *             {@inheritDoc}
 */
public class ComperatorFaz implements Comparator<Arbeitspaket> {

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
