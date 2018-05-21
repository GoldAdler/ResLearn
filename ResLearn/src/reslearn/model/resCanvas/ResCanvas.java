package reslearn.model.resCanvas;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;

import reslearn.model.algorithmus.Algorithmus;
import reslearn.model.paket.Arbeitspaket;
import reslearn.model.paket.ResEinheit;
import reslearn.model.paket.Teilpaket;
import reslearn.model.utils.ComperatorVektor2iX;
import reslearn.model.utils.ComperatorVektor2iY;
import reslearn.model.utils.Vektor2i;

public class ResCanvas {

	private ArrayList<Arbeitspaket> arbeitspaketListe;
	private ArrayList<Arbeitspaket> historienArbeitspaketListe;
	private ResEinheit[][] koordinatenSystem;
	private ArrayList<ResEinheit[][]> historieKoordinatenSystem;
	public static final int koorHoehe = 28;
	public static final int koorBreite = 43;

	public ResCanvas() {
		arbeitspaketListe = new ArrayList<Arbeitspaket>();
		koordinatenSystem = new ResEinheit[koorHoehe][koorBreite];
		historieKoordinatenSystem = new ArrayList<ResEinheit[][]>();
	}

	public void updatePosition(ResEinheit resEinheit, Vektor2i altePosition) {
		this.koordinatenSystem[altePosition.getyKoordinate()][altePosition.getxKoordinate()] = null;
		Vektor2i position = resEinheit.getPosition();
		this.koordinatenSystem[position.getyKoordinate()][position.getxKoordinate()] = resEinheit;
	}

	/**
	 * true: verschiebbar, false: nicht verschiebbar
	 *
	 * @param tp
	 * @param xMove
	 * @param yMove
	 * @return
	 */
	public boolean ueberpruefePosition(Teilpaket tp, int xMove, int yMove) {
		ResEinheit tmp;
		for (ResEinheit res : tp.getResEinheitListe()) {

			Vektor2i neuePosition = res.getPosition();
			int xPos = neuePosition.getxKoordinate() + xMove;
			int yPos = neuePosition.getyKoordinate() - yMove;

			if (xPos < 0 || xPos >= ResCanvas.koorBreite || yPos < 0 || yPos >= ResCanvas.koorHoehe) {
				return false;
			}

			tmp = koordinatenSystem[yPos][xPos];
			if (tmp != null && tmp.getTeilpaket() != res.getTeilpaket()) {
				return false;
			}
		}
		return true;
	}

	/**
	 * entfernen des Arbeitspaket aus dem Koordinatensystem! Arbeitspaket ist nicht
	 * gelöscht, sondern weiß immer noch die Position
	 *
	 * @param arbeitspaket
	 */
	public void entferneArbeitspaket(Arbeitspaket arbeitspaket) {
		for (Teilpaket tp : arbeitspaket.getTeilpaketListe()) {
			entfernenTeilpaket(tp);
		}
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
	 */
	public void herunterfallenAlleTeilpakete() {
		for (Arbeitspaket arbeitspaket : this.getArbeitspaketListe()) {

			var teilpaketListe = arbeitspaket.getTeilpaketListe();
			for (int i = 0; i < teilpaketListe.size(); i++) {
				this.herunterfallen(teilpaketListe.get(i));

				Algorithmus.ausgeben(koordinatenSystem);
			}
		}
	}

	/**
	 * Jedes Teilpaket, außer eines, dass im Canvas heruntergesenkt werden kann,
	 * wird heruntergelassen.
	 */
	public void herunterfallenAlleTeilpaketeAußerEines(Arbeitspaket nichtHerunterfallen) {
		for (Arbeitspaket arbeitspaket : this.getArbeitspaketListe()) {

			if (arbeitspaket != nichtHerunterfallen) {

				var teilpaketListe = arbeitspaket.getTeilpaketListe();
				for (int i = 0; i < teilpaketListe.size(); i++) {
					this.herunterfallen(teilpaketListe.get(i));

					Algorithmus.ausgeben(koordinatenSystem);
				}
			}
		}
	}

	/**
	 * Das übergebene Teilpaket wird soweit nach unten gesenkt, wie möglich. Bei
	 * Kollision wird das Paket in mehrere Teilpaket aufgetrennt. Die neu
	 * entstandenen Teilpakete werden wieder herabgesenkt.
	 *
	 * @param teilpaket
	 */
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

		ArrayList<Teilpaket> zuFallendeTeilpakete = new ArrayList<Teilpaket>();
		ArrayList<ResEinheit> herauszutrennendeResEinheiten = null;
		Teilpaket tmp = null;
		if (altesTeilpaketResEinheiten.size() != resEinheitFuerNeuesTeilpaket.size()
				&& !resEinheitFuerNeuesTeilpaket.isEmpty()) {

			// Prüfe hier, ob Vorgangsunterbrechungen in der resEinheitFuerNeuesTeilpaket
			// stattfindet. Wenn ja mehre Teilpakete erstellen und in eine Liste aufnehmen

			/*
			 * .............DDDDD.........................
			 * .............DDDDD.........................
			 * .............DDDDD.........................
			 * .............DDDDD.........................
			 * .............DDDDD.........................
			 * ..............CC...........................
			 * AAAAAA........CC...........................
			 * AAAAAA...BBBBBBB...........EEEEEE..........
			 * AAAAAAZZZBBBBBBBCCCCCC.....EEEEEE..........
			 * AAAAAAZZZBBBBBBBCCCCCC.....EEEEEE..........
			 *
			 */

			do {

				herauszutrennendeResEinheiten = ueberpruefeVorgangsunterbrechungHerunterfallen(
						resEinheitFuerNeuesTeilpaket);

				tmp = teilpaket.trenneTeilpaketVertikal(herauszutrennendeResEinheiten);
				zuFallendeTeilpakete.add(tmp);

			} while (!resEinheitFuerNeuesTeilpaket.isEmpty());

		} else {
			zuFallendeTeilpakete.add(teilpaket);
		}

		for (Teilpaket lasseFallen : zuFallendeTeilpakete) {

			// if (!tmp.getResEinheitListe().isEmpty()) {
			ResEinheit ausgangspunkt = lasseFallen.getResEinheitListe().get(0);

			boolean kollision = falle(lasseFallen, ausgangspunkt, zuVerschiebenListe);

			if (kollision) {

				Algorithmus.ausgeben(koordinatenSystem);

				this.herunterfallen(lasseFallen);
			}
			// }
		}
	}

	/**
	 * Überprüft eine ArrayList<ResEinheiten> darauf, ob in ihr eine
	 * Vorgangsunterbrechung stattfindet. Wenn ja wird eine neue
	 * ArrayList<ResEinheiten> zurückgegeben, die keine Vorgangsunterbrechung mehr
	 * enthällt. Ansonsten wird die übergeben ArrayList resEinheitFuerNeuesTeilpaket
	 * zurückgegeben.
	 *
	 * @param resEinheitFuerNeuesTeilpaket
	 * @return
	 */
	private ArrayList<ResEinheit> ueberpruefeVorgangsunterbrechungHerunterfallen(
			ArrayList<ResEinheit> resEinheitFuerNeuesTeilpaket) {

		ArrayList<ResEinheit> result = new ArrayList<ResEinheit>();

		Collections.sort(resEinheitFuerNeuesTeilpaket, new ComperatorVektor2iX());

		int xPos = resEinheitFuerNeuesTeilpaket.get(0).getPosition().getxKoordinate();
		int xPosZuPruefen = 0;

		for (ResEinheit resEinheit : resEinheitFuerNeuesTeilpaket) {

			xPosZuPruefen = resEinheit.getPosition().getxKoordinate();

			if (xPos == xPosZuPruefen) {
				result.add(resEinheit);
			} else if (xPos + 1 == xPosZuPruefen) {
				xPos = xPosZuPruefen;
				result.add(resEinheit);
			} else {
				break;
			}

		}

		for (ResEinheit zuEntfernen : result) {
			resEinheitFuerNeuesTeilpaket.remove(zuEntfernen);
		}

		if (!resEinheitFuerNeuesTeilpaket.isEmpty()) {
			Collections.sort(resEinheitFuerNeuesTeilpaket, new ComperatorVektor2iY());
		}

		Collections.sort(result, new ComperatorVektor2iY());

		return result;
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

		for (int xPos = x; xPos <= x + tmp.getVorgangsdauer() - 1; xPos++) {
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

		if (minAbstand != 0) {
			tmp.bewegeY(this, -minAbstand);
			for (Teilpaket teilpaket : zuVerschiebenListe) {
				herunterfallen(teilpaket);
			}
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

		Collections.sort(resEinheitFuerNeuesTeilpaket, new ComperatorVektor2iY());
	}

	/**
	 * Wenn im Koordinatensystem Lücken in der untersten Ebene zwischen Teilpaketen
	 * sind, werden diese durch das verschieben der Teilpakete geschlossen.
	 */
	public void aufschliessen() {
		boolean untersteReiheLeer = false;
		int laengeLuecke = 0;

		for (int x = 0; x < ResCanvas.koorBreite; x++) {
			ResEinheit untersteReihe = koordinatenSystem[ResCanvas.koorHoehe - 1][x];

			if (untersteReihe == null) {
				untersteReiheLeer = true;
				laengeLuecke++;
			}

			if (untersteReiheLeer && untersteReihe != null) {

				Teilpaket verschiebe = untersteReihe.getTeilpaket();
				verschiebe.bewegeX(this, -laengeLuecke);
				this.herunterfallenAlleTeilpakete();

				Algorithmus.ausgeben(koordinatenSystem);

				untersteReiheLeer = false;
				laengeLuecke = 0;
				x = untersteReihe.getTeilpaket().getResEinheitListe().get(0).getPosition().getxKoordinate()
						+ untersteReihe.getVorgangsdauer();
			}

		}
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

	public ResEinheit[][] getKoordinatenSystem() {
		return koordinatenSystem;
	}

	public void setKoordinatenSystem(ResEinheit[][] koordinatenSystem) {
		this.koordinatenSystem = koordinatenSystem;
	}

	public ArrayList<Arbeitspaket> getArbeitspaketListe() {
		return arbeitspaketListe;
	}

	public void setArbeitspaketListe(ArrayList<Arbeitspaket> arbeitspaketListe) {
		this.arbeitspaketListe = arbeitspaketListe;
	}

	/**
	 * Erstellt einen Historieneintrag des aktuellen Koordinatenssystems. Dies
	 * findet dabei nur statt, wenn tatsälich eine Änderung zum voherigen
	 * gespeichterten Historieeintrag festgestellt wurde.
	 */
	public void aktuallisiereHistorie() {

		this.neueHistorie();

		if (pruefeHistorienAenderung(this.historienArbeitspaketListe, this.arbeitspaketListe)) {
			Algorithmus.ausgeben(koordinatenSystem);
		}

	}

	/**
	 * Prüft ob der neu einzufügende Historieneintrag sich von dem voherigen
	 * gespeichterten Historieeintrag unterscheidet. Wenn nicht, wird der neue
	 * Eintrag nicht gespeichtert.
	 *
	 * @param historienArbeitspaketListe
	 * @param aktuelleArbeitspaketListe
	 * @return
	 */
	private boolean pruefeHistorienAenderung(ArrayList<Arbeitspaket> historienArbeitspaketListe,
			ArrayList<Arbeitspaket> aktuelleArbeitspaketListe) {

		for (int i = 0; i < historienArbeitspaketListe.size(); i++) {
			Arbeitspaket apHistorie = historienArbeitspaketListe.get(i);
			Arbeitspaket apAktuell = aktuelleArbeitspaketListe.get(i);

			ArrayList<Teilpaket> tpListeHistorie = apHistorie.getTeilpaketListe();
			ArrayList<Teilpaket> tpListeAktuell = apAktuell.getTeilpaketListe();

			if (tpListeHistorie.size() != tpListeAktuell.size()) {
				return false;
			}

			for (int a = 0; a < tpListeHistorie.size(); a++) {

				ArrayList<ResEinheit> resListeHistorie = tpListeHistorie.get(a).getResEinheitListe();
				ArrayList<ResEinheit> resListeAktuell = tpListeAktuell.get(a).getResEinheitListe();

				for (int b = 0; b < resListeHistorie.size(); b++) {
					int xPosHistorie = resListeHistorie.get(b).getPosition().getxKoordinate();
					int yPosHistorie = resListeHistorie.get(b).getPosition().getyKoordinate();

					int xPosAktuell = resListeAktuell.get(b).getPosition().getxKoordinate();
					int yPosAktuell = resListeAktuell.get(b).getPosition().getyKoordinate();

					if (xPosHistorie != xPosAktuell || yPosHistorie != yPosAktuell) {
						return false;
					}

				}

			}

		}
		return true;

	}

	/**
	 * Erstellt den neuen Historieneintrag. Dabei handelt sich um eine Kopie des
	 * aktuellen Koordinatensystems inklusive der Pakete.
	 *
	 */
	private void neueHistorie() {

		ArrayList<Arbeitspaket> neueArbeitspaketListe = copyArbeitspaketliste();

		this.historienArbeitspaketListe = neueArbeitspaketListe;

		ResEinheit[][] neuesKoordinatenSystem = copyKoordinatenSystem(neueArbeitspaketListe);

		this.historieKoordinatenSystem.add(neuesKoordinatenSystem);

	}

	/**
	 * @param neueArbeitspaketListe
	 * @return
	 */
	private ResEinheit[][] copyKoordinatenSystem(ArrayList<Arbeitspaket> neueArbeitspaketListe) {
		ResEinheit[][] neuesKoordinatenSystem = new ResEinheit[koorHoehe][koorBreite];

		for (Arbeitspaket ap : neueArbeitspaketListe) {
			for (Teilpaket tp : ap.getTeilpaketListe()) {
				for (ResEinheit re : tp.getResEinheitListe()) {
					neuesKoordinatenSystem[re.getPosition().getyKoordinate()][re.getPosition().getxKoordinate()] = re;
				}
			}
		}
		return neuesKoordinatenSystem;
	}

	/**
	 * @return
	 */
	private ArrayList<Arbeitspaket> copyArbeitspaketliste() {
		ArrayList<Arbeitspaket> neueArbeitspaketListe = new ArrayList<Arbeitspaket>();

		for (Arbeitspaket ap : this.arbeitspaketListe) {
			neueArbeitspaketListe.add(ap.copy());
		}
		return neueArbeitspaketListe;
	}

	public ArrayList<ResEinheit[][]> getHistorieKoordinatenSystem() {
		return historieKoordinatenSystem;
	}

	private void setHistorieKoordinatenSystem(ArrayList<ResEinheit[][]> historieKoordinatenSystem) {
		this.historieKoordinatenSystem = historieKoordinatenSystem;
	}

	// TODO vermutlich löschen
	// /**
	// * Erstellt eine Kopie des aktuellen KoordinatenSystems.
	// *
	// * @return
	// */
	// public ResEinheit[][] kloneKoordinatenSystem() {
	//
	// ArrayList<Arbeitspaket> neueArbeitspaketListe = this.copyArbeitspaketliste();
	//
	// return copyKoordinatenSystem(neueArbeitspaketListe);
	//
	// }

	private ArrayList<Arbeitspaket> getHistorienArbeitspaketListe() {
		return historienArbeitspaketListe;
	}

	private void setHistorienArbeitspaketListe(ArrayList<Arbeitspaket> historienArbeitspaketListe) {
		this.historienArbeitspaketListe = historienArbeitspaketListe;
	}

	/**
	 * Erstellt eine Kopie des ResCanvas
	 *
	 * @return
	 */
	public ResCanvas copyResCanvas() {

		ResCanvas kopieResCanvas = new ResCanvas();

		kopieResCanvas.setArbeitspaketListe(this.copyArbeitspaketliste());
		kopieResCanvas.setKoordinatenSystem(this.copyKoordinatenSystem(kopieResCanvas.getArbeitspaketListe()));
		kopieResCanvas.setHistorieKoordinatenSystem(historieKoordinatenSystem);
		kopieResCanvas.setHistorienArbeitspaketListe(historienArbeitspaketListe);

		return kopieResCanvas;
	}

	/**
	 * Erstellt eine Kopie des ResCanvas. Das übergebene Arbeitspaket wird dabei
	 * nicht in die neue Kopie des Koordinatensystems übernommen.
	 *
	 * @param nichtKopierenOrginal
	 * @return
	 */
	public ResCanvas copyResCanvas(Arbeitspaket nichtKopierenOrginal) {
		ResCanvas kopieResCanvas = new ResCanvas();

		kopieResCanvas.setArbeitspaketListe(this.copyArbeitspaketliste());

		ArrayList<Arbeitspaket> kopieArbeitspaketListe = kopieResCanvas.getArbeitspaketListe();
		Arbeitspaket nichtKopierenKopie = null;

		for (Arbeitspaket ap : kopieResCanvas.getArbeitspaketListe()) {
			if (ap.getId() == nichtKopierenOrginal.getId()) {
				nichtKopierenKopie = ap;
				break;
			}
		}

		kopieArbeitspaketListe.remove(nichtKopierenKopie);

		kopieResCanvas.setKoordinatenSystem(this.copyKoordinatenSystem(kopieArbeitspaketListe));

		kopieArbeitspaketListe.add(nichtKopierenKopie);

		kopieResCanvas.setHistorieKoordinatenSystem(historieKoordinatenSystem);
		kopieResCanvas.setHistorienArbeitspaketListe(historienArbeitspaketListe);

		return kopieResCanvas;
	}

	/**
	 * Ermittelt die Anzahl anzahl an Stellen, welche eins unter der Obergrenze
	 * liegen.
	 *
	 * @return
	 */
	public int ermittleStellen(Arbeitspaket arbeitspaket, int mitarbeiterObergrenze) {

		int stellen = 0;

		for (Teilpaket teilpaket : arbeitspaket.getTeilpaketListe()) {
			ResEinheit ersteResEinheit = teilpaket.getResEinheitListe().get(0);
			int abstand = mitarbeiterObergrenze + ersteResEinheit.getPosition().getyKoordinate()
					- teilpaket.getMitarbeiteranzahl();
			stellen += (abstand * teilpaket.getVorgangsdauer());

		}
		return stellen;
	}

	/**
	 * Austausch der Attribute des aktuellen ResCanvas mit einem veränderten
	 * ResCanvas.
	 *
	 * Muss aufgerufen werden, da siehe https://stackoverflow.com/a/11699969
	 *
	 * What gets passed to a method is a copy of the reference of the object. So, no
	 * matter how many times you re-assign the references, the original reference
	 * will not be affected.
	 *
	 * @param resCanvas
	 */
	public void swap(ResCanvas resCanvas) {
		this.setArbeitspaketListe(resCanvas.getArbeitspaketListe());
		this.setHistorieKoordinatenSystem(resCanvas.getHistorieKoordinatenSystem());
		this.setKoordinatenSystem(resCanvas.getKoordinatenSystem());
		this.setHistorienArbeitspaketListe(resCanvas.getHistorienArbeitspaketListe());
	}

}