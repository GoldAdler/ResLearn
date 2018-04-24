package reslearn.model.algorithmus;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.Stack;

import reslearn.main.Main;
import reslearn.model.paket.Arbeitspaket;
import reslearn.model.paket.ComperatorErsteSchrittModus;
import reslearn.model.paket.ResEinheit;
import reslearn.model.paket.Teilpaket;
import reslearn.model.paket.Vektor2i;
import reslearn.model.resCanvas.ResCanvas;

/* Erklärung des Algorithmus AlgoErsteSchritt
 *
 * Zuerst werden die Arbeitspake sortiert. Diese Sortierung erfolgt nach dem FAZ. Sind die FAZ meherer Arbeitspakete
 * gleich, wird nach der Vorgangsdauer sortiert. #soritereAP
 *
 * Vom ResCanvas wird das Koordinatensystem (2-D-Array) geholt um in diesem die ResEinheiten
 * zu setzen, verschieben etc
 *
 * Der Algorithmus wird durchgeführt #algoDurchfuehren
 *
 * Aus jedem Arbeitspaket in der arbeitspaketliste wird das Teilpaket entommen
 * um den Startpunkt im KoordinatenSystem zu ermitteln.
 * Ist der optimalle Startpunkt (ResCanvas.koorHoehe - 1, arbeitspaket.getFaz() - 1)
 * bereits vergeben wird solange in Y-Richtung gesucht bis ein freier Platz gefunden ist.
 *  ANMERKUNG: (0,0) in einem 2-D-Array ist Links-Oben! (ArrayHoehe - 1, 0) ist Links-unten
 *
 * Ist der Startpunkt gefunden muss überprüft werden, ob über den einzufügenden ResEinheiten
 * bereits andere ResEinheiten anderer Arbeitspakete sind. #ueberpruefeObereFelder
 * Ist dies der Fall müssen diese um nach oben verschoben werden.
 *
 * Abschließend werden die einzufügenden Arbeitspakete in das Koordinatensystem gesetzt.
 *
 */

public abstract class AlgoErsteSchritt {

	public static ResEinheit[][] berechne(ResCanvas resCanvas) {

		ArrayList<Arbeitspaket> arbeitspaketListe = sortiereAP(resCanvas);
		ResEinheit[][] koordinantenSystem = resCanvas.getKoordinantenSystem();
		algoDurchfuehren(arbeitspaketListe, koordinantenSystem, resCanvas);

		return koordinantenSystem;
	}

	/**
	 * Sortieren der Arbeitspakete von
	 * ResCanvas->AktuellerZustand->ArbeitspaketListe
	 */
	private static ArrayList<Arbeitspaket> sortiereAP(ResCanvas resCanvas) {
		ArrayList<Arbeitspaket> arbeitspaketListe = resCanvas.getAktuellerZustand().getArbeitspaketListe();
		Collections.sort(arbeitspaketListe, new ComperatorErsteSchrittModus());
		return arbeitspaketListe;
	}

	/**
	 * Jedes Arbeitspaket in das KoordinatenSystem setzten
	 */
	private static void algoDurchfuehren(ArrayList<Arbeitspaket> arbeitspaketListe, ResEinheit[][] koordinantenSystem,
			ResCanvas resCanvas) {

		for (int i = 0; i < arbeitspaketListe.size(); i++) {
			// if (i != 0) {
			//
			// }
			Arbeitspaket arbeitspaket = arbeitspaketListe.get(i);
			ArrayList<Teilpaket> teilpaketListe = arbeitspaketListe.get(i).getTeilpaketListe();
			int size = 0;

			do {
				size = teilpaketListe.size();
				Teilpaket teilpaket = teilpaketListe.get(size - 1);
				int x_Start = arbeitspaket.getFaz() - 1;
				int y_Start = ResCanvas.koorHoehe - 1;

				y_Start = findeStartpunkt(koordinantenSystem, x_Start, y_Start);

				ueberpruefeObereFelder(koordinantenSystem, resCanvas, arbeitspaket, x_Start, y_Start);

				befuelleKoordinatenSystem(koordinantenSystem, arbeitspaket, teilpaket, x_Start, y_Start);

				// TODO löschen
				Main.ausgeben(koordinantenSystem);

			} while (size != teilpaketListe.size());

		}
	}

	/**
	 * Ermittelt den Startpunkt des Teilpaketes im 2-D-Array der Klasse ResCanvas
	 * (koordinatenSystem)
	 */
	private static int findeStartpunkt(ResEinheit[][] koordinantenSystem, int x_Start, int y_Start) {
		boolean gefunden = false;
		for (int posY = y_Start; posY >= 0; posY--) {
			if (koordinantenSystem[posY][x_Start] == null) {
				gefunden = true;
				y_Start = posY;
				break;
			}
			if (gefunden == true) {
				break;
			}
		}
		return y_Start;
	}

	/**
	 * Befüllt anhand des gefunden Startpunktes (#findeStartpunkt) das 2-D-Array
	 * koordinatenSystem mit den Reseinheiten eines Teilpaketes
	 */
	private static void befuelleKoordinatenSystem(ResEinheit[][] koordinantenSystem, Arbeitspaket arbeitspaket,
			Teilpaket teilpaket, int x_Start, int y_Start) {

		var resEinheitenListe = teilpaket.getResEinheitListe();
		Iterator<ResEinheit> it = resEinheitenListe.iterator();

		for (int y = y_Start; y > y_Start - arbeitspaket.getMitarbeiteranzahl(); y--) {
			for (int x = x_Start; x < arbeitspaket.getFez(); x++) {
				if (koordinantenSystem[y][x] == null) {
					if (it.hasNext()) {
						koordinantenSystem[y][x] = it.next();
						koordinantenSystem[y][x].setPosition(new Vektor2i(y, x));
					}
				}
			}
		}
	}

	/**
	 * Methode überprüft ob über dem einzufügenden Teilpaket A bereits ein Teilpaket
	 * B liegt. Ist dies der Fall muss Teilpaket B nach oben verschoben werden.
	 * Pakete die über B liegen werden ebenfalls nach oben verschoben.
	 *
	 * @param koordinantenSystem
	 * @param resCanvas
	 * @param arbeitspaket
	 * @param x_Start
	 * @param y_Start
	 */
	private static void ueberpruefeObereFelder(ResEinheit[][] koordinantenSystem, ResCanvas resCanvas,
			Arbeitspaket arbeitspaket, int x_Start, int y_Start) {
		for (int y = y_Start; y > y_Start - arbeitspaket.getMitarbeiteranzahl(); y--) {
			if (koordinantenSystem[y][x_Start] != null) {
				int y_Move = arbeitspaket.getMitarbeiteranzahl() - (y_Start - y);
				Stack<Teilpaket> stackTeilpaket = new Stack<>();
				stackTeilpaket.add(koordinantenSystem[y][x_Start].getTeilpaket());

				// über dem einzufügenden Teilpaket A liegt bereits Teilpaket B
				// hier muss jetzt aber auch überprüft werden, ob ein Teilpaket C
				// über B liegt, etc.
				for (int y_Kollision = y; y_Kollision >= 0; y_Kollision--) {
					if (koordinantenSystem[y_Kollision][x_Start] != null) {
						if (!stackTeilpaket.contains(koordinantenSystem[y_Kollision][x_Start].getTeilpaket())) {
							stackTeilpaket.add(koordinantenSystem[y_Kollision][x_Start].getTeilpaket());
						}
					}
				}

				while (!stackTeilpaket.isEmpty()) {
					resCanvas.bewegeNachOben(stackTeilpaket.pop(), y_Move);
				}
				// TODO löschen
				Main.ausgeben(koordinantenSystem);

				break;
			}
			// TODO Algo verbessern
			// Die Überprüfung ob über einem Teilpaket B noch andere Reseinheiten liegen ist
			// noch nicht ausgereift
		}

	}
}
