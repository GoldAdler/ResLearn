package reslearn.model.paket;

/**
 * @author Lukas Willburger
 * @version 1.0
 * @created 18-Apr-2018 10:53:19
 */
public class ResEinheit extends Paket {

	/**
	 * Referenz auf das Teilpaket
	 */
	private Teilpaket teilpaket;

	public ResEinheit(){

	}

	public void finalize() throws Throwable {
		super.finalize();
	}
}//end ResEinheit