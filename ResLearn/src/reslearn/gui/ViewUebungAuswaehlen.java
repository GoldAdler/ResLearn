package reslearn.gui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import reslearn.gui.controller.ControllerUebungAuswaehlen;

public class ViewUebungAuswaehlen extends Application {
	public static Stage classStage = new Stage();
	// private Pane pane;

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
		System.out.println("Juhuuuu");
		Parent root = FXMLLoader.load(getClass().getResource("./fxml/UebungAuswaehlen.fxml"));
		Scene scene = new Scene(root);
		ControllerUebungAuswaehlen cua = new ControllerUebungAuswaehlen();
		Pane pane = cua.erstellePane();
		stage.setMaximized(true);
		// TODO Scheiﬂ Scrollbar funktioniert nicht
		((Pane) scene.getRoot()).getChildren().add(pane);
		stage.setScene(scene);
		stage.setTitle("ResLearn");
		stage.show();
		classStage = stage;

	}

	public static void main(String[] args) {
		launch(args);
	}

	// public Pane getPane() {
	// return pane;
	// }

}
