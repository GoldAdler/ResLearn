package reslearn.model.validierung;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import reslearn.gui.ResFeld;
import reslearn.model.paket.Arbeitspaket;
import reslearn.model.paket.ResEinheit;
import reslearn.model.validierung.Feedback.MsgType;

// TODO: Validierung mit der GUI testen!!!
public class Validierung {
	private ResFeld[][] koordinatenSystem;
	private ArrayList<Feedback> feedbackListe;
	private ArrayList<Arbeitspaket> arbeitspaketeGeprueft;
	private ArrayList<Arbeitspaket> arbeitspaketListeVorgangsunterbrechung;
	private Map<Arbeitspaket, Integer> arbeitspaketAktuelleVorgangsdauer;
	private int iterator;

	/**
	 * In dieser Klasse werden die Lösungen, die der User in der GUI erstellt,
	 * validiert.
	 *
	 * @param koordinatenSystem
	 */
	public Validierung(ResFeld[][] koordinatenSystem) {
		this.koordinatenSystem = koordinatenSystem;
		feedbackListe = new ArrayList<Feedback>();
	}

	/**
	 * In dieser Methode wird die Lösung des ErstenSchrittModus validiert. Hierfür
	 * wird für jede ResEinheit FAZ und FEZ überprüft. Somit wurde auch automatisch
	 * geprüft, dass sowohl die Vorgangsdauer des Paketes stimmt, als auch dass
	 * keine Vorgangsunterbrechung existiert.
	 *
	 * @return ArrayList<Feedback> - Liste, in der alle gesammelten Infos bzw.
	 *         Fehler stehen.
	 */
	public ArrayList<Feedback> AlgoErsterSchritt() {
		// TODO: Herunterfallen-Methode aufrufen! (Geht aber nur, wenn hier im
		// Konstruktor statt dem Koordinatensystem die Instanz des ResCanvas uebergeben
		// wird)

		for (ResFeld[] zeile : koordinatenSystem) {
			for (ResFeld resEinheit : zeile) {
				pruefeFAZ(resEinheit.getResEinheit());
				pruefeFEZ(resEinheit.getResEinheit());
			}
		}
		for (int x = 0; x < koordinatenSystem[0].length; x++) {
			// arbeitspaketeGeprueft muss nach jeder Spalte neu initialisiert
			// werden damit naechste Spalte richtig ueberprueft werden kann
			arbeitspaketeGeprueft = new ArrayList<Arbeitspaket>();

			for (int y = koordinatenSystem.length - 1; y >= 0; y--) {
				if (!arbeitspaketeGeprueft
						.contains(koordinatenSystem[y][x].getResEinheit().getTeilpaket().getArbeitspaket())) {
					pruefeMitarbeiterParallelArbeitspaket(koordinatenSystem[y][x].getResEinheit());
				}
			}
		}
		if (feedbackListe.isEmpty()) {
			feedbackListe.add(new Feedback("Alles in Ordnung!", MsgType.INFO));
		}
		return feedbackListe;
	}

	/**
	 * In dieser Methode wird die Lösung des AlgoKapazitaetstreu validiert. Hierfür
	 * wird für jede ResEinheit FAZ, das Überschreiten der Kapazitaetsgrenze und die
	 * Einhaltung der Vorgangsdauer überprüft. Für jede Zeile wird außerdem
	 * überprüft, ob eine Vorgangsunterbrechung vorliegt.
	 *
	 * @param grenzeMitarbeiterParallel
	 * @return ArrayList<Feedback> - Liste, in der alle gesammelten Infos bzw.
	 *         Fehler stehen.
	 */
	public ArrayList<Feedback> AlgoKapazitaetstreu(int grenzeMitarbeiterParallel) {
		// TODO: Herunterfallen-Methode aufrufen! (Geht aber nur, wenn hier im
		// Konstruktor statt dem Koordinatensystem die Instanz des ResCanvas uebergeben
		// wird)

		for (ResFeld[] zeile : koordinatenSystem) {
			// arbeitspaketAktuelleVorgangsdauer und arbeitspaketListeVorgangsunterbrechung
			// und der Iterator müssen nach jeder Zeile neu initialisiert werden,
			// damit naechste Zeile richtig ueberprueft werden kann.
			arbeitspaketAktuelleVorgangsdauer = new HashMap<Arbeitspaket, Integer>();
			arbeitspaketListeVorgangsunterbrechung = new ArrayList<Arbeitspaket>();
			iterator = 0;
			pruefeVorgangsunterbrechung(zeile);

			for (ResFeld resEinheit : zeile) {
				pruefeFAZ(resEinheit.getResEinheit());
				pruefeGrenzeKapazitaetUeberschritten(resEinheit.getResEinheit(), grenzeMitarbeiterParallel);
				pruefeVorgangsdauerUeberschritten(resEinheit.getResEinheit());
			}
		}
		for (int x = 0; x < koordinatenSystem[0].length; x++) {
			// arbeitspaketeGeprueft muss nach jeder Spalte neu initialisiert werden,
			// damit naechste Spalte richtig ueberprueft werden kann
			arbeitspaketeGeprueft = new ArrayList<Arbeitspaket>();

			for (int y = koordinatenSystem.length - 1; y >= 0; y--) {
				if (!arbeitspaketeGeprueft
						.contains(koordinatenSystem[y][x].getResEinheit().getTeilpaket().getArbeitspaket())) {
					pruefeMitarbeiterParallelArbeitspaket(koordinatenSystem[y][x].getResEinheit());
				}
			}
		}
		if (feedbackListe.isEmpty()) {
			feedbackListe.add(new Feedback("Alles in Ordnung!", MsgType.INFO));
		}
		return feedbackListe;
	}

	/**
	 * In dieser Methode wird die Lösung des AlgoTermintreu validiert. Hierfür wird
	 * für jede ResEinheit FAZ, SEZ, und die Einhaltung der Vorgangsdauer überprüft.
	 * Für jede Zeile wird außerdem überprüft, ob eine Vorgangsunterbrechung
	 * vorliegt.
	 *
	 * @return ArrayList<Feedback> - Liste, in der alle gesammelten Infos bzw.
	 *         Fehler stehen.
	 */
	public ArrayList<Feedback> AlgoTermintreu() {
		// TODO: Herunterfallen-Methode aufrufen! (Geht aber nur, wenn hier im
		// Konstruktor statt dem Koordinatensystem die Instanz des ResCanvas uebergeben
		// wird)

		for (ResFeld[] zeile : koordinatenSystem) {
			// arbeitspaketAktuelleVorgangsdauer und arbeitspaketListeVorgangsunterbrechung
			// und der Iterator müssen nach jeder Zeile neu initialisiert werden,
			// damit naechste Zeile richtig ueberprueft werden kann.
			arbeitspaketAktuelleVorgangsdauer = new HashMap<Arbeitspaket, Integer>();
			arbeitspaketListeVorgangsunterbrechung = new ArrayList<Arbeitspaket>();
			iterator = 0;
			pruefeVorgangsunterbrechung(zeile);

			for (ResFeld resEinheit : zeile) {
				pruefeFAZ(resEinheit.getResEinheit());
				pruefeSEZ(resEinheit.getResEinheit());
				pruefeVorgangsdauerUeberschritten(resEinheit.getResEinheit());
			}
		}
		if (feedbackListe.isEmpty()) {
			feedbackListe.add(new Feedback("Alles in Ordnung!", MsgType.INFO));
		}
		return feedbackListe;
	}

	/**
	 * In dieser Methode prüfen wir, ob die übergebene ResEinheit im Rahmen des
	 * vorgegebenen FAZ liegt. Sollte dies nicht der Fall sein, so wird ein Feedback
	 * mit einer Fehlerbeschreibung und der betroffenenen ResEinheit an die GUI
	 * zurückgegeben.
	 *
	 * @param resEinheit
	 */
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

	/**
	 * In dieser Methode prüfen wir, ob die übergebene ResEinheit im Rahmen des
	 * vorgegebenen FEZ liegt. Sollte dies nicht der Fall sein, so wird ein Feedback
	 * mit einer Fehlerbeschreibung und der betroffenenen ResEinheit an die GUI
	 * zurückgegeben.
	 *
	 * @param resEinheit
	 */
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

	/**
	 * In dieser Methode prüfen wir, ob die übergebene ResEinheit im Rahmen des
	 * vorgegebenen SEZ liegt. Sollte dies nicht der Fall sein, so wird ein Feedback
	 * mit einer Fehlerbeschreibung und der betroffenenen ResEinheit an die GUI
	 * zurückgegeben.
	 *
	 * @param resEinheit
	 */
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

	/**
	 * Hier wird überprüft, ob die übergebene ResEinheit innerhalb der allgemein
	 * vorgegbenen Mitarbeiterkapazitätsgrenze liegt. Sollte dies nicht der Fall
	 * sein, so wird ein Feedback mit einer Fehlerbeschreibung und der betroffenenen
	 * ResEinheit an die GUI zurückgegeben.
	 *
	 * @param resEinheit
	 * @param grenzeMitarbeiterParallel
	 */
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

	/**
	 * Hier wird überprüft, ob bei dem Arbeitspaket, zu dem die übergebene
	 * ResEinheit gehört, die Vorgangsdauer überschritten wurde. Sollte dies der
	 * Fall sein, so wird ein Feedback mit einer Fehlerbeschreibung und der
	 * betroffenenen ResEinheit an die GUI zurückgegeben.
	 *
	 * @param resEinheit
	 */
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

	/**
	 * Hier wird für die übergebene Zeile des Koordinatensystems rekursiv überprüft,
	 * ob bei den darin enthaltenen ResEinheiten eine Vorgangsunterbrechung
	 * innerhalb eines Arbeitspaketes auftritt. Sollte dies der Fall sein, so wird
	 * ein Feedback mit einer Fehlerbeschreibung und der betroffenenen ResEinheit an
	 * die GUI zurückgegeben.
	 *
	 * @param zeile
	 */
	private void pruefeVorgangsunterbrechung(ResFeld[] zeile) {
		int zeilenLaenge = zeile.length - 1;
		Arbeitspaket aktuellesArbeitspaket = zeile[iterator].getResEinheit().getTeilpaket().getArbeitspaket();

		if (arbeitspaketListeVorgangsunterbrechung.contains(aktuellesArbeitspaket)) {
			if (iterator != zeilenLaenge) {
				iterator++;
				pruefeVorgangsunterbrechung(zeile);
			}
			return;
		}

		boolean unterbrochen = false;
		for (int i = iterator + 1; i < koordinatenSystem.length - 1; i++) {
			if (aktuellesArbeitspaket != zeile[iterator].getResEinheit().getTeilpaket().getArbeitspaket()) {
				unterbrochen = true;
			}
			if (aktuellesArbeitspaket == zeile[iterator].getResEinheit().getTeilpaket().getArbeitspaket()
					&& unterbrochen) {
				String message = "Vorgang am Punkt (" + zeile[i].getResEinheit().getPosition().getxKoordinate() + ", "
						+ (koordinatenSystem.length - 1 - zeile[i].getResEinheit().getPosition().getyKoordinate())
						+ ") unterbrochen!\n" + "Der Vorgang eines Paketes darf nicht unterbrochen werden.";
				feedbackListe.add(new Feedback(message, MsgType.ERROR, zeile[iterator].getResEinheit()));
			}
		}

		arbeitspaketListeVorgangsunterbrechung.add(aktuellesArbeitspaket);
		if (iterator != zeilenLaenge) {
			iterator++;
			pruefeVorgangsunterbrechung(zeile);
		}
	}

	public ArrayList<Feedback> getFeedbackListe() {
		return feedbackListe;
	}

	/**
	 * Hier wird überprüft, ob die übergebene ResEinheit innerhalb der vorgegbenen
	 * Mitarbeiterkapazitätsgrenze des Arbeitspaketes liegt. Sollte dies nicht der
	 * Fall sein, so wird ein Feedback mit einer Fehlerbeschreibung und der
	 * betroffenenen ResEinheit an die GUI zurückgegeben.
	 *
	 * @param resEinheit
	 * @param grenzeMitarbeiterParallel
	 */
	private void pruefeMitarbeiterParallelArbeitspaket(ResEinheit resEinheit) {
		Arbeitspaket arbeitspaket = resEinheit.getTeilpaket().getArbeitspaket();
		int xPos = resEinheit.getPosition().getxKoordinate();
		int yPos = resEinheit.getPosition().getyKoordinate();
		int mitarbeiterParallelArbeitspaket = arbeitspaket.getMitarbeiteranzahl();
		int mitarbeiterParallelCount = 1;

		for (int y = yPos; y >= 0; y--) {

			ResEinheit oben = koordinatenSystem[y][xPos].getResEinheit();
			if (arbeitspaket == oben.getTeilpaket().getArbeitspaket()) {
				mitarbeiterParallelCount++;
			}

			if (koordinatenSystem[y][xPos].getResEinheit() == null) {
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