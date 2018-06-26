package reslearn.main;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;

public class Startprogramm extends Application {

	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage stage) throws Exception {
		try {
			Parent root = (Parent) FXMLLoader.load(getClass().getResource("/reslearn/gui/fxml/Hauptmenue.fxml"));

			Scene scene = new Scene(root);

			stage.setMaximized(true);
			stage.setScene(scene);
			stage.setTitle("ResLearn");
			stage.show();
		} catch (Exception e) {
			String meldung = e.getMessage();
			Alert alert = new Alert(AlertType.INFORMATION);
			alert.setTitle("Fehlermeldung");
			alert.setHeaderText("Fehlermeldung");
			alert.setContentText(meldung);
			alert.showAndWait();
		}

	}
}