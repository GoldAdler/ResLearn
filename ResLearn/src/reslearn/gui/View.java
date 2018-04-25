package reslearn.gui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import reslearn.model.algorithmus.AlgoErsteSchritt;
import reslearn.model.paket.ResEinheit;
import javafx.scene.canvas.*;
import javafx.scene.layout.Pane;

public class View extends Application{
	
	public static void main(String[] args) {
		launch(args);
	}
	
	public void start (Stage stage) throws Exception {
//        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("./fxml/Uebungsmodus.fxml"));
//        Parent root = fxmlLoader.load();
//        
		Parent root = FXMLLoader.load(getClass().getResource("./fxml/Uebungsmodus.fxml"));
		Scene scene = new Scene(new Group());
		scene.getStylesheets().add("Stylesheet.css");
		
        Canvas canvas = new Canvas(900, 600);
        canvas.setLayoutX(230);
        canvas.setLayoutY(80);
        GraphicsContext gc = canvas.getGraphicsContext2D();
		Group diagramm = new Group();
        diagramm.getChildren().add(canvas);
           
        Scene sceneFenster = new Scene(root);
        Scene sceneDiagramm = new Scene(diagramm);
        ((Pane) sceneFenster.getRoot()).getChildren().add(sceneDiagramm.getRoot());
        
		stage.setMaximized(true);
		stage.setScene(sceneFenster);
		stage.setTitle("ResLearn");
		stage.show();
		

		Diagramm meincanvas = new Diagramm();
        meincanvas.zeichneCanvas(gc,canvas);
	}

}
