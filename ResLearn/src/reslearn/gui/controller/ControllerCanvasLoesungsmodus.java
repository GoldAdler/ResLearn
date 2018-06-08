package reslearn.gui.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javafx.beans.binding.Bindings;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.util.Callback;
import javafx.util.Pair;
import reslearn.gui.Diagramm;
import reslearn.gui.DisplayCanvas;
import reslearn.gui.ResFeld;
import reslearn.gui.ImportExport.AufgabeLadenImport;
import reslearn.model.algorithmus.AlgoKapazitaetstreu;
import reslearn.model.algorithmus.AlgoTermintreu;
import reslearn.model.paket.Arbeitspaket;
import reslearn.model.paket.ResEinheit;
import reslearn.model.paket.Teilpaket;
import reslearn.model.resCanvas.ResCanvas;

public class ControllerCanvasLoesungsmodus {

	private Diagramm diagramm;
	private ResCanvas resCanvas;
	private Teilpaket teilpaketClicked;
	private ResFeld rect;
	private ColorPicker colorPicker;
	Arbeitspaket[] arbeitspakete;
	private ArrayList<ResEinheit[][]> historieListe;
	private int historieNummer = 0;
	private ResEinheit[][] koordinatenSystemUrspruenglich;
	private ObservableMap<ResEinheit, Double> positionXObservableMap = FXCollections.observableHashMap();
	private ObservableMap<ResEinheit, Double> positionYObservableMap = FXCollections.observableHashMap();

	public ControllerCanvasLoesungsmodus(Arbeitspaket[] arbeitspakete, ArrayList<ResEinheit[][]> historieListe,
			ResCanvas resCanvas, Diagramm diagramm) {
		this.arbeitspakete = arbeitspakete;
		this.historieListe = historieListe;
		this.koordinatenSystemUrspruenglich = historieListe.get(0);
		this.resCanvas = resCanvas;
		this.diagramm = diagramm;
		erstelleTabelleLinks();
		erstelleTabelleArbeitspakete();
		erstelleButtons();
	}

	/**
	 * Initialisiert die ResFelder mit den ursprünglichen Werten. Zudem werden die
	 * Positionen der ResFelder in den ObservableMaps gespeichert und an die
	 * ResFelder gebunden.
	 *
	 * @return
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
		}
	};

	private EventHandler<MouseEvent> OnButtonKapazitaetstreuPressedEventHandler = new EventHandler<MouseEvent>() {
		@Override
		public void handle(MouseEvent e) {
			System.out.println("BLUBB");
			schrittZurueck.setDisable(true);
			schrittVor.setDisable(false);
			historieNummer = 0;

			ResCanvas resCanvas = new ResCanvas();

			for (Arbeitspaket arbeitspaket : arbeitspakete) {
				resCanvas.hinzufuegen(arbeitspaket);
			}
			// koordinatenSystemUrspruenglich = historieListe.get(0); //Hier werden noch die
			// "alten" Referenzen benötigt
			historieListe.clear();
			historieListe.add(koordinatenSystemUrspruenglich);

			ArrayList<ResEinheit[][]> historieListeNeu = AlgoKapazitaetstreu
					.getInstance(AufgabeLadenImport.maxPersonenParallel).algoDurchfuehren(resCanvas)
					.getHistorieKoordinatenSystem();
			for (int i = 1; i < historieListeNeu.size(); i++) {
				historieListe.add(historieListeNeu.get(i));
			}
			extrahiereArbeitspakete(0);
		}
	};

	private EventHandler<MouseEvent> OnButtonTermintreuPressedEventHandler = new EventHandler<MouseEvent>() {
		@Override
		public void handle(MouseEvent e) {
			schrittZurueck.setDisable(true);
			schrittVor.setDisable(false);
			historieNummer = 0;

			ResCanvas resCanvas = new ResCanvas();
			for (Arbeitspaket arbeitspaket : arbeitspakete) {
				resCanvas.hinzufuegen(arbeitspaket);
			}
			// koordinatenSystemUrspruenglich = historieListe.get(0); //Hier werden noch die
			// "alten" Referenzen benötigt
			historieListe.clear();
			historieListe.add(koordinatenSystemUrspruenglich);

			ArrayList<ResEinheit[][]> historieListeNeu = AlgoTermintreu.getInstance().algoDurchfuehren(resCanvas)
					.getHistorieKoordinatenSystem();
			for (int i = 1; i < historieListeNeu.size(); i++) {
				historieListe.add(historieListeNeu.get(i));
			}
			extrahiereArbeitspakete(0);

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
						String arbeitspaketId = arbeitspaketAktuell.getId();

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
					if (resEinheit.getTeilpaket().getArbeitspaket().getId() == arbeitspaketId) {
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

	/////////////////////////////////////////////////////////////////////////
	// Erstellung der Legende für die einzelnen Farben der Arbeitspakete //
	///////////////////////////////////////////////////////////////////////
	private Pane legende = new Pane();
	private ObservableMap<Arbeitspaket, Color> colorObservableMap = FXCollections.observableHashMap();

	// ResEinheiten -> ObservableMap ResEinheit, Vektor2i
	// ArrayList<blabala> = asfasf;
	// arrayList.get(0)[i][j] == arrayList.get(1)[i][j]
	// arrayList.get(1)[i][j] -> ResEinheit
	// ResEinheit -> update Observableblababla
	public void erstelleLegende(HashMap<Arbeitspaket, Color> arbeitspaketeMitFarbe) {
		legende.setLayoutX(DisplayCanvas.canvasStartpunktX);
		legende.setLayoutY(DisplayCanvas.canvasStartpunktY + DisplayCanvas.canvasLaenge + DisplayCanvas.gesamtAbstandX);
		legende.setPrefWidth(DisplayCanvas.canvasBreite);
		legende.setPrefHeight(DisplayCanvas.legendeHoehe);
		legende.setStyle("-fx-background-radius: 30;");
		legende.setStyle("-fx-background-color: #c0c0c0;");
		Label label = null;
		Circle circle = null;
		for (Map.Entry<Arbeitspaket, Color> entry : arbeitspaketeMitFarbe.entrySet()) {
			colorObservableMap.put(entry.getKey(), entry.getValue());
			if (label == null) {
				circle = new Circle(DisplayCanvas.abstandX, DisplayCanvas.legendeKreisStartpunktY,
						DisplayCanvas.legendeKreisRadius);
				circle.fillProperty().bind(Bindings.valueAt(colorObservableMap, entry.getKey()));
				// circle.setFill(entry.getValue());
			} else {
				circle = new Circle(label.getLayoutX() + DisplayCanvas.legendeAbstand,
						DisplayCanvas.legendeKreisStartpunktY, DisplayCanvas.legendeKreisRadius);
				circle.fillProperty().bind(Bindings.valueAt(colorObservableMap, entry.getKey()));
				// circle.setFill(entry.getValue());
			}

			label = new Label(entry.getKey().getId());
			label.setFont(new Font("Arial", DisplayCanvas.schriftGroesse));
			label.setLayoutX(circle.getCenterX() + DisplayCanvas.abstandX);
			label.layoutYProperty().bind(legende.heightProperty().subtract(label.heightProperty()).divide(2));
			legende.getChildren().addAll(circle, label);

		}
	}

	/////////////////////////////////////////////////////////////////////////
	// Erstellung der unterschiedlichen Datentypen für die Tabelle links //
	///////////////////////////////////////////////////////////////////////

	private TableView<Pair<String, Object>> table = new TableView<>();
	private ObservableList<Pair<String, Object>> data;

	@SuppressWarnings("unchecked")
	private void befuelleTabelle() {
		// Erstellen der Informationsleiste links
		data = FXCollections.observableArrayList(
				pair("Arbeitspaket", rect.getResEinheit().getTeilpaket().getArbeitspaket().getId()),
				pair("FAZ", rect.getResEinheit().getTeilpaket().getArbeitspaket().getFaz()),
				pair("FEZ", rect.getResEinheit().getTeilpaket().getArbeitspaket().getFez()),
				pair("SAZ", rect.getResEinheit().getTeilpaket().getArbeitspaket().getSaz()),
				pair("SEZ", rect.getResEinheit().getTeilpaket().getArbeitspaket().getSez()),
				pair("Vorgangsdauer", rect.getResEinheit().getTeilpaket().getArbeitspaket().getVorgangsdauer()),
				pair("Mitarbeiter", rect.getResEinheit().getTeilpaket().getArbeitspaket().getMitarbeiteranzahl()),
				pair("Aufwand", rect.getResEinheit().getTeilpaket().getArbeitspaket().getAufwand()));

		table.setItems(data);
	}

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

	class PairKeyFactory
			implements Callback<TableColumn.CellDataFeatures<Pair<String, Object>, String>, ObservableValue<String>> {
		@Override
		public ObservableValue<String> call(TableColumn.CellDataFeatures<Pair<String, Object>, String> data) {
			return new ReadOnlyObjectWrapper<>(data.getValue().getKey());
		}
	}

	class PairValueFactory
			implements Callback<TableColumn.CellDataFeatures<Pair<String, Object>, Object>, ObservableValue<Object>> {
		@SuppressWarnings({ "unchecked", "rawtypes" })
		@Override
		public ObservableValue<Object> call(TableColumn.CellDataFeatures<Pair<String, Object>, Object> data) {
			Object value = data.getValue().getValue();
			return (value instanceof ObservableValue) ? (ObservableValue) value : new ReadOnlyObjectWrapper<>(value);
		}
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

	private Button schrittZurueck = new Button();
	private Button schrittVor = new Button();
	private RadioButton termintreuModus = new RadioButton();
	private RadioButton kapazitaetstreuModus = new RadioButton();
	private final ToggleGroup modusToggleGroup = new ToggleGroup();

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

	}

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

	//////////////////////////////////////////////////////////////////////////////////
	// Erstellung der Tabelle zur Anzeige der Arbeitspakete //
	//////////////////////////////////////////////////////////////////////////////////

	private TableView<Arbeitspaket> tabelleArbeitspakete = new TableView<>();
	private ObservableList<Arbeitspaket> dataPakete;

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
		apId.setCellValueFactory(new PropertyValueFactory<Arbeitspaket, String>("id"));
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
	}

	// Markieren des gewählten Arbeitspakets in der Anzeige-Tabelle
	private void markiereArbeitspaketInTabelle(Arbeitspaket ap) {
		System.out.println("Arbeitspaket angeklickt: " + ap.getId());
		tabelleArbeitspakete.getSelectionModel().select(ap);
		tabelleArbeitspakete.scrollTo(ap);
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
}
