package reslearn.gui.controller;

import java.io.IOException;
import java.util.Optional;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;

public class ControllerUebungsmodus extends Controller {

	@FXML
	private Button zurueck;
	@FXML
	private Button home;

	@FXML
	public void home(ActionEvent event) throws Exception {
		Scene newScene;
		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setHeaderText("Warnung");
		alert.setContentText("Wirklich zum Hauptmenü zurückkehren? Alle Änderungen gehen verloren");
		Optional<ButtonType> result = alert.showAndWait();
		if (result.get() == ButtonType.OK) {
			Parent root;
			try {
				root = FXMLLoader.load(getClass().getResource(hauptmenue()));
				newScene = new Scene(root);
				Stage stage = new Stage();
				stage.setTitle("ResLearn");
				stage.setMaximized(true);
				stage.setScene(newScene);
				stage.show();
			} catch (IOException e) {
				e.printStackTrace();
			}
			((Node) (event.getSource())).getScene().getWindow().hide();
		} else {
			alert.close();
		}
	}

	@FXML
	public void zurueck(ActionEvent event) throws Exception {
		Scene newScene;
		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setHeaderText("Warnung");
		alert.setContentText("Wirklich zum vorherigen Fenster zurückkehren");
		Optional<ButtonType> result = alert.showAndWait();
		if (result.get() == ButtonType.OK) {
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
		} else {
			alert.close();
		}
	}

}
