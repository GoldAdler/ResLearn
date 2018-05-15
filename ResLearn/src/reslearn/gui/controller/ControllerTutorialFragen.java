package reslearn.gui.controller;

import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import reslearn.gui.fxml.TutorialVideo;

public class ControllerTutorialFragen extends Controller {

	int counter = 0;
	int counterFrageAntwort = 1;

	@FXML
	private Button zurueck;
	@FXML
	private Button home;
	@FXML
	private Button weiter;
	@FXML
	private Label label;
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
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		((Node) (event.getSource())).getScene().getWindow().hide();
	}

	@FXML
	public void zurueck(ActionEvent event) throws Exception {
		if (counter <= 0) {
			TutorialVideo tut = new TutorialVideo();
			try {
				tut.start(TutorialVideo.classStage);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			((Node) (event.getSource())).getScene().getWindow().hide();
		} else {
			frage(--counter);
			System.out.println("ZurueckButton Gedrücht" + counter);
		}
	}

	@FXML
	public void weiter(ActionEvent event) {
		System.out.println("Button gedrückt" + counter);
		if (counter < 4) {
			if (counterFrageAntwort == 1) {
				frage(counter);
				counterFrageAntwort = 2;
			} else {
				antwort(counter);
				counterFrageAntwort = 1;
				counter += 1;
			}

		} else if (counter == 4) {
			counter = 0;
			Scene newScene;
			Parent root;
			alleFenster.add("../fxml/TutorialFragen.fxml");
			try {
				root = FXMLLoader.load(getClass().getResource(hauptmenue()));
				newScene = new Scene(root);
				Stage stage = new Stage();
				stage.setTitle("ResLearn");
				stage.setMaximized(true);
				stage.setScene(newScene);
				stage.show();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			((Node) (event.getSource())).getScene().getWindow().hide();
		}
	}

	public void antwort(int counter) {
		if (counter == 0) {
			if (rb1.isSelected()) {
				label.setText("Antwort 1 ist richtig");
				rb1.setTextFill(Color.GREEN);
				rb2.setDisable(true);
				rb2.setTextFill(Color.RED);
				rb3.setDisable(true);
				rb3.setTextFill(Color.RED);
				rb4.setDisable(true);
				rb4.setTextFill(Color.RED);
			} else {
				// label.setText("Antwort " + tg.getSelectedToggle().getUserData() + " war
				// falsch");
				rb1.setTextFill(Color.GREEN);
				rb2.setDisable(true);
				rb2.setTextFill(Color.RED);
				rb3.setDisable(true);
				rb3.setTextFill(Color.RED);
				rb4.setDisable(true);
				rb4.setTextFill(Color.RED);
			}

		} else if (counter == 1) {
			label.setText("Antwort 1");
			tg = new ToggleGroup();
			rb1.setText("Richtig 1");
			rb1.setToggleGroup(tg);
			rb2.setText("Falsch 2");
			rb2.setToggleGroup(tg);
			rb3.setText("Falsch 3");
			rb3.setToggleGroup(tg);
			rb4.setText("Falsch 4");
			rb4.setToggleGroup(tg);
		} else if (counter == 2) {
			label.setText("Antwort 2");
			tg = new ToggleGroup();
			rb1.setText("Richtig 11");
			rb1.setToggleGroup(tg);
			rb2.setText("Falsch 22");
			rb2.setToggleGroup(tg);
			rb3.setText("Falsch 33");
			rb3.setToggleGroup(tg);
			rb4.setText("Falsch 44");
			rb4.setToggleGroup(tg);
		} else if (counter == 3) {
			label.setText("Antwort 3");
			tg = new ToggleGroup();
			rb1.setText("Falsch 111");
			rb1.setToggleGroup(tg);
			rb2.setText("Richtig 222");
			rb2.setToggleGroup(tg);
			rb3.setText("Falsch 333");
			rb3.setToggleGroup(tg);
			rb4.setText("Falsch 444");
			rb4.setToggleGroup(tg);
		}
	}

	public void frage(int counter) {
		if (counter == 0) {
			selektieren();
			label.setText("Frage 0");
			;
			tg = new ToggleGroup();

			rb1.setText("Antwort 01");
			rb1.setUserData(rb1.getText());
			rb1.setToggleGroup(tg);

			rb2.setText("Antwort 02");
			rb2.setUserData(rb2.getText());
			rb2.setToggleGroup(tg);

			rb3.setText("Antwort 03");
			rb3.setUserData(rb3.getText());
			rb3.setToggleGroup(tg);

			rb4.setText("Antwort 04");
			rb4.setUserData(rb4.getText());
			rb4.setToggleGroup(tg);
		} else if (counter == 1) {
			selektieren();
			label.setText("Frage 1");
			tg = new ToggleGroup();

			rb1.setText("Antwort 1");
			rb1.setUserData(rb1.getText());
			rb1.setToggleGroup(tg);

			rb2.setText("Antwort 2");
			rb2.setUserData(rb2.getText());
			rb2.setToggleGroup(tg);

			rb3.setText("Antwort 3");
			rb3.setUserData(rb3.getText());
			rb3.setToggleGroup(tg);

			rb4.setText("Antwort 4");
			rb4.setUserData(rb4.getText());
			rb4.setToggleGroup(tg);
		} else if (counter == 2) {
			label.setText("Frage 2");
			tg = new ToggleGroup();
			rb1.setText("Antwort 11");
			rb1.setToggleGroup(tg);
			rb2.setText("Antwort 22");
			rb2.setToggleGroup(tg);
			rb3.setText("Antwort 33");
			rb3.setToggleGroup(tg);
			rb4.setText("Antwort 44");
			rb4.setToggleGroup(tg);
		} else if (counter == 3) {
			label.setText("Frage 3");
			tg = new ToggleGroup();
			rb1.setText("Antwort 111");
			rb1.setToggleGroup(tg);
			rb2.setText("Antwort 222");
			rb2.setToggleGroup(tg);
			rb3.setText("Antwort 333");
			rb3.setToggleGroup(tg);
			rb4.setText("Antwort 444");
			rb4.setToggleGroup(tg);
		}

	}

	public void selektieren() {
		rb1.setDisable(false);
		rb1.setTextFill(Color.BLACK);
		rb2.setDisable(false);
		rb2.setTextFill(Color.BLACK);
		rb3.setDisable(false);
		rb3.setTextFill(Color.BLACK);
		rb4.setDisable(false);
		rb4.setTextFill(Color.BLACK);
	}

	@FXML
	public void home() throws Exception {
		home.addEventFilter(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
			Scene newScene;

			@Override
			public void handle(MouseEvent event) {
				alleFenster.add("../fxml/UebungAuswaehlen.fxml");
				Parent root;
				try {
					root = FXMLLoader.load(getClass().getResource(hauptmenue()));
					newScene = new Scene(root);
					Stage stage = new Stage();
					stage.setTitle("ResLearn");
					stage.setMaximized(true);
					stage.setScene(newScene);
					stage.show();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				((Node) (event.getSource())).getScene().getWindow().hide();
			}
		});
	}
}
