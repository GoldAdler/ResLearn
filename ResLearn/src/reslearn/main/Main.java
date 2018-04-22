package reslearn.main;

import reslearn.model.algorithmus.AlgoErsteSchritt;
import reslearn.model.canvas.Canvas;
import reslearn.model.paket.Arbeitspaket;
import reslearn.model.paket.ResEinheit;

public class Main {

	public static void main(String[] args) {
		Canvas canvas = new Canvas();

		erstelleTestDaten(canvas);

		// durchführen des Algorithmus
		var koordinantenSystem = AlgoErsteSchritt.berechne(canvas);

		ausgeben(koordinantenSystem);

	}

	private static void erstelleTestDaten(Canvas canvas) {
		// ---------------------------------------------------------------------
		// -----------------------------TestDaten-------------------------------
		// -------------------------------Anfang--------------------------------

		/*
		 * Erster Test
		 */
		// Arbeitspaket apA = new Arbeitspaket("A", 1, 2, 3, 4, 2, 3, 6);
		// Arbeitspaket apB = new Arbeitspaket("B", 1, 2, 4, 4, 2, 4, 8);
		// Arbeitspaket apC = new Arbeitspaket("C", 3, 5, 5, 7, 2, 3, 6);
		//
		// canvas.hinzufuegen(apB);
		// canvas.hinzufuegen(apC);
		// canvas.hinzufuegen(apA);

		/*
		 * PTMA_U09_L Test
		 */
		Arbeitspaket apA = new Arbeitspaket("A", 1, 6, 3, 3, 6, 4, 24);
		Arbeitspaket apB = new Arbeitspaket("B", 7, 13, 10, 10, 7, 3, 21);
		Arbeitspaket apC = new Arbeitspaket("C", 9, 16, 14, 14, 8, 2, 16);
		Arbeitspaket apD = new Arbeitspaket("D", 14, 18, 17, 17, 5, 5, 25);
		Arbeitspaket apE = new Arbeitspaket("E", 17, 22, 21, 21, 6, 3, 18);

		canvas.hinzufuegen(apB);
		canvas.hinzufuegen(apD);
		canvas.hinzufuegen(apA);
		canvas.hinzufuegen(apE);
		canvas.hinzufuegen(apC);

		// -----------------------------TestDaten-------------------------------
		// -------------------------------ENDE--------------------------------
		// ---------------------------------------------------------------------
	}

	private static void ausgeben(ResEinheit[][] koordinantenSystem) {
		// ---------------------------------------------------------------------
		// -----------------------------TestAusgabe-----------------------------
		// -------------------------------Anfang--------------------------------
		for (ResEinheit[] a : koordinantenSystem) {
			for (ResEinheit b : a) {
				if (b == null) {
					System.out.print(" ");
				} else {
					System.out.print(b.getTeilpaket().getArbeitspaket().getId());
				}
			}
			System.out.println();
		}
		// -----------------------------TestAusgabe-----------------------------
		// -------------------------------ENDE--------------------------------
		// ---------------------------------------------------------------------
	}

}
