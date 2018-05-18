package reslearn.gui;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
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
	private static final Random random = new Random();

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

		double hue = random.nextInt(18) * 20.0;

		System.out.println("Farbe zum anlegen in echt: " + hue);

		double saturation = 0.7d + 0.3d * Math.round(Math.random());// 1.0 for brilliant, 0.0 for dull
		hue = pruefeFarbeBereitsBenutzt(hue);
		double luminance = 1.0d; // 1.0 for brighter, 0.0 for black
		Color color = Color.hsb(hue, saturation, luminance, 0.7);
		// color.get
		// color.deriveColor(1, 1, 1, 0.5);
		System.out.println("Saturation: " + saturation);
		arbeitspaketFarben.put(arbeitspaket, color);
		return color;
	}

	private static double pruefeFarbeBereitsBenutzt(double hue) {
		for (Map.Entry<Arbeitspaket, Color> entry : arbeitspaketFarben.entrySet()) {
			double entryHue = entry.getValue().getHue();
			System.out.println("EntryHue:" + entryHue);
			if (entryHue % 1 > 0.0001)
				entryHue = Math.floor(entryHue) + 1;
			else
				entryHue = Math.floor(entryHue);
			System.out.println("EntryHue:" + entryHue + ", Hue: " + hue);
			if (entryHue == hue) {
				System.out.println("Treffer gefunden!");
				hue = random.nextInt(18) * 20.0;
				System.out.println("Neue Farbe: " + hue);
				hue = pruefeFarbeBereitsBenutzt(hue);

			}
		}
		return hue;
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
