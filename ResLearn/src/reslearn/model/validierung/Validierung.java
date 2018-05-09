package reslearn.model.validierung;

import java.util.ArrayList;

import reslearn.model.paket.Arbeitspaket;
import reslearn.model.paket.ResEinheit;
import reslearn.model.validierung.Feedback.MsgType;

public class Validierung {
	private ResEinheit[][] koordinatenSystem;
	private ArrayList<Feedback> feedbackListe;
	private ArrayList<Arbeitspaket> arbeitspaketeGeprueft;

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