package reslearn.model.paket;

import java.util.ArrayList;

/**
 * @author Lukas Willburger
 * @version 1.0
 * @created 18-Apr-2018 10:53:15
 */
public class ArbeitspaketZustand {

	private ArrayList<Arbeitspaket> arbeitspaketListe;

	public ArbeitspaketZustand() {
		arbeitspaketListe = new ArrayList<Arbeitspaket>();
	}

	/**
	 *
	 * @param arbeitspaket
	 */
	public void hinzufuegen(Arbeitspaket arbeitspaket) {
		arbeitspaketListe.add(arbeitspaket);
	}

	/**
	 *
	 * @param arbeitspaket
	 */
	public void entfernen(Arbeitspaket arbeitspaket) {
		arbeitspaketListe.remove(arbeitspaket);
	}

	public void bearbeiten() {

	}
}// end ArbeitspaketZustand