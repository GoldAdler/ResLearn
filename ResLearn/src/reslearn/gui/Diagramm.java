package reslearn.gui;

import javafx.scene.canvas.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Shape;
import reslearn.model.paket.ResEinheit;

public class Diagramm {

	static int abstandX = 20;
	static int abstandY = 20;
	static int spaltX = 5;
	static int spaltY = 5;
	static int x = abstandX + spaltX;
	static int y = + abstandY - spaltY;
	int zeile = 430;
	int spalte = 280;
	ResFeld[][] feld;
	public static ResFeld[][] res;

	
	public void zeichneCanvas(GraphicsContext gc, Canvas canvas) {
		//Zeichne 4 x Rahmen & 2 x Koordinaten-Achsen
		gc.setStroke(Color.GRAY);
		gc.strokeLine(0, canvas.getHeight(), 0, 0);
		gc.strokeLine(0, canvas.getHeight(), canvas.getWidth(), canvas.getHeight());
		gc.strokeLine(0, 0, canvas.getWidth(), 0);
		gc.strokeLine(canvas.getWidth(), canvas.getHeight(), canvas.getWidth(), 0);

		gc.setLineWidth(4);
		gc.strokeLine(abstandX, canvas.getHeight() - abstandY, abstandX, abstandY - spaltY);
		gc.strokeLine(abstandX, canvas.getHeight() - abstandY, canvas.getWidth() - abstandX + spaltX, canvas.getHeight() - abstandY);
		
		//TODO Koordinaten-Achsen um Pfeile & Beschriftung erweitern, evtl auch Holzhintergrund
		zeichneArray();
	}
	
	public void zeichneArray() {
		feld = new ResFeld[zeile][spalte];
		for (int i = 0; i < zeile; i += 10) {
			for (int j = 0; j < spalte; j += 10) {
				feld[i][j] = new ResFeld(i*2, j*2, 20, 20);
				feld[i][j].setFill(Color.TRANSPARENT);
				feld[i][j].setStroke(Color.GRAY);
				View.pane.getChildren().add(feld[i][j]);
			}
		}
	}
	
	public void zeichnePaket(ResEinheit[][] koordinatenSystem) {
		res = new ResFeld[zeile][spalte];
		for (int i = 0; i < koordinatenSystem.length; i++) {
			for (int j = 0; j < koordinatenSystem[i].length; j++) {
				if (koordinatenSystem[i][j] != null) {
					res[i][j] = feld[i*10][j*10].setzeFeld(Diagramm.x, Diagramm.y, j*10, i*10, koordinatenSystem[i][j]);
					View.pane.getChildren().add(res[i][j]);
					View.cc.makeDraggable(res[i][j]);
				}
			}
		}
	}
}
