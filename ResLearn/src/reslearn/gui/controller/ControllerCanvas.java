package reslearn.gui.controller;

import java.util.ArrayList;
import java.util.LinkedList;

import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.ContextMenuEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Callback;
import javafx.util.Pair;
import reslearn.gui.Diagramm;
import reslearn.gui.DisplayCanvas;
import reslearn.gui.ResFeld;
import reslearn.gui.View;
import reslearn.model.paket.Arbeitspaket;
import reslearn.model.paket.ResEinheit;
import reslearn.model.paket.Teilpaket;
import reslearn.model.resCanvas.ResCanvas;

public class ControllerCanvas {

	private double zeigerX, zeigerY;
	private double translateX, translateY;
	private double newTranslateX, newTranslateY;
	private Teilpaket teilpaketClicked;
	private ResFeld feld, rect;
	private ColorPicker colorPicker;
	private ResCanvas resCanvas;
	public static TableView<Pair<String, Object>> table = new TableView<>();
	private ObservableList<Pair<String, Object>> data;
	private boolean vertikal;

	public ControllerCanvas(ResCanvas resCanvas) {
		this.resCanvas = resCanvas;
	}

	public void makeDraggable(ResFeld feld) {
		this.feld = feld;
		feld.setOnMousePressed(OnMousePressedEventHandler);
		feld.setOnMouseDragged(OnMouseDraggedEventHandler);
		feld.setOnMouseReleased(OnMouseReleasedEventHandler);
		feld.setOnContextMenuRequested(OnMouseSecondaryEventHandler);
		View.ap.setOnAction(OnMenuItemApEventHandler);
	}

	// Event Handler Maus klicken
	EventHandler<MouseEvent> OnMousePressedEventHandler = new EventHandler<MouseEvent>() {
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

	// Event Handler Maus ziehen
	EventHandler<MouseEvent> OnMouseDraggedEventHandler = new EventHandler<MouseEvent>() {
		@Override
		public void handle(MouseEvent e) {

			// 1. nur ein ResFeld (A) verschieben
			// 2. die anderen ResFelder müssen sich an dem ersten Resfeld orientieren
			// 3. offset von a auf andere übetragen
			// 4. Offset muss gemerkt werden ( falls etwas rückgängig gemacht werden muss)

			// Überprüfung ( logische verschieben abgschlossen, aber noch nicht in der Gui
			// dargestellt)

			double neuePositionZeigerX = e.getSceneX();
			double neuePositionZeigerY = e.getSceneY();

			int differenzX = (int) ((neuePositionZeigerX - zeigerX) / DisplayCanvas.resFeldBreite); // neue Position
																									// Mauszeiger - alte
			int differenzY = (int) ((neuePositionZeigerY - zeigerY) / DisplayCanvas.resFeldLaenge); // Position
																									// Mauszeiger

			// Verschiebung auf der X-Achse bewirkt logisches Verschieben im
			// Koordinatensystem

			while (differenzX != 0 || differenzY != 0) {

				boolean verschiebbar = false;

				if (differenzX > 0) {
					differenzX--;
					verschiebbar = rect.getTeilpaket().bewegeX(resCanvas, 1);
					verschiebenX(verschiebbar, 1);
				}
				if (differenzX < 0) {
					differenzX++;
					verschiebbar = rect.getTeilpaket().bewegeX(resCanvas, -1);
					verschiebenX(verschiebbar, -1);
				}

				// Verschiebung auf der Y-Achse bewirkt logisches Verschieben im
				// Koordinatensystem

				verschiebbar = false;

				if (differenzY > 0) {
					differenzY--;
					verschiebbar = rect.getTeilpaket().bewegeY(resCanvas, -1);
					verschiebenY(verschiebbar, 1);
				}
				if (differenzY < 0) {
					differenzY++;
					verschiebbar = rect.getTeilpaket().bewegeY(resCanvas, 1);
					verschiebenY(verschiebbar, -1);
				}
			}
		}
	};

	public void verschiebenX(boolean verschiebbar, int vorzeichen) {
		if (verschiebbar) {
			newTranslateX = translateX + DisplayCanvas.resFeldBreite * vorzeichen;
			zeigerX += DisplayCanvas.resFeldBreite * vorzeichen;
			bewegeX();
			translateX = rect.getTranslateX();
		}
	}

	public void verschiebenY(boolean verschiebbar, int vorzeichen) {
		if (verschiebbar) {
			newTranslateY = translateY + DisplayCanvas.resFeldLaenge * vorzeichen;
			zeigerY += DisplayCanvas.resFeldLaenge * vorzeichen;
			bewegeY();
			translateY = rect.getTranslateY();
		}
	}

	public void bewegeX() {
		for (ResFeld[] resAr : Diagramm.res) {
			for (ResFeld teilpaket : resAr) {
				if (teilpaket != null) {
					if (teilpaketClicked == teilpaket.getResEinheit().getTeilpaket()) {
						teilpaket.setTranslateX(newTranslateX);
					}
				}
			}
		}
	}

	public void bewegeY() {
		for (ResFeld[] resAr : Diagramm.res) {
			for (ResFeld teilpaket : resAr) {
				if (teilpaket != null) {
					if (teilpaketClicked == teilpaket.getResEinheit().getTeilpaket()) {
						teilpaket.setTranslateY(newTranslateY);
					}
				}
			}
		}
	}

	EventHandler<MouseEvent> OnMouseReleasedEventHandler = new EventHandler<MouseEvent>() {
		@Override
		public void handle(MouseEvent e) {

		}
	};

	// Zeige KontextMenü mit Rechtsklick
	EventHandler<ContextMenuEvent> OnMouseSecondaryEventHandler = new EventHandler<ContextMenuEvent>() {
		@Override
		public void handle(ContextMenuEvent e) {
			View.menu.show(feld, e.getSceneX(), e.getSceneY());
		}
	};

	// MenuItem Arbeitspaket teilen
	EventHandler<ActionEvent> OnMenuItemApEventHandler = new EventHandler<ActionEvent>() {
		@Override
		public void handle(ActionEvent e) {

			Pane pane = new Pane();
			Label arbeitspaket = new Label("neues Arbeitspaket: " + rect.getTeilpaket().getArbeitspaket().getId());
			Label farbe = new Label("Wählen Sie die Pakete, die sie abtrennen möchten.");
			Button teilen = new Button("Teile Arbeitspaket");

			Slider sliderX = new Slider();
			sliderX.setMin(0);
			sliderX.setMax(rect.getTeilpaket().getVorgangsdauer());
			sliderX.setShowTickLabels(true);
			sliderX.setMajorTickUnit(1);
			sliderX.setMinorTickCount(0);
			sliderX.setSnapToTicks(true);

			Slider sliderY = new Slider();
			sliderY.setMaxWidth(120);
			sliderY.setRotate(270);
			sliderY.setMin(0);
			sliderY.setMax(rect.getTeilpaket().getMitarbeiteranzahl());
			sliderY.setShowTickLabels(true);
			sliderY.setMajorTickUnit(1);
			sliderY.setMinorTickCount(0);
			sliderY.setSnapToTicks(true);

			LinkedList<ResFeld> resFeldListe = new LinkedList<ResFeld>();
			arbeitspaket.setLayoutX(15);
			arbeitspaket.setLayoutY(5);
			farbe.setLayoutX(15);
			farbe.setLayoutY(30);
			sliderY.setLayoutX(180);
			sliderY.setLayoutY(100);
			sliderX.setLayoutX(60);
			sliderX.setLayoutY(170);
			teilen.setLayoutX(15);
			teilen.setLayoutY(210);

			pane.getChildren().addAll(arbeitspaket, farbe, teilen, sliderX, sliderY);
			Scene scene = new Scene(pane, 300, 250);
			Stage bearbeitungsmodus = new Stage();

			bearbeitungsmodus.initModality(Modality.WINDOW_MODAL);
			bearbeitungsmodus.initStyle(StageStyle.UTILITY);
			bearbeitungsmodus.initOwner(View.classStage);
			bearbeitungsmodus.setTitle("Arbeitspaket bearbeiten");
			bearbeitungsmodus.setScene(scene);
			bearbeitungsmodus.setX(View.classStage.getWidth() / 2);
			bearbeitungsmodus.setY(View.classStage.getHeight() / 2);
			bearbeitungsmodus.show();

			/*
			 * geklicktes Teilpaket im Pop-Up nachzeichnen
			 */
			for (int i = 0; i < rect.getTeilpaket().getVorgangsdauer(); i++) {
				for (int j = 0; j < rect.getTeilpaket().getMitarbeiteranzahl(); j++) {
					ResFeld dummy = new ResFeld(i * 20 + 65, j * 20 + 65, 20, 20);
					dummy.setFill(rect.getFill());
					// dummy.setStroke(Color.GRAY);
					pane.getChildren().add(dummy);

				}
			}

			sliderY.valueProperty().addListener(new ChangeListener<Number>() {

				@Override
				public void changed(ObservableValue<? extends Number> arg0, Number arg1, Number neuerWert) {
					if (neuerWert.doubleValue() % neuerWert.intValue() == 0 || neuerWert.intValue() == 0) {
						// nur bei ganzzahligem Wertwechsel des Sliders soll das Paket aktualisiert
						// werden

						System.out.println("CHANGE" + arg1 + " " + neuerWert);
						int counter = 0;
						for (ResFeld feld : resFeldListe) {
							pane.getChildren().remove(feld);
						}
						resFeldListe.clear();

						// Wert des Sliders entspricht der Anzahl der Spalten die abgeschnitten werden
						// sollen
						for (int i = rect.getTeilpaket().getMitarbeiteranzahl(); i > 0; i--) {
							for (int j = 0; j < rect.getTeilpaket().getVorgangsdauer(); j++) {
								if (counter < neuerWert.intValue() * rect.getTeilpaket().getVorgangsdauer()) {
									System.out.println("FELD ANMALEN");
									ResFeld dummy = new ResFeld(j * 20 + 65, i * 20 + 45, 20, 20);
									dummy.setStroke(rect.getFill());
									dummy.setTeilpaket(rect.getTeilpaket());
									dummy.setResEinheit(rect.getTeilpaket().getResEinheitListe().get(counter));
									resFeldListe.add(dummy);
									pane.getChildren().add(dummy);
									counter++;
								} else {
									break;
								}
							}
						}
					}
				}
			});

			sliderY.setOnMousePressed(new EventHandler<MouseEvent>() {
				@Override
				public void handle(MouseEvent e) {
					sliderX.setOpacity(0.4);
					sliderY.setOpacity(1);
					vertikal = false;
				}
			});

			sliderX.valueProperty().addListener(new ChangeListener<Number>() {

				@Override
				public void changed(ObservableValue<? extends Number> arg0, Number arg1, Number neuerWert) {
					if (neuerWert.doubleValue() % neuerWert.intValue() == 0 || neuerWert.intValue() == 0) {

						System.out.println("CHANGE" + arg1 + " " + neuerWert);
						int counter = 0;
						for (ResFeld feld : resFeldListe) {
							pane.getChildren().remove(feld);
						}
						resFeldListe.clear();

						for (int i = 0; i < rect.getTeilpaket().getVorgangsdauer(); i++) {
							for (int j = 0; j < rect.getTeilpaket().getMitarbeiteranzahl(); j++) {
								if (counter < neuerWert.intValue() * rect.getTeilpaket().getMitarbeiteranzahl()) {
									System.out.println("FELD ANMALEN");
									ResFeld dummy = new ResFeld(i * 20 + 65, j * 20 + 65, 20, 20);
									dummy.setStroke(Color.GREY);
									dummy.setTeilpaket(rect.getTeilpaket());
									dummy.setResEinheit(rect.getTeilpaket().getResEinheitListe()
											.get((j * rect.getTeilpaket().getVorgangsdauer()) + i));
									System.out.println();
									resFeldListe.add(dummy);
									pane.getChildren().add(dummy);
									counter++;
								} else {
									break;
								}
							}
						}
					}
				}
			});
			// counter + j * rect.getTeilpaket().getVorgangsdauer() - (neuerWert.intValue()
			// * j)
			sliderX.setOnMousePressed(new EventHandler<MouseEvent>() {
				@Override
				public void handle(MouseEvent e) {
					sliderY.setOpacity(0.4);
					sliderX.setOpacity(1);
					vertikal = true;
				}
			});

			teilen.setOnMouseClicked(new EventHandler<MouseEvent>() {

				@Override
				public void handle(MouseEvent arg0) {
					ArrayList<ResEinheit> neueResEinheitListe = new ArrayList<>();
					for (ResFeld resFeld : resFeldListe) {
						neueResEinheitListe.add(resFeld.getResEinheit());
					}
					if (neueResEinheitListe.size() != 0) {
						if (vertikal) {
							int vorgangsdauer = resFeldListe.size() / rect.getTeilpaket().getMitarbeiteranzahl();
							System.out.println("Vorgangsdauer: " + vorgangsdauer);

							// rect.getTeilpaket().trenneTeilpaketVertikal(neueResEinheitListe,
							// vorgangsdauer);
							rect.getTeilpaket().trenneTeilpaketVertikal(neueResEinheitListe);
							System.out.println("vertikal");
						} else {
							rect.getTeilpaket().trenneTeilpaketHorizontal(neueResEinheitListe);
							System.out.println("horizontal");
						}
					}
					bearbeitungsmodus.close();
				}
			});
		}
	};

	/////////////////////////////////////////////////////////////////////////
	// Erstellung der unterschiedlichen Datentypen für die Tabelle links //
	///////////////////////////////////////////////////////////////////////

	@SuppressWarnings("unchecked")
	public void befuelleTabelle() {
		// Erstellen der Informationsleiste links
		data = FXCollections.observableArrayList(pair("Arbeitspaket", rect.getTeilpaket().getArbeitspaket().getId()),
				pair("Farbe", rect.getFill()), pair("FAZ", rect.getTeilpaket().getArbeitspaket().getFaz()),
				pair("FEZ", rect.getTeilpaket().getArbeitspaket().getFez()),
				pair("SAZ", rect.getTeilpaket().getArbeitspaket().getSaz()),
				pair("SEZ", rect.getTeilpaket().getArbeitspaket().getSez()),
				pair("Vorgangsdauer", rect.getTeilpaket().getArbeitspaket().getVorgangsdauer()),
				pair("Mitarbeiter", rect.getTeilpaket().getArbeitspaket().getMitarbeiteranzahl()),
				pair("Aufwand", rect.getTeilpaket().getArbeitspaket().getAufwand()));

		table.setItems(data);
	}

	@SuppressWarnings("unchecked")
	public void erstelleTabelle() {

		table.setEditable(true);
		table.setLayoutX(DisplayCanvas.tabelleLayoutX);
		table.setLayoutY(DisplayCanvas.tabelleLayoutY);
		// table.
		// table.setPrefSize(DisplayCanvas.tabelleBreite, DisplayCanvas.tabelleLaenge);
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

	public void wechsleFarbe() {
		colorPicker.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				for (ResFeld[] resAr : Diagramm.res) {
					for (ResFeld resG : resAr) {
						if (resG != null) {
							if (teilpaketClicked == resG.getResEinheit().getTeilpaket()) {
								resG.setFill(colorPicker.getValue());
							}
						}
					}
				}
			}
		});
	}
	
	
//////////////////////////////////////////////////////////////////////////////////
//Erstellung der Tabelle zur Anzeige der Arbeitspakete			    //
//////////////////////////////////////////////////////////////////////////////////

	public static TableView<Arbeitspaket> tabelleArbeitspakete = new TableView<>();
	private ObservableList<Arbeitspaket> dataPakete;

	@SuppressWarnings("unchecked")
	public void erstelleTabelleArbeitspakete() {
		dataPakete = FXCollections.observableArrayList();
		ArrayList<Arbeitspaket> arbeitspaketeArrayList = resCanvas.getArbeitspaketListe();
		arbeitspaketeArrayList.forEach(p -> dataPakete.add(p));
		
		int breite = DisplayCanvas.tabelleArbeitspaketBreite;
		if (dataPakete.size() > 2) {
			breite -= 12;
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
		tabelleArbeitspakete.setPrefSize(DisplayCanvas.tabelleArbeitspaketBreite, DisplayCanvas.tabelleArbeitspaketLaenge);
		tabelleArbeitspakete.setStyle("-fx-font:" + DisplayCanvas.schriftGroesse + " Arial;");
		tabelleArbeitspakete.getColumns().addAll(apId, apFaz, apSaz, apFez, apSez, apAnzMitarbeiter, apAufwand);
		tabelleArbeitspakete.getSortOrder().add(apId);
	}
	
	// Markieren des gewählten Arbeitspakets in der Anzeige-Tabelle
	private void markiereArbeitspaketInTabelle(Arbeitspaket ap) {
		tabelleArbeitspakete.getSelectionModel().select(ap);
		tabelleArbeitspakete.scrollTo(ap);
	}
}
