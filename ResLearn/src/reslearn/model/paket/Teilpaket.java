package reslearn.model.paket;

import java.util.ArrayList;
import java.util.Collections;
import java.util.ListIterator;

import reslearn.model.resCanvas.ResCanvas;
import reslearn.model.utils.ComperatorVektor2iY;
import reslearn.model.utils.Vektor2i;

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

	public Teilpaket trenneTeilpaketHorizontal(ArrayList<ResEinheit> neueResEinheitListe) {

		Teilpaket neuesTeilpaket = null;

		if (!neueResEinheitListe.isEmpty()) {
			// && resEinheitListe.size() != neueResEinheitListe.size()
			// && !(resEinheitListe.containsAll(neueResEinheitListe)

			for (ResEinheit zuEntfernen : neueResEinheitListe) {
				this.resEinheitListe.remove(zuEntfernen);
			}

			if (!resEinheitListe.isEmpty()) {
				this.aufwand = resEinheitListe.size();
				this.vorgangsdauer = (int) Math.ceil(((double) aufwand / (double) mitarbeiteranzahl));
			} else {
				this.arbeitspaket.entferneTeilpaket(this);
			}

			neuesTeilpaket = new Teilpaket(this.arbeitspaket, neueResEinheitListe);
			this.arbeitspaket.teilpaketHinzufuegen(neuesTeilpaket);
		}

		return neuesTeilpaket;
	}

	public Teilpaket trenneTeilpaketVertikal(ArrayList<ResEinheit> neueResEinheitListe, int vorgangsdauer) {

		Teilpaket neuesTeilpaket = null;

		if (!neueResEinheitListe.isEmpty()) {

			neuesTeilpaket = trenneTeilpaketHorizontal(neueResEinheitListe);

			neuesTeilpaket.vorgangsdauer = vorgangsdauer;
			neuesTeilpaket.aufwand = neueResEinheitListe.size();
			neuesTeilpaket.mitarbeiteranzahl = (int) Math.ceil(((double) aufwand / (double) vorgangsdauer));

		}

		return neuesTeilpaket;

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

	// private void bewegeRechts(ResCanvas resCanvas, int yMove, int xMove) {
	// ListIterator<ResEinheit> li;
	// li = resEinheitListe.listIterator();
	// while (li.hasNext()) {
	// li.next().bewegen(resCanvas, yMove, xMove);
	// }
	// }
	//
	// private void bewegeLinks(ResCanvas resCanvas, int yMove, int xMove) {
	// ListIterator<ResEinheit> li;
	// li = resEinheitListe.listIterator(resEinheitListe.size());
	// // Iterate in reverse.
	// while (li.hasPrevious()) {
	// li.previous().bewegen(resCanvas, yMove, xMove);
	// }
	// }

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