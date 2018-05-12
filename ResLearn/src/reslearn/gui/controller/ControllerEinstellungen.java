package reslearn.gui.controller;

import java.io.File;
import java.io.IOException;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class ControllerEinstellungen extends Controller{
/*////////////////////////////////////////////////////////////////////
//  Dropdown Buttons in Einstellungen                 			   //
//////////////////////////////////////////////////////////////////*/

	@FXML
	private MenuButton dropdownDesigntheme;
	
	@FXML
	private MenuButton dropdownFarbschema;

	@FXML
	private MenuButton dropdownSchriftgroesse;
	
	@FXML
	private MenuButton dropdownAufloesung;
	
	@FXML
	private ImageView ordner1;
	
	@FXML
	private TextField textField1;
	
	@FXML
	private ImageView ordner2;
	
	@FXML
	private TextField textField2;
	
	@FXML
	private ImageView zurueck;
	
	
	@FXML
	public void initialize() {
/*////////////////////////////////////////////////////////////////////
//  			           DesignTheme			      			   //
//////////////////////////////////////////////////////////////////*/
		MenuItem modena = new MenuItem("Modena");
		MenuItem caspian = new MenuItem("Caspian");
		dropdownDesigntheme.getItems().addAll(modena,caspian);
		
		modena.setOnAction(new EventHandler<ActionEvent>() {
		    @Override
		    public void handle(ActionEvent event) {
		    	dropdownDesigntheme.setText(modena.getText());
		    	Application.setUserAgentStylesheet(Application.STYLESHEET_MODENA);
		    }
		});
		caspian.setOnAction(new EventHandler<ActionEvent>() {
		    @Override
		    public void handle(ActionEvent event) {
		    	dropdownDesigntheme.setText(caspian.getText());
		    	Application.setUserAgentStylesheet(Application.STYLESHEET_CASPIAN);
		    }
		});

/*////////////////////////////////////////////////////////////////////
//      					Farbschema				      		   //
//////////////////////////////////////////////////////////////////*/
		
		MenuItem lightheme = new MenuItem("Light Theme");
		MenuItem darktheme = new MenuItem("Dark Theme");
		dropdownFarbschema.getItems().addAll(lightheme,darktheme);
		
		lightheme.setOnAction(new EventHandler<ActionEvent>() {
		    @Override
		    public void handle(ActionEvent event) {
		    	dropdownFarbschema.setText(lightheme.getText());
		    }
		});
		darktheme.setOnAction(new EventHandler<ActionEvent>() {
		    @Override
		    public void handle(ActionEvent event) {
		    	dropdownFarbschema.setText(darktheme.getText());
		    }
		});

/*////////////////////////////////////////////////////////////////////
//							Schriftgröße			      		   //
//////////////////////////////////////////////////////////////////*/

		MenuItem schrift12 = new MenuItem("12");
		MenuItem schrift14 = new MenuItem("14");
		MenuItem schrift16 = new MenuItem("16");
		MenuItem schrift18 = new MenuItem("18");
		MenuItem schrift20 = new MenuItem("20");
		dropdownSchriftgroesse.getItems().addAll(schrift12, schrift14, schrift16, schrift18, schrift20);

		schrift12.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				dropdownSchriftgroesse.setText(schrift12.getText());
			}
		});
		schrift14.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				dropdownSchriftgroesse.setText(schrift14.getText());
			}
		});
		schrift16.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				dropdownSchriftgroesse.setText(schrift16.getText());
			}
		});
		schrift18.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				dropdownSchriftgroesse.setText(schrift18.getText());
			}
		});
		schrift20.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				dropdownSchriftgroesse.setText(schrift20.getText());
			}
		});
		
/*////////////////////////////////////////////////////////////////////
//							Auflösung				      		   //
//////////////////////////////////////////////////////////////////*/

		MenuItem klein = new MenuItem("900x600");
		MenuItem mittel = new MenuItem("1200x900");
		MenuItem gross = new MenuItem("1600x1200");
		dropdownAufloesung.getItems().addAll(klein, mittel, gross);

		klein.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				dropdownAufloesung.setText(klein.getText());
			}
		});
		mittel.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				dropdownAufloesung.setText(mittel.getText());
			}
		});
		gross.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				dropdownAufloesung.setText(gross.getText());
			}
		});
	
	}
	
	@FXML
	public void ordnerOpen() {
		ordner1.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {

            public void handle(MouseEvent event) {
               FileChooser fileChooser = new FileChooser();
               FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("TXT files (*.txt)", "*.txt");
               fileChooser.getExtensionFilters().add(extFilter);
               File file = fileChooser.showOpenDialog(ordner1.getScene().getWindow());
               textField1.setText(file.getAbsolutePath());
           }
       });
		ordner2.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {

            public void handle(MouseEvent event) {
               FileChooser fileChooser = new FileChooser();
               FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("TXT files (*.txt)", "*.txt");
               fileChooser.getExtensionFilters().add(extFilter);
               File file = fileChooser.showOpenDialog(ordner2.getScene().getWindow());
               textField2.setText(file.getAbsolutePath());
           }
       });
	}
	
	@FXML
	public void zurueck() throws Exception{
		zurueck.addEventFilter(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
			Scene newScene;
			public void handle(MouseEvent event) {
				Parent root;
				try {
					root = FXMLLoader.load(getClass().getResource(vorherigesFenster(alleFenster)));
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

