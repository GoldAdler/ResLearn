package reslearn.model.canvas;

import reslearn.model.paket.ArbeitspaketZustand;

/**
 * @author Lukas Willburger
 * @version 1.0
 * @created 18-Apr-2018 10:53:17
 */
public class Canvas {
	int i; 	//ich will nur was verändern, um zu sehen, was passiert
	private ArbeitspaketZustand arbeitspaketZustandListe;
	private ArbeitspaketZustand aktuellerZustand;
	public ArbeitspaketZustand m_ArbeitspaketZustand;

	public Canvas() {

	}

	@Override
	public void finalize() throws Throwable {

	}
}// end Canvas