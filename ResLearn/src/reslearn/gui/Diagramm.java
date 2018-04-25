package reslearn.gui;

import javafx.scene.canvas.*;
import javafx.scene.paint.Color;
import reslearn.model.paket.ResEinheit;
import reslearn.gui.ResFeld;

public class Diagramm {

	Canvas canvas;
	private int abstandX = 20;
	private int abstandY = 20;
	private int spaltX = 5;
	private int spaltY = 5;
	private ResEinheit[][] koordinatenSystem;
	ResFeld[][] feld;
	int i, j;
	int zeile = 430;
	int spalte = 280;

	public Diagramm(ResEinheit[][] koordinateSystem) {
		this.koordinatenSystem = koordinateSystem;
	}

	public void zeichneCanvas(GraphicsContext gc, Canvas canvas) {

		gc.setStroke(Color.GRAY);
		gc.strokeLine(0, canvas.getHeight(), 0, 0);
		gc.strokeLine(0, canvas.getHeight(), canvas.getWidth(), canvas.getHeight());
		gc.strokeLine(0, 0, canvas.getWidth(), 0);
		gc.strokeLine(canvas.getWidth(), canvas.getHeight(), canvas.getWidth(), 0);

		gc.setLineWidth(4);
		gc.strokeLine(abstandX, canvas.getHeight() - abstandY, abstandX, abstandY - spaltY);
		gc.strokeLine(abstandX, canvas.getHeight() - abstandY, canvas.getWidth() - abstandX + spaltX,
				canvas.getHeight() - abstandY);

		feld = new ResFeld[zeile][spalte];
		for (i = 0; i < zeile; i += 10) {
			for (j = 0; j < spalte; j += 10) {
				feld[i][j] = new ResFeld(gc, canvas, abstandX, abstandY, spaltX, spaltY, i, j);
			}
		}
	}

	public void zeichneResEinheit(GraphicsContext gc, Canvas myCanvas) {
		for (int i = 0; i < koordinatenSystem.length; i++) {
			for (int j = 0; j < koordinatenSystem[i].length; j++) {
				if (koordinatenSystem[i][j] != null) {
					feld[i*10][j*10].setzeFeld(gc, myCanvas, abstandX, abstandY, spaltX, spaltY, j*10, i*10, koordinatenSystem[i][j]);
				}
			}
		}
	}
}
