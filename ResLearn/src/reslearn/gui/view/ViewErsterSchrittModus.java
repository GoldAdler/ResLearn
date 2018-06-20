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
import reslearn.gui.controller.ControllerCanvasErsterSchrittModus;
import reslearn.gui.rescanvas.Diagramm;
import reslearn.gui.rescanvas.DisplayCanvas;
import reslearn.gui.rescanvas.ResFeld;
import reslearn.gui.utils.StandardColors;
import reslearn.model.algorithmus.AlgoErsteSchritt;
import reslearn.model.paket.Arbeitspaket;
import reslearn.model.paket.ResEinheit;
import reslearn.model.resCanvas.ResCanvas;

public class ViewErsterSchrittModus extends Application {
	private static ViewErsterSchrittModus view;
	private Stage stage;
	private ContextMenu menu;
	private MenuItem ap;
	private MenuItem reset;
	private Pane pane;
	private ControllerCanvasErsterSchrittModus controllerCanvas;

	public static ViewErsterSchrittModus getInstance() {
		if (view == null) {
			view = new ViewErsterSchrittModus();
		}
		return view;
	}

	@Override
	public void start(Stage stage) throws Exception {

	}

	public void initializeCanvasView(Arbeitspaket[] arbeitspakete) throws IOException {

		stage = new Stage();

		Parent root = FXMLLoader.load(getClass().getResource("/reslearn/gui/fxml/ErsteSchritteModus.fxml"));
		Scene hauptszene = new Scene(root);

		ResCanvas resCanvas = new ResCanvas();

		for (Arbeitspaket arbeitspaket : arbeitspakete) {
			System.out.println("Arbeitspaket-ID:" + arbeitspaket.getIdIntern());
			resCanvas.hinzufuegen(arbeitspaket);
		}

		Canvas canvas = new Canvas(DisplayCanvas.canvasBreite, DisplayCanvas.canvasLaenge);
		canvas.setLayoutX(DisplayCanvas.canvasStartpunktX);
		canvas.setLayoutY(DisplayCanvas.canvasStartpunktY);

		Group group = new Group();

		pane = new Pane();
		pane.setPrefWidth(DisplayCanvas.paneBreite);
		pane.setPrefHeight(DisplayCanvas.paneLaenge);
		pane.setLayoutX(DisplayCanvas.paneLayoutX);
		pane.setLayoutY(DisplayCanvas.paneLayoutY);

		ResEinheit[][] koordinatenSystem = AlgoErsteSchritt.getInstance().algoDurchfuehren(resCanvas).getKoordinatenSystem();
		Diagramm diagramm = new Diagramm();
		Rectangle[][] weisseFelder = diagramm.zeichneCanvas(canvas);
		ResFeld[][] teilpakete = diagramm.zeichneTeilpaketeOben(koordinatenSystem, resCanvas);
		controllerCanvas = new ControllerCanvasErsterSchrittModus(resCanvas, diagramm);

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
					controllerCanvas.makeDraggable(resFeld);
				}
			}
		}

		rahmenErstellen();

		group.getChildren().addAll(canvas, pane, controllerCanvas.getTable(),controllerCanvas.getTabelleArbeitspakete(),
				controllerCanvas.getLegende(), controllerCanvas.getValidierenButton(), controllerCanvas.getKorrekturvorschlaege(),
				controllerCanvas.getFehlermeldung());

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
		for(Rectangle rahmen : controllerCanvas.erstelleRahmen()) {
			pane.getChildren().add(rahmen);
		}
	}

	/**
	 * Entfernt alle Rahmen im Koordinatensystem, die vorher dem Koordinatensystem
	 * hinzugefügt wurden
	 */
	public void rahmenLoeschen(ArrayList<Rectangle> rahmenListe) {
		for(Rectangle rahmen : rahmenListe) {
			pane.getChildren().remove(rahmen);
		}
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
