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

	private Teilpaket() {
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

			/*
			 * ...........................................
			 * .............DDD...........................
			 * .............DDD...........................
			 * .............DDDDD.........................
			 * AAAAAA...CCCCFFFDD.........................
			 * AAAAAABBBBBBBFFFDD......EEEEEE.............
			 * AAAAAABBBBBBBCCCCCC.....EEEEEE.............
			 * AAAAAABBBBBBBCCCCCC.....EEEEEE.............
			 *
			 * Problem: Diese D's ------------------------
			 * .............DDD...........................
			 * .............DDD...........................
			 * .............DDDDD.........................
			 *
			 * überschreiten die Grenze. Allerdings gehören die D's zu zwei verschieden
			 * Teilpaketen. Dem Teilpaket aus 9 ResEinheiten und das andere aus dem
			 * Teilpaket mit 6 ResEinheiten.
			 *
			 * Für diesen speziellen Speziallfall unter den Speizallfällen müssen die
			 * überschrittenen ResEinheiten aus meheren Teilpaketen entefernt werden. Diese
			 * Teilpakete müssen natürlich auch wieder über ihre Veränderung informiert
			 * werden.
			 *
			 */

			ArrayList<ArrayList<ResEinheit>> resEinheitsListe = new ArrayList<>();

			Teilpaket tmpTeilpaket = null;
			Teilpaket testTeilpaket = null;
			ArrayList<ResEinheit> tmpResEinheitsListe = new ArrayList<>();

			// Wir spalten die neueResEinheitListe in mehere Listen auf. Die Anzahl dieser
			// neuen Listen ist dabei identisch mit der Anzahl der unterschiedlichen
			// Teilpaketen, aus dennen die ResEinheiten für die neueResEinehitListe stammen.
			// Dies ist notwendig, da diese neuen temporeren Listen dazu dienen werden, die
			// Teilpakete übere ihre Veränderungen zu informieren.

			for (ResEinheit res : neueResEinheitListe) {

				if (tmpResEinheitsListe.isEmpty()) {
					resEinheitsListe.add(tmpResEinheitsListe);
					tmpResEinheitsListe.add(res);
					tmpTeilpaket = res.getTeilpaket();
				} else {
					testTeilpaket = res.getTeilpaket();
					if (testTeilpaket == tmpTeilpaket) {
						tmpResEinheitsListe.add(res);
					} else {
						tmpTeilpaket = testTeilpaket;
						tmpResEinheitsListe = new ArrayList<>();
						resEinheitsListe.add(tmpResEinheitsListe);
						tmpResEinheitsListe.add(res);

					}
				}
			}

			// for (Teilpaket tp : teilpaketListe) {
			// for (ResEinheit zuEntfernen : neueResEinheitListe) {
			// tp.resEinheitListe.remove(zuEntfernen);
			// }
			// }

			// die ResEinheitsListe besteht nur aus einer Liste. Das bedeutet, dass nur ein
			// einziges Teilpaket, also das aktuelle, von den Änderungen betroffen sind. Der
			// einfache Fall
			if (resEinheitsListe.size() == 1) {
				neuesTeilpaket = new Teilpaket(this, neueResEinheitListe);

				neuesTeilpaket.setVorgangsdauer(this.getVorgangsdauer());
				neuesTeilpaket.setMitarbeiteranzahl(neuesTeilpaket.getAufwand() / neuesTeilpaket.getVorgangsdauer());

				this.setMitarbeiteranzahl(this.getMitarbeiteranzahl() - neuesTeilpaket.getMitarbeiteranzahl());

				if (this.resEinheitListe.isEmpty()) {
					this.arbeitspaket.entferneTeilpaket(this);
				}
			} else {
				// In resEinheitsListe sind mehere Listen. Der schwere Fall. Mehrere Teilpakete
				// müssen über ihre Änderung informiert werden. Da mehrere TEilpakete die
				// ResEinheiten stellen.

				// update der sich zu verändernden Teilpakete

				Teilpaket tmp = null;
				for (ArrayList<ResEinheit> resList : resEinheitsListe) {
					tmp = resList.get(0).getTeilpaket();
					for (ResEinheit zuEntfernenRE : resList) {
						tmp.getResEinheitListe().remove(zuEntfernenRE);
					}
					if (tmp.getResEinheitListe().isEmpty()) {
						tmp.getArbeitspaket().entferneTeilpaket(tmp);
					} else {
						tmp.setAufwand(tmp.getResEinheitListe().size());

						int minY = Integer.MAX_VALUE;
						int maxY = 0;
						for (ResEinheit res : tmp.getResEinheitListe()) {
							if (res.getPosition().getyKoordinate() < minY) {
								minY = res.getPosition().getyKoordinate();
							}
							if (res.getPosition().getyKoordinate() > maxY) {
								maxY = res.getPosition().getyKoordinate();
							}
						}

						int mitarbeiteranzahl = maxY - minY;
						tmp.setMitarbeiteranzahl(mitarbeiteranzahl);

						tmp.setVorgangsdauer((int) Math.ceil(((double) tmp.aufwand / (double) tmp.mitarbeiteranzahl)));
					}
				}

				neuesTeilpaket = new Teilpaket();
				neuesTeilpaket.setArbeitspaket(this.getArbeitspaket());
				neuesTeilpaket.getArbeitspaket().teilpaketHinzufuegen(neuesTeilpaket);
				neuesTeilpaket.setResEinheitListe(neueResEinheitListe);
				for (ResEinheit re : neueResEinheitListe) {
					re.setTeilpaket(neuesTeilpaket);
				}
				// Diese Werte werden nur benötigt, damit später keine Nullpointer geworfen
				// wird.
				// Allerdings spielen diese Werte neben diesem Grund keine Rolle, da es sich
				// hierbei um ein Teilpaket, dass die ResEinheiten zusammenasst, die über der
				// Grenze liegen. Also wird dieses Teilpaket eh wieder gelöschst
				// -----------------------------------------
				neuesTeilpaket.setMitarbeiteranzahl(this.getArbeitspaket().getMitarbeiteranzahl());
				neuesTeilpaket.setAufwand(neueResEinheitListe.size());
				neuesTeilpaket.setVorgangsdauer(
						neueResEinheitListe.get(neueResEinheitListe.size() - 1).getPosition().getxKoordinate()
								- neueResEinheitListe.get(0).getPosition().getxKoordinate());
				// -----------------------------------------

			}

		}

		Collections.sort(neuesTeilpaket.getArbeitspaket().getTeilpaketListe(), new ComperatorTeilpaket());

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

			int minY = Integer.MAX_VALUE;
			int maxY = 0;
			for (ResEinheit res : neuesTeilpaket.getResEinheitListe()) {
				if (res.getPosition().getyKoordinate() < minY) {
					minY = res.getPosition().getyKoordinate();
				}
				if (res.getPosition().getyKoordinate() > maxY) {
					maxY = res.getPosition().getyKoordinate();
				}
			}

			int mitarbeiteranzahl = maxY - minY;

			neuesTeilpaket.setMitarbeiteranzahl(mitarbeiteranzahl);
			if (mitarbeiteranzahl != 0) {
				neuesTeilpaket.setVorgangsdauer(neuesTeilpaket.getAufwand() / neuesTeilpaket.getMitarbeiteranzahl());
			} else {
				neuesTeilpaket.setVorgangsdauer(1);
			}
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