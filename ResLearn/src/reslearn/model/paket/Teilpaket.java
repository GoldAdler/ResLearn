package reslearn.model.paket;

import java.util.ArrayList;
import java.util.Collections;
import java.util.ListIterator;

import reslearn.model.resCanvas.ResCanvas;
import reslearn.model.utils.ComperatorTeilpaket;
import reslearn.model.utils.ComperatorVektor2iY;
import reslearn.model.utils.Vektor2i;

public class Teilpaket extends Paket {

	/**
	 * Nur verwendet in AlgoKapa.
	 *
	 * Gibt an in welche Richtung ein Teilpaket und das zugehörige Arbeitspaket
	 * verschoben werden muss. FAZ gibt an, dass das Teilpaket an der selben Stelle
	 * bleiben, da es bereits am optimalsten Zeitpunkt liegt.
	 *
	 */
	public enum VerschiebeRichtung {
		RECHTS, LINKS, FAZ
	}

	/**
	 * Referenz auf das Arbeitspaket
	 */
	private Arbeitspaket arbeitspaket;
	private ArrayList<ResEinheit> resEinheitListe;

	public Teilpaket(Arbeitspaket arbeitspaket) {
		super(arbeitspaket.getVorgangsdauer(), arbeitspaket.getMitarbeiteranzahl(), arbeitspaket.getAufwand());
		this.arbeitspaket = arbeitspaket;

		resEinheitListe = new ArrayList<ResEinheit>();
		for (int i = 0; i < this.aufwand; i++) {
			resEinheitListe.add(new ResEinheit(this));
		}
	}

	/**
	 * Dieser Konstruktor wurde ausschließlich zur Verwendung in den Methoden
	 * {@link #copy()} und {@link #trenneVariabel(ArrayList)} angelegt.
	 *
	 * @param arbeitspaket
	 * @param resEinheitListe
	 */
	private Teilpaket(Arbeitspaket arbeitspaket, ArrayList<ResEinheit> resEinheitListe) {
		this.arbeitspaket = arbeitspaket;
		for (ResEinheit resEinheit : resEinheitListe) {
			resEinheit.setTeilpaket(this);
		}
		this.resEinheitListe = resEinheitListe;
		this.mitarbeiteranzahl = arbeitspaket.getMitarbeiteranzahl();
		this.aufwand = resEinheitListe.size();
		this.vorgangsdauer = (int) Math.ceil(((double) aufwand / (double) mitarbeiteranzahl));
	}

	/**
	 * Dieser Konstruktor wurde ausschließlich zur Verwendung in den Methoden
	 * {@link #trenneTeilpaketHorizontal(ArrayList)} und
	 * {@link #trenneTeilpaketVertikal(ArrayList)} angelegt.
	 *
	 * @param teilpaket
	 * @param neueResEinheitListe
	 */
	private Teilpaket(Teilpaket teilpaket, ArrayList<ResEinheit> neueResEinheitListe) {
		for (ResEinheit zuEntfernen : neueResEinheitListe) {
			teilpaket.resEinheitListe.remove(zuEntfernen);
		}
		this.arbeitspaket = teilpaket.getArbeitspaket();
		this.resEinheitListe = neueResEinheitListe;
		for (ResEinheit resEinheit : resEinheitListe) {
			resEinheit.setTeilpaket(this);
		}
		this.aufwand = neueResEinheitListe.size();
		teilpaket.setAufwand(teilpaket.getResEinheitListe().size());
		if (teilpaket.getResEinheitListe().isEmpty()) {
			this.arbeitspaket.entferneTeilpaket(teilpaket);
		}
		this.arbeitspaket.getTeilpaketListe().add(this);

		Collections.sort(this.resEinheitListe, new ComperatorVektor2iY());
		Collections.sort(teilpaket.getResEinheitListe(), new ComperatorVektor2iY());
	}

	/**
	 * Damit diese Methode funktionieren kann, wird die zu übergebende ArrayList
	 * nach der Sortierungsmethode des ComperatorVektor2iY sortiert.
	 *
	 * @param neueResEinheitListe
	 * @return
	 */
	public Teilpaket trenneTeilpaketHorizontal(ArrayList<ResEinheit> neueResEinheitListe) {

		Teilpaket neuesTeilpaket = null;

		if (!neueResEinheitListe.isEmpty()) {

			neuesTeilpaket = new Teilpaket(this, neueResEinheitListe);

			neuesTeilpaket.setVorgangsdauer(this.getVorgangsdauer());
			neuesTeilpaket.setMitarbeiteranzahl(neuesTeilpaket.getAufwand() / neuesTeilpaket.getVorgangsdauer());

			this.setMitarbeiteranzahl(this.getMitarbeiteranzahl() - neuesTeilpaket.getMitarbeiteranzahl());

			if (this.resEinheitListe.isEmpty()) {
				this.arbeitspaket.entferneTeilpaket(this);
			}

		}

		return neuesTeilpaket;

	}

	/**
	 * Damit diese Methode funktionieren kann, wird die zu übergebende ArrayList
	 * nach der Sortierungsmethode des ComperatorVektor2iY sortiert.
	 *
	 * @param neueResEinheitListe
	 * @return
	 */
	public Teilpaket trenneTeilpaketVertikal(ArrayList<ResEinheit> neueResEinheitListe) {

		Teilpaket neuesTeilpaket = null;

		if (!neueResEinheitListe.isEmpty()) {

			neuesTeilpaket = new Teilpaket(this, neueResEinheitListe);

			neuesTeilpaket.setMitarbeiteranzahl(this.getMitarbeiteranzahl());
			neuesTeilpaket.setVorgangsdauer(neuesTeilpaket.getAufwand() / neuesTeilpaket.getMitarbeiteranzahl());

			this.setVorgangsdauer(this.getVorgangsdauer() - neuesTeilpaket.getVorgangsdauer());

			if (this.resEinheitListe.isEmpty()) {
				this.arbeitspaket.entferneTeilpaket(this);
			}

			Collections.sort(this.getArbeitspaket().getTeilpaketListe(), new ComperatorTeilpaket());

		}

		return neuesTeilpaket;

	}

	/**
	 * Trennt die mitgegebene ResEinheiten-Liste aus dem Teilpaket und erstellt ein
	 * neues Teilpaket mit Vorgangsdauer 1.
	 *
	 * Damit diese Methode funktionieren kann, muss die zu übergebende ArrayList mit
	 * nach der Sortierungsmethode des ComperatorVektor2iY sortiert worden sein.
	 *
	 * @param gesezteResEinheiten
	 */
	public void trenneVariabel(ArrayList<ResEinheit> gesezteResEinheiten) {
		if (!gesezteResEinheiten.isEmpty()) {
			Teilpaket neuesTeilpaket = new Teilpaket(this.arbeitspaket, gesezteResEinheiten);
			neuesTeilpaket.getArbeitspaket().teilpaketHinzufuegen(neuesTeilpaket);

			for (ResEinheit zuEntfernen : gesezteResEinheiten) {
				this.resEinheitListe.remove(zuEntfernen);
			}

			neuesTeilpaket.vorgangsdauer = 1;
			neuesTeilpaket.aufwand = gesezteResEinheiten.size();
			neuesTeilpaket.mitarbeiteranzahl = (int) Math.ceil(((double) aufwand / (double) vorgangsdauer));

			if (!resEinheitListe.isEmpty()) {
				this.aufwand = resEinheitListe.size();
				this.vorgangsdauer = (int) Math.ceil(((double) aufwand / (double) mitarbeiteranzahl));
			} else {
				this.arbeitspaket.entferneTeilpaket(this);
			}

		}
	}

	/**
	 * Zwei Teilpakete eines gemeinsamen Arbeitspaketes werden zu einem gemeinsamen
	 * Teilpaket zusammengeführt, wenn die untersten ResEinheiten sich auf einer
	 * gemeinsamen Y_Achse befinden
	 *
	 * @param tp
	 * @return
	 */
	public boolean zusammenfuehren(Teilpaket tp) {

		int xPunkt1Tp1 = this.resEinheitListe.get(0).getPosition().getxKoordinate();
		int xPunkt2Tp1 = xPunkt1Tp1 + this.vorgangsdauer - 1;

		int xPunkt1Tp2 = tp.resEinheitListe.get(0).getPosition().getxKoordinate();
		int xPunkt2Tp2 = xPunkt1Tp2 + tp.vorgangsdauer - 1;

		int yAchse1 = tp.resEinheitListe.get(0).getPosition().getyKoordinate();
		int yAchse2 = this.resEinheitListe.get(0).getPosition().getyKoordinate();

		if ((yAchse1 == yAchse2) && (xPunkt1Tp1 == xPunkt2Tp2 - 1 || xPunkt2Tp1 == xPunkt1Tp2 - 1)) {
			for (ResEinheit res : tp.getResEinheitListe()) {
				res.setTeilpaket(this);
				resEinheitListe.add(res);
			}
			aufwand += tp.getAufwand();
			vorgangsdauer += tp.getVorgangsdauer();
			Collections.sort(resEinheitListe, new ComperatorVektor2iY());

			arbeitspaket.entferneTeilpaket(tp);

			return true;
		} else {
			return false;
		}
	}

	@Override
	public void bewegen(ResCanvas resCanvas, int yMove, int xMove) {

		ListIterator<ResEinheit> li = resEinheitListe.listIterator(resEinheitListe.size());

		// Iterate in reverse.
		while (li.hasPrevious()) {
			li.previous().bewegen(resCanvas, yMove, xMove);
		}

	}

	@Override
	public boolean bewegeX(ResCanvas resCanvas, int xMove) {
		ListIterator<ResEinheit> li;

		if (resCanvas.ueberpruefePosition(this, xMove, 0)) {

			if (xMove < 0) {

				li = resEinheitListe.listIterator();
				while (li.hasNext()) {
					li.next().bewegeX(resCanvas, xMove);
				}
			} else {

				li = resEinheitListe.listIterator(resEinheitListe.size());
				// Iterate in reverse.
				while (li.hasPrevious()) {
					li.previous().bewegeX(resCanvas, xMove);
				}
			}

			return true;
		}
		return false;
	}

	@Override
	public boolean bewegeY(ResCanvas resCanvas, int yMove) {
		ListIterator<ResEinheit> li;

		if (resCanvas.ueberpruefePosition(this, 0, yMove)) {
			if (yMove < 0) {

				li = resEinheitListe.listIterator();
				while (li.hasNext()) {
					li.next().bewegeY(resCanvas, yMove);
				}
			} else {

				li = resEinheitListe.listIterator(resEinheitListe.size());
				// Iterate in reverse.
				while (li.hasPrevious()) {
					li.previous().bewegeY(resCanvas, yMove);
				}
			}
			return true;
		}
		return false;

	}

	/**
	 * Überprüfe die Position jeder Reseinheit eines Teilpakets, ob diese die
	 * vorgebebenen Zeiten des zugehörigen Arbeitspakets einhält. Ist das Teilpaket
	 * zu früh, so gibt die Methode einen positiven Wert zurück. Ist es zu spät,
	 * entsprechend einen negativen!
	 *
	 * @return
	 */
	public int ueberpruefeZeiten() {
		Vektor2i position;
		int verschieben = 0;
		for (ResEinheit res : resEinheitListe) {
			position = res.position;

			if (this.arbeitspaket.getFaz() > position.getxKoordinate() + 1) {
				int neuVerschieben = this.arbeitspaket.getFaz() - (position.getxKoordinate() + 1);
				if (neuVerschieben > verschieben) {
					verschieben = neuVerschieben;
				}
			} else if (this.arbeitspaket.getSez() < position.getxKoordinate() + 1) {
				int neuVerschieben = this.arbeitspaket.getSez() - (position.getxKoordinate() + 1);
				if (neuVerschieben < verschieben) {
					verschieben = neuVerschieben;
				}
			}
		}
		return verschieben;
	}

	/**
	 * Überprüft die Lage des Teilpaktes bezüglich der Zeiten des Arbeitspaktes.
	 *
	 * @return
	 */
	public VerschiebeRichtung ueberpruefeZeitenEnum() {

		VerschiebeRichtung verschieben = null;

		// int xPos = resEinheitListe.get(0).getPosition().getxKoordinate();

		ArrayList<ResEinheit> resEinheiten = this.getResEinheitListe();
		int xPos = resEinheitListe.get(resEinheiten.size() - 1).getPosition().getxKoordinate();

		if (this.arbeitspaket.getFez() - 1 > xPos) {
			verschieben = VerschiebeRichtung.RECHTS;
		} else if (this.arbeitspaket.getFez() - 1 == xPos) {
			verschieben = VerschiebeRichtung.FAZ;
		} else {
			verschieben = VerschiebeRichtung.LINKS;
		}

		return verschieben;
	}

	public Arbeitspaket getArbeitspaket() {
		return arbeitspaket;
	}

	public void setArbeitspaket(Arbeitspaket arbeitspaket) {
		this.arbeitspaket = arbeitspaket;
	}

	public ArrayList<ResEinheit> getResEinheitListe() {
		return resEinheitListe;
	}

	public void setResEinheitListe(ArrayList<ResEinheit> resEinheitListe) {
		this.resEinheitListe = resEinheitListe;
	}

	/**
	 * Legt eine WIRKLICHE Kopie des Teilpaketes an.
	 *
	 * D.h. es wird nicht einfach die Referenz kopiert. Sondern ein neues, vom
	 * ursprünglichen Teilpaket unabhäniges Teilpaket, angleget.
	 *
	 * @return
	 */
	public Teilpaket copy(Arbeitspaket copyArbeitspaket) {

		ArrayList<ResEinheit> neueResEinheitListe = new ArrayList<ResEinheit>();

		for (ResEinheit re : this.resEinheitListe) {
			neueResEinheitListe.add(re.copy());
		}

		return new Teilpaket(copyArbeitspaket, neueResEinheitListe);

	}

}