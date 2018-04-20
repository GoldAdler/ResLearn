package reslearn.main;

import java.util.ArrayList;
import java.util.Collections;

import reslearn.model.canvas.Canvas;
import reslearn.model.paket.Arbeitspaket;
import reslearn.model.paket.ComperatorErsteSchrittModus;

public class Main {

	public static void main(String[] args) {
		Canvas canvas = new Canvas();

		Arbeitspaket ap1 = new Arbeitspaket("Testpaket1", 1, 2, 3, 4, 2, 3, 6);
		Arbeitspaket ap1_5 = new Arbeitspaket("Testpaket1_5", 1, 2, 4, 4, 2, 4, 8);
		Arbeitspaket ap2 = new Arbeitspaket("Testpaket2", 3, 5, 5, 7, 2, 3, 6);
		Arbeitspaket ap3 = new Arbeitspaket("Testpaket3", 6, 6, 6, 6, 6, 6, 6);

		canvas.hinzufuegen(ap3);
		canvas.hinzufuegen(ap1_5);
		canvas.hinzufuegen(ap2);
		canvas.hinzufuegen(ap1);

		ArrayList<Arbeitspaket> arbeitspaketListe = canvas.getAktuellerZustand().getArbeitspaketListe();

		Collections.sort(arbeitspaketListe, new ComperatorErsteSchrittModus());

		for (int i = 0; i < arbeitspaketListe.size(); i++) {
			if (i != 0) {

			} else {
				arbeitspaketListe.get(i);
				// for()
			}
			// falls nicht 0
			// schau dir an an was der Vektor des letzten Reseinheit rechts unten
			// nimm den und vergieb Vektoren entsprechend

			// Vergeben wir anhhand faz die doppel for schleife
		}

	}

}
