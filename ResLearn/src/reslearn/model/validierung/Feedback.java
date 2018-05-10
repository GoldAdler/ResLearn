package reslearn.model.validierung;

import reslearn.model.paket.ResEinheit;

public class Feedback {

	private ResEinheit resEinheit;
	private String message;
	private MsgType type;

	/**
	 * Hier werden zwei verschiedene Messagetypen angelegt. Der Typ "INFO" wird
	 * verwendet, wenn lediglich eine Information an den User zur�ckgegeben werden
	 * soll. Bei dem Typ "ERROR" handelt es sich um die Fehlermeldung und die
	 * betroffene ResEinheit, die im Falle eines Fehlers, an den User zur�ckgegeben
	 * wird.
	 */
	public enum MsgType {
		ERROR, INFO
	}

	/**
	 * Hier k�nnte Ihr Feedback stehen.
	 * 
	 * @param message
	 * @param type
	 * @param resEinheit
	 */
	public Feedback(String message, MsgType type, ResEinheit resEinheit) {
		this.message = message;
		this.resEinheit = resEinheit;
		this.type = type;
	}

	/**
	 * Hier k�nnte Ihr Feedback stehen.
	 * 
	 * @param message
	 * @param type
	 */
	public Feedback(String message, MsgType type) {
		this.message = message;
		this.type = type;
	}

	@Override
	public String toString() {
		String msg = "";
		switch (type) {
		case ERROR:
			msg = errorToString();
			break;
		case INFO:
			msg = infoToString();
			break;
		}

		return msg;
	}

	private String errorToString() {
		String overview = "Fehler bei Arbeitspaket " + resEinheit.getTeilpaket().getArbeitspaket().getId() + "!";
		String msg = overview + "\n" + message;
		return msg;
	}

	private String infoToString() {
		String overview = "Information: ";
		String msg = overview + "\n" + message;
		return msg;
	}

	public ResEinheit getResEinheit() {
		return resEinheit;
	}

}
