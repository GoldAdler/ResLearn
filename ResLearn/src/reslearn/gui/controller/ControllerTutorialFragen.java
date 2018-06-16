package reslearn.gui.controller;

import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class ControllerTutorialFragen extends Controller {

	private int counter = 0;
	private int counterFrageAntwort = 1;
	private int counterRichtigeAntworten = 0;
	private final int counterAlleFragen = 4;

	@FXML
	private Button zurueck;
	@FXML
	private Button home;
	@FXML
	private Button weiter;
	@FXML
	private Label label;
	@FXML
	private Label labelErgebnis;
	@FXML
	private ToggleGroup tg;
	@FXML
	private RadioButton rb1;
	@FXML
	private RadioButton rb2;
	@FXML
	private RadioButton rb3;
	@FXML
	private RadioButton rb4;

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

	@FXML
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

	@FXML
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
					label.setText("Keine Antwort ist falsch, netter Versuch");
				}
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
					label.setText("Keine Antwort ist falsch, netter Versuch");
				}
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
					label.setText("Keine Antwort ist falsch, netter Versuch");
				}
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
					label.setText("Keine Antwort ist falsch, netter Versuch");
				}
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
