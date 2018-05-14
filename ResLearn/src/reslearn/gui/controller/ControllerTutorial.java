package reslearn.gui.controller;

import java.io.IOException;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

public class ControllerTutorial extends Controller{
		
	@FXML
	private ImageView zurueck;
	@FXML 
	private ImageView home;
	@FXML
	private Button quizStarten;
	
	@FXML
	public void quizStarten (ActionEvent event) throws Exception{
		Scene newScene;
		alleFenster.add("../fxml/Tutorial.fxml");
		Parent root = FXMLLoader.load(getClass().getResource("../fxml/TutorialFragen.fxml"));
		newScene = new Scene(root);
		Stage stage = new Stage();
		stage.setTitle("ResLearn");
		stage.setMaximized(true);
		stage.setScene(newScene);
		stage.show();
		((Node) (event.getSource())).getScene().getWindow().hide();
	}
		
	@FXML
	public void zurueck() throws Exception{
		zurueck.addEventFilter(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
			Scene newScene;
			public void handle(MouseEvent event) {
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
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				((Node) (event.getSource())).getScene().getWindow().hide();
			}
		});
	}
	
	@FXML
	public void home() throws Exception {
		home.addEventFilter(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
			Scene newScene;

			public void handle(MouseEvent event) {
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
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				((Node) (event.getSource())).getScene().getWindow().hide();
			}
		});
	}

}
