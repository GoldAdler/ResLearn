package reslearn.gui.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.util.Callback;
import javafx.util.Pair;
import reslearn.gui.ImportExport.AufgabeLadenImport;
import reslearn.gui.rescanvas.DisplayCanvas;
import reslearn.gui.rescanvas.ResFeld;
import reslearn.gui.tableUtils.PairKeyFactory;
import reslearn.gui.tableUtils.PairValueFactory;
import reslearn.gui.view.ViewLoesungsmodus;
import reslearn.model.algorithmus.AlgoKapazitaetstreu;
import reslearn.model.algorithmus.AlgoTermintreu;
import reslearn.model.paket.Arbeitspaket;
import reslearn.model.paket.ResEinheit;
import reslearn.model.paket.Teilpaket;
import reslearn.model.resCanvas.ResCanvas;

public class ControllerCanvasLoesungsmodus {
	// TODO: Vorgangsdauer veränderbar Checkbox nur bei Kapazitätstreu (Setter im
	// Algorithmus)
	private ResCanvas resCanvas;
	private Teilpaket teilpaketClicked;
	private ColorPicker colorPicker;
	private Arbeitspaket[] arbeitspakete;
	private ArrayList<Arbeitspaket> arbeitspaketeArrayList;
	private Line[] kapazitaetsgrenzeLinien = new Line[DisplayCanvas.resFeldZeile];

	/**
	 * Das aktuell ausgewählte ResFeld.
	 */
	private ResFeld rect;

	/**
	 * Liste der einzelnen Schritte. Wegen den Positions-Bindings muss das erste
	 * Element der historieListe IMMER die originalen Referenzen enthalten (wichtig
	 * zum ersetzen der Positionen!)
	 */
	private ArrayList<ResEinheit[][]> historieListe;

	/**
	 * Merkt sich die aktuelle Position in der historieListe.
	 */
	private int historieNummer = 0;

	/**
	 * Das ursprüngliche KoordinatenSystem, mit dem verglichen wird. Enthält die
	 * ursprünglichen Referenzen.
	 */
	private ResEinheit[][] koordinatenSystemUrspruenglich;

	/**
	 * Wichtig für die X-Positions-Bindings der ResEinheiten. Hier werden die
	 * INITIALEN Referenzen eingefügt.
	 */
	private ObservableMap<ResEinheit, Double> positionXObservableMap = FXCollections.observableHashMap();

	/**
	 * Wichtig für die Y-Positions-Bindings der ResEinheiten. Hier werden die
	 * INITIALEN Referenzen eingefügt.
	 */
	private ObservableMap<ResEinheit, Double> positionYObservableMap = FXCollections.observableHashMap();
	private Rectangle rectangle;
	private ArrayList<Rectangle> rahmenListe = new ArrayList<>();
	private HashMap<Teilpaket, Rectangle> teilpaketRahmenZuordnung = new HashMap<Teilpaket, Rectangle>();

	// Legende unten
	private Pane legende = new Pane();
	private ObservableMap<Arbeitspaket, Color> colorObservableMap = FXCollections.observableHashMap();

	// Tabelle links
	private TableView<Pair<String, Object>> table = new TableView<>();
	private ObservableList<Pair<String, Object>> data;

	private Pane konfigModus = new Pane();
	private Button schrittZurueck = new Button();
	private Button schrittVor = new Button();
	private RadioButton termintreuModus = new RadioButton();
	private RadioButton kapazitaetstreuModus = new RadioButton();
	private final ToggleGroup modusToggleGroup = new ToggleGroup();

	// Konfigurationsmöglichkeiten im kapazitätstreuen Verfahren
	private Button buttonMaxPersonenMinus;
	private Button buttonMaxPersonenPlus;
	private TextField textFieldMaxPersonen;
	private Label maxPersonen;

	// Konfigurationsmöglichkeiten im termintreuen Verfahren
	private Label dauerLabel = new Label();
	private CheckBox dauerCheckBox = new CheckBox();

	// Tabelle oben
	private TableView<Arbeitspaket> tabelleArbeitspakete = new TableView<>();
	private ObservableList<Arbeitspaket> dataPakete;

	public ControllerCanvasLoesungsmodus(Arbeitspaket[] arbeitspakete, ArrayList<ResEinheit[][]> historieListe,
			ResCanvas resCanvas) {
		this.arbeitspakete = arbeitspakete;
		this.historieListe = historieListe;
		this.koordinatenSystemUrspruenglich = historieListe.get(0);
		this.resCanvas = resCanvas;
		erstelleTabelleLinks();
		erstelleTabelleArbeitspakete();
		erstelleButtons();
		erstelleGrenzLinie();

	}

	/**
	 * Initialisiert die ResFelder mit den ursprünglichen Werten. Zudem werden die
	 * Positionen der ResFelder in den ObservableMaps gespeichert und an die
	 * ResFelder gebunden.
	 *
	 * @return resFeldArray
	 */
	public ResFeld[][] initializePositionObservableMap() {
		ResFeld[][] resFeldArray = new ResFeld[DisplayCanvas.resFeldSpalte][DisplayCanvas.resFeldZeile];

		// Befüllen der ObservableMaps
		for (ResEinheit[] zeile : koordinatenSystemUrspruenglich) {
			for (ResEinheit resEinheit : zeile) {
				if (resEinheit != null) {
					positionXObservableMap.put(resEinheit,
							(double) resEinheit.getPosition().getxKoordinate() * DisplayCanvas.resFeldBreite);
					positionYObservableMap.put(resEinheit,
							(double) resEinheit.getPosition().getyKoordinate() * DisplayCanvas.resFeldLaenge);
				}
			}
		}

		// Zuweisung der Bindings
		for (int i = 0; i < koordinatenSystemUrspruenglich.length; i++) {
			for (int j = 0; j < koordinatenSystemUrspruenglich[i].length; j++) {
				if (koordinatenSystemUrspruenglich[i][j] != null) {
					resFeldArray[i][j] = new ResFeld(j * DisplayCanvas.resFeldBreite, i * DisplayCanvas.resFeldLaenge,
							koordinatenSystemUrspruenglich[i][j]);
					resFeldArray[i][j].xProperty()
							.bind(Bindings.valueAt(positionXObservableMap, resFeldArray[i][j].getResEinheit()));
					resFeldArray[i][j].yProperty()
							.bind(Bindings.valueAt(positionYObservableMap, resFeldArray[i][j].getResEinheit()));
					resFeldArray[i][j].setOnMousePressed(OnMousePressedEventHandler);
				}
			}
		}
		return resFeldArray;
	}

	public void makeDraggable(ResFeld feld) {
		feld.setOnMousePressed(OnMousePressedEventHandler);
	}

	// Event Handler Maus klicken
	private EventHandler<MouseEvent> OnMousePressedEventHandler = new EventHandler<MouseEvent>() {
		@Override
		public void handle(MouseEvent e) {
			rect = (ResFeld) e.getSource();
			teilpaketClicked = rect.getResEinheit().getTeilpaket();

			befuelleTabelle();
			markiereArbeitspaketInTabelle(teilpaketClicked.getArbeitspaket());
		}
	};

	private EventHandler<MouseEvent> OnButtonZurueckPressedEventHandler = new EventHandler<MouseEvent>() {
		@Override
		public void handle(MouseEvent e) {
			schrittVor.setDisable(false);
			historieNummer--;
			if (historieNummer == 0) {
				schrittZurueck.setDisable(true);
			}
			extrahiereArbeitspakete(historieNummer);
			ViewLoesungsmodus.getInstance().rahmenLoeschen();
			ViewLoesungsmodus.getInstance().rahmenErstellen();
		}
	};

	private EventHandler<MouseEvent> OnButtonVorPressedEventHandler = new EventHandler<MouseEvent>() {
		@Override
		public void handle(MouseEvent e) {
			schrittZurueck.setDisable(false);
			historieNummer++;
			if (historieNummer + 1 == historieListe.size()) {
				schrittVor.setDisable(true);
			}
			extrahiereArbeitspakete(historieNummer);
			ViewLoesungsmodus.getInstance().rahmenLoeschen();
			ViewLoesungsmodus.getInstance().rahmenErstellen();
		}
	};

	private EventHandler<MouseEvent> OnButtonKapazitaetstreuPressedEventHandler = new EventHandler<MouseEvent>() {
		@Override
		public void handle(MouseEvent e) {
			kapazitaetstreuReset();
			modusKonfiguration();
		}
	};

	/**
	 * Hier werden die Klötzchen der kapazitätstreuen Optimierung auf den
	 * Anfangszustand zurückgesetzt. Diese Methode kann nicht innerhalb des
	 * Eventhandlers stehen, da sie auch beim Drücken der Buttons zum Ändern der
	 * Kapazitätsgrenze aufgerufen werden muss.
	 */
	private void kapazitaetstreuReset() {
		schrittZurueck.setDisable(true);
		schrittVor.setDisable(false);

		// Kapazitätsgrenze-Buttons + Linie aktivieren
		buttonMaxPersonenMinus.setDisable(false);
		buttonMaxPersonenPlus.setDisable(false);
		kapazitaetsgrenzeLinien[AufgabeLadenImport.maxPersonenParallel].setOpacity(100);

		// Wieder von vorne anfangen
		historieNummer = 0;

		resCanvas = new ResCanvas();

		ArrayList<Arbeitspaket> copyArbeitspaket = new ArrayList<>();
		for (Arbeitspaket arbeitspaket : arbeitspakete) {
			copyArbeitspaket.add(arbeitspaket.copy());
		}

		buttonMaxPersonenMinus.setDisable(false);
		buttonMaxPersonenPlus.setDisable(false);
		kapazitaetsgrenzeLinien[AufgabeLadenImport.maxPersonenParallel].setOpacity(100);

		copyArbeitspaket.forEach(arbeitspaket -> resCanvas.hinzufuegen(arbeitspaket));
		System.out.println("Maxparallel: " + AufgabeLadenImport.maxPersonenParallel);
		historieListe = AlgoKapazitaetstreu.getInstance(AufgabeLadenImport.maxPersonenParallel)
				.algoDurchfuehren(resCanvas).getHistorieKoordinatenSystem();
		extrahiereArbeitspakete(historieNummer);
	}

	private EventHandler<MouseEvent> OnButtonTermintreuPressedEventHandler = new EventHandler<MouseEvent>() {
		@Override
		public void handle(MouseEvent e) {
			schrittZurueck.setDisable(true);
			schrittVor.setDisable(false);

			// Kapazitätsgrenze-Buttons + Linie deaktivieren
			buttonMaxPersonenMinus.setDisable(true);
			buttonMaxPersonenPlus.setDisable(true);
			kapazitaetsgrenzeLinien[AufgabeLadenImport.maxPersonenParallel].setOpacity(0);

			// Wieder von vorne anfangen
			historieNummer = 0;

			resCanvas = new ResCanvas();

			ArrayList<Arbeitspaket> copyArbeitspaket = new ArrayList<>();
			for (Arbeitspaket arbeitspaket : arbeitspakete) {
				copyArbeitspaket.add(arbeitspaket.copy());
			}

			copyArbeitspaket.forEach(arbeitspaket -> resCanvas.hinzufuegen(arbeitspaket));

			historieListe = AlgoTermintreu.getInstance().algoDurchfuehren(resCanvas).getHistorieKoordinatenSystem();
			extrahiereArbeitspakete(historieNummer);

			modusKonfiguration();
		}
	};

	private EventHandler<ActionEvent> handleButtonMaxPersonenMinusAction = new EventHandler<ActionEvent>() {

		@Override
		public void handle(ActionEvent event) {
			arbeitspaketeArrayList = resCanvas.getArbeitspaketListe();
			int maxMitarbeiterAnzahl = 0;
			buttonMaxPersonenPlus.setDisable(false);
			for (int i = 0; i < arbeitspaketeArrayList.size(); i++) {
				if (arbeitspaketeArrayList.get(i).getMitarbeiteranzahl() > maxMitarbeiterAnzahl) {
					maxMitarbeiterAnzahl = arbeitspaketeArrayList.get(i).getMitarbeiteranzahl();
				}
			}
			if (AufgabeLadenImport.maxPersonenParallel > maxMitarbeiterAnzahl) {
				AufgabeLadenImport.maxPersonenParallel--;
				textFieldMaxPersonen.setText(Integer.toString(AufgabeLadenImport.maxPersonenParallel));
			} else {
				buttonMaxPersonenMinus.setDisable(true);
			}
			for (int i = 0; i < kapazitaetsgrenzeLinien.length; i++) {
				if (i == AufgabeLadenImport.maxPersonenParallel) {
					kapazitaetsgrenzeLinien[i].setOpacity(100);
				} else {
					kapazitaetsgrenzeLinien[i].setOpacity(0);
				}
			}
			kapazitaetstreuReset();
		}
	};

	private EventHandler<ActionEvent> handleButtonMaxPersonenPlusAction = new EventHandler<ActionEvent>() {

		@Override
		public void handle(ActionEvent event) {
			buttonMaxPersonenMinus.setDisable(false);
			if (AufgabeLadenImport.maxPersonenParallel < 25) {
				AufgabeLadenImport.maxPersonenParallel++;
				textFieldMaxPersonen.setText(Integer.toString(AufgabeLadenImport.maxPersonenParallel));
			} else {
				buttonMaxPersonenPlus.setDisable(true);
			}
			for (int i = 0; i < kapazitaetsgrenzeLinien.length; i++) {
				if (i == AufgabeLadenImport.maxPersonenParallel) {
					kapazitaetsgrenzeLinien[i].setOpacity(100);
				} else {
					kapazitaetsgrenzeLinien[i].setOpacity(0);
				}
			}
			kapazitaetstreuReset();
		}
	};

	private EventHandler<ActionEvent> handleCheckBoxDauerAction = new EventHandler<ActionEvent>() {

		@Override
		public void handle(ActionEvent event) {
			// TODO: Richtigen Algorithmus auswählen
			boolean vorgangsdauerVeraenderbar = dauerCheckBox.isSelected() ? true : false;
			AlgoKapazitaetstreu.getInstance(AufgabeLadenImport.maxPersonenParallel)
					.setVorgangsdauerVeraenderbar(vorgangsdauerVeraenderbar);
			kapazitaetstreuReset();
		}
	};

	/**
	 * Extrahiert die einzelnen Arbeitspakete aus dem neuen Koordinatensystem anhand
	 * der Historiennummer und ersetzt alle zugehörigen ResEinheiten-Position
	 * mithilfe von {@link #ersetzeResEinheiten(String, ArrayList)}.
	 *
	 * @param historieNummer
	 */
	private void extrahiereArbeitspakete(int historieNummer) {
		ResEinheit[][] koordinatenSystemNeu = historieListe.get(historieNummer);
		ArrayList<Arbeitspaket> arbeitspaketeAbgearbeitet = new ArrayList<Arbeitspaket>();

		for (int y = 0; y < koordinatenSystemNeu.length; y++) {
			for (int x = 0; x < koordinatenSystemNeu[0].length; x++) {
				if (koordinatenSystemNeu[y][x] != null) {
					Arbeitspaket arbeitspaketAktuell = koordinatenSystemNeu[y][x].getTeilpaket().getArbeitspaket();
					if (!arbeitspaketeAbgearbeitet.contains(arbeitspaketAktuell)) {
						arbeitspaketeAbgearbeitet.add(arbeitspaketAktuell);
						String arbeitspaketId = arbeitspaketAktuell.getIdIntern();

						ArrayList<ResEinheit> abzuarbeitendeResEinheiten = new ArrayList<ResEinheit>();
						arbeitspaketAktuell.getTeilpaketListe().forEach(teilpaket -> teilpaket.getResEinheitListe()
								.forEach(resEinheit -> abzuarbeitendeResEinheiten.add(resEinheit)));

						ersetzeResEinheiten(arbeitspaketId, abzuarbeitendeResEinheiten);
					}
				}
			}
		}
	}

	/**
	 * Überschreibt die Positionen aller ResFelder.
	 *
	 * @param arbeitspaketId
	 * @param abzuarbeitendeResEinheiten
	 */
	private void ersetzeResEinheiten(String arbeitspaketId, ArrayList<ResEinheit> abzuarbeitendeResEinheiten) {
		int index = 0;
		for (ResEinheit[] zeile : koordinatenSystemUrspruenglich) {
			for (ResEinheit resEinheit : zeile) {
				if (resEinheit != null) {
					if (resEinheit.getTeilpaket().getArbeitspaket().getIdIntern() == arbeitspaketId) {
						if (index == abzuarbeitendeResEinheiten.size()) {
							return;
						}
						positionXObservableMap.replace(resEinheit,
								(double) abzuarbeitendeResEinheiten.get(index).getPosition().getxKoordinate()
										* DisplayCanvas.resFeldBreite);
						positionYObservableMap.replace(resEinheit,
								(double) abzuarbeitendeResEinheiten.get(index).getPosition().getyKoordinate()
										* DisplayCanvas.resFeldLaenge);
						index++;
					}
				}
			}
		}
	}

	/**
	 * Erstellt die Legende mit den einzelnen Farben der Arbeitspakete
	 *
	 * @param arbeitspaketeMitFarbe
	 */
	public void erstelleLegende(HashMap<Arbeitspaket, Color> arbeitspaketeMitFarbe) {
		Label label = null;
		Circle circle = null;
		int xCounter = 0;
		int yCounter = 1;

		legende.setLayoutX(DisplayCanvas.tabelleLayoutX);
		legende.setLayoutY(DisplayCanvas.buttonLoesungsmodusLayoutY + 8 * DisplayCanvas.resFeldLaenge);
		legende.setPrefWidth(DisplayCanvas.breiteFehlermeldung);
		legende.setStyle("-fx-background-radius: 30;");
		legende.setStyle("-fx-background-color: #c0c0c0;");

		for (Map.Entry<Arbeitspaket, Color> entry : arbeitspaketeMitFarbe.entrySet()) {
			colorObservableMap.put(entry.getKey(), entry.getValue());

			circle = new Circle(
					DisplayCanvas.breiteFehlermeldung / 7 + (DisplayCanvas.breiteFehlermeldung / 3) * xCounter,
					DisplayCanvas.legendeHoehe / 4 + (DisplayCanvas.legendeHoehe / 2) * yCounter,
					DisplayCanvas.legendeKreisRadius);
			circle.fillProperty().bind(Bindings.valueAt(colorObservableMap, entry.getKey()));

			label = new Label(entry.getKey().getIdExtern());
			label.setFont(new Font("Arial", DisplayCanvas.schriftGroesse));
			label.setLayoutX(circle.getCenterX() + DisplayCanvas.abstandX);
			label.setLayoutY(circle.getCenterY() - DisplayCanvas.legendeKreisRadius / 2);

			legende.getChildren().addAll(circle, label);

			if (xCounter == 2) {
				xCounter = 0;
				yCounter += 2.7;
			} else {
				xCounter++;
			}
		}
		legende.setPrefHeight((DisplayCanvas.legendeHoehe * 1.3) + (yCounter * 12));
	}

	/**
	 * Befüllt die TabelleLinks mit den Werten der angeklickten ResEinheit (rect).
	 */
	@SuppressWarnings("unchecked")
	private void befuelleTabelle() {
		data = FXCollections.observableArrayList(
				pair("Arbeitspaket", rect.getResEinheit().getTeilpaket().getArbeitspaket().getIdExtern()),
				pair("FAZ", rect.getResEinheit().getTeilpaket().getArbeitspaket().getFaz()),
				pair("FEZ", rect.getResEinheit().getTeilpaket().getArbeitspaket().getFez()),
				pair("SAZ", rect.getResEinheit().getTeilpaket().getArbeitspaket().getSaz()),
				pair("SEZ", rect.getResEinheit().getTeilpaket().getArbeitspaket().getSez()),
				pair("Vorgangsdauer", rect.getResEinheit().getTeilpaket().getArbeitspaket().getVorgangsdauer()),
				pair("Mitarbeiter", rect.getResEinheit().getTeilpaket().getArbeitspaket().getMitarbeiteranzahl()),
				pair("Aufwand", rect.getResEinheit().getTeilpaket().getArbeitspaket().getAufwand()));

		table.setItems(data);
	}

	/**
	 * Erstellt die Grundstruktur der TabelleLinks.
	 */
	@SuppressWarnings("unchecked")
	private void erstelleTabelleLinks() {

		table.setEditable(true);
		table.setLayoutX(DisplayCanvas.tabelleLayoutX);
		table.setLayoutY(DisplayCanvas.tabelleLayoutY);
		table.setPrefHeight(DisplayCanvas.tabelleLaenge);
		table.setStyle("-fx-font:" + DisplayCanvas.schriftGroesse + " Arial;");

		TableColumn<Pair<String, Object>, String> name = new TableColumn<>("Name");
		name.setCellValueFactory(new PairKeyFactory());
		name.setMinWidth(DisplayCanvas.tabelleBreite / 2);
		name.setSortable(false);

		TableColumn<Pair<String, Object>, Object> wert = new TableColumn<>("Wert");
		wert.setCellValueFactory(new PairValueFactory());
		wert.setMinWidth(DisplayCanvas.tabelleBreite / 2);
		wert.setSortable(false);

		table.getColumns().addAll(name, wert);
		wert.setCellFactory(
				new Callback<TableColumn<Pair<String, Object>, Object>, TableCell<Pair<String, Object>, Object>>() {
					@Override
					public TableCell<Pair<String, Object>, Object> call(
							TableColumn<Pair<String, Object>, Object> column) {
						return new PairValueCell();
					}
				});
	}

	private Pair<String, Object> pair(String name, Object value) {
		return new Pair<>(name, value);
	}

	class PairValueCell extends TableCell<Pair<String, Object>, Object> {
		@Override
		protected void updateItem(Object item, boolean empty) {
			super.updateItem(item, empty);

			if (item != null) {
				if (item instanceof String) {
					setText((String) item);
					setGraphic(null);
				} else if (item instanceof Integer) {
					setText(Integer.toString((Integer) item));
					setGraphic(null);
				} else if (item instanceof Color) {
					colorPicker = new ColorPicker();
					colorPicker.setStyle("-fx-color-label-visible: false;");
					colorPicker.setValue((Color) rect.getFill());
					setGraphic(colorPicker);
				}
			} else {
				setText(null);
				setGraphic(null);
			}
		}
	}

	private void erstelleButtons() {
		schrittZurueck.setLayoutX(DisplayCanvas.buttonLoesungsmodusLayoutX);
		schrittZurueck.setLayoutY(DisplayCanvas.buttonLoesungsmodusLayoutY);
		schrittZurueck.setPrefWidth(DisplayCanvas.buttonLoesungsmodusBreite);
		schrittZurueck.setFont(new Font("Arial", DisplayCanvas.schriftGroesse));
		schrittZurueck.setText("Schritt zurück");
		schrittZurueck.setOnMouseClicked(OnButtonZurueckPressedEventHandler);
		schrittZurueck.setDisable(true);

		schrittVor.setLayoutX(DisplayCanvas.buttonLoesungsmodusLayoutX * 2 + DisplayCanvas.buttonLoesungsmodusBreite);
		schrittVor.setLayoutY(DisplayCanvas.buttonLoesungsmodusLayoutY);
		schrittVor.setPrefWidth(DisplayCanvas.buttonLoesungsmodusBreite);
		schrittVor.setFont(new Font("Arial", DisplayCanvas.schriftGroesse));
		schrittVor.setText("Schritt vor");
		schrittVor.setOnMouseClicked(OnButtonZurueckPressedEventHandler);
		schrittVor.setOnMouseClicked(OnButtonVorPressedEventHandler);

		termintreuModus.setLayoutX(DisplayCanvas.buttonLoesungsmodusLayoutX);
		termintreuModus.setLayoutY(DisplayCanvas.buttonLoesungsmodusLayoutY + DisplayCanvas.resFeldBreite * 2);
		termintreuModus.setPrefWidth(DisplayCanvas.buttonLoesungsmodusBreite);
		termintreuModus.setFont(new Font("Arial", DisplayCanvas.schriftGroesse));
		termintreuModus.setText("Termintreu");
		termintreuModus.setOnMouseClicked(OnButtonTermintreuPressedEventHandler);
		termintreuModus.setToggleGroup(modusToggleGroup);

		kapazitaetstreuModus
				.setLayoutX(DisplayCanvas.buttonLoesungsmodusLayoutX * 2 + DisplayCanvas.buttonLoesungsmodusBreite);
		kapazitaetstreuModus.setLayoutY(DisplayCanvas.buttonLoesungsmodusLayoutY + DisplayCanvas.resFeldBreite * 2);
		kapazitaetstreuModus.setPrefWidth(DisplayCanvas.buttonLoesungsmodusBreite);
		kapazitaetstreuModus.setFont(new Font("Arial", DisplayCanvas.schriftGroesse));
		kapazitaetstreuModus.setText("Kapazitätstreu");
		kapazitaetstreuModus.setOnMouseClicked(OnButtonKapazitaetstreuPressedEventHandler);
		kapazitaetstreuModus.setToggleGroup(modusToggleGroup);
		kapazitaetstreuModus.setSelected(true);

		maxPersonen = new Label("Kapazitätsgrenze:");
		maxPersonen.setFont(new Font("Arial", DisplayCanvas.schriftGroesse));
		maxPersonen.setPrefWidth(DisplayCanvas.buttonLoesungsmodusBreite + DisplayCanvas.resFeldBreite);

		buttonMaxPersonenMinus = new Button("-");
		buttonMaxPersonenMinus.setPrefWidth(DisplayCanvas.resFeldBreite * 1.5);
		buttonMaxPersonenMinus.setFont(new Font("Arial", DisplayCanvas.schriftGroesse));
		buttonMaxPersonenMinus.setOnAction(handleButtonMaxPersonenMinusAction);

		textFieldMaxPersonen = new TextField(Integer.toString(AufgabeLadenImport.maxPersonenParallel));
		textFieldMaxPersonen.setPrefWidth(DisplayCanvas.resFeldBreite * 1.5);
		textFieldMaxPersonen.setAlignment(Pos.CENTER);
		textFieldMaxPersonen.setEditable(false);
		textFieldMaxPersonen.setFont(new Font("Arial", DisplayCanvas.schriftGroesse));

		buttonMaxPersonenPlus = new Button("+");
		buttonMaxPersonenPlus.setFont(new Font("Arial", DisplayCanvas.schriftGroesse));
		buttonMaxPersonenPlus.setPrefWidth(DisplayCanvas.resFeldBreite * 1.5);
		buttonMaxPersonenPlus.setOnAction(handleButtonMaxPersonenPlusAction);

		dauerLabel.setText("Vorgangsdauer änderbar?");
		dauerLabel.setFont(new Font("Arial", DisplayCanvas.schriftGroesse));
		dauerLabel.setPrefWidth(DisplayCanvas.buttonLoesungsmodusBreite + DisplayCanvas.resFeldBreite);

		dauerCheckBox.setFont(new Font("Arial", DisplayCanvas.schriftGroesse));
		dauerCheckBox.setOnAction(handleCheckBoxDauerAction);

		konfigModus.setLayoutX(DisplayCanvas.buttonLoesungsmodusLayoutX);
		konfigModus.setLayoutY(DisplayCanvas.buttonLoesungsmodusLayoutY + DisplayCanvas.resFeldBreite * 4);
		konfigModus.setPrefWidth(DisplayCanvas.breiteFehlermeldung);
		konfigModus.setPrefHeight(DisplayCanvas.resFeldLaenge * 4);

		modusKonfiguration();
	}

	/**
	 * Erstellt alle Grenzlinien.
	 */
	private void erstelleGrenzLinie() {

		if (kapazitaetstreuModus.isSelected()) {
			for (int i = 0; i < DisplayCanvas.resFeldZeile; i++) {
				kapazitaetsgrenzeLinien[i] = new Line(
						DisplayCanvas.canvasStartpunktX + DisplayCanvas.abstandX + DisplayCanvas.spaltX,
						DisplayCanvas.canvasStartpunktY + DisplayCanvas.canvasLaenge - DisplayCanvas.abstandY
								- DisplayCanvas.spaltY - i * DisplayCanvas.resFeldBreite,
						DisplayCanvas.canvasStartpunktX + DisplayCanvas.canvasBreite - DisplayCanvas.abstandX,
						DisplayCanvas.canvasStartpunktY + DisplayCanvas.canvasLaenge - DisplayCanvas.abstandY
								- DisplayCanvas.spaltY - i * DisplayCanvas.resFeldBreite);

				kapazitaetsgrenzeLinien[i].setStroke(Color.RED);

				if (i != AufgabeLadenImport.maxPersonenParallel) {
					kapazitaetsgrenzeLinien[i].setOpacity(0);
				}
			}
			ViewLoesungsmodus.getInstance().getPane().getChildren()
					.add(kapazitaetsgrenzeLinien[AufgabeLadenImport.maxPersonenParallel]);
		}

	}

	/**
	 * Erstellung der Tabelle zur Anzeige der Arbeitspakete.
	 */
	@SuppressWarnings("unchecked")
	private void erstelleTabelleArbeitspakete() {
		dataPakete = FXCollections.observableArrayList();
		ArrayList<Arbeitspaket> arbeitspaketeArrayList = resCanvas.getArbeitspaketListe();
		arbeitspaketeArrayList.forEach(p -> dataPakete.add(p));

		int breite = DisplayCanvas.tabelleArbeitspaketBreite;
		if (dataPakete.size() > 2) {
			breite -= 15;
		}

		TableColumn<Arbeitspaket, String> apId = new TableColumn<>("Arbeitspaket-ID");
		apId.setMinWidth(breite / 7);
		apId.setCellValueFactory(new PropertyValueFactory<Arbeitspaket, String>("idExtern"));
		apId.setSortType(TableColumn.SortType.ASCENDING);

		TableColumn<Arbeitspaket, Integer> apFaz = new TableColumn<>("FAZ");
		apFaz.setMinWidth(breite / 7);
		apFaz.setCellValueFactory(new PropertyValueFactory<Arbeitspaket, Integer>("faz"));

		TableColumn<Arbeitspaket, Integer> apSaz = new TableColumn<>("SAZ");
		apSaz.setMinWidth(breite / 7);
		apSaz.setCellValueFactory(new PropertyValueFactory<Arbeitspaket, Integer>("saz"));

		TableColumn<Arbeitspaket, Integer> apFez = new TableColumn<>("FEZ");
		apFez.setMinWidth(breite / 7);
		apFez.setCellValueFactory(new PropertyValueFactory<Arbeitspaket, Integer>("fez"));

		TableColumn<Arbeitspaket, Integer> apSez = new TableColumn<>("SEZ");
		apSez.setMinWidth(breite / 7);
		apSez.setCellValueFactory(new PropertyValueFactory<Arbeitspaket, Integer>("sez"));

		TableColumn<Arbeitspaket, Integer> apAnzMitarbeiter = new TableColumn<>("Max. Personen");
		apAnzMitarbeiter.setMinWidth(breite / 7);
		apAnzMitarbeiter.setCellValueFactory(new PropertyValueFactory<Arbeitspaket, Integer>("mitarbeiteranzahl"));

		TableColumn<Arbeitspaket, Integer> apAufwand = new TableColumn<>("Aufwand (PT)");
		apAufwand.setMinWidth(breite / 7);
		apAufwand.setCellValueFactory(new PropertyValueFactory<Arbeitspaket, Integer>("aufwand"));

		tabelleArbeitspakete.setItems(dataPakete);
		tabelleArbeitspakete.setEditable(true);
		tabelleArbeitspakete.setLayoutX(DisplayCanvas.tabelleArbeitspaketLayoutX);
		tabelleArbeitspakete.setLayoutY(DisplayCanvas.tabelleArbeitspaketLayoutY);
		tabelleArbeitspakete.setPrefSize(DisplayCanvas.tabelleArbeitspaketBreite,
				DisplayCanvas.tabelleArbeitspaketLaenge);
		tabelleArbeitspakete.setStyle("-fx-font:" + DisplayCanvas.schriftGroesse + " Arial;");
		tabelleArbeitspakete.getColumns().addAll(apId, apFaz, apSaz, apFez, apSez, apAnzMitarbeiter, apAufwand);
		tabelleArbeitspakete.getSortOrder().add(apId);

		// Spalten-Breite automatisch anpassen
		tabelleArbeitspakete.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
	}

	/**
	 * Markieren des gewählten Arbeitspakets in der Anzeige-Tabelle
	 *
	 * @param ap
	 */
	private void markiereArbeitspaketInTabelle(Arbeitspaket ap) {
		System.out.println("Arbeitspaket angeklickt: " + ap.getIdIntern());

		// Da hier mit Kopien von Arbeitspaketen gearbeitet wird, muss das ausgewählte
		// Arbeitspaket durch die ID herausgefunden werden
		// Die ID des ausgewählten AP wird herausgelesen
		String id = ap.getIdIntern();
		Arbeitspaket apAusgewaehlt = null;

		// Die in der Tabelle befindlichen APs werden überprüft, ob sie die gleiche ID
		// haben
		for (int i = 0; i < tabelleArbeitspakete.getItems().size(); i++) {
			if (tabelleArbeitspakete.getItems().get(i).getIdIntern().equals(id)) {
				apAusgewaehlt = tabelleArbeitspakete.getItems().get(i);
				break;
			}
		}

		// das Paket mit der gleichen ID wird in der Tabelle markiert
		tabelleArbeitspakete.getSelectionModel().select(apAusgewaehlt);
		tabelleArbeitspakete.scrollTo(apAusgewaehlt);
	}

	public ArrayList<Rectangle> erstelleRahmen() {
		rahmenListe.clear();

		for (Arbeitspaket ap : resCanvas.getArbeitspaketListe()) {
			for (Teilpaket tp : ap.getTeilpaketListe()) {
				ResEinheit reseinheit = tp.getResEinheitListe()
						.get(tp.getVorgangsdauer() * (tp.getMitarbeiteranzahl() - 1));
				rectangle = new Rectangle(reseinheit.getPosition().getxKoordinate() * DisplayCanvas.resFeldBreite,
						reseinheit.getPosition().getyKoordinate() * DisplayCanvas.resFeldLaenge,
						reseinheit.getTeilpaket().getVorgangsdauer() * DisplayCanvas.resFeldBreite,
						reseinheit.getTeilpaket().getMitarbeiteranzahl() * DisplayCanvas.resFeldLaenge);
				rectangle.setFill(Color.TRANSPARENT);
				rectangle.setStroke(Color.BLACK);
				rectangle.setStrokeWidth(1);
				rectangle.setMouseTransparent(true);

				rahmenListe.add(rectangle);

				if (teilpaketRahmenZuordnung.containsKey(tp)) {
					teilpaketRahmenZuordnung.replace(tp, rectangle);

				} else {
					teilpaketRahmenZuordnung.put(tp, rectangle);
				}
			}
		}
		return rahmenListe;
	}

	/**
	 * Je nach dem, welcher Modus ausgewählt ist, sollen die jeweiligen
	 * Konfigurationsmöglichkeiten angezeigt werden
	 */
	private void modusKonfiguration() {

		// Pane beim ersten Aufruf befüllen
		if (konfigModus.getChildren().size() == 0) {
			konfigModus.getChildren().addAll(maxPersonen, buttonMaxPersonenMinus, textFieldMaxPersonen,
					buttonMaxPersonenPlus, dauerLabel, dauerCheckBox);

			// Kapazitätsgrenzen-Konfig Positionen festlegen
			buttonMaxPersonenMinus.setLayoutX(maxPersonen.getLayoutX() + maxPersonen.getPrefWidth());
			textFieldMaxPersonen
					.setLayoutX(buttonMaxPersonenMinus.getLayoutX() + buttonMaxPersonenMinus.getPrefWidth());
			buttonMaxPersonenPlus.setLayoutX(textFieldMaxPersonen.getLayoutX() + textFieldMaxPersonen.getPrefWidth());

			// Dauer veränderbar-Konfig Position festlegen
			dauerLabel.setLayoutY(DisplayCanvas.resFeldBreite * 2);
			dauerCheckBox.setLayoutX(dauerLabel.getLayoutX() + dauerLabel.getPrefWidth());
			dauerCheckBox.setLayoutY(DisplayCanvas.resFeldBreite * 2);
		}

		if (kapazitaetstreuModus.isSelected()) {
			buttonMaxPersonenMinus.setDisable(false);
			buttonMaxPersonenPlus.setDisable(false);
			dauerCheckBox.setDisable(false);
			maxPersonen.textFillProperty().set(Color.BLACK);
			dauerLabel.textFillProperty().set(Color.BLACK);
			textFieldMaxPersonen.setStyle("-fx-text-fill: black;");
		} else if (termintreuModus.isSelected()) {
			buttonMaxPersonenMinus.setDisable(true);
			buttonMaxPersonenPlus.setDisable(true);
			dauerCheckBox.setDisable(true);
			maxPersonen.textFillProperty().set(Color.GREY);
			dauerLabel.textFillProperty().set(Color.GREY);
			textFieldMaxPersonen.setStyle("-fx-text-fill: grey;");
		}
	}

	public TableView<Arbeitspaket> getTabelleArbeitspakete() {
		return tabelleArbeitspakete;
	}

	public TableView<Pair<String, Object>> getTable() {
		return table;
	}

	public Pane getLegende() {
		return legende;
	}

	public Button getButtonSchrittZurueck() {
		return schrittZurueck;
	}

	public Button getButtonSchrittVor() {
		return schrittVor;
	}

	public RadioButton getButtonTermintreuModus() {
		return termintreuModus;
	}

	public RadioButton getButtonKapazitaetstreuModus() {
		return kapazitaetstreuModus;
	}

	public Line getKapaGrenze(int i) {
		return kapazitaetsgrenzeLinien[i];
	}

	public Button getButtonMaxPersonenMinus() {
		return buttonMaxPersonenMinus;
	}

	public Button getButtonMaxPersonenPlus() {
		return buttonMaxPersonenPlus;
	}

	public TextField getTextFieldMaxPersonen() {
		return textFieldMaxPersonen;
	}

	public Label getMaxPersonen() {
		return maxPersonen;
	}

	public ArrayList<Rectangle> getRahmenListe() {
		return rahmenListe;
	}

	public Pane getKonfigModus() {
		return konfigModus;
	}
}