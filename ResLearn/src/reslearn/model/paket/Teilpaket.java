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

	public void zusammenfuehren(Teilpaket tp) {
		for (ResEinheit res : tp.getResEinheitListe()) {
			res.setTeilpaket(this);
			resEinheitListe.add(res);
		}
		aufwand += tp.getAufwand();
		vorgangsdauer += tp.getVorgangsdauer();
		Collections.sort(resEinheitListe, new ComperatorVektor2i());

		arbeitspaket.getTeilpaketListe().remove(tp);

	}

	@Override
	public void bewegen(ResCanvas resCanvas, int yMove, int xMove) {

		ListIterator li = resEinheitListe.listIterator(resEinheitListe.size());

		// Iterate in reverse.
		while (li.hasPrevious()) {
			((ResEinheit) li.previous()).bewegen(resCanvas, yMove, xMove);
		}

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