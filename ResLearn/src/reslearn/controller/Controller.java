package reslearn.controller;

import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.stage.Stage;

public class Controller {
	@FXML
	Button uebungauswaehlen;
	@FXML
	Button aufgabeerstellen;
	@FXML
	Button aufgabeladen;
	@FXML
	Button tutorial;
	@FXML
	Button einstellungen;

	//Hallo Tätärä
	public Controller() {
		uebungauswaehlen = new Button();
		aufgabeerstellen = new Button();
		aufgabeladen = new Button();
		tutorial = new Button();
		einstellungen = new Button();
	}

	@FXML
    public void weiter(ActionEvent event) throws Exception {
		Scene newScene;
		
		if(event.getSource()==uebungauswaehlen) {
			Parent root = FXMLLoader.load(getClass().getResource("AufgabeBearbeiten.fxml"));
			newScene = new Scene(root);
		}else if(event.getSource()==aufgabeerstellen) {
			Parent root = FXMLLoader.load(getClass().getResource("AufgabeBearbeiten.fxml"));
			newScene = new Scene(root);
		}else if(event.getSource()==aufgabeladen) {
			Parent root = FXMLLoader.load(getClass().getResource("AufgabeLaden.fxml"));
			newScene = new Scene(root);
		}else if(event.getSource()==tutorial) {
			Parent root = FXMLLoader.load(getClass().getResource("AufgabeBearbeiten.fxml"));
			newScene = new Scene(root);
		}else if(event.getSource()==einstellungen) {
			Parent root = FXMLLoader.load(getClass().getResource("Einstellungen.fxml"));
			newScene = new Scene(root);
		}else {
			Parent root = FXMLLoader.load(getClass().getResource("Einstellungen.fxml"));
			newScene = new Scene(root);
		}
		
		Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
		window.setScene(newScene);
		window.show();
	}
	
	public void zurueck() {
		//zum vorherigen Fenster zurück
	}

/*////////////////////////////////////////////////////////////////////
//  Dropdown Buttons in Einstellungen                 			   //
//////////////////////////////////////////////////////////////////*/

	@FXML
	private ChoiceBox<String> choiceboxDesigntheme;
	
	@FXML
	private ChoiceBox<String> choiceboxFarbschema;

	@FXML
	private ChoiceBox<String> choiceboxSchriftgroesse;
	
	@FXML
	private ChoiceBox<String> choiceboxAufloesung;
	
	@FXML
	public void initialize() {
		choiceboxDesigntheme.getItems().add("Modena");
		choiceboxDesigntheme.getItems().add("Caspian");
		choiceboxDesigntheme.getSelectionModel().selectFirst();
		
		choiceboxDesigntheme.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>() {
	        @Override
	        public void changed(ObservableValue<? extends Number> observableValue, Number number, Number number2) {
	          if(choiceboxDesigntheme.getItems().get((Integer) number2)== "Modena") {
	        	  Application.setUserAgentStylesheet(Application.STYLESHEET_MODENA);
	          } else 
	        	  Application.setUserAgentStylesheet(Application.STYLESHEET_CASPIAN);
	        }
		 });
	        
		choiceboxFarbschema.getItems().add("Dark Theme");
		choiceboxFarbschema.getItems().add("Light Theme");
		choiceboxFarbschema.getSelectionModel().selectFirst();
		
		choiceboxSchriftgroesse.getItems().add("12");
		choiceboxSchriftgroesse.getItems().add("14");
		choiceboxSchriftgroesse.setValue("12");
		
		choiceboxAufloesung.getItems().add("1600x900");
		choiceboxAufloesung.getItems().add("900x600");
		choiceboxAufloesung.setValue("1600x900");
		
		
	}
	
	protected void buttonPressed(){
		
	}
}

