package reslearn.model.algorithmus;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.Stack;

import reslearn.model.paket.Arbeitspaket;
import reslearn.model.paket.ResEinheit;
import reslearn.model.paket.Teilpaket;
import reslearn.model.resCanvas.ResCanvas;
import reslearn.model.utils.ComperatorFaz;
import reslearn.model.utils.Vektor2i;

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
 * Abschließend werden die einzufügenden Arbeitspakete in das Koordinatensystem gesetzt. #befuelleKoordinatenSystem
 *
 */

public class AlgoErsteSchritt extends Algorithmus {

	private static AlgoErsteSchritt algoErsteSchritt;

	private AlgoErsteSchritt() {
	}

	public static AlgoErsteSchritt getInstance() {
		if (algoErsteSchritt == null) {
			algoErsteSchritt = new AlgoErsteSchritt();
		}
		return algoErsteSchritt;
	}

	@Override
	public ResCanvas algoDurchfuehren(ResCanvas resCanvas) {

		ArrayList<Arbeitspaket> arbeitspaketListe = sortiereAP(resCanvas);
		ResEinheit[][] koordinatenSystem = resCanvas.getKoordinatenSystem();
		berechne(arbeitspaketListe, koordinatenSystem, resCanvas);

		return resCanvas;
	}

	/**
	 * Sortieren der Arbeitspakete von
	 * ResCanvas->AktuellerZustand->ArbeitspaketListe
	 */
	public static ArrayList<Arbeitspaket> sortiereAP(ResCanvas resCanvas) {
		ArrayList<Arbeitspaket> arbeitspaketListe = resCanvas.getArbeitspaketListe();
		Collections.sort(arbeitspaketListe, new ComperatorFaz());
		return arbeitspaketListe;
	}

	/**
	 * Jedes Arbeitspaket in das KoordinatenSystem setzten
	 */
	private static void berechne(ArrayList<Arbeitspaket> arbeitspaketListe, ResEinheit[][] koordinatenSystem,
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

				y_Start = findeStartpunkt(koordinatenSystem, x_Start, y_Start);

				ueberpruefeObereFelder(koordinatenSystem, resCanvas, arbeitspaket, x_Start, y_Start);

				befuelleKoordinatenSystem(koordinatenSystem, arbeitspaket, teilpaket, x_Start, y_Start);

				ausgeben(koordinatenSystem);

			} while (size != teilpaketListe.size());

		}
	}

	/**
	 * Ermittelt den Startpunkt des Teilpaketes im 2-D-Array der Klasse ResCanvas
	 * (koordinatenSystem)
	 */
	private static int findeStartpunkt(ResEinheit[][] koordinatenSystem, int x_Start, int y_Start) {
		boolean gefunden = false;
		for (int posY = y_Start; posY >= 0; posY--) {
			if (koordinatenSystem[posY][x_Start] == null) {
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
	private static void befuelleKoordinatenSystem(ResEinheit[][] koordinatenSystem, Arbeitspaket arbeitspaket,
			Teilpaket teilpaket, int x_Start, int y_Start) {

		var resEinheitenListe = teilpaket.getResEinheitListe();
		Iterator<ResEinheit> it = resEinheitenListe.iterator();

		for (int y = y_Start; y > y_Start - arbeitspaket.getMitarbeiteranzahl(); y--) {
			for (int x = x_Start; x < arbeitspaket.getFez(); x++) {
				if (koordinatenSystem[y][x] == null) {
					if (it.hasNext()) {
						koordinatenSystem[y][x] = it.next();
						koordinatenSystem[y][x].setPosition(new Vektor2i(y, x));
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
	 * @param koordinatenSystem
	 * @param resCanvas
	 * @param arbeitspaket
	 * @param xStart
	 * @param yStart
	 */
	private static void ueberpruefeObereFelder(ResEinheit[][] koordinatenSystem, ResCanvas resCanvas,
			Arbeitspaket arbeitspaket, int xStart, int yStart) {
		for (int y = yStart; y > yStart - arbeitspaket.getMitarbeiteranzahl(); y--) {
			if (koordinatenSystem[y][xStart] != null) {
				int yMove = arbeitspaket.getMitarbeiteranzahl() - (yStart - y);
				Stack<Teilpaket> stackTeilpaket = new Stack<>();
				stackTeilpaket.add(koordinatenSystem[y][xStart].getTeilpaket());

				// über dem einzufügenden Teilpaket A liegt bereits Teilpaket B
				// hier muss jetzt aber auch überprüft werden, ob ein Teilpaket C
				// über B liegt, etc.
				for (int yKollision = y; yKollision >= 0; yKollision--) {
					for (int xKollision = stackTeilpaket.peek().getArbeitspaket().getFaz(); xKollision < stackTeilpaket
							.peek().getArbeitspaket().getFez(); xKollision++) {
						if (koordinatenSystem[yKollision][xKollision] != null) {
							if (!stackTeilpaket.contains(koordinatenSystem[yKollision][xKollision].getTeilpaket())) {
								stackTeilpaket.add(koordinatenSystem[yKollision][xKollision].getTeilpaket());
							}
						}
					}
				}

				// TODO: Eventuell rekursiv lösen

				while (!stackTeilpaket.isEmpty()) {
					stackTeilpaket.pop().bewegeY(resCanvas, yMove);
				}

				ausgeben(koordinatenSystem);

				break;
			}
			// TODO Algo verbessern
			// Die Überprüfung ob über einem Teilpaket B noch andere Reseinheiten liegen ist
			// noch nicht ausgereift
		}

	}
}
