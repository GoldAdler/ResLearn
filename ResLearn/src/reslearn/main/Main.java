package reslearn.main;

import reslearn.model.algorithmus.AlgoKapazitaetstreu;
import reslearn.model.paket.Arbeitspaket;
import reslearn.model.paket.ResEinheit;
import reslearn.model.resCanvas.ResCanvas;

public class Main {

	public static void main(String[] args) {
		ResCanvas resCanvas = new ResCanvas();

		// erstelleTestDaten1(resCanvas);
		// erstelleTestDaten2(resCanvas);
		// erstelleTestDaten2_5(resCanvas);
		// erstelleTestDaten2_6(resCanvas);
		erstelleTestDaten2_7(resCanvas);
		// erstelleTestDaten2_8(resCanvas);

		// durchführen des Algorithmus ErsteSchritt
		// ResEinheit[][] koordinatenSystem =
		// AlgoErsteSchritt.getInstance().algoDurchfuehren(resCanvas);

		// durchführen des Algorithmus AlgoKapazitaetstreu
		ResEinheit[][] koordinatenSystem = AlgoKapazitaetstreu.getInstance().algoDurchfuehren(resCanvas);

		ausgeben(koordinatenSystem);

	}

	private static void erstelleTestDaten1(ResCanvas resCanvas) {
		// ---------------------------------------------------------------------
		// -----------------------------TestDaten-------------------------------
		// -------------------------------Anfang--------------------------------

		/*
		 * Erster Test
		 */
		Arbeitspaket apA = new Arbeitspaket("A", 1, 2, 3, 4, 2, 3, 6);
		Arbeitspaket apB = new Arbeitspaket("B", 1, 2, 4, 4, 2, 4, 8);
		Arbeitspaket apC = new Arbeitspaket("C", 3, 5, 5, 7, 2, 3, 6);

		resCanvas.hinzufuegen(apB);
		resCanvas.hinzufuegen(apC);
		resCanvas.hinzufuegen(apA);

		// -----------------------------TestDaten-------------------------------
		// -------------------------------ENDE--------------------------------
		// ---------------------------------------------------------------------
	}

	private static void erstelleTestDaten2(ResCanvas resCanvas) {
		// ---------------------------------------------------------------------
		// -----------------------------TestDaten-------------------------------
		// -------------------------------Anfang--------------------------------

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
		// resCanvas.hinzufuegen(apZ);

		// -----------------------------TestDaten-------------------------------
		// -------------------------------ENDE--------------------------------
		// ---------------------------------------------------------------------
	}

	private static void erstelleTestDaten2_5(ResCanvas resCanvas) {
		// ---------------------------------------------------------------------
		// -----------------------------TestDaten-------------------------------
		// -------------------------------Anfang--------------------------------

		/*
		 * PTMA_U09_L Test
		 */
		Arbeitspaket apA = new Arbeitspaket("A", 1, 6, 3, 3, 6, 4, 24);
		Arbeitspaket apB = new Arbeitspaket("B", 7, 13, 10, 10, 7, 3, 21);
		Arbeitspaket apC = new Arbeitspaket("C", 9, 16, 14, 14, 8, 2, 16);
		Arbeitspaket apD = new Arbeitspaket("D", 14, 18, 17, 17, 5, 5, 25);
		Arbeitspaket apE = new Arbeitspaket("E", 17, 22, 21, 21, 6, 3, 18);
		// Arbeitspaket apZ = new Arbeitspaket("Z", 10, 12, 10, 12, 3, 2, 6);

		resCanvas.hinzufuegen(apB);
		resCanvas.hinzufuegen(apD);
		resCanvas.hinzufuegen(apA);
		resCanvas.hinzufuegen(apE);
		resCanvas.hinzufuegen(apC);
		// resCanvas.hinzufuegen(apZ);

		// -----------------------------TestDaten-------------------------------
		// -------------------------------ENDE--------------------------------
		// ---------------------------------------------------------------------
	}

	private static void erstelleTestDaten2_6(ResCanvas resCanvas) {
		// ---------------------------------------------------------------------
		// -----------------------------TestDaten-------------------------------
		// -------------------------------Anfang--------------------------------

		/*
		 * PTMA_U09_L Test
		 */
		Arbeitspaket apA = new Arbeitspaket("A", 1, 6, 3, 3, 6, 4, 24);
		Arbeitspaket apB = new Arbeitspaket("B", 7, 13, 10, 10, 7, 3, 21);
		Arbeitspaket apC = new Arbeitspaket("C", 9, 16, 14, 14, 8, 2, 16);
		Arbeitspaket apD = new Arbeitspaket("D", 14, 18, 17, 17, 5, 2, 10);
		Arbeitspaket apE = new Arbeitspaket("E", 17, 22, 21, 21, 6, 3, 18);
		// Arbeitspaket apZ = new Arbeitspaket("Z", 10, 12, 10, 12, 3, 2, 6);

		resCanvas.hinzufuegen(apB);
		resCanvas.hinzufuegen(apD);
		resCanvas.hinzufuegen(apA);
		resCanvas.hinzufuegen(apE);
		resCanvas.hinzufuegen(apC);
		// resCanvas.hinzufuegen(apZ);

		// -----------------------------TestDaten-------------------------------
		// -------------------------------ENDE--------------------------------
		// ---------------------------------------------------------------------
	}

	private static void erstelleTestDaten2_8(ResCanvas resCanvas) {
		// ---------------------------------------------------------------------
		// -----------------------------TestDaten-------------------------------
		// -------------------------------Anfang--------------------------------

		/*
		 * PTMA_U09_L Test
		 */
		Arbeitspaket apA = new Arbeitspaket("A", 1, 6, 3, 3, 6, 4, 24);
		Arbeitspaket apB = new Arbeitspaket("B", 7, 13, 10, 10, 7, 3, 21);
		Arbeitspaket apC = new Arbeitspaket("C", 9, 16, 14, 14, 8, 2, 16);
		Arbeitspaket apD = new Arbeitspaket("D", 11, 15, 14, 18, 5, 2, 10);
		Arbeitspaket apE = new Arbeitspaket("E", 14, 19, 21, 21, 6, 3, 18);
		// Arbeitspaket apZ = new Arbeitspaket("Z", 10, 12, 10, 12, 3, 2, 6);

		resCanvas.hinzufuegen(apB);
		resCanvas.hinzufuegen(apD);
		resCanvas.hinzufuegen(apA);
		resCanvas.hinzufuegen(apE);
		resCanvas.hinzufuegen(apC);
		// resCanvas.hinzufuegen(apZ);

		// -----------------------------TestDaten-------------------------------
		// -------------------------------ENDE--------------------------------
		// ---------------------------------------------------------------------
	}

	private static void erstelleTestDaten2_7(ResCanvas resCanvas) {
		// ---------------------------------------------------------------------
		// -----------------------------TestDaten-------------------------------
		// -------------------------------Anfang--------------------------------

		/*
		 * PTMA_U09_L Test
		 */
		Arbeitspaket apA = new Arbeitspaket("A", 1, 6, 3, 3, 6, 4, 24);
		Arbeitspaket apB = new Arbeitspaket("B", 7, 13, 10, 10, 7, 3, 21);
		Arbeitspaket apC = new Arbeitspaket("C", 9, 16, 14, 14, 8, 2, 16);
		Arbeitspaket apD = new Arbeitspaket("D", 11, 15, 14, 18, 5, 2, 10);
		Arbeitspaket apE = new Arbeitspaket("E", 17, 22, 21, 21, 6, 3, 18);
		// Arbeitspaket apZ = new Arbeitspaket("Z", 10, 12, 10, 12, 3, 2, 6);

		resCanvas.hinzufuegen(apB);
		resCanvas.hinzufuegen(apD);
		resCanvas.hinzufuegen(apA);
		resCanvas.hinzufuegen(apE);
		resCanvas.hinzufuegen(apC);
		// resCanvas.hinzufuegen(apZ);

		// -----------------------------TestDaten-------------------------------
		// -------------------------------ENDE--------------------------------
		// ---------------------------------------------------------------------
	}

	public static void ausgeben(ResEinheit[][] koordinatenSystem) {
		// ---------------------------------------------------------------------
		// -----------------------------TestAusgabe-----------------------------
		// -------------------------------Anfang--------------------------------
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
		// -----------------------------TestAusgabe-----------------------------
		// -------------------------------ENDE--------------------------------
		// ---------------------------------------------------------------------
	}

}
