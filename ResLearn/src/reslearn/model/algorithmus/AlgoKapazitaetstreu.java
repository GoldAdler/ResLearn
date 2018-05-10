package reslearn.model.algorithmus;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.Stack;

import reslearn.main.Main;
import reslearn.model.paket.Arbeitspaket;
import reslearn.model.paket.ResEinheit;
import reslearn.model.paket.Teilpaket;
import reslearn.model.resCanvas.ResCanvas;
import reslearn.model.utils.ComperatorArbeitspaket;
import reslearn.model.utils.Vektor2i;

public class AlgoKapazitaetstreu extends Algorithmus {

	private static AlgoKapazitaetstreu algoKapazitaetstreu;

	// TODO: Vorl�ufige Integer. Wieder l�schen!!!!
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

		// Durchf�hren des AlgoErsteSchritt und anschlie�endes herabsenken aller in der
		// Luft stehenden ResEinheiten.
		// Damit wir die Ausgangssituation des AlgoKapazitaetstreu geschaffen.
		ResEinheit[][] koordinatenSystem = AlgoErsteSchritt.getInstance().algoDurchfuehren(resCanvas);
		resCanvas.herunterfallenAlleTeilpakete();

		kapazitaetsOptimierung(resCanvas, koordinatenSystem);

		zeitValidierung(resCanvas);

		return koordinatenSystem;
	}

	/**
	 * Diese Methode �berp�ft f�r jedes Arbeitspaket ob die zugeh�rigen Teilpakete
	 * innerhalb der zeitlichen Rahmenbedingen (FAZ - SEZ) liegen. Wenn die
	 * Rahmenbedingung verletzt worden ist, versucht die Methode die Pakete zu
	 * verschieben, damit die Position g�ltig ist. Ist dies nicht m�glich, kann die
	 * Aufgabe nicht gel�st werden und der Algorithmus wird abgebrochen.
	 *
	 * @param resCanvas
	 */
	private void zeitValidierung(ResCanvas resCanvas) {

		// TODO �berarbeiten wenn mit Klutke gekl�rt
		// verschieben unterhalb und rechts blabla
		/*
		 * Teilpakete von links nach rechts durchgegehen und Zeiten �berpr�fen. Wenn
		 * Zeiten nicht passen wird zu diesem Arbeitspaket alle Teilpakete gel�scht und
		 * danach wieder um die differenz eingesetzt. Herunterfallen ausf�hren und
		 * pr�fen ob maximum verletzt. wenn maximum verlezt wird, pr�fen wir die
		 * teilpakete unterhalb und rechts, ob diese noch verschoben werden k�nnen, wenn
		 * nicht m�ssen wir vertikal schneiden.
		 */

		Collections.sort(resCanvas.getArbeitspaketListe(), new ComperatorArbeitspaket());

		for (Arbeitspaket ap : resCanvas.getArbeitspaketListe()) {
			resCanvas.aufschliessen();
			for (Teilpaket tp : ap.getTeilpaketListe()) {
				int verschieben = tp.ueberpruefeZeiten();
				if (verschieben != 0) {

					resCanvas.entferneArbeitspaket(ap);
					resCanvas.herunterfallenAlleTeilpakete();
					ap.neuSetzen(verschieben, resCanvas);

					// TODO: l�schen
					Main.ausgeben(resCanvas.getKoordinatenSystem());

					Teilpaket neuesTeilpaket = ueberpruefeObergrenzeResEinheit(resCanvas,
							resCanvas.getKoordinatenSystem());

					if (neuesTeilpaket != null) {

						int grenze = ResCanvas.koorHoehe - maxBegrenzung - 1;

						ArrayList<ResEinheit> zuSetzendeResEinheiten = neuesTeilpaket.getResEinheitListe();
						ResEinheit[][] koordinatenSystem = resCanvas.getKoordinatenSystem();

						verschiebeLinks(resCanvas, ap, neuesTeilpaket, grenze, zuSetzendeResEinheiten,
								koordinatenSystem);

						verschiebeRechts(resCanvas, ap, neuesTeilpaket, grenze, zuSetzendeResEinheiten,
								koordinatenSystem);
					}

					break;
				}
			}

		}

	}

	/**
	 * Verschiebt nach Links wenn m�glich.
	 *
	 * @param resCanvas
	 * @param ap
	 * @param neuesTeilpaket
	 * @param grenze
	 * @param zuSetzendeResEinheiten
	 * @param koordinatenSystem
	 */
	private void verschiebeLinks(ResCanvas resCanvas, Arbeitspaket ap, Teilpaket neuesTeilpaket, int grenze,
			ArrayList<ResEinheit> zuSetzendeResEinheiten, ResEinheit[][] koordinatenSystem) {
		ArrayList<ResEinheit> gesezteResEinheiten;

		if (!zuSetzendeResEinheiten.isEmpty()) {

			int xPosLinks = zuSetzendeResEinheiten.get(0).getPosition().getxKoordinate();
			for (int x = xPosLinks - 1; x >= ap.getFaz(); x--) {

				boolean gesetzt = false;
				gesezteResEinheiten = new ArrayList<ResEinheit>();

				if (zuSetzendeResEinheiten.isEmpty()) {
					ap.entferneTeilpaket(neuesTeilpaket);
					break;
				}

				gesetzt = setzeResEinheitenNeu(resCanvas, ap, neuesTeilpaket, grenze, gesezteResEinheiten,
						zuSetzendeResEinheiten, koordinatenSystem, x, gesetzt);

				if (!gesetzt) {
					break;
				}
			}
		}
	}

	/**
	 * Verschiebt nach Rechts wenn m�glich.
	 *
	 * @param resCanvas
	 * @param ap
	 * @param neuesTeilpaket
	 * @param grenze
	 * @param zuSetzendeResEinheiten
	 * @param koordinatenSystem
	 */
	private void verschiebeRechts(ResCanvas resCanvas, Arbeitspaket ap, Teilpaket neuesTeilpaket, int grenze,
			ArrayList<ResEinheit> zuSetzendeResEinheiten, ResEinheit[][] koordinatenSystem) {
		ArrayList<ResEinheit> gesezteResEinheiten;

		if (!zuSetzendeResEinheiten.isEmpty()) {

			int xPosRechts = zuSetzendeResEinheiten.get(neuesTeilpaket.getVorgangsdauer() - 1).getPosition()
					.getxKoordinate();

			for (int x = xPosRechts + 1; x <= ResCanvas.koorBreite; x++) {

				boolean gesetzt = false;
				gesezteResEinheiten = new ArrayList<ResEinheit>();

				if (zuSetzendeResEinheiten.isEmpty()) {
					ap.entferneTeilpaket(neuesTeilpaket);
					break;
				}

				gesetzt = setzeResEinheitenNeu(resCanvas, ap, neuesTeilpaket, grenze, gesezteResEinheiten,
						zuSetzendeResEinheiten, koordinatenSystem, x, gesetzt);

				if (!gesetzt) {
					break;
				}
			}
		}
	}

	/**
	 * Setzt die ResEinheiten in der ArrayList zuSetzendeResEinheiten neu ins
	 * Koordinatensystem um der Zeitanforderung des Arbeitspaktes zu entsprechen.
	 * Wenn ResEinheiten neu gesetzt werden konnten wird der booleanwert true
	 * zur�ckgegeben.
	 *
	 * @param resCanvas
	 * @param ap
	 * @param neuesTeilpaket
	 * @param grenze
	 * @param gesezteResEinheiten
	 * @param zuSetzendeResEinheiten
	 * @param koordinatenSystem
	 * @param x
	 * @param gesetzt
	 * @return
	 */
	private boolean setzeResEinheitenNeu(ResCanvas resCanvas, Arbeitspaket ap, Teilpaket neuesTeilpaket, int grenze,
			ArrayList<ResEinheit> gesezteResEinheiten, ArrayList<ResEinheit> zuSetzendeResEinheiten,
			ResEinheit[][] koordinatenSystem, int x, boolean gesetzt) {

		int gleicheResEinheiten = 0;

		for (int y = ResCanvas.koorHoehe - 1; y >= grenze; y--) {

			if (koordinatenSystem[y][x] != null) {

				Arbeitspaket aptmp = koordinatenSystem[y][x].getTeilpaket().getArbeitspaket();

				if (ap == aptmp) {
					gleicheResEinheiten++;
				}

			} else if (koordinatenSystem[y][x] == null) {

				for (int i = y; i > grenze; i--) {
					if (gesezteResEinheiten.size() < zuSetzendeResEinheiten.size()
							&& gleicheResEinheiten < ap.getMitarbeiteranzahl()) {
						ResEinheit zuSetzen = zuSetzendeResEinheiten.get(gesezteResEinheiten.size());
						koordinatenSystem[zuSetzen.getPosition().getyKoordinate()][zuSetzen.getPosition()
								.getxKoordinate()] = null;
						koordinatenSystem[i][x] = zuSetzen;
						zuSetzen.setPosition(new Vektor2i(i, x));
						gesezteResEinheiten.add(zuSetzen);
						gleicheResEinheiten++;
					} else {
						break;
					}

					// TODO: l�schen
					Main.ausgeben(resCanvas.getKoordinatenSystem());

				}

				neuesTeilpaket.trenneTeilpaketVertikal(gesezteResEinheiten, 1);

				gesetzt = true;
				break;
			}
		}
		return gesetzt;
	}

	/**
	 * Diese Methode sorgt daf�r, dass alle Teilpakete unterhalb der Begrenzung, die
	 * durch die maximale Anzahl an Mitarbeitern definiert wird, liegen.
	 *
	 * @param resCanvas
	 * @param koordinatenSystem
	 */
	private void kapazitaetsOptimierung(ResCanvas resCanvas, ResEinheit[][] koordinatenSystem) {

		LinkedList<Teilpaket> teilpaketListe = ueberpruefeObergrenzeTeilpaket(resCanvas, koordinatenSystem);
		Stack<Teilpaket> unterhalbStack = new Stack<Teilpaket>();
		Stack<Teilpaket> rechtsVonUnterhalbStack = new Stack<Teilpaket>();
		Vektor2i position = null;

		for (Teilpaket teilpaket : teilpaketListe) {

			position = sucheDarunterLiegendePakete(koordinatenSystem, unterhalbStack, position, teilpaket);

			sucheUntenRechts(koordinatenSystem, rechtsVonUnterhalbStack, unterhalbStack, position);

			verschieben(resCanvas, koordinatenSystem, unterhalbStack, rechtsVonUnterhalbStack, teilpaket);

			resCanvas.herunterfallen(teilpaket);

			// TODO: l�schen
			Main.ausgeben(koordinatenSystem);

			resCanvas.aufschliessen();

			// TODO: l�schen
			Main.ausgeben(koordinatenSystem);
		}
	}

	/**
	 * Diese Methode ordnet die Teilpakete im Koordinatensystem neu an, sodass die
	 * Begrenzung, die durch die maximale Anzahl an Mitarbeitern definiert wird,
	 * nicht �berschritten wird.
	 *
	 * @param resCanvas
	 * @param koordinatenSystem
	 * @param unterhalbStack
	 * @param rechtsVonunterhalbStack
	 * @param teilpaket
	 */
	private void verschieben(ResCanvas resCanvas, ResEinheit[][] koordinatenSystem, Stack<Teilpaket> unterhalbStack,
			Stack<Teilpaket> rechtsVonunterhalbStack, Teilpaket teilpaket) {

		// pr�fen ob, in unterhalbUndRechts ein Teilpaket liegt, das zum selben
		// Arbeitspaket des aktuellen Teilpaketes geh�hrt
		Teilpaket zusammenzufuerhen = null;
		for (Teilpaket tp : rechtsVonunterhalbStack) {
			if (tp.getArbeitspaket() == teilpaket.getArbeitspaket()) {
				zusammenzufuerhen = tp;
			}
		}

		int xFrueheste = Integer.MAX_VALUE;
		for (Teilpaket tp : unterhalbStack) {

			int xTmp = tp.getResEinheitListe().get(0).getPosition().getxKoordinate();

			if (xTmp < xFrueheste) {
				xFrueheste = xTmp;
			}
		}

		int abstand = teilpaket.getResEinheitListe().get(0).getPosition().getxKoordinate() - xFrueheste;
		int xMove = teilpaket.getVorgangsdauer() + abstand;

		if (zusammenzufuerhen != null) {

			// Wenn in unterhalbUndRechts ein Teilpaket(TpB) liegt, das zum selben
			// Arbeitspaket
			// des aktuellen Teilpaketes(TpA) geh�hrt,
			// dann darf das Teilpaket(TpB), das direkt unter dem aktuellen Teilpaket(TpA)
			// liegt, nicht
			// verschoben werden.
			// Sondern es m�ssen die Teilpakete verschoben werden, die recht von (TpB)
			// liegen
			// verschoben werden.
			// Danach m�ssen TpA und TpB nebeneinander gelegt und wieder vereint werden.

			verschiebenUndZusammenfuehren(resCanvas, koordinatenSystem, unterhalbStack, rechtsVonunterhalbStack,
					teilpaket, zusammenzufuerhen, xMove);

		} else {
			// ansonsten muss das Teilpaket(TpB), das unterhalb des aktuellen
			// Teilpaketes(TpA) liegt,
			// verschoben werden.

			while (!rechtsVonunterhalbStack.isEmpty()) {
				Teilpaket tp = rechtsVonunterhalbStack.pop();
				tp.bewegen(resCanvas, 0, xMove);

				// TODO: l�schen
				Main.ausgeben(koordinatenSystem);
			}

			while (!unterhalbStack.isEmpty()) {
				Teilpaket tp = unterhalbStack.pop();
				tp.bewegen(resCanvas, 0, xMove);

				// TODO: l�schen
				Main.ausgeben(koordinatenSystem);
			}

			resCanvas.herunterfallen(teilpaket);
		}
	}

	/**
	 * Diese Methode verschiebt die Teilpakete im Koordinatensystem und f�hgt sie
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

				// TODO: l�schen
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

					// TODO: l�schen
					Main.ausgeben(koordinatenSystem);

					// TODO: nice to have
					// swappen der Teilpakete vor dem Herunterfallen,
					// damit sch�nere Aufteilung in der Gui zu sehen ist
					// ccc ||||ddd
					// dddccc |cccccc
					//

					resCanvas.herunterfallenAlleTeilpakete();

					teilpaket.zusammenfuehren(tp);

					// TODO: l�schen
					Main.ausgeben(koordinatenSystem);
				}

			}

		}
	}

	/**
	 * Methode sucht ob rechts von den Teilpaketen, die unter dem aktuellen
	 * Teilpaket liegen (Teilpakete im Stack unterhalbStack) , weitere Teilpakete
	 * sind. Wenn ja werde diese dem Stack rechtsVonUnterhalbStack hinzugef�gt.
	 *
	 * @param koordinatenSystem
	 * @param rechtsVonUnterhalbStack
	 * @param position
	 */
	private void sucheUntenRechts(ResEinheit[][] koordinatenSystem, Stack<Teilpaket> rechtsVonUnterhalbStack,
			Stack<Teilpaket> unterhalbStack, Vektor2i position) {

		ResEinheit oberhalb;

		for (int x = position.getxKoordinate() + 1; x < ResCanvas.koorBreite; x++) {
			for (int y = 0; y < ResCanvas.koorHoehe; y++) {

				oberhalb = koordinatenSystem[y][x];
				if (oberhalb != null && !rechtsVonUnterhalbStack.contains(oberhalb.getTeilpaket())
						&& !unterhalbStack.contains(oberhalb.getTeilpaket())) {
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
				// wir die Schleife abgebrochen und die n�chste resEinheit betrachtet
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
	 * �berpr�ft ob Teilpakete �ber der Maximalgrenze an parallel arbeitenden
	 * Mitarbeitern liegen. Wenn ja, wer diesen in eine LinkList gespeichtert.
	 *
	 * @param resCanvas
	 * @param koordinatenSystem
	 * @return teilpaketListe
	 */
	private LinkedList<Teilpaket> ueberpruefeObergrenzeTeilpaket(ResCanvas resCanvas,
			ResEinheit[][] koordinatenSystem) {
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

	/**
	 * Ueberpr�ft ob es ResEinheiten gibt, die �ber der Mitarbeitergrenze liegen.
	 * Wenn ja werden diese der grenzeUeberschrittenListe hinzugef�gt. Die
	 * hinzugef�gten ResEinheiten k�nnen dabei nur von einem einzigen Teilpaket
	 * stammen. Aus den ResEinheiten wird das neue Teilpaket erstellt und
	 * zur�ckgegeben.
	 *
	 * @param resCanvas
	 * @param koordinatenSystem
	 * @return
	 */
	private Teilpaket ueberpruefeObergrenzeResEinheit(ResCanvas resCanvas, ResEinheit[][] koordinatenSystem) {
		ArrayList<ResEinheit> grenzeUeberschrittenListe = new ArrayList<ResEinheit>();
		ResEinheit tempResEinheit;
		Teilpaket neuesTeilpaket = null;
		int vorgangsdauer = 0;
		for (int x = 0; x < ResCanvas.koorBreite; x++) {

			// maxBegrenzung nicht -1, weil Paket innerhalb der Begrenzung noch valide ist!
			// deswegen maxBegrenzung -2
			tempResEinheit = koordinatenSystem[ResCanvas.koorHoehe - maxBegrenzung - 1][x];
			if (tempResEinheit != null) {
				vorgangsdauer++;
				grenzeUeberschrittenListe.add(tempResEinheit);

				for (int y = ResCanvas.koorHoehe - maxBegrenzung - 2; y >= 0; y--) {
					if (koordinatenSystem[y][x] != null) {
						grenzeUeberschrittenListe.add(koordinatenSystem[y][x]);
					} else {
						break;
					}
				}
			}
		}
		if (!grenzeUeberschrittenListe.isEmpty()) {
			neuesTeilpaket = grenzeUeberschrittenListe.get(0).getTeilpaket()
					.trenneTeilpaketVertikal(grenzeUeberschrittenListe, vorgangsdauer);
		}

		return neuesTeilpaket;
	}

}
