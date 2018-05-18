package reslearn.gui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import reslearn.gui.controller.Controller;
import reslearn.gui.controller.ControllerCanvas;
import reslearn.model.algorithmus.AlgoErsteSchritt;
import reslearn.model.paket.Arbeitspaket;
import reslearn.model.paket.ResEinheit;
import reslearn.model.resCanvas.ResCanvas;

public class View extends Application {

	public static Stage classStage = new Stage();
	public static Pane pane;
	public static ControllerCanvas cc;
	public static ContextMenu menu = new ContextMenu();
	public static MenuItem ap = new MenuItem("Teile Arbeitspaket");
	public static MenuItem reset = new MenuItem("Zur�cksetzen");

	@Override
	public void start(Stage stage) throws Exception {
		// Lade FXML
		Parent root = FXMLLoader.load(getClass().getResource("./fxml/Uebungsmodus.fxml"));

		// Erstelle Canvas-Zeichenfl�che & Gruppe
		Canvas canvas = new Canvas(DisplayCanvas.canvasBreite, DisplayCanvas.canvasLaenge);
		canvas.setLayoutX(DisplayCanvas.canvasStartpunktX);
		canvas.setLayoutY(DisplayCanvas.canvasStartpunktY);
		GraphicsContext gc = canvas.getGraphicsContext2D();
		Group group = new Group();

		// Erstelle 2 Szenen: Hauptszene = FXML, Unterszene = Java-Canvas
		Scene hauptszene = new Scene(root);
		Scene unterszene = new Scene(group);

		// Erstelle neue Zeichenfl�che f�r Kl�tzchen und f�ge Canvas & Pane
		// der Unterszene hinzu
		pane = new Pane();
		pane.setPrefWidth(DisplayCanvas.paneBreite);
		pane.setPrefHeight(DisplayCanvas.paneLaenge);
		pane.setLayoutX(DisplayCanvas.canvasStartpunktX + DisplayCanvas.resFeldBreite + DisplayCanvas.spaltX);
		pane.setLayoutY(DisplayCanvas.canvasStartpunktY + DisplayCanvas.resFeldLaenge);

		group.getChildren().addAll(canvas, pane, ControllerCanvas.table);

		// Durchf�hren des Algorithmus
		ResCanvas resCanvas = new ResCanvas();
		erstelleTestDaten(resCanvas);
		ResEinheit[][] koordinatenSystem = AlgoErsteSchritt.getInstance().algoDurchfuehren(resCanvas)
				.getKoordinatenSystem();

		cc = new ControllerCanvas(resCanvas);

		((Pane) hauptszene.getRoot()).getChildren().add(unterszene.getRoot());

		Diagramm meincanvas = new Diagramm();
		meincanvas.zeichneCanvas(gc, canvas);
		meincanvas.zeichnePaket(koordinatenSystem);

		menu.getItems().addAll(ap, reset);

		cc.erstelleTabelle();

		classStage = stage;
		stage.setMaximized(true);
		stage.setScene(hauptszene);
		stage.setTitle("ResLearn");
		stage.show();

	}

	private static void erstelleTestDaten(ResCanvas resCanvas) {
		switch (Controller.AufgabenNummer) {
		case 1:
			// Erster Test
			Arbeitspaket apA1 = new Arbeitspaket("A", 1, 2, 3, 4, 2, 3, 6);
			Arbeitspaket apB1 = new Arbeitspaket("B", 1, 2, 4, 4, 2, 4, 8);
			Arbeitspaket apC1 = new Arbeitspaket("C", 3, 5, 5, 7, 2, 3, 6);
			resCanvas.hinzufuegen(apB1);
			resCanvas.hinzufuegen(apC1);
			resCanvas.hinzufuegen(apA1);
			break;
		case 2:
			// Pr�fungsaufgabe
			Arbeitspaket apA2 = new Arbeitspaket("A", 1, 6, 1, 6, 6, 4, 24);
			Arbeitspaket apB2 = new Arbeitspaket("B", 7, 10, 7, 10, 4, 2, 8);
			Arbeitspaket apC2 = new Arbeitspaket("C", 8, 15, 8, 15, 8, 3, 24);
			Arbeitspaket apD2 = new Arbeitspaket("D", 13, 15, 15, 17, 3, 4, 12);
			resCanvas.hinzufuegen(apB2);
			resCanvas.hinzufuegen(apD2);
			resCanvas.hinzufuegen(apA2);
			resCanvas.hinzufuegen(apC2);
			break;
		case 3:
			// PTMA_U09_L Test
			Arbeitspaket apA3 = new Arbeitspaket("A", 1, 6, 3, 8, 6, 4, 24);
			Arbeitspaket apB3 = new Arbeitspaket("B", 7, 13, 10, 16, 7, 3, 21);
			Arbeitspaket apC3 = new Arbeitspaket("C", 9, 16, 14, 21, 8, 2, 16);
			Arbeitspaket apD3 = new Arbeitspaket("D", 14, 18, 17, 21, 5, 5, 25);
			Arbeitspaket apE3 = new Arbeitspaket("E", 17, 22, 21, 26, 6, 3, 18);
			resCanvas.hinzufuegen(apB3);
			resCanvas.hinzufuegen(apD3);
			resCanvas.hinzufuegen(apA3);
			resCanvas.hinzufuegen(apE3);
			resCanvas.hinzufuegen(apC3);
			break;
		case 4:
			// Pr�fungsaufgabe mit E
			Arbeitspaket apA4 = new Arbeitspaket("A", 1, 6, 1, 6, 6, 4, 24);
			Arbeitspaket apB4 = new Arbeitspaket("B", 7, 10, 7, 10, 4, 2, 8);
			Arbeitspaket apC4 = new Arbeitspaket("C", 8, 15, 8, 15, 8, 3, 24);
			Arbeitspaket apD4 = new Arbeitspaket("D", 13, 15, 15, 17, 3, 4, 12);
			Arbeitspaket apE4 = new Arbeitspaket("E", 16, 19, 17, 20, 4, 4, 16);
			resCanvas.hinzufuegen(apB4);
			resCanvas.hinzufuegen(apD4);
			resCanvas.hinzufuegen(apA4);
			resCanvas.hinzufuegen(apC4);
			resCanvas.hinzufuegen(apE4);
			break;
		case 5:
			Arbeitspaket apA5 = new Arbeitspaket("A", 1, 6, 3, 8, 6, 4, 24);
			Arbeitspaket apB5 = new Arbeitspaket("B", 7, 13, 10, 16, 7, 3, 21);
			Arbeitspaket apC5 = new Arbeitspaket("C", 9, 16, 14, 21, 8, 2, 16);
			Arbeitspaket apD5 = new Arbeitspaket("D", 14, 18, 17, 21, 5, 5, 25);
			Arbeitspaket apE5 = new Arbeitspaket("E", 17, 22, 21, 26, 6, 3, 18);
			Arbeitspaket apF5 = new Arbeitspaket("F", 10, 12, 10, 12, 3, 2, 6);
			resCanvas.hinzufuegen(apB5);
			resCanvas.hinzufuegen(apD5);
			resCanvas.hinzufuegen(apA5);
			resCanvas.hinzufuegen(apE5);
			resCanvas.hinzufuegen(apC5);
			resCanvas.hinzufuegen(apF5);
			break;
		case 6:
			// Aufgabe aus der Kinderuni-Vorlesung
			Arbeitspaket apA6 = new Arbeitspaket("A", 1, 2, 1, 2, 2, 1, 2);
			Arbeitspaket apB6 = new Arbeitspaket("B", 3, 3, 3, 3, 1, 3, 3);
			Arbeitspaket apC6 = new Arbeitspaket("C", 4, 5, 4, 5, 2, 2, 4);
			Arbeitspaket apD6 = new Arbeitspaket("D", 4, 4, 4, 4, 1, 2, 2);

			resCanvas.hinzufuegen(apB6);
			resCanvas.hinzufuegen(apC6);
			resCanvas.hinzufuegen(apA6);
			resCanvas.hinzufuegen(apD6);
			break;
		}
	}

	public static void main(String[] args) {
		launch(args);
	}

}
