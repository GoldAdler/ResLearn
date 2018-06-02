package reslearn.model.algorithmus;

import java.util.ArrayList;

import reslearn.model.paket.ResEinheit;
import reslearn.model.resCanvas.ResCanvas;

public abstract class Algorithmus {

	static final boolean testModus = false;
	static final boolean trotzdem = false;
	static final boolean historie = true;
	static final boolean test = false;
	public static int zaehlerTest = 0;
	public static int zaehlerTrotzdem = 0;

	public abstract ResCanvas algoDurchfuehren(ResCanvas resCanvas);

	public static void ausgebenKurzerTest(ResEinheit[][] koordinatenSystem) {

		if (test) {
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

	}

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

	/**
	 * Die möglichen Lösungen werden Anhand des weichen Abgleiches bewertet. Siehe
	 * Skript.
	 *
	 * @param keineZeitueberschreitung
	 * @param grenze
	 * @return
	 */
	protected ArrayList<ResCanvas> rankingWeicheKriterien(ArrayList<ResCanvas> keineZeitueberschreitung, int grenze) {
		int min = Integer.MAX_VALUE;

		ArrayList<ResCanvas> durchlaufenListe = keineZeitueberschreitung;
		ArrayList<ResCanvas> optimalListe = new ArrayList<>();
		ResEinheit[][] koordinatenSystem = null;
		for (int y = grenze; y < ResCanvas.koorHoehe; y++) {

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
					boolean gefunden = false;
					for (ResCanvas resCanvas : optimalListe) {
						if (resCanvas == canvas) {
							gefunden = true;
						}
					}
					if (gefunden == false) {
						optimalListe.add(canvas);
					}
				}
			}
			durchlaufenListe.clear();
			for (ResCanvas rescanvas : optimalListe) {
				durchlaufenListe.add(rescanvas);
			}

		}
		return optimalListe;
	}

}
