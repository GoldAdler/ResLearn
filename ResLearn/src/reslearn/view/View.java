package reslearn.view;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class View extends Application{
	
	public static void main(String[] args) {
		launch(args);
	}
	public void start (Stage stage) throws Exception {
		
	
		
		Parent root = (Parent) FXMLLoader.load(getClass().getResource("Einstellungen.fxml"));
		Scene scene = new Scene(new Group());
		scene.getStylesheets().add("Stylesheet.css");
		stage.setScene(new Scene(root));
		stage.setTitle("ResLearn");
		stage.show();
	
	}
}
