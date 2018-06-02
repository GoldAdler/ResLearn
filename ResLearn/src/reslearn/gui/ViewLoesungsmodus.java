package reslearn.gui;

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
import reslearn.gui.controller.ControllerCanvasLoesungsmodus;
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
		// Lade FXML
		Parent root = FXMLLoader.load(getClass().getResource("./fxml/Uebungsmodus.fxml"));
		Scene hauptszene = new Scene(root);

		ResCanvas resCanvas = new ResCanvas();

		for (Arbeitspaket arbeitspaket : arbeitspakete) {
			System.out.println("Arbeitspaket-ID:" + arbeitspaket.getId());
			resCanvas.hinzufuegen(arbeitspaket);
		}

		Canvas canvas = new Canvas(DisplayCanvas.canvasBreite, DisplayCanvas.canvasLaenge);
		canvas.setLayoutX(DisplayCanvas.canvasStartpunktX);
		canvas.setLayoutY(DisplayCanvas.canvasStartpunktY);

		Group group = new Group();

		// Erstelle neue Zeichenfläche für Klötzchen und füge Canvas & Pane
		// der Unterszene hinzu
		pane = new Pane();
		pane.setPrefWidth(DisplayCanvas.paneBreite);
		pane.setPrefHeight(DisplayCanvas.paneLaenge);
		pane.setLayoutX(DisplayCanvas.paneLayoutX);
		pane.setLayoutY(DisplayCanvas.paneLayoutY);

		ArrayList<ResEinheit[][]> historieListe = AlgoKapazitaetstreu.getInstance().algoDurchfuehren(resCanvas)
				.getHistorieKoordinatenSystem();
		Diagramm diagramm = new Diagramm();
		Rectangle[][] weisseFelder = diagramm.zeichneCanvas(canvas);
		ControllerCanvasLoesungsmodus controllerCanvas = new ControllerCanvasLoesungsmodus(historieListe, resCanvas,
				diagramm);
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

		group.getChildren().addAll(canvas, pane, controllerCanvas.getTable(),
				controllerCanvas.getTabelleArbeitspakete(), controllerCanvas.getLegende());

		Scene unterszene = new Scene(group);
		((Pane) hauptszene.getRoot()).getChildren().add(unterszene.getRoot());

		stage.setMaximized(true);
		stage.setScene(hauptszene);
		stage.setTitle("ResLearn");
		stage.show();
	}

	public static void main(String[] args) {
		launch(args);
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