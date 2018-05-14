package reslearn.gui.controller;

import java.io.IOException;

import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.image.ImageView;
import javafx.scene.input.ContextMenuEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Callback;
import javafx.util.Pair;
import reslearn.gui.ResFeld;
import reslearn.gui.Diagramm;
import reslearn.gui.View;

public class ControllerCanvas extends Controller{

	double zeigerX, zeigerY;
	double translateX, translateY;
	String nameClicked;
	ResFeld feld;
	ResFeld rect;
	ColorPicker colorPicker;
	public static TableView<Pair<String, Object>> table = new TableView<>();
	ObservableList<Pair<String, Object>> data;


	public void makeDraggable(ResFeld feld) {
		this.feld = feld;
		feld.setOnMousePressed(OnMousePressedEventHandler);
		feld.setOnMouseDragged(OnMouseDraggedEventHandler);
		feld.setOnContextMenuRequested(OnMouseSecondaryEventHandler);
		feld.setOnMouseClicked(OnMouseClickedEventHandler);
	}

	//Event Handler Maus klicken
	EventHandler<MouseEvent> OnMousePressedEventHandler = new EventHandler<MouseEvent>() {
		@Override
		public void handle(MouseEvent e) {

			zeigerX = e.getSceneX();
			zeigerY = e.getSceneY();

			rect = (ResFeld) e.getSource();
			nameClicked = rect.getResEinheit().getTeilpaket().getArbeitspaket().getId();

			translateX = rect.getTranslateX();
			translateY = rect.getTranslateY();

		}
	};
	//Event Handler Maus ziehen
	EventHandler<MouseEvent> OnMouseDraggedEventHandler = new EventHandler<MouseEvent>() {
		@Override
		public void handle(MouseEvent e) {

			double offsetX = e.getSceneX() - zeigerX;
			double offsetY = e.getSceneY() - zeigerY;

			double newTranslateX = translateX + offsetX;
			double newTranslateY = translateY + offsetY;

			newTranslateX -= newTranslateX % 20;
			newTranslateY -= newTranslateY % 20;

			for (ResFeld[] resAr : Diagramm.res) {
				for (ResFeld resG : resAr) {
					if (resG != null) {
						if (nameClicked == resG.getResEinheit().getTeilpaket().getArbeitspaket().getId()) {
							// if(resG.getBoundsInParent().getMinX() < 0)
							// resG.setTranslateX(newTranslateX);

							resG.setTranslateX(newTranslateX);
							resG.setTranslateY(newTranslateY);

						}
					}
				}
			}
		}
	};

	// Zeige KontextMenü mit Rechtsklick
	EventHandler<ContextMenuEvent> OnMouseSecondaryEventHandler = new EventHandler<ContextMenuEvent>() {
		@Override
		public void handle(ContextMenuEvent e) {
			View.menu.show(feld, e.getSceneX(), e.getSceneY());
		}
	};

	EventHandler<ActionEvent> OnMenuItemApEventHandler = new EventHandler<ActionEvent>() {
		@Override
		public void handle(ActionEvent e) {
			// teile Arbeitspaket
		}
	};

	//////////////////////////////////////////////////////////////////////////////////
	// Erstellung der unterschiedlichen Datentypen für die Tabelle links //
	//////////////////////////////////////////////////////////////////////////////////

	EventHandler<MouseEvent> OnMouseClickedEventHandler = new EventHandler<MouseEvent>() {
		@SuppressWarnings("unchecked")
		@Override
		public void handle(MouseEvent e) {

			rect = (ResFeld) e.getSource();

			data = FXCollections.observableArrayList(
					pair("Arbeitspaket", rect.getTeilpaket().getArbeitspaket().getId()),
					pair("Farbe", rect.getFill()), 
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

	public void erstelleTabelle() {

		table.setEditable(true);
		table.setLayoutX(15);
		table.setLayoutY(85);
		table.setPrefSize(200, 300);

		TableColumn<Pair<String, Object>, String> name = new TableColumn<>("Name");
		name.setCellValueFactory(new PairKeyFactory());
		name.setMinWidth(98);
		name.setSortable(false);

		TableColumn<Pair<String, Object>, Object> wert = new TableColumn<>("Wert");
		wert.setCellValueFactory(new PairValueFactory());
		wert.setMinWidth(98);
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
		@SuppressWarnings("unchecked")
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
							if (nameClicked == resG.getResEinheit().getTeilpaket().getArbeitspaket().getId()) {
								resG.setFill(colorPicker.getValue());
							}
						}
					}
				}
			}
		});
	}

}
