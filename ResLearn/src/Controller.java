import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
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

	//Hallo T‰t‰r‰
	public Controller() {
		uebungauswaehlen = new Button();
		aufgabeerstellen = new Button();
		aufgabeladen = new Button();
		tutorial = new Button();
		einstellungen = new Button();
	}

	@FXML
    public void weiter(ActionEvent event) throws Exception {
		//weiter zur n√§chsten Funktion
		System.out.println("Hello World");
		
		Parent root = FXMLLoader.load(getClass().getResource("/reslearn.view/Vorlage.fxml"));
		Scene newScene = new Scene(root);
		
		Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
		window.setScene(newScene);
		window.show();
	}
	public void zurueck() {
		//zum vorherigen Fenster zur√ºck
	}
	
}

