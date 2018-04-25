package reslearn.model.resCanvas;

import java.util.LinkedList;
import java.util.ListIterator;

import reslearn.model.paket.Arbeitspaket;
import reslearn.model.paket.ArbeitspaketZustand;
import reslearn.model.paket.ResEinheit;
import reslearn.model.paket.Teilpaket;
import reslearn.model.paket.Vektor2i;

public class ResCanvas {

	private LinkedList<ArbeitspaketZustand> arbeitspaketZustandListe;
	private ArbeitspaketZustand aktuellerZustand;
	public static final int koorHoehe = 28;
	public static final int koorBreite = 43;
	private ResEinheit[][] koordinantenSystem;

	public ResCanvas() {
		arbeitspaketZustandListe = new LinkedList<ArbeitspaketZustand>();
		aktuellerZustand = new ArbeitspaketZustand();
		// arbeitspaketZustandListe.add(aktuellerZustand);
		koordinantenSystem = new ResEinheit[koorBreite][koorHoehe];
	}

	/**
	 *
	 * @param arbeitspaket
	 */
	public void hinzufuegen(Arbeitspaket arbeitspaket) {

		// /*
		// * Im Falle, dass man mehre Schritte zurückgegangen ist (mit undo()) muss beim
		// * Hinzufügen eines neuen Elements die zurückgegangen Elemente gelöscht werden
		// */
		// while (arbeitspaketZustandListe.peekLast() != aktuellerZustand &&
		// !arbeitspaketZustandListe.isEmpty()) {
		// arbeitspaketZustandListe.removeLast();
		// }
		//
		// try {
		// aktuellerZustand = (ArbeitspaketZustand)
		// arbeitspaketZustandListe.getLast().clone();
		// } catch (CloneNotSupportedException e1) {
		// e1.printStackTrace();
		// }

		aktuellerZustand.hinzufuegen(arbeitspaket);
		// arbeitspaketZustandListe.add(aktuellerZustand);

	}

	/**
	 *
	 * @param arbeitspaket
	 */
	public void entfernen(Arbeitspaket arbeitspaket) {
		// /*
		// * Im Falle, dass man mehre Schritte zurückgegangen ist (mit undo()) muss beim
		// * Hinzufügen eines neuen Elements die zurückgegangen Elemente gelöscht werden
		// */
		// while (arbeitspaketZustandListe.peekLast() != aktuellerZustand) {
		// arbeitspaketZustandListe.removeLast();
		// }
		aktuellerZustand.entfernen(arbeitspaket);
		// arbeitspaketZustandListe.add(aktuellerZustand);
	}

	// // TODO: später was sinnvolleres als return
	// public void undo() {
	// int index = arbeitspaketZustandListe.indexOf(aktuellerZustand);
	// if (index == -1 || index == 0) {
	// return;
	// }
	//
	// aktuellerZustand = arbeitspaketZustandListe.get(index - 1);
	// }
	//
	// // TODO: später was sinnvolleres als return
	// public void redo() {
	// int index = arbeitspaketZustandListe.indexOf(aktuellerZustand);
	// if (index + 1 == arbeitspaketZustandListe.size() || index == -1) {
	// return;
	// }
	//
	// aktuellerZustand = arbeitspaketZustandListe.get(index + 1);
	// }

	public LinkedList<ArbeitspaketZustand> getArbeitspaketZustandListe() {
		return arbeitspaketZustandListe;
	}

	public void setArbeitspaketZustandListe(LinkedList<ArbeitspaketZustand> arbeitspaketZustandListe) {
		this.arbeitspaketZustandListe = arbeitspaketZustandListe;
	}

	public ArbeitspaketZustand getAktuellerZustand() {
		return aktuellerZustand;
	}

	public void setAktuellerZustand(ArbeitspaketZustand aktuellerZustand) {
		this.aktuellerZustand = aktuellerZustand;
	}

	public ResEinheit[][] getKoordinantenSystem() {
		return koordinantenSystem;
	}

	public void setKoordinantenSystem(ResEinheit[][] koordinantenSystem) {
		this.koordinantenSystem = koordinantenSystem;
	}

	// TODO Richtge Stelle?
	// Überlegen ob die Bewegen Methode hier angebracht ist
	// Arbeitspaket und Teilpakete sollten eine Bewegen Methode haben???
	public void bewegeNachOben(Teilpaket teilpaket, int y_Move) {
		var resEinheitListe = teilpaket.getResEinheitListe();

		// itereriert rückwärts
		// somit mussen die Reseinheiten nur einmal angefasst werden
		// i.a. Reseinheit von aktueller Position im Koordinatensystem löschen und um
		// eines nach oben verschieben
		ListIterator<ResEinheit> li = resEinheitListe.listIterator(resEinheitListe.size());

		ResEinheit resEinheit;
		while (li.hasPrevious()) {
			resEinheit = li.previous();
			Vektor2i vektor = resEinheit.getPosition();
			this.koordinantenSystem[vektor.getyKoordinate()][vektor.getxKoordinate()] = null;
			vektor.add(new Vektor2i(y_Move, 0));
			this.koordinantenSystem[vektor.getyKoordinate()][vektor.getxKoordinate()] = resEinheit;
		}

	}

}