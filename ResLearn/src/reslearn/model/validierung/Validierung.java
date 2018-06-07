package reslearn.model.validierung;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import reslearn.gui.ResFeld;
import reslearn.model.paket.Arbeitspaket;
import reslearn.model.paket.ResEinheit;
import reslearn.model.paket.Teilpaket;
import reslearn.model.validierung.Feedback.MsgType;

public class Validierung {
	private ResFeld[][] koordinatenSystem;
	private ArrayList<Feedback> feedbackListe;
	private ArrayList<Arbeitspaket> arbeitspaketeGeprueft;
	private Map<Arbeitspaket, Integer> arbeitspaketAktuelleVorgangsdauer;
	ArrayList<Arbeitspaket> alleArbeitspakete = new ArrayList<Arbeitspaket>();

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
		for (ResFeld[] zeile : koordinatenSystem) {
			for (ResFeld resFeld : zeile) {
				if (resFeld != null) {
					pruefeFAZ(resFeld.getResEinheit());
					pruefeFEZ(resFeld.getResEinheit());
				}
			}
		}
		for (int x = 0; x < koordinatenSystem[0].length; x++) {
			// arbeitspaketeGeprueft muss nach jeder Spalte neu initialisiert
			// werden damit naechste Spalte richtig ueberprueft werden kann
			arbeitspaketeGeprueft = new ArrayList<Arbeitspaket>();

			for (int y = koordinatenSystem.length - 1; y >= 0; y--) {
				if (koordinatenSystem[y][x] != null) {
					if (!arbeitspaketeGeprueft
							.contains(koordinatenSystem[y][x].getResEinheit().getTeilpaket().getArbeitspaket())) {
						pruefeMitarbeiterParallelArbeitspaket(koordinatenSystem[y][x].getResEinheit());
					}
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
		for (ResFeld[] zeile : koordinatenSystem) {

			arbeitspaketAktuelleVorgangsdauer = new HashMap<Arbeitspaket, Integer>();

			for (ResFeld resFeld : zeile) {
				if (resFeld != null) {
					pruefeFAZ(resFeld.getResEinheit());
					pruefeGrenzeKapazitaetUeberschritten(resFeld.getResEinheit(), grenzeMitarbeiterParallel);
					pruefeVorgangsdauerUeberschritten(resFeld.getResEinheit());
					pruefeVorgangsunterbrechungTeil1(resFeld.getResEinheit());
				}
			}
		}
		pruefeVorgangsunterbrechungTeil2();

		for (int x = 0; x < koordinatenSystem[0].length; x++) {
			// arbeitspaketeGeprueft muss nach jeder Spalte neu initialisiert werden,
			// damit naechste Spalte richtig ueberprueft werden kann
			arbeitspaketeGeprueft = new ArrayList<Arbeitspaket>();

			for (int y = koordinatenSystem.length - 1; y >= 0; y--) {
				if (koordinatenSystem[y][x] != null) {
					if (!arbeitspaketeGeprueft
							.contains(koordinatenSystem[y][x].getResEinheit().getTeilpaket().getArbeitspaket())) {
						pruefeMitarbeiterParallelArbeitspaket(koordinatenSystem[y][x].getResEinheit());
					}
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
		for (ResFeld[] zeile : koordinatenSystem) {
			// arbeitspaketAktuelleVorgangsdauer und arbeitspaketListeVorgangsunterbrechung
			// und der Iterator müssen nach jeder Zeile neu initialisiert werden,
			// damit naechste Zeile richtig ueberprueft werden kann.
			arbeitspaketAktuelleVorgangsdauer = new HashMap<Arbeitspaket, Integer>();

			for (ResFeld resFeld : zeile) {
				if (resFeld != null) {
					pruefeFAZ(resFeld.getResEinheit());
					pruefeSEZ(resFeld.getResEinheit());
					pruefeVorgangsdauerUeberschritten(resFeld.getResEinheit());
					pruefeVorgangsunterbrechungTeil1(resFeld.getResEinheit());
				}
			}
		}
		pruefeVorgangsunterbrechungTeil2();
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
	 * Hier wird für jede ResEinheit das dazugehöroge Arbeitspaket gesucht und wenn
	 * das Arbeitspaket noch nicht vorhanden ist, der alleArbeitspakete Arraylist
	 * hinzugefügt.
	 *
	 * @param resEinheit
	 */

	private void pruefeVorgangsunterbrechungTeil1(ResEinheit resEinheit) {
		// XKoordinaten von allen Teilpaketen holen
		// in aufsteigende Reihenfolge bringen
		// wenns durchzählen scheitert --> Vorgangsunterbrechung
		if (resEinheit.getTeilpaket().getArbeitspaket() != null) {
			if (!alleArbeitspakete.contains(resEinheit.getTeilpaket().getArbeitspaket())) {
				alleArbeitspakete.add(resEinheit.getTeilpaket().getArbeitspaket());
			}
		}

	}

	/**
	 * Hier wird für jedes Arbeitspaket alle ResEinheiten des Arbeitspaketes
	 * durchlaufen und dann in aufsteigender Reihenfolge sortiert und dann wird auf
	 * eine Lücke in der Reihenfolge überprüft und beim Auftreten einer Lücke eine
	 * Fehlermeldung zurückgegeben.
	 */
	public void pruefeVorgangsunterbrechungTeil2() {
		ArrayList<Integer> alleXKoordinatenOhneDuplikate = new ArrayList<Integer>();
		ArrayList<ResEinheit> alleResEinheiten = new ArrayList<ResEinheit>();
		ArrayList<Teilpaket> alleTeilpakete = new ArrayList<Teilpaket>();
		for (int i = 0; i < alleArbeitspakete.size(); i++) {
			for (int l = 0; l < alleArbeitspakete.get(i).getTeilpaketListe().size(); l++) {
				alleTeilpakete.add(alleArbeitspakete.get(i).getTeilpaketListe().get(l));
			}

			for (int g = 0; g < alleTeilpakete.size(); g++) {
				for (int h = 0; h < alleTeilpakete.get(g).getResEinheitListe().size(); h++) {
					alleResEinheiten.add(alleTeilpakete.get(g).getResEinheitListe().get(h));
				}
			}
			for (int j = 0; j < alleResEinheiten.size(); j++) {
				if (!alleXKoordinatenOhneDuplikate.contains(alleResEinheiten.get(j).getPosition().getxKoordinate())) {
					alleXKoordinatenOhneDuplikate.add(alleResEinheiten.get(j).getPosition().getxKoordinate());
				}
			}
			Collections.sort(alleXKoordinatenOhneDuplikate);

			int k = alleXKoordinatenOhneDuplikate.get(0);
			for (int counter = 0; counter <= alleXKoordinatenOhneDuplikate.size() - 1; counter++) {
				if (k != alleXKoordinatenOhneDuplikate.get(counter)) {
					String message = "Vorgang ab der Spalte " + alleXKoordinatenOhneDuplikate.get(counter)
							+ " unterbrochen.\n";
					feedbackListe.add(new Feedback(message, MsgType.ERROR, alleResEinheiten.get(0)));
					break;
				}
				k++;
			}
			alleXKoordinatenOhneDuplikate.clear();
			alleResEinheiten.clear();
			alleTeilpakete.clear();
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
	 */
	private void pruefeMitarbeiterParallelArbeitspaket(ResEinheit resEinheit) {
		Arbeitspaket arbeitspaket = resEinheit.getTeilpaket().getArbeitspaket();
		int xPos = resEinheit.getPosition().getxKoordinate();
		int yPos = resEinheit.getPosition().getyKoordinate();
		int mitarbeiterParallelArbeitspaket = arbeitspaket.getMitarbeiteranzahl();
		int mitarbeiterParallelCount = 0;

		for (int y = yPos; y >= 0; y--) {

			if (koordinatenSystem[y][xPos] == null) {
				break;
			} else {

				ResEinheit oben = koordinatenSystem[y][xPos].getResEinheit();
				if (arbeitspaket == oben.getTeilpaket().getArbeitspaket()) {
					mitarbeiterParallelCount++;
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
}