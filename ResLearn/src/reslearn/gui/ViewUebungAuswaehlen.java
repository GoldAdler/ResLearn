package reslearn.gui;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;

import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Orientation;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollBar;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import reslearn.gui.ImportExport.AufgabeLadenImport;
import reslearn.gui.controller.Controller;
import reslearn.gui.controller.ControllerModusAuswaehlen;
import reslearn.gui.controller.ControllerUebungsmodus;

public class ViewUebungAuswaehlen extends Application {

	private Pane pane;
	File f = new File("./uebungen");
	public File[] fileArray = f.listFiles(new FilenameFilter() {
		@Override
		public boolean accept(File f, String name) {
			return name.toLowerCase().endsWith(".csv");
		}
	});
	private static ViewUebungAuswaehlen view;

	public static ViewUebungAuswaehlen getInstance() {
		if (view == null) {
			view = new ViewUebungAuswaehlen();
		}
		return view;
	}

	@Override
	public void start(Stage stage) throws Exception {
		// Lade FXML

		Parent root = FXMLLoader.load(getClass().getResource("/reslearn/gui/fxml/UebungAuswaehlen.fxml"));

		Group group = new Group();
		pane = erstellePane();
		pane.setLayoutY(DisplayCanvas.StartpunktPaneUebungAuswaehlen);
		ScrollBar scrolli = new ScrollBar();
		if (fileArray.length <= 10) {
			scrolli.setVisible(false);
		}
		scrolli.setOrientation(Orientation.VERTICAL);
		scrolli.setLayoutX(DisplayCanvas.paneAufgabeLadenBreite - scrolli.getWidth());
		scrolli.setLayoutY(DisplayCanvas.hoeheUeberschrift);
		scrolli.setMinHeight(DisplayCanvas.scrolliHoehe);
		scrolli.valueProperty().addListener(new ChangeListener<Number>() {
			@Override
			public void changed(ObservableValue<? extends Number> ov, Number old_val, Number new_val) {
				pane.setLayoutY(-new_val.doubleValue() * ((Math.ceil(fileArray.length / 5.0) - 2.0) * 3.2));
				pane.getParent().toBack();
			}
		});
		Scene hauptszene = new Scene(root);
		group.getChildren().addAll(pane, scrolli);

		Scene unterszene = new Scene(group);

		((Pane) hauptszene.getRoot()).getChildren().add(unterszene.getRoot());
		stage.setMaximized(true);
		stage.setScene(hauptszene);
		stage.setTitle("ResLearn");
		stage.show();

	}

	public Button dateiname;
	public String datei;

	/**
	 * In dieser Methode werden je nachdem, wie viele Aufgaben in dem vorgegebenen
	 * Ordner vorhanden sind, dementsprechend viele Buttons dynamisch generiert und
	 * auf der Pane positioniert.
	 *
	 * @return
	 */
	public Pane erstellePane() {

		pane = new Pane();
		int buttonXPosition = 0;
		int buttonYPosition = 0;

		for (int i = 0; i < fileArray.length; i++) {
			Button b = new Button(fileArray[i].getName().substring(0, fileArray[i].getName().length() - 4));
			b.setId(fileArray[i].getName());
			b.setOnAction(ButtonAction);
			if (i % 5 == 0 && i != 0) {
				buttonYPosition += DisplayCanvas.abstandButtonY;
				buttonXPosition = 0;
			}
			b.setStyle("-fx-font:" + DisplayCanvas.schriftGroesse + " Arial;");
			b.setPrefHeight(DisplayCanvas.buttonHoehe);
			b.setPrefWidth(DisplayCanvas.buttonBreite);
			b.setLayoutX(DisplayCanvas.buttonXStart + buttonXPosition);
			b.setLayoutY(DisplayCanvas.buttonYStart + buttonYPosition);

			pane.getChildren().add(b);
			buttonXPosition += DisplayCanvas.abstandButtonX + DisplayCanvas.buttonBreite;

		}
		return pane;
	}

	// Event Handler Maus klicken
	private EventHandler<ActionEvent> ButtonAction = new EventHandler<ActionEvent>() {

		@Override
		public void handle(ActionEvent event) {
			weiter(event);

		}
	};

	public void weiter(ActionEvent event) {
		Scene newScene;
		Parent root;
		AufgabeLadenImport ali = new AufgabeLadenImport();
		Controller.alleFenster.add("/reslearn/gui/fxml/UebungAuswaehlen.fxml");
		try {
			FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/reslearn/gui/fxml/ModusAuswaehlen.fxml"));
			root = fxmlLoader.load();
			newScene = new Scene(root);
			ControllerModusAuswaehlen controller = fxmlLoader.<ControllerModusAuswaehlen>getController();
			dateiname = (Button) event.getSource();
			datei = dateiname.getId();
			ControllerUebungsmodus.letztesArbeitspaket = ali.aufgabeLaden(f + File.separator + datei);
			controller.initialize(ali.aufgabeLaden(f + File.separator + datei));
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

	public static void main(String[] args) {
		launch(args);
	}

}
