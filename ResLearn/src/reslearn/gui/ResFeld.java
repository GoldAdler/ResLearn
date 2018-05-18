package reslearn.gui;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Random;

import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import reslearn.model.paket.Arbeitspaket;
import reslearn.model.paket.ResEinheit;
import reslearn.model.paket.Teilpaket;

public class ResFeld extends Rectangle {

	public static int breite = 20;
	public static int laenge = 20;
	private ResEinheit resEinheit;
	ResFeld resFeld;
	Teilpaket teilpaket;
	Rectangle bound;
	private LinkedList<ResFeld> resFeldListe = new LinkedList<ResFeld>();
	private static HashMap<Arbeitspaket, Color> arbeitspaketFarben = new HashMap<Arbeitspaket, Color>();

	public ResFeld(double x, double y, double width, double height) {
		super(x, y, width, height);
	}

	public ResFeld setzeFeld(int i, int j, ResEinheit resEinheit) {
		this.resEinheit = resEinheit;
		resFeld = new ResFeld(i * 20, j * 20, breite, laenge);
		resFeld.setFill(setzeFarbe(resEinheit));
		resFeld.setResEinheit(resEinheit);
		resFeld.setTeilpaket(resEinheit.getTeilpaket());
		return resFeld;
	}

	public static Color setzeFarbe(ResEinheit resEinheit) {
		Arbeitspaket arbeitspaket = resEinheit.getTeilpaket().getArbeitspaket();

		// Arbeitspaket hat bereits eine zugewiesene Farbe
		if (arbeitspaketFarben.containsKey(arbeitspaket)) {
			return arbeitspaketFarben.get(arbeitspaket);
		}

		Random rand = new Random();
		double r = rand.nextDouble();
		double g = rand.nextDouble();
		double b = rand.nextDouble();
		Color color = Color.color(r, g, b, 0.7);
		color = color.saturate(); // Mehr Sättigung
		arbeitspaketFarben.put(arbeitspaket, color);
		return color;
	}

	public Rectangle getTeilpaketBounds(Teilpaket teilpaketClicked) {
		for (int i = 0; i < Diagramm.res.length; i += 10) {
			for (int j = 0; j < Diagramm.res[i].length; j += 10) {
				if (Diagramm.res[i][j] != null) {
					if (teilpaketClicked == Diagramm.res[i][j].getResEinheit().getTeilpaket()) {

						for (int a = Diagramm.res[i][j].getTeilpaket().getArbeitspaket().getAufwand(); a > 0; a--) {
							resFeldListe.add(Diagramm.res[i][j]);

							bound = new Rectangle(resFeldListe.getFirst().getBoundsInParent().getMinX(),
									resFeldListe.getFirst().getBoundsInParent().getMinY(),
									Diagramm.res[i][j].getTeilpaket().getVorgangsdauer() * 20,
									Diagramm.res[i][j].getTeilpaket().getMitarbeiteranzahl() * 20);
						}
					}
				}
			}
		}
		return bound;
	}

	public void setTeilpaket(Teilpaket teilpaket) {
		this.teilpaket = teilpaket;
	}

	public Teilpaket getTeilpaket() {
		return resEinheit.getTeilpaket();
	}

	public ResEinheit getResEinheit() {
		return resEinheit;
	}

	public void setResEinheit(ResEinheit resEinheit) {
		this.resEinheit = resEinheit;
	}

}
