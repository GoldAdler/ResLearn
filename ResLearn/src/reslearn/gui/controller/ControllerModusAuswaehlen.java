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
import javafx.stage.Stage;
import reslearn.gui.View;
import reslearn.gui.ImportExport.AufgabeLadenImport;

public class ControllerModusAuswaehlen extends Controller {

	@FXML
	private Button ersterSchritt;
	@FXML
	private Button uebungsmodus;
	@FXML
	private Button loesungsmodus;
	@FXML
	private Button zurueck;
	@FXML
	private Button home;

	@SuppressWarnings("static-access")
	@FXML
	public void weiter(ActionEvent event) throws Exception {
		ControllerUebungAuswaehlen cua = new ControllerUebungAuswaehlen();
		AufgabeLadenImport ali = new AufgabeLadenImport();
		ControllerAufgabeErstellen cae = new ControllerAufgabeErstellen();
		if (event.getSource() == ersterSchritt) {
			System.out.println(alleFenster.get(alleFenster.size() - 1));
			if (alleFenster.get(alleFenster.size() - 1) == "../fxml/AufgabeErstellen.fxml") {
				View.getInstance().initializeCanvasView(cae.pakete);
				View.getInstance().start(new Stage());
				alleFenster.add("../fxml/ModusAuswaehlen.fxml");
				((Node) (event.getSource())).getScene().getWindow().hide();
			} else {
				View.getInstance().initializeCanvasView(ali.aufgabeLaden(cua.f + File.separator + cua.datei));
				View.getInstance().start(new Stage());
				alleFenster.add("../fxml/ModusAuswaehlen.fxml");
				((Node) (event.getSource())).getScene().getWindow().hide();

			}
		} else if (event.getSource() == uebungsmodus) {
			View.getInstance().initializeCanvasView(ali.aufgabeLaden(cua.f + "\\" + cua.datei));
			View.getInstance().start(new Stage());
			((Node) (event.getSource())).getScene().getWindow().hide();
		} else if (event.getSource() == loesungsmodus) {
			View.getInstance().initializeCanvasView(ali.aufgabeLaden(cua.f + "\\" + cua.datei));
			View.getInstance().start(new Stage());
			((Node) (event.getSource())).getScene().getWindow().hide();
		}
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
}
