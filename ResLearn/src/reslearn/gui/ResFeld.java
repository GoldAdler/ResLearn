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
	private final Random random = new Random();

	public ResFeld(double x, double y, ResEinheit resEinheit) {
		super(x, y, DisplayCanvas.resFeldBreite, DisplayCanvas.resFeldLaenge);
		this.resEinheit = resEinheit;
		setFill(setFarbe(resEinheit));
	}

	private Color setFarbe(ResEinheit resEinheit) {
		Arbeitspaket arbeitspaket = resEinheit.getTeilpaket().getArbeitspaket();


		// Arbeitspaket hat bereits eine zugewiesene Farbe
		if (arbeitspaketFarben.containsKey(arbeitspaket)) {
			return arbeitspaketFarben.get(arbeitspaket);
		}

		double hue = random.nextInt(18) * 20.0;
		double saturation = 0.7d + 0.3d * Math.round(Math.random());// 1.0 for brilliant, 0.0 for dull
		hue = pruefeFarbeBereitsBenutzt(hue);
		double luminance = 1.0d; // 1.0 for brighter, 0.0 for black
		Color color = Color.hsb(hue, saturation, luminance, 0.7);
		arbeitspaketFarben.put(arbeitspaket, color);

		return color;
	}

	private double pruefeFarbeBereitsBenutzt(double hue) {
		for (Map.Entry<Arbeitspaket, Color> entry : arbeitspaketFarben.entrySet()) {
			double entryHue = entry.getValue().getHue();
			if (entryHue % 1 > 0.0001) {
				entryHue = Math.floor(entryHue) + 1;
			} else {
				entryHue = Math.floor(entryHue);
			}
			if (entryHue == hue) {
				hue = random.nextInt(18) * 20.0;
				hue = pruefeFarbeBereitsBenutzt(hue);
			}
		}
		return hue;
	}

	public ResEinheit getResEinheit() {
		return resEinheit;
	}
}
