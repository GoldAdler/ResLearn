package reslearn.gui;

import javafx.geometry.Rectangle2D;
import javafx.stage.Screen;
import reslearn.model.resCanvas.ResCanvas;

public final class DisplayCanvas {

	private static Screen screen = Screen.getPrimary();
	private static Rectangle2D bounds = screen.getVisualBounds();
	public static final double faktor = bounds.getWidth() / 1920.0;

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
	public static final int canvasStartpunktY = ((int) bounds.getHeight() - canvasLaenge) / 2 + (5 * resFeldBreite);

	public static final int paneLayoutX = canvasStartpunktX + resFeldBreite + spaltX;
	public static final int paneLayoutY = canvasStartpunktY + resFeldLaenge;

	public static final int abstandX = resFeldBreite;
	public static final int abstandY = resFeldLaenge;

	public static final int gesamtAbstandX = abstandX + spaltX;
	public static final int gesamtAbstandY = abstandY + spaltY;

	public static final int tabelleBreite = canvasStartpunktX - 2 * resFeldBreite;
	public static final int tabelleLaenge = 15 * resFeldBreite;

	public static final int tabelleLayoutX = resFeldBreite;
	public static final int tabelleLayoutY = 4 * resFeldBreite;

	public static final int buttonLoesungsmodusLayoutX = resFeldBreite;
	public static final int buttonLoesungsmodusLayoutY = tabelleLayoutY + tabelleLaenge + resFeldBreite;
	public static final int buttonLoesungsmodusBreite = (tabelleBreite - resFeldBreite) / 2;

	public static final int tabelleArbeitspaketBreite = canvasBreite + spaltX;
	public static final int tabelleArbeitspaketLaenge = (int) bounds.getHeight() -  canvasLaenge - 8 * resFeldBreite;

	public static final int tabelleArbeitspaketLayoutX = canvasStartpunktX;

	// Tabelle überhalb des Canvas (am Rand bündig)
	public static final int tabelleArbeitspaketLayoutY = 4 * resFeldBreite;

	// Tabelle unterhalb des Canvas (ein Feld Abstand)
	// public static final int tabelleArbeitspaketLayoutY = canvasStartpunktY +
	// canvasLaenge + resFeldLaenge;

	public static final int paneAufgabeLadenBreite = (int) bounds.getWidth();
	public static final int paneAufgabeLadenHoehe = (int) bounds.getHeight();

	public static final int hoeheUeberschrift = (int) (faktor * 60);

	public static final int scrolliHoehe = (int) (paneAufgabeLadenHoehe - hoeheUeberschrift * 1.5);

	public static final int buttonBreite = (int) (faktor * 250);
	public static final int buttonHoehe = (int) (faktor * 300);
	public static final int abstandButtonX = (paneAufgabeLadenBreite - 5 * buttonBreite) / 6;
	public static final int abstandButtonY = buttonHoehe + abstandButtonX;
	public static final int buttonXStart = abstandButtonX;
	public static final int buttonYStart = hoeheUeberschrift * 2;

	public static final int legendeHoehe = (int) (faktor * 43);
	public static final int legendeKreisRadius = (int) (faktor * 15);
	public static final int legendeKreisStartpunktX = (int) (faktor * 20);
	public static final int legendeKreisStartpunktY = (int) (faktor * 22);

	public static final int legendeAbstand = (int) (faktor * 85);

	public static final int breiteFehlermeldung = (int) (bounds.getWidth() - canvasBreite - canvasStartpunktX
			- abstandX * 2);

}
