package reslearn.model.paket;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;

import reslearn.model.algorithmus.Algorithmus;
import reslearn.model.resCanvas.ResCanvas;
import reslearn.model.utils.ComperatorTeilpaket;
import reslearn.model.utils.Vektor2i;

public class Arbeitspaket extends Paket {

	/**
	 * Bennenung des Arbeitspaktes (für interne Berechnungen durchlaufend von A bis
	 * Z)
	 */
	private String idIntern;

	/**
	 * Benennung des Arbeitspakets (externn, vom User), wird auch in der Legende
	 * angezeigt
	 */
	private String idExtern;

	/**
	 * Fruehester Anfangszeitpunkt
	 */
	private int faz;
	/**
	 * Spaetester Anfangszeitpunkt
	 */
	private int saz;
	/**
	 * Fruehester Endzeitpunkt
	 */
	private int fez;
	/**
	 * Spaetester Endzeitpunkt
	 */
	private int sez;

	private ArrayList<Teilpaket> teilpaketListe;

	public Arbeitspaket() {

	}

	public Arbeitspaket(String idIntern, int faz, int fez, int saz, int sez, int vorgangsdauer, int mitarbeiteranzahl,
			int aufwand) {
		super(vorgangsdauer, mitarbeiteranzahl, aufwand);
		this.idIntern = idIntern;
		this.faz = faz;
		this.saz = saz;
		this.fez = fez;
		this.sez = sez;
		teilpaketListe = new ArrayList<Teilpaket>();
		teilpaketListe.add(new Teilpaket(this));
	}

	/**
	 * Fügt Teilpakete eines Arbeitspaketes die nebeneinander liegen, zu einem
	 * Teilpaket zusammen.
	 *
	 */
	public void zusammenfuegen() {

		if (teilpaketListe.size() > 1) {

			Collections.sort(this.teilpaketListe, new ComperatorTeilpaket());

			int iterator = 0;
			boolean zusammengefuehrt = false;

			while (iterator < teilpaketListe.size()) {
				if (iterator + 1 >= teilpaketListe.size()) {
					break;
				}
				zusammengefuehrt = teilpaketListe.get(iterator).zusammenfuehren(teilpaketListe.get(iterator + 1));
				if (!zusammengefuehrt) {
					iterator++;
				}
			}
		}
	}

	/**
	 * Überprüft ob eine Vorgangsunterbrechung im Arbeitspaket stattfindet. Wenn ja,
	 * wird diese Behoben, in dem das ganze Arbeitspaket gelöscht wird und hinten im
	 * Koordinatensystem eingefügt wird.
	 *
	 * @param resCanvas
	 */
	public void ueberpruefeVorgangsunterbrechung(ResCanvas resCanvas) {
		Collections.sort(teilpaketListe, new ComperatorTeilpaket());
		int xEnde = 0;
		for (Teilpaket tp : teilpaketListe) {
			int xStart = tp.getResEinheitListe().get(0).getPosition().getxKoordinate();

			if (xStart != xEnde + 1 && xEnde != 0) {
				resCanvas.entferneArbeitspaket(tp.getArbeitspaket());
				resCanvas.herunterfallenAlleTeilpakete();
				resCanvas.aufschliessenTeilpaket();
				ResEinheit[][] koordinatensystem = resCanvas.getKoordinatenSystem();
				int abstand = 0;
				for (int x = 0; x < ResCanvas.koorBreite; x++) {
					if (koordinatensystem[ResCanvas.koorHoehe - 1][x] == null) {
						abstand = x - tp.getArbeitspaket().getTeilpaketListe().get(0).getResEinheitListe().get(0)
								.getPosition().getxKoordinate();
						break;
					}

				}

				tp.getArbeitspaket().neuSetzenKapa(abstand, resCanvas);
				break;
			}
			xEnde = xStart + tp.getVorgangsdauer() - 1;

		}

		Algorithmus.ausgeben(resCanvas.getKoordinatenSystem());

	}

	/**
	 * Überprüft ob eine Vorgangsunterbrechung im Arbeitspaket stattfindet. Wenn ja,
	 * wird true zurückgegeben.
	 *
	 * @return
	 */
	public boolean ueberpruefeVorgangsunterbrechungSimulation() {

		Collections.sort(teilpaketListe, new ComperatorTeilpaket());
		int xEnde = 0;
		for (Teilpaket tp : teilpaketListe) {
			int xStart = tp.getResEinheitListe().get(0).getPosition().getxKoordinate();

			if (xStart != xEnde + 1 && xEnde != 0) {
				return true;
			}

			xEnde = xStart + tp.getVorgangsdauer() - 1;
		}
		return false;
	}

	/**
	 * Die Methode nimmt einen int abstand entgegen um den das Arbeitspaket
	 * verschoben werden muss. Dabei wird dieses aus dem Koordinatensystem gelöscht
	 * und um den übergebenen versetzt eingesetzt. Dieses Einsetzten geschieht aber
	 * am obersten Rand des Koordiantensystems. Anschließend wird das Paket mit
	 * #herunterfallen an seine neue finalle Position gesenkt.
	 *
	 * Beim Löschvorgang des Arbeitspaketes werden auch die unterschiedlichen
	 * Teilpaketet, aus dem das Arbeitspaket besteht, wieder zu einem Teilpaket
	 * zusammengeführt. Sollte beim Herunterfallen es von Nöten sein neue Teilpakete
	 * anzulegen, wird dies in der Methode #herunterfallen durchgeführt.
	 *
	 * @param abstand
	 * @param resCanvas
	 */
	public void neuSetzenKapa(int abstand, ResCanvas resCanvas) {
		Teilpaket ersteTP = teilpaketListe.get(0);
		ResEinheit erstesRes = ersteTP.getResEinheitListe().get(0);

		int alteXposition = erstesRes.position.getxKoordinate();
		int neueXPosition = alteXposition + abstand;

		teilpaketListe.clear();

		Teilpaket vereint = new Teilpaket(this);
		teilpaketListe.add(vereint);

		ArrayList<ResEinheit> resEinheitenListe = vereint.getResEinheitListe();
		Iterator<ResEinheit> it = resEinheitenListe.iterator();

		ResEinheit[][] koordinatenSystem = resCanvas.getKoordinatenSystem();

		for (int y = this.mitarbeiteranzahl - 1; y >= 0; y--) {
			for (int x = neueXPosition; x < neueXPosition + this.vorgangsdauer; x++) {
				if (koordinatenSystem[y][x] == null) {
					if (it.hasNext()) {
						koordinatenSystem[y][x] = it.next();
						koordinatenSystem[y][x].setPosition(new Vektor2i(y, x));
					}
				}
			}
		}

		Algorithmus.ausgeben(koordinatenSystem);

		resCanvas.herunterfallen(vereint);

	}

	/**
	 * Der Methode wird der int start übergeben. Dieser int bestimmt die
	 * Start-X-Position in der das Arbeitspaket eingestzt wird. Dieses Einsetzten
	 * geschieht aber am obersten Rand des Koordiantensystems. Anschließend wird das
	 * Paket mit #herunterfallen an seine neue finalle Position gesenkt.
	 *
	 * @param start
	 * @param resCanvas
	 */
	public void neuSetzenTermin(int start, ResCanvas resCanvas) {

		ArrayList<ResEinheit> resEinheitenListe = this.getTeilpaketListe().get(0).getResEinheitListe();
		Iterator<ResEinheit> it = resEinheitenListe.iterator();

		ResEinheit[][] koordinatenSystem = resCanvas.getKoordinatenSystem();

		for (int y = this.mitarbeiteranzahl - 1; y >= 0; y--) {
			for (int x = start; x < start + this.vorgangsdauer; x++) {
				if (koordinatenSystem[y][x] == null) {
					if (it.hasNext()) {
						koordinatenSystem[y][x] = it.next();
						koordinatenSystem[y][x].setPosition(new Vektor2i(y, x));
					}
				}
			}
		}

		Algorithmus.ausgeben(koordinatenSystem);

		resCanvas.herunterfallen(this.getTeilpaketListe().get(0));

	}

	/**
	 * @author Team-Gui:
	 *
	 *         Beim Betätigen des reset-Buttons werden alle vorhandenen Teilpakete
	 *         des Arbeitspaktes gelöscht und ein neues Teilpaket mit den Daten des
	 *         Arbeitspaktes (Mitarbeiteranzahl, Vorgangsdauer) angelegt. Das neue
	 *         Teilpaket besteht dabei aus allen ResEinheiten der gelöschten
	 *         Teilpakete.
	 *
	 *         Das neue Teilpaket, bzw. das Arbeitspaket, wird am oberen Rand des
	 *         Koordinatensystems eingefügt.
	 *
	 * @param resCanvas
	 * @return
	 */
	public ResEinheit[][] reset(int startX, ResCanvas resCanvas) {

		ArrayList<ResEinheit> resListe = new ArrayList<>();

		resCanvas.entferneArbeitspaket(this);

		for (Teilpaket teilpaket : teilpaketListe) {
			for (ResEinheit res : teilpaket.getResEinheitListe()) {
				resListe.add(res);
			}
		}

		teilpaketListe.clear();

		Teilpaket vereint = new Teilpaket(this);
		vereint.setAufwand(this.getAufwand());
		vereint.setMitarbeiteranzahl(this.getMitarbeiteranzahl());
		vereint.setVorgangsdauer(this.getVorgangsdauer());
		teilpaketListe.add(vereint);

		vereint.setResEinheitListe(resListe);

		// vereint.setPosition(new Vektor2i(0, 0));

		ArrayList<ResEinheit> resEinheitenListe = vereint.getResEinheitListe();
		Iterator<ResEinheit> it = resEinheitenListe.iterator();

		ResEinheit[][] koordinatenSystem = resCanvas.getKoordinatenSystem();

		for (int y = this.mitarbeiteranzahl - 1; y >= 0; y--) {
			for (int x = startX; x < startX + this.vorgangsdauer; x++) {
				if (koordinatenSystem[y][x] == null) {
					if (it.hasNext()) {
						koordinatenSystem[y][x] = it.next();
						koordinatenSystem[y][x].setPosition(new Vektor2i(y, x));
						koordinatenSystem[y][x].setTeilpaket(vereint);
					}
				}
			}
		}
		return koordinatenSystem;
	}

	public void teilpaketHinzufuegen(Teilpaket teilpaket) {
		teilpaketListe.add(teilpaket);
	}

	@Override
	public void bewegen(ResCanvas resCanvas, int yMove, int xMove) {
		for (Teilpaket teilpaket : teilpaketListe) {
			teilpaket.bewegen(resCanvas, yMove, xMove);
		}
	}

	@Override
	public boolean bewegeX(ResCanvas resCanvas, int xMove) {
		for (Teilpaket teilpaket : teilpaketListe) {
			teilpaket.bewegeX(resCanvas, xMove);
		}
		return true;
	}

	@Override
	public boolean bewegeY(ResCanvas resCanvas, int yMove) {
		for (Teilpaket teilpaket : teilpaketListe) {
			teilpaket.bewegeY(resCanvas, yMove);
		}
		return true;
	}

	public void entferneTeilpaket(Teilpaket teilpaket) {
		getTeilpaketListe().remove(teilpaket);
	}

	/**
	 * Legt eine WIRKLICHE Kopie des Arbeitspaketes an.
	 *
	 * D.h. es wird nicht einfach die Referenz kopiert. Sondern ein neues, vom
	 * ursprünglichen Arbeitspaket unabhäniges Arbeitspaket, angleget.
	 *
	 * @return
	 */
	public Arbeitspaket copy() {

		Arbeitspaket copyArbeitsPaket = new Arbeitspaket(this.idIntern, this.faz, this.fez, this.saz, this.sez,
				this.vorgangsdauer, this.mitarbeiteranzahl, this.aufwand);

		ArrayList<Teilpaket> neueTeilpaketListe = new ArrayList<Teilpaket>();

		for (Teilpaket tp : this.teilpaketListe) {
			neueTeilpaketListe.add(tp.copy(copyArbeitsPaket));
		}

		copyArbeitsPaket.setTeilpaketListe(neueTeilpaketListe);

		return copyArbeitsPaket;

	}

	public int getFaz() {
		return faz;
	}

	public void setFaz(int faz) {
		this.faz = faz;
	}

	public int getSaz() {
		return saz;
	}

	public void setSaz(int saz) {
		this.saz = saz;
	}

	public int getFez() {
		return fez;
	}

	public void setFez(int fez) {
		this.fez = fez;
	}

	public int getSez() {
		return sez;
	}

	public void setSez(int sez) {
		this.sez = sez;
	}

	public ArrayList<Teilpaket> getTeilpaketListe() {
		return teilpaketListe;
	}

	public void setTeilpaketListe(ArrayList<Teilpaket> teilpaketListe) {
		this.teilpaketListe = teilpaketListe;
	}

	/**
	 * Die interne Bezeichung eines Arbeitspaktes. Wichtig für Debuggen und
	 * Konsolenausgaben. Wird vom User nicht gesehen.
	 *
	 * @return
	 */
	public String getIdIntern() {
		return idIntern;
	}

	/**
	 * Die interne Bezeichung eines Arbeitspaktes. Wichtig für Debuggen und
	 * Konsolenausgaben. Wird vom User nicht gesehen.
	 *
	 * @return
	 */
	public void setIdIntern(String id) {
		this.idIntern = id;
	}

	/**
	 * Name, der vom User eingesehen werden kann. Wird angezeigt in Tabellen und
	 * Legende.
	 *
	 * @return
	 */
	public String getArbeitspaketName() {
		return idExtern;
	}

	/**
	 * Name, der vom User eingesehen werden kann. Wird angezeigt in Tabellen und
	 * Legende.
	 *
	 * @return
	 */
	public void setArbeitspaketName(String idIntern) {
		this.idExtern = idIntern;
	}

}