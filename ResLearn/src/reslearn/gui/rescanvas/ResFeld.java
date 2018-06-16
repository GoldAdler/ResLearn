package reslearn.gui.rescanvas;

import javafx.scene.shape.Rectangle;
import reslearn.model.paket.ResEinheit;

public class ResFeld extends Rectangle {

	private ResEinheit resEinheit;

	public ResFeld(double x, double y, ResEinheit resEinheit) {
		super(x, y, DisplayCanvas.resFeldBreite, DisplayCanvas.resFeldLaenge);
		this.resEinheit = resEinheit;
	}

	public ResEinheit getResEinheit() {
		return resEinheit;
	}
}