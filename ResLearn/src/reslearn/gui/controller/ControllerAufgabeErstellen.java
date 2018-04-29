package reslearn.gui.controller;

import java.io.IOException;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import reslearn.model.paket.Arbeitspaket;

public class ControllerAufgabeErstellen {
	private int anzPakete, anzMaxPersonen;
	final ToggleGroup rbGruppe = new ToggleGroup();	
	private boolean paketKorrekt;
	
	// Zurück-Button ins Hauptmenü
	@FXML
	ImageView imagePfeil = new ImageView();
	
	@FXML
	private void handleImagePfeilAction(ActionEvent event) throws IOException {
//		AnchorPane pane = FXMLLoader.load(getClass().getResource("./fxml/Hauptmenue.fxml"));
//		rootPane.getChildern().setAll(pane);
	}
	
	// Anzahl Pakete
	@FXML
	Button buttonAnzPaketeMinus = new Button();
	@FXML
	Button buttonAnzPaketePlus = new Button();
	@FXML
	TextField textFieldAnzPakete = new TextField();
	
	@FXML
	private void handleButtonAnzPaketeMinusAction(ActionEvent event) {
		if (anzPakete > 1) {
			anzPakete--;
			textFieldAnzPakete.setText(Integer.toString(anzPakete));
		}
	}
	@FXML
	private void handleButtonAnzPaketePlusAction(ActionEvent event) {
		anzPakete++;
		textFieldAnzPakete.setText(Integer.toString(anzPakete));		
	}
	
	
	// Tabelle
	@FXML
	TableView<Arbeitspaket> tabelle = new TableView<>();
	@FXML
	TableColumn<Arbeitspaket, String> spalteID = new TableColumn<>();
	@FXML
	TableColumn<Arbeitspaket, Integer> spalteFaz = new TableColumn<>();
	@FXML
	TableColumn<Arbeitspaket, Integer> spalteSaz = new TableColumn<>();
	@FXML
	TableColumn<Arbeitspaket, Integer> spalteFez = new TableColumn<>();
	@FXML
	TableColumn<Arbeitspaket, Integer> spalteSez = new TableColumn<>();
	@FXML
	TableColumn<Arbeitspaket, Integer> spalteAnzMitarbeiter = new TableColumn<>();
	@FXML
	TableColumn<Arbeitspaket, Integer> spalteAufwand = new TableColumn<>();
	
	// Tabelle mit Werten befüllen
	public ObservableList<Arbeitspaket> getArbeitspaket() {
		ObservableList<Arbeitspaket> pakete = FXCollections.observableArrayList();
		pakete.add(new Arbeitspaket("A", 1, 6, 3, 3, 6, 4, 24));
		pakete.add(new Arbeitspaket("B", 7, 13, 10, 10, 7, 3, 21));
		pakete.add(new Arbeitspaket("C", 9, 16, 14, 14, 8, 2, 16));
		pakete.add(new Arbeitspaket("D", 14, 18, 17, 17, 5, 5, 25));
		pakete.add(new Arbeitspaket("E", 17, 22, 21, 21, 6, 3, 18));
//		pakete.add(new Arbeitspaket("A", 1, 6, 3, 3, 6, 4, 24));
//		pakete.add(new Arbeitspaket("B", 7, 13, 10, 10, 7, 3, 21));
//		pakete.add(new Arbeitspaket("C", 9, 16, 14, 14, 8, 2, 16));
//		pakete.add(new Arbeitspaket("D", 14, 18, 17, 17, 5, 5, 25));
//		pakete.add(new Arbeitspaket("E", 17, 22, 21, 21, 6, 3, 18));
		return pakete;
	}
	
	
	// Auswahl Optimierungsverfahren (Radiobuttons)
	@FXML
	RadioButton radioButtonKapazitaet = new RadioButton();
	@FXML
	RadioButton radioButtonTermin = new RadioButton();
	@FXML
	Pane panePersonen = new Pane();
	

	// maximale Personen Parallel (kapazitätstreu)
	@FXML
	Button buttonMaxPersonenMinus = new Button();
	@FXML
	Button buttonMaxPersonenPlus = new Button();
	@FXML
	TextField textFieldMaxPersonen = new TextField();
	
	@FXML
	private void handleButtonMaxPersonenMinusAction(ActionEvent event) {
		if (anzMaxPersonen > 1) {
			anzMaxPersonen--;
			textFieldMaxPersonen.setText(Integer.toString(anzMaxPersonen));
		}
	}
	@FXML
	private void handleButtonMaxPersonenPlusAction(ActionEvent event) {
		anzMaxPersonen++;
		textFieldMaxPersonen.setText(Integer.toString(anzMaxPersonen));		
	}
	
	// max. Personen bei termintreuer Opt. ausblenden
	@FXML
	private void handleRadioButtonKapazitaetAction(ActionEvent event) {
		panePersonen.setVisible(true);
	}
	@FXML
	private void handleRadioButtonTerminAction(ActionEvent event) {
		panePersonen.setVisible(false);
	}
	
	
	// Validieren
	@FXML
	Button buttonValidieren = new Button();
	
	@FXML
	private void handleButtonValidierenAction(ActionEvent event) {
		do {
			// TODO: prüfen aller eingegebenen Pakete
		} while (paketKorrekt);
		
		// TODO: Pane für Validierung Erfolgreich/Fehlgeschlagen erstellen
		if(paketKorrekt) {
//			paneValidierungErfolgreich.setVisible(true);
		} else {
//			paneValidierungFehler.setVisible(true);
		}
	}
	
	
	
	public void initialize() {		
		anzPakete = Integer.parseInt(textFieldAnzPakete.getText());
		anzMaxPersonen = Integer.parseInt(textFieldMaxPersonen.getText());
		
		spalteID.setCellValueFactory(new PropertyValueFactory<>("id"));
		spalteFaz.setCellValueFactory(new PropertyValueFactory<>("faz"));
		spalteSaz.setCellValueFactory(new PropertyValueFactory<>("saz"));
		spalteFez.setCellValueFactory(new PropertyValueFactory<>("fez"));
		spalteSez.setCellValueFactory(new PropertyValueFactory<>("sez"));
		spalteAnzMitarbeiter.setCellValueFactory(new PropertyValueFactory<>("mitarbeiteranzahl"));
		spalteAufwand.setCellValueFactory(new PropertyValueFactory<>("aufwand"));
		tabelle.setItems(getArbeitspaket());
		
		
		radioButtonKapazitaet.setToggleGroup(rbGruppe);
		radioButtonKapazitaet.setSelected(true);
		radioButtonTermin.setToggleGroup(rbGruppe);
	}

}
