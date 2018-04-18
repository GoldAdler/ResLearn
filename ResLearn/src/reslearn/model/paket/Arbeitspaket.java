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
	public Teilpaket m_Teilpaket;
	public ArbeitspaketZustand m_ArbeitspaketZustand;

	public Arbeitspaket() {

	}

	@Override
	public void finalize() throws Throwable {
		super.finalize();
	}
}// end Arbeitspaket