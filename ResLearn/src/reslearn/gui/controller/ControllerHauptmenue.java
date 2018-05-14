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
import javafx.stage.Window;
import reslearn.gui.View;
import reslearn.gui.fxml.TutorialVideo;

public class ControllerHauptmenue extends Controller{
	
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
	@FXML
	private ImageView zurueck;
	
	//Hallo
	@FXML
    public void weiter(ActionEvent event) throws Exception{
		Scene newScene;
		alleFenster.add("../fxml/Hauptmenue.fxml");
		if (event.getSource() == uebungAuswaehlen) {
			View view = new View();
			view.start(View.classStage);
			((Node) (event.getSource())).getScene().getWindow().hide();
		} else if (event.getSource() == aufgabeErstellen) {
			Parent root = FXMLLoader.load(getClass().getResource("../fxml/AufgabeErstellen.fxml"));
			newScene = new Scene(root);
			Stage stage = new Stage();
			stage.setTitle("ResLearn");
			stage.setMaximized(true);
			stage.setScene(newScene);
			stage.show();
			((Node) (event.getSource())).getScene().getWindow().hide();
		}else if(event.getSource()==aufgabeLaden) {
			Parent root = FXMLLoader.load(getClass().getResource("../fxml/AufgabeLaden.fxml"));
			newScene = new Scene(root);
			Stage stage = new Stage();
			stage.setTitle("ResLearn");
			stage.setMaximized(true);
			stage.setScene(newScene);
			stage.show();
			((Node) (event.getSource())).getScene().getWindow().hide();
		}else if(event.getSource()==tutorial) {
			TutorialVideo tut = new TutorialVideo();
			tut.start(TutorialVideo.classStage);
			((Node) (event.getSource())).getScene().getWindow().hide();
		}else if(event.getSource()==einstellungen) {
			Parent root = FXMLLoader.load(getClass().getResource("../fxml/Einstellungen.fxml"));
			newScene = new Scene(root);
			Stage stage = new Stage();
			stage.setTitle("ResLearn");
			stage.setMaximized(true);
			stage.setScene(newScene);
			stage.show();
			((Node) (event.getSource())).getScene().getWindow().hide();
		}else {
			Parent root = FXMLLoader.load(getClass().getResource("../fxml/Hauptmenue.fxml"));
			newScene = new Scene(root);
			Stage stage = new Stage();
			stage.setTitle("ResLearn");
			stage.setMaximized(true);
			stage.setScene(newScene);
			stage.show();
			((Node) (event.getSource())).getScene().getWindow().hide();
		}
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

}