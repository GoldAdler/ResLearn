package reslearn.gui.controller;


import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

public class ControllerModusAuswaehlen {
	@FXML
	private ImageView ersterSchritt;
	@FXML
	private ImageView uebungsmodus;
	@FXML
	private ImageView loesungsmodus;


	@FXML
	public void initialize(){
		ersterSchritt.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
		    @Override
		    public void handle(MouseEvent event) {
		    	Scene newScene;
		    	System.out.println("Erfolg");
				try {		
					newScene = new Scene(FXMLLoader.load(getClass().getResource("./../fxml/Uebungsmodus")));
					System.out.println("Hurra");
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

		    	
		    }
		});
	}
	
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

