package reslearn.model.validierung;

import java.util.ArrayList;

import reslearn.model.paket.ResEinheit;
import reslearn.model.validierung.Feedback.MsgType;

public class Validierung {
	private ResEinheit[][] koordinatenSystem;
	private ArrayList<Feedback> feedbackListe;

	public Validierung(ResEinheit[][] koordinatenSystem) {
		this.koordinatenSystem = koordinatenSystem;
		feedbackListe = new ArrayList<Feedback>();
	}

	public ArrayList<Feedback> AlgoErsterSchritt() {
		for (ResEinheit[] spalte : koordinatenSystem) {
			for (ResEinheit resEinheit : spalte) {
				pruefeFAZ(resEinheit);
				// TODO: pruefeFEZ()
				// pruefeFEZ(resEinheit);

			}
		}
		// TODO: Prüfe ob feedbackListe leer (keine Fehler!) ...
		return feedbackListe;
	}

	private void pruefeFAZ(ResEinheit resEinheit) {
		int faz = resEinheit.getTeilpaket().getArbeitspaket().getFaz();
		int xPos = resEinheit.getPosition().getxKoordinate();
		if (xPos + 1 < faz) {
			// TODO: Message für Feedback überlegen
			feedbackListe.add(new Feedback("", MsgType.ERROR, resEinheit));
		}
	}
}
