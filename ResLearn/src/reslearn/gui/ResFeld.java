package reslearn.gui;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class ResFeld extends Canvas{
	
	GraphicsContext gc;
	private static int breite = 20;
	private static int laenge = 20;
	ResFeld[][] resfeld;
	
	public ResFeld() {
		
	}
	
	public ResFeld(GraphicsContext gc, int breite, int laenge) {
		this.gc = gc;
		ResFeld.breite = breite;
		ResFeld.laenge = laenge;
	}
	
    public static void zeichneDiagramm(GraphicsContext gc, Canvas canvas, int abstandX, int abstandY, int spaltX, int spaltY, int i, int j) {
        gc.setFill(Color.rgb(255, 0, 0));
        gc.setLineWidth(1);
        gc.strokeRect(spaltX + abstandX + i*2, canvas.getHeight() - spaltY - abstandY -20 - j*2, breite, laenge);
        gc.fillRect(spaltX + abstandX + i*2, canvas.getHeight() - spaltY - abstandY -20 - j*2, breite, laenge);
 
    }
    
    
    public void setzeFeld(GraphicsContext gc) {
    	
    }
    
    public int getBreite() {
    	return breite;
    }
    
    public void setBreite(int breite) {
    	this.breite = breite;
    }
    
    public int getLaenge() {
    	return laenge;
    }
    
    public void setLaenge(int laenge) {
    	this.laenge = laenge;
    }
	
    public int getFlaeche() {
    	return laenge*breite;
    }
}
