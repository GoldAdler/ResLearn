package reslearn.model.paket;

import java.util.ArrayList;

import reslearn.model.resCanvas.ResCanvas;

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
	private ArbeitspaketZustand arbeitspaketZustand;
	/**
	 * Spaetester Endzeitpunkt
	 */
	private int sez;
	private ArrayList<Teilpaket> teilpaketListe;

	public Arbeitspaket() {

	}

	// TODO: Validierung der Vorgangsdauer
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

	public void teilpaketHinzufuegen(Teilpaket teilpaket) {
		teilpaketListe.add(teilpaket);
	}

	@Override
	public void bewegen(ResCanvas resCanvas, int yMove, int xMove) {
		for (Teilpaket teilpaket : teilpaketListe) {
			teilpaket.bewegen(resCanvas, yMove, xMove);
		}
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

	public ArbeitspaketZustand getArbeitspaketZustand() {
		return arbeitspaketZustand;
	}

	public void setArbeitspaketZustand(ArbeitspaketZustand arbeitspaketZustand) {
		this.arbeitspaketZustand = arbeitspaketZustand;
	}

}