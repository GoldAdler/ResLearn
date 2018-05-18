package reslearn.gui.controller;

import java.util.ArrayList;

public class Controller {

	// Alle Pfade in einem String Array speichern
	public static ArrayList<String> alleFenster = new ArrayList<String>();
	public static int AufgabenNummer = 5;

	public String vorherigesFenster(ArrayList<String> list) {
		String string = list.get(list.size() - 1);
		list.remove(list.get(list.size() - 1));
		return string;
	}

	public String hauptmenue() {
		return "../fxml/Hauptmenue.fxml";
	}

}
