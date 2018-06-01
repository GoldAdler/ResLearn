package reslearn.model.algorithmus;

import reslearn.model.paket.ResEinheit;
import reslearn.model.resCanvas.ResCanvas;

public abstract class Algorithmus {

	static final boolean testModus = false;
	static final boolean trotzdem = false;
	static final boolean historie = true;
	public static int zaehlerTest = 0;
	public static int zaehlerTrotzdem = 0;

	public abstract ResCanvas algoDurchfuehren(ResCanvas resCanvas);
	
	public static void ausgebenHistorie(ResEinheit[][] koordinatenSystem) {

		if (historie) {
			System.out.println("zaehlerTest: " + ++zaehlerTest);
			for (ResEinheit[] a : koordinatenSystem) {
				for (ResEinheit b : a) {
					if (b == null) {
						System.out.print(".");
					} else {
						System.out.print(b.getTeilpaket().getArbeitspaket().getId());
					}
				}
				System.out.println();
			}
			System.out.println();
		}

		// TODO löschen
		if (zaehlerTest == 128) {
			System.out.println("Pause");
		}

	}


	/*
	 * Methode gibt den aktuellen Zustand des koordinatenSystems (ResEinheit[][]) in
	 * der Eclipse-Console aus. Diese Ausgabe ist als Unterstützung für Entwickler
	 * gedacht. Im laufenden Betrieb sollte die Methode deaktivert werden. Dazu muss
	 * in der Klasse Algorithmus, also die Klasse die #ausgeben bereitstellt, der
	 * boolean testModus auf den Wert false gesetzt werden.
	 *
	 */
	public static void ausgeben(ResEinheit[][] koordinatenSystem) {

		if (testModus) {
			System.out.println("zaehlerTest: " + ++zaehlerTest);
			for (ResEinheit[] a : koordinatenSystem) {
				for (ResEinheit b : a) {
					if (b == null) {
						System.out.print(".");
					} else {
						System.out.print(b.getTeilpaket().getArbeitspaket().getId());
					}
				}
				System.out.println();
			}
			System.out.println();
		}

		// TODO löschen
		if (zaehlerTest == 128) {
			System.out.println("Pause");
		}

	}

	/*
	 * Gibt aus wenn trotzdem true ist.
	 *
	 */
	public static void ausgebenTrotzdem(ResEinheit[][] koordinatenSystem) {

		if (trotzdem) {
			System.out.println("zaehlerTest: " + ++zaehlerTrotzdem);
			for (ResEinheit[] a : koordinatenSystem) {
				for (ResEinheit b : a) {
					if (b == null) {
						System.out.print(".");
					} else {
						System.out.print(b.getTeilpaket().getArbeitspaket().getId());
					}
				}
				System.out.println();
			}
			System.out.println();
		}
	}

}
