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

	// TODO: Error-Return-Message zusammenbauen
	private String errorToString() {
		String msg = "";
		return msg;
	}

	// TODO: Info-Return-Message zusammenbauen
	private String infoToString() {
		String msg = "";
		return msg;
	}

}
