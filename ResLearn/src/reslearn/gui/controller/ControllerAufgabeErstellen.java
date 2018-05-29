package reslearn.gui.controller;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TablePosition;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import reslearn.gui.ImportExport.CsvWriter;
import reslearn.gui.tableedit.ArbeitspaketTableData;
import reslearn.gui.tableedit.EditCell;
import reslearn.gui.tableedit.MyIntegerStringConverter;
import reslearn.model.paket.Arbeitspaket;

public class ControllerAufgabeErstellen extends Controller {

	private static int anzPakete, anzMaxPersonen;
	final ToggleGroup rbGruppe = new ToggleGroup();
	private String ergebnisValidierung = "";
	private ObservableList<ArbeitspaketTableData> data = FXCollections.observableArrayList();

	@FXML
	private Button zurueck;
	@FXML
	private Button home;

	// Anzahl Pakete
	@FXML
	Button buttonAnzPaketeMinus = new Button();
	@FXML
	Button buttonAnzPaketePlus = new Button();
	@FXML
	TextField textFieldAnzPakete = new TextField();

	// Tabelle
	@FXML
	TableView<ArbeitspaketTableData> tabelle;
	@FXML
	TableColumn<ArbeitspaketTableData, String> spalteID;
	@FXML
	TableColumn<ArbeitspaketTableData, Integer> spalteFaz;
	@FXML
	TableColumn<ArbeitspaketTableData, Integer> spalteSaz;
	@FXML
	TableColumn<ArbeitspaketTableData, Integer> spalteFez;
	@FXML
	TableColumn<ArbeitspaketTableData, Integer> spalteSez;
	@FXML
	TableColumn<ArbeitspaketTableData, Integer> spalteAnzMitarbeiter;
	@FXML
	TableColumn<ArbeitspaketTableData, Integer> spalteAufwand;

	// Auswahl Optimierungsverfahren (Radiobuttons)
	@FXML
	RadioButton radioButtonKapazitaet = new RadioButton();
	@FXML
	RadioButton radioButtonTermin = new RadioButton();
	@FXML
	Pane panePersonen = new Pane();

	// maximale Personen Parallel (kapazitÃ¤tstreu)
	@FXML
	Button buttonMaxPersonenMinus = new Button();
	@FXML
	Button buttonMaxPersonenPlus = new Button();
	@FXML
	TextField textFieldMaxPersonen = new TextField();

	// Button zur Validierung
	@FXML
	Button buttonValidieren;

	// Ergebnis Validierung anzeigen
	@FXML
	Pane paneErgebnis;
	@FXML
	Label labelErgebnis;

	TextField dateiname;
	String dateipfad = "C:\\Users\\Eric Botor\\git\\ResLearn\\";

	public static Arbeitspaket[] pakete;

	public void initialize() {
		anzPakete = Integer.parseInt(textFieldAnzPakete.getText());
		anzMaxPersonen = Integer.parseInt(textFieldMaxPersonen.getText());

		tabelle.setItems(data);
		populate(retrieveData());

		// den Spalten die richtigen Attribute zuteilen und bearbeitbar machen
		setupSpalteID();
		setupSpalteFaz();
		setupSpalteSaz();
		setupSpalteFez();
		setupSpalteSez();
		setupSpalteAnzMitarbeiter();
		setupSpalteAufwand();
		setTableEditable();
		tabelle.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

		radioButtonKapazitaet.setToggleGroup(rbGruppe);
		radioButtonKapazitaet.setSelected(true);
		radioButtonTermin.setToggleGroup(rbGruppe);
	}

	@FXML
	private void handleButtonValidierenAction(ActionEvent event) {
		paneErgebnis.setVisible(true);
//		pakete = getArbeitspaketArray(retrieveData());
		apArray(tabelle.getItems());
		System.out.println(pakete.length);
		if (paketeValidieren(pakete)) {
			labelErgebnis.setText(ergebnisValidierung);
			speichern(pakete, event);
		} else {
			labelErgebnis.setText(ergebnisValidierung);
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

	@FXML
	public void weiter(ActionEvent event) {
		Scene newScene;
		alleFenster.add("../fxml/AufgabeErstellen.fxml");
		Parent root;
		try {
			root = FXMLLoader.load(getClass().getResource("../fxml/ModusAuswaehlen.fxml"));
			newScene = new Scene(root);
			Stage stage = new Stage();
			stage.setTitle("ResLearn");
			stage.setMaximized(true);
			stage.setScene(newScene);
			stage.show();
			AufgabenNummer = 8;
			((Node) (event.getSource())).getScene().getWindow().hide();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@FXML
	private void handleButtonAnzPaketeMinusAction(ActionEvent event) {
		// Anzahl der Pakete soll nicht unter 1 sein
		if (anzPakete > 1) {
			anzPakete--;
			textFieldAnzPakete.setText(Integer.toString(anzPakete));

			// letzte Zeile entfernen
			tabelle.getItems().remove(anzPakete);
		}
	}

	@FXML
	private void handleButtonAnzPaketePlusAction(ActionEvent event) {
		if (anzPakete < 30) {
			anzPakete++;
			textFieldAnzPakete.setText(Integer.toString(anzPakete));

			// neue Zeile hinzufügen
			tabelle.getItems().add(new ArbeitspaketTableData(Integer.toString(anzPakete), 0, 0, 0, 0, 0, 0, 0));
		}
	}
	
//	private Arbeitspaket[] apArray(ObservableList<ArbeitspaketTableData> pakete) {
//		Arbeitspaket[] ap = new Arbeitspaket[pakete.size()];
//		
//		for (int i = 0; i < pakete.size(); i++) {
//			ap[i] = new Arbeitspaket();
//			ap[i].setId(pakete.get(i).getId());
//			ap[i].setFaz(pakete.get(i).getFaz());
//			ap[i].setSaz(pakete.get(i).getSaz());
//			ap[i].setFez(pakete.get(i).getFez());
//			ap[i].setSez(pakete.get(i).getSez());
//			ap[i].setMitarbeiteranzahl(pakete.get(i).getMitarbeiteranzahl());
//			ap[i].setAufwand(pakete.get(i).getAufwand());
//		}
//		return ap;
//	}
	private void apArray(ObservableList<ArbeitspaketTableData> paketList) {
		pakete = new Arbeitspaket[paketList.size()];
		
		for (int i = 0; i < paketList.size(); i++) {
			pakete[i] = new Arbeitspaket();
			pakete[i].setId(paketList.get(i).getId());
			pakete[i].setFaz(paketList.get(i).getFaz());
			pakete[i].setSaz(paketList.get(i).getSaz());
			pakete[i].setFez(paketList.get(i).getFez());
			pakete[i].setSez(paketList.get(i).getSez());
			pakete[i].setMitarbeiteranzahl(paketList.get(i).getMitarbeiteranzahl());
			pakete[i].setAufwand(paketList.get(i).getAufwand());
		}
	}

	// Tabelle mit Default-Werten befüllen
	private List<Arbeitspaket> retrieveData() {

//		return Arrays.asList(new Arbeitspaket("A", 1, 2, 1, 2, 2, 1, 2), new Arbeitspaket("B", 3, 3, 3, 3, 1, 3, 3),
//				new Arbeitspaket("C", 4, 5, 4, 5, 2, 2, 4), new Arbeitspaket("D", 4, 4, 4, 4, 1, 2, 2));
		
		return Arrays.asList(new Arbeitspaket("1", 0, 0, 0, 0, 0, 0, 0), new Arbeitspaket("2", 0, 0, 0, 0, 0, 0, 0),
				new Arbeitspaket("3", 0, 0, 0, 0, 0, 0, 0), new Arbeitspaket("4", 0, 0, 0, 0, 0, 0, 0));
	}

	private void populate(final List<Arbeitspaket> pakete) {
		pakete.forEach(p -> data.add(new ArbeitspaketTableData(p)));
	}

	private void setupSpalteID() {
		spalteID.setCellValueFactory(new PropertyValueFactory<>("id"));
		// sets the cell factory to use EditCell which will handle key presses
		// and firing commit events
		spalteID.setCellFactory(EditCell.<ArbeitspaketTableData>forTableColumn());
		// updates the salary field on the PersonTableData object to the
		// committed value
		spalteID.setOnEditCommit(event -> {
			final String value = event.getNewValue() != null ? event.getNewValue() : event.getOldValue();
			event.getTableView().getItems().get(event.getTablePosition().getRow()).setId(value);
			tabelle.refresh();
		});
	}

	private void setupSpalteFaz() {
		spalteFaz.setCellValueFactory(new PropertyValueFactory<>("faz"));
		// sets the cell factory to use EditCell which will handle key presses
		// and firing commit events
		spalteFaz.setCellFactory(
				EditCell.<ArbeitspaketTableData, Integer>forTableColumn(new MyIntegerStringConverter()));
		// updates the salary field on the PersonTableData object to the
		// committed value
		spalteFaz.setOnEditCommit(event -> {
			final Integer value = event.getNewValue() != null ? event.getNewValue() : event.getOldValue();
			event.getTableView().getItems().get(event.getTablePosition().getRow()).setFaz(value);
			tabelle.refresh();
		});
	}

	private void setupSpalteSaz() {
		spalteSaz.setCellValueFactory(new PropertyValueFactory<>("saz"));
		// sets the cell factory to use EditCell which will handle key presses
		// and firing commit events
		spalteSaz.setCellFactory(
				EditCell.<ArbeitspaketTableData, Integer>forTableColumn(new MyIntegerStringConverter()));
		// updates the salary field on the PersonTableData object to the
		// committed value
		spalteSaz.setOnEditCommit(event -> {
			final Integer value = event.getNewValue() != null ? event.getNewValue() : event.getOldValue();
			event.getTableView().getItems().get(event.getTablePosition().getRow()).setSaz(value);
			tabelle.refresh();
		});
	}

	private void setupSpalteFez() {
		spalteFez.setCellValueFactory(new PropertyValueFactory<>("fez"));
		// sets the cell factory to use EditCell which will handle key presses
		// and firing commit events
		spalteFez.setCellFactory(
				EditCell.<ArbeitspaketTableData, Integer>forTableColumn(new MyIntegerStringConverter()));
		// updates the salary field on the PersonTableData object to the
		// committed value
		spalteFez.setOnEditCommit(event -> {
			final Integer value = event.getNewValue() != null ? event.getNewValue() : event.getOldValue();
			event.getTableView().getItems().get(event.getTablePosition().getRow()).setFez(value);
			tabelle.refresh();
		});
	}

	private void setupSpalteSez() {
		spalteSez.setCellValueFactory(new PropertyValueFactory<>("sez"));
		// sets the cell factory to use EditCell which will handle key presses
		// and firing commit events
		spalteSez.setCellFactory(
				EditCell.<ArbeitspaketTableData, Integer>forTableColumn(new MyIntegerStringConverter()));
		// updates the salary field on the PersonTableData object to the
		// committed value
		spalteSez.setOnEditCommit(event -> {
			final Integer value = event.getNewValue() != null ? event.getNewValue() : event.getOldValue();
			event.getTableView().getItems().get(event.getTablePosition().getRow()).setSez(value);
			tabelle.refresh();
		});
	}

	private void setupSpalteAnzMitarbeiter() {
		spalteAnzMitarbeiter.setCellValueFactory(new PropertyValueFactory<>("mitarbeiteranzahl"));
		// sets the cell factory to use EditCell which will handle key presses
		// and firing commit events
		spalteAnzMitarbeiter.setCellFactory(
				EditCell.<ArbeitspaketTableData, Integer>forTableColumn(new MyIntegerStringConverter()));
		// updates the salary field on the PersonTableData object to the
		// committed value
		spalteAnzMitarbeiter.setOnEditCommit(event -> {
			final Integer value = event.getNewValue() != null ? event.getNewValue() : event.getOldValue();
			event.getTableView().getItems().get(event.getTablePosition().getRow()).setMitarbeiteranzahl(value);
			tabelle.refresh();
		});
	}

	private void setupSpalteAufwand() {
		spalteAufwand.setCellValueFactory(new PropertyValueFactory<>("aufwand"));
		// sets the cell factory to use EditCell which will handle key presses
		// and firing commit events
		spalteAufwand.setCellFactory(
				EditCell.<ArbeitspaketTableData, Integer>forTableColumn(new MyIntegerStringConverter()));
		// updates the salary field on the PersonTableData object to the
		// committed value
		spalteAufwand.setOnEditCommit(event -> {
			final Integer value = event.getNewValue() != null ? event.getNewValue() : event.getOldValue();
			event.getTableView().getItems().get(event.getTablePosition().getRow()).setAufwand(value);
			tabelle.refresh();
		});
	}

	private void setTableEditable() {
		tabelle.setEditable(true);
		// allows the individual cells to be selected
		tabelle.getSelectionModel().cellSelectionEnabledProperty().set(true);
		// when character or numbers pressed it will start edit in editable fields
		tabelle.setOnKeyPressed(event -> {
			if (event.getCode().isLetterKey() || event.getCode().isDigitKey()) {
				editFocusedCell();
			}
		});
	}

	@SuppressWarnings("unchecked")
	private void editFocusedCell() {
		final TablePosition<ArbeitspaketTableData, ?> focusedCell = tabelle.focusModelProperty().get()
				.focusedCellProperty().get();
		tabelle.edit(focusedCell.getRow(), focusedCell.getTableColumn());
	}

	public static Arbeitspaket[] getArbeitspaketArray(List<Arbeitspaket> pakete) {
		Arbeitspaket[] arbeitspakete = new Arbeitspaket[anzPakete];
		for (int i = 0; i < anzPakete; i++) {
			arbeitspakete[i] = pakete.get(i);
		}
		return arbeitspakete;
	}

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

	private boolean paketeValidieren(Arbeitspaket[] arbeitspaket) {
		boolean idKorrekt, fazKorrekt, sazKorrekt, fezKorrekt, sezKorrekt, paketKorrekt;
		String[] id = new String[arbeitspaket.length];
		int faz, saz, fez, sez;

		idKorrekt = fazKorrekt = sazKorrekt = fezKorrekt = sezKorrekt = paketKorrekt = false;

		MAIN_LOOP: for (int i = 0; i < arbeitspaket.length; i++) {
			id[i] = arbeitspaket[i].getId();
			faz = arbeitspaket[i].getFaz();
			saz = arbeitspaket[i].getSaz();
			fez = arbeitspaket[i].getFez();
			sez = arbeitspaket[i].getSez();

			// ID prüfen (einzigartig?)
			for (int j = i - 1; j >= 0; j--) {
				if (id[i] != id[j]) {
					idKorrekt = true;
				} else {
					paketKorrekt = false;
					ergebnisValidierung = "Die Arbeitspaket-ID '" + arbeitspaket[i].getId()
							+ "' darf nur ein mal verwendet werden";
					break MAIN_LOOP;
				}
			}

			// FAZ prüfen
			if (faz >= 1) {
				fazKorrekt = true;
			} else {
				ergebnisValidierung = "Der Wert FAZ für das Arbeitspaket " + arbeitspaket[i].getId()
						+ " muss mindestens 1 sein";
				paketKorrekt = false;
				break;
			}

			// SAZ prüfen
			if (saz >= faz) {
				sazKorrekt = true;
			} else {
				ergebnisValidierung = "Der Wert SAZ für das Arbeitspaket " + arbeitspaket[i].getId()
						+ " muss mindestens gleich gro wie der Wert FAZ sein";
				paketKorrekt = false;
				break;
			}

			// FEZ prüfen
			if (fez > faz) {
				fezKorrekt = true;
			} else {
				ergebnisValidierung = "Der Wert FEZ für das Arbeitspaket " + arbeitspaket[i].getId()
						+ " muss größer als der Wert FAZ sein";
				paketKorrekt = false;
				break;
			}

			// SEZ prüfen
			if (sez >= fez) {
				sezKorrekt = true;
			} else {
				ergebnisValidierung = "Der Wert SEZ für das Arbeitspaket " + arbeitspaket[i].getId()
						+ " muss mindestens gleich groß wie der Wert FEZ sein";
				paketKorrekt = false;
				break;
			}

			// Alle Bedingungen prüfen
			if (idKorrekt && fazKorrekt && sazKorrekt && fezKorrekt && sezKorrekt) {
				ergebnisValidierung = "Validierung erfolgreich";
				paketKorrekt = true;
			}
		}

		return paketKorrekt;
	}

	public void export(Arbeitspaket[] arbeitspakete, ActionEvent event) {
		// TODO Mit anderen dateipfaden gibt es ein Berechtigungsproblem
		String outputFile = dateipfad + dateiname.getText() + ".csv";
		boolean alreadyExists = new File(outputFile).exists();
		String spalten[] = new String[8];

		spalten[0] = "Id";
		spalten[1] = "FAZ";
		spalten[2] = "FEZ";
		spalten[3] = "SAZ";
		spalten[4] = "SEZ";
		spalten[5] = "Vorgangsdauer";
		spalten[6] = "Mitarbeiteranzahl";
		spalten[7] = "Aufwand";

		try {
			// use FileWriter constructor that specifies open for appending
			CsvWriter csvOutput = new CsvWriter(new FileWriter(outputFile, true), ';');

			// if the file didn't already exist then we need to write out the header line
			if (!alreadyExists) {
				csvOutput.writeRecord(spalten);
				weiter(event);
			} else {
				Alert alert = new Alert(AlertType.CONFIRMATION);
				alert.setHeaderText("Warnung");
				alert.setContentText("Der Dateiname existiert bereits. Datei überschreiben");
				Optional<ButtonType> result = alert.showAndWait();
				if (result.get() == ButtonType.OK) {
					weiter(event);
				} else {
					alert.close();
					return;
				}
			}
			// else assume that the file already has the correct header line

			// write out a few records
			for (Arbeitspaket ap : arbeitspakete) {
				int vorgangsdauer = ap.getSez() - ap.getFez();
				csvOutput.write(ap.getId().toString());
				csvOutput.write(String.valueOf(ap.getFaz()));
				csvOutput.write(String.valueOf(ap.getFez()));
				csvOutput.write(String.valueOf(ap.getSaz()));
				csvOutput.write(String.valueOf(ap.getSez()));
				csvOutput.write(String.valueOf(vorgangsdauer));
				csvOutput.write(String.valueOf(ap.getMitarbeiteranzahl()));
				csvOutput.write(String.valueOf(ap.getAufwand()));
				csvOutput.endRecord();
			}

			csvOutput.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public void speichern(Arbeitspaket[] arbeitspakete, ActionEvent event) {
		Dialog<ButtonType> dialog = new Dialog<>();
		dialog.setTitle("Speichern");
		dialog.setHeaderText("Wollen Sie die Aufgabe speichern?");

		ButtonType speichernButton = new ButtonType("Speichern", ButtonData.OK_DONE);
		ButtonType weiterButton = new ButtonType("Weiter", ButtonData.OK_DONE);
		dialog.getDialogPane().getButtonTypes().addAll(speichernButton, weiterButton);

		GridPane grid = new GridPane();
		grid.setHgap(10);
		grid.setVgap(10);
		grid.setPadding(new Insets(20, 150, 10, 10));

		dateiname = new TextField();
		dateiname.setPromptText("Dateiname");

		grid.add(new Label("Dateiname:"), 0, 0);
		grid.add(dateiname, 1, 0);

		Node nodeSpeichern = dialog.getDialogPane().lookupButton(speichernButton);
		nodeSpeichern.setDisable(true);

		dialog.getDialogPane().getButtonTypes().add(ButtonType.CLOSE);
		Node closeButton = dialog.getDialogPane().lookupButton(ButtonType.CLOSE);
		closeButton.managedProperty().bind(closeButton.visibleProperty());
		closeButton.setVisible(false);

		dateiname.textProperty().addListener((observable, oldValue, newValue) -> {
			nodeSpeichern.setDisable(newValue.trim().isEmpty());
		});

		dialog.getDialogPane().setContent(grid);
		Platform.runLater(() -> dateiname.requestFocus());

		Optional<ButtonType> result = dialog.showAndWait();
		if (result.get() == speichernButton) {
			export(arbeitspakete, event);
		} else if (result.get() == weiterButton) {
			weiter(event);
		} else {
			dialog.close();
		}
	}

}
