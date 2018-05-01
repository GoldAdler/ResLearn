package reslearn.model.paket;

import java.util.ArrayList;
import java.util.Collections;
import java.util.ListIterator;

import reslearn.model.resCanvas.ResCanvas;

public class Teilpaket extends Paket {

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

	public Teilpaket trenneTeilpaket(ArrayList<ResEinheit> neueResEinheitListe) {

		for (ResEinheit zuEntfernen : neueResEinheitListe) {
			this.resEinheitListe.remove(zuEntfernen);
		}

		this.aufwand = resEinheitListe.size();
		this.vorgangsdauer = (int) Math.ceil(((double) aufwand / (double) mitarbeiteranzahl));

		Teilpaket neuesTeilpaket = new Teilpaket(this.arbeitspaket, neueResEinheitListe);
		this.arbeitspaket.teilpaketHinzufuegen(neuesTeilpaket);

		return neuesTeilpaket;
	}

	/**
	 * Zwei Teilpakete eines gemeinsamen Arbeitspaketes werden zu einem gemeinsamen
	 * Teilpaket zusammengef�hrt, wenn die untersten ResEinheiten sich auf einer
	 * gemeinsamen Y_Achse befinden
	 *
	 * @param tp
	 * @return
	 */
	public boolean zusammenfuehren(Teilpaket tp) {

		int xPunkt1Tp1 = tp.resEinheitListe.get(0).getPosition().getxKoordinate();
		int xPunkt2Tp1 = xPunkt1Tp1 + tp.aufwand;

		int xPunkt1Tp2 = this.resEinheitListe.get(0).getPosition().getxKoordinate();
		int xPunkt2Tp2 = xPunkt1Tp2 + this.aufwand;

		int yAchse1 = tp.resEinheitListe.get(0).getPosition().getyKoordinate();
		int yAchse2 = this.resEinheitListe.get(0).getPosition().getyKoordinate();

		if ((yAchse1 == yAchse2) && (xPunkt1Tp1 == xPunkt2Tp2 + 1 || xPunkt2Tp1 == xPunkt1Tp2 + 1)) {
			for (ResEinheit res : tp.getResEinheitListe()) {
				res.setTeilpaket(this);
				resEinheitListe.add(res);
			}
			aufwand += tp.getAufwand();
			vorgangsdauer += tp.getVorgangsdauer();
			Collections.sort(resEinheitListe, new ComperatorVektor2i());

			arbeitspaket.getTeilpaketListe().remove(tp);

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

	/**
	 * �berpr�fe die Position jeder Reseinheit eines Teilpakets, ob diese die
	 * vorgebebenen Zeiten des zugeh�rigen Arbeitspakets einh�lt. Ist das Teilpaket
	 * zu fr�h, so gibt die Methode einen positiven Wert zur�ck. Ist es zu sp�t,
	 * entsprechend einen negativen!
	 *
	 * @return
	 */

	public int ueberpruefeZeiten() {
		Vektor2i position;
		for (ResEinheit res : resEinheitListe) {
			position = res.position;
			if (this.arbeitspaket.getFaz() > position.getxKoordinate()) {
				return this.arbeitspaket.getFaz() - position.getxKoordinate();
			} else if (this.arbeitspaket.getSez() < position.getxKoordinate()) {
				return this.arbeitspaket.getSez() - position.getxKoordinate();
			}

		}

		return 0;
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

}