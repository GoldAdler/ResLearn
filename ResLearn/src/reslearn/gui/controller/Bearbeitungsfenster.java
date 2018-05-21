package reslearn.gui.controller;

import java.util.ArrayList;
import java.util.LinkedList;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import reslearn.gui.DisplayCanvas;
import reslearn.gui.ResFeld;
import reslearn.gui.View;
import reslearn.model.paket.ResEinheit;

public class Bearbeitungsfenster extends Pane {
	private Stage bearbeitungsmodus;
	private Scene scene;
	LinkedList<ResFeld> resFeldListe;
	private boolean vertikal;

	private Label arbeitspaket;
	private Label hilfetext;
	private Button teilen;

	private Slider sliderX;
	private Slider sliderY;
	View view = new View();

	public Bearbeitungsfenster(ResFeld rect) {

		arbeitspaket = new Label("Teile Arbeitspaket: " + rect.getResEinheit().getTeilpaket().getArbeitspaket().getId());
		hilfetext = new Label("Wählen Sie die Pakete, die sie abtrennen möchten.");
		teilen = new Button("Teile Arbeitspaket");

		resFeldListe = new LinkedList<ResFeld>();

		sliderX = new Slider();
		sliderX.setMin(0);
		sliderX.setMax(rect.getResEinheit().getTeilpaket().getVorgangsdauer());
		sliderX.setShowTickLabels(true);
		sliderX.setMajorTickUnit(1);
		sliderX.setMinorTickCount(0);
		sliderX.setSnapToTicks(true);

		sliderY = new Slider();
		sliderY.setMaxWidth(120);
		sliderY.setRotate(270);
		sliderY.setMin(0);
		sliderY.setMax(rect.getResEinheit().getTeilpaket().getMitarbeiteranzahl());
		sliderY.setShowTickLabels(true);
		sliderY.setMajorTickUnit(1);
		sliderY.setMinorTickCount(0);
		sliderY.setSnapToTicks(true);

		this.getChildren().addAll(arbeitspaket, hilfetext, teilen, sliderX, sliderY);
		scene = new Scene(this, 300, 250);
		bearbeitungsmodus = new Stage();

		bearbeitungsmodus.initModality(Modality.WINDOW_MODAL);
		bearbeitungsmodus.initStyle(StageStyle.UTILITY);
		bearbeitungsmodus.initOwner(view.classStage);
		bearbeitungsmodus.setTitle("Arbeitspaket bearbeiten");
		bearbeitungsmodus.setScene(scene);
		bearbeitungsmodus.setX(view.classStage.getWidth() / 2);
		bearbeitungsmodus.setY(view.classStage.getHeight() / 2);
		bearbeitungsmodus.show();

		/*
		 * geklicktes Teilpaket im Pop-Up nachzeichnen
		 */
		for (int i = 0; i < rect.getResEinheit().getTeilpaket().getVorgangsdauer(); i++) {
			for (int j = 0; j < rect.getResEinheit().getTeilpaket().getMitarbeiteranzahl(); j++) {
				ResFeld dummy = new ResFeld(i * DisplayCanvas.resFeldBreite + 65, j * DisplayCanvas.resFeldLaenge + 65, rect.getResEinheit());
				dummy.setFill(rect.getFill());
				// dummy.setStroke(Color.GRAY);
				getChildren().add(dummy);

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
						getChildren().remove(feld);
					}
					resFeldListe.clear();

					// Wert des Sliders entspricht der Anzahl der Spalten die abgeschnitten werden
					// sollen
					for (int i = rect.getResEinheit().getTeilpaket().getMitarbeiteranzahl(); i > 0; i--) {
						for (int j = 0; j < rect.getResEinheit().getTeilpaket().getVorgangsdauer(); j++) {
							if (counter < neuerWert.intValue() * rect.getResEinheit().getTeilpaket().getVorgangsdauer()) {
								System.out.println("FELD ANMALEN");
								ResFeld dummy = new ResFeld(j * DisplayCanvas.resFeldBreite + 65, i * DisplayCanvas.resFeldLaenge + 45, rect.getResEinheit());
								dummy.setStroke(rect.getFill());
								dummy.getResEinheit().setTeilpaket(rect.getResEinheit().getTeilpaket());
								dummy.setResEinheit(rect.getResEinheit().getTeilpaket().getResEinheitListe().get(counter));
								resFeldListe.add(dummy);
								getChildren().add(dummy);
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
						getChildren().remove(feld);
					}
					resFeldListe.clear();

					for (int i = 0; i < rect.getResEinheit().getTeilpaket().getVorgangsdauer(); i++) {
						for (int j = 0; j < rect.getResEinheit().getTeilpaket().getMitarbeiteranzahl(); j++) {
							if (counter < neuerWert.intValue() * rect.getResEinheit().getTeilpaket().getMitarbeiteranzahl()) {
								System.out.println("FELD ANMALEN");
								ResFeld dummy = new ResFeld(i * DisplayCanvas.resFeldBreite + 65, j * DisplayCanvas.resFeldLaenge + 65, rect.getResEinheit());
								dummy.setStroke(Color.GREY);
								dummy.getResEinheit().setTeilpaket(rect.getResEinheit().getTeilpaket());
								dummy.setResEinheit(rect.getResEinheit().getTeilpaket().getResEinheitListe()
										.get((j * rect.getResEinheit().getTeilpaket().getVorgangsdauer()) + i));
								System.out.println();
								resFeldListe.add(dummy);
								getChildren().add(dummy);
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
						int vorgangsdauer = resFeldListe.size() / rect.getResEinheit().getTeilpaket().getMitarbeiteranzahl();
						System.out.println("Vorgangsdauer: " + vorgangsdauer);

						// rect.getTeilpaket().trenneTeilpaketVertikal(neueResEinheitListe,
						// vorgangsdauer);
						rect.getResEinheit().getTeilpaket().trenneTeilpaketVertikal(neueResEinheitListe);
						System.out.println("vertikal");
					} else {
						rect.getResEinheit().getTeilpaket().trenneTeilpaketHorizontal(neueResEinheitListe);
						System.out.println("horizontal");
					}
				}
				bearbeitungsmodus.close();

			}
		});
	}
}
