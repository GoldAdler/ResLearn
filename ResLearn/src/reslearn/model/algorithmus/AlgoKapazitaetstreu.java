package reslearn.model.algorithmus;

import java.util.LinkedList;

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

	@Override
	public ResEinheit[][] algoDurchfuehren(ResCanvas resCanvas) {
		ResEinheit[][] koordinatenSystem = resCanvas.getKoordinatenSystem();
		LinkedList<Teilpaket> teilpaketListe = ueberpruefeObergrenze(resCanvas, koordinatenSystem);
		LinkedList<Teilpaket> unterhalbUndRechts = new LinkedList<Teilpaket>();
		Vektor2i position = null;
		ResEinheit unterhalb;

		for (Teilpaket teilpaket : teilpaketListe) {
			LOOP: for (ResEinheit res : teilpaket.getResEinheitListe()) {

				position = res.getPosition();
				int x = position.getxKoordinate();
				for (int y = position.getyKoordinate(); y > ResCanvas.koorHoehe; y++) {
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

					unterhalbUndRechts.add(unterhalb.getTeilpaket());
					resCanvas.entfernenTeilpaket(unterhalb.getTeilpaket());
				}
			}

			for (int y = position.getyKoordinate() - 1; y > ResCanvas.koorHoehe; y++) {
				for (int x = position.getxKoordinate() + 1; x < ResCanvas.koorBreite; x++) {
					unterhalb = koordinatenSystem[y][x];
					if (unterhalb != null) {
						unterhalbUndRechts.add(unterhalb.getTeilpaket());
						resCanvas.entfernenTeilpaket(unterhalb.getTeilpaket());
					}
				}
			}

		}
		return null;
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
			tempResEinheit = koordinatenSystem[maxBegrenzung][x];
			if (tempResEinheit != null) {
				teilpaketListe.add(koordinatenSystem[maxBegrenzung][x].getTeilpaket());
				resCanvas.entfernenTeilpaket(tempResEinheit.getTeilpaket());
			}
		}

		return teilpaketListe;
	}

}
