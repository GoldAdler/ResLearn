package reslearn.gui;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import reslearn.model.paket.ResEinheit;

public class Diagramm {

	static int abstandX = 20;
	static int abstandY = 20;
	static int spaltX = 5;
	static int spaltY = 5;
	static int x = abstandX + spaltX;
	static int y = +abstandY - spaltY;
	public static int zeile = 430;
	public static int spalte = 280;
	ResFeld[][] feld;
	public static ResFeld[][] res;
	int counterXAchse;
	int counterYAchse;

	public void zeichneCanvas(GraphicsContext gc, Canvas canvas) {
		// Zeichne 4 x Rahmen & 2 x Koordinaten-Achsen
		gc.setStroke(Color.GRAY);
		gc.strokeLine(0, canvas.getHeight(), 0, 0);
		gc.strokeLine(0, canvas.getHeight(), canvas.getWidth(), canvas.getHeight());
		gc.strokeLine(0, 0, canvas.getWidth(), 0);
		gc.strokeLine(canvas.getWidth(), canvas.getHeight(), canvas.getWidth(), 0);

		gc.setLineWidth(4);
		gc.setStroke(Color.rgb(205, 133, 63));
		gc.strokeLine(abstandX, canvas.getHeight() - abstandY, abstandX, abstandY - spaltY);
		gc.strokeLine(abstandX, canvas.getHeight() - abstandY, canvas.getWidth() - abstandX + spaltX,
				canvas.getHeight() - abstandY);
		gc.bezierCurveTo(20, 30, 40, 50, 60, 70);
		// Koordinatenbeschriftung
		gc.setLineWidth(1);
		gc.setStroke(Color.BLACK);
		for (double i = abstandX + spaltX; i < canvas.getWidth(); i += 100) {
			if (i != abstandX + spaltX) {
				gc.strokeLine(i, canvas.getHeight() - abstandX / 2, i, canvas.getHeight() - abstandY - spaltY);
				counterYAchse += 5;
				gc.strokeText(String.valueOf(counterYAchse), i - spaltX, canvas.getHeight());
			}
		}
		for (double i = canvas.getHeight() - abstandY - spaltY; i > abstandY + spaltY; i -= 100) {
			if (i != canvas.getHeight() - abstandY - spaltY) {
				gc.strokeLine(abstandX - abstandX / 2, i, abstandX + spaltX, i);
				counterXAchse += 5;
				gc.strokeText(String.valueOf(counterXAchse), 0, i + spaltY);
			}
		}

		// Achsenbeschriftung
		gc.strokeText("Tage", canvas.getWidth() - abstandX - spaltX * 2, canvas.getHeight() - spaltY);
		gc.strokeText("Personen", 2, spaltY * 2 + 1);

		zeichneArray();
	}

	// Weiße Klötzchen
	public void zeichneArray() {
		feld = new ResFeld[zeile][spalte];
		for (int i = 0; i < zeile; i += 10) {
			for (int j = 0; j < spalte; j += 10) {
				feld[i][j] = new ResFeld(i * 2, j * 2, ResFeld.breite, ResFeld.laenge);
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
