package reslearn.gui.controller;


import java.io.IOException;

import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.MenuItem;
import javafx.stage.Stage;

public class ControllerHauptmenue {
	@FXML
	private Button uebungAuswaehlen;
	@FXML
	private Button aufgabeErstellen;
	@FXML
	private Button aufgabeLaden;
	@FXML
	private Button tutorial;
	@FXML
	private Button einstellungen;

	
	public ControllerHauptmenue() {
		uebungAuswaehlen = new Button();
		aufgabeErstellen = new Button();
		aufgabeLaden = new Button();
		tutorial = new Button();
		einstellungen = new Button();
	}
	
	@FXML
    public void weiter(ActionEvent event) throws Exception{
		Scene newScene;


		if(event.getSource()==uebungAuswaehlen) {
			Parent root = FXMLLoader.load(getClass().getResource("../fxml/Uebungsmodus.fxml"));
			newScene = new Scene(root);
		}else if(event.getSource()==aufgabeErstellen) {
			Parent root = FXMLLoader.load(getClass().getResource("../fxml/AufgabeBearbeiten.fxml"));
			newScene = new Scene(root);
		}else if(event.getSource()==aufgabeLaden) {
			Parent root = FXMLLoader.load(getClass().getResource("../fxml/AufgabeLaden.fxml"));
			newScene = new Scene(root);
		}else if(event.getSource()==tutorial) {
			Parent root = FXMLLoader.load(getClass().getResource("../fxml/AufgabeBearbeiten.fxml"));
			newScene = new Scene(root);
		}else if(event.getSource()==einstellungen) {
			Parent root = FXMLLoader.load(getClass().getResource("../fxml/Einstellungen.fxml"));
			newScene = new Scene(root);
		}else {
			Parent root = FXMLLoader.load(getClass().getResource("../fxml/Einstellungen.fxml"));
			newScene = new Scene(root);
		}
		
		
		Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
		window.setScene(newScene);
		window.show();
	}
	
	public void zurueck() {
		//zum vorherigen Fenster zurück
	}


}

