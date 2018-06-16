package reslearn.gui.controller;

import java.io.File;
import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import reslearn.gui.ImportExport.AufgabeLadenImport;
import reslearn.model.paket.Arbeitspaket;

public class ControllerAufgabeLaden extends Controller {

	@FXML
	private Button zurueck;
	@FXML
	private Button home;
	@FXML
	private Label labellDateiname;
	@FXML
	private Button laden;
	@FXML
	private Button dateiauswaehlen;

	private Arbeitspaket[] paketeArray;
	private String name;
	private String dateipfad = "./eigeneAufgaben/";

	/**
	 * Diese Methode oeffnet einen FileChooser, mit dem eine selbsterstellte Aufgabe
	 * ausgewaehlt und geladen werden kann. Der Pfad ist an dieser Stelle schon
	 * eingetragen.
	 */
	@FXML
	public void dateiauswaehlen() {
		FileChooser fileChooser = new FileChooser();
		File defaultDirectory = new File(dateipfad);
		fileChooser.setInitialDirectory(defaultDirectory);
		FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("CSV files (*.csv)", "*.csv");
		fileChooser.getExtensionFilters().add(extFilter);
		File file = fileChooser.showOpenDialog(dateiauswaehlen.getScene().getWindow());
		name = file.getName();
		labellDateiname.setText("Wollen Sie die " + name.substring(0, name.length() - 4) + " Aufgabe laden?");
		labellDateiname.setVisible(true);
		laden.setDisable(false);
	}

	@FXML
	public void laden(ActionEvent event) {
		AufgabeLadenImport importAufgabe = new AufgabeLadenImport();

		paketeArray = importAufgabe.aufgabeLaden(dateipfad + name);

		weiter(event);

	}

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

	public void weiter(ActionEvent event) {
		alleFenster.add("/reslearn/gui/fxml/AufgabeLaden.fxml");
		Scene newScene;
		Parent root;
		try {
			FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/reslearn/gui/fxml/ModusAuswaehlen.fxml"));
			root = fxmlLoader.load();
			newScene = new Scene(root);
			ControllerModusAuswaehlen controller = fxmlLoader.<ControllerModusAuswaehlen>getController();
			controller.initialize(paketeArray);
			ControllerUebungsmodus.letztesArbeitspaket = paketeArray;
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

	public Arbeitspaket[] getPaketeArray() {
		return paketeArray;
	}

}