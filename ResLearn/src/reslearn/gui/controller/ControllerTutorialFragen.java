package reslearn.gui.controller;

import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import reslearn.gui.rescanvas.DisplayCanvas;

public class ControllerTutorialFragen extends Controller {

	private static ControllerTutorialFragen controllerTutorialFragen;

	public static ControllerTutorialFragen getInstance() {
		if (controllerTutorialFragen == null) {
			controllerTutorialFragen = new ControllerTutorialFragen();
		}
		return controllerTutorialFragen;
	}

	private static int counter = 0;
	private int counterFrageAntwort = 1;
	private int counterRichtigeAntworten = 0;
	private final int counterAlleFragen = 4;

	private Button zurueck = new Button();

	@FXML
	private Button home;

	private Label labelErgebnis;

	private GridPane gridpane;

	private ToggleGroup tg;
	private Label label;
	private RadioButton rb1 = new RadioButton();
	private RadioButton rb2 = new RadioButton();
	private RadioButton rb3 = new RadioButton();
	private RadioButton rb4 = new RadioButton();
	private Button weiter = new Button("Weiter");;

	public void start(Stage stage) throws Exception {
		selektieren();
		counter = 0;
		counterFrageAntwort = 1;
		counterRichtigeAntworten = 0;

		Parent root = FXMLLoader.load(getClass().getResource("/reslearn/gui/fxml/Quiz.fxml"));

		Scene hauptszene = new Scene(root);

		zurueck.setLayoutX(7);
		zurueck.setLayoutY(6);
		zurueck.setPrefHeight(35);
		zurueck.setPrefWidth(46);
		zurueck.setOnAction(zurueckAction);
		zurueck.setOpacity(0);

		Group group = new Group();
		rb1.setFont(new Font("Arial", DisplayCanvas.schriftGroesse));
		rb2.setFont(new Font("Arial", DisplayCanvas.schriftGroesse));
		rb3.setFont(new Font("Arial", DisplayCanvas.schriftGroesse));
		rb4.setFont(new Font("Arial", DisplayCanvas.schriftGroesse));

		tg = new ToggleGroup();
		gridpane = new GridPane();
		label = new Label();
		labelErgebnis = new Label();

		rb1.setText("Termin- und kapazitätstreue Bedarfsoptimierung");
		rb1.setUserData(rb1.getText());
		rb1.setToggleGroup(tg);

		rb2.setText("Termin- und planungstreue Bedarfsoptimierung");
		rb2.setUserData(rb2.getText());
		rb2.setToggleGroup(tg);

		rb3.setText("Planungs- und kapazitätstreue Bedarfsoptimierung");
		rb3.setUserData(rb3.getText());
		rb3.setToggleGroup(tg);

		rb4.setText("Termin-, planungs- und kapazitätstreue Bedarfsoptimierung");
		rb4.setUserData(rb4.getText());
		rb4.setToggleGroup(tg);

		gridpane.add(rb1, 0, 0);
		gridpane.add(rb2, 0, 1);
		gridpane.add(rb3, 0, 2);
		gridpane.add(rb4, 0, 3);
		gridpane.setLayoutX(DisplayCanvas.canvasStartpunktX);
		gridpane.setLayoutY(DisplayCanvas.canvasStartpunktY);
		gridpane.setPrefHeight(55);

		label.setLayoutX(DisplayCanvas.canvasStartpunktX);
		label.setLayoutY(DisplayCanvas.canvasStartpunktY - 2 * DisplayCanvas.resFeldBreite);
		label.setFont(new Font("Arial", DisplayCanvas.schriftGroesse + 5));
		label.setText("Welche Bedarfsoptimierungsverfahren gibt es?");

		weiter.setOnAction(ButtonAction);
		weiter.setLayoutX(DisplayCanvas.canvasStartpunktX + DisplayCanvas.canvasBreite / 3);
		weiter.setLayoutY(gridpane.getLayoutY() + gridpane.getPrefHeight() + DisplayCanvas.resFeldBreite);

		labelErgebnis.setLayoutX(DisplayCanvas.canvasStartpunktX);
		labelErgebnis.setLayoutY(weiter.getLayoutY() + DisplayCanvas.resFeldBreite * 2);
		labelErgebnis.setFont(new Font("Arial", DisplayCanvas.schriftGroesse + 5));

		group.getChildren().addAll(gridpane, label, labelErgebnis, weiter, zurueck);
		Scene unterszene = new Scene(group);
		((Pane) hauptszene.getRoot()).getChildren().add(unterszene.getRoot());
		stage.setMaximized(true);
		stage.setScene(hauptszene);
		stage.setTitle("ResLearn");
		stage.show();
	}

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

	private EventHandler<ActionEvent> zurueckAction = new EventHandler<ActionEvent>() {

		@Override
		public void handle(ActionEvent event) {
			try {
				zurueck(event);
			} catch (Exception e) {
				e.printStackTrace();
			}

		}
	};

	public void zurueck(ActionEvent event) throws Exception {
		if (counter <= 0) {
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
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			frage(--counter);
		}
	}

	private EventHandler<ActionEvent> ButtonAction = new EventHandler<ActionEvent>() {

		@Override
		public void handle(ActionEvent event) {
			weiter(event);

		}
	};

	public void weiter(ActionEvent event) {
		if (counter < counterAlleFragen) {
			if (counter == 0) {
				antwort(counter);
				counter += 1;
			} else {
				if (counterFrageAntwort == 1) {
					frage(counter);
					counterFrageAntwort = 2;
				} else {
					antwort(counter);
					counterFrageAntwort = 1;
					counter++;
				}
			}
		} else if (counter == counterAlleFragen) {
			counter++;
			if (counterRichtigeAntworten > 2) {
				labelErgebnis.setText(
						"Es wurden " + counterRichtigeAntworten + " von 4 Fragen richtig beantwortet.\nKlasse!!");
			} else {
				labelErgebnis.setText(
						"Es wurden " + counterRichtigeAntworten + " von 4 Fragen richtig beantwortet.\nSchäm dich!!");
			}
			labelErgebnis.setVisible(true);
		} else if (counter == counterAlleFragen + 1) {
			Scene newScene;
			Parent root;
			alleFenster.add("/reslearn/gui/fxml/TutorialFragen.fxml");
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
	}

	public void antwort(int counter) {
		switch (counter) {
		case 0:
			if (rb1.isSelected()) {
				label.setText("Antwort: " + rb1.getText() + " ist richtig");
				rb1.setDisable(true);
				rb1.setTextFill(Color.GREEN);
				rb2.setDisable(true);
				rb2.setTextFill(Color.RED);
				rb3.setDisable(true);
				rb3.setTextFill(Color.RED);
				rb4.setDisable(true);
				rb4.setTextFill(Color.RED);
				counterRichtigeAntworten++;
			} else {
				if (rb2.isSelected()) {
					label.setText("Antwort: " + rb2.getText() + " war falsch");
				} else if (rb3.isSelected()) {
					label.setText("Antwort: " + rb3.getText() + " war falsch");
				} else if (rb4.isSelected()) {
					label.setText("Antwort: " + rb4.getText() + " war falsch");
				} else {
					label.setText("Keine Antwort auszuwählen ist die falsche Lösung");
				}
				rb1.setDisable(true);
				rb1.setTextFill(Color.GREEN);
				rb2.setDisable(true);
				rb2.setTextFill(Color.RED);
				rb3.setDisable(true);
				rb3.setTextFill(Color.RED);
				rb4.setDisable(true);
				rb4.setTextFill(Color.RED);
			}
			break;
		case 1:
			if (rb2.isSelected()) {
				label.setText("Antwort: " + rb2.getText() + " ist richtig");
				rb2.setDisable(true);
				rb2.setTextFill(Color.GREEN);
				rb1.setDisable(true);
				rb1.setTextFill(Color.RED);
				rb3.setDisable(true);
				rb3.setTextFill(Color.RED);
				rb4.setDisable(true);
				rb4.setTextFill(Color.RED);
				counterRichtigeAntworten++;
			} else {
				if (rb1.isSelected()) {
					label.setText("Antwort: " + rb1.getText() + " war falsch");
				} else if (rb3.isSelected()) {
					label.setText("Antwort: " + rb3.getText() + " war falsch");
				} else if (rb4.isSelected()) {
					label.setText("Antwort: " + rb4.getText() + " war falsch");
				} else {
					label.setText("Keine Antwort auszuwählen ist die falsche Lösung");
				}
				rb2.setDisable(true);
				rb2.setTextFill(Color.GREEN);
				rb1.setDisable(true);
				rb1.setTextFill(Color.RED);
				rb3.setDisable(true);
				rb3.setTextFill(Color.RED);
				rb4.setDisable(true);
				rb4.setTextFill(Color.RED);
			}
			break;
		case 2:
			if (rb1.isSelected()) {
				label.setText("Antwort: " + rb1.getText() + " ist richtig");
				rb1.setDisable(true);
				rb1.setTextFill(Color.GREEN);
				rb2.setDisable(true);
				rb2.setTextFill(Color.RED);
				rb3.setDisable(true);
				rb3.setTextFill(Color.RED);
				rb4.setDisable(true);
				rb4.setTextFill(Color.RED);
				counterRichtigeAntworten++;
			} else {
				if (rb2.isSelected()) {
					label.setText("Antwort: " + rb2.getText() + " war falsch");
				} else if (rb3.isSelected()) {
					label.setText("Antwort: " + rb3.getText() + " war falsch");
				} else if (rb4.isSelected()) {
					label.setText("Antwort: " + rb4.getText() + " war falsch");
				} else {
					label.setText("Keine Antwort auszuwählen ist die falsche Lösung");
				}
				rb1.setDisable(true);
				rb1.setTextFill(Color.GREEN);
				rb2.setDisable(true);
				rb2.setTextFill(Color.RED);
				rb3.setDisable(true);
				rb3.setTextFill(Color.RED);
				rb4.setDisable(true);
				rb4.setTextFill(Color.RED);
			}
			break;
		case 3:
			if (rb1.isSelected()) {
				label.setText("Antwort: " + rb1.getText() + " ist richtig");
				rb1.setDisable(true);
				rb1.setTextFill(Color.GREEN);
				rb2.setDisable(true);
				rb2.setTextFill(Color.RED);
				rb3.setDisable(true);
				rb3.setTextFill(Color.RED);
				rb4.setDisable(true);
				rb4.setTextFill(Color.RED);
				counterRichtigeAntworten++;
			} else {
				if (rb2.isSelected()) {
					label.setText("Antwort: " + rb2.getText() + " war falsch");
				} else if (rb3.isSelected()) {
					label.setText("Antwort: " + rb3.getText() + " war falsch");
				} else if (rb4.isSelected()) {
					label.setText("Antwort: " + rb4.getText() + " war falsch");
				} else {
					label.setText("Keine Antwort auszuwählen ist die falsche Lösung");
				}
				rb1.setDisable(true);
				rb1.setTextFill(Color.GREEN);
				rb2.setDisable(true);
				rb2.setTextFill(Color.RED);
				rb3.setDisable(true);
				rb3.setTextFill(Color.RED);
				rb4.setDisable(true);
				rb4.setTextFill(Color.RED);
			}
			break;
		}
	}

	public void frage(int counter) {
		switch (counter) {
		case 0:
			selektieren();
			label.setText("Welche Bedarfsoptimierungsverfahren gibt es?");
			tg = new ToggleGroup();

			rb1.setText("Termin- und kapazitätstreue Bedarfsoptimierung");
			rb1.setUserData(rb1.getText());
			rb1.setToggleGroup(tg);

			rb2.setText("Termin- und planungstreue Bedarfsoptimierung");
			rb2.setUserData(rb2.getText());
			rb2.setToggleGroup(tg);

			rb3.setText("Planungs- und kapazitätstreue Bedarfsoptimierung");
			rb3.setUserData(rb3.getText());
			rb3.setToggleGroup(tg);

			rb4.setText("Termin-, planungs- und kapazitätstreue Bedarfsoptimierung");
			rb4.setUserData(rb4.getText());
			rb4.setToggleGroup(tg);
			break;

		case 1:
			selektieren();
			label.setText("Wobei handelt es sich NICHT um eine Vorraussetzung der Terminplanung der Ressourcen?");
			tg = new ToggleGroup();

			rb1.setText("Projektstrukturplan wurde erstellt");
			rb1.setUserData(rb1.getText());
			rb1.setToggleGroup(tg);

			rb2.setText("Terminbeschleunigung/Crashing wurde durchgeführt");
			rb2.setUserData(rb2.getText());
			rb2.setToggleGroup(tg);

			rb3.setText("Arbeitspakete wurden mit dazugehöriger Aufwandsschätzung und Dauer erstellt");
			rb3.setUserData(rb3.getText());
			rb3.setToggleGroup(tg);

			rb4.setText("Netzplan wurde erstellt");
			rb4.setUserData(rb4.getText());
			rb4.setToggleGroup(tg);
			break;

		case 2:
			selektieren();
			label.setText("Wie wird die kapazitätstreue Bedarfsoptimierung noch genannt?");
			tg = new ToggleGroup();

			rb1.setText("harter Abgleich");
			rb1.setUserData(rb1.getText());
			rb1.setToggleGroup(tg);

			rb2.setText("weicher Abgleich");
			rb2.setUserData(rb2.getText());
			rb2.setToggleGroup(tg);

			rb3.setText("schwerer Abgleich");
			rb3.setUserData(rb3.getText());
			rb3.setToggleGroup(tg);

			rb4.setText("stabiler Abgleich");
			rb4.setUserData(rb4.getText());
			rb4.setToggleGroup(tg);
			break;

		case 3:
			selektieren();
			label.setText("Welchen Vorteil bietet die termintreue Bedarfsoptimierung?");
			tg = new ToggleGroup();

			rb1.setText("Der Anfangs-und Endezeitpunkt des Projektes wird eingehalten");
			rb1.setUserData(rb1.getText());
			rb1.setToggleGroup(tg);

			rb2.setText("Die Anzahl der maximal parallel arbeitenden Mitarbeiter wird eingehalten");
			rb2.setUserData(rb2.getText());
			rb2.setToggleGroup(tg);

			rb3.setText("Hier könnte Ihre Werbung stehen");
			rb3.setUserData(rb3.getText());
			rb3.setToggleGroup(tg);

			rb4.setText("Das Projekt ist immer zum frühesten Endzeitpunkt fertiggestellt ");
			rb4.setUserData(rb4.getText());
			rb4.setToggleGroup(tg);
			break;
		}

	}

	public void selektieren() {
		rb1.setDisable(false);
		rb1.setSelected(false);
		rb1.setTextFill(Color.BLACK);
		rb2.setDisable(false);
		rb2.setSelected(false);
		rb2.setTextFill(Color.BLACK);
		rb3.setDisable(false);
		rb3.setSelected(false);
		rb3.setTextFill(Color.BLACK);
		rb4.setDisable(false);
		rb4.setSelected(false);
		rb4.setTextFill(Color.BLACK);
	}

}
