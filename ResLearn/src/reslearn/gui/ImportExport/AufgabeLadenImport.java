package reslearn.gui.ImportExport;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

import reslearn.model.paket.Arbeitspaket;

public class AufgabeLadenImport {

	public Arbeitspaket[] aufgabeLaden(String dateipfad) {
		Arbeitspaket[] paketeArray = null;
		try {

			CsvReader arbeitspaketImport = new CsvReader(dateipfad);

			arbeitspaketImport.readHeaders();
			ArrayList<Arbeitspaket> pakete = new ArrayList<Arbeitspaket>();

			while (arbeitspaketImport.readRecord()) {

				Arbeitspaket ap = new Arbeitspaket();

				try {
					String zeile = arbeitspaketImport.get(0);
					String[] spalten = zeile.split(";");

					if (spalten.length == 8) {
						ap.setId(spalten[0]);
						ap.setFaz(Integer.valueOf(spalten[1]));
						ap.setFez(Integer.valueOf(spalten[2]));
						ap.setSaz(Integer.valueOf(spalten[3]));
						ap.setSez(Integer.valueOf(spalten[4]));
						ap.setVorgangsdauer(Integer.valueOf(spalten[5]));
						ap.setMitarbeiteranzahl(Integer.valueOf(spalten[6]));
						ap.setAufwand(Integer.valueOf(spalten[7]));
					}

					pakete.add(ap);
				} catch (NumberFormatException e) {
					e.printStackTrace();
				}
			}

			arbeitspaketImport.close();

			paketeArray = getArbeitspaketArray(pakete);

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return paketeArray;
	}

	public Arbeitspaket[] getArbeitspaketArray(ArrayList<Arbeitspaket> pakete) {
		Arbeitspaket arbeitspakete[] = new Arbeitspaket[pakete.size()];
		int i = 0;
		for (Arbeitspaket ap : pakete) {
			arbeitspakete[i++] = ap;
		}
		return arbeitspakete;
	}
}
