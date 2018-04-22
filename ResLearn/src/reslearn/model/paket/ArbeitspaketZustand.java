package reslearn.model.paket;

import java.util.ArrayList;

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

	public ArrayList<Arbeitspaket> getArbeitspaketListe() {
		return arbeitspaketListe;
	}

	public void setArbeitspaketListe(ArrayList<Arbeitspaket> arbeitspaketListe) {
		this.arbeitspaketListe = arbeitspaketListe;
	}

}