package reslearn.model.paket;

import java.util.ArrayList;

public class Teilpaket extends Paket {

	/**
	 * Referenz auf das Arbeitspaket
	 */
	private Arbeitspaket arbeitspaket;
	private ArrayList<ResEinheit> resEinheitListe;

	public Teilpaket() {

	}

	public Teilpaket(Arbeitspaket arbeitspaket) {
		super(arbeitspaket.getVorgangsdauer(), arbeitspaket.getMitarbeiteranzahl(), arbeitspaket.getAufwand());
		this.arbeitspaket = arbeitspaket;

		resEinheitListe = new ArrayList<ResEinheit>();
		for (int i = 0; i < this.aufwand; i++) {
			resEinheitListe.add(new ResEinheit(this));
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