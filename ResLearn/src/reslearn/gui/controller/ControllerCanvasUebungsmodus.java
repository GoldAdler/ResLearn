package reslearn.gui.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Timer;
import java.util.TimerTask;

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
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.ContextMenuEvent;
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
import reslearn.gui.rescanvas.Diagramm;
import reslearn.gui.rescanvas.DisplayCanvas;
import reslearn.gui.rescanvas.ResFeld;
import reslearn.gui.tableUtils.PairKeyFactory;
import reslearn.gui.tableUtils.PairValueFactory;
import reslearn.gui.view.ViewLoesungsmodus;
import reslearn.gui.view.ViewUebungsmodus;
import reslearn.model.algorithmus.AlgoKapazitaetstreu;
import reslearn.model.algorithmus.Algorithmus;
import reslearn.model.paket.Arbeitspaket;
import reslearn.model.paket.ResEinheit;
import reslearn.model.paket.Teilpaket;
import reslearn.model.resCanvas.ResCanvas;
import reslearn.model.utils.Vektor2i;
import reslearn.model.validierung.Validierung;

public class ControllerCanvasUebungsmodus {

	private double zeigerX, zeigerY;
	private double translateX, translateY;
	private double newTranslateX, newTranslateY;
	private Diagramm diagramm;
	private ResCanvas resCanvas;
	private Teilpaket teilpaketClicked;
	private ResFeld rect;
	private ColorPicker colorPicker;
	private Button validierenButton;
	private Button loesungsButton;
	private TextArea fehlerMeldung;
	private Button buttonMaxPersonenMinus;
	private Button buttonMaxPersonenPlus;
	private TextField textFieldMaxPersonen;
	private Label maxPersonen;
	private ArrayList<Arbeitspaket> arbeitspaketeArrayList;
	private Line[] alleLinien = new Line[DisplayCanvas.resFeldSpalte + 1];
	private Label korrekturvorschlaege;
	private Rectangle rectangle;

	private ArrayList<Rectangle> rahmenListe = new ArrayList<>();
	private HashMap<Teilpaket, Rectangle> teilpaketRahmenZuordnung = new HashMap<Teilpaket, Rectangle>();

	private Pane legende = new Pane();
	private ObservableMap<Arbeitspaket, Color> colorObservableMap = FXCollections.observableHashMap();

	private TableView<Pair<String, Object>> table = new TableView<>();
	private ObservableList<Pair<String, Object>> data;

	private TableView<Arbeitspaket> tabelleArbeitspakete = new TableView<>();
	private ObservableList<Arbeitspaket> dataPakete;

	private Pane konfigModus = new Pane();
	private RadioButton termintreuModus = new RadioButton();
	private RadioButton kapazitaetstreuModus = new RadioButton();
	private final ToggleGroup modusToggleGroup = new ToggleGroup();

	// Konfigurationsmöglichkeiten im termintreuen Verfahren
	private Label dauerLabel = new Label();
	private CheckBox dauerCheckBox = new CheckBox();

	public ControllerCanvasUebungsmodus(ResCanvas resCanvas, Diagramm diagramm) {
		this.resCanvas = resCanvas;
		this.diagramm = diagramm;
		// erstelleRahmen();
		erstelleTabelle();
		erstelleTabelleArbeitspakete();
		erstelleValidierenButton();
		erstelleButtons();
		erstelleGrenzLinie();
		erstelleKorrekturvorschlaege();
		erstelleLeereFehlermeldung();
		erstelleLoesungsButton();

		AlgoKapazitaetstreu.getInstance(AufgabeLadenImport.maxPersonenParallel).setVorgangsdauerVeraenderbar(false);
	}

	public void makeDraggable(ResFeld feld) {
		feld.setOnMousePressed(OnMousePressedEventHandler);
		feld.setOnMouseDragged(OnMouseDraggedEventHandler);
		feld.setOnContextMenuRequested(OnMouseSecondaryEventHandler);
		ViewUebungsmodus.getInstance().getApTeilen().setOnAction(OnMenuItemApTeilenEventHandler);
		ViewUebungsmodus.getInstance().getReset().setOnAction(OnMenuItemResetEventHandler);
	}

	private EventHandler<MouseEvent> OnMousePressedEventHandler = new EventHandler<MouseEvent>() {
		@Override
		public void handle(MouseEvent e) {

			zeigerX = e.getSceneX();
			zeigerY = e.getSceneY();

			rect = (ResFeld) e.getSource();
			teilpaketClicked = rect.getResEinheit().getTeilpaket();

			translateX = rect.getTranslateX();
			translateY = rect.getTranslateY();

			befuelleTabelle();
			markiereArbeitspaketInTabelle(teilpaketClicked.getArbeitspaket());

		}
	};

	private EventHandler<MouseEvent> OnMouseDraggedEventHandler = new EventHandler<MouseEvent>() {
		@Override
		public void handle(MouseEvent e) {

			// 1. nur ein ResFeld (A) verschieben
			// 2. die anderen ResFelder müssen sich an dem ersten Resfeld orientieren
			// 3. offset von a auf andere übetragen
			// 4. Offset muss gemerkt werden ( falls etwas rückgängig gemacht werden muss)

			double neuePositionZeigerX = e.getSceneX();
			double neuePositionZeigerY = e.getSceneY();

			int differenzX = (int) ((neuePositionZeigerX - zeigerX) / DisplayCanvas.resFeldBreite);
			int differenzY = (int) ((neuePositionZeigerY - zeigerY) / DisplayCanvas.resFeldLaenge);

			while (differenzX != 0 || differenzY != 0) {

				boolean verschiebbar = false;

				if (differenzX > 0) {
					differenzX--;
					verschiebbar = rect.getResEinheit().getTeilpaket().bewegeX(resCanvas, 1);
					Algorithmus.ausgeben(resCanvas.getKoordinatenSystem());
					verschiebenX(verschiebbar, 1);
				}
				if (differenzX < 0) {
					differenzX++;
					verschiebbar = rect.getResEinheit().getTeilpaket().bewegeX(resCanvas, -1);
					Algorithmus.ausgeben(resCanvas.getKoordinatenSystem());
					verschiebenX(verschiebbar, -1);
				}

				verschiebbar = false;

				if (differenzY > 0) {
					differenzY--;
					verschiebbar = rect.getResEinheit().getTeilpaket().bewegeY(resCanvas, -1);
					Algorithmus.ausgeben(resCanvas.getKoordinatenSystem());
					verschiebenY(verschiebbar, 1);
				}
				if (differenzY < 0) {
					differenzY++;
					verschiebbar = rect.getResEinheit().getTeilpaket().bewegeY(resCanvas, 1);
					Algorithmus.ausgeben(resCanvas.getKoordinatenSystem());
					verschiebenY(verschiebbar, -1);
				}
			}
		}
	};

	private void verschiebenX(boolean verschiebbar, int vorzeichen) {
		if (verschiebbar) {
			newTranslateX = translateX + DisplayCanvas.resFeldBreite * vorzeichen;
			zeigerX += DisplayCanvas.resFeldBreite * vorzeichen;
			bewegeX(vorzeichen);
			translateX = rect.getTranslateX();
		}
	}

	private void verschiebenY(boolean verschiebbar, int vorzeichen) {
		if (verschiebbar) {
			newTranslateY = translateY + DisplayCanvas.resFeldLaenge * vorzeichen;
			zeigerY += DisplayCanvas.resFeldLaenge * vorzeichen;
			bewegeY(vorzeichen);
			translateY = rect.getTranslateY();
		}
	}

	/**
	 * Wenn ein angeklicktes ResFeld verschoben wird, müssen alle zugehörigen
	 * ResFelder mit dem gleichen Teilpaket und Rahmen um das X-Offset mitverschoben
	 * werden
	 *
	 * @param vorzeichen
	 */
	private void bewegeX(int vorzeichen) {
		HashMap<ResFeld, Vektor2i> resFeldMapTmp = new HashMap<ResFeld, Vektor2i>();
		for (int y = 0; y < diagramm.getResFeldArray().length; y++) {
			for (int x = 0; x < diagramm.getResFeldArray()[y].length; x++) {
				ResFeld resFeld = diagramm.getResFeldArray()[y][x];
				if (resFeld != null) {
					if (teilpaketClicked == resFeld.getResEinheit().getTeilpaket()) {
						resFeld.setTranslateX(newTranslateX);
						resFeldMapTmp.put(resFeld, new Vektor2i(y, x));
						diagramm.getResFeldArray()[y][x] = null;
					}
					if (teilpaketRahmenZuordnung.containsKey(teilpaketClicked)) {
						teilpaketRahmenZuordnung.get(teilpaketClicked)
								.setX(teilpaketClicked.getResEinheitListe()
										.get(teilpaketClicked.getVorgangsdauer()
												* (teilpaketClicked.getMitarbeiteranzahl() - 1))
										.getPosition().getxKoordinate() * DisplayCanvas.resFeldBreite);
					}
				}
			}
		}
		Iterator<Entry<ResFeld, Vektor2i>> it = resFeldMapTmp.entrySet().iterator();
		while (it.hasNext()) {
			HashMap.Entry<ResFeld, Vektor2i> entry = it.next();
			Vektor2i vektor = entry.getValue();
			diagramm.getResFeldArray()[vektor.getyKoordinate()][vektor.getxKoordinate() + vorzeichen] = entry.getKey();
		}
	}

	private void bewegeY(int vorzeichen) {
		HashMap<ResFeld, Vektor2i> resFeldMapTmp = new HashMap<ResFeld, Vektor2i>();
		for (int y = 0; y < diagramm.getResFeldArray().length; y++) {
			for (int x = 0; x < diagramm.getResFeldArray()[y].length; x++) {
				ResFeld resFeld = diagramm.getResFeldArray()[y][x];
				if (resFeld != null) {

					if (teilpaketClicked == resFeld.getResEinheit().getTeilpaket()) {
						resFeld.setTranslateY(newTranslateY);
						resFeldMapTmp.put(resFeld, new Vektor2i(y, x));
						diagramm.getResFeldArray()[y][x] = null;
					}
					if (teilpaketRahmenZuordnung.containsKey(teilpaketClicked)) {
						teilpaketRahmenZuordnung.get(teilpaketClicked)
								.setY(teilpaketClicked.getResEinheitListe()
										.get(teilpaketClicked.getVorgangsdauer()
												* (teilpaketClicked.getMitarbeiteranzahl() - 1))
										.getPosition().getyKoordinate() * DisplayCanvas.resFeldLaenge);
					}
				}
			}
		}
		Iterator<Entry<ResFeld, Vektor2i>> it = resFeldMapTmp.entrySet().iterator();
		while (it.hasNext()) {
			HashMap.Entry<ResFeld, Vektor2i> entry = it.next();
			Vektor2i vektor = entry.getValue();
			diagramm.getResFeldArray()[vektor.getyKoordinate() + vorzeichen][vektor.getxKoordinate()] = entry.getKey();
		}
	}

	private EventHandler<ContextMenuEvent> OnMouseSecondaryEventHandler = new EventHandler<ContextMenuEvent>() {
		@Override
		public void handle(ContextMenuEvent e) {
			ViewUebungsmodus.getInstance().getMenu().show(rect, e.getSceneX(), e.getSceneY());

			for (int y = 0; y < rect.getResEinheit().getTeilpaket().getMitarbeiteranzahl(); y++) {
				for (int x = 0; x < rect.getResEinheit().getTeilpaket().getVorgangsdauer(); x++) {
					if (diagramm.getResFeldArray()[y][x] != null) {
						ViewUebungsmodus.getInstance().getReset().setDisable(true);
						return;
					} else {
						ViewUebungsmodus.getInstance().getReset().setDisable(false);
					}
				}
			}
		}
	};

	private EventHandler<ActionEvent> OnMenuItemApTeilenEventHandler = new EventHandler<ActionEvent>() {

		@Override
		public void handle(ActionEvent e) {
			@SuppressWarnings("unused")
			Bearbeitungsfenster bearbeitungsFenster = new Bearbeitungsfenster(rect);
		}
	};

	EventHandler<ActionEvent> OnMenuItemResetEventHandler = new EventHandler<ActionEvent>() {
		/**
		 * Beim Klick auf den Menü-Punkt "Zurücksetzen" wird zunächst in der Logik die
		 * Methode reset() am geklickten Arbeitspaket aufgerufen. In der GUI werden alle
		 * ResFelder, die dem geklickten Arbeitspaket angehören aus dem Diagramm
		 * gelöscht.
		 *
		 * Anhand des abgeänderten Koordinatensystems, das aus der Logik zurückkommt
		 * (reset()) wird das zurückgesetzte Paket in der oberen linke Ecke neu
		 * gezeichnet
		 */
		@Override
		public void handle(ActionEvent e) {

			ViewUebungsmodus.getInstance().rahmenLoeschen();

			ResEinheit[][] koordinatenSystem = rect.getResEinheit().getTeilpaket().getArbeitspaket().reset(0,
					resCanvas);

			for (int i = 0; i < diagramm.getResFeldArray().length; i++) {
				for (int j = 0; j < diagramm.getResFeldArray()[i].length; j++) {
					ResFeld resFeld = diagramm.getResFeldArray()[i][j];
					if (resFeld != null) {
						if (resFeld.getResEinheit().getTeilpaket().getArbeitspaket() == rect.getResEinheit()
								.getTeilpaket().getArbeitspaket()) {
							ViewUebungsmodus.getInstance().getPane().getChildren().remove(resFeld);
							diagramm.getResFeldArray()[i][j] = null;
						}
					}
				}
			}

			for (int i = 0; i < koordinatenSystem.length; i++) {
				for (int j = 0; j < koordinatenSystem[i].length; j++) {
					if (koordinatenSystem[i][j] != null) {
						if (koordinatenSystem[i][j].getTeilpaket().getArbeitspaket() == rect.getResEinheit()
								.getTeilpaket().getArbeitspaket()) {
							ResFeld resFeld = new ResFeld(j * DisplayCanvas.resFeldBreite,
									i * DisplayCanvas.resFeldLaenge, koordinatenSystem[i][j]);
							resFeld.getResEinheit().setTeilpaket(koordinatenSystem[i][j].getTeilpaket());

							resFeld.setFill(rect.getFill());

							ViewUebungsmodus.getInstance().getPane().getChildren().add(resFeld);
							makeDraggable(resFeld);

							diagramm.getResFeldArray()[i][j] = resFeld;
						}
					}
				}
			}
			ViewUebungsmodus.getInstance().rahmenErstellen();
		}
	};

	/**
	 * Erstellung der Grenzlinie für Anzahl Mitarbeiter parallel
	 */
	private void erstelleGrenzLinie() {

		if (kapazitaetstreuModus.isSelected()) {
			for (int i = 0; i < 26; i++) {
				alleLinien[i] = new Line(
						DisplayCanvas.canvasStartpunktX + DisplayCanvas.abstandX + DisplayCanvas.spaltX,
						DisplayCanvas.canvasStartpunktY + DisplayCanvas.canvasLaenge - DisplayCanvas.abstandY
								- DisplayCanvas.spaltY - i * DisplayCanvas.resFeldBreite,
						DisplayCanvas.canvasStartpunktX + DisplayCanvas.canvasBreite - DisplayCanvas.abstandX,
						DisplayCanvas.canvasStartpunktY + DisplayCanvas.canvasLaenge - DisplayCanvas.abstandY
								- DisplayCanvas.spaltY - i * DisplayCanvas.resFeldBreite);

				alleLinien[i].setStroke(Color.RED);

				if (i != AufgabeLadenImport.maxPersonenParallel) {
					alleLinien[i].setOpacity(0);
				}
			}
			ViewUebungsmodus.getInstance().getPane().getChildren()
					.add(alleLinien[AufgabeLadenImport.maxPersonenParallel]);
		}
	}

	/**
	 * Erstellung des Validieren-Buttons
	 */
	private void erstelleValidierenButton() {
		validierenButton = new Button("Validieren");
		validierenButton.setLayoutX(
				DisplayCanvas.canvasStartpunktX + DisplayCanvas.canvasBreite + DisplayCanvas.gesamtAbstandX);
		validierenButton.setLayoutY(DisplayCanvas.canvasStartpunktY + DisplayCanvas.canvasLaenge
				- DisplayCanvas.abstandX - DisplayCanvas.spaltX);
		validierenButton.setOnAction(OnButtonValidierenEventHandler);
		// validierenButton.setFont(new Font("Arial", DisplayCanvas.schriftGroesse));
	}

	private EventHandler<ActionEvent> OnButtonValidierenEventHandler = new EventHandler<ActionEvent>() {

		@Override
		public void handle(ActionEvent event) {
			validierenButton.setDisable(true);
			Validierung vali = new Validierung(diagramm.getResFeldArray());
			String ausgabe = "";

			if (termintreuModus.isSelected()) {

				vali.AlgoTermintreu(resCanvas);

				for (int i = 0; i < vali.getFeedbackListe().size(); i++) {
					ausgabe += vali.getFeedbackListe().get(i).toString();
				}
				fehlerMeldung.setText(ausgabe);

				if (vali.getFeedbackListe().get(0).toString() == "Alles in Ordnung!") {
					fehlerMeldung.setStyle("-fx-text-fill: green;");
				} else {
					fehlerMeldung.setStyle("-fx-text-fill: red;");
				}

			} else {

				vali.AlgoKapazitaetstreu(AufgabeLadenImport.maxPersonenParallel, resCanvas, dauerCheckBox.isSelected());

				for (int i = 0; i < vali.getFeedbackListe().size(); i++) {
					ausgabe += vali.getFeedbackListe().get(i).toString();
				}
				fehlerMeldung.setText(ausgabe);

				if (vali.getFeedbackListe().get(0).toString() == "Alles in Ordnung!") {
					fehlerMeldung.setStyle("-fx-text-fill: green;");
				} else {
					fehlerMeldung.setStyle("-fx-text-fill: red;");
				}
			}
			Timer timer = new Timer();
			timer.schedule(new TimerTask() {

				@Override
				public void run() {
					validierenButton.setDisable(false);

				}
			}, 2000);
		}
	};

	private void erstelleLoesungsButton() {
		loesungsButton = new Button("Lösung anzeigen");
		loesungsButton.setLayoutX(DisplayCanvas.tabelleLayoutX);
		loesungsButton.setLayoutY(DisplayCanvas.legendeStartpunktY + 7 * DisplayCanvas.resFeldLaenge);
		loesungsButton.setOnAction(OnButtonLoesungAnzeigenEventHandler);
	}

	private EventHandler<ActionEvent> OnButtonLoesungAnzeigenEventHandler = new EventHandler<ActionEvent>() {

		@Override
		public void handle(ActionEvent event) {
			try {
				ViewLoesungsmodus.getInstance().initializeCanvasView(ControllerUebungsmodus.letztesArbeitspaket);
			} catch (IOException e) {
				e.printStackTrace();
			}

		}
	};

	/**
	 * Erzeugung der linken Fehlermeldung
	 */
	private void erstelleLeereFehlermeldung() {
		fehlerMeldung = new TextArea("");
		fehlerMeldung.setFont(new Font("Arial", DisplayCanvas.schriftGroesse));
		fehlerMeldung.setEditable(false);
		fehlerMeldung.setLayoutX(
				DisplayCanvas.canvasStartpunktX + DisplayCanvas.canvasBreite + DisplayCanvas.gesamtAbstandX);
		fehlerMeldung.setLayoutY(DisplayCanvas.tabelleLayoutY + 2 * DisplayCanvas.resFeldLaenge);
		fehlerMeldung.setPrefWidth(DisplayCanvas.breiteFehlermeldung);
		fehlerMeldung.setPrefHeight(DisplayCanvas.canvasLaenge);
		fehlerMeldung.setPrefHeight(DisplayCanvas.hoeheFehlermeldung);
	}

	/**
	 * Erzeugung der Korrekturvorschläge in der Fehlermeldung
	 */
	private void erstelleKorrekturvorschlaege() {
		korrekturvorschlaege = new Label("Korrekturvorschläge");
		korrekturvorschlaege.setLayoutX(
				DisplayCanvas.canvasStartpunktX + DisplayCanvas.canvasBreite + DisplayCanvas.gesamtAbstandX);
		korrekturvorschlaege.setLayoutY(DisplayCanvas.tabelleLayoutY);
		korrekturvorschlaege.setPrefWidth(DisplayCanvas.breiteFehlermeldung);
		korrekturvorschlaege.setPrefHeight(DisplayCanvas.resFeldBreite * 1.5);
		korrekturvorschlaege.setAlignment(Pos.CENTER);
		korrekturvorschlaege.setFont(new Font("Arial", DisplayCanvas.schriftGroesse * 1.5));
		korrekturvorschlaege.setStyle("-fx-font-weight: bold");
	}

	/**
	 * Erstellung der Legende für die einzelnen Arbeitspakete mit den entsprechenden
	 * Farben
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
	 * Befüllen der Informationstabelle bei Klick auf ein Arbeitspaket mit den
	 * entsprechenden Werten
	 */
	@SuppressWarnings("unchecked")
	private void befuelleTabelle() {
		data = FXCollections.observableArrayList(
				pair("Arbeitspaket", rect.getResEinheit().getTeilpaket().getArbeitspaket().getIdExtern()),
				pair("Farbe", rect.getFill()),
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
	 * Erzeugung der Informationstabelle auf der linken Seite
	 */
	@SuppressWarnings("unchecked")
	private void erstelleTabelle() {

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
					wechsleFarbe();
				}
			} else {
				setText(null);
				setGraphic(null);
			}
		}
	}

	/**
	 * Beim Klick auf den Farbbutton in der Informationstabelle wechselt das
	 * Arbeitspaket seine Farbe
	 */
	private void wechsleFarbe() {
		colorPicker.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				for (ResFeld[] resAr : diagramm.getResFeldArray()) {
					for (ResFeld teilpaket : resAr) {
						if (teilpaket != null) {
							if (teilpaketClicked.getArbeitspaket() == teilpaket.getResEinheit().getTeilpaket()
									.getArbeitspaket()) {
								teilpaket.setFill(colorPicker.getValue());
								colorObservableMap.replace(teilpaketClicked.getArbeitspaket(), colorPicker.getValue());
							}
						}
					}
				}
			}
		});
	}

	/**
	 * Erzeugung der Übersichtstabelle aller Arbeitspakete
	 */
	@SuppressWarnings("unchecked")
	private void erstelleTabelleArbeitspakete() {
		dataPakete = FXCollections.observableArrayList();
		arbeitspaketeArrayList = resCanvas.getArbeitspaketListe();
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
		tabelleArbeitspakete.getSelectionModel().select(ap);
		tabelleArbeitspakete.scrollTo(ap);
	}

	/**
	 * Erzeugung der RadioButtons Kapazitätstreu und Termintreu
	 */
	private void erstelleButtons() {
		termintreuModus.setLayoutX(DisplayCanvas.buttonLoesungsmodusLayoutX);
		termintreuModus.setLayoutY(DisplayCanvas.resFeldBreite);
		termintreuModus.setPrefWidth(DisplayCanvas.buttonLoesungsmodusBreite);
		termintreuModus.setFont(new Font("Arial", DisplayCanvas.schriftGroesse));
		termintreuModus.setText("Termintreu");
		termintreuModus.setOnMouseClicked(OnButtonTermintreuPressedEventHandler);
		termintreuModus.setToggleGroup(modusToggleGroup);

		kapazitaetstreuModus
				.setLayoutX(DisplayCanvas.buttonLoesungsmodusLayoutX * 2 + DisplayCanvas.buttonLoesungsmodusBreite);
		kapazitaetstreuModus.setLayoutY(DisplayCanvas.resFeldBreite);
		kapazitaetstreuModus.setPrefWidth(DisplayCanvas.buttonLoesungsmodusBreite);
		kapazitaetstreuModus.setFont(new Font("Arial", DisplayCanvas.schriftGroesse));
		kapazitaetstreuModus.setText("Kapazitätstreu");
		kapazitaetstreuModus.setToggleGroup(modusToggleGroup);
		kapazitaetstreuModus.setOnMouseClicked(OnButtonKapazitaetstreuPressedEventHandler);
		kapazitaetstreuModus.setSelected(true);

		maxPersonen = new Label("Kapazitätsgrenze:");
		maxPersonen.setFont(new Font("Arial", DisplayCanvas.schriftGroesse));
		maxPersonen.setPrefWidth(DisplayCanvas.buttonLoesungsmodusBreite + DisplayCanvas.resFeldBreite);
		maxPersonen.setLayoutX(DisplayCanvas.resFeldBreite);
		maxPersonen.setLayoutY(DisplayCanvas.resFeldLaenge * 3);

		buttonMaxPersonenMinus = new Button("-");
		buttonMaxPersonenMinus.setPrefWidth(DisplayCanvas.resFeldBreite * 1.5);
		buttonMaxPersonenMinus.setFont(new Font("Arial", DisplayCanvas.schriftGroesse));
		buttonMaxPersonenMinus.setLayoutX(DisplayCanvas.resFeldBreite);
		buttonMaxPersonenMinus.setLayoutY(DisplayCanvas.resFeldLaenge * 3);
		buttonMaxPersonenMinus.setOnAction(OnButtonMaxPersonenMinusEventHandler);

		textFieldMaxPersonen = new TextField(Integer.toString(AufgabeLadenImport.maxPersonenParallel));
		textFieldMaxPersonen.setPrefWidth(DisplayCanvas.resFeldBreite * 1.5);
		textFieldMaxPersonen.setLayoutX(DisplayCanvas.resFeldBreite);
		textFieldMaxPersonen.setLayoutY(DisplayCanvas.resFeldLaenge * 3);
		textFieldMaxPersonen.setAlignment(Pos.CENTER);
		textFieldMaxPersonen.setEditable(false);
		textFieldMaxPersonen.setFont(new Font("Arial", DisplayCanvas.schriftGroesse));

		buttonMaxPersonenPlus = new Button("+");
		buttonMaxPersonenPlus.setFont(new Font("Arial", DisplayCanvas.schriftGroesse));
		buttonMaxPersonenPlus.setPrefWidth(DisplayCanvas.resFeldBreite * 1.5);
		buttonMaxPersonenPlus.setLayoutX(DisplayCanvas.resFeldBreite);
		buttonMaxPersonenPlus.setLayoutY(DisplayCanvas.resFeldLaenge * 3);
		buttonMaxPersonenPlus.setOnAction(OnButtonMaxPersonenPlusEventHandler);

		dauerLabel.setText("Vorgangsdauer änderbar?");
		dauerLabel.setFont(new Font("Arial", DisplayCanvas.schriftGroesse));
		dauerLabel.setPrefWidth(DisplayCanvas.buttonLoesungsmodusBreite + DisplayCanvas.resFeldBreite);
		dauerLabel.setLayoutX(DisplayCanvas.resFeldBreite);

		dauerCheckBox.setFont(new Font("Arial", DisplayCanvas.schriftGroesse));
		dauerCheckBox.setLayoutX(DisplayCanvas.resFeldBreite);

		konfigModus.setLayoutX(DisplayCanvas.konfigModusStartpunktX);
		konfigModus.setLayoutY(DisplayCanvas.konfigModusStartpunktY);
		konfigModus.setPrefWidth(DisplayCanvas.konfigModusBreite);
		konfigModus.setPrefHeight(DisplayCanvas.konfigModusHoehe);
		konfigModus.setStyle("-fx-border-color: black");

		modusKonfiguration();
	}

	private EventHandler<MouseEvent> OnButtonKapazitaetstreuPressedEventHandler = new EventHandler<MouseEvent>() {
		@Override
		public void handle(MouseEvent e) {
			for (int i = 0; i < alleLinien.length; i++) {
				if (i == AufgabeLadenImport.maxPersonenParallel) {
					alleLinien[i].setOpacity(100);
				} else {
					alleLinien[i].setOpacity(0);
				}
			}
			modusKonfiguration();
		}
	};

	private EventHandler<MouseEvent> OnButtonTermintreuPressedEventHandler = new EventHandler<MouseEvent>() {
		@Override
		public void handle(MouseEvent e) {
			for (int i = 0; i < alleLinien.length; i++) {
				alleLinien[i].setOpacity(0);
			}
			modusKonfiguration();
		}
	};

	private EventHandler<ActionEvent> OnButtonMaxPersonenMinusEventHandler = new EventHandler<ActionEvent>() {

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
			for (int i = 0; i < alleLinien.length; i++) {
				if (i == AufgabeLadenImport.maxPersonenParallel) {
					alleLinien[i].setOpacity(100);
				} else {
					alleLinien[i].setOpacity(0);
				}
			}
		}
	};

	private EventHandler<ActionEvent> OnButtonMaxPersonenPlusEventHandler = new EventHandler<ActionEvent>() {

		@Override
		public void handle(ActionEvent event) {
			buttonMaxPersonenMinus.setDisable(false);

			if (AufgabeLadenImport.maxPersonenParallel < ViewUebungsmodus.maxMitarbeiter - 1) {
				AufgabeLadenImport.maxPersonenParallel++;
				textFieldMaxPersonen.setText(Integer.toString(AufgabeLadenImport.maxPersonenParallel));
			} else {
				buttonMaxPersonenPlus.setDisable(true);
			}
			for (int i = 0; i < alleLinien.length; i++) {
				if (i == AufgabeLadenImport.maxPersonenParallel) {
					alleLinien[i].setOpacity(100);
				} else {
					alleLinien[i].setOpacity(0);
				}
			}

		}
	};

	/**
	 * Je nach dem, welcher Modus ausgewählt ist, sollen die jeweiligen
	 * Konfigurationsmöglichkeiten angezeigt werden
	 */
	private void modusKonfiguration() {

		// Pane beim ersten Aufruf befüllen
		if (konfigModus.getChildren().size() == 0) {
			konfigModus.getChildren().addAll(kapazitaetstreuModus, termintreuModus, maxPersonen, buttonMaxPersonenMinus,
					textFieldMaxPersonen, buttonMaxPersonenPlus, dauerLabel, dauerCheckBox);

			// Kapazitätsgrenzen-Konfig Positionen festlegen
			buttonMaxPersonenMinus.setLayoutX(maxPersonen.getLayoutX() + maxPersonen.getPrefWidth());
			textFieldMaxPersonen
					.setLayoutX(buttonMaxPersonenMinus.getLayoutX() + buttonMaxPersonenMinus.getPrefWidth());
			buttonMaxPersonenPlus.setLayoutX(textFieldMaxPersonen.getLayoutX() + textFieldMaxPersonen.getPrefWidth());

			// Dauer veränderbar-Konfig Position festlegen
			dauerLabel.setLayoutY(DisplayCanvas.resFeldBreite * 5);
			dauerCheckBox.setLayoutX(dauerLabel.getLayoutX() + dauerLabel.getPrefWidth());
			dauerCheckBox.setLayoutY(DisplayCanvas.resFeldBreite * 5);
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

	/**
	 * Erzeugung aller Rahmen für jedes Teilpaket Der Rahmen hat die Position des
	 * linken oberen ResEinheit im Teilpaket
	 *
	 * @return rahmenListe
	 */
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

	public TableView<Arbeitspaket> getTabelleArbeitspakete() {
		return tabelleArbeitspakete;
	}

	public TableView<Pair<String, Object>> getTable() {
		return table;
	}

	public Pane getLegende() {
		return legende;
	}

	public Button getValidierenButton() {
		return validierenButton;
	}

	public ToggleGroup getModusToggleGroup() {
		return modusToggleGroup;
	}

	public RadioButton getButtonTermintreuModus() {
		return termintreuModus;
	}

	public RadioButton getButtonKapazitaetstreuModus() {
		return kapazitaetstreuModus;
	}

	public Line getKapaGrenze(int i) {
		return alleLinien[i];
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

	public Label getKorrekturvorschlaege() {
		return korrekturvorschlaege;
	}

	public TextArea getFehlermeldung() {
		return fehlerMeldung;
	}

	public ArrayList<Rectangle> getRahmenListe() {
		return rahmenListe;
	}

	public Pane getKonfigModus() {
		return konfigModus;
	}

	public Button getLoesungsButton() {
		return loesungsButton;
	}
}