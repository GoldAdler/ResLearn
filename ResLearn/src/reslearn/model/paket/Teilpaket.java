package reslearn.model.paket;

import java.util.ArrayList;

/**
 * @author Lukas Willburger
 * @version 1.0
 * @created 18-Apr-2018 10:53:20
 */
public class Teilpaket extends Paket {

	/**
	 * Referenz auf das Arbeitspaket
	 */
	private Arbeitspaket arbeitspaket;
	private ArrayList<ResEinheit> resEinheitListe;
	public ResEinheit m_ResEinheit;

	public Teilpaket() {

	}

	@Override
	public void finalize() throws Throwable {
		super.finalize();
	}
}// end Teilpaket