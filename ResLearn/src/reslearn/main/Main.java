package reslearn.main;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;

import reslearn.model.canvas.Canvas;
import reslearn.model.paket.Arbeitspaket;
import reslearn.model.paket.ComperatorErsteSchrittModus;
import reslearn.model.paket.ResEinheit;
import reslearn.model.paket.Teilpaket;

public class Main {

	public static void main(String[] args) {
		Canvas canvas = new Canvas();

		Arbeitspaket apA = new Arbeitspaket("A", 1, 2, 3, 4, 2, 3, 6);
		Arbeitspaket apB = new Arbeitspaket("B", 1, 2, 4, 4, 2, 4, 8);
		Arbeitspaket apC = new Arbeitspaket("C", 3, 5, 5, 7, 2, 3, 6);

		canvas.hinzufuegen(apB);
		canvas.hinzufuegen(apC);
		canvas.hinzufuegen(apA);

		ArrayList<Arbeitspaket> arbeitspaketListe = canvas.getAktuellerZustand().getArbeitspaketListe();

		Collections.sort(arbeitspaketListe, new ComperatorErsteSchrittModus());

		var koordinantenSystem = canvas.getKoordinantenSystem();
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
				int x = arbeitspaket.getFaz() - 1;
				int y = 0;
				boolean gefunden = false;
				for (int posY = y; posY < koordinantenSystem[0].length; posY++) {
					if (koordinantenSystem[x][posY] == null) {
						gefunden = true;
						y = posY;
						break;
					}
					if (gefunden == true) {
						break;
					}
				}
				var resEinheitenListe = teilpaket.getResEinheitListe();
				Iterator<ResEinheit> it = resEinheitenListe.iterator();

				for (int mitarbeiter = y; mitarbeiter < y + arbeitspaket.getMitarbeiteranzahl(); mitarbeiter++) {
					for (int tag = x; tag < arbeitspaket.getSez(); tag++) {
						if (koordinantenSystem[tag][mitarbeiter] == null) {
							if (it.hasNext()) {
								koordinantenSystem[tag][mitarbeiter] = it.next();
							}
						}
					}
				}
			} while (size != teilpaketListe.size());

			// for()
		}
		// falls nicht 0
		// schau dir an an was der Vektor des letzten Reseinheit rechts unten
		// nimm den und vergieb Vektoren entsprechend

		// Vergeben wir anhhand faz die doppel for schleife

		for (int i = koordinantenSystem.length - 1; i >= 0; i--) {
			System.out.println();
			for (int j = 0; j < koordinantenSystem[i].length - 1; j++) {
				if (koordinantenSystem[i][j] == null) {
					System.out.print(".");
				} else {
					System.out.print(koordinantenSystem[i][j].getTeilpaket().getArbeitspaket().getId());
				}
			}
		}
	}

}

/*
 * Array falsch umgesetzt: Umsetzten wie hier
 */
// TODO Festwerte für Hohe Länge von 2-D-Array festlegen
// https://
// stackoverflow.com/questions/12231453/syntax-for-creating-a-two-dimensional-array
// public int setzeStein(Spielstein stein, int spalte) {
// int i = -1;
// if (this.spielbrett[0][spalte - 1] == Spielstein.LEER) {
// for (i = this.hoehe - 1; i >= 0; i--) {
// if (this.spielbrett[i][spalte - 1] == Spielstein.LEER) {
// this.spielbrett[i][spalte - 1] = stein;
// break;
// }
// }
// }
// return i;
// }