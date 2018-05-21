package reslearn.gui.controller;

import java.io.File;
import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import reslearn.gui.View;

public class ControllerUebungAuswaehlen extends Controller {

	File f = new File("C:/Programme");
	File[] fileArray = f.listFiles();
	
	@FXML
	private Button zurueck;
	@FXML
	private Button home;
	@FXML
	private Button aufgabe1;
	@FXML
	private Button aufgabe2;
	@FXML
	private Button aufgabe3;
	@FXML
	private Button aufgabe4;
	@FXML
	private Button aufgabe5;
	@FXML
	private Button aufgabe6;

	@FXML
	public void home(ActionEvent event) throws Exception {
		Scene newScene;
		Parent root;
		try {
			root = FXMLLoader.load(getClass().getResource(hauptmenue()));
			newScene = new Scene(root);
			Stage stage = new Stage();
			stage.setTitle("ResLearn");
			stage.setMaximized(true);
			stage.setScene(newScene);
			stage.show();
			((Node) (event.getSource())).getScene().getWindow().hide();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@FXML
	public void zurueck(ActionEvent event) throws Exception {
		Scene newScene;
		Parent root;
		try {
			root = FXMLLoader.load(getClass().getResource(vorherigesFenster(alleFenster)));
			newScene = new Scene(root);
			Stage stage = new Stage();
			stage.setTitle("ResLearn");
			stage.setMaximized(true);
			stage.setScene(newScene);
			stage.show();
			((Node) (event.getSource())).getScene().getWindow().hide();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@FXML
	public void weiter1(ActionEvent event) {
		View view = new View();
		try {
			AufgabenNummer = 1;
			alleFenster.add("../fxml/UebungAuswaehlen.fxml");
			view.start(View.classStage);
			((Node) (event.getSource())).getScene().getWindow().hide();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@FXML
	public void weiter2(ActionEvent event) {
		View view = new View();
		try {
			AufgabenNummer = 2;
			alleFenster.add("../fxml/UebungAuswaehlen.fxml");
			view.start(View.classStage);
			((Node) (event.getSource())).getScene().getWindow().hide();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@FXML
	public void weiter3(ActionEvent event) {
		View view = new View();
		try {
			AufgabenNummer = 3;
			alleFenster.add("../fxml/UebungAuswaehlen.fxml");
			view.start(View.classStage);
			((Node) (event.getSource())).getScene().getWindow().hide();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@FXML
	public void weiter4(ActionEvent event) {
		View view = new View();
		try {
			AufgabenNummer = 4;
			alleFenster.add("../fxml/UebungAuswaehlen.fxml");
			view.start(View.classStage);
			((Node) (event.getSource())).getScene().getWindow().hide();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@FXML
	public void weiter5(ActionEvent event) {
		View view = new View();
		try {
			AufgabenNummer = 5;
			alleFenster.add("../fxml/UebungAuswaehlen.fxml");
			view.start(View.classStage);
			((Node) (event.getSource())).getScene().getWindow().hide();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@FXML
	public void weiter6(ActionEvent event) {
		View view = new View();
		try {
			AufgabenNummer = 6;
			alleFenster.add("../fxml/UebungAuswaehlen.fxml");
			view.start(View.classStage);
			((Node) (event.getSource())).getScene().getWindow().hide();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
