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
import reslearn.gui.DisplayCanvas;
import reslearn.gui.ImportExport.AufgabeLadenImport;
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

	// maximale Personen Parallel (kapazitätstreu)
	@FXML
	Button buttonMaxPersonenMinus = new Button();
	@FXML
	Button buttonMaxPersonenPlus = new Button();
	@FXML
	TextField textFieldMaxPersonen = new TextField();

	// Button zur Validierung
	@FXML
	Button buttonValidieren;

	TextField dateiname;
	// Pfad, unter dem die angelegte Aufgabe gespeichert wird
	String dateipfad = ".." + File.separator + "ResLearn" + File.separator + "bin" + File.separator + "reslearn"
			+ File.separator + "gui" + File.separator + "eigeneAufgaben" + File.separator;

	public Arbeitspaket[] pakete;

	public void initialize() {
		anzPakete = Integer.parseInt(textFieldAnzPakete.getText());
		// anzMaxPersonen = Integer.parseInt(textFieldMaxPersonen.getText());

		setupLayout();
		setupTabelle();

		radioButtonKapazitaet.setToggleGroup(rbGruppe);
		radioButtonKapazitaet.setSelected(true);
		radioButtonTermin.setToggleGroup(rbGruppe);
	}

	/**
	 * Ueberprueft die Eingaben des Useres, ob diese korrekt sind.
	 *
	 * @param event
	 */
	@FXML
	private void handleButtonValidierenAction(ActionEvent event) {
		getArbeitspaketArray(tabelle.getItems());
		if (paketeValidieren(pakete)) {
			speichern(pakete, event);
		} else {
			validierungFehlgeschlagen();
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
		alleFenster.add("/reslearn/gui/fxml/AufgabeErstellen.fxml");
		Parent root;
		try {
			FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/reslearn/gui/fxml/ModusAuswaehlen.fxml"));
			root = fxmlLoader.load();
			newScene = new Scene(root);
			ControllerModusAuswaehlen controller = fxmlLoader.<ControllerModusAuswaehlen>getController();
			controller.initialize(pakete);
			ControllerUebungsmodus.letztesArbeitspaket = pakete;
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
	private void handleButtonAnzPaketeMinusAction(ActionEvent event) {
		// Anzahl der Pakete soll nicht unter 2 sein
		if (anzPakete > 2) {
			anzPakete--;
			textFieldAnzPakete.setText(Integer.toString(anzPakete));

			// letzte Zeile entfernen
			tabelle.getItems().remove(anzPakete);
		}
	}

	@FXML
	private void handleButtonAnzPaketePlusAction(ActionEvent event) {
		if (anzPakete < 26) {
			anzPakete++;
			textFieldAnzPakete.setText(Integer.toString(anzPakete));

			// neue Zeile hinzufügen
			tabelle.getItems().add(new ArbeitspaketTableData(setIdBuchstabe(anzPakete), 0, 0, 0, 0, 0, 0, 0));
		}
	}

	private String setIdBuchstabe(int i) {
		String buchstaben[] = { "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q",
				"R", "S", "T", "U", "V", "W", "X", "Y", "Z" };
		return buchstaben[i - 1];
	}

	private void getArbeitspaketArray(ObservableList<ArbeitspaketTableData> paketList) {
		pakete = new Arbeitspaket[paketList.size()];

		for (int i = 0; i < paketList.size(); i++) {
			int vorgangsdauer = paketList.get(i).getSez() - paketList.get(i).getFez() + 1;

			pakete[i] = new Arbeitspaket(paketList.get(i).getId(), paketList.get(i).getFaz(), paketList.get(i).getFez(),
					paketList.get(i).getSaz(), paketList.get(i).getSez(), vorgangsdauer,
					paketList.get(i).getMitarbeiteranzahl(), paketList.get(i).getAufwand());
		}
	}

	// Tabelle mit Default-Werten befüllen
	private List<Arbeitspaket> retrieveData() {

		// return Arrays.asList(new Arbeitspaket("A", 1, 2, 1, 2, 2, 1, 2), new
		// Arbeitspaket("B", 3, 3, 3, 3, 1, 3, 3),
		// new Arbeitspaket("C", 4, 5, 4, 5, 2, 2, 4), new Arbeitspaket("D", 4, 4, 4, 4,
		// 1, 2, 2));

		return Arrays.asList(new Arbeitspaket("A", 0, 0, 0, 0, 0, 0, 0), new Arbeitspaket("B", 0, 0, 0, 0, 0, 0, 0),
				new Arbeitspaket("C", 0, 0, 0, 0, 0, 0, 0), new Arbeitspaket("D", 0, 0, 0, 0, 0, 0, 0));
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

	private void setupLayout() {
		double fensterBreite = DisplayCanvas.faktor * 1920;
		double tabelleBreite = fensterBreite / 2;
		double layoutX = (fensterBreite - tabelleBreite) / 2;

		// Breite der Tabelle setzen
		tabelle.setPrefWidth(tabelleBreite);

		// Position der Tabelle setzen (Tabelle -> BorderPane -> StackPane)
		tabelle.parentProperty().get().parentProperty().get().setLayoutX(layoutX);
	}

	private void setupTabelle() {
		// den Spalten die richtigen Attribute zuteilen und bearbeitbar machen
		setupSpalteID();
		setupSpalteFaz();
		setupSpalteSaz();
		setupSpalteFez();
		setupSpalteSez();
		setupSpalteAnzMitarbeiter();
		setupSpalteAufwand();
		setTableEditable();

		// Tabelle mit Default-Daten füllen
		tabelle.setItems(data);
		populate(retrieveData());

		// Schriftgröße
		tabelle.setStyle("-fx-font:" + DisplayCanvas.schriftGroesse + " Arial;");

		// Spalten-Breite automatisch anpassen
		tabelle.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
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
		int faz, saz, fez, sez, ma, groessteMA;
		groessteMA = 0;
		// String[] id = new String[arbeitspaket.length];
		String id;

		for (int i = 0; i < arbeitspaket.length; i++) {
			// id[i] = arbeitspaket[i].getId();
			id = arbeitspaket[i].getId();
			faz = arbeitspaket[i].getFaz();
			saz = arbeitspaket[i].getSaz();
			fez = arbeitspaket[i].getFez();
			sez = arbeitspaket[i].getSez();
			ma = arbeitspaket[i].getMitarbeiteranzahl();

			// ID nicht größer als 3 Zeichen
			if (id.length() > 3) {
				ergebnisValidierung = "Die ID des Arbeitspakets " + arbeitspaket[i].getId()
						+ " darf nicht mehr als 3 Zeichen enthalten.";
				tabelle.getSelectionModel().clearAndSelect(i, spalteID);
				return false;
			}

			// ID darf nur einmal vorkommen
			// for (int j = i; j > 0; j--) {
			// if (id[i].equals(id[j - 1])) {
			// ergebnisValidierung = "Die ID " + arbeitspaket[i].getId()
			// + " darf nur einmal vergeben werden.";
			// tabelle.getSelectionModel().clearAndSelect(i, spalteID);
			// return false;
			// }
			// }
			for (int j = i; j > 0; j--) {
				if (id.equals(arbeitspaket[j - 1].getId())) {
					ergebnisValidierung = "Die ID " + arbeitspaket[i].getId() + " darf nur einmal vergeben werden.";
					tabelle.getSelectionModel().clearAndSelect(i, spalteID);
					return false;
				}
			}

			// FAZ prüfen
			if (faz < 1) {
				ergebnisValidierung = "Der Wert FAZ für das Arbeitspaket " + arbeitspaket[i].getId()
						+ " muss mindestens 1 sein.";
				tabelle.getSelectionModel().clearAndSelect(i, spalteFaz);
				return false;
			}

			// SAZ prüfen
			if (saz < faz) {
				ergebnisValidierung = "Der Wert SAZ für das Arbeitspaket " + arbeitspaket[i].getId()
						+ " muss mindestens gleich groß wie der Wert FAZ sein.";
				tabelle.getSelectionModel().clearAndSelect(i, spalteSaz);
				return false;
			}

			// FEZ prüfen
			if (fez < faz) {
				ergebnisValidierung = "Der Wert FEZ für das Arbeitspaket " + arbeitspaket[i].getId()
						+ " muss mindestens gleich groß wie der Wert FAZ sein.";
				tabelle.getSelectionModel().clearAndSelect(i, spalteFez);
				return false;
			}

			// SEZ prüfen
			if (sez < fez) {
				ergebnisValidierung = "Der Wert SEZ für das Arbeitspaket " + arbeitspaket[i].getId()
						+ " muss mindestens gleich groß wie der Wert FEZ sein.";
				tabelle.getSelectionModel().clearAndSelect(i, spalteSez);
				return false;
			}

			// Differenz zwischen FEZ-FAZ und SEZ-SAZ gleich groß?
			if ((fez - faz) != (sez - saz)) {
				ergebnisValidierung = "Die Differenzen zwischen FEZ und FAZ sowie zwichen SEZ und SAZ für das Arbeitspaket "
						+ arbeitspaket[i].getId() + " müssen gleich groß sein.";
				tabelle.getSelectionModel().clearAndSelect(i, spalteSez);
				return false;
			}

			// Mitarbeiterzahl prüfen
			if (ma < 1) {
				ergebnisValidierung = "Der Wert Mitarbeiteranzahl für das Arbeitspaket " + arbeitspaket[i].getId()
						+ " muss größer 0 sein.";
				tabelle.getSelectionModel().clearAndSelect(i, spalteAnzMitarbeiter);
				return false;
			}

			// // Bei kapazitätstreuer Optimierung darf die Anzahl an Mitarbeiter des
			// Arbeitspakets nicht größer als die Obergrenze sein
			// if (radioButtonKapazitaet.isSelected() && (ma > anzMaxPersonen)) {
			// ergebnisValidierung = "Der Wert Mitarbeiteranzahl für das Arbeitspaket " +
			// arbeitspaket[i].getId()
			// + " darf nicht größer als die angegebene Kapazitätsgrenze sein.";
			// tabelle.getSelectionModel().clearAndSelect(i, spalteAnzMitarbeiter);
			// return false;
			// }

			// Größte Anzahl an Mitarbieter ermitteln
			if (ma > groessteMA) {
				groessteMA = ma;
			}

		}
		anzMaxPersonen = groessteMA + 2;
		ergebnisValidierung = "Validierung erfolgreich!";
		return true;
	}

	private void validierungFehlgeschlagen() {
		Alert alert = new Alert(AlertType.WARNING);
		alert.setTitle("Ergebnis der Validierung");
		alert.setHeaderText("Die Validierung ist fehlgeschlagen! Bitte korrigieren Sie den Fehler.");
		alert.setContentText(ergebnisValidierung);

		alert.showAndWait();
	}

	public void export(Arbeitspaket[] arbeitspakete, ActionEvent event) {
		String outputFile = dateipfad + dateiname.getText() + ".csv";
		boolean alreadyExists = new File(outputFile).exists();
		String spalten[] = new String[9];

		spalten[0] = "Id";
		spalten[1] = "FAZ";
		spalten[2] = "FEZ";
		spalten[3] = "SAZ";
		spalten[4] = "SEZ";
		spalten[5] = "Vorgangsdauer";
		spalten[6] = "Mitarbeiteranzahl";
		spalten[7] = "Aufwand";
		spalten[8] = "MaxPersonenParallel";

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

			for (Arbeitspaket ap : arbeitspakete) {
				int vorgangsdauer = ap.getSez() - ap.getFez() + 1;
				csvOutput.write(ap.getId().toString());
				csvOutput.write(String.valueOf(ap.getFaz()));
				csvOutput.write(String.valueOf(ap.getFez()));
				csvOutput.write(String.valueOf(ap.getSaz()));
				csvOutput.write(String.valueOf(ap.getSez()));
				csvOutput.write(String.valueOf(vorgangsdauer));
				csvOutput.write(String.valueOf(ap.getMitarbeiteranzahl()));
				csvOutput.write(String.valueOf(ap.getAufwand()));
				csvOutput.write(String.valueOf(anzMaxPersonen));
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
			AufgabeLadenImport.maxPersonenParallel = anzMaxPersonen;
		} else {
			dialog.close();
		}
	}

}
