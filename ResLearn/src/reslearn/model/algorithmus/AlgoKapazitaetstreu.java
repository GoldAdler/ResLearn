package reslearn.model.algorithmus;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.Stack;

import reslearn.model.paket.Arbeitspaket;
import reslearn.model.paket.ResEinheit;
import reslearn.model.paket.Teilpaket;
import reslearn.model.paket.Teilpaket.VerschiebeRichtung;
import reslearn.model.resCanvas.ResCanvas;
import reslearn.model.utils.ComperatorArbeitspaketLR;
import reslearn.model.utils.ComperatorArbeitspaketRL;
import reslearn.model.utils.ComperatorTeilpaket;
import reslearn.model.utils.ComperatorVektor2iX;
import reslearn.model.utils.ComperatorVektor2iY;
import reslearn.model.utils.Vektor2i;

// TODO 1 KLUTKE ERZÄHLEN
// Siehe Kinder UNI maxBegrenzung 2
// ist nicht lösbar, da kompllet neuer Algo nötig währe
// siehe unten

// TODO Fange Fehler ab, wenn ein AP größer ist als maxBegrenzung!!!
/*
 * bzw. löse diesen Fall, falls noch Zeit ist. Wenn vorgangsdauerVeraenderbar
 * möglich ist eine Lösung erstellbar. Dazu muss aber ein neuer Algo erstellt werden,
 * der hier verwendet hier. Viele Methoden sollten, aber recyclbar sein.
 */

public class AlgoKapazitaetstreu extends Algorithmus {

	private static AlgoKapazitaetstreu algoKapazitaetstreu;
	private boolean vorgangsdauerVeraenderbar = true;

	// TODO: Vorläufige Integer. Wieder löschen!!!!
	private static int maxBegrenzung = 5;
	// TODO maxBegrenzung ist zu hoch fall
	// z.B maxBegrenzung = 10;
	// warum werden die Pakete angefasst, obwohl sie in FAZ liegen? Verändert dürfte
	// sich nicht, da wenn maxBegrenzung zu hoch ist, passiert in
	// #kapazitaetsOptimierung eigentlich nichts

	// TODO: Vorläufige Integer. Wieder löschen!!!!
	private static int simZaehler = 0;

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

		resCanvas.aktuallisiereHistorie();
		kapazitaetsOptimierung(resCanvas, koordinatenSystem);

		zeitValidierung(resCanvas);

		return resCanvas;
	}

	/**
	 * Je nach Modus, sprich ob Vorgangsunterbrechung erlaubst ist oder nicht, wird
	 * das aktuelle resCanvas anhand der Zeitkriterien angepasst.
	 *
	 * @param resCanvas
	 */
	private void zeitValidierung(ResCanvas resCanvas) {

		Collections.sort(resCanvas.getArbeitspaketListe(), new ComperatorArbeitspaketLR());
		for (Arbeitspaket ap : resCanvas.getArbeitspaketListe()) {
			ap.zusammenfuegen();
		}

		for (Arbeitspaket ap : resCanvas.getArbeitspaketListe()) {
			ap.ueberpruefeVorgangsunterbrechung(resCanvas);

		}
		Collections.sort(resCanvas.getArbeitspaketListe(), new ComperatorArbeitspaketLR());
		ausgeben(resCanvas.getKoordinatenSystem());

		ArrayList<ResCanvas> moeglicheLoesungenResCanvas = new ArrayList<ResCanvas>();

		zeitValidierung(resCanvas, moeglicheLoesungenResCanvas);

	}

	/**
	 * Alle Arbeitspakete die nicht in FAZ liegen, werden simuliert. Das heißt, sie
	 * werden zwischen FAZ und SAZ eingefügt. Aus diesen Simulationen werden mehre
	 * Lösungsmöglichkeiten erstellt. Für diese Lösungsmöglichkeiten werden wiederum
	 * alle möglichen Positionen der anderen Arbeitspakete simuliert. Diese
	 * Simulatioen ergeben dabei wieder mögliche Lösungen. Alle generierten
	 * Lösungsmöglichkeiten werden im Anschluss bewertet und die beste wird als
	 * Endergebnis verwendet.
	 *
	 * @param resCanvas
	 * @param moeglicheLoesungenResCanvas
	 */
	private void zeitValidierung(ResCanvas resCanvas, ArrayList<ResCanvas> moeglicheLoesungenResCanvas) {

		System.out.println("Simulation start");

		simulationDurchfuehren(resCanvas, moeglicheLoesungenResCanvas, null);

		System.out.println("HALT STOPP");
		for (ResCanvas lul : moeglicheLoesungenResCanvas) {
			Algorithmus.ausgeben(lul.getKoordinatenSystem());
		}

		ResCanvas ergebnis = bewerteLoesungen(moeglicheLoesungenResCanvas);

		// TODO überprüfen ob Arbeitspakete eingemauert sind und beheben

		Algorithmus.ausgeben(ergebnis.getKoordinatenSystem());
		Algorithmus.ausgebenTrotzdem(ergebnis.getKoordinatenSystem());

		resCanvas.swap(ergebnis);

		resCanvas.aktuallisiereHistorie();
		
		System.out.println("Ergebnis");
		Algorithmus.ausgeben(resCanvas.getKoordinatenSystem());
		Algorithmus.ausgebenTrotzdem(resCanvas.getKoordinatenSystem());

	}

	/**
	 * Die gefunden möglichen Lösungen werden Anhand der Kriteren des Harten- und
	 * Weichenabgleiches bewertet. Anschließend wird aus den bewerteten Lösungen,
	 * diejenige ausgewählt, bei der die Arbeitspakete in der frühsten Lage liegen.
	 * Das beste Ergebnis wird zurückgegeben.
	 *
	 * @param moeglicheLoesungenResCanvas
	 * @return
	 */
	private ResCanvas bewerteLoesungen(ArrayList<ResCanvas> moeglicheLoesungenResCanvas) {

		// Es erfolgt auf die nun zu verwendende Liste die bewertung Anhand der
		// rankingHarteKriterien.
		// überprüfe die Lösungen auf Zeitüberschreitungen. Alle Lösungen, die die
		// Zeiten nicht überschreiten, werden in eine gesonderte Liste aufgenommen.

		ArrayList<ResCanvas> keineZeitueberschreitung = new ArrayList<ResCanvas>();

		rankingHarteKriterien(moeglicheLoesungenResCanvas, keineZeitueberschreitung);

		// Wenn keine Lösungen für die Liste keineZeitueberschreitung gefunden werden,
		// wird mit der Liste moeglicheLoesungenResCanvas weitergearbeitet.

		ArrayList<ResCanvas> optimalListe = null;

		if (keineZeitueberschreitung.isEmpty()) {
			moeglicheLoesungenResCanvas = geringsteVerstoesse(moeglicheLoesungenResCanvas);
			optimalListe = rankingWeicheKriterien(moeglicheLoesungenResCanvas);
		} else {
			optimalListe = rankingWeicheKriterien(keineZeitueberschreitung);

			System.out.println("optimal");
			for (ResCanvas lul : optimalListe) {
				Algorithmus.ausgeben(lul.getKoordinatenSystem());
			}

		}

		// Es erfolgt auf die nun zu verwendende Liste die bewertung Anhand der
		// rankingWeicheKriterien.

		ResCanvas ergebnis = sucheFruehesteLage(optimalListe);

		return ergebnis;
	}

	/**
	 * Die möglichen Lösungen werden Anhand des harten Abgleiches bewertet. Siehe
	 * Skript.
	 *
	 * @param moeglicheLoesungenResCanvas
	 * @param keineZeitueberschreitung
	 */
	private void rankingHarteKriterien(ArrayList<ResCanvas> moeglicheLoesungenResCanvas,
			ArrayList<ResCanvas> keineZeitueberschreitung) {
		int ueberschreitung = 0;
		ArrayList<ResCanvas> zuLoeschenListe = new ArrayList<ResCanvas>();
		for (ResCanvas resCanvas : moeglicheLoesungenResCanvas) {
			LOOP: for (Arbeitspaket arbeitspaket : resCanvas.getArbeitspaketListe()) {
				for (Teilpaket teilpaket : arbeitspaket.getTeilpaketListe()) {
					ueberschreitung = teilpaket.ueberpruefeZeiten();
					if (ueberschreitung != 0) {
						break LOOP;
					}
				}
			}
			if (ueberschreitung == 0) {
				keineZeitueberschreitung.add(resCanvas);

			} else if (ueberschreitung > 0) {

				// ist FAZ verletzt, so muss dieses Canvas aus der Liste der möglichen
				// Lösungen entfernt werden. Da dies nie als mögliche Lösung akzeptiert werden
				// darf
				zuLoeschenListe.add(resCanvas);
			}
		}

		for (ResCanvas canvas : zuLoeschenListe) {
			moeglicheLoesungenResCanvas.remove(canvas);
		}

		System.out.println("bewerte");
		for (ResCanvas lul : keineZeitueberschreitung) {
			Algorithmus.ausgeben(lul.getKoordinatenSystem());
		}
	}

	/**
	 * Aus der ArrayList<ResCanvas> optimalListe wird die Lösung ausgewählt, bei der
	 * die Arbeitspakete in der frühsten Lage liegen.
	 *
	 * @param optimalListe
	 * @return
	 */
	private ResCanvas sucheFruehesteLage(ArrayList<ResCanvas> optimalListe) {
		int min = Integer.MAX_VALUE;

		ResCanvas ergebnis = null;

		for (int y = (ResCanvas.koorHoehe - maxBegrenzung); y < ResCanvas.koorHoehe; y++) {

			for (ResCanvas canvas : optimalListe) {

				int counter = 0;

				for (Arbeitspaket ap : canvas.getArbeitspaketListe()) {
					counter += ap.getTeilpaketListe().get(0).getResEinheitListe().get(0).getPosition().getxKoordinate();
				}

				if (counter < min) {
					ergebnis = canvas;
					min = counter;
				}
			}

		}
		return ergebnis;
	}

	/**
	 * Die möglichen Lösungen werden Anhand des weichen Abgleiches bewertet. Siehe
	 * Skript.
	 *
	 * @param keineZeitueberschreitung
	 * @return
	 */
	private ArrayList<ResCanvas> rankingWeicheKriterien(ArrayList<ResCanvas> keineZeitueberschreitung) {
		int min = Integer.MAX_VALUE;

		ArrayList<ResCanvas> durchlaufenListe = keineZeitueberschreitung;
		ArrayList<ResCanvas> optimalListe = new ArrayList<>();
		ResEinheit[][] koordinatenSystem = null;
		for (int y = (ResCanvas.koorHoehe - maxBegrenzung); y < ResCanvas.koorHoehe; y++) {

			for (ResCanvas canvas : durchlaufenListe) {
				koordinatenSystem = canvas.getKoordinatenSystem();
				int counter = 0;

				for (int x = 0; x < ResCanvas.koorBreite; x++) {
					if (koordinatenSystem[y][x] != null) {
						counter++;
					}
				}

				if (counter < min) {
					optimalListe.clear();
					optimalListe.add(canvas);
					min = counter;
				} else if (counter == min) {
					optimalListe.add(canvas);
				}
			}
			durchlaufenListe.clear();
			for (ResCanvas rescanvas : optimalListe) {
				durchlaufenListe.add(rescanvas);
			}

		}
		return optimalListe;
	}

	public ArrayList<ResCanvas> geringsteVerstoesse(ArrayList<ResCanvas> Zeitueberschreitung) {
		int min = Integer.MAX_VALUE;

		ArrayList<ResCanvas> durchlaufenListe = Zeitueberschreitung;
		ArrayList<ResCanvas> optimalListe = new ArrayList<>();
		ResEinheit[][] koordinatenSystem = null;
		for (int y = (ResCanvas.koorHoehe - maxBegrenzung); y < ResCanvas.koorHoehe; y++) {

			for (ResCanvas canvas : durchlaufenListe) {
				koordinatenSystem = canvas.getKoordinatenSystem();
				int counter = 0;
				for (Arbeitspaket arbeitspaket : canvas.getArbeitspaketListe()) {
					for (Teilpaket teilpaket : arbeitspaket.getTeilpaketListe()) {
						int ueberschreitung = teilpaket.ueberpruefeZeiten();
						if (ueberschreitung != 0) {
							counter++;
							break;
						}
					}

				}

				if (counter < min) {
					optimalListe.clear();
					optimalListe.add(canvas);
					min = counter;
				} else if (counter == min) {
					optimalListe.add(canvas);
				}
			}
			durchlaufenListe.clear();
			for (ResCanvas rescanvas : optimalListe) {
				durchlaufenListe.add(rescanvas);
			}

		}
		return optimalListe;

	}

	/**
	 * Alle Arbeitspakete die nicht in FAZ liegen, werden simuliert. Das heißt, sie
	 * werden zwischen FAZ und SAZ eingefügt. Aus diesen Simulationen werden mehre
	 * Lösungsmöglichkeiten erstellt. Für diese Lösungsmöglichkeiten werden wiederum
	 * alle möglichen Positionen der anderen Arbeitspakete simuliert. Diese
	 * Simulatioen ergeben dabei wieder mögliche Lösungen.
	 *
	 * Die möglichen Lösungen werden in die ArrayList<ResCanvas>
	 * moeglicheLoesungenResCanvas eingetragen.
	 *
	 * Diese Methode wird Rekursiv aufgerufen. Deswegen muss der String
	 * nichtMehrAnschauenApID übergeben werden, dass Arbeitspakte nicht mehrfach
	 * überprüft werden. Beim ersten Aufruf der Methode muss lediglich null
	 * übergeben werden.
	 *
	 * @param resCanvas
	 * @param moeglicheLoesungenResCanvas
	 * @param nichtMehrAnschauenApID
	 */
	private void simulationDurchfuehren(ResCanvas resCanvas, ArrayList<ResCanvas> moeglicheLoesungenResCanvas,
			String nichtMehrAnschauenApID) {

		ResCanvas bevorSimulationStartResCanvs = resCanvas.copyResCanvas();

		// hier für z simulation aufrufen : man bekommt fünf ergebnisse / extra Liste
		// ErgebnissevonZListe
		// diese fünf ergebnisse den meoglichenLoesungen hinzufügen

		// ErgebnissevonZListe: entnehmen lösungen und simuliere für jede davon
		// simulationDurchfuehren foreach

		ArrayList<ResCanvas> simLoesungenResCanvas = new ArrayList<ResCanvas>();

		String apID = null;

		ArrayList<Arbeitspaket> arbeitspaketListe = resCanvas.getArbeitspaketListe();

		Collections.sort(arbeitspaketListe, new ComperatorArbeitspaketLR());

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
			resCanvas.aufschliessenTeilpaket();

			ArrayList<Teilpaket> tpListe = arbeitspaketListe.get(i).getTeilpaketListe();
			Teilpaket letztesTeilpaket = tpListe.get(tpListe.size() - 1);
			// TODO: HIER WEITERMACHEN LOGIK warum letztesTeilpaket überprüfen nach Faz?
			// Möglicherweise muss erstes
			// Teilpaket überprüft werden nicht das letzte. Wenn nach dem ersten
			// SchrittModus pakete herunterfallen, läuft er trotzdem in die schleife, da das
			// letzte tp nicht mehr in faz. Aber arbeitspaket liegt in faz
			VerschiebeRichtung verschieben = letztesTeilpaket.ueberpruefeZeitenEnum();
			if (verschieben != VerschiebeRichtung.FAZ) {
				apID = arbeitspaketListe.get(i).getId();
				System.out.println("simZaehler = " + ++simZaehler);
				if (simZaehler == 3) {
					System.out.println("Da ist die Kacke am dampfen!!!");
				}
				System.out.println("Beginn Sim für AP: " + arbeitspaketListe.get(i).getId());
				simulationStarten(resCanvas, arbeitspaketListe.get(i), verschieben, simLoesungenResCanvas);
				break;
			}

			// ausgeben(resCanvas.getKoordinatenSystem());
			// zeitOptimieren(resCanvas, ap, tpListe, letztesTeilpaket);

		}

		if (simLoesungenResCanvas.isEmpty()) {
			moeglicheLoesungenResCanvas.add(bevorSimulationStartResCanvs);
		} else {

			// TODO Lösche identische Lösungen aus der Liste simLoesungenResCanvas heraus,
			// das für diese nicht auch noch mal millionfach simuliert wird

			for (ResCanvas simResCanvas : simLoesungenResCanvas) {
				moeglicheLoesungenResCanvas.add(simResCanvas);
				Algorithmus.ausgeben(simResCanvas.getKoordinatenSystem());
				Algorithmus.ausgebenTrotzdem(simResCanvas.getKoordinatenSystem());
			}

			int nummer = 0;
			for (ResCanvas simResCanvas : simLoesungenResCanvas) {
				++nummer;
				System.out.println("Beginn Sim für Lösungen von AP : " + apID + " " + nummer + " von "
						+ simLoesungenResCanvas.size());

				// TODO Löschen
				if (apID == "Z") {
					System.out.println("jo");
				}

				simulationDurchfuehren(simResCanvas, moeglicheLoesungenResCanvas, apID);

			}
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
	 * Diese Methode startet die Simulation. Um die Simulation durchzuführen, wird
	 * zunächst die Situation vorbereitet die für die Simulation benötigt wird. Im
	 * Anschluss werden, je nach Szenario einer der drei möglichen Algorithmen
	 * verwendet. Eine genauere Beschreibung der Szenarien findet sich inherhalb
	 * dieser Methode.
	 *
	 * @param resCanvas
	 * @param ap
	 * @param verschieben
	 * @param simLoesungenResCanvas
	 */
	private void simulationStarten(ResCanvas resCanvas, Arbeitspaket ap, VerschiebeRichtung verschieben,
			ArrayList<ResCanvas> simLoesungenResCanvas) {

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

		if (verschieben == VerschiebeRichtung.RECHTS) {
			// Paket ist zu früh | FAZ verletzt

			Stack<Teilpaket> unterhalbStack = new Stack<Teilpaket>();

			Vektor2i position = this.sucheDarunterLiegendePakete(vorbereitungSimulation.getKoordinatenSystem(),
					unterhalbStack, zuVerschiebenAp.getTeilpaketListe().get(0));
			ArrayList<Arbeitspaket> rechtsListe = new ArrayList<Arbeitspaket>();

			if (unterhalbStack.isEmpty()) {
				// 1. Szenario: nichts liegt unter Z

				/*
				 * Erklärung des folgenden Algorithmus
				 *
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
				 * liegeben, in diesem Beispiel B, müssen um die Vorgangsdauer von Z nach links
				 * verschoben werden. Alle Paket rechts von Z außer B, werden um die
				 * Vorgangsdauer von Z nach rechts verschoben! Herunterfallen. Simulation
				 * startet, arbeitet bei jedem Versuch mit unterschiedlichen Kopien. Z von FAZ
				 * bis SEZ versuchen einzufügen und obergrenze prüfen. Wenn obergrenze inordnung
				 * wird eine Kopie der Simulation einer Liste hinzugefügt. Ist die Simulation
				 * beendet, müssen diese Kopien danach überprüft werden, ob die Position eine
				 * möglichst gerine Anzahl an parallen Arbeiten Mitarbeitern gewährleistet. Mit
				 * dem optimalsten Ergebnis wird anschließend weitergearbeitet.
				 */

				Arbeitspaket rechtsAp = vorbereitungSimulation.getKoordinatenSystem()[position
						.getyKoordinate()][position.getxKoordinate() + 1].getTeilpaket().getArbeitspaket();

				for (Arbeitspaket arbP : vorbereitungSimulation.getArbeitspaketListe()) {
					if (arbP != rechtsAp) {
						int ersteXpos = arbP.getTeilpaketListe().get(0).getResEinheitListe().get(0).getPosition()
								.getxKoordinate();
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
				 * Erklärung des folgenden Algorithmus
				 *
				 * Z ist zu früh. Das heißt Z muss nach rechts verschoben werden.
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
				 */

				for (Arbeitspaket arbP : vorbereitungSimulation.getArbeitspaketListe()) {

					int ersteXpos = arbP.getTeilpaketListe().get(0).getResEinheitListe().get(0).getPosition()
							.getxKoordinate();
					if (ersteXpos > position.getxKoordinate()) {
						rechtsListe.add(arbP);

					}
				}

			}

			Collections.sort(rechtsListe, new ComperatorArbeitspaketRL());

			for (Arbeitspaket arbeitspaket : rechtsListe) {
				arbeitspaket.bewegeX(vorbereitungSimulation, ap.getVorgangsdauer());
			}

			vorbereitungSimulation.herunterfallenAlleTeilpaketeAußerEines(zuVerschiebenAp);
			simuliere(resCanvas, ausgangssituationResCanvs, vorbereitungSimulation, zuVerschiebenAp,
					simLoesungenResCanvas);

		} else if (verschieben == VerschiebeRichtung.LINKS) {
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

			simuliere(resCanvas, ausgangssituationResCanvs, vorbereitungSimulation, zuVerschiebenAp,
					simLoesungenResCanvas);

		}
	}

	/**
	 * Simuliert für das übergebene Arbeitspaket zuVerschiebenPaket mehre Szenarien
	 * in dem es Versucht das Paket zwischen FAZ und SEZ einzusetzen. Für jedes
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

			copyZuVerschiebenAP = simulation.findeAPnachID(zuVerschiebenAp.getId());

			int xStart = copyZuVerschiebenAP.getTeilpaketListe().get(0).getResEinheitListe().get(0).getPosition()
					.getxKoordinate();

			Algorithmus.ausgeben(simulation.getKoordinatenSystem());

			simulation.herunterfallenAlleTeilpakete();

			int abstand = x - xStart;
			copyZuVerschiebenAP.neuSetzen(abstand, simulation);

			Algorithmus.ausgeben(simulation.getKoordinatenSystem());

			System.out.println("Lösung gefunden bearbeiten");

			if (!ueberpruefeObergrenzeUeberschritten(simulation)) {

				loesungGefunden = true;
				simulation.aufschliessenArbeitspaket();
				simLoesungenResCanvas.add(simulation);

				System.out.println("Lösung gefunden fertig");

			} else if (ueberpruefeObergrenzeUeberschritten(simulation) && vorgangsdauerVeraenderbar) {

				simuliereVorgangsdauerAnpassen(simLoesungenResCanvas, simulation);
			}

		}

		if (!loesungGefunden) {
			simLoesungenResCanvas.add(ausgangssituationResCanvs);
		}

	}

	/**
	 * ResEinheiten die über der Begrenzung, der maximallen Anzahl an parallel
	 * arbeitenden Mitarbeitern liegt, werden je nach Sitation entweder nach links
	 * oder rechts gesetzt.
	 *
	 * @param simLoesungenResCanvas
	 * @param simulation
	 */
	private void simuliereVorgangsdauerAnpassen(ArrayList<ResCanvas> simLoesungenResCanvas, ResCanvas simulation) {

		ResCanvas copySimulation = simulation.copyResCanvas();
		Teilpaket neuesTeilpaket = ueberpruefeObergrenzeResEinheit(simulation, simulation.getKoordinatenSystem());
		Teilpaket copyNeuesTeilpaket = ueberpruefeObergrenzeResEinheit(copySimulation,
				copySimulation.getKoordinatenSystem());
		if (neuesTeilpaket != null) {

			int grenze = ResCanvas.koorHoehe - maxBegrenzung - 1;

			verschiebeLinks(copySimulation, copyNeuesTeilpaket, grenze);
			verschiebeLinks(simulation, neuesTeilpaket, grenze);

			copySimulation.aufschliessenArbeitspaket();

			boolean hinzufuegen = false;

			// verschiebe für copysimulation
			hinzufuegen = verschiebeRechts(copySimulation, copyNeuesTeilpaket, grenze);
			Algorithmus.ausgeben(copySimulation.getKoordinatenSystem());
			if (!copyNeuesTeilpaket.getArbeitspaket().ueberpruefeVorgangsunterbrechungSimulation() && hinzufuegen) {
				simLoesungenResCanvas.add(copySimulation);
			}

			// verschiebe für orginal simulation

			hinzufuegen = verschiebeRechts(simulation, neuesTeilpaket, grenze);

			simulation.aufschliessenArbeitspaket();
			if (!neuesTeilpaket.getArbeitspaket().ueberpruefeVorgangsunterbrechungSimulation() && hinzufuegen) {
				simLoesungenResCanvas.add(simulation);
			}

			Algorithmus.ausgeben(simulation.getKoordinatenSystem());
			System.out.println("Lösung gefunden variable Vorgangsdauer");

		}
	}

	/**
	 * Verschiebt nach Links wenn möglich.
	 *
	 * @param resCanvas
	 * @param neuesTeilpaket
	 * @param grenze
	 */
	private void verschiebeLinks(ResCanvas resCanvas, Teilpaket neuesTeilpaket, int grenze) {
		ArrayList<ResEinheit> gesezteResEinheiten;
		ArrayList<ResEinheit> zuSetzendeResEinheiten = neuesTeilpaket.getResEinheitListe();
		ResEinheit[][] koordinatenSystem = resCanvas.getKoordinatenSystem();
		Arbeitspaket ap = neuesTeilpaket.getArbeitspaket();

		if (!zuSetzendeResEinheiten.isEmpty()) {

			int xPosLinks = zuSetzendeResEinheiten.get(0).getPosition().getxKoordinate();
			for (int x = xPosLinks - 1; x >= ap.getFaz() - 1; x--) {

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
	 * @param neuesTeilpaket
	 * @param grenze
	 */
	private boolean verschiebeRechts(ResCanvas resCanvas, Teilpaket neuesTeilpaket, int grenze) {
		ArrayList<ResEinheit> gesezteResEinheiten;
		ArrayList<ResEinheit> zuSetzendeResEinheiten = neuesTeilpaket.getResEinheitListe();
		ResEinheit[][] koordinatenSystem = resCanvas.getKoordinatenSystem();

		Arbeitspaket ap = neuesTeilpaket.getArbeitspaket();
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
					return false;
				}
			}

			// wenn eine Vorgangsunterbrechung gefunden wird, muss das erste Teilpaket
			// genommen werden und hinten angefügt werden.

			if (ap.ueberpruefeVorgangsunterbrechungSimulation()) {

				korriegiereVorgangsunterbrechung(resCanvas, ap, koordinatenSystem);

			}
		}
		return true;
	}

	/**
	 * Methode behebt die Vorgangsunterbrechung indem das erste Teilpaket hinter dem
	 * letzten Teilpaket des aktuellen Arbeitspaketes gesetzt wird.
	 *
	 *
	 * @param resCanvas
	 * @param ap
	 * @param koordinatenSystem
	 */
	private void korriegiereVorgangsunterbrechung(ResCanvas resCanvas, Arbeitspaket ap,
			ResEinheit[][] koordinatenSystem) {
		Teilpaket tp1 = ap.getTeilpaketListe().get(0);
		ArrayList<ResEinheit> resListe = tp1.getResEinheitListe();
		Teilpaket tp2 = ap.getTeilpaketListe().get(ap.getTeilpaketListe().size() - 1);

		int xPos = tp2.getResEinheitListe().get(0).getPosition().getxKoordinate() + tp2.getVorgangsdauer();
		int index = 0;
		int mitarbeiterAnzahl = 0;

		Loop: for (int x = xPos; x < ResCanvas.koorBreite; x++) {
			for (int y = ResCanvas.koorHoehe - 1; y > ResCanvas.koorHoehe - maxBegrenzung - 1; y--) {
				if (koordinatenSystem[y][x] == null) {
					ResEinheit res = resListe.get(index++);
					mitarbeiterAnzahl++;
					Vektor2i altePos = res.getPosition();

					res.setPosition(new Vektor2i(y, x));
					resCanvas.updatePosition(res, altePos);

					if (index > resListe.size() - 1) {
						break Loop;
					}
					if (mitarbeiterAnzahl == ap.getMitarbeiteranzahl()) {
						mitarbeiterAnzahl = 0;
						break;
					}
				}
			}

		}

		tp1.getArbeitspaket().zusammenfuegen();
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
						return false;
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

			position = sucheDarunterLiegendePakete(koordinatenSystem, unterhalbStack, teilpaket);

			sucheUntenRechts(koordinatenSystem, rechtsVonUnterhalbStack, unterhalbStack, position);

			verschieben(resCanvas, koordinatenSystem, unterhalbStack, rechtsVonUnterhalbStack, teilpaket);
			
			resCanvas.aktuallisiereHistorie();
			
			resCanvas.herunterfallen(teilpaket);

			resCanvas.aktuallisiereHistorie();
			ausgeben(koordinatenSystem);

			resCanvas.aufschliessenTeilpaket();
			resCanvas.aktuallisiereHistorie();
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
			xMove = teilpaket.getVorgangsdauer();
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
			resCanvas.aktuallisiereHistorie();
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
					resCanvas.aktuallisiereHistorie();
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
			Teilpaket teilpaket) {

		ResEinheit unterhalb;

		Vektor2i result = null;

		LOOP: for (ResEinheit res : teilpaket.getResEinheitListe()) {

			result = res.getPosition();
			int x = result.getxKoordinate();
			for (int y = result.getyKoordinate() + 1; y < ResCanvas.koorHoehe; y++) {
				unterhalb = koordinatenSystem[y][x];

				// wenn unterhalb der aktuellen resEinheit nichts liegt,
				// wird die Schleife abgebrochen und die nächste resEinheit betrachtet
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

			tempResEinheit = koordinatenSystem[ResCanvas.koorHoehe - maxBegrenzung - 1][x];
			if (tempResEinheit != null) {
				ueberschritten = true;
				break;
			}
		}

		return ueberschritten;
	}

}
