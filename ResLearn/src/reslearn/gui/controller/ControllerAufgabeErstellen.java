package reslearn.gui.controller;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TablePosition;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import reslearn.gui.tableedit.ArbeitspaketTableData;
import reslearn.gui.tableedit.EditCell;
import reslearn.gui.tableedit.MyIntegerStringConverter;
import reslearn.model.paket.Arbeitspaket;

public class ControllerAufgabeErstellen extends Controller {
	@FXML
	private Button zurueck;
	@FXML
	private Button home;

	private int anzPakete, anzMaxPersonen;
	final ToggleGroup rbGruppe = new ToggleGroup();
	private String ergebnisValidierung = "";
	private ObservableList<ArbeitspaketTableData> data = FXCollections.observableArrayList();

	@FXML
	private void handleButtonValidierenAction(ActionEvent event) {
		paneErgebnis.setVisible(true);
		Arbeitspaket pakete[] = getArbeitspaketArray(getArbeitspaket());
		if (paketeValidieren(pakete)) {
			labelErgebnis.setText("Validierung erfolgreich, die Aufgabe wurde gespeichert.");

			weiter(event);
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
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		((Node) (event.getSource())).getScene().getWindow().hide();
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
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		((Node) (event.getSource())).getScene().getWindow().hide();
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
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		((Node) (event.getSource())).getScene().getWindow().hide();
	}

	public void initialize() {
		anzPakete = Integer.parseInt(textFieldAnzPakete.getText());
		anzMaxPersonen = Integer.parseInt(textFieldMaxPersonen.getText());

		// den Spalten die richtigen Attribute zuteilen
		tabelle.setItems(data);
		populate(retrieveData());
		setupSpalteID();
		setupSpalteFaz();
		setupSpalteSaz();
		setupSpalteFez();
		setupSpalteSez();
		setupSpalteAnzMitarbeiter();
		setupSpalteAufwand();

		// tabelle.setItems(getArbeitspaket());
		setTableEditable();

		radioButtonKapazitaet.setToggleGroup(rbGruppe);
		radioButtonKapazitaet.setSelected(true);
		radioButtonTermin.setToggleGroup(rbGruppe);
	}

	// Zurück-Button ins Hauptmenü
	@FXML
	ImageView imagePfeil = new ImageView();

	@FXML
	private void handleImagePfeilAction(ActionEvent event) throws IOException {
		Parent root = FXMLLoader.load(getClass().getResource("Hauptmenue.fxml"));
		Scene newScene = new Scene(root);
		Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
		window.setScene(newScene);
		window.show();
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
		anzPakete++;
		textFieldAnzPakete.setText(Integer.toString(anzPakete));

		// neue Zeile hinzufügen
		tabelle.getItems().add(new ArbeitspaketTableData(Integer.toString(anzPakete), 0, 0, 0, 0, 0, 0, 0));
	}

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

	// Tabelle mit Default-Werten befüllen
	private List<Arbeitspaket> retrieveData() {

		return Arrays.asList(new Arbeitspaket("1", 0, 0, 0, 0, 0, 0, 0), new Arbeitspaket("2", 0, 0, 0, 0, 0, 0, 0),
				new Arbeitspaket("3", 0, 0, 0, 0, 0, 0, 0), new Arbeitspaket("4", 0, 0, 0, 0, 0, 0, 0));
	}

	private void populate(final List<Arbeitspaket> pakete) {
		pakete.forEach(p -> data.add(new ArbeitspaketTableData(p)));
	}

	public ObservableList<ArbeitspaketTableData> getArbeitspaket() {
		ObservableList<ArbeitspaketTableData> pakete = FXCollections.observableArrayList();

		// pakete.add(new ArbeitspaketTableData("1", 0, 0, 0, 0, 0, 0, 0));
		// pakete.add(new ArbeitspaketTableData("2", 0, 0, 0, 0, 0, 0, 0));
		// pakete.add(new ArbeitspaketTableData("3", 0, 0, 0, 0, 0, 0, 0));
		// pakete.add(new ArbeitspaketTableData("4", 0, 0, 0, 0, 0, 0, 0));
		return pakete;
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
			((ArbeitspaketTableData) event.getTableView().getItems().get(event.getTablePosition().getRow()))
					.setId(value);
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
			((ArbeitspaketTableData) event.getTableView().getItems().get(event.getTablePosition().getRow()))
					.setFaz(value);
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
			((ArbeitspaketTableData) event.getTableView().getItems().get(event.getTablePosition().getRow()))
					.setSaz(value);
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
			((ArbeitspaketTableData) event.getTableView().getItems().get(event.getTablePosition().getRow()))
					.setFez(value);
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
			((ArbeitspaketTableData) event.getTableView().getItems().get(event.getTablePosition().getRow()))
					.setSez(value);
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
			((ArbeitspaketTableData) event.getTableView().getItems().get(event.getTablePosition().getRow()))
					.setMitarbeiteranzahl(value);
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
			((ArbeitspaketTableData) event.getTableView().getItems().get(event.getTablePosition().getRow()))
					.setAufwand(value);
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
				// } else if (event.getCode() == KeyCode.RIGHT || event.getCode() ==
				// KeyCode.PLUS ||
				// event.getCode() == KeyCode.TAB) {
				// tabelle.getSelectionModel().selectNext();
				// event.consume();
				// } else if (event.getCode() == KeyCode.LEFT || event.getCode() ==
				// KeyCode.MINUS) {
				// // work around due to
				// // TableView.getSelectionModel().selectPrevious() due to a bug
				// // stopping it from working on
				// // the first column in the last row of the table
				// selectPrevious();
				// event.consume();
			}
		});
	}

	@SuppressWarnings("unchecked")
	private void editFocusedCell() {
		final TablePosition<ArbeitspaketTableData, ?> focusedCell = tabelle.focusModelProperty().get()
				.focusedCellProperty().get();
		tabelle.edit(focusedCell.getRow(), focusedCell.getTableColumn());
	}

	// @SuppressWarnings("unchecked")
	// private void selectPrevious() {
	// if (tabelle.getSelectionModel().isCellSelectionEnabled()) {
	// // in cell selection mode, we have to wrap around, going from
	// // right-to-left, and then wrapping to the end of the previous line
	// TablePosition<ArbeitspaketTableData, ?> pos =
	// tabelle.getFocusModel().getFocusedCell();
	// if (pos.getColumn() - 1 >= 0) {
	// // go to previous row
	// tabelle.getSelectionModel().select(pos.getRow(),
	// getTableColumn(pos.getTableColumn(), -1));
	// } else if (pos.getRow() < tabelle.getItems().size()) {
	// // wrap to end of previous row
	// tabelle.getSelectionModel().select(pos.getRow() - 1,
	// tabelle.getVisibleLeafColumn(tabelle.getVisibleLeafColumns().size() - 1));
	// }
	// } else {
	// int focusIndex = tabelle.getFocusModel().getFocusedIndex();
	// if (focusIndex == -1) {
	// tabelle.getSelectionModel().select(tabelle.getItems().size() - 1);
	// } else if (focusIndex > 0) {
	// tabelle.getSelectionModel().select(focusIndex - 1);
	// }
	// }
	// }

	private TableColumn<ArbeitspaketTableData, ?> getTableColumn(final TableColumn<ArbeitspaketTableData, ?> column,
			int offset) {
		int columnIndex = tabelle.getVisibleLeafIndex(column);
		int newColumnIndex = columnIndex + offset;
		return tabelle.getVisibleLeafColumn(newColumnIndex);
	}

	// ObservableList zu Array konvertieren
	public static Arbeitspaket[] getArbeitspaketArray(ObservableList<ArbeitspaketTableData> pakete) {
		Arbeitspaket arbeitspakete[] = pakete.get(0).getArbeitspakete();
		return arbeitspakete;
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
		paneErgebnis.setVisible(true);
		Arbeitspaket pakete[] = getArbeitspaketArray(getArbeitspaket());
		if (paketeValidieren(pakete)) {
			labelErgebnis.setText("Validierung erfolgreich, die Aufgabe wurde gespeichert.");
		} else {
			labelErgebnis.setText(ergebnisValidierung);
		}
	}

	// Ergebnis Validierung anzeigen
	@FXML
	Pane paneErgebnis = new Pane();
	@FXML
	Label labelErgebnis = new Label();

	public boolean paketeValidieren(Arbeitspaket[] arbeitspaket) {
		boolean idKorrekt, fazKorrekt, sazKorrekt, fezKorrekt, sezKorrekt, paketKorrekt = false;
		String id;
		int faz, saz, fez, sez;

		for (int i = 0; i < arbeitspaket.length; i++) {
			id = arbeitspaket[i].getId();
			faz = arbeitspaket[i].getFaz();
			saz = arbeitspaket[i].getSaz();
			fez = arbeitspaket[i].getFez();
			sez = arbeitspaket[i].getSez();

			// ID prüfen (einzigartig?)

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
						+ " muss mindestens gleich groß wie der Wert FAZ sein";
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
			if (fazKorrekt == true && sazKorrekt == true && fezKorrekt == true && sezKorrekt == true) {
				paketKorrekt = true;
			}
		}

		return paketKorrekt;
	}

}
