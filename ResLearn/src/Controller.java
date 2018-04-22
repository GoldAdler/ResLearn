

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;

public class Controller {
	@FXML
	Button uebungauswaehlen = new Button();
	@FXML
	Button aufgabeerstellen = new Button();
	@FXML
	Button aufgabeladen = new Button();
	@FXML
	Button tutorial = new Button();
	@FXML
	Button einstellungen = new Button();

	//Hallo Tätärä

	@FXML
    public void weiter() {
		//weiter zur nächsten Funktion
	}
	public void zurueck() {
		//zum vorherigen Fenster zurück
	}

/*////////////////////////////////////////////////////////////////////
//  Dropdown Buttons in Einstellungen                 			   //
//////////////////////////////////////////////////////////////////*/

	@FXML
	private ChoiceBox<String> choiceboxFarbschema;

	@FXML
	private ChoiceBox<String> choiceboxSchriftgroesse;
	
	@FXML
	private ChoiceBox<String> choiceboxAufloesung;
	
	@FXML
	public void initialize() {
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
}

