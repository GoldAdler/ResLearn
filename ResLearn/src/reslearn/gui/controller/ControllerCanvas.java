package reslearn.gui.controller;

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
import javafx.scene.input.ContextMenuEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Callback;
import javafx.util.Pair;
import reslearn.gui.Diagramm;
import reslearn.gui.ResFeld;
import reslearn.gui.View;
import reslearn.model.paket.Teilpaket;
import reslearn.model.resCanvas.ResCanvas;

public class ControllerCanvas {

	double zeigerX, zeigerY;
	double translateX, translateY;
	double altePositionX, altePositionY;
	double newTranslateX, newTranslateY;
	private static int verschiebungX, verschiebungY;
	private static double merkeX, merkeY;
	Teilpaket teilpaketClicked;
	ResFeld feld, rect;
	ColorPicker colorPicker;
	Rectangle bound;
	private ResCanvas resCanvas;
	public static TableView<Pair<String, Object>> table = new TableView<>();
	ObservableList<Pair<String, Object>> data;

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
		@SuppressWarnings("unchecked")
		@Override
		public void handle(MouseEvent e) {

			zeigerX = e.getSceneX();
			zeigerY = e.getSceneY();

			rect = (ResFeld) e.getSource();
			teilpaketClicked = rect.getResEinheit().getTeilpaket();

			// bound = rect.getTeilpaketBounds(teilpaketClicked);
			// bound.setFill(Color.WHITE.deriveColor(1, 1, 1, 0.2));
			// View.pane.getChildren().add(bound);

			translateX = rect.getTranslateX();
			translateY = rect.getTranslateY();

			altePositionX = rect.getBoundsInParent().getMinX();
			altePositionY = rect.getBoundsInParent().getMinY();
			System.out.println("PosaltX: " + altePositionX);
			System.out.println("i: " + rect.getResEinheit().getPosition().getxKoordinate() + " j: "
					+ rect.getResEinheit().getPosition().getyKoordinate() + "\n");

			verschiebungX = 0;
			verschiebungY = 0;

			// Erstellen der Informationsleiste links
			data = FXCollections.observableArrayList(
					pair("Arbeitspaket", rect.getTeilpaket().getArbeitspaket().getId()), pair("Farbe", rect.getFill()),
					pair("FAZ", rect.getTeilpaket().getArbeitspaket().getFaz()),
					pair("FEZ", rect.getTeilpaket().getArbeitspaket().getFez()),
					pair("SAZ", rect.getTeilpaket().getArbeitspaket().getSaz()),
					pair("SEZ", rect.getTeilpaket().getArbeitspaket().getSez()),
					pair("Vorgangsdauer", rect.getTeilpaket().getArbeitspaket().getVorgangsdauer()),
					pair("Mitarbeiter", rect.getTeilpaket().getArbeitspaket().getMitarbeiteranzahl()),
					pair("Aufwand", rect.getTeilpaket().getArbeitspaket().getAufwand()));

			table.setItems(data);

		}
	};

	// Event Handler Maus ziehen
	EventHandler<MouseEvent> OnMouseDraggedEventHandler = new EventHandler<MouseEvent>() {
		@Override
		public void handle(MouseEvent e) {

			// 1. nur ein ResFeld (A) verschieben
			// 2. die anderen ResFeld müssen sich an dem ersten Resfeld orientieren
			// des offset von a auf andere übetragen
			// 3. Offset muss gemerkt werden ( falls etwas rückgängig gemacht werden muss)

			// Überprüfung ( logische verschieben abgschlossen, aber noch nicht in der Gui
			// dargestellt)

			// Überpüfung boundries
			// füre jede resEinheit (for
			// ist resG.getBoundsInParent() ist es außerhabl der Boundries
			// Dann mit dem gemekrten offstet (3.) die operation wieder rückgängig

			// übeprüfung kollision mit anderen Paketen

			double neuePositionX = e.getSceneX();
			double neuePositionY = e.getSceneY();

			int differenzX = (int) ((neuePositionX - zeigerX) / 20); // gibt immer die Zahl zurück, um die das
			// Klötzchen verschoben werden soll
			int differenzY = (int) ((neuePositionY - zeigerY) / 20); // Bsp. 440.0 - 420.0 -> 20.0/20.0 = 1

			if (differenzX != 0 || differenzY != 0) {
				System.out.println();
			}

			if (differenzX > 1 || differenzY != 0) {
				System.out.println();
			}

			// differenzX -= differenzX % 20;
			// differenzY -= differenzY % 20;

			boolean verschiebbar = false;

			// Verschiebung auf der X-Achse bewirkt logisches Verschieben im
			// Koordinatensystem

			System.out.println("zeigerX: " + zeigerX);
			System.out.println("zeigerY: " + zeigerY);
			System.out.println("neuePositionX: " + neuePositionX);
			System.out.println("neuePositionY: " + neuePositionY);
			System.out.println("DifferenzX: " + differenzX);
			System.out.println("DifferenzY: " + differenzY);
			System.out.println("VerschiebungX: " + verschiebungX);
			System.out.println("VerschiebungY: " + verschiebungY);

			if (differenzX > verschiebungX) {
				verschiebungX = differenzX;
				// differenzX--;
				System.out.println("DifferenzX: " + differenzX);
				verschiebbar = rect.getTeilpaket().bewegeX(resCanvas, 1);

				System.out.println(verschiebbar);
				System.out.println("" + differenzX + " > " + verschiebungX);
				System.out.println("i: " + rect.getResEinheit().getPosition().getxKoordinate() + " j: "
						+ rect.getResEinheit().getPosition().getyKoordinate() + "\n");
			}
			if (differenzX < verschiebungX) {
				verschiebungX = differenzX;
				// differenzX++;
				System.out.println("DifferenzX: " + differenzX);
				verschiebbar = rect.getTeilpaket().bewegeX(resCanvas, -1);

				System.out.println(verschiebbar);
				System.out.println("" + differenzX + " < " + verschiebungX);
				System.out.println("i: " + rect.getResEinheit().getPosition().getxKoordinate() + " j: "
						+ rect.getResEinheit().getPosition().getyKoordinate() + "\n");
			}
			// if (differenzX == 0) {
			// System.out.println("DifferenzX: " + 0);
			// verschiebbar = rect.getTeilpaket().bewegeX(resCanvas, 0);
			// System.out.println("i: " +
			// rect.getResEinheit().getPosition().getxKoordinate() + " j: "
			// + rect.getResEinheit().getPosition().getyKoordinate() + "\n");
			// }

			if (verschiebbar) {
				System.out.println("X if-Verschiebbar" + verschiebbar);
				double offsetX = e.getSceneX() - zeigerX;
				newTranslateX = translateX + offsetX;
				newTranslateX -= newTranslateX % 20;
				bewegeX();

			}

			// Verschiebung auf der Y-Achse bewirkt logisches Verschieben im
			// Koordinatensystem

			verschiebbar = false;

			if (differenzY > verschiebungY) {
				verschiebungY = differenzY;
				System.out.println("DifferenzY: " + differenzY);
				verschiebbar = rect.getTeilpaket().bewegeY(resCanvas, -1);
				// differenzY--;
				System.out.println(verschiebbar);
				System.out.println("" + differenzY + " > " + verschiebungY);
				System.out.println("i: " + rect.getResEinheit().getPosition().getxKoordinate() + " j: "
						+ rect.getResEinheit().getPosition().getyKoordinate() + "\n");
			}
			if (differenzY < verschiebungY) {
				verschiebungY = differenzY;
				System.out.println("DifferenzY: " + differenzY);
				verschiebbar = rect.getTeilpaket().bewegeY(resCanvas, 1);
				System.out.println(verschiebbar);
				System.out.println("" + differenzY + " < " + verschiebungY);
				System.out.println("i: " + rect.getResEinheit().getPosition().getxKoordinate() + " j: "
						+ rect.getResEinheit().getPosition().getyKoordinate() + "\n");
			}
			// if (differenzY == 0) {
			// System.out.println("DifferenzY: " + 0);
			// verschiebbar = rect.getTeilpaket().bewegeY(resCanvas, 0);
			// System.out.println("i: " +
			// rect.getResEinheit().getPosition().getxKoordinate() + " j: "
			// + rect.getResEinheit().getPosition().getyKoordinate() + "\n");
			// }

			if (verschiebbar) {
				System.out.println("Y if-Verschiebbar" + verschiebbar);
				double offsetY = e.getSceneY() - zeigerY;
				newTranslateY = translateY + offsetY;
				newTranslateY -= newTranslateY % 20;
				bewegeY();
			}

		}
	};

	public void bewege() {
		for (ResFeld[] resAr : Diagramm.res) {
			for (ResFeld teilpaket : resAr) {
				if (teilpaket != null) {
					if (teilpaketClicked == teilpaket.getResEinheit().getTeilpaket()) {
						teilpaket.setTranslateX(newTranslateX);
						teilpaket.setTranslateY(newTranslateY);
					}
				}
			}
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
			Label arbeitspaket = new Label("neues Arbeitspaket: A2");
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

			for (int i = 0; i < rect.getTeilpaket().getArbeitspaket().getVorgangsdauer(); i++) {
				for (int j = 0; j < rect.getTeilpaket().getArbeitspaket().getMitarbeiteranzahl(); j++) {
					ResFeld dummy = new ResFeld(i * 20 + 65, j * 20 + 65, 20, 20);
					dummy.setFill(rect.getFill());
					// dummy.setStroke(Color.GRAY);
					pane.getChildren().add(dummy);

					dummy.setOnMouseClicked(new EventHandler<MouseEvent>() {
						@Override
						public void handle(MouseEvent e) {
							dummy.setStroke(Color.GRAY);

							for (int a = 0; a < rect.getTeilpaket().getAufwand(); a++) {
								resFeldListe.add(dummy);

							}

						}
					});

				}
			}
			sliderY.setOnMousePressed(new EventHandler<MouseEvent>() {
				public void handle(MouseEvent e) {
					sliderX.setOpacity(0.4);
					sliderY.setOpacity(1);
				}
			});

			sliderX.valueProperty().addListener(new ChangeListener<Number>() {

				@Override
				public void changed(ObservableValue<? extends Number> arg0, Number arg1, Number arg2) {
					if (arg2.doubleValue() % arg2.intValue() == 0 || arg2.intValue()==0) {
						
						System.out.println("CHANGE" + arg1 + " " + arg2);
						int counter = 0;
						for (ResFeld feld : resFeldListe) {
							pane.getChildren().remove(feld);
						}
						resFeldListe.clear();

						
						for (int i = 0; i < rect.getTeilpaket().getArbeitspaket().getVorgangsdauer(); i++) {
							for (int j = 0; j < rect.getTeilpaket().getArbeitspaket().getMitarbeiteranzahl(); j++) {
								if (counter < arg2.intValue()*rect.getTeilpaket().getMitarbeiteranzahl()) {
									System.out.println("FELD ANMALEN");
									ResFeld dummy = new ResFeld(i * 20 + 65, j * 20 + 65, 20, 20);
									dummy.setFill(Color.GREY);
									resFeldListe.add(dummy);
									pane.getChildren().add(dummy);
									counter++;
								}else {
									break;
								}
							}
						}
					}
				}
			});

			sliderX.setOnMousePressed(new EventHandler<MouseEvent>() {
				public void handle(MouseEvent e) {
					sliderY.setOpacity(0.4);
					sliderX.setOpacity(1);
				}
			});

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
		}
	};

	//////////////////////////////////////////////////////////////////////////////////
	// Erstellung der unterschiedlichen Datentypen für die Tabelle links //
	//////////////////////////////////////////////////////////////////////////////////

	@SuppressWarnings("unchecked")
	public void erstelleTabelle() {

		table.setEditable(true);
		table.setLayoutX(15);
		table.setLayoutY(200);
		table.setPrefSize(200, 300);

		TableColumn<Pair<String, Object>, String> name = new TableColumn<>("Name");
		name.setCellValueFactory(new PairKeyFactory());
		name.setMinWidth(99);
		name.setSortable(false);

		TableColumn<Pair<String, Object>, Object> wert = new TableColumn<>("Wert");
		wert.setCellValueFactory(new PairValueFactory());
		wert.setMinWidth(99);
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
					colorPicker.setStyle("-fx-color-label-visible: false ;");
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
}
