package reslearn.main;

import reslearn.model.canvas.Canvas;
import reslearn.model.paket.Arbeitspaket;

public class Main {

	public static void main(String[] args) {
		Canvas canvas = new Canvas();

		Arbeitspaket ap1 = new Arbeitspaket("Testpaket1", 1, 2, 3, 4, 2, 3, 6);
		Arbeitspaket ap2 = new Arbeitspaket("Testpaket2", 3, 5, 5, 7, 2, 3, 6);
		Arbeitspaket ap3 = new Arbeitspaket("Testpaket2", 6, 6, 6, 6, 6, 6, 6);

		canvas.hinzufuegen(ap1);
		canvas.hinzufuegen(ap2);
		canvas.hinzufuegen(ap3);

		canvas.entfernen(ap3);

	}

}
