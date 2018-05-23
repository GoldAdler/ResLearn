package reslearn.gui.controller;

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
import reslearn.gui.ImportExport.AufgabeLadenImport;
import reslearn.model.paket.Arbeitspaket;

public class ControllerModusAuswaehlen extends Controller {

	@FXML
	private Button ersterSchritt;
	@FXML
	private Button uebungsmodus;
	@FXML
	private Button loesungsmodus;
	@FXML
	private Button zurueck;
	@FXML
	private Button home;

	@FXML
	public void weiter(ActionEvent event) throws Exception {
		alleFenster.add("../fxml/ModusAuswaehlen.fxml");
		ControllerUebungAuswaehlen cua = new ControllerUebungAuswaehlen();
		AufgabeLadenImport ali = new AufgabeLadenImport();
		System.out.println(cua.f + "\\Aufgabe3" + ".csv");
		if (event.getSource() == ersterSchritt) {
			Arbeitspaket[] paketeArray = ali.aufgabeLaden("..\\Reslearn\\bin\\reslearn\\gui\\uebungen\\Aufgabe3.csv");
			// for (Arbeitspaket ap : paketeArray) {
			// System.out.print(ap.getId().toString());
			// System.out.print(", ");
			// System.out.print(String.valueOf(ap.getFaz()));
			// System.out.print(", ");
			// System.out.print(String.valueOf(ap.getFez()));
			// System.out.print(", ");
			// System.out.print(String.valueOf(ap.getSaz()));
			// System.out.print(", ");
			// System.out.print(String.valueOf(ap.getSez()));
			// System.out.print(", ");
			// System.out.print(String.valueOf(ap.getVorgangsdauer()));
			// System.out.print(", ");
			// System.out.print(String.valueOf(ap.getMitarbeiteranzahl()));
			// System.out.print(", ");
			// System.out.print(String.valueOf(ap.getAufwand()));
			// System.out.print(", ");
			// System.out.println();

			// }

			View.getInstance().initializeCanvasView(paketeArray);
			View.getInstance().start(new Stage());
			((Node) (event.getSource())).getScene().getWindow().hide();
		} else if (event.getSource() == uebungsmodus) {
			View.getInstance().start(new Stage());
			((Node) (event.getSource())).getScene().getWindow().hide();
		} else if (event.getSource() == loesungsmodus) {
			View.getInstance().start(new Stage());
			((Node) (event.getSource())).getScene().getWindow().hide();
		}
	}

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
}
