package reslearn.model.algorithmus;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.Stack;

import reslearn.model.canvas.Canvas;
import reslearn.model.paket.Arbeitspaket;
import reslearn.model.paket.ComperatorErsteSchrittModus;
import reslearn.model.paket.ResEinheit;
import reslearn.model.paket.Teilpaket;
import reslearn.model.paket.Vektor2i;

// TODO Erklärung ausarbeiten
/* Erklärung des Algorithmus AlgoErsteSchritt
 *
 * Zuerst werden die Arbeitspake sortiert
 *
 *
 */

public abstract class AlgoErsteSchritt {

	public static ResEinheit[][] berechne(Canvas canvas) {

		ArrayList<Arbeitspaket> arbeitspaketListe = sortiereAP(canvas);
		ResEinheit[][] koordinantenSystem = canvas.getKoordinantenSystem();
		algoDurchfuehren(arbeitspaketListe, koordinantenSystem, canvas);

		return koordinantenSystem;
	}

	/**
	 * Sortieren der Arbeitspakete von Canvas->AktuellerZustand->ArbeitspaketListe
	 */
	private static ArrayList<Arbeitspaket> sortiereAP(Canvas canvas) {
		ArrayList<Arbeitspaket> arbeitspaketListe = canvas.getAktuellerZustand().getArbeitspaketListe();
		Collections.sort(arbeitspaketListe, new ComperatorErsteSchrittModus());
		return arbeitspaketListe;
	}

	/**
	 * Jedes Arbeitspaket in das KoordinatenSystem setzten
	 */
	private static void algoDurchfuehren(ArrayList<Arbeitspaket> arbeitspaketListe, ResEinheit[][] koordinantenSystem,
			Canvas canvas) {

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
				int y_Start = Canvas.koorHoehe - 1;

				y_Start = findeStartpunkt(koordinantenSystem, x_Start, y_Start);

				// ---------------------------------------------------------------------------------------------------------
				ueberpruefeObereFelder(koordinantenSystem, canvas, arbeitspaket, x_Start, y_Start);
				// ---------------------------------------------------------------------------------------------------------

				befuelleKoordinatenSystem(koordinantenSystem, arbeitspaket, teilpaket, x_Start, y_Start);

			} while (size != teilpaketListe.size());

		}
	}

	/**
	 * Ermittelt den Startpunkt des Teilpaketes im 2-D-Array der Klasse Canvas
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
						koordinantenSystem[y][x].setVektor(new Vektor2i(y, x));
					}
				}
			}
		}
	}

	/**
	 * Methode überprüft ob über dem einzufügenden Teilpaket A bereits ein Teilpaket
	 * B liegt ist dies der Fall muss Teilpaket B nach oben verschoben werden
	 *
	 * @param koordinantenSystem
	 * @param canvas
	 * @param arbeitspaket
	 * @param x_Start
	 * @param y_Start
	 */
	private static void ueberpruefeObereFelder(ResEinheit[][] koordinantenSystem, Canvas canvas,
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
					canvas.bewegeNachOben(stackTeilpaket.pop(), y_Move);
				}
				break;
			}
		}
	}
}
