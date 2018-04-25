package reslearn.main;

import reslearn.model.algorithmus.AlgoErsteSchritt;
import reslearn.model.paket.Arbeitspaket;
import reslearn.model.paket.ResEinheit;
import reslearn.model.resCanvas.ResCanvas;

public class Main {

	public static void main(String[] args) {
		ResCanvas resCanvas = new ResCanvas();

		erstelleTestDaten(resCanvas);

		// durchführen des Algorithmus
		ResEinheit[][] koordinantenSystem = AlgoErsteSchritt.berechne(resCanvas);

		ausgeben(koordinantenSystem);
		
		//TODO Lösche mich weg
		System.out.println("Commit Klemens");

	}

	private static void erstelleTestDaten(ResCanvas resCanvas) {
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
		// resCanvas.hinzufuegen(apB);
		// resCanvas.hinzufuegen(apC);
		// resCanvas.hinzufuegen(apA);

		/*
		 * PTMA_U09_L Test
		 */
		Arbeitspaket apA = new Arbeitspaket("A", 1, 6, 3, 3, 6, 4, 24);
		Arbeitspaket apB = new Arbeitspaket("B", 7, 13, 10, 10, 7, 3, 21);
		Arbeitspaket apC = new Arbeitspaket("C", 9, 16, 14, 14, 8, 2, 16);
		Arbeitspaket apD = new Arbeitspaket("D", 14, 18, 17, 17, 5, 5, 25);
		Arbeitspaket apE = new Arbeitspaket("E", 17, 22, 21, 21, 6, 3, 18);

		resCanvas.hinzufuegen(apB);
		resCanvas.hinzufuegen(apD);
		resCanvas.hinzufuegen(apA);
		resCanvas.hinzufuegen(apE);
		resCanvas.hinzufuegen(apC);

		// -----------------------------TestDaten-------------------------------
		// -------------------------------ENDE--------------------------------
		// ---------------------------------------------------------------------
	}

	public static void ausgeben(ResEinheit[][] koordinantenSystem) {
		// ---------------------------------------------------------------------
		// -----------------------------TestAusgabe-----------------------------
		// -------------------------------Anfang--------------------------------
		for (ResEinheit[] a : koordinantenSystem) {
			for (ResEinheit b : a) {
				if (b == null) {
					System.out.print(".");
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
