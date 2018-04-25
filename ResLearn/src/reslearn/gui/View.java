package reslearn.gui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import reslearn.model.algorithmus.AlgoErsteSchritt;
import reslearn.model.paket.Arbeitspaket;
import reslearn.model.paket.ResEinheit;
import reslearn.model.resCanvas.ResCanvas;
import javafx.scene.canvas.*;
import javafx.scene.layout.Pane;

public class View extends Application {

	public static void main(String[] args) {
		launch(args);
	}

	public void start(Stage stage) throws Exception {
		// FXMLLoader fxmlLoader = new
		// FXMLLoader(getClass().getResource("./fxml/Uebungsmodus.fxml"));
		// Parent root = fxmlLoader.load();
		//
		Parent root = FXMLLoader.load(getClass().getResource("./fxml/Uebungsmodus.fxml"));
		Scene scene = new Scene(new Group());
		scene.getStylesheets().add("Stylesheet.css");

		Canvas canvas = new Canvas(900, 600);
		canvas.setLayoutX(230);
		canvas.setLayoutY(80);
		GraphicsContext gc = canvas.getGraphicsContext2D();
		Group diagramm = new Group();
		diagramm.getChildren().add(canvas);

		Scene sceneFenster = new Scene(root);
		Scene sceneDiagramm = new Scene(diagramm);
		((Pane) sceneFenster.getRoot()).getChildren().add(sceneDiagramm.getRoot());

		stage.setMaximized(true);
		stage.setScene(sceneFenster);
		stage.setTitle("ResLearn");
		stage.show();

		

		ResCanvas resCanvas = new ResCanvas();

		erstelleTestDaten(resCanvas);

		// durchführen des Algorithmus
		ResEinheit[][] koordinantenSystem = AlgoErsteSchritt.berechne(resCanvas);
		
		Diagramm meincanvas = new Diagramm(koordinantenSystem);
		meincanvas.zeichneCanvas(gc, canvas);
		meincanvas.zeichneResEinheit(gc, canvas);
		
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

}
