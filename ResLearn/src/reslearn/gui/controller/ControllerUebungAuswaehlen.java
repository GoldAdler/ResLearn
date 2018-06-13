package reslearn.gui.controller;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import reslearn.gui.DisplayCanvas;
import reslearn.gui.ImportExport.AufgabeLadenImport;
import reslearn.model.paket.Arbeitspaket;

public class ControllerUebungAuswaehlen extends Controller {
	public Arbeitspaket[] paketeArray;
	private Pane pane;
	File f = new File("./uebungen");
	public File[] fileArray = f.listFiles(new FilenameFilter() {
		@Override
		public boolean accept(File f, String name) {
			return name.toLowerCase().endsWith(".csv");
		}
	});

	public Button dateiname;
	public String datei;

	/**
	 * In dieser Methode werden je nachdem, wie viele Aufgaben in dem vorgegebenen
	 * Ordner vorhanden sind, dementsprechend viele Buttons dynamisch generiert und
	 * auf der Pane positioniert.
	 *
	 * @return
	 */
	public Pane erstellePane() {

		pane = new Pane();

		int buttonXPosition = 0;
		int buttonYPosition = 0;

		for (int i = 0; i < fileArray.length; i++) {
			Button b = new Button(fileArray[i].getName().substring(0, fileArray[i].getName().length() - 4));
			b.setId(fileArray[i].getName());
			b.setOnAction(ButtonAction);
			if (i % 5 == 0 && i != 0) {
				buttonYPosition += DisplayCanvas.abstandButtonY;
				buttonXPosition = 0;
			}
			b.setStyle("-fx-font:" + DisplayCanvas.schriftGroesse + " Arial;");
			b.setPrefHeight(DisplayCanvas.buttonHoehe);
			b.setPrefWidth(DisplayCanvas.buttonBreite);
			b.setLayoutX(DisplayCanvas.buttonXStart + buttonXPosition);
			b.setLayoutY(DisplayCanvas.buttonYStart + buttonYPosition);

			pane.getChildren().add(b);
			buttonXPosition += DisplayCanvas.abstandButtonX + DisplayCanvas.buttonBreite;

		}
		return pane;
	}

	// Event Handler Maus klicken
	private EventHandler<ActionEvent> ButtonAction = new EventHandler<ActionEvent>() {

		@Override
		public void handle(ActionEvent event) {
			weiter(event);

		}
	};

	public void weiter(ActionEvent event) {
		Scene newScene;
		Parent root;
		AufgabeLadenImport ali = new AufgabeLadenImport();
		try {
			FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/reslearn/gui/fxml/ModusAuswaehlen.fxml"));
			root = fxmlLoader.load();
			newScene = new Scene(root);
			ControllerModusAuswaehlen controller = fxmlLoader.<ControllerModusAuswaehlen>getController();
			dateiname = (Button) event.getSource();
			datei = dateiname.getId();
			ControllerUebungsmodus.letztesArbeitspaket = ali.aufgabeLaden(f + File.separator + datei);
			controller.initialize(ali.aufgabeLaden(f + File.separator + datei));
			Stage stage = new Stage();
			stage.setTitle("ResLearn");
			stage.setMaximized(true);
			stage.setScene(newScene);
			stage.show();
			((Node) (event.getSource())).getScene().getWindow().hide();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@FXML
	private Button zurueck;
	@FXML
	private Button home;

	@FXML
	public void home(ActionEvent event) throws Exception {
		Scene newScene;
		Parent root;
		try {
			root = FXMLLoader.load(getClass().getResource(hauptmenue()));
			newScene = new Scene(root);
			Stage stage = new Stage();
			stage.setTitle("ResLearn");
			stage.setMaximized(true);
			stage.setScene(newScene);
			stage.show();
			((Node) (event.getSource())).getScene().getWindow().hide();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@FXML
	public void zurueck(ActionEvent event) throws Exception {
		Scene newScene;
		Parent root;
		try {
			root = FXMLLoader.load(getClass().getResource(vorherigesFenster(alleFenster)));
			newScene = new Scene(root);
			Stage stage = new Stage();
			stage.setTitle("ResLearn");
			stage.setMaximized(true);
			stage.setScene(newScene);
			stage.show();
			((Node) (event.getSource())).getScene().getWindow().hide();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}