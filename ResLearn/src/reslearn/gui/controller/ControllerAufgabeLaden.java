package reslearn.gui.controller;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import reslearn.gui.ImportExport.CsvReader;
import reslearn.model.paket.Arbeitspaket;

public class ControllerAufgabeLaden extends Controller {

	@FXML
	private Button zurueck;
	@FXML
	private Button home;
	@FXML
	private Label labellDateiname;
	@FXML
	private Button laden;
	@FXML
	private Button dateiauswaehlen;

	public static Arbeitspaket[] paketeArray;
	private String name;
	private String dateipfad = "C:\\Users\\Eric Botor\\git\\ResLearn\\";

	@FXML
	public void dateiauswaehlen() {
		FileChooser fileChooser = new FileChooser();
		File defaultDirectory = new File(dateipfad);
		fileChooser.setInitialDirectory(defaultDirectory);
		FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("CSV files (*.csv)", "*.csv");
		fileChooser.getExtensionFilters().add(extFilter);
		File file = fileChooser.showOpenDialog(dateiauswaehlen.getScene().getWindow());
		name = file.getName();
		labellDateiname.setText("Wollen Sie die " + name + " Aufgabe laden?");
		labellDateiname.setVisible(true);
		laden.setDisable(false);
	}

	@FXML
	public void laden(ActionEvent event) {
		try {

			CsvReader arbeitspaketImport = new CsvReader(dateipfad + name);

			arbeitspaketImport.readHeaders();
			ArrayList<Arbeitspaket> pakete = new ArrayList<Arbeitspaket>();

			while (arbeitspaketImport.readRecord()) {

				Arbeitspaket ap = new Arbeitspaket();

				try {
					String zeile = arbeitspaketImport.get(0);
					String[] spalten = zeile.split(";");

					if (spalten.length == 8) {
						ap.setId(spalten[0]);
						ap.setFaz(Integer.valueOf(spalten[1]));
						ap.setFez(Integer.valueOf(spalten[2]));
						ap.setSaz(Integer.valueOf(spalten[3]));
						ap.setSez(Integer.valueOf(spalten[4]));
						ap.setVorgangsdauer(Integer.valueOf(spalten[5]));
						ap.setMitarbeiteranzahl(Integer.valueOf(spalten[6]));
						ap.setAufwand(Integer.valueOf(spalten[7]));
					}

					pakete.add(ap);
				} catch (NumberFormatException e) {
					e.printStackTrace();
				}
			}

			arbeitspaketImport.close();

			paketeArray = getArbeitspaketArray(pakete);

			weiter(event);

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
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

	public void weiter(ActionEvent event) {
		alleFenster.add("../fxml/AufgabeLaden.fxml");
		Scene newScene;
		Parent root;
		try {
			root = FXMLLoader.load(getClass().getResource("../fxml/ModusAuswaehlen.fxml"));
			newScene = new Scene(root);
			Stage stage = new Stage();
			stage.setTitle("ResLearn");
			stage.setMaximized(true);
			stage.setScene(newScene);
			stage.show();
			AufgabenNummer = 7;
			((Node) (event.getSource())).getScene().getWindow().hide();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static Arbeitspaket[] getArbeitspaketArray(ArrayList<Arbeitspaket> pakete) {
		Arbeitspaket arbeitspakete[] = new Arbeitspaket[pakete.size()];
		int i = 0;
		for (Arbeitspaket ap : pakete) {
			arbeitspakete[i++] = ap;
		}
		return arbeitspakete;
	}
}
