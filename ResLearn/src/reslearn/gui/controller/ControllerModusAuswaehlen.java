package reslearn.gui.controller;

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
import reslearn.model.paket.Arbeitspaket;

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

	private Arbeitspaket[] ap = new Arbeitspaket[5];

	public void initialize(Arbeitspaket[] ap) {
		this.ap = ap;

	}

	@SuppressWarnings("static-access")
	@FXML
	public void weiter(ActionEvent event) throws Exception {
		alleFenster.add("../fxml/ModusAuswaehlen.fxml");
		if (event.getSource() == ersterSchritt) {
			View.getInstance().initializeCanvasView(ap);
			((Node) (event.getSource())).getScene().getWindow().hide();
		} else if (event.getSource() == uebungsmodus) {
			// View.getInstance().initializeCanvasView(ali.aufgabeLaden(cua.f + "\\" +
			// cua.datei));
			// View.getInstance().start(new Stage());
			// ((Node) (event.getSource())).getScene().getWindow().hide();
		} else if (event.getSource() == loesungsmodus) {
			// View.getInstance().initializeCanvasView(ali.aufgabeLaden(cua.f + "\\" +
			// cua.datei));
			// View.getInstance().start(new Stage());
			// ((Node) (event.getSource())).getScene().getWindow().hide();
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
