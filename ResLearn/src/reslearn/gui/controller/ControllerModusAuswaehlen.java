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
	private Group ersterSchritt;
	@FXML
	private Group uebungsmodus;
	@FXML
	private Group loesungsmodus;


	@FXML
    public void weiter(ActionEvent event) throws Exception{
		Scene newScene = null;


		if(event.getSource()==ersterSchritt) {
			newScene = new Scene(FXMLLoader.load(getClass().getResource("ErsterSchritt")));
		}else if(event.getSource()==uebungsmodus) {
			newScene = new Scene(FXMLLoader.load(getClass().getResource("Uebungsmodus.fxml")));
		}else if(event.getSource()==loesungsmodus) {
			newScene = new Scene(FXMLLoader.load(getClass().getResource("Loesungsmodus.fxml")));
		} 
		
		Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
		window.setScene(newScene);
		window.show();
	}
	


}

