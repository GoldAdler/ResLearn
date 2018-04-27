package reslearn.model.resCanvas;

import java.util.ArrayList;
import java.util.LinkedList;

import reslearn.model.paket.Arbeitspaket;
import reslearn.model.paket.ArbeitspaketZustand;
import reslearn.model.paket.ResEinheit;
import reslearn.model.paket.Teilpaket;
import reslearn.model.paket.Vektor2i;

public class ResCanvas {

	private LinkedList<ArbeitspaketZustand> arbeitspaketZustandListe;
	private ArbeitspaketZustand aktuellerZustand;
	private ResEinheit[][] koordinatenSystem;
	public static final int koorHoehe = 28;
	public static final int koorBreite = 43;
	private int maxMitarbeiter = koorHoehe - 1;

	public ResCanvas() {
		arbeitspaketZustandListe = new LinkedList<ArbeitspaketZustand>();
		aktuellerZustand = new ArbeitspaketZustand();
		// arbeitspaketZustandListe.add(aktuellerZustand);
		koordinatenSystem = new ResEinheit[koorHoehe][koorBreite];
	}

	public ResCanvas(int maxMitarbeiter) {
		arbeitspaketZustandListe = new LinkedList<ArbeitspaketZustand>();
		aktuellerZustand = new ArbeitspaketZustand();
		// arbeitspaketZustandListe.add(aktuellerZustand);
		koordinatenSystem = new ResEinheit[koorHoehe][koorBreite];
		this.maxMitarbeiter = maxMitarbeiter;
	}

	public void updatePosition(ResEinheit resEinheit, Vektor2i altePosition) {
		this.koordinatenSystem[altePosition.getyKoordinate()][altePosition.getxKoordinate()] = null;
		Vektor2i position = resEinheit.getPosition();
		this.koordinatenSystem[position.getyKoordinate()][position.getxKoordinate()] = resEinheit;
	}

	/**
	 * entfernen des Teilpakets aus dem Koordinatensystem! Teilpaket ist nicht
	 * gelöscht, sondern weiß immer noch die Position
	 *
	 * @param teilpaket
	 */
	public void entfernenTeilpaket(Teilpaket teilpaket) {
		Vektor2i position;
		for (ResEinheit resEinheit : teilpaket.getResEinheitListe()) {
			position = resEinheit.getPosition();
			koordinatenSystem[position.getyKoordinate()][position.getxKoordinate()] = null;
		}
	}

	public void herunterfallen(Teilpaket teilpaket) {
		Vektor2i position;
		ResEinheit unterhalb;
		ResEinheit oberhalb;
		ArrayList<ResEinheit> neuesTeilpaket = new ArrayList<ResEinheit>();
		for (ResEinheit res : teilpaket.getResEinheitListe()) {

			position = res.getPosition();
			int x = position.getxKoordinate();
			int y = position.getyKoordinate();

			if (y != ResCanvas.koorHoehe - 1) {
				unterhalb = koordinatenSystem[y + 1][x];

				if (unterhalb == null) {

					for (int yPos = y; yPos >= 0; yPos--) {
						oberhalb = koordinatenSystem[yPos][x];
						if (oberhalb == null) {
							break;
						}

						neuesTeilpaket.add(oberhalb);
					}
				} else {
					break;
				}

				// TODO: da mache mer weiter ihr lappos
				// - sortieren der ArrayList
				// - ResEinheiten aus dem altten Teilpaket entfernen
				// - neues Teilpaket anlegen und ARbeitspaket zuordnen
				// - neuem Teilpaket Arraylist übergeben
				// - nach unten fahren lassen
				// - Rekursives Problem bedenken (wenn kleines Paketchen unter packtneahn unt
				// dkaghhe t)

			}

		}

		// TODO: aus anderem Algo rauskopiert. löschen wenn nicht mehr gebraucht
		// // wenn unterhalb der aktuellen resEinheit nichts liegt,
		// // wir die Schleife abgebrochen und die nächste resEinheit betrachtet
		// if (unterhalb == null) {
		// break;
		// }
		// // wenn unterhalb der aktuellen resEinheit eine Reseinheit liegt, die
		// // vom selben Teilpaket ist, wird die Betrachtung des aktuellen Teilpaketes
		// // abgebrochen
		// // die variable position wird auf die vorhergegane ResEinheit gesetzt
		// if (teilpaket == unterhalb.getTeilpaket()) {
		// int index = teilpaket.getResEinheitListe().indexOf(res);
		// position = teilpaket.getResEinheitListe().get(index - 1).getPosition();
		// break LOOP;
		// }
		//
		// unterhalbUndRechts.add(unterhalb.getTeilpaket());
		// resCanvas.entfernenTeilpaket(unterhalb.getTeilpaket());

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

	public int getMaxMitarbeiter() {
		return maxMitarbeiter;
	}

	public void setMaxMitarbeiter(int maxMitarbeiter) {
		this.maxMitarbeiter = maxMitarbeiter;
	}

}