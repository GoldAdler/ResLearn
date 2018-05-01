package reslearn.gui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import reslearn.gui.controller.ControllerCanvas;
import reslearn.model.algorithmus.AlgoErsteSchritt;
import reslearn.model.paket.Arbeitspaket;
import reslearn.model.paket.ResEinheit;
import reslearn.model.resCanvas.ResCanvas;
import javafx.scene.canvas.*;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.Pane;

public class View extends Application {

	public static Pane pane;
	public static ControllerCanvas cc = new ControllerCanvas();
	public static ContextMenu menu = new ContextMenu();
	public static MenuItem ap = new MenuItem("Teile Arbeitspaket");
	public static MenuItem farbe = new MenuItem("Ändere Farbe");

	public void start(Stage stage) throws Exception {
		//Lade FXML & CSS Datei
		Parent root = FXMLLoader.load(getClass().getResource("./fxml/Hauptmenue.fxml"));
		Scene scene = new Scene(new Group());
		scene.getStylesheets().add("Stylesheet.css");
		
		//Erstelle Canvas-Zeichenfläche & Gruppe
		Canvas canvas = new Canvas(900, 600);
		canvas.setLayoutX(230);
		canvas.setLayoutY(80);
		GraphicsContext gc = canvas.getGraphicsContext2D();
		Group group = new Group();

		//Erstelle 2 Szenen: Hauptszene = FXML, Unterszene = Java-Canvas
		Scene hauptszene = new Scene(root);
		Scene unterszene = new Scene(group);
		
		//Erstelle neue Zeichenfläche für Klötzchen und füge Canvas & Pane
		//der Unterszene hinzu
		pane = new Pane();
		pane.setPrefWidth(860);
		pane.setPrefHeight(560);
		pane.setLayoutX(255);
		pane.setLayoutY(95);
		
		group.getChildren().addAll(canvas, pane);

		//Durchführen des Algorithmus
		ResCanvas resCanvas = new ResCanvas();
		erstelleTestDaten(resCanvas);	
		ResEinheit[][] koordinatenSystem = AlgoErsteSchritt.getInstance().algoDurchfuehren(resCanvas);
		
		//((Pane) hauptszene.getRoot()).getChildren().add(unterszene.getRoot());
		
		Diagramm meincanvas = new Diagramm();
		//meincanvas.zeichneCanvas(gc, canvas);
		//meincanvas.zeichnePaket(koordinatenSystem);
	
		stage.setMaximized(true);
		stage.setScene(hauptszene);
		stage.setTitle("ResLearn");
		stage.show();
	}
	
	private static void erstelleTestDaten(ResCanvas resCanvas) {
		// ---------------------------------------------------------------------
		// -----------------------------TestDaten-------------------------------
		// -------------------------------Anfang--------------------------------

		/*
		 * Erster Test
		 */
		// Arbeitspaket apA = new Arbeitspaket("A", 1, 2, 3, 4, 2, 3, 6);
		// Arbeitspaket apB = new Arbeitspaket("B", 1, 2, 4, 4, 2, 4, 8);
		// Arbeitspaket apC = new Arbeitspaket("C", 3, 5, 5, 7, 2, 3, 6);
		//
		// resCanvas.hinzufuegen(apB);
		// resCanvas.hinzufuegen(apC);
		// resCanvas.hinzufuegen(apA);

		/*
		 * PTMA_U09_L Test
		 */
		Arbeitspaket apA = new Arbeitspaket("A", 1, 6, 3, 3, 6, 4, 24);
		Arbeitspaket apB = new Arbeitspaket("B", 7, 13, 10, 10, 7, 3, 21);
		Arbeitspaket apC = new Arbeitspaket("C", 9, 16, 14, 14, 8, 2, 16);
		Arbeitspaket apD = new Arbeitspaket("D", 14, 18, 17, 17, 5, 5, 25);
		Arbeitspaket apE = new Arbeitspaket("E", 17, 22, 21, 21, 6, 3, 18);

		resCanvas.hinzufuegen(apB);
		resCanvas.hinzufuegen(apD);
		resCanvas.hinzufuegen(apA);
		resCanvas.hinzufuegen(apE);
		resCanvas.hinzufuegen(apC);

		// -----------------------------TestDaten-------------------------------
		// -------------------------------ENDE--------------------------------
		// ---------------------------------------------------------------------
	}

	public static void main(String[] args) {
		launch(args);
	}

}
