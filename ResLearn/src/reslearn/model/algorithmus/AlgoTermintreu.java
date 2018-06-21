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

		String letzteApID = simulationVorbereiten(resCanvas);

		ArrayList<ResCanvas> moeglicheLoesungenResCanvas = new ArrayList<>();

		this.simulationDurchfuehren(resCanvas, moeglicheLoesungenResCanvas, null, letzteApID);

		ResCanvas result = bewerteLoesungen(moeglicheLoesungenResCanvas);

		return result;
	}

	/**
	 * Bewertet die Lösungen anhand der Kriterien des Algotermintreu.
	 *
	 * @param moeglicheLoesungenResCanvas
	 * @return
	 */
	private ResCanvas bewerteLoesungen(ArrayList<ResCanvas> moeglicheLoesungenResCanvas) {
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
			Algorithmus.ausgebenKurzerTest(lul.getKoordinatenSystem());
		}

		// gebe erstes ResCanvas in der optimalListe zurück. Je früher es in der Liste
		// liegt, desto früher sind die Zeitpunkte der Arbeitspakete.
		ResCanvas result = optimalListe.get(0);
		result.aktuallisiereHistorie();
		return result;
	}

	/**
	 * Führt den AlgoErsteSchritt durch und aktuallierst dabei die Historie
	 * entsprechend.
	 *
	 * Der zurückgegeben String gibt die ID des Arbeitspaktes zurück, welches als
	 * letztes im Koordinantesystem liegt. Letztes im Sinne von spätliegenstes.
	 *
	 * @param resCanvas
	 * @return
	 */
	private String simulationVorbereiten(ResCanvas resCanvas) {
		ResCanvas ersteSchritt = resCanvas.copyResCanvas();
		AlgoErsteSchritt.getInstance().algoDurchfuehren(ersteSchritt);
		ersteSchritt.aktuallisiereHistorie();

		resCanvas.setHistorieKoordinatenSystem(ersteSchritt.getHistorieKoordinatenSystem());
		resCanvas.setHistorienArbeitspaketListe(ersteSchritt.getHistorienArbeitspaketListe());

		resCanvas.sortiereAP();

		String letzteApID = resCanvas.getArbeitspaketListe().get(resCanvas.getArbeitspaketListe().size() - 1)
				.getIdIntern();
		return letzteApID;
	}

	/**
	 *
	 * Bewertet die übergebenen Lösungen. Sind in diesen Lücken zwischen den
	 * Arbeitspaketen vorhanden, werden diese Lösungen nicht mehr von der Methode
	 * zurückgegeben.
	 *
	 * @param moeglicheLoesungenResCanvas
	 * @param reduzierteMoeglicheLoesungenResCanvas
	 */
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

	/**
	 * Führt die simulation durch.
	 *
	 * @param resCanvas
	 * @param moeglicheLoesungenResCanvas
	 * @param nichtMehrAnschauenApID
	 * @param letzteApID
	 */
	private void simulationDurchfuehren(ResCanvas resCanvas, ArrayList<ResCanvas> moeglicheLoesungenResCanvas,
			String nichtMehrAnschauenApID, String letzteApID) {

		ArrayList<ResCanvas> simLoesungenResCanvas = new ArrayList<ResCanvas>();

		ArrayList<Arbeitspaket> arbeitspaketListe = resCanvas.getArbeitspaketListe();

		resCanvas.sortiereAP();

		int startAPint = 0;
		if (!(nichtMehrAnschauenApID == null)) {
			for (Arbeitspaket simAp : arbeitspaketListe) {
				if (simAp.getIdIntern() == nichtMehrAnschauenApID) {
					startAPint++;
					break;
				}
				startAPint++;
			}
		}

		Arbeitspaket startAP = arbeitspaketListe.get(startAPint);
		String apID = startAP.getIdIntern();

		// System.out.println("Beginn Sim für AP: " + apID);
		this.simuliere(resCanvas, startAP, simLoesungenResCanvas);

		// int nummer = 0;
		for (ResCanvas sim : simLoesungenResCanvas) {

			if (apID == letzteApID) {

				moeglicheLoesungenResCanvas.add(sim);

			} else {

				// ++nummer;
				// System.out.println("Beginn Sim für Lösungen von AP : " + apID + " " + nummer
				// + " von "
				// + simLoesungenResCanvas.size());

				this.simulationDurchfuehren(sim, moeglicheLoesungenResCanvas, apID, letzteApID);
			}
		}

	}

	/**
	 *
	 * @param resCanvas
	 * @param zuVerschiebenAp
	 * @param simLoesungenResCanvas
	 */
	private void simuliere(ResCanvas resCanvas, Arbeitspaket zuVerschiebenAp,
			ArrayList<ResCanvas> simLoesungenResCanvas) {

		ResCanvas simulation = null;
		Arbeitspaket copyZuVerschiebenAP = null;

		for (int x = zuVerschiebenAp.getFaz() - 1; x < zuVerschiebenAp.getSaz(); x++) {

			simulation = resCanvas.copyResCanvas(zuVerschiebenAp);

			copyZuVerschiebenAP = simulation.findeAPnachID(zuVerschiebenAp.getIdIntern());
			copyZuVerschiebenAP.neuSetzenStartpunkt(x, simulation);
			simLoesungenResCanvas.add(simulation);
		}

	}

}
