package reslearn.gui.controller;

import java.io.IOException;
import java.util.Optional;

import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

public class ControllerUebungsmodus extends Controller{
		
	@FXML
	private ImageView zurueck;
	@FXML 
	private ImageView home;
		
	@FXML
	public void zurueck() throws Exception{
		zurueck.addEventFilter(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
			Scene newScene;
			public void handle(MouseEvent event) {
				Alert alert = new Alert(AlertType.CONFIRMATION);
				alert.setHeaderText("Warnung");
				alert.setContentText("Wirklich zum " + vorherigesFenster(alleFenster) +" zur�ck");
				Optional<ButtonType> result = alert.showAndWait();
				if (result.get()== ButtonType.OK) {
					System.out.println("Ok Button gedr�ckt");
					Parent root;
					try {
						root = FXMLLoader.load(getClass().getResource(vorherigesFenster(alleFenster)));
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
		});
	}
	
	@FXML
	public void home() throws Exception {
		home.addEventFilter(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
			Scene newScene;
			public void handle(MouseEvent event) {
				Alert alert = new Alert(AlertType.CONFIRMATION);
				alert.setHeaderText("Warnung");
				alert.setContentText("Wirklich zum Hauptmen� zur�ckkehren? Alle �nderungen gehen verloren");
				Optional<ButtonType> result = alert.showAndWait();
				if (result.get()== ButtonType.OK) {
					System.out.println("Ok Button gedr�ckt");
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
		});
	}

}
