package reslearn.gui.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class ControllerModusAuswaehlen {
	
	@FXML
	private Node ersterSchritt;
	@FXML
	private Node uebungsmodus;
	@FXML
	private Group loesungsmodus;

	@FXML
    public void weiter(ActionEvent event) throws Exception{
		Scene newScene;

		if(event.getSource()==ersterSchritt) {
			newScene = new Scene(FXMLLoader.load(getClass().getResource("../fxml/Uebungsmodus.fxml")));
			Stage stage = new Stage();
			stage.setTitle("ResLearn");
			stage.setMaximized(true);
			stage.setScene(newScene);
			stage.show();
			((Node) (event.getSource())).getScene().getWindow().hide();
		}else if(event.getSource()==uebungsmodus) {
			newScene = new Scene(FXMLLoader.load(getClass().getResource("../fxml/Uebungsmodus.fxml")));
			Stage stage = new Stage();
			stage.setTitle("ResLearn");
			stage.setMaximized(true);
			stage.setScene(newScene);
			stage.show();
			((Node) (event.getSource())).getScene().getWindow().hide();
		}else if(event.getSource()==loesungsmodus) {
			newScene = new Scene(FXMLLoader.load(getClass().getResource("../fxml/Uebungsmodus.fxml")));
			Stage stage = new Stage();
			stage.setTitle("ResLearn");
			stage.setMaximized(true);
			stage.setScene(newScene);
			stage.show();
			((Node) (event.getSource())).getScene().getWindow().hide();
		} 
	}
}

