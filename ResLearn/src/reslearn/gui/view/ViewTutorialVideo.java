package reslearn.gui.view;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;
import reslearn.gui.utils.VideoPlayer;

public class ViewTutorialVideo extends Application {

	private static ViewTutorialVideo view;

	public static ViewTutorialVideo getInstance() {
		if (view == null) {
			view = new ViewTutorialVideo();
		}
		return view;
	}

	@Override
	public void start(Stage stage) throws Exception {
		// Lade FXML-Dokument
		Parent root = FXMLLoader.load(getClass().getResource("/reslearn/gui/fxml/Tutorial.fxml"));

		Pane pane = new Pane();
		pane.setPrefHeight(500);
		pane.setPrefWidth(800);
		pane.setLayoutX(200);
		pane.setLayoutY(85);

		MediaPlayer player = new MediaPlayer(
				new Media(getClass().getResource("/reslearn/gui/images/tutorialNeu.mp4").toExternalForm()));
		VideoPlayer video = new VideoPlayer(player);

		pane.getChildren().add(video);

		// Erstelle 2 Szenen: Hauptszene = FXML, Unterszene = Java-Videoplayer
		Scene hauptszene = new Scene(root);
		Scene unterszene = new Scene(pane);

		((Pane) hauptszene.getRoot()).getChildren().add(unterszene.getRoot());

		stage.setMaximized(true);
		stage.setScene(hauptszene);
		stage.setTitle("ResLearn");
		stage.show();
	}
}
