package reslearn.gui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
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
		Scene hauptscene = new Scene(root);
		ControllerUebungAuswaehlen cua = new ControllerUebungAuswaehlen();
		ScrollPane pane = cua.erstellePane();
		stage.setMaximized(true);
		Scene unterscene = new Scene(pane);

		// TODO Schei� Scrollbar funktioniert nicht
		((Pane) hauptscene.getRoot()).getChildren().add(unterscene.getRoot());
		stage.setScene(hauptscene);
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
