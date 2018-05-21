package reslearn.gui;

import javafx.geometry.Rectangle2D;
import javafx.stage.Screen;
import reslearn.model.resCanvas.ResCanvas;

public final class DisplayCanvas {

	private static Screen screen = Screen.getPrimary();
	private static Rectangle2D bounds = screen.getVisualBounds();
	private static final double faktor = bounds.getWidth() / 1920.0;

	public static final int schriftGroesse = (int) (faktor * 15);
	public static final int resFeldBreite = (int) (faktor * 25);
	public static final int resFeldLaenge = resFeldBreite;

	public static final int resFeldZeile = ResCanvas.koorBreite;
	public static final int resFeldSpalte = ResCanvas.koorHoehe;

	public static final int paneBreite = resFeldBreite * resFeldZeile;
	public static final int paneLaenge = resFeldLaenge * resFeldSpalte;

	public static final int spaltX = resFeldBreite / 4;
	public static final int spaltY = resFeldLaenge / 4;

	public static final int canvasBreite = 2 * resFeldBreite + paneBreite + spaltX;
	public static final int canvasLaenge = 2 * resFeldLaenge + paneLaenge + spaltY;

	public static final int canvasStartpunktX = ((int) bounds.getWidth() - canvasBreite) / 2;
	public static final int canvasStartpunktY = ((int) bounds.getHeight() - canvasLaenge) / 2;

	public static final int paneLayoutX = canvasStartpunktX + resFeldBreite + spaltX;
	public static final int paneLayoutY = canvasStartpunktY + resFeldLaenge;

	public static final int abstandX = resFeldBreite;
	public static final int abstandY = resFeldLaenge;

	public static final int gesamtAbstandX = abstandX + spaltX;
	public static final int gesamtAbstandY = abstandY + spaltY;

	public static final int tabelleBreite = canvasStartpunktX - 2 * resFeldBreite;
	public static final int tabelleLaenge = canvasLaenge;

	public static final int tabelleLayoutX = resFeldBreite;
	public static final int tabelleLayoutY = canvasStartpunktY;
	
	public static final int tabelleArbeitspaketBreite = paneBreite + spaltX;
	public static final int tabelleArbeitspaketLaenge = canvasStartpunktY - 2 * resFeldLaenge;
	
	public static final int tabelleArbeitspaketLayoutX = paneLayoutX;
	
	// Tabelle �berhalb des Canvas (am Rand b�ndig)
//	public static final int tabelleArbeitspaketLayoutY = canvasStartpunktY - tabelleArbeitspaketLaenge;
	
	//Tabelle unterhalb des Canvas (ein Feld Abstand)
	public static final int tabelleArbeitspaketLayoutY = canvasStartpunktY + canvasLaenge + resFeldLaenge;

}