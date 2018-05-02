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

	@Override
	public ResEinheit[][] algoDurchfuehren(ResCanvas resCanvas) {

		// Durchführen des AlgoErsteSchritt und anschließendes herabsenken aller in der
		// Luft stehenden ResEinheiten.
		// Damit wir die Ausgangssituation des AlgoKapazitaetstreu geschaffen.
		ResEinheit[][] koordinatenSystem = AlgoErsteSchritt.getInstance().algoDurchfuehren(resCanvas);
		resCanvas.herunterfallenAlleTeilpakete();

		kapazitaetsOptimierung(resCanvas, koordinatenSystem);

		zeitValidierung(resCanvas);

		return koordinatenSystem;
	}

	/**
	 * Diese Methode überpüft für jedes Arbeitspaket ob die zugehörigen Teilpakete
	 * innerhalb der zeitlichen Rahmenbedingen (FAZ - SEZ) liegen. Wenn die
	 * Rahmenbedingung verletzt worden ist, versucht die Methode die Pakete zu
	 * verschieben, damit die Position gültig ist. Ist dies nicht möglich, kann die
	 * Aufgabe nicht gelöst werden und der Algorithmsu wird abgebrochen.
	 *
	 * @param resCanvas
	 */
	private void zeitValidierung(ResCanvas resCanvas) {

		// TODO HIER weiter machen ihr Lappos
		/*
		 * Bevor hier weiter!!!!! Zuerst die Testfälle alle durchgehen
		 *
		 * WICHTIG: Testdaten auf Korrektheit überprüfen Aktuell sind die meisten Falsch
		 * Wenn die nicht korrekt sind fliegen Exceptions ohne Ende
		 *
		 * Teilpakete von links nach rechts durchgegehen und Zeiten überprüfen. Wenn
		 * Zeiten nicht passen wird zu diesem Arbeitspaket alle Teilpakete gelöscht und
		 * danach wieder um die differenz eingesetzt. Herunterfallen ausführen und
		 * prüfen ob maximum verletzt. wenn maximum verlezt wird, dann ist der algo
		 * wahrscheinlich nicht lösbar
		 *
		 *
		 *
		 *
		 *
		 *
		 */

		for (Arbeitspaket ap : resCanvas.getAktuellerZustand().getArbeitspaketListe()) {
			for (Teilpaket tp : ap.getTeilpaketListe()) {
				int verschieben = tp.ueberpruefeZeiten();

			}
		}
	}

	/**
	 * Diese Methode sorgt dafür, dass alle Teilpakete unterhalb der Begrenzung, die
	 * durch die maximale Anzahl an Mitarbeitern definiert wird, liegen.
	 *
	 * @param resCanvas
	 * @param koordinatenSystem
	 */
	private void kapazitaetsOptimierung(ResCanvas resCanvas, ResEinheit[][] koordinatenSystem) {

		LinkedList<Teilpaket> teilpaketListe = ueberpruefeObergrenze(resCanvas, koordinatenSystem);
		Stack<Teilpaket> unterhalbStack = new Stack<Teilpaket>();
		Stack<Teilpaket> rechtsVonunterhalbStack = new Stack<Teilpaket>();
		Vektor2i position = null;

		for (Teilpaket teilpaket : teilpaketListe) {

			position = sucheDarunterLiegendePakete(koordinatenSystem, unterhalbStack, position, teilpaket);

			sucheUntenRechts(koordinatenSystem, rechtsVonunterhalbStack, position);

			verschieben(resCanvas, koordinatenSystem, unterhalbStack, rechtsVonunterhalbStack, teilpaket);

			resCanvas.herunterfallen(teilpaket);

			// TODO: löschen
			Main.ausgeben(koordinatenSystem);

			resCanvas.aufschliessen();

			// TODO: löschen
			Main.ausgeben(koordinatenSystem);
		}
	}

	/**
	 * Diese Methode ordnet die Teilpakete im Koordinatensystem neu an, sodass die
	 * Begrenzung, die durch die maximale Anzahl an Mitarbeitern definiert wird,
	 * nicht überschritten wird.
	 *
	 * @param resCanvas
	 * @param koordinatenSystem
	 * @param unterhalbStack
	 * @param rechtsVonunterhalbStack
	 * @param teilpaket
	 */
	private void verschieben(ResCanvas resCanvas, ResEinheit[][] koordinatenSystem, Stack<Teilpaket> unterhalbStack,
			Stack<Teilpaket> rechtsVonunterhalbStack, Teilpaket teilpaket) {

		// prüfen ob, in unterhalbUndRechts ein Teilpaket liegt, das zum selben
		// Arbeitspaket des aktuellen Teilpaketes gehöhrt
		boolean zusammenfuehren = false;
		Teilpaket zusammenzufuerhen = null;
		for (Teilpaket tp : rechtsVonunterhalbStack) {
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

			verschiebenUndZusammenfuehren(resCanvas, koordinatenSystem, unterhalbStack, rechtsVonunterhalbStack,
					teilpaket, zusammenzufuerhen, xMove);

		} else {
			// ansonsten muss das Teilpaket(TpB), das unterhalb des aktuellen
			// Teilpaketes(TpA) liegt,
			// verschoben werden.

			while (!rechtsVonunterhalbStack.isEmpty()) {
				Teilpaket tp = rechtsVonunterhalbStack.pop();
				tp.bewegen(resCanvas, 0, xMove);

				// TODO: löschen
				Main.ausgeben(koordinatenSystem);
			}

			while (!unterhalbStack.isEmpty()) {
				Teilpaket tp = unterhalbStack.pop();
				tp.bewegen(resCanvas, 0, xMove);

				// TODO: löschen
				Main.ausgeben(koordinatenSystem);
			}
		}
	}

	/**
	 * Diese Methode verschiebt die Teilpakete im Koordinatensystem und fühgt sie
	 * gegbenfalls wieder zusammen, falls sie nebeinanderliegen.
	 *
	 * @param resCanvas
	 * @param koordinatenSystem
	 * @param unterhalbStack
	 * @param rechtsVonunterhalbStack
	 * @param teilpaket
	 * @param zusammenzufuerhen
	 * @param xMove
	 */
	private void verschiebenUndZusammenfuehren(ResCanvas resCanvas, ResEinheit[][] koordinatenSystem,
			Stack<Teilpaket> unterhalbStack, Stack<Teilpaket> rechtsVonunterhalbStack, Teilpaket teilpaket,
			Teilpaket zusammenzufuerhen, int xMove) {

		while (!rechtsVonunterhalbStack.isEmpty()) {
			Teilpaket tp = rechtsVonunterhalbStack.pop();

			boolean verschieben = true;
			for (Teilpaket tpt : unterhalbStack) {
				if (tpt.getArbeitspaket() == tp.getArbeitspaket()) {
					verschieben = false;
				}
			}

			if (verschieben) {
				tp.bewegeX(resCanvas, xMove);

				// TODO: löschen
				Main.ausgeben(koordinatenSystem);

			}

			if (tp.getArbeitspaket() == teilpaket.getArbeitspaket()) {
				// Wir berechen die neue Position des Teilpaketes(TpA)
				// indem:
				// 1. Berechnen der Position vor TpB (neuer Starpunkt von TpA)
				// 2. Berechnen der Differenz von Startpunt(alt) von TpA und neuer Startpunkt
				// 3. Verschieben
				Vektor2i vektor2i = zusammenzufuerhen.getResEinheitListe().get(0).getPosition();
				Vektor2i neuePosition = new Vektor2i(vektor2i.getyKoordinate(), vektor2i.getxKoordinate());
				neuePosition.add(new Vektor2i(0, -xMove));

				if (koordinatenSystem[neuePosition.getyKoordinate()][neuePosition.getxKoordinate()] == null) {
					Vektor2i altePostion = teilpaket.getResEinheitListe().get(0).getPosition();
					Vektor2i differenz = new Vektor2i(neuePosition, altePostion, Vektor2i.Methode.SUBTRACT);

					teilpaket.bewegen(resCanvas, -differenz.getyKoordinate(), differenz.getxKoordinate());

					// TODO: löschen
					Main.ausgeben(koordinatenSystem);

					// TODO: nice to have
					// swappen der Teilpakete vor dem Herunterfallen,
					// damit schönere Aufteilung in der Gui zu sehen ist
					// ccc ||||ddd
					// dddccc |cccccc
					//

					resCanvas.herunterfallenAlleTeilpakete();

					teilpaket.zusammenfuehren(tp);

					// TODO: löschen
					Main.ausgeben(koordinatenSystem);
				}

			}

		}
	}

	/**
	 * Methode sucht ob rechts von den Teilpaketen, die unter dem aktuellen
	 * Teilpaket liegen (Teilpakete im Stack unterhalbStack) , weitere Teilpakete
	 * sind. Wenn ja werde diese dem Stack rechtsVonUnterhalbStack hinzugefügt.
	 *
	 * @param koordinatenSystem
	 * @param rechtsVonUnterhalbStack
	 * @param position
	 */
	private void sucheUntenRechts(ResEinheit[][] koordinatenSystem, Stack<Teilpaket> rechtsVonUnterhalbStack,
			Vektor2i position) {

		ResEinheit oberhalb;

		for (int x = position.getxKoordinate() + 1; x < ResCanvas.koorBreite; x++) {
			for (int y = 0; y < ResCanvas.koorHoehe; y++) {

				oberhalb = koordinatenSystem[y][x];
				if (oberhalb != null && !rechtsVonUnterhalbStack.contains(oberhalb.getTeilpaket())) {
					rechtsVonUnterhalbStack.add(oberhalb.getTeilpaket());
				}
			}
		}
	}

	/**
	 * Methode sucht unter dem aktuellen Teilpaket, ob andere Teilpakete direkt
	 * darunter liegen. Wenn ja, werden sie in dem Stack unterhalbStack gespeichert.
	 *
	 * @param koordinatenSystem
	 * @param unterhalbStack
	 * @param position
	 * @param teilpaket
	 */
	private Vektor2i sucheDarunterLiegendePakete(ResEinheit[][] koordinatenSystem, Stack<Teilpaket> unterhalbStack,
			Vektor2i position, Teilpaket teilpaket) {

		ResEinheit unterhalb;

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

				if (!unterhalbStack.contains(unterhalb.getTeilpaket())) {
					unterhalbStack.add(unterhalb.getTeilpaket());
				}
			}
		}
		return position;
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
