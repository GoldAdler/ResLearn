package reslearn.gui;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import reslearn.model.paket.ResEinheit;

public class Diagramm {

	public static int zeile = 430;
	public static int spalte = 280;
	ResFeld[][] feld;
	public static ResFeld[][] res;
	int counterXAchse;
	int counterYAchse;

	public void zeichneCanvas(GraphicsContext gc, Canvas canvas) {
		// Zeichne 4 x Rahmen & 2 x Koordinaten-Achsen
		gc.setFont(new Font("Arial", DisplayCanvas.schriftGroesse));
		gc.setStroke(Color.GRAY);
		gc.strokeLine(0, canvas.getHeight(), 0, 0);
		gc.strokeLine(0, canvas.getHeight(), canvas.getWidth(), canvas.getHeight());
		gc.strokeLine(0, 0, canvas.getWidth(), 0);
		gc.strokeLine(canvas.getWidth(), canvas.getHeight(), canvas.getWidth(), 0);

		gc.setLineWidth(4);
		gc.setStroke(Color.rgb(205, 133, 63));
		gc.strokeLine(DisplayCanvas.resFeldBreite, canvas.getHeight() - DisplayCanvas.resFeldLaenge,
				DisplayCanvas.resFeldBreite, DisplayCanvas.resFeldLaenge - DisplayCanvas.spaltY); // Y-Achse
		gc.strokeLine(DisplayCanvas.resFeldBreite, canvas.getHeight() - DisplayCanvas.resFeldLaenge,
				canvas.getWidth() - DisplayCanvas.gesamtAbstandX + DisplayCanvas.spaltX,
				canvas.getHeight() - DisplayCanvas.resFeldLaenge); // X-Achse
		gc.bezierCurveTo(20, 30, 40, 50, 60, 70);
		// Koordinatenbeschriftung
		gc.setLineWidth(1);
		gc.setStroke(Color.BLACK);

		for (double i = DisplayCanvas.gesamtAbstandX; i < canvas.getWidth(); i += 5 * DisplayCanvas.resFeldBreite) {
			if (i != DisplayCanvas.gesamtAbstandX) {
				gc.strokeLine(i, canvas.getHeight() - DisplayCanvas.resFeldBreite / 1.5, i,
						canvas.getHeight() - DisplayCanvas.resFeldLaenge - DisplayCanvas.spaltY);
				counterYAchse += 5;

				gc.fillText(String.valueOf(counterYAchse), i - DisplayCanvas.spaltX, canvas.getHeight());
			}
		}
		for (double i = canvas.getHeight() - DisplayCanvas.resFeldLaenge
				- DisplayCanvas.spaltY; i > DisplayCanvas.gesamtAbstandY; i -= 5 * DisplayCanvas.resFeldBreite) {
			if (i != canvas.getHeight() - DisplayCanvas.resFeldLaenge - DisplayCanvas.spaltY) {
				gc.strokeLine(DisplayCanvas.resFeldBreite - DisplayCanvas.resFeldBreite / 4, i,
						DisplayCanvas.gesamtAbstandX, i);
				counterXAchse += 5;
				gc.fillText(String.valueOf(counterXAchse), 0, i + DisplayCanvas.spaltY);
			}
		}

		// Achsenbeschriftung
		gc.fillText("Tage", canvas.getWidth() - DisplayCanvas.resFeldBreite - DisplayCanvas.spaltX * 2,
				canvas.getHeight() - DisplayCanvas.spaltY);
		gc.fillText("Personen", 2, DisplayCanvas.spaltY * 2 + 1);

		zeichneArray();
	}

	// Weiße Klötzchen
	public void zeichneArray() {
		feld = new ResFeld[zeile][spalte];
		for (int i = 0; i < zeile; i += 10) {
			for (int j = 0; j < spalte; j += 10) {
				feld[i][j] = new ResFeld(i * (DisplayCanvas.resFeldBreite / 10.0),
						j * (DisplayCanvas.resFeldBreite / 10.0), ResFeld.breite, ResFeld.laenge);
				feld[i][j].setFill(Color.WHITE);
				feld[i][j].setStroke(Color.GRAY);
				View.pane.getChildren().add(feld[i][j]);
			}
		}
	}

	// Farbige Pakete
	public void zeichnePaket(ResEinheit[][] koordinatenSystem) {
		res = new ResFeld[zeile][spalte];
		for (int i = 0; i < koordinatenSystem.length; i++) {
			for (int j = 0; j < koordinatenSystem[i].length; j++) {
				if (koordinatenSystem[i][j] != null) {
					res[i * 10][j * 10] = feld[i * 10][j * 10].setzeFeld(j, i, koordinatenSystem[i][j]);
					View.pane.getChildren().add(res[i * 10][j * 10]);
					View.cc.makeDraggable(res[i * 10][j * 10]);
				}
			}
		}
	}
}
