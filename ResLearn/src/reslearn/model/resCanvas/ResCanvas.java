package reslearn.model.resCanvas;

import java.util.LinkedList;

import reslearn.model.paket.Arbeitspaket;
import reslearn.model.paket.ArbeitspaketZustand;
import reslearn.model.paket.ResEinheit;
import reslearn.model.paket.Vektor2i;

public class ResCanvas {

	private LinkedList<ArbeitspaketZustand> arbeitspaketZustandListe;
	private ArbeitspaketZustand aktuellerZustand;
	public static final int koorHoehe = 50;
	public static final int koorBreite = 50;
	private ResEinheit[][] koordinatenSystem;

	public ResCanvas() {
		arbeitspaketZustandListe = new LinkedList<ArbeitspaketZustand>();
		aktuellerZustand = new ArbeitspaketZustand();
		// arbeitspaketZustandListe.add(aktuellerZustand);
		koordinatenSystem = new ResEinheit[koorHoehe][koorBreite];
	}

	public void updatePosition(ResEinheit resEinheit, Vektor2i altePosition) {
		this.koordinatenSystem[altePosition.getyKoordinate()][altePosition.getxKoordinate()] = null;
		Vektor2i position = resEinheit.getPosition();
		this.koordinatenSystem[position.getyKoordinate()][position.getxKoordinate()] = resEinheit;
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

	public ResEinheit[][] getKoordinatenSystem() {
		return koordinatenSystem;
	}

	public void setKoordinatenSystem(ResEinheit[][] koordinatenSystem) {
		this.koordinatenSystem = koordinatenSystem;
	}

}