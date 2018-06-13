package reslearn.gui.controller;

import java.util.ArrayList;
import java.util.LinkedList;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.geometry.Orientation;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import reslearn.gui.DisplayCanvas;
import reslearn.gui.ResFeld;
import reslearn.gui.ViewUebungsmodus;
import reslearn.model.paket.ResEinheit;

public class Bearbeitungsfenster extends Pane {
	private Stage bearbeitungsmodus;
	private Scene scene;
	private LinkedList<ResFeld> resFeldListe;
	private boolean vertikal;

	private Label arbeitspaket;
	private Label hilfetext;
	private Button teilen;

	private Slider sliderX;
	private Slider sliderY;

	public Bearbeitungsfenster(ResFeld rect) {

		arbeitspaket = new Label(
				"Teile Arbeitspaket: " + rect.getResEinheit().getTeilpaket().getArbeitspaket().getIdIntern());
		arbeitspaket.setFont(new Font("Arial", DisplayCanvas.schriftGroesse));
		hilfetext = new Label("Wählen Sie die Pakete, die sie abtrennen möchten.");
		hilfetext.setFont(new Font("Arial", DisplayCanvas.schriftGroesse));

		teilen = new Button("Teile Arbeitspaket");
		teilen.setFont(new Font("Arial", DisplayCanvas.schriftGroesse));
		resFeldListe = new LinkedList<ResFeld>();

		sliderX = new Slider();
		sliderX.setStyle("-fx-font-size: " + DisplayCanvas.schriftGroesse);
		sliderX.setMinSize(rect.getResEinheit().getTeilpaket().getVorgangsdauer() * DisplayCanvas.resFeldBreite,
				DisplayCanvas.resFeldBreite);
		sliderX.setMaxSize(rect.getResEinheit().getTeilpaket().getVorgangsdauer() * DisplayCanvas.resFeldBreite,
				DisplayCanvas.resFeldBreite);
		sliderX.setMin(0);
		sliderX.setMax(rect.getResEinheit().getTeilpaket().getVorgangsdauer());
		sliderX.setShowTickMarks(true);
		sliderX.setShowTickLabels(true);
		sliderX.setMajorTickUnit(1);
		sliderX.setMinorTickCount(0);
		sliderX.setSnapToTicks(true);

		sliderY = new Slider();
		sliderY.setStyle("-fx-font-size: " + DisplayCanvas.schriftGroesse);

		sliderY.setOrientation(Orientation.VERTICAL);
		sliderY.setMinSize(DisplayCanvas.resFeldBreite,
				rect.getResEinheit().getTeilpaket().getMitarbeiteranzahl() * DisplayCanvas.resFeldBreite);
		sliderY.setMaxSize(DisplayCanvas.resFeldBreite,
				rect.getResEinheit().getTeilpaket().getMitarbeiteranzahl() * DisplayCanvas.resFeldBreite);

		sliderY.setMin(0);
		sliderY.setMax(rect.getResEinheit().getTeilpaket().getMitarbeiteranzahl());
		sliderY.setShowTickMarks(true);
		sliderY.setShowTickLabels(true);
		sliderY.setMajorTickUnit(1);
		sliderY.setMinorTickCount(0);
		sliderY.setSnapToTicks(true);

		arbeitspaket.setLayoutX(DisplayCanvas.canvasBreite / 51.3);
		arbeitspaket.setLayoutY(DisplayCanvas.canvasLaenge / 102.8);
		hilfetext.setLayoutX(DisplayCanvas.canvasBreite / 51.6);
		hilfetext.setLayoutY(DisplayCanvas.canvasLaenge / 17.1);
		sliderX.setLayoutX(DisplayCanvas.resFeldBreite);
		sliderX.setLayoutY(hilfetext.getLayoutY()
				+ (rect.getResEinheit().getTeilpaket().getMitarbeiteranzahl() + 2) * DisplayCanvas.resFeldBreite);
		sliderY.setLayoutX((rect.getResEinheit().getTeilpaket().getVorgangsdauer() + 2) * DisplayCanvas.resFeldBreite);
		sliderY.setLayoutY(hilfetext.getLayoutY() + DisplayCanvas.resFeldBreite);
		teilen.setLayoutX(DisplayCanvas.resFeldBreite);
		teilen.setLayoutY(sliderX.getLayoutY() + DisplayCanvas.resFeldBreite * 2);

		this.getChildren().addAll(arbeitspaket, hilfetext, teilen, sliderX, sliderY);
		scene = new Scene(this, DisplayCanvas.canvasLaenge / 2, teilen.getLayoutY() + 2 * DisplayCanvas.resFeldBreite);
		bearbeitungsmodus = new Stage();

		bearbeitungsmodus.initModality(Modality.WINDOW_MODAL);
		bearbeitungsmodus.initStyle(StageStyle.UTILITY);
		bearbeitungsmodus.initOwner(ViewUebungsmodus.getInstance().getStage());
		bearbeitungsmodus.setTitle("Arbeitspaket bearbeiten");
		bearbeitungsmodus.setScene(scene);
		bearbeitungsmodus.setX(DisplayCanvas.canvasBreite / 2);
		bearbeitungsmodus.setY(DisplayCanvas.canvasLaenge / 2);
		bearbeitungsmodus.show();

		/*
		 * geklicktes Teilpaket im Pop-Up nachzeichnen
		 */
		for (int i = 0; i < rect.getResEinheit().getTeilpaket().getVorgangsdauer(); i++) {
			for (int j = 0; j < rect.getResEinheit().getTeilpaket().getMitarbeiteranzahl(); j++) {
				ResFeld dummy = new ResFeld(i * DisplayCanvas.resFeldBreite + DisplayCanvas.resFeldBreite,
						j * DisplayCanvas.resFeldLaenge + (hilfetext.getLayoutY() + DisplayCanvas.resFeldBreite),
						rect.getResEinheit());
				dummy.setFill(rect.getFill());
				// dummy.setStroke(Color.GRAY);
				getChildren().add(dummy);

			}
		}

		sliderY.valueProperty().addListener(new ChangeListener<Number>() {

			/**
			 * Bei ganzzahligem Wertwechsel des Sliders werden die jeweiligen
			 * ResEinheiten/Felder des Teilpaketes markiert, die beim Bestätigen des
			 * Teilen-Buttons abgetrennt werden würden. Der Wert des Sliders entspricht den
			 * Zeilen die abgetrennt werden.
			 */
			@Override
			public void changed(ObservableValue<? extends Number> arg0, Number arg1, Number neuerWert) {
				if (neuerWert.doubleValue() % neuerWert.intValue() == 0 || neuerWert.intValue() == 0) {

					int markierteResFelder = 0;
					for (ResFeld feld : resFeldListe) {
						getChildren().remove(feld);
					}
					resFeldListe.clear();

					for (int i = rect.getResEinheit().getTeilpaket().getMitarbeiteranzahl(); i > 0; i--) {
						for (int j = 0; j < rect.getResEinheit().getTeilpaket().getVorgangsdauer(); j++) {
							if (markierteResFelder < neuerWert.intValue()
									* rect.getResEinheit().getTeilpaket().getVorgangsdauer()) {
								ResFeld dummy = new ResFeld(
										j * DisplayCanvas.resFeldBreite + DisplayCanvas.resFeldBreite,
										i * DisplayCanvas.resFeldLaenge
												+ (hilfetext.getLayoutY() + DisplayCanvas.resFeldBreite)
												- DisplayCanvas.resFeldLaenge,
										rect.getResEinheit().getTeilpaket().getResEinheitListe()
												.get(markierteResFelder));
								dummy.setStroke(rect.getFill());
								dummy.getResEinheit().setTeilpaket(rect.getResEinheit().getTeilpaket());
								resFeldListe.add(dummy);
								getChildren().add(dummy);
								markierteResFelder++;
							} else {
								break;
							}
						}
					}
				}
			}
		});

		sliderY.setOnMousePressed(new EventHandler<MouseEvent>() {

			/**
			 * Um dem Nutzer zu verdeutlichen, welcher Slider "aktiv" ist bzw. ob
			 * horizontal/vertikal abgetrennt wird, wird nur der zuletzt angeklickte Slider
			 * mit Opacity (1) angezeigt. Der jeweils andere Slider ist ausgegraut.
			 */
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

					int counter = 0;
					for (ResFeld feld : resFeldListe) {
						getChildren().remove(feld);
					}
					resFeldListe.clear();

					for (int i = 0; i < rect.getResEinheit().getTeilpaket().getVorgangsdauer(); i++) {
						for (int j = 0; j < rect.getResEinheit().getTeilpaket().getMitarbeiteranzahl(); j++) {
							if (counter < neuerWert.intValue()
									* rect.getResEinheit().getTeilpaket().getMitarbeiteranzahl()) {
								ResFeld dummy = new ResFeld(
										i * DisplayCanvas.resFeldBreite + DisplayCanvas.resFeldBreite,
										j * DisplayCanvas.resFeldLaenge
												+ (hilfetext.getLayoutY() + DisplayCanvas.resFeldBreite),
										rect.getResEinheit().getTeilpaket().getResEinheitListe()
												.get((j * rect.getResEinheit().getTeilpaket().getVorgangsdauer()) + i));
								dummy.setStroke(Color.GREY);
								dummy.getResEinheit().setTeilpaket(rect.getResEinheit().getTeilpaket());
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

		sliderX.setOnMousePressed(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent e) {
				sliderY.setOpacity(0.4);
				sliderX.setOpacity(1);
				vertikal = true;
			}
		});

		/**
		 * Beim Klicken auf den Teilen-Button werden die markierten ResEinheiten/Felder
		 * abgetrennt und einem neuen Teilpaket hinzugefügt. Danach schließt sich das
		 * Bearbeitungsfenster und beide Teilpakete können unabhängig voneinander bewegt
		 * werden.
		 */
		teilen.setOnMouseClicked(new EventHandler<MouseEvent>() {

			@Override
			public void handle(MouseEvent arg0) {
				ArrayList<ResEinheit> neueResEinheitListe = new ArrayList<>();
				for (ResFeld resFeld : resFeldListe) {
					neueResEinheitListe.add(resFeld.getResEinheit());
				}
				if (neueResEinheitListe.size() != 0) {
					if (vertikal) {
						rect.getResEinheit().getTeilpaket().trenneTeilpaketVertikal(neueResEinheitListe);
					} else {
						rect.getResEinheit().getTeilpaket().trenneTeilpaketHorizontal(neueResEinheitListe);
					}
				}
				bearbeitungsmodus.close();

			}
		});
	}
}
