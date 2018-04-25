package reslearn.gui;

import javafx.scene.canvas.*;
import javafx.scene.paint.Color;

public class Diagramm {
	
	Canvas canvas;
	private int abstandX = 20;
	private int abstandY = 20;
	private int spaltX = 5;
	private int spaltY = 5;
	
    public void zeichneCanvas(GraphicsContext gc, Canvas canvas) {
        gc.setStroke(Color.GRAY);
        gc.strokeLine(0, canvas.getHeight(), 0, 0);
        gc.strokeLine(0, canvas.getHeight(), canvas.getWidth(), canvas.getHeight());
        gc.strokeLine(0, 0, canvas.getWidth(), 0);
        gc.strokeLine(canvas.getWidth(), canvas.getHeight(), canvas.getWidth(), 0);
        /*    P2
         *     |
         *    P1 
         *    	P1--P2
         */
        gc.setLineWidth(4);
        gc.strokeLine(abstandX, canvas.getHeight()-abstandY, abstandX, abstandY - spaltY);
        gc.strokeLine(abstandX, canvas.getHeight()-abstandY, canvas.getWidth()-abstandX + spaltX, canvas.getHeight()-abstandY);
        zeichneKoordinatensystem(gc,canvas);

    }

    public void zeichneKoordinatensystem(GraphicsContext gc, Canvas canvas) {
//			43*28
    	for(int i=0; i<430;i+=10) {
    		for(int j=0; j<280;j+=10) {
    	    	int random = (int)(Math.random() * 255);
    	        gc.setFill(Color.rgb(random/1, 200, random/8));
    	        gc.setLineWidth(1);
    	        gc.strokeRect(spaltX + abstandX + i*2, canvas.getHeight() - spaltY - abstandY -20 - j*2, 20, 20);
    	        gc.fillRect(spaltX + abstandX + i*2, canvas.getHeight() - spaltY - abstandY -20 - j*2, 20, 20);
    		}
    	}
    	        
    }
    
    public void zeichePaket() {
    	
    }
	
    public void zeichneTeilpaket() {
    	
    }
}
