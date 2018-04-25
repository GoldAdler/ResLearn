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
	int i;
	int j;
	
    public void zeichneCanvas(GraphicsContext gc, Canvas canvas) {
    	
        gc.setStroke(Color.GRAY);
        gc.strokeLine(0, canvas.getHeight(), 0, 0);
        gc.strokeLine(0, canvas.getHeight(), canvas.getWidth(), canvas.getHeight());
        gc.strokeLine(0, 0, canvas.getWidth(), 0);
        gc.strokeLine(canvas.getWidth(), canvas.getHeight(), canvas.getWidth(), 0);

        gc.setLineWidth(4);
        gc.strokeLine(abstandX, canvas.getHeight()-abstandY, abstandX, abstandY - spaltY);
        gc.strokeLine(abstandX, canvas.getHeight()-abstandY, canvas.getWidth()-abstandX + spaltX, canvas.getHeight()-abstandY);
        
        
        
		feld = new ResFeld[i][j];
		for(i=0; i<430;i+=10) {
			for(j=0; j<280;j+=10) {
//				feld[i][j] = new ResFeld();
				ResFeld.zeichneDiagramm(gc, canvas, abstandX, abstandY, spaltX, spaltY, i, j);
				
			}
		}
//		feld[120][40].setzeFarbe(gc);
    }
	
    public void zeichneTeilpaket() {

    }
}
