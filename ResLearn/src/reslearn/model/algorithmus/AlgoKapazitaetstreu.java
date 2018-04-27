package reslearn.model.algorithmus;

import reslearn.model.paket.ResEinheit;
import reslearn.model.resCanvas.ResCanvas;

public class AlgoKapazitaetstreu extends Algorithmus {

	private static AlgoKapazitaetstreu algoKapazitaetstreu;

	private AlgoKapazitaetstreu() {
	}

	public static AlgoKapazitaetstreu getInstance() {
		if (algoKapazitaetstreu == null) {
			algoKapazitaetstreu = new AlgoKapazitaetstreu();
		}
		return algoKapazitaetstreu;
	}

	@Override
	public ResEinheit[][] algoDurchfuehren(ResCanvas resCanvas) {

		return null;
	}

}
