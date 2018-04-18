package reslearn.model.canvas;

import java.util.LinkedList;

import reslearn.model.paket.Arbeitspaket;
import reslearn.model.paket.ArbeitspaketZustand;

/**
 * @author Lukas Willburger
 * @version 1.0
 * @created 18-Apr-2018 10:53:17
 */
public class Canvas {

	private LinkedList<ArbeitspaketZustand> arbeitspaketZustandListe;
	private ArbeitspaketZustand aktuellerZustand;

	public Canvas() {
		arbeitspaketZustandListe = new LinkedList<ArbeitspaketZustand>();
		aktuellerZustand = new ArbeitspaketZustand();
	}

	/**
	 *
	 * @param arbeitspaket
	 */
	public void hinzufuegen(Arbeitspaket arbeitspaket) {
		while (arbeitspaketZustandListe.peekLast() != aktuellerZustand && !arbeitspaketZustandListe.isEmpty()) {
			arbeitspaketZustandListe.removeLast();
		}

		ArbeitspaketZustand tmp = aktuellerZustand;

		aktuellerZustand.hinzufuegen(arbeitspaket);

		arbeitspaketZustandListe.add(tmp);
	}

	/**
	 *
	 * @param arbeitspaket
	 */
	public void entfernen(Arbeitspaket arbeitspaket) {
		while (arbeitspaketZustandListe.peekLast() != aktuellerZustand) {
			arbeitspaketZustandListe.removeLast();
		}
		aktuellerZustand.entfernen(arbeitspaket);
		arbeitspaketZustandListe.add(aktuellerZustand);
	}

	// TODO: später was sinnvolleres als return
	public void undo() {
		int index = arbeitspaketZustandListe.indexOf(aktuellerZustand);
		if (index == -1 || index == 0) {
			return;
		}

		aktuellerZustand = arbeitspaketZustandListe.get(index - 1);
	}

	// TODO: später was sinnvolleres als return
	public void redo() {
		int index = arbeitspaketZustandListe.indexOf(aktuellerZustand);
		if (index + 1 == arbeitspaketZustandListe.size() || index == -1) {
			return;
		}

		aktuellerZustand = arbeitspaketZustandListe.get(index + 1);
	}

}