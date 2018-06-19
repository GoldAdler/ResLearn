package reslearn.gui.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import reslearn.gui.view.ViewTutorialVideo;
import reslearn.gui.view.ViewUebungAuswaehlen;

public class ControllerHauptmenue extends Controller {

	@FXML
	private Button uebungAuswaehlen;
	@FXML
	private Button aufgabeErstellen;
	@FXML
	private Button aufgabeLaden;
	@FXML
	private Button tutorial;
	@FXML
	private Button quiz;

	@FXML
	public void weiter(ActionEvent event) throws Exception {
		Scene newScene;
		alleFenster.add("/reslearn/gui/fxml/Hauptmenue.fxml");
		if (event.getSource() == uebungAuswaehlen) {
			ViewUebungAuswaehlen.getInstance().start(new Stage());
			((Node) (event.getSource())).getScene().getWindow().hide();
		} else if (event.getSource() == aufgabeErstellen) {
			Parent root = FXMLLoader.load(getClass().getResource("/reslearn/gui/fxml/AufgabeErstellen.fxml"));
			newScene = new Scene(root);
			Stage stage = new Stage();
			stage.setTitle("ResLearn");
			stage.setMaximized(true);
			stage.setScene(newScene);
			stage.show();
			((Node) (event.getSource())).getScene().getWindow().hide();
		} else if (event.getSource() == aufgabeLaden) {
			Parent root = FXMLLoader.load(getClass().getResource("/reslearn/gui/fxml/AufgabeLaden.fxml"));
			newScene = new Scene(root);
			Stage stage = new Stage();
			stage.setTitle("ResLearn");
			stage.setMaximized(true);
			stage.setScene(newScene);
			stage.show();
			((Node) (event.getSource())).getScene().getWindow().hide();
		} else if (event.getSource() == tutorial) {
			ViewTutorialVideo.getInstance().start(new Stage());
			((Node) (event.getSource())).getScene().getWindow().hide();
		} else if (event.getSource() == quiz) {
			ControllerTutorialFragen.getInstance().start(new Stage());
			((Node) (event.getSource())).getScene().getWindow().hide();
			// Parent root =
			// FXMLLoader.load(getClass().getResource("/reslearn/gui/fxml/Quiz.fxml"));
			// newScene = new Scene(root);
			// Stage stage = new Stage();
			// stage.setTitle("ResLearn");
			// stage.setMaximized(true);
			// stage.setScene(newScene);
			// stage.show();
			// ((Node) (event.getSource())).getScene().getWindow().hide();
		} else {
			Parent root = FXMLLoader.load(getClass().getResource("/reslearn/gui/fxml/Hauptmenue.fxml"));
			newScene = new Scene(root);
			Stage stage = new Stage();
			stage.setTitle("ResLearn");
			stage.setMaximized(true);
			stage.setScene(newScene);
			stage.show();
			((Node) (event.getSource())).getScene().getWindow().hide();
		}
	}
}