package reslearn.model.validierung;

import reslearn.model.paket.ResEinheit;

public class Feedback {

	private ResEinheit resEinheit;
	private String message;
	private MsgType type;

	public enum MsgType {
		ERROR, INFO
	}

	public Feedback(String message, MsgType type, ResEinheit resEinheit) {
		this.message = message;
		this.resEinheit = resEinheit;
		this.type = type;
	}

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

}
