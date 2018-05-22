package reslearn.main;

import reslearn.model.algorithmus.AlgoKapazitaetstreu;
import reslearn.model.algorithmus.Algorithmus;
import reslearn.model.paket.Arbeitspaket;
import reslearn.model.paket.ResEinheit;
import reslearn.model.resCanvas.ResCanvas;

public class Main {

	public static void main(String[] args) {
		ResCanvas resCanvas = new ResCanvas();

		// -----------------------------------------------------
		// Datensätze

		// kleine Test
		// erstelleTestDaten1(resCanvas);

		// PTMA_U09
		// erstelleTestDaten2(resCanvas);

		// Random Datensätze
		// erstelleTestDaten2_5(resCanvas);
		// erstelleTestDaten2_55(resCanvas);
		// erstelleTestDaten2_56(resCanvas);
		// erstelleTestDaten2_6(resCanvas);
		// erstelleTestDaten2_7(resCanvas);
		// erstelleTestDaten2_8(resCanvas);

		// Kinder-Uni
		erstelleAufgabeKinderuni(resCanvas);

		// Prüfungsaufgabe
		// erstellePruefungsAufgabe(resCanvas);
		// erstellePruefungsAufgabeMitE(resCanvas);
		// erstellePruefungsAufgabeMitEBlosKleiner(resCanvas);
		// TODO: Klutke fragen
		// Müssen überprüft werden ob die Pakete unterhalb der Obergrenze verschoben
		// werden können

		// ----------------------------------------------------------
		// Algos

		Algorithmus algorithmus;

		// durchführen des Algorithmus ErsteSchritt
		// algorithmus = AlgoErsteSchritt.getInstance();

		// durchführen des Algorithmus AlgoKapazitaetstreu
		algorithmus = AlgoKapazitaetstreu.getInstance();

		resCanvas = algorithmus.algoDurchfuehren(resCanvas);

		Algorithmus.ausgeben(resCanvas.getKoordinatenSystem());

		System.out.println("----------------------------------------------------------");
		System.out.println("----------------------------------------------------------");
		System.out.println("--------------Historien-Ausgabe---------------------------");
		for (ResEinheit[][] koor : resCanvas.getHistorieKoordinatenSystem()) {
			Algorithmus.ausgeben(koor);
		}

	}

	private static void erstelleAufgabeKinderuni(ResCanvas resCanvas) {
		/*
		 * Aufgabe aus der Kinderuni-Vorlesung
		 */
		Arbeitspaket apA = new Arbeitspaket("A", 1, 2, 1, 2, 2, 1, 2);
		Arbeitspaket apB = new Arbeitspaket("B", 3, 3, 3, 3, 1, 3, 3);
		Arbeitspaket apC = new Arbeitspaket("C", 4, 5, 4, 5, 2, 2, 4);
		Arbeitspaket apD = new Arbeitspaket("D", 4, 4, 4, 4, 1, 2, 2);

		resCanvas.hinzufuegen(apB);
		resCanvas.hinzufuegen(apC);
		resCanvas.hinzufuegen(apA);
		resCanvas.hinzufuegen(apD);
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
		Arbeitspaket apC = new Arbeitspaket("C", 3, 5, 5, 7, 3, 2, 6);

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
		Arbeitspaket apA = new Arbeitspaket("A", 1, 6, 3, 8, 6, 4, 24);
		Arbeitspaket apB = new Arbeitspaket("B", 7, 13, 10, 16, 7, 3, 21);
		Arbeitspaket apC = new Arbeitspaket("C", 9, 16, 14, 21, 8, 2, 16);
		Arbeitspaket apD = new Arbeitspaket("D", 14, 18, 17, 21, 5, 5, 25);
		Arbeitspaket apE = new Arbeitspaket("E", 17, 22, 21, 26, 6, 3, 18);

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

		Arbeitspaket apA = new Arbeitspaket("A", 1, 6, 3, 8, 6, 4, 24);
		Arbeitspaket apB = new Arbeitspaket("B", 7, 13, 10, 16, 7, 3, 21);
		Arbeitspaket apC = new Arbeitspaket("C", 9, 16, 14, 21, 8, 2, 16);
		Arbeitspaket apD = new Arbeitspaket("D", 14, 18, 17, 21, 5, 5, 25);
		Arbeitspaket apE = new Arbeitspaket("E", 17, 22, 21, 26, 6, 3, 18);
		Arbeitspaket apZ = new Arbeitspaket("Z", 10, 12, 10, 12, 3, 2, 6);

		resCanvas.hinzufuegen(apB);
		resCanvas.hinzufuegen(apD);
		resCanvas.hinzufuegen(apA);
		resCanvas.hinzufuegen(apE);
		resCanvas.hinzufuegen(apC);
		resCanvas.hinzufuegen(apZ);

		// -----------------------------TestDaten-------------------------------
		// -------------------------------ENDE--------------------------------
		// ---------------------------------------------------------------------
	}

	private static void erstelleTestDaten2_55(ResCanvas resCanvas) {
		// ---------------------------------------------------------------------
		// -----------------------------TestDaten-------------------------------
		// -------------------------------Anfang--------------------------------

		Arbeitspaket apA = new Arbeitspaket("A", 1, 6, 3, 8, 6, 4, 24);
		Arbeitspaket apB = new Arbeitspaket("B", 7, 13, 10, 16, 7, 3, 21);
		Arbeitspaket apC = new Arbeitspaket("C", 9, 16, 14, 21, 8, 2, 16);
		Arbeitspaket apD = new Arbeitspaket("D", 14, 18, 17, 21, 5, 3, 15);
		Arbeitspaket apE = new Arbeitspaket("E", 17, 22, 21, 26, 6, 3, 18);
		Arbeitspaket apZ = new Arbeitspaket("Z", 10, 12, 10, 12, 3, 2, 6);

		resCanvas.hinzufuegen(apB);
		resCanvas.hinzufuegen(apD);
		resCanvas.hinzufuegen(apA);
		resCanvas.hinzufuegen(apE);
		resCanvas.hinzufuegen(apC);
		resCanvas.hinzufuegen(apZ);

		// -----------------------------TestDaten-------------------------------
		// -------------------------------ENDE--------------------------------
		// ---------------------------------------------------------------------
	}

	private static void erstelleTestDaten2_56(ResCanvas resCanvas) {
		// ---------------------------------------------------------------------
		// -----------------------------TestDaten-------------------------------
		// -------------------------------Anfang--------------------------------

		Arbeitspaket apA = new Arbeitspaket("A", 1, 6, 3, 8, 6, 4, 24);
		Arbeitspaket apB = new Arbeitspaket("B", 7, 13, 10, 16, 7, 3, 21);
		Arbeitspaket apC = new Arbeitspaket("C", 9, 16, 14, 21, 8, 2, 16);
		Arbeitspaket apD = new Arbeitspaket("D", 14, 18, 17, 21, 5, 5, 25);
		Arbeitspaket apE = new Arbeitspaket("E", 17, 22, 21, 26, 6, 3, 18);
		Arbeitspaket apZ = new Arbeitspaket("Z", 10, 12, 10, 12, 3, 2, 6);

		resCanvas.hinzufuegen(apB);
		resCanvas.hinzufuegen(apD);
		resCanvas.hinzufuegen(apA);
		resCanvas.hinzufuegen(apE);
		resCanvas.hinzufuegen(apC);
		resCanvas.hinzufuegen(apZ);

		// -----------------------------TestDaten-------------------------------
		// -------------------------------ENDE--------------------------------
		// ---------------------------------------------------------------------
	}

	private static void erstelleTestDaten2_6(ResCanvas resCanvas) {
		// ---------------------------------------------------------------------
		// -----------------------------TestDaten-------------------------------
		// -------------------------------Anfang--------------------------------

		Arbeitspaket apA = new Arbeitspaket("A", 1, 6, 3, 8, 6, 4, 24);
		Arbeitspaket apB = new Arbeitspaket("B", 7, 13, 10, 16, 7, 3, 21);
		Arbeitspaket apC = new Arbeitspaket("C", 9, 16, 14, 21, 8, 2, 16);
		Arbeitspaket apD = new Arbeitspaket("D", 14, 18, 17, 17, 5, 2, 10);
		Arbeitspaket apE = new Arbeitspaket("E", 17, 22, 21, 26, 6, 3, 18);
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

		Arbeitspaket apA = new Arbeitspaket("A", 1, 6, 3, 8, 6, 4, 24);
		Arbeitspaket apB = new Arbeitspaket("B", 7, 13, 10, 16, 7, 3, 21);
		Arbeitspaket apC = new Arbeitspaket("C", 9, 16, 14, 21, 8, 2, 16);
		Arbeitspaket apD = new Arbeitspaket("D", 11, 15, 14, 18, 5, 2, 10);
		Arbeitspaket apE = new Arbeitspaket("E", 17, 22, 21, 26, 6, 3, 18);
		Arbeitspaket apZ = new Arbeitspaket("Z", 10, 12, 10, 12, 3, 2, 6);

		resCanvas.hinzufuegen(apB);
		resCanvas.hinzufuegen(apD);
		resCanvas.hinzufuegen(apA);
		resCanvas.hinzufuegen(apE);
		resCanvas.hinzufuegen(apC);
		resCanvas.hinzufuegen(apZ);

		// -----------------------------TestDaten-------------------------------
		// -------------------------------ENDE--------------------------------
		// ---------------------------------------------------------------------
	}

	private static void erstelleTestDaten2_7(ResCanvas resCanvas) {
		// ---------------------------------------------------------------------
		// -----------------------------TestDaten-------------------------------
		// -------------------------------Anfang--------------------------------

		Arbeitspaket apA = new Arbeitspaket("A", 1, 6, 3, 8, 6, 4, 24);
		Arbeitspaket apB = new Arbeitspaket("B", 7, 13, 10, 16, 7, 3, 21);
		Arbeitspaket apC = new Arbeitspaket("C", 9, 16, 14, 21, 8, 2, 16);
		Arbeitspaket apD = new Arbeitspaket("D", 11, 15, 14, 18, 5, 2, 10);
		Arbeitspaket apE = new Arbeitspaket("E", 17, 22, 21, 26, 6, 3, 18);
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

	private static void erstellePruefungsAufgabe(ResCanvas resCanvas) {
		// ---------------------------------------------------------------------
		// -----------------------------TestDaten-------------------------------
		// -------------------------------Anfang--------------------------------

		Arbeitspaket apA = new Arbeitspaket("A", 1, 6, 1, 6, 6, 4, 24);
		Arbeitspaket apB = new Arbeitspaket("B", 7, 10, 7, 10, 4, 2, 8);
		Arbeitspaket apC = new Arbeitspaket("C", 8, 15, 8, 15, 8, 3, 24);
		Arbeitspaket apD = new Arbeitspaket("D", 13, 15, 15, 17, 3, 4, 12);

		resCanvas.hinzufuegen(apB);
		resCanvas.hinzufuegen(apD);
		resCanvas.hinzufuegen(apA);

		resCanvas.hinzufuegen(apC);
		// resCanvas.hinzufuegen(apZ);

		// -----------------------------TestDaten-------------------------------
		// -------------------------------ENDE--------------------------------
		// ---------------------------------------------------------------------
	}

	private static void erstellePruefungsAufgabeMitE(ResCanvas resCanvas) {
		// ---------------------------------------------------------------------
		// -----------------------------TestDaten-------------------------------
		// -------------------------------Anfang--------------------------------

		Arbeitspaket apA = new Arbeitspaket("A", 1, 6, 1, 6, 6, 4, 24);
		Arbeitspaket apB = new Arbeitspaket("B", 7, 10, 7, 10, 4, 2, 8);
		Arbeitspaket apC = new Arbeitspaket("C", 8, 15, 8, 15, 8, 3, 24);
		Arbeitspaket apD = new Arbeitspaket("D", 13, 15, 15, 17, 3, 4, 12);
		Arbeitspaket apE = new Arbeitspaket("E", 16, 19, 17, 20, 4, 4, 16);

		resCanvas.hinzufuegen(apB);
		resCanvas.hinzufuegen(apD);
		resCanvas.hinzufuegen(apA);
		resCanvas.hinzufuegen(apC);
		resCanvas.hinzufuegen(apE);

		// -----------------------------TestDaten-------------------------------
		// -------------------------------ENDE--------------------------------
		// ---------------------------------------------------------------------
	}

	private static void erstellePruefungsAufgabeMitEBlosKleiner(ResCanvas resCanvas) {
		// ---------------------------------------------------------------------
		// -----------------------------TestDaten-------------------------------
		// -------------------------------Anfang--------------------------------

		Arbeitspaket apA = new Arbeitspaket("A", 1, 6, 1, 6, 6, 4, 24);
		Arbeitspaket apB = new Arbeitspaket("B", 7, 10, 7, 10, 4, 2, 8);
		Arbeitspaket apC = new Arbeitspaket("C", 8, 15, 8, 15, 8, 3, 24);
		Arbeitspaket apD = new Arbeitspaket("D", 13, 15, 15, 17, 3, 4, 12);
		Arbeitspaket apE = new Arbeitspaket("E", 16, 16, 17, 17, 1, 4, 4);

		resCanvas.hinzufuegen(apB);
		resCanvas.hinzufuegen(apD);
		resCanvas.hinzufuegen(apA);
		resCanvas.hinzufuegen(apC);
		resCanvas.hinzufuegen(apE);

		// -----------------------------TestDaten-------------------------------
		// -------------------------------ENDE--------------------------------
		// ---------------------------------------------------------------------
	}

}
