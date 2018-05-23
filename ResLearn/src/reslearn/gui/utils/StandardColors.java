package reslearn.gui.utils;

import java.util.Random;

import javafx.scene.paint.Color;

public class StandardColors {
	public static StandardColors standardColors;
	private final Random random = new Random();

	public static StandardColors getInstance() {
		if (standardColors == null) {
			standardColors = new StandardColors();
		}
		return standardColors;
	}

	public Color getColor(int i) {
		Color c;
		switch (i) {
		case 0:
			c = Color.CRIMSON; // rot
			break;
		case 1:
			c = Color.DARKBLUE;
			break;
		case 2:
			c = Color.FORESTGREEN;
			break;
		case 3:
			c = Color.DARKORANGE;
			break;
		case 4:
			c = Color.DARKVIOLET;
			break;
		case 5:
			c = Color.DEEPSKYBLUE;
			break;
		case 6:
			c = Color.GOLD; // gelb
			break;
		case 7:
			c = Color.LIME;
			break;
		case 8:
			c = Color.ORANGERED; // lila
			break;
		case 9:
			c = Color.LIGHTSEAGREEN;
			break;
		case 10:
			c = Color.ROYALBLUE;
			break;
		case 11:
			c = Color.MEDIUMVIOLETRED;
			break;
		case 12:
			c = Color.YELLOW;
			break;
		case 13:
			c = Color.WHEAT;
			break;
		case 14:
			c = Color.LIGHTPINK;
			break;
		case 15:
			c = Color.LIGHTSTEELBLUE;
			break;
		case 16:
			c = Color.MAGENTA;
			break;
		case 17:
			c = Color.MEDIUMSPRINGGREEN;
			break;
		case 18:
			c = Color.GOLDENROD;
			break;
		case 19:
			c = Color.INDIANRED;
			break;
		case 20:
			c = Color.DARKTURQUOISE;
			break;
		default:
			c = randomColor();
			break;
		}
		c = c.deriveColor(1, 1, 1, 0.7);
		return c;
	}

	private Color randomColor() {
		Color color = Color.rgb(random.nextInt(256), random.nextInt(256), random.nextInt(256));
		return color;
	}

}
