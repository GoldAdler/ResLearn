package reslearn.model.paket;

import java.util.ArrayList;

/**
 * @author Lukas Willburger
 * @version 1.0
 * @created 18-Apr-2018 10:53:12
 */
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

}