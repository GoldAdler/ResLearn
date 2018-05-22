package reslearn.gui.controller;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import reslearn.gui.View;
import reslearn.gui.ImportExport.AufgabeLadenImport;
import reslearn.model.paket.Arbeitspaket;

public class ControllerUebungAuswaehlen extends Controller {

	private List<Button> buttonlist = new ArrayList<>();
	public Arbeitspaket[] paketeArray;
	File f = new File("..\\dateien\\uebungen");
	File[] fileArray = f.listFiles();

	// public void initialize() {
	// buttonErstellen();
	// }

	// @FXML
	public void buttonErstellen(ActionEvent event) throws Exception {

		for (int i = 0; i < fileArray.length; i++) {
			String name = fileArray[i].getName();
			buttonlist.add(new Button(name));
		}
	}

	public void aufgabeLadenKlick(ActionEvent event) {
		View view = new View();
		try {
			String buttonName = event.getSource().toString() + ".csv";
			for (int i = 0; i < buttonlist.size(); i++) {
				if (buttonName == buttonlist.get(i).toString()) {
					AufgabeLadenImport importAufgabe = new AufgabeLadenImport();
					paketeArray = importAufgabe.aufgabeLaden(fileArray[i].getPath());
				}
			}

			alleFenster.add("../fxml/UebungAuswaehlen.fxml");
			view.start(View.classStage);
			((Node) (event.getSource())).getScene().getWindow().hide();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@FXML
	private Button zurueck;
	@FXML
	private Button home;

	// @FXML
	// private Button aufgabe1;
	// @FXML
	// private Button aufgabe2;
	// @FXML
	// private Button aufgabe3;
	// @FXML
	// private Button aufgabe4;
	// @FXML
	// private Button aufgabe5;
	// @FXML
	// private Button aufgabe6;

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
