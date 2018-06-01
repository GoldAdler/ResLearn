package reslearn.model.algorithmus;

import java.util.ArrayList;

import reslearn.model.paket.Arbeitspaket;
import reslearn.model.paket.ResEinheit;
import reslearn.model.resCanvas.ResCanvas;

public class AlgoTermintreu extends Algorithmus {
	private static AlgoTermintreu algoTermintreu;

	private AlgoTermintreu() {
	}

	public static AlgoTermintreu getInstance() {
		if (algoTermintreu == null) {
			algoTermintreu = new AlgoTermintreu();
		}
		return algoTermintreu;
	}

	@Override
	public ResCanvas algoDurchfuehren(ResCanvas resCanvas) {
		ResCanvas ersteSchritt = resCanvas.copyResCanvas();
		AlgoErsteSchritt.getInstance().algoDurchfuehren(ersteSchritt);
		ersteSchritt.aktuallisiereHistorie();

		resCanvas.setHistorieKoordinatenSystem(ersteSchritt.getHistorieKoordinatenSystem());

		AlgoErsteSchritt.sortiereAP(resCanvas);

		String letzteApID = resCanvas.getArbeitspaketListe().get(resCanvas.getArbeitspaketListe().size() - 1).getId();

		ArrayList<ResCanvas> moeglicheLoesungenResCanvas = new ArrayList<>();

		this.simulationDurchfuehren(resCanvas, moeglicheLoesungenResCanvas, null, letzteApID);

		ArrayList<ResCanvas> reduzierteMoeglicheLoesungenResCanvas = new ArrayList<ResCanvas>();

		loescheLoesungenMitLuecken(moeglicheLoesungenResCanvas, reduzierteMoeglicheLoesungenResCanvas);

		int yMin = 0;
		for (ResCanvas canvas : reduzierteMoeglicheLoesungenResCanvas) {
			int yWert = canvas.berechneMaxY();
			if (yWert > yMin) {
				yMin = yWert;
			}
		}

		ArrayList<ResCanvas> flacheLoesungen = new ArrayList<>();
		for (ResCanvas canvas : reduzierteMoeglicheLoesungenResCanvas) {
			int yWert = canvas.berechneMaxY();
			if (yWert == yMin) {
				flacheLoesungen.add(canvas);
			}
		}

		ArrayList<ResCanvas> optimalListe = this.rankingWeicheKriterien(flacheLoesungen, yMin);

		for (ResCanvas lul : optimalListe) {
			Algorithmus.ausgebenHistorie(lul.getKoordinatenSystem());
		}
		return null;
	}

	private void loescheLoesungenMitLuecken(ArrayList<ResCanvas> moeglicheLoesungenResCanvas,
			ArrayList<ResCanvas> reduzierteMoeglicheLoesungenResCanvas) {
		for (ResCanvas canvas : moeglicheLoesungenResCanvas) {

			ResEinheit[][] koordinaten = canvas.getKoordinatenSystem();

			ResEinheit gefundenRE = null;

			boolean nullGefunden = false;
			boolean loeschen = false;

			for (int x = 0; x < ResCanvas.koorBreite; x++) {

				gefundenRE = koordinaten[ResCanvas.koorHoehe - 1][x];

				if (gefundenRE == null) {
					nullGefunden = true;
				}

				if (gefundenRE != null && nullGefunden == true) {
					loeschen = true;
					break;
				}

			}

			if (loeschen == false) {
				reduzierteMoeglicheLoesungenResCanvas.add(canvas);
			}

		}
	}

	private void simulationDurchfuehren(ResCanvas resCanvas, ArrayList<ResCanvas> moeglicheLoesungenResCanvas,
			String nichtMehrAnschauenApID, String letzteApID) {

		ArrayList<ResCanvas> simLoesungenResCanvas = new ArrayList<ResCanvas>();

		ArrayList<Arbeitspaket> arbeitspaketListe = resCanvas.getArbeitspaketListe();

		AlgoErsteSchritt.sortiereAP(resCanvas);

		int startAPint = 0;
		if (!(nichtMehrAnschauenApID == null)) {
			for (Arbeitspaket simAp : arbeitspaketListe) {
				if (simAp.getId() == nichtMehrAnschauenApID) {
					startAPint++;
					break;
				}
				startAPint++;
			}
		}

		Arbeitspaket startAP = arbeitspaketListe.get(startAPint);
		String apID = startAP.getId();

		System.out.println("Beginn Sim für AP: " + apID);
		this.simuliere(resCanvas, startAP, simLoesungenResCanvas);

		int nummer = 0;
		for (ResCanvas sim : simLoesungenResCanvas) {

			if (apID == letzteApID) {
				moeglicheLoesungenResCanvas.add(sim);
			} else {

				++nummer;
				System.out.println("Beginn Sim für Lösungen von AP : " + apID + " " + nummer + " von "
						+ simLoesungenResCanvas.size());

				this.simulationDurchfuehren(sim, moeglicheLoesungenResCanvas, apID, letzteApID);
			}
		}

	}

	private void simuliere(ResCanvas resCanvas, Arbeitspaket zuVerschiebenAp,
			ArrayList<ResCanvas> simLoesungenResCanvas) {

		ResCanvas simulation = null;
		Arbeitspaket copyZuVerschiebenAP = null;

		for (int x = zuVerschiebenAp.getFaz() - 1; x < zuVerschiebenAp.getSaz(); x++) {

			simulation = resCanvas.copyResCanvas(zuVerschiebenAp);

			copyZuVerschiebenAP = simulation.findeAPnachID(zuVerschiebenAp.getId());
			copyZuVerschiebenAP.neuSetzenTermin(x, simulation);
			simLoesungenResCanvas.add(simulation);
		}

	}

	/**
	 * Die möglichen Lösungen werden Anhand des weichen Abgleiches bewertet. Siehe
	 * Skript.
	 *
	 * @param keineZeitueberschreitung
	 * @return
	 */
	private ArrayList<ResCanvas> rankingWeicheKriterien(ArrayList<ResCanvas> keineZeitueberschreitung, int grenze) {
		int min = Integer.MAX_VALUE;

		ArrayList<ResCanvas> durchlaufenListe = keineZeitueberschreitung;
		ArrayList<ResCanvas> optimalListe = new ArrayList<>();
		ResEinheit[][] koordinatenSystem = null;
		for (int y = grenze; y < ResCanvas.koorHoehe; y++) {

			for (ResCanvas canvas : durchlaufenListe) {
				koordinatenSystem = canvas.getKoordinatenSystem();
				int counter = 0;

				for (int x = 0; x < ResCanvas.koorBreite; x++) {
					if (koordinatenSystem[y][x] != null) {
						counter++;
					}
				}

				if (counter < min) {
					optimalListe.clear();
					optimalListe.add(canvas);
					min = counter;
				} else if (counter == min) {
					optimalListe.add(canvas);
				}
			}
			durchlaufenListe.clear();
			for (ResCanvas rescanvas : optimalListe) {
				durchlaufenListe.add(rescanvas);
			}

		}
		return optimalListe;
	}
}
