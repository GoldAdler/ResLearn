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
	 * Benennung des Arbeitspakets
	 */
	private String id;
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

	public Arbeitspaket(String id, int faz, int fez, int saz, int sez, int vorgangsdauer, int mitarbeiteranzahl,
			int aufwand) {
		super(vorgangsdauer, mitarbeiteranzahl, aufwand);
		this.id = id;
		this.faz = faz;
		this.saz = saz;
		this.fez = fez;
		this.sez = sez;
		teilpaketListe = new ArrayList<Teilpaket>();
		teilpaketListe.add(new Teilpaket(this));
	}

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
	public ResEinheit[][] reset(ResCanvas resCanvas) {
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

		vereint.setPosition(new Vektor2i(0, 0));

		ArrayList<ResEinheit> resEinheitenListe = vereint.getResEinheitListe();
		Iterator<ResEinheit> it = resEinheitenListe.iterator();

		ResEinheit[][] koordinatenSystem = resCanvas.getKoordinatenSystem();

		for (int y = this.mitarbeiteranzahl - 1; y >= 0; y--) {
			for (int x = 0; x < this.vorgangsdauer; x++) {
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

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Arbeitspaket copy() {

		Arbeitspaket copyArbeitsPaket = new Arbeitspaket(this.id, this.faz, this.fez, this.saz, this.sez,
				this.vorgangsdauer, this.mitarbeiteranzahl, this.aufwand);

		ArrayList<Teilpaket> neueTeilpaketListe = new ArrayList<Teilpaket>();

		for (Teilpaket tp : this.teilpaketListe) {
			neueTeilpaketListe.add(tp.copy(copyArbeitsPaket));
		}

		copyArbeitsPaket.setTeilpaketListe(neueTeilpaketListe);

		return copyArbeitsPaket;

	}

}