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
import javafx.scene.control.TableColumn;
import javafx.scene.control.TablePosition;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import reslearn.gui.ImportExport.AufgabeLadenImport;
import reslearn.gui.ImportExport.CsvWriter;
import reslearn.gui.rescanvas.DisplayCanvas;
import reslearn.gui.tableUtils.ArbeitspaketTableData;
import reslearn.gui.tableUtils.EditCell;
import reslearn.gui.tableUtils.MyIntegerStringConverter;
import reslearn.model.paket.Arbeitspaket;

public class ControllerAufgabeErstellen extends Controller {

	/**
	 * Die Anzahl an Paketen, die die Aufgabe enthalten soll
	 */
	private static int anzPakete;

	/**
	 * Die Kapazit�tsgrenze, die beim kapazit�tstreuen nicht �berschritten werden
	 * darf (im �bungsmodus �nderbar)
	 */
	private static int anzMaxPersonen;

	final ToggleGroup rbGruppe = new ToggleGroup();

	/**
	 * Dieser String wird beim Validieren auf die entsprechende Fehlermeldung
	 * gesetzt
	 */
	private String ergebnisValidierung = "";

	/**
	 * In dieser ObservableList werden die Inhalte der Tabelle gespeichert
	 */
	private ObservableList<ArbeitspaketTableData> data = FXCollections.observableArrayList();

	/**
	 * Button, der ins vorherige Fenster zur�ckf�hrt
	 */
	@FXML
	private Button zurueck;

	/**
	 * Button, der zum Hauptmen� zur�ckf�hrt
	 */
	@FXML
	private Button home;

	// Anzahl Pakete

	/**
	 * Button mit einem "-", der die Anzahl der Pakete um 1 senkt
	 */
	@FXML
	Button buttonAnzPaketeMinus = new Button();

	/**
	 * Button mit einem "+", der die Anzahl der Pakete um 1 erh�ht
	 */
	@FXML
	Button buttonAnzPaketePlus = new Button();

	/**
	 * Textfeld, das die aktuell angegebene Anzahl an Paketen anzeigt (nur durch
	 * Buttons �nderbar)
	 */
	@FXML
	TextField textFieldAnzPakete = new TextField();

	// Tabelle
	/**
	 * Tabelle, die aus den Spalten und dem Inhalt besteht
	 */
	@FXML
	TableView<ArbeitspaketTableData> tabelle;

	/**
	 * Spalte zur Repr�sentation der (externen) ID
	 */
	@FXML
	TableColumn<ArbeitspaketTableData, String> spalteID;

	/**
	 * Spalte zur Repr�sentation des fr�hesten Anfangszeitpunktes (FAZ) eines
	 * Paketes
	 */
	@FXML
	TableColumn<ArbeitspaketTableData, Integer> spalteFaz;

	/**
	 * Spalte zur Repr�sentation des sp�testen Anfangszeitpunktes (SAZ) eines
	 * Paketes
	 */
	@FXML
	TableColumn<ArbeitspaketTableData, Integer> spalteSaz;

	/**
	 * Spalte zur Repr�sentation des fr�hesten Endzeitpunktes (FEZ) eines Paketes
	 */
	@FXML
	TableColumn<ArbeitspaketTableData, Integer> spalteFez;

	/**
	 * Spalte zur Repr�sentation des sp�testen Endzeitpunkt (SEZ) eines Paketes
	 */
	@FXML
	TableColumn<ArbeitspaketTableData, Integer> spalteSez;

	/**
	 * Spalte zur Repr�sentation der Anzahl der maximalen Anzahl an Mitarbeiter
	 * eines Paketes
	 */
	@FXML
	TableColumn<ArbeitspaketTableData, Integer> spalteAnzMitarbeiter;

	/**
	 * Spalte zur Repr�sentation des Aufwand in Personen-Tage (PT) eines Paketes
	 */
	@FXML
	TableColumn<ArbeitspaketTableData, Integer> spalteAufwand;

	// Button zur Validierung
	/**
	 * Button zur Validierung
	 */
	@FXML
	Button buttonValidieren;

	/**
	 * Name der Datei, unter der die Aufgabe gespeichert werden soll
	 */
	TextField dateiname;

	/**
	 * Pfad, unter dem die angelegte Aufgabe gespeichert wird
	 */
	String dateipfad = "./eigeneAufgaben/";

	/**
	 * Array aus Arbeitspaketen, in dem die erstellten Arbeitspakete nach
	 * erfolgreicher Validierung liegen
	 */
	public Arbeitspaket[] pakete;

	public void initialize() {
		// liest aus dem Textfeld die voreingestellte Zahl aus
		anzPakete = Integer.parseInt(textFieldAnzPakete.getText());

		// Tabelle erzeugen
		setupTabelle();
	}

	/**
	 * Ueberprueft beim Klick auf den Validieren-Button, ob die eingegebnen
	 * Arbeitspakete des Useres korrekt sind. Sind sie korrekt, �ffnet sich ein
	 * Pop-Up-Fenster zum Speichern der Aufgabe. Sind sie fehlerhaft, �ffnet sich
	 * ein Pop-Up-Fenster mit entsprechender Fehlermeldung und der fehlerhafte Wert
	 * wird in der Tabelle markiert
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

	/**
	 * Der Aufruf dieser Methode fuehrt zurueck zum Hauptmenue
	 *
	 * @param event
	 * @throws Exception
	 */
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

	/**
	 * Der Aufruf dieser Methode fuehrt zurueck zum vorherigen Fenster
	 *
	 * @param event
	 * @throws Exception
	 */
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

	/**
	 * Nach erfolgreicher Validierung �ffnet sich beim Klick auf Weiter das Fenster
	 * zur Auswahl des Modus, welchen man mit der erstellen Aufgabe durchf�hren
	 * m�chte
	 */
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
			AufgabeLadenImport.maxPersonenParallel = anzMaxPersonen;
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

	/**
	 * Dr�ckt man bei der Anzahl der Pakete auf den Minus-Button, wird diese Anzahl
	 * um 1 gesenkt und die letzte Zeile in der Tabelle entfernt
	 */
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

	/**
	 * Dr�ckt man bei der Anzahl der Pakete auf den Plus-Button, wird diese Anzahl
	 * um 1 erh�ht und eine neue Zeile hinzugef�gt
	 *
	 * @param event
	 */
	@FXML
	private void handleButtonAnzPaketePlusAction(ActionEvent event) {
		// Anzahl der Pakete soll nicht �ber 26 sein
		if (anzPakete < 26) {
			anzPakete++;
			textFieldAnzPakete.setText(Integer.toString(anzPakete));

			// neue Zeile hinzuf�gen
			tabelle.getItems().add(new ArbeitspaketTableData(setIdBuchstabe(anzPakete), 0, 0, 0, 0, 0, 0, 0));
		}
	}

	/**
	 * Die ID soll alphabetisch fortlaufend sein. Die interne ID beh�lt diesen
	 * Buchstaben, die angezeigte externe ID kann hingegen durch den User ge�ndert
	 * werden
	 *
	 * @param i
	 * @return
	 */
	private String setIdBuchstabe(int i) {
		String buchstaben[] = { "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q",
				"R", "S", "T", "U", "V", "W", "X", "Y", "Z" };
		return buchstaben[i - 1];
	}

	/**
	 * Diese Methode liest aus der Tabelle die Werte und legt diese in ein Array aus
	 * Arbeitspakten an
	 *
	 * @param paketList
	 */
	private void getArbeitspaketArray(ObservableList<ArbeitspaketTableData> paketList) {
		// Initialisieren des Arrays
		pakete = new Arbeitspaket[paketList.size()];

		for (int i = 0; i < paketList.size(); i++) {
			// herauslesen der externen ID, damit diese nicht verloren geht (Konstruktor
			// nimmt nur einen Parameter f�r die ID entgegen und setzt die interne und
			// externe ID auf diesen Wert)
			String idExtern = paketList.get(i).getIdExtern();

			// automatisches Setzen der Vorgangsdauer, da diese in der Tabelle nicht
			// angegeben wird
			int vorgangsdauer = paketList.get(i).getFez() - paketList.get(i).getFaz() + 1;

			// setzen der einzelnen Werte der Arbeitspakte
			pakete[i] = new Arbeitspaket(paketList.get(i).getIdIntern(), paketList.get(i).getFaz(),
					paketList.get(i).getFez(), paketList.get(i).getSaz(), paketList.get(i).getSez(), vorgangsdauer,
					paketList.get(i).getMitarbeiteranzahl(), paketList.get(i).getAufwand());

			// Da die externe ID auf die interne ID gesetzt wurde, wird diese nun wieder auf
			// die vorher herausgelesene externe ID gesetzt
			pakete[i].setIdExtern(idExtern);
		}
	}

	/**
	 * Diese Methode legt die Default-Werte der Tabelle fest
	 *
	 * @return
	 */
	private List<Arbeitspaket> retrieveData() {
		return Arrays.asList(new Arbeitspaket("A", 0, 0, 0, 0, 0, 0, 0), new Arbeitspaket("B", 0, 0, 0, 0, 0, 0, 0),
				new Arbeitspaket("C", 0, 0, 0, 0, 0, 0, 0), new Arbeitspaket("D", 0, 0, 0, 0, 0, 0, 0));
	}

	/**
	 * Diese Methode bef�llt die Tabelle mit der im Parameter �bergebenen
	 * Arbeitspaket-Liste
	 *
	 * @param pakete
	 */
	private void populate(final List<Arbeitspaket> pakete) {
		pakete.forEach(p -> data.add(new ArbeitspaketTableData(p)));
	}

	/**
	 * Diese Methode legt die Einstellungen der Spalte ID fest
	 */
	private void setupSpalteID() {
		// legt fest, welches Attribut von Arbeitspaket in dieser Spalte angezeigt wird
		spalteID.setCellValueFactory(new PropertyValueFactory<>("idExtern"));

		// l�sst die Zelle mit Hilfe der Klasse EditCell bei Tastatureingabe bearbeitbar
		// machen
		spalteID.setCellFactory(EditCell.<ArbeitspaketTableData>forTableColumn());

		// �berschreibt den alten Attributwert mit der User-Eingabe
		spalteID.setOnEditCommit(event -> {
			final String value = event.getNewValue() != null ? event.getNewValue() : event.getOldValue();
			event.getTableView().getItems().get(event.getTablePosition().getRow()).setIdExtern(value);
			tabelle.refresh();
		});
	}

	/**
	 * Diese Methode legt die Einstellungen der Spalte FAZ fest
	 */
	private void setupSpalteFaz() {
		// legt fest, welches Attribut von Arbeitspaket in dieser Spalte angezeigt wird
		spalteFaz.setCellValueFactory(new PropertyValueFactory<>("faz"));

		// l�sst die Zelle mit Hilfe der Klasse EditCell bei Tastatureingabe bearbeitbar
		// machen
		spalteFaz.setCellFactory(
				EditCell.<ArbeitspaketTableData, Integer>forTableColumn(new MyIntegerStringConverter()));

		// �berschreibt den alten Attributwert mit der User-Eingabe
		spalteFaz.setOnEditCommit(event -> {
			final Integer value = event.getNewValue() != null ? event.getNewValue() : event.getOldValue();
			event.getTableView().getItems().get(event.getTablePosition().getRow()).setFaz(value);
			tabelle.refresh();
		});
	}

	/**
	 * Diese Methode legt die Einstellungen der Spalte SAZ fest
	 */
	private void setupSpalteSaz() {
		// legt fest, welches Attribut von Arbeitspaket in dieser Spalte angezeigt wird
		spalteSaz.setCellValueFactory(new PropertyValueFactory<>("saz"));

		// l�sst die Zelle mit Hilfe der Klasse EditCell bei Tastatureingabe bearbeitbar
		// machen
		spalteSaz.setCellFactory(
				EditCell.<ArbeitspaketTableData, Integer>forTableColumn(new MyIntegerStringConverter()));

		// �berschreibt den alten Attributwert mit der User-Eingabe
		spalteSaz.setOnEditCommit(event -> {
			final Integer value = event.getNewValue() != null ? event.getNewValue() : event.getOldValue();
			event.getTableView().getItems().get(event.getTablePosition().getRow()).setSaz(value);
			tabelle.refresh();
		});
	}

	/**
	 * Diese Methode legt die Einstellungen der Spalte FEZ fest
	 */
	private void setupSpalteFez() {
		// legt fest, welches Attribut von Arbeitspaket in dieser Spalte angezeigt wird
		spalteFez.setCellValueFactory(new PropertyValueFactory<>("fez"));

		// l�sst die Zelle mit Hilfe der Klasse EditCell bei Tastatureingabe bearbeitbar
		// machen
		spalteFez.setCellFactory(
				EditCell.<ArbeitspaketTableData, Integer>forTableColumn(new MyIntegerStringConverter()));

		// �berschreibt den alten Attributwert mit der User-Eingabe
		spalteFez.setOnEditCommit(event -> {
			final Integer value = event.getNewValue() != null ? event.getNewValue() : event.getOldValue();
			event.getTableView().getItems().get(event.getTablePosition().getRow()).setFez(value);
			tabelle.refresh();
		});
	}

	/**
	 * Diese Methode legt die Einstellungen der Spalte SEZ fest
	 */
	private void setupSpalteSez() {
		// legt fest, welches Attribut von Arbeitspaket in dieser Spalte angezeigt wird
		spalteSez.setCellValueFactory(new PropertyValueFactory<>("sez"));

		// l�sst die Zelle mit Hilfe der Klasse EditCell bei Tastatureingabe bearbeitbar
		// machen
		spalteSez.setCellFactory(
				EditCell.<ArbeitspaketTableData, Integer>forTableColumn(new MyIntegerStringConverter()));

		// �berschreibt den alten Attributwert mit der User-Eingabe
		spalteSez.setOnEditCommit(event -> {
			final Integer value = event.getNewValue() != null ? event.getNewValue() : event.getOldValue();
			event.getTableView().getItems().get(event.getTablePosition().getRow()).setSez(value);
			tabelle.refresh();
		});
	}

	/**
	 * Diese Methode legt die Einstellungen der Spalte Mitarbeiteranzahl fest
	 */
	private void setupSpalteAnzMitarbeiter() {
		// legt fest, welches Attribut von Arbeitspaket in dieser Spalte angezeigt wird
		spalteAnzMitarbeiter.setCellValueFactory(new PropertyValueFactory<>("mitarbeiteranzahl"));

		// l�sst die Zelle mit Hilfe der Klasse EditCell bei Tastatureingabe bearbeitbar
		// machen
		spalteAnzMitarbeiter.setCellFactory(
				EditCell.<ArbeitspaketTableData, Integer>forTableColumn(new MyIntegerStringConverter()));

		// �berschreibt den alten Attributwert mit der User-Eingabe
		spalteAnzMitarbeiter.setOnEditCommit(event -> {
			final Integer value = event.getNewValue() != null ? event.getNewValue() : event.getOldValue();
			event.getTableView().getItems().get(event.getTablePosition().getRow()).setMitarbeiteranzahl(value);
			tabelle.refresh();
		});
	}

	/**
	 * Diese Methode legt die Einstellungen der Spalte Aufwand fest
	 */
	private void setupSpalteAufwand() {
		// legt fest, welches Attribut von Arbeitspaket in dieser Spalte angezeigt wird
		spalteAufwand.setCellValueFactory(new PropertyValueFactory<>("aufwand"));

		// l�sst die Zelle mit Hilfe der Klasse EditCell bei Tastatureingabe bearbeitbar
		// machen
		spalteAufwand.setCellFactory(
				EditCell.<ArbeitspaketTableData, Integer>forTableColumn(new MyIntegerStringConverter()));

		// �berschreibt den alten Attributwert mit der User-Eingabe
		spalteAufwand.setOnEditCommit(event -> {
			final Integer value = event.getNewValue() != null ? event.getNewValue() : event.getOldValue();
			event.getTableView().getItems().get(event.getTablePosition().getRow()).setAufwand(value);
			tabelle.refresh();
		});
	}

	/**
	 * Erlaubt es einzelne Zell-Inhalte der Tabelle zu �ndern
	 */
	private void setTableEditable() {
		// Tabelle bearbeitbar machen
		tabelle.setEditable(true);

		// erlaubt es einzelne Zellen auszuw�hlen
		tabelle.getSelectionModel().cellSelectionEnabledProperty().set(true);

		// bei Tastatur-Eingabe wird erlaubt, in die Zelle zu schreiben
		tabelle.setOnKeyPressed(event -> {
			if (event.getCode().isLetterKey() || event.getCode().isDigitKey()) {
				editFocusedCell();
			}
		});
	}

	/**
	 * Erlaubt dem User, Werte in eine Zelle zu schreiben
	 */
	@SuppressWarnings("unchecked")
	private void editFocusedCell() {
		final TablePosition<ArbeitspaketTableData, ?> focusedCell = tabelle.focusModelProperty().get()
				.focusedCellProperty().get();
		tabelle.edit(focusedCell.getRow(), focusedCell.getTableColumn());
	}

	// public static Arbeitspaket[] getArbeitspaketArray(List<Arbeitspaket> pakete)
	// {
	// Arbeitspaket[] arbeitspakete = new Arbeitspaket[anzPakete];
	// for (int i = 0; i < anzPakete; i++) {
	// arbeitspakete[i] = pakete.get(i);
	// }
	// return arbeitspakete;
	// }

	/**
	 * Setzt die Breite, Position, Schriftgr��e und Spaltenbreiten der Tabelle
	 */
	private void setupLayout() {
		// Herauslesen der Fensterbreite
		double fensterBreite = DisplayCanvas.faktor * 1920;

		// berechnet die Breite der Tabelle auf die halbe Fensterbreite
		double tabelleBreite = fensterBreite / 2;

		// berechnet die Position der Tabelle so, dass sie zentriert dargestellt wird
		double layoutX = (fensterBreite - tabelleBreite) / 2;

		// Breite der Tabelle setzen
		tabelle.setPrefWidth(tabelleBreite);

		// Position der Tabelle setzen (Tabelle -> BorderPane -> StackPane)
		tabelle.parentProperty().get().parentProperty().get().setLayoutX(layoutX);

		// Schriftgr��e
		tabelle.setStyle("-fx-font:" + DisplayCanvas.schriftGroesse + " Arial;");

		// Spalten-Breite automatisch anpassen
		tabelle.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
	}

	/**
	 * F�hrt alle Methoden aus, die ben�tigt werden, um die fertige Tabelle zu
	 * erzeugen
	 */
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

		// Tabelle mit Default-Daten f�llen
		tabelle.setItems(data);
		populate(retrieveData());

		// Layout der Tabelle festlegen
		setupLayout();
	}

	/**
	 * Algorithmus, der �berpr�ft, ob die angegebenen Werte korrekt sind
	 *
	 * @param arbeitspaket
	 * @return
	 */
	private boolean paketeValidieren(Arbeitspaket[] arbeitspaket) {
		// tempor�re Hilfsvariablen
		int faz, saz, fez, sez, ma, aufwand, groessteMA;
		String id;

		// in dieser Variable wird die gr��e Anzahl an Mitarbeiter eines Arbeitspakets
		// gespeichert, um einen Fehler beim kapazit�tstreuen Algorithmus zu verhindern
		groessteMA = 0;

		// Es wird jedes einzelen Arbeitspaket gepr�ft, ob die Werte korrekt sind
		for (int i = 0; i < arbeitspaket.length; i++) {

			// �berschreiben der tempor�ren Variablen auf das aktuelle Arbeitspaket
			id = arbeitspaket[i].getIdExtern();
			faz = arbeitspaket[i].getFaz();
			saz = arbeitspaket[i].getSaz();
			fez = arbeitspaket[i].getFez();
			sez = arbeitspaket[i].getSez();
			ma = arbeitspaket[i].getMitarbeiteranzahl();
			aufwand = arbeitspaket[i].getAufwand();

			// Externe ID darf aus Visualisierungsgr�nden nicht l�nger als 3 Zeichen
			if (id.length() > 3) {

				// Fehlermeldung setzen
				ergebnisValidierung = "Die ID des Arbeitspakets " + arbeitspaket[i].getIdExtern()
						+ " darf nicht mehr als 3 Zeichen enthalten.";

				// Fehlerhafte Zelle markieren
				tabelle.getSelectionModel().clearAndSelect(i, spalteID);

				return false;
			}

			// Externe ID darf nur einmal vorkommen. Verlgeich der ID des aktuellen
			// Arbeitspakets mit allen vorherigen Paketen
			for (int j = i; j > 0; j--) {
				if (id.equals(arbeitspaket[j - 1].getIdExtern())) {

					// Fehlermeldung setzen
					ergebnisValidierung = "Die ID " + arbeitspaket[i].getIdExtern()
							+ " darf nur einmal vergeben werden.";

					// Fehlerhafte Zelle markieren
					tabelle.getSelectionModel().clearAndSelect(i, spalteID);

					return false;
				}
			}

			// FAZ pr�fen. Darf nicht unter 1 liegen
			if (faz < 1) {

				// Fehlermeldung setzen
				ergebnisValidierung = "Der Wert FAZ f�r das Arbeitspaket " + arbeitspaket[i].getIdExtern()
						+ " muss mindestens 1 sein.";

				// Fehlerhafte Zelle markieren
				tabelle.getSelectionModel().clearAndSelect(i, spalteFaz);

				return false;
			}

			// SAZ pr�fen. Darf nicht kleiner als FAZ sein
			if (saz < faz) {

				// Fehlermeldung setzen
				ergebnisValidierung = "Der Wert SAZ f�r das Arbeitspaket " + arbeitspaket[i].getIdExtern()
						+ " muss mindestens gleich gro� wie der Wert FAZ sein.";

				// Fehlerhafte Zelle markieren
				tabelle.getSelectionModel().clearAndSelect(i, spalteSaz);

				return false;
			}

			// FEZ pr�fen. Darf nicht kleiner als FAZ sein
			if (fez < faz) {

				// Fehlermeldung setzen
				ergebnisValidierung = "Der Wert FEZ f�r das Arbeitspaket " + arbeitspaket[i].getIdExtern()
						+ " muss mindestens gleich gro� wie der Wert FAZ sein.";

				// Fehlerhafte Zelle markieren
				tabelle.getSelectionModel().clearAndSelect(i, spalteFez);

				return false;
			}

			// SEZ pr�fen. Darf nicht kleiner als FEZ sein
			if (sez < fez) {

				// Fehlermeldung setzen
				ergebnisValidierung = "Der Wert SEZ f�r das Arbeitspaket " + arbeitspaket[i].getIdExtern()
						+ " muss mindestens gleich gro� wie der Wert FEZ sein.";

				// Fehlerhafte Zelle markieren
				tabelle.getSelectionModel().clearAndSelect(i, spalteSez);

				return false;
			}

			// Die Differenz zwischen FEZ-FAZ und SEZ-SAZ muss gleich gro� sein
			if ((fez - faz) != (sez - saz)) {

				// Fehlermeldung setzen
				ergebnisValidierung = "Die Differenzen zwischen FEZ und FAZ sowie zwichen SEZ und SAZ f�r das Arbeitspaket "
						+ arbeitspaket[i].getIdExtern() + " m�ssen gleich gro� sein.";

				// Fehlerhafte Zelle markieren
				tabelle.getSelectionModel().clearAndSelect(i, spalteSez);

				return false;
			}

			// Mitarbeiterzahl pr�fen. Darf nicht kleiner als 1 sein
			if (ma < 1) {

				// Fehlermeldung setzen
				ergebnisValidierung = "Der Wert Mitarbeiteranzahl f�r das Arbeitspaket " + arbeitspaket[i].getIdExtern()
						+ " muss gr��er 0 sein.";

				// Fehlerhafte Zelle markieren
				tabelle.getSelectionModel().clearAndSelect(i, spalteAnzMitarbeiter);

				return false;
			}
			
			// Aufwand pr�fen. Aufwand muss das Produkt aus Vorgangsdauer und Mitarbeiteranzahl sein
			if ((sez - saz + 1) * ma != aufwand) {
				// Fehlermeldung setzen
				ergebnisValidierung = "Der Aufwand des Arbeitspakets " + arbeitspaket[i].getIdExtern()
						+ " muss das Produkt aus Vorgangsdauer und Mitarbeiteranzahl sein.";

				// Fehlerhafte Zelle markieren
				tabelle.getSelectionModel().clearAndSelect(i, spalteAufwand);

				return false;
			}

			// Gr��te Anzahl an Mitarbieter ermitteln
			if (ma > groessteMA) {
				groessteMA = ma;
			}

		}

		// Default-Kapazit�tsgrenze auf die gr��te Mitarbeieranzahl + 2 setzen. Kann
		// beim Ausf�hren der �bung ge�ndert werden
		anzMaxPersonen = groessteMA + 2;

		ergebnisValidierung = "Validierung erfolgreich!";
		return true;
	}

	/**
	 * Schl�gt die Validierung der Arbeitspakete fehl, so wird in einem
	 * Pop-Up-Fenster der entsprechende Fehler angezeigt
	 */
	private void validierungFehlgeschlagen() {
		Alert alert = new Alert(AlertType.WARNING);
		alert.setTitle("Ergebnis der Validierung");
		alert.setHeaderText("Die Validierung ist fehlgeschlagen! Bitte korrigieren Sie den Fehler.");
		alert.setContentText(ergebnisValidierung);

		alert.showAndWait();
	}

	/**
	 * M�cht man die angelegten Arbeitspakte als Aufgabe speichern, wird eine
	 * CSV-Datei erstellt und unter einem festgelegten Pfad gespeichert, um diese im
	 * Men�punkt "Aufgabe laden" aufrufen zu k�nnen
	 *
	 * @param arbeitspakete
	 * @param event
	 */
	public void export(Arbeitspaket[] arbeitspakete, ActionEvent event) {
		String outputFile = dateipfad + dateiname.getText() + ".csv";
		File file = new File(outputFile);
		boolean alreadyExists = file.exists();
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
				for (Arbeitspaket ap : arbeitspakete) {
					int vorgangsdauer = ap.getFez() - ap.getFaz() + 1;
					csvOutput.write(ap.getIdIntern().toString());
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
				weiter(event);
			} else {
				Alert alert = new Alert(AlertType.INFORMATION);
				alert.setTitle("Warnung");
				alert.setHeaderText("Warnung");
				alert.setContentText("Der Dateiname existiert bereits. Bitte anderen Namen eingeben.");
				alert.showAndWait();
			}

		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public void speichern(Arbeitspaket[] arbeitspakete, ActionEvent event) {
		Dialog<ButtonType> dialog = new Dialog<>();
		dialog.setTitle("Speichern");
		dialog.setHeaderText("Wollen Sie die Aufgabe speichern?");

		ButtonType speichernButton = new ButtonType("Speichern", ButtonData.OK_DONE);
		dialog.getDialogPane().getButtonTypes().add(speichernButton);

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
		} else {
			dialog.close();
		}
	}

}
