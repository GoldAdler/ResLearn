package reslearn.gui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Startprogramm extends Application {

	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage stage) throws Exception {

		Parent root = (Parent) FXMLLoader.load(getClass().getResource("./fxml/Hauptmenue.fxml"));
		Scene scene = new Scene(root);

		stage.setMaximized(true);
		stage.setScene(scene);
		stage.setTitle("ResLearn");
		stage.show();
	}
}