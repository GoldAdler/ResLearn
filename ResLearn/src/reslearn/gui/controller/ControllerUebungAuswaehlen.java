package reslearn.gui.controller;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import reslearn.gui.DisplayCanvas;
import reslearn.model.paket.Arbeitspaket;

public class ControllerUebungAuswaehlen extends Controller {

	private List<Button> buttonlist = new ArrayList<>();
	public Arbeitspaket[] paketeArray;
	private Pane pane;
	File f = new File("..\\uebungen");
	File[] fileArray = f.listFiles();

	public void initialize() {

	}

	public Pane erstellePane() {

		pane = new Pane();
		pane.setPrefWidth(DisplayCanvas.paneBreite);
		pane.setPrefHeight(DisplayCanvas.paneLaenge);
		pane.setLayoutX(DisplayCanvas.paneLayoutX);
		pane.setLayoutY(DisplayCanvas.paneLayoutY);

		for (int i = 0; i < 5; i++) {
			System.out.println("TOLL ein BUTTONG: " + i);
			Button b = new Button("Hallo" + i);
			b.setOnAction(ButtonAction);
			b.setPrefHeight(i * 50);
			b.setPrefWidth(i * 50);
			b.setLayoutX(i * 50);
			b.setLayoutY(i * 100);
			// b.setAlignment(i * 50);
			pane.getChildren().add(b);
		}
		return pane;
	}

	// Event Handler Maus klicken
	private EventHandler<ActionEvent> ButtonAction = new EventHandler<ActionEvent>() {

		@Override
		public void handle(ActionEvent event) {
			weiter(event);

		}
	};

	@FXML
	public void weiter(ActionEvent event) {
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

	// public void buttonZuordnen() {
	// // buttonlist.add(button1);
	// // buttonlist.add(button2);
	// // buttonlist.add(button3);
	// // buttonlist.add(button4);
	// // buttonlist.add(button5);
	// // buttonlist.add(button6);
	// // buttonlist.add(button7);
	// // buttonlist.add(button8);
	// // buttonlist.add(button9);
	// // buttonlist.add(button10);
	// // buttonlist.add(button11);
	// // buttonlist.add(button12);
	// button5.setVisible(true);
	// // for (int i = 0; i < 5; i++) {
	// // System.out.println("jkhgbjhvjkbk");
	// // buttonlist.get(i).setVisible(true);
	// // // buttonlist.get(i).setText(fileArray[i].getName());
	// // }
	//
	// // weiter(event, name) übergeben, mit Name aufgabe auswählen
	// // aufgabe laden aufrufen
	// }

	// // @FXML
	// public void buttonErstellen(ActionEvent event) throws Exception {
	//
	// for (int i = 0; i < fileArray.length; i++) {
	// String name = fileArray[i].getName();
	// buttonlist.add(new Button(name));
	// }
	// }
	//
	// public void aufgabeLadenKlick(ActionEvent event) {
	// View view = new View();
	// try {
	// String buttonName = event.getSource().toString() + ".csv";
	// for (int i = 0; i < buttonlist.size(); i++) {
	// if (buttonName == buttonlist.get(i).toString()) {
	// AufgabeLadenImport importAufgabe = new AufgabeLadenImport();
	// paketeArray = importAufgabe.aufgabeLaden(fileArray[i].getPath());
	// }
	// }
	//
	// alleFenster.add("../fxml/UebungAuswaehlen.fxml");
	// view.start(View.classStage);
	// ((Node) (event.getSource())).getScene().getWindow().hide();
	// } catch (Exception e) {
	// e.printStackTrace();
	// }
	// }

	@FXML
	private Button zurueck;
	@FXML
	private Button home;

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

	// @FXML
	// public void weiter1(ActionEvent event) {
	// View view = new View();
	// try {
	// AufgabenNummer = 1;
	// alleFenster.add("../fxml/UebungAuswaehlen.fxml");
	// view.start(View.classStage);
	// ((Node) (event.getSource())).getScene().getWindow().hide();
	// } catch (Exception e) {
	// e.printStackTrace();
	// }
	// }
	//
	// @FXML
	// public void weiter2(ActionEvent event) {
	// View view = new View();
	// try {
	// AufgabenNummer = 2;
	// alleFenster.add("../fxml/UebungAuswaehlen.fxml");
	// view.start(View.classStage);
	// ((Node) (event.getSource())).getScene().getWindow().hide();
	// } catch (Exception e) {
	// e.printStackTrace();
	// }
	// }
	//
	// @FXML
	// public void weiter3(ActionEvent event) {
	// View view = new View();
	// try {
	// AufgabenNummer = 3;
	// alleFenster.add("../fxml/UebungAuswaehlen.fxml");
	// view.start(View.classStage);
	// ((Node) (event.getSource())).getScene().getWindow().hide();
	// } catch (Exception e) {
	// e.printStackTrace();
	// }
	// }
	//
	// @FXML
	// public void weiter4(ActionEvent event) {
	// View view = new View();
	// try {
	// AufgabenNummer = 4;
	// alleFenster.add("../fxml/UebungAuswaehlen.fxml");
	// view.start(View.classStage);
	// ((Node) (event.getSource())).getScene().getWindow().hide();
	// } catch (Exception e) {
	// e.printStackTrace();
	// }
	// }
	//
	// @FXML
	// public void weiter5(ActionEvent event) {
	// View view = new View();
	// try {
	// AufgabenNummer = 5;
	// alleFenster.add("../fxml/UebungAuswaehlen.fxml");
	// view.start(View.classStage);
	// ((Node) (event.getSource())).getScene().getWindow().hide();
	// } catch (Exception e) {
	// e.printStackTrace();
	// }
	// }
	//
	// @FXML
	// public void weiter6(ActionEvent event) {
	// View view = new View();
	// try {
	// AufgabenNummer = 6;
	// alleFenster.add("../fxml/UebungAuswaehlen.fxml");
	// view.start(View.classStage);
	// ((Node) (event.getSource())).getScene().getWindow().hide();
	// } catch (Exception e) {
	// e.printStackTrace();
	// }
	// }
}
