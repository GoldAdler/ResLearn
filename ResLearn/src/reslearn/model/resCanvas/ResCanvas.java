package reslearn.model.resCanvas;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;

import reslearn.model.algorithmus.Algorithmus;
import reslearn.model.paket.Arbeitspaket;
import reslearn.model.paket.ResEinheit;
import reslearn.model.paket.Teilpaket;
import reslearn.model.utils.ComperatorVektor2iY;
import reslearn.model.utils.Vektor2i;

public class ResCanvas {

	private ArrayList<Arbeitspaket> arbeitspaketListe;
	private ArrayList<Arbeitspaket> historienArbeitspaketListe;
	private ResEinheit[][] koordinatenSystem;
	// TODO Historie der sich ver�nderten koordiantenSysteme anlegen
	// dient daf�r im L�sungsmodus die einzelnen Schritte anzuzeigen
	private ArrayList<ResEinheit[][]> historieKoordinatenSystem;
	public static final int koorHoehe = 28;
	public static final int koorBreite = 43;
	private int maxMitarbeiter = koorHoehe - 1;

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
	 * gel�scht, sondern wei� immer noch die Position
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
	 * gel�scht, sondern wei� immer noch die Position
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
	 * Das �bergebene Teilpaket wird soweit nach unten gesenkt, wie m�glich. Bei
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
		// Teilpaket besteht w�rde,
		// muss kein neues Teilpaket angelegt werden. Dies liegt daran, da das alte und
		// das
		// neue Teilpaket identisch w�hren.
		Teilpaket tmp = teilpaket;
		if (altesTeilpaketResEinheiten.size() != resEinheitFuerNeuesTeilpaket.size()
				&& !resEinheitFuerNeuesTeilpaket.isEmpty()) {
			// TODO: LUKAS SEI GEPRIESSEN NICHT �NDERN
			tmp = teilpaket.trenneTeilpaketVertikal(resEinheitFuerNeuesTeilpaket);

		}

		if (!tmp.getResEinheitListe().isEmpty()) {
			ResEinheit ausgangspunkt = tmp.getResEinheitListe().get(0);

			boolean kollision = falle(tmp, ausgangspunkt, zuVerschiebenListe);

			if (kollision) {

				Algorithmus.ausgeben(koordinatenSystem);

				this.herunterfallen(tmp);
			}
		}
	}

	/**
	 * Ermittlung des Mindestabstands zwischen dem aktuellen Teilpaket und ersten
	 * eintreffenden Kollision. Anhand des ermittelten Mindestabstands wird das
	 * aktuelle Teilpaket und die dar�berliegenden Teilpakete nach unten verschoben.
	 * Gibt es mehrere unterschidliche Kollisionen wird der R�ckgabeparameter als
	 * true zur�ckgegeben.
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
				// teilpaket.bewegen(this, -minAbstand, 0);
				herunterfallen(teilpaket);
			}
		}

		return kollision;
	}

	/**
	 * F�r jede Reseinheit im teilpaket wird gepr�ft, ob unter der resEinheit ein
	 * Feld frei ist, wenn ja wird dieses in die Liste resEinheitFuerNeuesTeilpaket
	 * aufgenommen. Teilpakete, die �ber dem aktuellen Teilpaket liegen, werden in
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
	 * Wenn im Koordinatensystem L�cken in der untersten Ebene zwischen Teilpaketen
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

	public int getMaxMitarbeiter() {
		return maxMitarbeiter;
	}

	public void setMaxMitarbeiter(int maxMitarbeiter) {
		this.maxMitarbeiter = maxMitarbeiter;
	}

	public ArrayList<Arbeitspaket> getArbeitspaketListe() {
		return arbeitspaketListe;
	}

	public void setArbeitspaketListe(ArrayList<Arbeitspaket> arbeitspaketListe) {
		this.arbeitspaketListe = arbeitspaketListe;
	}

	public void aktuallisiereHistorie() {

		// this.neueHistorie();
		//
		// if (pruefeHistorienAenderung(this.historienArbeitspaketListe,
		// this.arbeitspaketListe)) {
		// Algorithmus.ausgeben(koordinatenSystem);
		// }

	}

	private boolean pruefeHistorienAenderung(ArrayList<Arbeitspaket> historienArbeitspaketListe,
			ArrayList<Arbeitspaket> aktuelleArbeitspaketListe) {

		for (int i = 0; i <= historienArbeitspaketListe.size(); i++) {
			Arbeitspaket apHistorie = historienArbeitspaketListe.get(i);
			Arbeitspaket apAktuell = aktuelleArbeitspaketListe.get(i);

			ArrayList<Teilpaket> tpListeHistorie = apHistorie.getTeilpaketListe();
			ArrayList<Teilpaket> tpListeAktuell = apAktuell.getTeilpaketListe();

			if (tpListeHistorie.size() != tpListeAktuell.size()) {
				return false;
			}

			for (int a = 0; a <= tpListeHistorie.size(); a++) {

				ArrayList<ResEinheit> resListeHistorie = tpListeHistorie.get(a).getResEinheitListe();
				ArrayList<ResEinheit> resListeAktuell = tpListeAktuell.get(a).getResEinheitListe();

				for (int b = 0; b <= resListeHistorie.size(); b++) {
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

	private void neueHistorie() {

		ResEinheit[][] neuesKoordinatenSystem = new ResEinheit[koorHoehe][koorBreite];

		ArrayList<Arbeitspaket> neueArbeitspaketListe = new ArrayList<Arbeitspaket>();

		for (Arbeitspaket ap : this.arbeitspaketListe) {
			neueArbeitspaketListe.add(ap.copy());
		}

		for (Arbeitspaket ap : neueArbeitspaketListe) {
			for (Teilpaket tp : ap.getTeilpaketListe()) {
				for (ResEinheit re : tp.getResEinheitListe()) {
					koordinatenSystem[re.getPosition().getyKoordinate()][re.getPosition().getyKoordinate()] = re;
				}
			}
		}

		this.historieKoordinatenSystem.add(neuesKoordinatenSystem);
		this.historienArbeitspaketListe = neueArbeitspaketListe;

	}

}