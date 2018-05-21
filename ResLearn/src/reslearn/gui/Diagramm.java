package reslearn.gui;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import reslearn.model.paket.ResEinheit;

public class Diagramm {

	private int resFeldZeile = DisplayCanvas.resFeldZeile;
	private int resFeldSpalte = DisplayCanvas.resFeldSpalte;
	ResFeld[][] resFeldArray;
	Rectangle[][] rectangleArray;
	View view = new View();

	public Diagramm() {

	}

	public void zeichneCanvas(Canvas canvas) {
		// Zeichne 4 x Rahmen & 2 x Koordinaten-Achsen
		GraphicsContext gc = canvas.getGraphicsContext2D();
		gc.setFont(new Font("Arial", DisplayCanvas.schriftGroesse));
		gc.setStroke(Color.GRAY);
		gc.strokeLine(0, canvas.getHeight(), 0, 0);
		gc.strokeLine(0, canvas.getHeight(), canvas.getWidth(), canvas.getHeight());
		gc.strokeLine(0, 0, canvas.getWidth(), 0);
		gc.strokeLine(canvas.getWidth(), canvas.getHeight(), canvas.getWidth(), 0);

		gc.setLineWidth(4);
		gc.setStroke(Color.rgb(205, 133, 63));
		gc.strokeLine(DisplayCanvas.resFeldBreite, canvas.getHeight() - DisplayCanvas.resFeldLaenge, DisplayCanvas.resFeldBreite, DisplayCanvas.resFeldLaenge - DisplayCanvas.spaltY); // Y-Achse
		gc.strokeLine(DisplayCanvas.resFeldBreite, canvas.getHeight() - DisplayCanvas.resFeldLaenge, canvas.getWidth() - DisplayCanvas.gesamtAbstandX + DisplayCanvas.spaltX, canvas.getHeight() - DisplayCanvas.resFeldLaenge); // X-Achse

		gc.bezierCurveTo(20, 30, 40, 50, 60, 70);
		// Koordinatenbeschriftung
		gc.setLineWidth(1);
		gc.setStroke(Color.BLACK);

		for (double i = DisplayCanvas.gesamtAbstandX; i < canvas.getWidth(); i += 5 * DisplayCanvas.resFeldBreite) {
			int counterYAchse = 0;
			if (i != DisplayCanvas.gesamtAbstandX) {
				gc.strokeLine(i, canvas.getHeight() - DisplayCanvas.resFeldBreite / 1.5, i, canvas.getHeight() - DisplayCanvas.resFeldLaenge - DisplayCanvas.spaltY);
				counterYAchse += 5;
				gc.fillText(String.valueOf(counterYAchse), i - DisplayCanvas.spaltX, canvas.getHeight());
			}
		}

		for (double i = canvas.getHeight() - DisplayCanvas.resFeldLaenge - DisplayCanvas.spaltY; i > DisplayCanvas.gesamtAbstandY; i -= 5 * DisplayCanvas.resFeldBreite) {
			int counterXAchse = 0;
			if (i != canvas.getHeight() - DisplayCanvas.resFeldLaenge - DisplayCanvas.spaltY) {
				gc.strokeLine(DisplayCanvas.resFeldBreite - DisplayCanvas.resFeldBreite / 4, i, DisplayCanvas.gesamtAbstandX, i);
				counterXAchse += 5;
				gc.fillText(String.valueOf(counterXAchse), 0, i + DisplayCanvas.spaltY);
			}
		}

		// Achsenbeschriftung
		gc.fillText("Tage", canvas.getWidth() - DisplayCanvas.resFeldBreite - DisplayCanvas.spaltX * 2,
				canvas.getHeight() - DisplayCanvas.spaltY);
		gc.fillText("Personen", 2, DisplayCanvas.spaltY * 2 + 1);

		zeichneWeisseFelder();
	}

	// Weiße Klötzchen
	private void zeichneWeisseFelder() {
		rectangleArray = new Rectangle[resFeldZeile][resFeldSpalte];
		for (int i = 0; i < resFeldZeile; i++) {
			for (int j = 0; j < resFeldSpalte; j++) {
				rectangleArray[i][j] = new Rectangle(i * DisplayCanvas.resFeldBreite, j * DisplayCanvas.resFeldLaenge, DisplayCanvas.resFeldBreite, DisplayCanvas.resFeldLaenge);
				rectangleArray[i][j].setFill(Color.WHITE);
				rectangleArray[i][j].setStroke(Color.GRAY);
				view.pane.getChildren().add(rectangleArray[i][j]);
			}
		}
	}

	// Farbige Pakete
	public ResFeld[][] zeichneTeilpakete(ResEinheit[][] koordinatenSystem) {
		resFeldArray = new ResFeld[resFeldZeile][resFeldSpalte];
		for (int i = 0; i < koordinatenSystem.length; i++) {
			for (int j = 0; j < koordinatenSystem[i].length; j++) {
				if (koordinatenSystem[i][j] != null) {
					resFeldArray[i][j] = new ResFeld(j * DisplayCanvas.resFeldBreite, i * DisplayCanvas.resFeldLaenge, koordinatenSystem[i][j]);
					view.pane.getChildren().add(resFeldArray[i][j]);
					view.controllerCanvas.makeDraggable(resFeldArray[i][j]);
				}
			}
		}
		return resFeldArray;
	}

	public Rectangle[][] getRectangleArray(){
		return rectangleArray;
	}

	public ResFeld[][] getResFeldArray(){
		return resFeldArray;
	}
}
