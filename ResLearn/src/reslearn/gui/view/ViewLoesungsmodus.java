package reslearn.gui.view;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import reslearn.gui.ImportExport.AufgabeLadenImport;
import reslearn.gui.controller.ControllerCanvasLoesungsmodus;
import reslearn.gui.rescanvas.Diagramm;
import reslearn.gui.rescanvas.DisplayCanvas;
import reslearn.gui.rescanvas.ResFeld;
import reslearn.gui.utils.StandardColors;
import reslearn.model.algorithmus.AlgoKapazitaetstreu;
import reslearn.model.paket.Arbeitspaket;
import reslearn.model.paket.ResEinheit;
import reslearn.model.resCanvas.ResCanvas;

public class ViewLoesungsmodus extends Application {
	private static ViewLoesungsmodus view;
	private Stage stage;
	private ContextMenu menu;
	private MenuItem ap;
	private MenuItem reset;
	private Pane pane;
	private ControllerCanvasLoesungsmodus controllerCanvas;

	public static ViewLoesungsmodus getInstance() {
		if (view == null) {
			view = new ViewLoesungsmodus();
		}
		return view;
	}

	@Override
	public void start(Stage stage) throws Exception {

	}

	public void initializeCanvasView(Arbeitspaket[] arbeitspakete) throws IOException {

		stage = new Stage();

		Parent root = FXMLLoader.load(getClass().getResource("/reslearn/gui/fxml/Loesungsmodus.fxml"));
		Scene hauptszene = new Scene(root);

		ResCanvas resCanvas = new ResCanvas();

		ArrayList<Arbeitspaket> copyArbeitspaket = new ArrayList<>();

		for (Arbeitspaket arbeitspaket : arbeitspakete) {
			copyArbeitspaket.add(arbeitspaket.copy());
		}

		copyArbeitspaket.forEach(ap -> resCanvas.hinzufuegen(ap));

		Canvas canvas = new Canvas(DisplayCanvas.canvasBreite, DisplayCanvas.canvasLaenge);
		canvas.setLayoutX(DisplayCanvas.canvasStartpunktX);
		canvas.setLayoutY(DisplayCanvas.canvasStartpunktY);

		Group group = new Group();

		pane = new Pane();
		pane.setPrefWidth(DisplayCanvas.paneBreite);
		pane.setPrefHeight(DisplayCanvas.paneLaenge);
		pane.setLayoutX(DisplayCanvas.paneLayoutX);
		pane.setLayoutY(DisplayCanvas.paneLayoutY);
		ResCanvas cvNeu = AlgoKapazitaetstreu.getInstance(AufgabeLadenImport.maxPersonenParallel)
				.algoDurchfuehren(resCanvas);

		ArrayList<ResEinheit[][]> historieListe = cvNeu.getHistorieKoordinatenSystem();

		Diagramm diagramm = new Diagramm();
		Rectangle[][] weisseFelder = diagramm.zeichneCanvas(canvas);
		controllerCanvas = new ControllerCanvasLoesungsmodus(arbeitspakete, historieListe, resCanvas);
		ResFeld[][] teilpakete = controllerCanvas.initializePositionObservableMap();

		HashMap<Arbeitspaket, Color> arbeitspaketeMitFarbe = new HashMap<Arbeitspaket, Color>();

		int farbenNummer = 0;
		for (ResFeld[] zeile : teilpakete) {
			for (ResFeld resFeld : zeile) {
				if (resFeld != null) {
					Arbeitspaket resFeldAp = resFeld.getResEinheit().getTeilpaket().getArbeitspaket();
					if (!arbeitspaketeMitFarbe.containsKey(resFeldAp)) {
						arbeitspaketeMitFarbe.put(resFeldAp, StandardColors.getInstance().getColor(farbenNummer));
						farbenNummer++;
					}
					resFeld.setFill(arbeitspaketeMitFarbe.get(resFeldAp));
				}
			}
		}
		controllerCanvas.erstelleLegende(arbeitspaketeMitFarbe);

		menu = new ContextMenu();
		ap = new MenuItem("Teile Arbeitspaket");
		reset = new MenuItem("Zurücksetzen");
		menu.setStyle("-fx-font:" + DisplayCanvas.schriftGroesse + " Arial;");
		menu.getItems().addAll(ap, reset);

		for (Rectangle[] rectangleZeile : weisseFelder) {
			for (Rectangle rectangle : rectangleZeile) {
				pane.getChildren().add(rectangle);
			}
		}

		for (ResFeld[] resFeldZeile : teilpakete) {
			for (ResFeld resFeld : resFeldZeile) {
				if (resFeld != null) {
					pane.getChildren().add(resFeld);
				}
			}
		}

		rahmenErstellen();

		group.getChildren().addAll(canvas, pane, controllerCanvas.getTable(),
				controllerCanvas.getTabelleArbeitspakete(), controllerCanvas.getLegende(),
				controllerCanvas.getButtonSchrittZurueck(), controllerCanvas.getButtonSchrittVor(),
				// controllerCanvas.getButtonMaxPersonenMinus(),
				// controllerCanvas.getButtonMaxPersonenPlus(),
				// controllerCanvas.getMaxPersonen(),
				// controllerCanvas.getTextFieldMaxPersonen(),
				controllerCanvas.getKonfigModus());

		for (int i = 0; i < DisplayCanvas.resFeldZeile; i++) {
			group.getChildren().add(controllerCanvas.getKapaGrenze(i));
		}

		Scene unterszene = new Scene(group);
		((Pane) hauptszene.getRoot()).getChildren().add(unterszene.getRoot());

		stage.setMaximized(true);
		stage.setScene(hauptszene);
		stage.setTitle("ResLearn");
		stage.show();
	}

	/**
	 * Fügt dem Koordinatensystem alle Rahmen hinzu, die vorher im ControllerCanvas
	 * erstellt wurden
	 */
	public void rahmenErstellen() {
		for (Rectangle rahmen : controllerCanvas.erstelleRahmen()) {
			pane.getChildren().add(rahmen);
		}
	}

	/**
	 * Entfernt alle Rahmen im Koordinatensystem, die vorher dem Koordinatensystem
	 * hinzugefügt wurden
	 */
	public void rahmenLoeschen() {
		for (Rectangle rahmen : controllerCanvas.getRahmenListe()) {
			pane.getChildren().remove(rahmen);
		}
	}

	public Stage getStage() {
		return stage;
	}

	public ContextMenu getMenu() {
		return menu;
	}

	public MenuItem getAp() {
		return ap;
	}

	public MenuItem getReset() {
		return reset;
	}

	public Pane getPane() {
		return pane;
	}
}