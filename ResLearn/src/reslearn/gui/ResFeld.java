package reslearn.gui;

import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import reslearn.model.paket.ResEinheit;
import reslearn.model.paket.Teilpaket;

public class ResFeld extends Rectangle{
	
	private int breite = 20;
	private int laenge = 20;
	private ResEinheit resEinheit;
	
	public ResFeld(double x, double y, double width, double height) {
		super(x,y, width,height);
	}
    
	public ResFeld setzeFeld(int x, int y, int i, int j, ResEinheit resEinheit) {
		this.resEinheit = resEinheit;
		
		ResFeld res = new ResFeld(i*2, j*2, breite, laenge);
		res.setFill(setFarbe(resEinheit));
		return res;
	}
	
    public static Color setFarbe(ResEinheit resEinheit) {
		switch (resEinheit.getTeilpaket().getArbeitspaket().getId()) {
		case "A": 
			return Color.FIREBRICK.deriveColor(1, 1, 1, 0.7);
		case "B":
			return Color.GREEN.deriveColor(1, 1, 1, 0.7);
		case "C":
			return Color.ROYALBLUE.deriveColor(1, 1, 1, 0.7);
		case "D":
			return Color.GOLD.deriveColor(1, 1, 1, 0.7);
		case "E":
			return Color.GOLDENROD.deriveColor(1, 1, 1, 0.7);
		default:
			return Color.WHITE;
		}
	}
    
    public Teilpaket getTeilpaket() {
    	return resEinheit.getTeilpaket();
    }
    
}
