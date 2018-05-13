package reslearn.model.algorithmus;

import reslearn.model.paket.ResEinheit;
import reslearn.model.resCanvas.ResCanvas;

public abstract class Algorithmus {

	static final boolean testModus = true;

	public abstract ResEinheit[][] algoDurchfuehren(ResCanvas resCanvas);

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
