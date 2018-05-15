package reslearn.gui.tableedit;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import reslearn.model.paket.Arbeitspaket;
import reslearn.model.paket.Paket;
import reslearn.model.resCanvas.ResCanvas;

public class ArbeitspaketTableData extends Paket {
	private SimpleStringProperty id;
	private SimpleIntegerProperty faz;
	private SimpleIntegerProperty saz;
	private SimpleIntegerProperty fez;
	private SimpleIntegerProperty sez;
	private SimpleIntegerProperty mitarbeiteranzahl;
	private SimpleIntegerProperty aufwand;

	private static int anzPakete = 0;
	private static ArbeitspaketTableData[] paketeTD = new ArbeitspaketTableData[10];
	private static Arbeitspaket[] pakete;

	public ArbeitspaketTableData(Arbeitspaket arbeitspaket) {
		this.id = new SimpleStringProperty(arbeitspaket.getId());
		this.faz = new SimpleIntegerProperty(arbeitspaket.getFaz());
		this.saz = new SimpleIntegerProperty(arbeitspaket.getSaz());
		this.fez = new SimpleIntegerProperty(arbeitspaket.getFez());
		this.sez = new SimpleIntegerProperty(arbeitspaket.getSez());
		this.mitarbeiteranzahl = new SimpleIntegerProperty(arbeitspaket.getMitarbeiteranzahl());
		this.aufwand = new SimpleIntegerProperty(arbeitspaket.getAufwand());

		paketeTD[anzPakete++] = this;
	}

	public ArbeitspaketTableData(final String id, final int faz, final int saz, final int fez, final int sez,
			final int vorgangsdauer, final int aufwand, final int mitarbeiteranzahl) {
		super(vorgangsdauer, mitarbeiteranzahl, aufwand);
		this.id = new SimpleStringProperty(id);
		this.faz = new SimpleIntegerProperty(faz);
		this.saz = new SimpleIntegerProperty(saz);
		this.fez = new SimpleIntegerProperty(fez);
		this.sez = new SimpleIntegerProperty(sez);
		this.mitarbeiteranzahl = new SimpleIntegerProperty(mitarbeiteranzahl);
		this.aufwand = new SimpleIntegerProperty(aufwand);

		paketeTD[anzPakete++] = this;
	}
	
	private static void setArbeitspakete() {
		pakete = new Arbeitspaket[anzPakete];

		for (int i = 0; i <= anzPakete; i++) {
			pakete[i].setId(paketeTD[i].getId());
			pakete[i].setFaz(paketeTD[i].getFaz());
			pakete[i].setSaz(paketeTD[i].getSaz());
			pakete[i].setFez(paketeTD[i].getFez());
			pakete[i].setSez(paketeTD[i].getSez());
			pakete[i].setMitarbeiteranzahl(paketeTD[i].getMitarbeiteranzahl());
			pakete[i].setAufwand(paketeTD[i].getAufwand());
		}
	}


	public Arbeitspaket[] getArbeitspakete() {
		if (pakete == null) {
			setArbeitspakete();
		}
		return pakete;
	}

	public String getId() {
		return id.get();
	}

	public void setId(final String id) {
		this.id.set(id);
	}

	public int getFaz() {
		return faz.get();
	}

	public void setFaz(final int faz) {
		this.faz.set(faz);
	}

	public int getSaz() {
		return saz.get();
	}

	public void setSaz(final int saz) {
		this.saz.set(saz);
	}

	public int getFez() {
		return fez.get();
	}

	public void setFez(final int fez) {
		this.fez.set(fez);
	}

	public int getSez() {
		return sez.get();
	}

	public void setSez(final int sez) {
		this.sez.set(sez);
	}

	public int getMitarbeiteranzahl() {
		return mitarbeiteranzahl.get();
	}

	public void setMitarbeiteranzahl(final int mitarbeiteranzahl) {
		this.mitarbeiteranzahl.set(mitarbeiteranzahl);
	}

	public int getAufwand() {
		return aufwand.get();
	}

	public void setAufwand(final int aufwand) {
		this.aufwand.set(aufwand);
	}


	@Override
	public void bewegen(ResCanvas resCanvas, int yMove, int xMove) {
	}

	@Override
	public void bewegeX(ResCanvas resCanvas, int xMove) {
	}

	@Override
	public void bewegeY(ResCanvas resCanvas, int yMove) {
	}

}
