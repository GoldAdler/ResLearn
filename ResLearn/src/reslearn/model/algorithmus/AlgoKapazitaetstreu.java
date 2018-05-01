package reslearn.model.algorithmus;

import java.util.LinkedList;
import java.util.Stack;

import reslearn.main.Main;
import reslearn.model.paket.Arbeitspaket;
import reslearn.model.paket.ResEinheit;
import reslearn.model.paket.Teilpaket;
import reslearn.model.paket.Vektor2i;
import reslearn.model.resCanvas.ResCanvas;

public class AlgoKapazitaetstreu extends Algorithmus {

	private static AlgoKapazitaetstreu algoKapazitaetstreu;

	// TODO: Vorläufige Integer. Wieder löschen!!!!
	private static int maxBegrenzung = 5;

	private AlgoKapazitaetstreu() {
	}

	public static AlgoKapazitaetstreu getInstance() {
		if (algoKapazitaetstreu == null) {
			algoKapazitaetstreu = new AlgoKapazitaetstreu();
		}
		return algoKapazitaetstreu;
	}

	// TODO: Hier weiter machen ihr lappen
	// Kompletter Algorithmus nochmal durchgehen um herauszufinden
	// - warum E nicht in die Liste unterhalbUndRechts aufgenommen wird
	// - Zeile 71 warum wird hier gelöscht
	// - Irgendwas stimmt nicht mit D, was ist hier HILFE

	@Override
	public ResEinheit[][] algoDurchfuehren(ResCanvas resCanvas) {

		ResEinheit[][] koordinatenSystem = AlgoErsteSchritt.getInstance().algoDurchfuehren(resCanvas);
		resCanvas.herunterfallenAlleTeilpakete();
		LinkedList<Teilpaket> teilpaketListe = ueberpruefeObergrenze(resCanvas, koordinatenSystem);
		Stack<Teilpaket> unterhalbUndRechts = new Stack<Teilpaket>();
		Vektor2i position = null;
		ResEinheit unterhalb;
		ResEinheit oberhalb;

		for (Teilpaket teilpaket : teilpaketListe) {
			LOOP: for (ResEinheit res : teilpaket.getResEinheitListe()) {

				position = res.getPosition();
				int x = position.getxKoordinate();
				for (int y = position.getyKoordinate() + 1; y < ResCanvas.koorHoehe; y++) {
					unterhalb = koordinatenSystem[y][x];

					// wenn unterhalb der aktuellen resEinheit nichts liegt,
					// wir die Schleife abgebrochen und die nächste resEinheit betrachtet
					if (unterhalb == null) {
						break;
					}
					// wenn unterhalb der aktuellen resEinheit eine Reseinheit liegt, die
					// vom selben Teilpaket ist, wird die Betrachtung des aktuellen Teilpaketes
					// abgebrochen
					// die variable position wird auf die vorhergegane ResEinheit gesetzt
					if (teilpaket == unterhalb.getTeilpaket()) {
						int index = teilpaket.getResEinheitListe().indexOf(res);
						position = teilpaket.getResEinheitListe().get(index - 1).getPosition();
						break LOOP;
					}

					if (!unterhalbUndRechts.contains(unterhalb.getTeilpaket())) {
						unterhalbUndRechts.add(unterhalb.getTeilpaket());
					}
				}
			}

			// Unterhalb und rechts von dem aktuellen Teilpaket wird geprüft, ob andere
			// Teilpakete
			// darunterligen
			// Wenn ja, werden sie unterhalbUndRechts hinzugefügt
			for (int x = position.getxKoordinate() + 1; x < ResCanvas.koorBreite; x++) {
				for (int y = 0; y < ResCanvas.koorHoehe; y++) {

					oberhalb = koordinatenSystem[y][x];
					if (oberhalb != null && !unterhalbUndRechts.contains(oberhalb.getTeilpaket())) {
						unterhalbUndRechts.add(oberhalb.getTeilpaket());
					}
				}
			}

			// prüfen ob, in unterhalbUndRechts ein Teilpaket liegt, das zum selben
			// Arbeitspaket des aktuellen Teilpaketes gehöhrt
			boolean zusammenfuehren = false;
			Teilpaket zusammenzufuerhen = null;
			for (Teilpaket tp : unterhalbUndRechts) {
				if (tp.getArbeitspaket() == teilpaket.getArbeitspaket()) {
					zusammenzufuerhen = tp;
					zusammenfuehren = true;
				}
			}

			int xMove = teilpaket.getVorgangsdauer();

			if (zusammenfuehren) {

				// Wenn in unterhalbUndRechts ein Teilpaket(TpB) liegt, das zum selben
				// Arbeitspaket
				// des aktuellen Teilpaketes(TpA) gehöhrt,
				// dann darf das Teilpaket(TpB), das direkt unter dem aktuellen Teilpaket(TpA)
				// liegt, nicht
				// verschoben werden.
				// Sondern es müssen die Teilpakete verschoben werden, die recht von (TpB)
				// liegen
				// verschoben werden.
				// Danach müssen TpA und TpB nebeneinander gelegt und wieder vereint werden.

				while (unterhalbUndRechts.size() > 1) {
					Teilpaket tp = unterhalbUndRechts.pop();
					tp.bewegen(resCanvas, 0, xMove);

					// TODO: löschen
					Main.ausgeben(koordinatenSystem);

					if (tp.getArbeitspaket() == teilpaket.getArbeitspaket()) {
						// Wir berechen die neue Position des Teilpaketes(TpA)
						// indem:
						// 1. Berechnen der Position vor TpB (neuer Starpunkt von TpA)
						// 2. Berechnen der Differenz von Startpunt(alt) von TpA und neuer Startpunkt
						// 3. Verschieben
						Vektor2i vektor2i = zusammenzufuerhen.getResEinheitListe().get(0).getPosition();
						vektor2i.add(new Vektor2i(0, -xMove));

						Vektor2i altePostion = teilpaket.getResEinheitListe().get(0).getPosition();
						vektor2i.subtract(new Vektor2i(-altePostion.getyKoordinate(), altePostion.getxKoordinate()));

						teilpaket.bewegen(resCanvas, -vektor2i.getyKoordinate(), vektor2i.getxKoordinate());

						// TODO: löschen
						Main.ausgeben(koordinatenSystem);

						resCanvas.herunterfallenAlleTeilpakete();

						teilpaket.zusammenfuehren(tp);

						// TODO: löschen
						Main.ausgeben(koordinatenSystem);

					}

				}

			} else {
				// ansonsten muss das Teilpaket(TpB), das unterhalb des aktuellen
				// Teilpaketes(TpA) liegt,
				// verschoben werden.
				while (!unterhalbUndRechts.isEmpty()) {
					Teilpaket tp = unterhalbUndRechts.pop();
					tp.bewegen(resCanvas, 0, xMove);

					// TODO: löschen
					Main.ausgeben(koordinatenSystem);
				}
			}

			resCanvas.herunterfallen(teilpaket);

			// TODO: löschen
			Main.ausgeben(koordinatenSystem);

		}

		for (Arbeitspaket ap : resCanvas.getAktuellerZustand().getArbeitspaketListe()) {
			for (Teilpaket tp : ap.getTeilpaketListe()) {
				int verschieben = tp.ueberpruefeZeiten();

			}
		}

		return koordinatenSystem;
	}

	/**
	 * Überprüft ob Teilpakete über der Maximalgrenze an parallel arbeitenden
	 * Mitarbeitern liegen. Wenn ja, wer diesen in eine LinkList gespeichtert.
	 *
	 * @param resCanvas
	 * @param koordinatenSystem
	 * @return teilpaketListe
	 */
	private LinkedList<Teilpaket> ueberpruefeObergrenze(ResCanvas resCanvas, ResEinheit[][] koordinatenSystem) {
		LinkedList<Teilpaket> teilpaketListe = new LinkedList<Teilpaket>();
		ResEinheit tempResEinheit;
		for (int x = 0; x < ResCanvas.koorBreite; x++) {

			// maxBegrenzung nicht -1, weil Paket innerhalb der Begrenzung noch valide ist!
			tempResEinheit = koordinatenSystem[ResCanvas.koorHoehe - maxBegrenzung - 1][x];
			if (tempResEinheit != null && !teilpaketListe.contains(tempResEinheit.getTeilpaket())) {
				teilpaketListe.add(tempResEinheit.getTeilpaket());
			}
		}

		return teilpaketListe;
	}

}
