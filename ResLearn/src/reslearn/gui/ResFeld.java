package reslearn.gui;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import reslearn.model.paket.Arbeitspaket;
import reslearn.model.paket.ResEinheit;

public class ResFeld extends Rectangle {

	private ResEinheit resEinheit;

	private HashMap<Arbeitspaket, Color> arbeitspaketFarben = new HashMap<Arbeitspaket, Color>();
	public ResFeld(double x, double y, ResEinheit resEinheit) {
		super(x, y, DisplayCanvas.resFeldBreite, DisplayCanvas.resFeldLaenge);
		this.resEinheit = resEinheit;
	}

	public ResEinheit getResEinheit() {
		return resEinheit;
	}
}