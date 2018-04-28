package reslearn.model.resCanvas;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;

import reslearn.main.Main;
import reslearn.model.paket.Arbeitspaket;
import reslearn.model.paket.ArbeitspaketZustand;
import reslearn.model.paket.ComperatorVektor2i;
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

	/**
	 * Jedes Teilpaket, dass im Canvas heruntergesenkt werden kann, wird
	 * heruntergelassen.
	 *
	 */
	public void herunterfallen() {
		for (Arbeitspaket arbeitspaket : this.getAktuellerZustand().getArbeitspaketListe()) {

			var teilpaketListe = arbeitspaket.getTeilpaketListe();
			for (int i = 0; i < teilpaketListe.size(); i++) {
				this.herunterfallen(teilpaketListe.get(i));

				// TODO: Ausgeben löschen
				Main.ausgeben(koordinatenSystem);
			}
		}
	}

	public void herunterfallen(Teilpaket teilpaket) {

		ArrayList<ResEinheit> altesTeilpaketResEinheiten = teilpaket.getResEinheitListe();
		ArrayList<ResEinheit> resEinheitFuerNeuesTeilpaket = new ArrayList<ResEinheit>();
		LinkedList<Teilpaket> zuVerschiebenListe = new LinkedList<Teilpaket>();

		ueberpruefeFallraum(altesTeilpaketResEinheiten, resEinheitFuerNeuesTeilpaket, zuVerschiebenListe);

		// Wenn das neue zu erstellende Teilpaket, aus allen ResEinheiten des alten
		// Teilpaket besteht würde,
		// muss kein neues Teilpaket angelegt werden. Dies liegt daran, da das alte und
		// das
		// neue Teilpaket identisch währen.
		Teilpaket tmp = teilpaket;
		if (altesTeilpaketResEinheiten.size() != resEinheitFuerNeuesTeilpaket.size()) {
			tmp = teilpaket.trenneTeilpaket(resEinheitFuerNeuesTeilpaket);

		}

		if (!tmp.getResEinheitListe().isEmpty()) {
			ResEinheit ausgangspunkt = tmp.getResEinheitListe().get(0);

			boolean kollision = falle(tmp, ausgangspunkt, zuVerschiebenListe);

			if (kollision) {

				// TODO: Ausgeben löschen
				Main.ausgeben(koordinatenSystem);

				this.herunterfallen(tmp);
			}
		}
	}

	/**
	 * Ermittlung des Mindestabstands zwischen dem aktuellen Teilpaket und ersten
	 * eintreffenden Kollision. Anhand des ermittelten Mindestabstands wird das
	 * aktuelle Teilpaket und die darüberliegenden Teilpakete nach unten verschoben.
	 * Gibt es mehrere unterschidliche Kollisionen wird der Rückgabeparameter als
	 * true zurückgegeben.
	 *
	 * @param tmp
	 * @param ausgangspunkt
	 * @return
	 */
	private boolean falle(Teilpaket tmp, ResEinheit ausgangspunkt, LinkedList<Teilpaket> zuVerschiebenListe) {

		int x = ausgangspunkt.getPosition().getxKoordinate();
		int y = ausgangspunkt.getPosition().getyKoordinate();
		int minAbstand = 0;
		int aktuellerAbstand;
		boolean kollision = false;

		for (int xPos = x; xPos < x + tmp.getVorgangsdauer() - 1; xPos++) {
			aktuellerAbstand = 0;
			for (int yPos = y + 1; yPos < ResCanvas.koorHoehe; yPos++) {

				if (koordinatenSystem[yPos][xPos] == null) {
					aktuellerAbstand++;
				} else {
					break;
				}
			}
			if (minAbstand == 0) {
				minAbstand = aktuellerAbstand;

			} else if (minAbstand != aktuellerAbstand) {
				minAbstand = Math.min(minAbstand, aktuellerAbstand);
				kollision = true;
			}

		}

		tmp.bewegen(this, -minAbstand, 0);
		for (Teilpaket teilpaket : zuVerschiebenListe) {
			teilpaket.bewegen(this, -minAbstand, 0);

		}

		return kollision;
	}

	/**
	 * Für jede Reseinheit im teilpaket wird geprüft, ob unter der resEinheit ein
	 * Feld frei ist, wenn ja wird dieses in die Liste resEinheitFuerNeuesTeilpaket
	 * aufgenommen. Teilpakete, die über dem aktuellen Teilpaket liegen, werden in
	 * die zuVerschiebenListe aufgenommen
	 *
	 * @param altesTeilpaketResEinheiten
	 * @param resEinheitFuerNeuesTeilpaket
	 * @param zuVerschiebenListe
	 */
	private void ueberpruefeFallraum(ArrayList<ResEinheit> altesTeilpaketResEinheiten,
			ArrayList<ResEinheit> resEinheitFuerNeuesTeilpaket, LinkedList<Teilpaket> zuVerschiebenListe) {

		Vektor2i position;
		ResEinheit unterhalb;
		ResEinheit oberhalb;

		for (ResEinheit res : altesTeilpaketResEinheiten) {

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

						if (oberhalb.getTeilpaket() == res.getTeilpaket()) {
							resEinheitFuerNeuesTeilpaket.add(oberhalb);
						} else {
							if (!zuVerschiebenListe.contains(oberhalb.getTeilpaket())) {
								zuVerschiebenListe.add(oberhalb.getTeilpaket());
							}
						}
					}
				}
			}
		}

		Collections.sort(resEinheitFuerNeuesTeilpaket, new ComperatorVektor2i());
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