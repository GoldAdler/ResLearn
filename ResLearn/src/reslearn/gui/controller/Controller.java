package reslearn.gui.controller;

import java.util.ArrayList;
import java.util.ListIterator;

import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Window;

public class Controller {

	//Alle Pfade in einem String Array speichern
	public static ArrayList<String> alleFenster = new ArrayList<String>();
	

	public static String vorherigesFenster(ArrayList<String> list) {
		for(String ausgabe : alleFenster)
		{
			System.out.println(ausgabe);
		}
		return list.get(list.size() - 1);
		//list.remove(list.size() - 1);
	}

	
}




