package reslearn.gui;

import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Orientation;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ScrollBar;
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

		Parent root = FXMLLoader.load(getClass().getResource("/reslearn/gui/fxml/UebungAuswaehlen.fxml"));

		Group group = new Group();
		ControllerUebungAuswaehlen cua = new ControllerUebungAuswaehlen();
		Pane pane = cua.erstellePane();
		ScrollBar scrolli = new ScrollBar();
		if (cua.fileArray.length <= 10) {
			scrolli.setVisible(false);
		}
		scrolli.setOrientation(Orientation.VERTICAL);
		scrolli.setLayoutX(DisplayCanvas.paneAufgabeLadenBreite - scrolli.getWidth());
		scrolli.setLayoutY(DisplayCanvas.hoeheUeberschrift);
		scrolli.setMinHeight(DisplayCanvas.scrolliHoehe);
		scrolli.valueProperty().addListener(new ChangeListener<Number>() {
			@Override
			public void changed(ObservableValue<? extends Number> ov, Number old_val, Number new_val) {
				pane.setLayoutY(-new_val.doubleValue() * ((Math.ceil(cua.fileArray.length / 5.0) - 2.0) * 3.2));
				pane.getParent().toBack();
			}
		});
		Scene hauptszene = new Scene(root);
		group.getChildren().addAll(pane, scrolli);

		Scene unterszene = new Scene(group);

		((Pane) hauptszene.getRoot()).getChildren().add(unterszene.getRoot());
		stage.setMaximized(true);
		stage.setScene(hauptszene);
		stage.setTitle("ResLearn");
		stage.show();

	}

	public static void main(String[] args) {
		launch(args);
	}

}
