package reslearn.gui.rescanvas;

import java.util.ArrayList;
import java.util.Collections;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import reslearn.model.paket.Arbeitspaket;
import reslearn.model.paket.ResEinheit;
import reslearn.model.resCanvas.ResCanvas;

public class Diagramm {

	private ResFeld[][] resFeldArray;

	public Diagramm() {

	}

	/**
	 * Zeichnet 4 Rahmen und 2 Koordinatenachsen, inklusive Beschriftung der Achsen
	 * @param canvas
	 * @return zeichneWeisseFelder()
	 */
	public Rectangle[][] zeichneCanvas(Canvas canvas) {

		GraphicsContext gc = canvas.getGraphicsContext2D();
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

		int counterYAchse = 0;
		int counterXAchse = 0;

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

		return zeichneWeisseFelder();
	}

	/**
	 * Zeichnet die Weißen Klötzchen als Hintergrund.
	 * @return rectangleArray
	 */
	private Rectangle[][] zeichneWeisseFelder() {
		Rectangle[][] rectangleArray = new Rectangle[DisplayCanvas.resFeldZeile][DisplayCanvas.resFeldSpalte];
		for (int i = 0; i < DisplayCanvas.resFeldZeile; i++) {
			for (int j = 0; j < DisplayCanvas.resFeldSpalte; j++) {
				rectangleArray[i][j] = new Rectangle(i * DisplayCanvas.resFeldBreite, j * DisplayCanvas.resFeldLaenge,
						DisplayCanvas.resFeldBreite, DisplayCanvas.resFeldLaenge);
				rectangleArray[i][j].setFill(Color.WHITE);
				rectangleArray[i][j].setStroke(Color.GRAY);
			}
		}
		return rectangleArray;
	}

	/**
	 * Entnimmt dem Koordinatensystem die Position jeder ResEinheit, um dann die
	 * farbigen ResFelder an die entsprechende Position zu zeichnen.
	 * @param koordinatenSystem
	 * @return resFeldArray
	 */
	public ResFeld[][] zeichneTeilpakete(ResEinheit[][] koordinatenSystem) {
		resFeldArray = new ResFeld[DisplayCanvas.resFeldSpalte][DisplayCanvas.resFeldZeile];
		for (int i = 0; i < koordinatenSystem.length; i++) {
			for (int j = 0; j < koordinatenSystem[i].length; j++) {
				if (koordinatenSystem[i][j] != null) {
					resFeldArray[i][j] = new ResFeld(j * DisplayCanvas.resFeldBreite, i * DisplayCanvas.resFeldLaenge,
							koordinatenSystem[i][j]);
				}
			}
		}
		return resFeldArray;
	}

	/**
	 * Zeichnet die farbigen Pakete oben links (für den Erster Schritt Modus)
	 * @param koordinatenSystem
	 * @param resCanvas
	 * @return resFeldArray
	 */
	public ResFeld[][] zeichneTeilpaketeOben(ResEinheit[][] koordinatenSystem, ResCanvas resCanvas) {
		ArrayList<Arbeitspaket> arbeitspaketList = new ArrayList<>();
		Arbeitspaket tmpAP;
		int xCounter = 0;

		for (ResEinheit[] resAr : koordinatenSystem) {
			for (ResEinheit res : resAr) {
				if (res != null) {
					tmpAP = res.getTeilpaket().getArbeitspaket();
					if (!arbeitspaketList.contains(tmpAP)) {
						arbeitspaketList.add(tmpAP);
					}
				}
			}
		}

		Collections.shuffle(arbeitspaketList);
		for (Arbeitspaket ap : arbeitspaketList) {
			ap.reset(xCounter, resCanvas);
			xCounter += ap.getVorgangsdauer();
		}

		resFeldArray = new ResFeld[DisplayCanvas.resFeldSpalte][DisplayCanvas.resFeldZeile];
		for (int i = 0; i < koordinatenSystem.length; i++) {
			for (int j = 0; j < koordinatenSystem[i].length; j++) {
				if (koordinatenSystem[i][j] != null) {
					resFeldArray[i][j] = new ResFeld(j * DisplayCanvas.resFeldBreite, i * DisplayCanvas.resFeldLaenge,
							koordinatenSystem[i][j]);
				}
			}
		}
		return resFeldArray;
	}

	public ResFeld[][] getResFeldArray() {
		return resFeldArray;
	}
}