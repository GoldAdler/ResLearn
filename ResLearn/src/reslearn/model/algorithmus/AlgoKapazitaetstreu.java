package reslearn.model.algorithmus;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.Stack;

import reslearn.model.paket.Arbeitspaket;
import reslearn.model.paket.ResEinheit;
import reslearn.model.paket.Teilpaket;
import reslearn.model.resCanvas.ResCanvas;
import reslearn.model.utils.ComperatorArbeitspaketLR;
import reslearn.model.utils.ComperatorArbeitspaketRL;
import reslearn.model.utils.ComperatorTeilpaket;
import reslearn.model.utils.ComperatorVektor2iX;
import reslearn.model.utils.ComperatorVektor2iY;
import reslearn.model.utils.Vektor2i;

// TODO 1 KLUTKE ERZ�HLEN
// Siehe Kinder UNI maxBegrenzung 2
// ist nicht l�sbar, da kompllet neuer Algo n�tig w�hre
// TODO Fange Fehler ab, wenn ein AP gr��er ist als maxBegrenzung

public class AlgoKapazitaetstreu extends Algorithmus {

	private static AlgoKapazitaetstreu algoKapazitaetstreu;
	private boolean vorgangsdauerVeraenderbar = false;

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
	public ResCanvas algoDurchfuehren(ResCanvas resCanvas) {

		// Durchf�hren des AlgoErsteSchritt und anschlie�endes herabsenken aller in der
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

		Collections.sort(resCanvas.getArbeitspaketListe(), new ComperatorArbeitspaketRL());
		for (Arbeitspaket ap : resCanvas.getArbeitspaketListe()) {
			ap.zusammenfuegen();
		}

		for (Arbeitspaket ap : resCanvas.getArbeitspaketListe()) {
			ap.ueberpruefeVorgangsunterbrechung(resCanvas);

		}
		Collections.sort(resCanvas.getArbeitspaketListe(), new ComperatorArbeitspaketRL());
		ausgeben(resCanvas.getKoordinatenSystem());

		ArrayList<ResCanvas> moeglicheLoesungenResCanvas = new ArrayList<ResCanvas>();

		// TODO Unterscheidung zwishcen Vorgangsunterbrechung durchf�hren

		System.out.println("Simulation start");

		simulationDurchfuehren(resCanvas, moeglicheLoesungenResCanvas, null);

		System.out.println("Des l�uft!");

		// TODO BEWERTUNG HIER DURCHFUEHREN!!!!!

	}

	private void simulationDurchfuehren(ResCanvas resCanvas, ArrayList<ResCanvas> moeglicheLoesungenResCanvas,
			String nichtMehrAnschauenApID) {

		ResCanvas bevorSimulationStartResCanvs = resCanvas.copyResCanvas();

		// hier f�r z simulation aufrufen : man bekommt f�nf ergebnisse / extra Liste
		// ErgebnissevonZListe
		// diese f�nf ergebnisse den meoglichenLoesungen hinzuf�gen

		// ErgebnissevonZListe: entnehmen l�sungen und simuliere f�r jede davon
		// simulationDurchfuehren foreach

		ArrayList<ResCanvas> simLoesungenResCanvas = new ArrayList<ResCanvas>();

		String apID = null;

		ArrayList<Arbeitspaket> arbeitspaketListe = resCanvas.getArbeitspaketListe();

		Collections.sort(arbeitspaketListe, new ComperatorArbeitspaketRL());

		int startAP = 0;
		if (!(nichtMehrAnschauenApID == null)) {
			for (Arbeitspaket simAp : arbeitspaketListe) {
				if (simAp.getId() == nichtMehrAnschauenApID) {
					startAP++;
					break;
				}
				startAP++;
			}
		}

		for (int i = startAP; i < arbeitspaketListe.size(); i++) {
			// for (Arbeitspaket ap : resCanvas.getArbeitspaketListe()) {

			arbeitspaketListe.get(i).zusammenfuegen();
			resCanvas.aufschliessen();

			ArrayList<Teilpaket> tpListe = arbeitspaketListe.get(i).getTeilpaketListe();
			Teilpaket letztesTeilpaket = tpListe.get(tpListe.size() - 1);
			int verschieben = letztesTeilpaket.ueberpruefeZeiten();
			if (verschieben != 0) {
				apID = arbeitspaketListe.get(i).getId();
				System.out.println("Beginn Sim f�r AP: " + arbeitspaketListe.get(i).getId());
				verschiebeZeitueberschreitendePakete(resCanvas, arbeitspaketListe.get(i), verschieben,
						simLoesungenResCanvas);
				break;
			}

			// ausgeben(resCanvas.getKoordinatenSystem());
			// zeitOptimieren(resCanvas, ap, tpListe, letztesTeilpaket);

		}

		if (simLoesungenResCanvas.isEmpty()) {
			moeglicheLoesungenResCanvas.add(bevorSimulationStartResCanvs);
		} else {

			for (ResCanvas simResCanvas : simLoesungenResCanvas) {
				moeglicheLoesungenResCanvas.add(simResCanvas);
			}

			int nummer = 0;
			for (ResCanvas simResCanvas : simLoesungenResCanvas) {
				++nummer;
				System.out.println("Beginn Sim f�r L�sungen von AP : " + apID + " " + nummer + " von "
						+ simLoesungenResCanvas.size());

				simulationDurchfuehren(simResCanvas, moeglicheLoesungenResCanvas, apID);

			}
		}

	}

	/**
	 * �berpr�fe ob Arbeitspaket nach links (Richtung FAZ) verschoben werden kann.
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
							 * Wenn an diesem Block etwas ver�ndert wird, muss ebenso der untere Block mit
							 * der gleichen Ver�nderungen versehen werden!!!!!
							 *
							 * Erkl�rung: hieraus l�sst sich leider keien Methode extrahieren. Dem Finder
							 * einer besseren L�sung bieten wir einen Download-Gutschein f�r ein Gl�ssle
							 * B�rlauch Pesto. Der Finder meldet sich bitte bei willburger@hotmail.de.####
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
					 * Wenn an diesem Block etwas ver�ndert wird, muss ebenso der obere Block mit
					 * der gleichen Ver�nderungen versehen werden!!!!!
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
	 * Wenn Vorgangsdauer ver�nderbar: Die Pakete bei dennen die Zeitvaldierung
	 * verst��e feststellt werden gel�scht und um die entsprechende Differenz wieder
	 * eingef�gt. Falls ResEinheiten die Obergrenze �berscheiten werden diese
	 * entsprechend nach Links oder nach Rechts verschoben.
	 *
	 * Wenn Vorgangsdauer nicht ver�nderbar:
	 *
	 * @param resCanvas
	 * @param ap
	 * @param verschiebenRechts
	 */
	private void verschiebeZeitueberschreitendePakete(ResCanvas resCanvas, Arbeitspaket ap, int verschieben,
			ArrayList<ResCanvas> simLoesungenResCanvas) {

		if (vorgangsdauerVeraenderbar) {

			// TODO LOGIK e-Problem
			// TODO Heraustrennen dieses Bereiches in andere Methode und umstrukturierung

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

			if (verschieben != 0) {

				ResCanvas ausgangssituationResCanvs = resCanvas.copyResCanvas();
				ResCanvas vorbereitungSimulation = null;
				Arbeitspaket zuVerschiebenAp = null;

				vorbereitungSimulation = resCanvas.copyResCanvas();

				for (Arbeitspaket simAp : vorbereitungSimulation.getArbeitspaketListe()) {
					if (simAp.getId() == ap.getId()) {
						zuVerschiebenAp = simAp;
						break;
					}
				}

				vorbereitungSimulation.entferneArbeitspaket(zuVerschiebenAp);

				if (verschieben > 0) {
					// Paket ist zu fr�h | FAZ verletzt

					Stack<Teilpaket> unterhalbStack = new Stack<Teilpaket>();

					Vektor2i position = this.sucheDarunterLiegendePakete(vorbereitungSimulation.getKoordinatenSystem(),
							unterhalbStack, zuVerschiebenAp.getTeilpaketListe().get(0));
					ArrayList<Arbeitspaket> rechtsListe = new ArrayList<Arbeitspaket>();

					if (unterhalbStack.isEmpty()) {
						// 1. Szenario: nichts liegt unter Z

						/*
						 * Erkl�rung des folgenden Algorithmus
						 *
						 * Z ist zu fr�h. Das hei�t Z muss nach rechts verschoben werden.
						 *
						 * 1. Szenario
						 *
						 * ..............CC......DDDDD................
						 * AAAAAA........CC......DDDDD................
						 * AAAAAA...BBBBBBB......DDDDDEEEEEE..........
						 * AAAAAAZZZBBBBBBBCCCCCCDDDDDEEEEEE..........
						 * AAAAAAZZZBBBBBBBCCCCCCDDDDDEEEEEE..........
						 *
						 * unter Z liegt nichts. Das hei�t die Pakete die direkt rechts neben Z
						 * liegeben, in diesem Beispiel B, m�ssen um die Vorgangsdauer von Z nach links
						 * verschoben werden. Alle Paket rechts von Z au�er B, werden um die
						 * Vorgangsdauer von Z nach rechts verschoben! Herunterfallen. Simulation
						 * startet, arbeitet bei jedem Versuch mit unterschiedlichen Kopien. Z von FAZ
						 * bis SEZ versuchen einzuf�gen und obergrenze pr�fen. Wenn obergrenze inordnung
						 * wird eine Kopie der Simulation einer Liste hinzugef�gt. Ist die Simulation
						 * beendet, m�ssen diese Kopien danach �berpr�ft werden, ob die Position eine
						 * m�glichst gerine Anzahl an parallen Arbeiten Mitarbeitern gew�hrleistet. Mit
						 * dem optimalsten Ergebnis wird anschlie�end weitergearbeitet.
						 */

						Arbeitspaket rechtsAp = vorbereitungSimulation.getKoordinatenSystem()[position
								.getyKoordinate()][position.getxKoordinate() + 1].getTeilpaket().getArbeitspaket();

						for (Arbeitspaket arbP : vorbereitungSimulation.getArbeitspaketListe()) {
							if (arbP != rechtsAp) {
								int ersteXpos = arbP.getTeilpaketListe().get(0).getResEinheitListe().get(0)
										.getPosition().getxKoordinate();
								if (ersteXpos > position.getxKoordinate()) {
									rechtsListe.add(arbP);
								}
							}
						}
						rechtsAp.bewegeX(vorbereitungSimulation,
								-zuVerschiebenAp.getTeilpaketListe().get(0).getVorgangsdauer());
					} else {

						// 2. Szenario es liegen Pakete unter Z

						/*
						 * Erkl�rung des folgenden Algorithmus
						 *
						 * Z ist zu fr�h. Das hei�t Z muss nach rechts verschoben werden.
						 *
						 * 2. Szenario
						 *
						 * ......ZZZ|.CC......DDDDD................
						 * AAAAAAZZZ|.CC......DDDDD................
						 * AAAAAABBBBBBB......DDDDDEEEEEE..........
						 * AAAAAABBBBBBBCCCCCCDDDDDEEEEEE..........
						 * AAAAAABBBBBBBCCCCCCDDDDDEEEEEE..........
						 *
						 * | = eine leere L�cke, dient nur der erkl�rung im Folgetext
						 *
						 * unter Z liegt bereits etwas. Das Paket das unter Z liegt, darf nicht
						 * verschoben werden. Aber um zu gew�hrleisten, dass Z so fr�h wie m�glich und
						 * so optimal wie m�glich, das hei�t eine m�glichst gerine Anzahl an parallel
						 * arbeitend Mitarbeitern ereicht ist, m�ssen trotzdem Pakete nach rechts
						 * verschoben werden. Hirzu werden, beginnend dirket neben dem Paket Z ( im Bild
						 * gekennzeichnet mit |), alle Pakete die ungleich den Paketen sind, die direkt
						 * unter Z und B sind, in eine Liste aufgenommen. Die aufgennomen Pakete werden
						 * umd die Vorgansdauer von Z nach rechts verschoben. Im Anschluss beginnt die
						 * Simulation. Siehe Szenario 1.
						 *
						 */

						for (Arbeitspaket arbP : vorbereitungSimulation.getArbeitspaketListe()) {

							int ersteXpos = arbP.getTeilpaketListe().get(0).getResEinheitListe().get(0).getPosition()
									.getxKoordinate();
							if (ersteXpos > position.getxKoordinate()) {
								rechtsListe.add(arbP);

							}
						}

					}

					Collections.sort(rechtsListe, new ComperatorArbeitspaketLR());

					for (Arbeitspaket arbeitspaket : rechtsListe) {
						arbeitspaket.bewegeX(vorbereitungSimulation, ap.getVorgangsdauer());
					}

					vorbereitungSimulation.herunterfallenAlleTeilpaketeAu�erEines(zuVerschiebenAp);
					simuliere(resCanvas, ausgangssituationResCanvs, vorbereitungSimulation, zuVerschiebenAp,
							simLoesungenResCanvas);

				} else if (verschieben < 0) {
					// Paket ist zu sp�t | SEZ verletzt

					/*
					 * Erkl�rung des folgenden Algorithmus
					 *
					 * D ist zu sp�t. Das hei�t D muss nach M�glichkeit nach links verschoben
					 * werden. Ist dies nicht m�glich. Belasse D wo es ist.
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
					 * 2. Simulation starten: D rausl�schen
					 *
					 * In eigenen Kopien wird versucht
					 *
					 * D von FAZ bis SEZ versuchen einzuf�gen und obergrenze pr�fen. Wenn obergrenze
					 * inordnung wird eine Kopie der Simulation einer Liste hinzugef�gt. Ist die
					 * Simulation beendet, m�ssen diese Kopien danach �berpr�ft werden, ob die
					 * Position eine m�glichst gerine Anzahl an parallen Arbeiten Mitarbeitern
					 * gew�hrleistet.
					 *
					 * Wird keine M�glichkeit gefunden, D einzuf�gen, wird mit der Kopie der
					 * Ausgangssituation weitergearbeitet. Mit anderen Worten D wird dort belassen,
					 * wo es urspr�nglich lag.
					 *
					 */

					simuliere(resCanvas, ausgangssituationResCanvs, vorbereitungSimulation, zuVerschiebenAp,
							simLoesungenResCanvas);

				}
			}
		}

	}

	/**
	 * Simuliert f�r das �bergebene Arbeitspaket zuVerschiebenPaket mehre Szenarien
	 * in dem es Versucht das Paket zwischen FAZ und SEZ einzusetzen. F�r jedes
	 * Szenario wird eine Kopie des ResCanvas angelegt. Diese Kopien werden bewertet
	 * und das optimalste Ergebnis wird als unser aktuelles ResCanvas gesetzt. Das
	 * optimale Ergebnis ist das KoordinatenSystem mit der geringsten Anzahl an
	 * parallel arbeitenden Mitarbeitern.
	 *
	 *
	 * @param resCanvas
	 * @param ausgangssituationResCanvs
	 * @param vorbereitungSimulation
	 * @param zuVerschiebenAp
	 */
	private void simuliere(ResCanvas resCanvas, ResCanvas ausgangssituationResCanvs, ResCanvas vorbereitungSimulation,
			Arbeitspaket zuVerschiebenAp, ArrayList<ResCanvas> simLoesungenResCanvas) {

		ResCanvas simulation = null;
		Arbeitspaket copyZuVerschiebenAP = null;
		boolean loesungGefunden = false;

		for (int x = zuVerschiebenAp.getFaz() - 1; x < zuVerschiebenAp.getSaz(); x++) {

			simulation = vorbereitungSimulation.copyResCanvas(zuVerschiebenAp);

			for (Arbeitspaket simAp : simulation.getArbeitspaketListe()) {
				if (simAp.getId() == zuVerschiebenAp.getId()) {
					copyZuVerschiebenAP = simAp;
					break;
				}
			}

			int xStart = copyZuVerschiebenAP.getTeilpaketListe().get(0).getResEinheitListe().get(0).getPosition()
					.getxKoordinate();

			Algorithmus.ausgeben(simulation.getKoordinatenSystem());

			simulation.herunterfallenAlleTeilpakete();

			int abstand = x - xStart;
			copyZuVerschiebenAP.neuSetzen(abstand, simulation);

			Algorithmus.ausgeben(simulation.getKoordinatenSystem());

			System.out.println("L�sung gefunden");

			if (!ueberpruefeObergrenzeUeberschritten(simulation)) {
				loesungGefunden = true;
				simLoesungenResCanvas.add(simulation);
			}

		}

		if (!loesungGefunden) {
			simLoesungenResCanvas.add(ausgangssituationResCanvs);
		}

		// if (listeSimulationen.isEmpty()) {
		// // Wenn das Arbeitspaket nicht verschoben werden kann, wird ohne Ver�nderung
		// der
		// // Ausgangssituation weiter gearbeitet
		// resCanvas.swap(ausgangssituationResCanvs);
		// } else if (listeSimulationen.size() == 1) {
		// resCanvas.swap(listeSimulationen.get(0));
		// } else {
		//
		// // Bewertung der Koordinatensysteme
		//
		// int maxStellen = Integer.MIN_VALUE;
		// ResCanvas optimalesResCanvas = null;
		//
		// for (ResCanvas resCanvasSimulation : listeSimulationen) {
		//
		// for (Arbeitspaket simAp : resCanvasSimulation.getArbeitspaketListe()) {
		// if (simAp.getId() == zuVerschiebenAp.getId()) {
		// copyZuVerschiebenAP = simAp;
		// break;
		// }
		// }
		//
		// int stellen = resCanvasSimulation.ermittleStellen(copyZuVerschiebenAP,
		// AlgoKapazitaetstreu.maxBegrenzung);
		// if (stellen > maxStellen) {
		// maxStellen = stellen;
		// optimalesResCanvas = resCanvasSimulation;
		// }
		// }
		//
		// resCanvas.swap(optimalesResCanvas);
		//
		// }
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

			position = sucheDarunterLiegendePakete(koordinatenSystem, unterhalbStack, teilpaket);

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
				// tp.bewegen(resCanvas, 0, xMove);
				tp.bewegeX(resCanvas, xMove);

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
					// damit sch�nere Aufteilung in der Gui zu sehen ist
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
			Teilpaket teilpaket) {

		ResEinheit unterhalb;

		Vektor2i result = null;

		LOOP: for (ResEinheit res : teilpaket.getResEinheitListe()) {

			result = res.getPosition();
			int x = result.getxKoordinate();
			for (int y = result.getyKoordinate() + 1; y < ResCanvas.koorHoehe; y++) {
				unterhalb = koordinatenSystem[y][x];

				// wenn unterhalb der aktuellen resEinheit nichts liegt,
				// wird die Schleife abgebrochen und die n�chste resEinheit betrachtet
				if (unterhalb == null) {
					break;
				}
				// wenn unterhalb der aktuellen resEinheit eine Reseinheit liegt, die
				// vom selben Teilpaket ist, wird die Betrachtung des aktuellen Teilpaketes
				// abgebrochen
				// die variable result wird auf die vorhergegane ResEinheit gesetzt
				if (teilpaket == unterhalb.getTeilpaket()) {
					int index = teilpaket.getResEinheitListe().indexOf(res);
					result = teilpaket.getResEinheitListe().get(index - 1).getPosition();
					break LOOP;
				}

				if (!unterhalbStack.contains(unterhalb.getTeilpaket())) {
					unterhalbStack.add(unterhalb.getTeilpaket());
				}
			}
		}
		return result;
	}

	/**
	 * �berpr�ft ob Teilpakete �ber der Maximalgrenze an parallel arbeitenden
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
