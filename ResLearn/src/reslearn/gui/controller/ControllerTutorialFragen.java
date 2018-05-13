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
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

public class ControllerTutorialFragen extends Controller {

	static int counter = -1;

	@FXML
	private ImageView zurueck;
	@FXML
	private ImageView home;
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
	public void zurueck() throws Exception {
		zurueck.addEventFilter(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
			Scene newScene;

			public void handle(MouseEvent event) {
				if (counter < 0) {
					Parent root;
					try {
						root = FXMLLoader.load(getClass().getResource("../fxml/Tutorial.fxml"));
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
				} else {
					frage(counter-1);
					counter -= 1;
				}
			}
		});
	}

	@FXML
	public void weiter(ActionEvent event) {
		System.out.println("Button gedrückt" + counter);
		counter += 1;
		if (counter < 4) {
			frage(counter);
		} else if (counter > 4) {
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

	@FXML
	public void frage(int counter) {
		if (counter == 0) {
			label.setText("Frage 0");;
			tg = new ToggleGroup();
			rb1.setText("Antwort 01");
			rb1.setToggleGroup(tg);
			rb2.setText("Antwort 02");
			rb2.setToggleGroup(tg);
			rb3.setText("Antwort 03");
			rb3.setToggleGroup(tg);
			rb4.setText("Antwort 04");
			rb4.setToggleGroup(tg);
		} else if (counter == 1) {
			label.setText("Frage 1");
			tg = new ToggleGroup();
			rb1.setText("Antwort 1");
			rb1.setToggleGroup(tg);
			rb2.setText("Antwort 2");
			rb2.setToggleGroup(tg);
			rb3.setText("Antwort 3");
			rb3.setToggleGroup(tg);
			rb4.setText("Antwort 4");
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

	@FXML
	public void home() throws Exception {
		home.addEventFilter(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
			Scene newScene;

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
