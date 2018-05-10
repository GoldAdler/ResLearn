package reslearn.model.validierung;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import reslearn.model.paket.Arbeitspaket;
import reslearn.model.paket.ResEinheit;
import reslearn.model.validierung.Feedback.MsgType;

public class Validierung {
	private ResEinheit[][] koordinatenSystem;
	private ArrayList<Feedback> feedbackListe;
	private ArrayList<Arbeitspaket> arbeitspaketeGeprueft;
	private ArrayList<Arbeitspaket> arbeitspaketListeVorgangsunterbrechung;
	private Map<Arbeitspaket, Integer> arbeitspaketAktuelleVorgangsdauer;
	private int iterator;

	public Validierung(ResEinheit[][] koordinatenSystem) {
		this.koordinatenSystem = koordinatenSystem;
		feedbackListe = new ArrayList<Feedback>();
	}

	public ArrayList<Feedback> AlgoErsterSchritt() {
		// TODO: Herunterfallen-Methode aufrufen! (Geht aber nur, wenn hier im
		// Konstruktor statt dem Koordinatensystem die Instanz des ResCanvas uebergeben
		// wird)

		for (ResEinheit[] zeile : koordinatenSystem) {
			for (ResEinheit resEinheit : zeile) {
				pruefeFAZ(resEinheit);
				pruefeFEZ(resEinheit);
			}

		}
		for (int x = 0; x < koordinatenSystem[0].length; x++) {
			// Muss nach jeder Spalte neu initialisiert werden,
			// damit naechste Spalte richtig ueberprueft werden kann
			arbeitspaketeGeprueft = new ArrayList<Arbeitspaket>();

			for (int y = koordinatenSystem.length - 1; y >= 0; y--) {
				if (!arbeitspaketeGeprueft.contains(koordinatenSystem[y][x].getTeilpaket().getArbeitspaket())) {
					pruefeMitarbeiterParallelArbeitspaket(koordinatenSystem[y][x]);
				}
			}

		}

		if (feedbackListe.isEmpty()) {
			feedbackListe.add(new Feedback("Alles in Ordnung!", MsgType.INFO));
		}

		return feedbackListe;
	}

	public ArrayList<Feedback> AlgoKapazitaetstreu(int grenzeMitarbeiterParallel) {
		// TODO: Herunterfallen-Methode aufrufen! (Geht aber nur, wenn hier im
		// Konstruktor statt dem Koordinatensystem die Instanz des ResCanvas uebergeben
		// wird)

		for (ResEinheit[] zeile : koordinatenSystem) {
			// Muss nach jeder Zeile neu initialisiert werden,
			// damit naechste Zeile richtig ueberprueft werden kann
			arbeitspaketAktuelleVorgangsdauer = new HashMap<Arbeitspaket, Integer>();
			arbeitspaketListeVorgangsunterbrechung = new ArrayList<Arbeitspaket>();
			iterator = 0;
			pruefeVorgangsunterbrechung(zeile);

			for (ResEinheit resEinheit : zeile) {
				pruefeFAZ(resEinheit);
				pruefeSEZ(resEinheit);
				pruefeGrenzeKapazitaetUeberschritten(resEinheit, grenzeMitarbeiterParallel);
				pruefeVorgangsdauerUeberschritten(resEinheit);
			}
		}
		for (int x = 0; x < koordinatenSystem[0].length; x++) {
			// Muss nach jeder Spalte neu initialisiert werden,
			// damit naechste Spalte richtig ueberprueft werden kann
			arbeitspaketeGeprueft = new ArrayList<Arbeitspaket>();

			for (int y = koordinatenSystem.length - 1; y >= 0; y--) {
				if (!arbeitspaketeGeprueft.contains(koordinatenSystem[y][x].getTeilpaket().getArbeitspaket())) {
					pruefeMitarbeiterParallelArbeitspaket(koordinatenSystem[y][x]);
				}
			}
		}

		if (feedbackListe.isEmpty()) {
			feedbackListe.add(new Feedback("Alles in Ordnung!", MsgType.INFO));
		}

		return feedbackListe;
	}

	private void pruefeFAZ(ResEinheit resEinheit) {
		int faz = resEinheit.getTeilpaket().getArbeitspaket().getFaz();
		int xPos = resEinheit.getPosition().getxKoordinate();
		int yPos = resEinheit.getPosition().getyKoordinate();
		if (xPos + 1 < faz) {
			String message = "FAZ am Punkt (" + xPos + ", " + (koordinatenSystem.length - 1 - yPos) + ") verletzt!\n"
					+ "Das Paket muss nach rechts verschoben werden.";
			feedbackListe.add(new Feedback(message, MsgType.ERROR, resEinheit));
		}
	}

	private void pruefeFEZ(ResEinheit resEinheit) {
		int fez = resEinheit.getTeilpaket().getArbeitspaket().getFez();
		int xPos = resEinheit.getPosition().getxKoordinate();
		int yPos = resEinheit.getPosition().getyKoordinate();
		if (xPos + 1 > fez) {
			String message = "FEZ am Punkt (" + xPos + ", " + (koordinatenSystem.length - 1 - yPos) + ") verletzt!\n"
					+ "Das Paket muss nach links verschoben werden.";
			feedbackListe.add(new Feedback(message, MsgType.ERROR, resEinheit));
		}
	}

	private void pruefeSEZ(ResEinheit resEinheit) {
		int sez = resEinheit.getTeilpaket().getArbeitspaket().getSez();
		int xPos = resEinheit.getPosition().getxKoordinate();
		int yPos = resEinheit.getPosition().getyKoordinate();
		if (xPos + 1 > sez) {
			String message = "SEZ am Punkt (" + xPos + ", " + (koordinatenSystem.length - 1 - yPos) + ") verletzt!\n"
					+ "Das Paket muss nach links verschoben werden.";
			feedbackListe.add(new Feedback(message, MsgType.ERROR, resEinheit));
		}
	}

	private void pruefeGrenzeKapazitaetUeberschritten(ResEinheit resEinheit, int grenzeMitarbeiterParallel) {
		int xPos = resEinheit.getPosition().getxKoordinate();
		int yPos = resEinheit.getPosition().getyKoordinate();
		int yPosReal = koordinatenSystem.length - 1 - yPos;
		if (yPosReal + 1 > grenzeMitarbeiterParallel) {
			String message = "Kapazitaetsgrenze am Punkt (" + xPos + ", " + yPosReal + ") verletzt!\n"
					+ "Das Paket muss nach unten verschoben werden.";
			feedbackListe.add(new Feedback(message, MsgType.ERROR, resEinheit));
		}
	}

	private void pruefeVorgangsdauerUeberschritten(ResEinheit resEinheit) {
		Arbeitspaket arbeitspaket = resEinheit.getTeilpaket().getArbeitspaket();
		int xPos = resEinheit.getPosition().getxKoordinate();
		int yPos = resEinheit.getPosition().getyKoordinate();
		if (!arbeitspaketAktuelleVorgangsdauer.containsKey(arbeitspaket)) {
			arbeitspaketAktuelleVorgangsdauer.put(arbeitspaket, 0);
		}
		int value = arbeitspaketAktuelleVorgangsdauer.get(arbeitspaket);
		arbeitspaketAktuelleVorgangsdauer.put(arbeitspaket, ++value);

		if (arbeitspaketAktuelleVorgangsdauer.get(arbeitspaket) > arbeitspaket.getVorgangsdauer()) {
			String message = "Vorgangsdauer am Punkt (" + xPos + ", " + (koordinatenSystem.length - 1 - yPos)
					+ ") überschritten!\n" + "Die Vorgangsdauer des Pakets muss verkürzt werden.";
			feedbackListe.add(new Feedback(message, MsgType.ERROR, resEinheit));
		}
	}

	private void pruefeVorgangsunterbrechung(ResEinheit[] zeile) {
		int zeilenLaenge = zeile.length - 1;
		Arbeitspaket aktuellesArbeitspaket = zeile[iterator].getTeilpaket().getArbeitspaket();

		if (arbeitspaketListeVorgangsunterbrechung.contains(aktuellesArbeitspaket)) {
			if (iterator != zeilenLaenge) {
				iterator++;
				pruefeVorgangsunterbrechung(zeile);
			}
			return;
		}
		boolean unterbrochen = false;
		for (int i = iterator + 1; i < aktuellesArbeitspaket.getSez(); i++) {
			if (aktuellesArbeitspaket != zeile[iterator].getTeilpaket().getArbeitspaket()) {
				unterbrochen = true;
			}
			if (aktuellesArbeitspaket == zeile[iterator].getTeilpaket().getArbeitspaket() && unterbrochen) {
				String message = "Vorgang am Punkt (" + zeile[i].getPosition().getxKoordinate() + ", "
						+ (koordinatenSystem.length - 1 - zeile[i].getPosition().getyKoordinate()) + ") unterbrochen!\n"
						+ "Der Vorgang eines Paketes darf nicht unterbrochen werden.";
				feedbackListe.add(new Feedback(message, MsgType.ERROR, zeile[iterator]));
			}
		}
		arbeitspaketListeVorgangsunterbrechung.add(aktuellesArbeitspaket);
		if (iterator != zeilenLaenge) {
			iterator++;
			pruefeVorgangsunterbrechung(zeile);
		}
	}

	private void pruefeMitarbeiterParallelArbeitspaket(ResEinheit resEinheit) {
		Arbeitspaket arbeitspaket = resEinheit.getTeilpaket().getArbeitspaket();
		int xPos = resEinheit.getPosition().getxKoordinate();
		int yPos = resEinheit.getPosition().getyKoordinate();
		int mitarbeiterParallelArbeitspaket = arbeitspaket.getMitarbeiteranzahl();
		int mitarbeiterParallelCount = 1;

		for (int y = yPos; y >= 0; y--) {

			ResEinheit oben = koordinatenSystem[y][xPos];
			if (arbeitspaket == oben.getTeilpaket().getArbeitspaket()) {
				mitarbeiterParallelCount++;
			}

			// TODO: Dead-Code: falls statt oben hier koordinatenSystem[y][xPos] steht,
			// gehts
			if (oben == null) {
				break;
			}
		}

		if (mitarbeiterParallelCount > mitarbeiterParallelArbeitspaket) {
			String message = "Zahl der maximal parallel arbeitenden Mitarbeiter am Punkt (" + xPos + ", "
					+ (koordinatenSystem.length - 1 - yPos) + ") verletzt!\n"
					+ "Die maximal erlaubte Mitarbeiterzahl beträgt " + mitarbeiterParallelArbeitspaket + ".";
			feedbackListe.add(new Feedback(message, MsgType.ERROR, resEinheit));
		}
	}
}