package reslearn.model.algorithmus;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.Stack;

import reslearn.model.paket.Arbeitspaket;
import reslearn.model.paket.ResEinheit;
import reslearn.model.paket.Teilpaket;
import reslearn.model.resCanvas.ResCanvas;
import reslearn.model.utils.ComperatorArbeitspaket;
import reslearn.model.utils.ComperatorTeilpaket;
import reslearn.model.utils.ComperatorVektor2iX;
import reslearn.model.utils.ComperatorVektor2iY;
import reslearn.model.utils.Vektor2i;

public class AlgoKapazitaetstreu extends Algorithmus {

	private static AlgoKapazitaetstreu algoKapazitaetstreu;
	private boolean vorgangsdauerVeraenderbar = false;

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
	public ResCanvas algoDurchfuehren(ResCanvas resCanvas) {

		// Durchführen des AlgoErsteSchritt und anschließendes herabsenken aller in der
		// Luft stehenden ResEinheiten.
		// Damit wir die Ausgangssituation des AlgoKapazitaetstreu geschaffen.
		ResEinheit[][] koordinatenSystem = AlgoErsteSchritt.getInstance().algoDurchfuehren(resCanvas)
				.getKoordinatenSystem();

		resCanvas.herunterfallenAlleTeilpakete();

		kapazitaetsOptimierung(resCanvas, koordinatenSystem);

		zeitValidierung(resCanvas);

		return resCanvas;
	}

	/**
	 * Diese Methode überpüft für jedes Arbeitspaket ob die zugehörigen Teilpakete
	 * innerhalb der zeitlichen Rahmenbedingen (FAZ - SEZ) liegen. Wenn die
	 * Rahmenbedingung verletzt worden ist, versucht die Methode die Pakete zu
	 * verschieben, damit die Position gültig ist. Ist dies nicht möglich, kann die
	 * Aufgabe nicht gelöst werden und der Algorithmus wird abgebrochen.
	 *
	 * @param resCanvas
	 */
	private void zeitValidierung(ResCanvas resCanvas) {

		// TODO Überarbeiten wenn mit Klutke geklärt
		// verschieben unterhalb und rechts blabla
		/*
		 * Teilpakete von links nach rechts durchgegehen und Zeiten überprüfen. Wenn
		 * Zeiten nicht passen wird zu diesem Arbeitspaket alle Teilpakete gelöscht und
		 * danach wieder um die differenz eingesetzt. Herunterfallen ausführen und
		 * prüfen ob maximum verletzt. wenn maximum verlezt wird, prüfen wir die
		 * teilpakete unterhalb und rechts, ob diese noch verschoben werden können, wenn
		 * nicht müssen wir vertikal schneiden.
		 */

		Collections.sort(resCanvas.getArbeitspaketListe(), new ComperatorArbeitspaket());
		for (Arbeitspaket ap : resCanvas.getArbeitspaketListe()) {
			ap.zusammenfuegen();
		}

		for (Arbeitspaket ap : resCanvas.getArbeitspaketListe()) {
			ap.ueberpruefeVorgangsunterbrechung(resCanvas);

		}
		Collections.sort(resCanvas.getArbeitspaketListe(), new ComperatorArbeitspaket());
		ausgeben(resCanvas.getKoordinatenSystem());

		for (Arbeitspaket ap : resCanvas.getArbeitspaketListe()) {
			ap.zusammenfuegen();
			resCanvas.aufschliessen();

			ArrayList<Teilpaket> tpListe = ap.getTeilpaketListe();
			Teilpaket letztesTeilpaket = tpListe.get(tpListe.size() - 1);
			int verschieben = letztesTeilpaket.ueberpruefeZeiten();
			if (verschieben != 0) {

				verschiebeZeitueberschreitendePakete(resCanvas, ap, verschieben);

			}

			// ausgeben(resCanvas.getKoordinatenSystem());
			resCanvas.aktuallisiereHistorie();
			// zeitOptimieren(resCanvas, ap, tpListe, letztesTeilpaket);

		}

	}

	/**
	 * Überprüfe ob Arbeitspaket nach links (Richtung FAZ) verschoben werden kann.
	 * Wenn ja werden ResEinheiten vom Ende des Arbeitspaketes genommen und nach
	 * vorne gesetzt.
	 *
	 * @param resCanvas
	 * @param ap
	 * @param tpListe
	 * @param letztesTeilpaket
	 */
	private void zeitOptimieren(ResCanvas resCanvas, Arbeitspaket ap, ArrayList<Teilpaket> tpListe,
			Teilpaket letztesTeilpaket) {

		Teilpaket erstesTeilpaket = tpListe.get(0);
		ResEinheit ersteResEinheit = erstesTeilpaket.getResEinheitListe().get(0);
		boolean gesetzt = false;
		int xPos = ersteResEinheit.getPosition().getxKoordinate();

		ArrayList<ResEinheit> zuSetzendeResEinheiten = letztesTeilpaket.getResEinheitListe();
		ArrayList<ResEinheit> gesetzteResEinheiten;
		ResEinheit[][] koordinatenSystem = resCanvas.getKoordinatenSystem();
		for (int x = xPos - 1; x >= ap.getFaz(); xPos--) {

			gesetzt = false;

			Collections.sort(letztesTeilpaket.getResEinheitListe(), new ComperatorVektor2iX());

			gesetzteResEinheiten = new ArrayList<ResEinheit>();

			int grenze = ResCanvas.koorHoehe - maxBegrenzung - 1;

			for (int y = ResCanvas.koorHoehe - 1; y >= grenze; y--) {

				if (koordinatenSystem[y][x] == null) {
					int indexLetzteRes = zuSetzendeResEinheiten.size() - 1;
					for (int i = y; i > grenze; i--) {
						if (gesetzteResEinheiten.size() == zuSetzendeResEinheiten.size()) {

							/*
							 * ############## WICHTIG !!!!! ##################### Dieser Code Block ist
							 * nahezu identisch mit dem Block, der weiter unten ebenfalls mit WICHTIG
							 * markiert wurde. Der einzige unterschied besteht darin, dass im oberen Block,
							 * also diesem, das Teilpaket letztesTeilpaket aus dem Arbeitspaket entfernt
							 * wird.
							 *
							 * Wenn an diesem Block etwas verändert wird, muss ebenso der untere Block mit
							 * der gleichen Veränderungen versehen werden!!!!!
							 *
							 * Erklärung: hieraus lässt sich leider keien Methode extrahieren. Dem Finder
							 * einer besseren Lösung bieten wir einen Download-Gutschein für ein Glässle
							 * Bärlauch Pesto. Der Finder meldet sich bitte bei willburger@hotmail.de.####
							 * Der Lukas schwimmt in dem Zeug.
							 *
							 */
							// #################### ANFANG BLOCK #######################
							Collections.sort(gesetzteResEinheiten, new ComperatorVektor2iY());
							Collections.sort(zuSetzendeResEinheiten, new ComperatorVektor2iY());
							letztesTeilpaket.trenneTeilpaketHorizontal(gesetzteResEinheiten);

							ap.entferneTeilpaket(letztesTeilpaket);

							Collections.sort(tpListe, new ComperatorTeilpaket());
							letztesTeilpaket = tpListe.get(tpListe.size() - 1);
							zuSetzendeResEinheiten = letztesTeilpaket.getResEinheitListe();
							Collections.sort(zuSetzendeResEinheiten, new ComperatorVektor2iX());
							gesetzteResEinheiten = new ArrayList<ResEinheit>();

							// ################### ENDE BLOCK #######################

							indexLetzteRes = zuSetzendeResEinheiten.size() - 1;
						}
						ResEinheit zuSetzen = zuSetzendeResEinheiten.get(indexLetzteRes--);
						koordinatenSystem[zuSetzen.getPosition().getyKoordinate()][zuSetzen.getPosition()
								.getxKoordinate()] = null;
						koordinatenSystem[i][x] = zuSetzen;
						zuSetzen.setPosition(new Vektor2i(i, x));
						gesetzteResEinheiten.add(zuSetzen);

						ausgeben(resCanvas.getKoordinatenSystem());

						gesetzt = true;

					}

					/*
					 * ############## WICHTIG !!!!! ############################ Dieser Code Block
					 * ist nahezu identisch mit dem Block, der weiter oben ebenfalls mit WICHTIG
					 * markiert wurde. Der einzige unterschied besteht darin, dass im oberen Block
					 * das Teilpaket letztesTeilpaket aus dem Arbeitspaket entfernt wird.
					 *
					 * Wenn an diesem Block etwas verändert wird, muss ebenso der obere Block mit
					 * der gleichen Veränderungen versehen werden!!!!!
					 *
					 */
					// #################### ANFANG BLOCK #######################
					Collections.sort(gesetzteResEinheiten, new ComperatorVektor2iY());
					Collections.sort(zuSetzendeResEinheiten, new ComperatorVektor2iY());
					letztesTeilpaket.trenneTeilpaketVertikal(gesetzteResEinheiten);
					Collections.sort(tpListe, new ComperatorTeilpaket());
					letztesTeilpaket = tpListe.get(tpListe.size() - 1);
					zuSetzendeResEinheiten = letztesTeilpaket.getResEinheitListe();
					Collections.sort(zuSetzendeResEinheiten, new ComperatorVektor2iX());
					gesetzteResEinheiten = new ArrayList<ResEinheit>();

					// ################### ENDE BLOCK #######################

					break;
				}
			}
			if (!gesetzt) {
				break;
			}
		}
	}

	/**
	 * Wenn Vorgangsdauer veränderbar: Die Pakete bei dennen die Zeitvaldierung
	 * verstöße feststellt werden gelöscht und um die entsprechende Differenz wieder
	 * eingefügt. Falls ResEinheiten die Obergrenze überscheiten werden diese
	 * entsprechend nach Links oder nach Rechts verschoben.
	 *
	 * Wenn Vorgangsdauer nicht veränderbar:
	 *
	 * @param resCanvas
	 * @param ap
	 * @param verschiebenRechts
	 */
	private void verschiebeZeitueberschreitendePakete(ResCanvas resCanvas, Arbeitspaket ap, int verschieben) {

		// TODO vermutlich löschen
		// boolean durchfuehren = this.vorgangsdauerVeraenderbar;
		// if (!durchfuehren) {
		// TODO berechnen ob verschiebbar
		// wenn verschiebbar, dann durchführen = true;
		// #verschiebenLinks und Rechts werden dann auch nicht mehr aufgerufen
		// for (int x = ap.getFaz() - 1; x <= ap.getSez() - 1; x++) {
		// int counter = 0;
		// for (int y = ResCanvas.koorHoehe - 1; y > (ResCanvas.koorHoehe -
		// maxBegrenzung - 1); y--) {
		//
		// ResEinheit[][] koordinatenSystem = resCanvas.getKoordinatenSystem();
		// if (koordinatenSystem[y][x] != null) {
		// counter++;
		// } else {
		// if (counter + ap.getMitarbeiteranzahl() <= maxBegrenzung) {
		//
		// }
		// }
		//
		// }
		//
		// }
		// }

		if (vorgangsdauerVeraenderbar) {

			// TODO LOGIK e-Problem

			resCanvas.entferneArbeitspaket(ap);
			resCanvas.herunterfallenAlleTeilpakete();

			ap.neuSetzen(verschieben, resCanvas);
			// resCanvas.aufschliessen();
			ausgeben(resCanvas.getKoordinatenSystem());

			Teilpaket neuesTeilpaket = ueberpruefeObergrenzeResEinheit(resCanvas, resCanvas.getKoordinatenSystem());

			if (neuesTeilpaket != null) {

				int grenze = ResCanvas.koorHoehe - maxBegrenzung - 1;

				ArrayList<ResEinheit> zuSetzendeResEinheiten = neuesTeilpaket.getResEinheitListe();
				ResEinheit[][] koordinatenSystem = resCanvas.getKoordinatenSystem();

				verschiebeLinks(resCanvas, ap, neuesTeilpaket, grenze, zuSetzendeResEinheiten, koordinatenSystem);

				verschiebeRechts(resCanvas, ap, neuesTeilpaket, grenze, zuSetzendeResEinheiten, koordinatenSystem);
			}
		} else {

			if (verschieben < 0) {
				// Paket ist zu spät | SEZ verletzt

				/*
				 * Erklärung des folgenden Algorithmus
				 *
				 * D ist zu spät. Das heißt D muss nach Möglichkeit nach links verschoben
				 * werden. Ist dies nicht möglich. Belasse D wo es ist.
				 *
				 * ...........................................
				 * .........ZZZ.........DDDDD.................
				 * AAAAAA...ZZZ.........DDDDD.................
				 * AAAAAABBBBBBB........DDDDDEEEEEE...........
				 * AAAAAABBBBBBBCCCCCCCCDDDDDEEEEEE...........
				 * AAAAAABBBBBBBCCCCCCCCDDDDDEEEEEE...........
				 *
				 * 1. Kopie des ResCanvas anlegen (Ausgangssituation)
				 *
				 * 2. Simulation starten: D rauslöschen
				 *
				 * In eigenen Kopien wird versucht
				 *
				 * D von FAZ bis SEZ versuchen einzufügen und obergrenze prüfen. Wenn obergrenze
				 * inordnung wird eine Kopie der Simulation einer Liste hinzugefügt. Ist die
				 * Simulation beendet, müssen diese Kopien danach überprüft werden, ob die
				 * Position eine möglichst gerine Anzahl an parallen Arbeiten Mitarbeitern
				 * gewährleistet.
				 *
				 * Wird keine Möglichkeit gefunden, D einzufügen, wird mit der Kopie der
				 * Ausgangssituation weitergearbeitet. Mit anderen Worten D wird dort belassen,
				 * wo es ursprünglich lag.
				 *
				 */

				// ResCanvas ausgangssituation = resCanvas.copyResCanvas();

				ResCanvas simulation = null;
				ArrayList<ResCanvas> listeSimulationen = new ArrayList<ResCanvas>();
				Arbeitspaket zuVerschiebenAp = null;

				for (int x = ap.getFaz() - 1; x < ap.getSez(); x++) {

					simulation = resCanvas.copyResCanvas();

					for (Arbeitspaket simAp : simulation.getArbeitspaketListe()) {
						if (simAp.getId() == ap.getId()) {
							zuVerschiebenAp = simAp;
						}
					}

					simulation.entferneArbeitspaket(zuVerschiebenAp);
					int xStart = zuVerschiebenAp.getTeilpaketListe().get(0).getResEinheitListe().get(0).getPosition()
							.getxKoordinate();

					Algorithmus.ausgeben(simulation.getKoordinatenSystem());

					simulation.herunterfallenAlleTeilpakete();

					int abstand = x - xStart;
					zuVerschiebenAp.neuSetzen(abstand, simulation);

					Algorithmus.ausgeben(simulation.getKoordinatenSystem());

					if (ueberpruefeObergrenzeUeberschritten(simulation)) {
						listeSimulationen.add(simulation);
					}

				}

				if (listeSimulationen.isEmpty()) {
					// Wenn das Arbeitspaket nicht verschoben werden kann, wird ohne Veränderung mit
					// aktuellen Koordinatensystem des ResCanvas weitergearbeitet.

					// resCanvas = ausgangssituation;
				} else if (listeSimulationen.size() == 1) {
					resCanvas = listeSimulationen.get(0);
				} else {

					int maxStellen = Integer.MIN_VALUE;
					ResCanvas optimalesResCanvas = null;

					for (ResCanvas resCanvasSimulation : listeSimulationen) {
						int stellen = resCanvasSimulation.ermittleStellen(zuVerschiebenAp,
								AlgoKapazitaetstreu.maxBegrenzung);
						if (stellen > maxStellen) {
							maxStellen = stellen;
							optimalesResCanvas = resCanvasSimulation;
						}
					}

					resCanvas = optimalesResCanvas;

				}

			} else if (verschieben > 0) {
				// Paket ist zu früh | FAZ verletzt
			}

			/*
			 * -----------------------------------------------------------------------------
			 *
			 *
			 * -----------------------------------------------------------------------------
			 * Z ist zu früh. Das heißt Z muss nach rechts verschoben werden.
			 *
			 * 1. Szenario
			 *
			 * ..............CC......DDDDD................
			 * AAAAAA........CC......DDDDD................
			 * AAAAAA...BBBBBBB......DDDDDEEEEEE..........
			 * AAAAAAZZZBBBBBBBCCCCCCDDDDDEEEEEE..........
			 * AAAAAAZZZBBBBBBBCCCCCCDDDDDEEEEEE..........
			 *
			 * unter Z liegt nichts. Das heißt die Pakete die direkt rechts neben Z
			 * liegeben, in diesem Beispiel B, müssen um die Vorgangsdauer von Z verschoben
			 * werden. Herunterfallen. Simulation startet, arbeitet bei jedem Versuch mit
			 * unterschiedlichen Kopien. Z von FAZ bis SEZ versuchen einzufügen und
			 * obergrenze prüfen. Wenn obergrenze inordnung wird eine Kopie der Simulation
			 * einer Liste hinzugefügt. Ist die Simulation beendet, müssen diese Kopien
			 * danach überprüft werden, ob die Position eine möglichst gerine Anzahl an
			 * parallen Arbeiten Mitarbeitern gewährleistet. Mit dem optimalsten Ergebnis
			 * wird anschließend weitergearbeitet.
			 *
			 * 2. Szenario
			 *
			 * ......ZZZ|.CC......DDDDD................
			 * AAAAAAZZZ|.CC......DDDDD................
			 * AAAAAABBBBBBB......DDDDDEEEEEE..........
			 * AAAAAABBBBBBBCCCCCCDDDDDEEEEEE..........
			 * AAAAAABBBBBBBCCCCCCDDDDDEEEEEE..........
			 *
			 * | = eine leere Lücke, dient nur der erklärung im Folgetext
			 *
			 * unter Z liegt bereits etwas. Das Paket das unter Z liegt, darf nicht
			 * verschoben werden. Aber um zu gewährleisten, dass Z so früh wie möglich und
			 * so optimal wie möglich, das heißt eine möglichst gerine Anzahl an parallel
			 * arbeitend Mitarbeitern ereicht ist, müssen trotzdem Pakete nach rechts
			 * verschoben werden. Hirzu werden, beginnend dirket neben dem Paket Z ( im Bild
			 * gekennzeichnet mit |), alle Pakete die ungleich den Paketen sind, die direkt
			 * unter Z und B sind, in eine Liste aufgenommen. Die aufgennomen Pakete werden
			 * umd die Vorgansdauer von Z nach rechts verschoben. Im Anschluss beginnt die
			 * Simulation. Siehe Szenario 1.
			 *
			 *
			 */

		}

	}

	/**
	 * Verschiebt nach Links wenn möglich.
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

				Collections.sort(ap.getTeilpaketListe(), new ComperatorTeilpaket());

				gesetzt = setzeResEinheitenNeu(resCanvas, ap, neuesTeilpaket, grenze, gesezteResEinheiten,
						zuSetzendeResEinheiten, koordinatenSystem, x, gesetzt);

				if (!gesetzt) {
					break;
				}
			}
		}
	}

	/**
	 * Verschiebt nach Rechts wenn möglich.
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

			Collections.sort(ap.getTeilpaketListe(), new ComperatorTeilpaket());
			Collections.sort(zuSetzendeResEinheiten, new ComperatorVektor2iY());

			Teilpaket tp = ap.getTeilpaketListe().get(ap.getTeilpaketListe().size() - 1);
			int xPosRechts = tp.getResEinheitListe().get(tp.getVorgangsdauer() - 1).getPosition().getxKoordinate();

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
	 * zurückgegeben.
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

				Collections.sort(zuSetzendeResEinheiten, new ComperatorVektor2iX());

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

					ausgeben(resCanvas.getKoordinatenSystem());

				}

				Collections.sort(zuSetzendeResEinheiten, new ComperatorVektor2iY());

				neuesTeilpaket.trenneVariabel(gesezteResEinheiten);

				gesetzt = true;
				break;
			}
		}
		return gesetzt;
	}

	/**
	 * Diese Methode sorgt dafür, dass alle Teilpakete unterhalb der Begrenzung, die
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

			ausgeben(koordinatenSystem);

			resCanvas.aufschliessen();

			ausgeben(koordinatenSystem);
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

				ausgeben(koordinatenSystem);
			}

			while (!unterhalbStack.isEmpty()) {
				Teilpaket tp = unterhalbStack.pop();
				tp.bewegen(resCanvas, 0, xMove);

				ausgeben(koordinatenSystem);
			}

			resCanvas.herunterfallen(teilpaket);
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

				ausgeben(koordinatenSystem);

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

					ausgeben(koordinatenSystem);

					// TODO: nice to have
					// swappen der Teilpakete vor dem Herunterfallen,
					// damit schönere Aufteilung in der Gui zu sehen ist
					// ccc ||||ddd
					// dddccc |cccccc
					//

					resCanvas.herunterfallenAlleTeilpakete();

					teilpaket.zusammenfuehren(tp);

					ausgeben(koordinatenSystem);
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
	 * Mitarbeitern liegen. Wenn ja, werden diese in eine LinkList gespeichtert.
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
	 * Ueberprüft ob es ResEinheiten gibt, die über der Mitarbeitergrenze liegen.
	 * Wenn ja werden diese der grenzeUeberschrittenListe hinzugefügt. Die
	 * hinzugefügten ResEinheiten können dabei nur von einem einzigen Teilpaket
	 * stammen. Aus den ResEinheiten wird das neue Teilpaket erstellt und
	 * zurückgegeben.
	 *
	 * @param resCanvas
	 * @param koordinatenSystem
	 * @return
	 */
	private Teilpaket ueberpruefeObergrenzeResEinheit(ResCanvas resCanvas, ResEinheit[][] koordinatenSystem) {
		ArrayList<ResEinheit> grenzeUeberschrittenListe = new ArrayList<ResEinheit>();
		ResEinheit tempResEinheit;
		Teilpaket neuesTeilpaket = null;
		for (int x = 0; x < ResCanvas.koorBreite; x++) {

			// maxBegrenzung nicht -1, weil Paket innerhalb der Begrenzung noch valide ist!
			// deswegen maxBegrenzung -2
			tempResEinheit = koordinatenSystem[ResCanvas.koorHoehe - maxBegrenzung - 1][x];
			if (tempResEinheit != null) {
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
					.trenneTeilpaketHorizontal(grenzeUeberschrittenListe);

		}

		return neuesTeilpaket;
	}

	private boolean ueberpruefeObergrenzeUeberschritten(ResCanvas resCanvas) {
		boolean ueberschritten = false;

		ResEinheit[][] koordinatenSystem = resCanvas.getKoordinatenSystem();

		ResEinheit tempResEinheit;

		for (int x = 0; x < ResCanvas.koorBreite; x++) {

			// maxBegrenzung nicht -1, weil Paket innerhalb der Begrenzung noch valide ist!
			// deswegen maxBegrenzung -2
			tempResEinheit = koordinatenSystem[ResCanvas.koorHoehe - maxBegrenzung - 1][x];
			if (tempResEinheit != null) {
				ueberschritten = true;
				break;
			}
		}

		return ueberschritten;
	}

}
