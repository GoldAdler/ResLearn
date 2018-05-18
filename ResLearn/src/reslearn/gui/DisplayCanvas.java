package reslearn.gui;

import javafx.geometry.Rectangle2D;
import javafx.stage.Screen;
import reslearn.model.resCanvas.ResCanvas;

public final class DisplayCanvas {

	private static Screen screen = Screen.getPrimary();
	private static Rectangle2D bounds = screen.getVisualBounds();

	public static final int resFeldBreite = (int) (bounds.getWidth() / 1920 * 25);
	public static final int resFeldLaenge = resFeldBreite;

	public static final int resFeldZeile = ResCanvas.koorBreite;
	public static final int resFeldSpalte = ResCanvas.koorHoehe;

	public static final int paneBreite = resFeldBreite * resFeldZeile; // 860 = 20 * 43
	public static final int paneLaenge = resFeldLaenge * resFeldSpalte; // 560 = 20 * 28

	public static final int spaltX = resFeldBreite / 4;
	public static final int spaltY = resFeldLaenge / 4;

	public static final int canvasBreite = 2 * resFeldBreite + paneBreite + spaltX; // 900
	public static final int canvasLaenge = 2 * resFeldLaenge + paneLaenge + spaltY; // 600

	public static final int canvasStartpunktX = ((int) bounds.getWidth() - canvasBreite) / 2;
	public static final int canvasStartpunktY = ((int) bounds.getHeight() - canvasLaenge) / 2;
}
