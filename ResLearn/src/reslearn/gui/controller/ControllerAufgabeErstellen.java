package reslearn.gui.controller;

import java.io.IOException;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellEditEvent;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.util.Callback;
import reslearn.model.paket.Arbeitspaket;

public class ControllerAufgabeErstellen extends Controller{
	private int anzPakete, anzMaxPersonen;
	final ToggleGroup rbGruppe = new ToggleGroup();	
	private String ergebnisValidierung = "";
//	private boolean paketKorrekt;
	
	// Zurück-Button ins Hauptmenü
	@FXML
	ImageView imagePfeil = new ImageView();
	
	@FXML
	private void handleImagePfeilAction(ActionEvent event) throws IOException {
		Parent root = FXMLLoader.load(getClass().getResource("Hauptmenue.fxml"));
		Scene newScene = new Scene(root);
		Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
		window.setScene(newScene);
		window.show();
	}
	
	// Anzahl Pakete
	@FXML
	Button buttonAnzPaketeMinus = new Button();
	@FXML
	Button buttonAnzPaketePlus = new Button();
	@FXML
	TextField textFieldAnzPakete = new TextField();
	
	@FXML
	private void handleButtonAnzPaketeMinusAction(ActionEvent event) {
		// Anzahl der Pakete soll nicht unter 1 sein
		if (anzPakete > 1) {
			anzPakete--;
			textFieldAnzPakete.setText(Integer.toString(anzPakete));
			
			// letzte Zeile entfernen
			tabelle.getItems().remove(anzPakete);
		}
	}
	@FXML
	private void handleButtonAnzPaketePlusAction(ActionEvent event) {
		anzPakete++;
		textFieldAnzPakete.setText(Integer.toString(anzPakete));
		
		// neue Zeile hinzufügen
		tabelle.getItems().add(new Arbeitspaket(Integer.toString(anzPakete), 0, 0, 0, 0, 0, 0, 0));
	}
	
	
	// Tabelle
	@FXML
	TableView<Arbeitspaket> tabelle = new TableView<>();
	@FXML
	TableColumn<Arbeitspaket, String> spalteID = new TableColumn<>();
	@FXML
	TableColumn<Arbeitspaket, Integer> spalteFaz = new TableColumn<>();
	@FXML
	TableColumn<Arbeitspaket, Integer> spalteSaz = new TableColumn<>();
	@FXML
	TableColumn<Arbeitspaket, Integer> spalteFez = new TableColumn<>();
	@FXML
	TableColumn<Arbeitspaket, Integer> spalteSez = new TableColumn<>();
	@FXML
	TableColumn<Arbeitspaket, Integer> spalteAnzMitarbeiter = new TableColumn<>();
	@FXML
	TableColumn<Arbeitspaket, Integer> spalteAufwand = new TableColumn<>();
	
	// Tabelle mit Werten befüllen
	public ObservableList<Arbeitspaket> getArbeitspaket() {
		ObservableList<Arbeitspaket> pakete = FXCollections.observableArrayList();
//		pakete.add(new Arbeitspaket("A", 1, 6, 3, 8, 6, 4, 24));
//		pakete.add(new Arbeitspaket("A", 1, 6, 3, 3, 6, 4, 24));
//		pakete.add(new Arbeitspaket("B", 7, 13, 10, 10, 7, 3, 21));
//		pakete.add(new Arbeitspaket("C", 9, 16, 14, 14, 8, 2, 16));
//		pakete.add(new Arbeitspaket("D", 14, 18, 17, 17, 5, 5, 25));
//		pakete.add(new Arbeitspaket("E", 17, 22, 21, 21, 6, 3, 18));
//		pakete.add(new Arbeitspaket("A", 1, 6, 3, 3, 6, 4, 24));
//		pakete.add(new Arbeitspaket("B", 7, 13, 10, 10, 7, 3, 21));
//		pakete.add(new Arbeitspaket("C", 9, 16, 14, 14, 8, 2, 16));
//		pakete.add(new Arbeitspaket("D", 14, 18, 17, 17, 5, 5, 25));
//		pakete.add(new Arbeitspaket("E", 17, 22, 21, 21, 6, 3, 18));
		
		pakete.add(new Arbeitspaket("1", 0, 0, 0, 0, 0, 0, 0));
		pakete.add(new Arbeitspaket("2", 0, 0, 0, 0, 0, 0, 0));
		pakete.add(new Arbeitspaket("3", 0, 0, 0, 0, 0, 0, 0));
		pakete.add(new Arbeitspaket("4", 0, 0, 0, 0, 0, 0, 0));
		return pakete;
	}
	
	// ObservableList zu Array konvertieren
	public static Arbeitspaket[] getArbeitspaketArray(ObservableList<Arbeitspaket> pakete) {
		Arbeitspaket arbeitspakete[] = new Arbeitspaket[pakete.size()];
		int i = 0;
		for (Arbeitspaket ap : pakete) {
			arbeitspakete[i++] = ap;
		}
		return arbeitspakete;
	}
	
	
	// Auswahl Optimierungsverfahren (Radiobuttons)
	@FXML
	RadioButton radioButtonKapazitaet = new RadioButton();
	@FXML
	RadioButton radioButtonTermin = new RadioButton();
	@FXML
	Pane panePersonen = new Pane();
	

	// maximale Personen Parallel (kapazitätstreu)
	@FXML
	Button buttonMaxPersonenMinus = new Button();
	@FXML
	Button buttonMaxPersonenPlus = new Button();
	@FXML
	TextField textFieldMaxPersonen = new TextField();
	
	@FXML
	private void handleButtonMaxPersonenMinusAction(ActionEvent event) {
		if (anzMaxPersonen > 1) {
			anzMaxPersonen--;
			textFieldMaxPersonen.setText(Integer.toString(anzMaxPersonen));
		}
	}
	@FXML
	private void handleButtonMaxPersonenPlusAction(ActionEvent event) {
		anzMaxPersonen++;
		textFieldMaxPersonen.setText(Integer.toString(anzMaxPersonen));		
	}
	
	// max. Personen bei termintreuer Opt. ausblenden
	@FXML
	private void handleRadioButtonKapazitaetAction(ActionEvent event) {
		panePersonen.setVisible(true);
	}
	@FXML
	private void handleRadioButtonTerminAction(ActionEvent event) {
		panePersonen.setVisible(false);
	}
	
	
	// Validieren
	@FXML
	Button buttonValidieren = new Button();
	
	@FXML
	private void handleButtonValidierenAction(ActionEvent event) {
		paneErgebnis.setVisible(true);
		Arbeitspaket pakete[] = getArbeitspaketArray(getArbeitspaket());
		if(paketeValidieren(pakete)) {
			labelErgebnis.setText("Validierung erfolgreich, die Aufgabe wurde gespeichert.");
		} else {
			labelErgebnis.setText(ergebnisValidierung);
		}
	}
	
	
	// Ergebnis Validierung anzeigen
	@FXML
	Pane paneErgebnis = new Pane();
	@FXML
	Label labelErgebnis = new Label();
	
	public boolean paketeValidieren(Arbeitspaket[] arbeitspaket) {
		boolean idKorrekt, fazKorrekt, sazKorrekt, fezKorrekt, sezKorrekt, paketKorrekt = false;
		int faz, saz, fez, sez;
		
		for (int i = 0; i < arbeitspaket.length; i++) {
			faz = arbeitspaket[i].getFaz();
			saz = arbeitspaket[i].getSaz();
			fez = arbeitspaket[i].getFez();
			sez = arbeitspaket[i].getSez();
			
			// FAZ prüfen
			if (faz >= 1) {
				fazKorrekt = true;
			} else {
				ergebnisValidierung = "Der Wert FAZ für das Arbeitspaket "+ arbeitspaket[i].getId() +" muss mindestens 1 sein";
				paketKorrekt = false;
				break;
			}
			
			// SAZ prüfen
			if (saz >= faz) {
				sazKorrekt = true;
			} else {
				ergebnisValidierung = "Der Wert SAZ für das Arbeitspaket "+ arbeitspaket[i].getId() +" muss mindestens gleich groß wie der Wert FAZ sein";
				paketKorrekt = false;
				break;
			}
			
			// FEZ prüfen
			if (fez > faz) {
				fezKorrekt = true;
			} else {
				ergebnisValidierung = "Der Wert FEZ für das Arbeitspaket "+ arbeitspaket[i].getId() +" muss größer als der Wert FAZ sein";
				paketKorrekt = false;
				break;
			}
			
			// SEZ prüfen
			if (sez >= fez) {
				sezKorrekt = true;
			} else {
				ergebnisValidierung = "Der Wert SEZ für das Arbeitspaket "+ arbeitspaket[i].getId() +" muss mindestens gleich groß wie der Wert FEZ sein";
				paketKorrekt = false;
				break;
			}
			
			// Alle Bedingungen prüfen
			if (fazKorrekt == true && sazKorrekt == true && fezKorrekt == true && sezKorrekt == true) {
				paketKorrekt = true;
			}
		}			
		
		return paketKorrekt;
	}
	
	
	
	
	public void initialize() {		
		anzPakete = Integer.parseInt(textFieldAnzPakete.getText());
		anzMaxPersonen = Integer.parseInt(textFieldMaxPersonen.getText());
		
		// den Spalten die richtigen Attribute zuteilen
		spalteID.setCellValueFactory(new PropertyValueFactory<>("id"));
		spalteFaz.setCellValueFactory(new PropertyValueFactory<>("faz"));
		spalteSaz.setCellValueFactory(new PropertyValueFactory<>("saz"));
		spalteFez.setCellValueFactory(new PropertyValueFactory<>("fez"));
		spalteSez.setCellValueFactory(new PropertyValueFactory<>("sez"));
		spalteAnzMitarbeiter.setCellValueFactory(new PropertyValueFactory<>("mitarbeiteranzahl"));
		spalteAufwand.setCellValueFactory(new PropertyValueFactory<>("aufwand"));
		tabelle.setItems(getArbeitspaket());
		
		// Spalten sollen bearbeitet werden können
		tabelle.setEditable(true);
		Callback<TableColumn, TableCell> cellFactory = new Callback<TableColumn, TableCell>() {
			public TableCell call(TableColumn p) {
				return new EditingCell();
	                 }
			};
			
		
//		spalteID.setCellFactory(cellFactory); 
//		spalteID.setCellFactory(TextFieldTableCell.forTableColumn());
//		spalteFaz.setCellFactory(TextFieldTableCell.forTableColumn());
//		spalteFez.setCellFactory(TextFieldTableCell.forTableColumn());
//		spalteSaz.setCellFactory(TextFieldTableCell.forTableColumn());
//		spalteSez.setCellFactory(TextFieldTableCell.forTableColumn());
//		spalteAnzMitarbeiter.setCellFactory(TextFieldTableCell.forTableColumn());
//		spalteAufwand.setCellFactory(TextFieldTableCell.forTableColumn());
		
		spalteID.setOnEditCommit(new EventHandler<CellEditEvent<Arbeitspaket, String>>() {
			public void handle(CellEditEvent<Arbeitspaket, String> t) {
				((Arbeitspaket) t.getTableView().getItems().get(t.getTablePosition().getRow())).setId(t.getNewValue());
			}
		});
		
		spalteFaz.setOnEditCommit(new EventHandler<CellEditEvent<Arbeitspaket, Integer>>() {
			public void handle(CellEditEvent<Arbeitspaket, Integer> t) {
				((Arbeitspaket) t.getTableView().getItems().get(t.getTablePosition().getRow())).setFaz(t.getNewValue());
			}
		});
		
		spalteSaz.setOnEditCommit(new EventHandler<CellEditEvent<Arbeitspaket, Integer>>() {
			public void handle(CellEditEvent<Arbeitspaket, Integer> t) {
				((Arbeitspaket) t.getTableView().getItems().get(t.getTablePosition().getRow())).setSaz(t.getNewValue());
			}
		});
		
		spalteFez.setOnEditCommit(new EventHandler<CellEditEvent<Arbeitspaket, Integer>>() {
			public void handle(CellEditEvent<Arbeitspaket, Integer> t) {
				((Arbeitspaket) t.getTableView().getItems().get(t.getTablePosition().getRow())).setFez(t.getNewValue());
			}
		});
		
		spalteSez.setOnEditCommit(new EventHandler<CellEditEvent<Arbeitspaket, Integer>>() {
			public void handle(CellEditEvent<Arbeitspaket, Integer> t) {
				((Arbeitspaket) t.getTableView().getItems().get(t.getTablePosition().getRow())).setSez(t.getNewValue());
			}
		});
		
		spalteAnzMitarbeiter.setOnEditCommit(new EventHandler<CellEditEvent<Arbeitspaket, Integer>>() {
			public void handle(CellEditEvent<Arbeitspaket, Integer> t) {
				((Arbeitspaket) t.getTableView().getItems().get(t.getTablePosition().getRow())).setMitarbeiteranzahl(t.getNewValue());
			}
		});
		
		spalteAufwand.setOnEditCommit(new EventHandler<CellEditEvent<Arbeitspaket, Integer>>() {
			public void handle(CellEditEvent<Arbeitspaket, Integer> t) {
				((Arbeitspaket) t.getTableView().getItems().get(t.getTablePosition().getRow())).setAufwand(t.getNewValue());
			}
		});
		
		radioButtonKapazitaet.setToggleGroup(rbGruppe);
		radioButtonKapazitaet.setSelected(true);
		radioButtonTermin.setToggleGroup(rbGruppe);
	}
	
	
	class EditingCell extends TableCell<Arbeitspaket, String> {
		 
        private TextField textField;
 
        public EditingCell() {
        }
 
        @Override
        public void startEdit() {
            if (!isEmpty()) {
                super.startEdit();
                createTextField();
                setText(null);
                setGraphic(textField);
                textField.selectAll();
            }
        }
 
        @Override
        public void cancelEdit() {
            super.cancelEdit();
 
            setText((String) getItem());
            setGraphic(null);
        }
 
        @Override
        public void updateItem(String item, boolean empty) {
            super.updateItem(item, empty);
 
            if (empty) {
                setText(null);
                setGraphic(null);
            } else {
                if (isEditing()) {
                    if (textField != null) {
                        textField.setText(getString());
                    }
                    setText(null);
                    setGraphic(textField);
                } else {
                    setText(getString());
                    setGraphic(null);
                }
            }
        }
 
        private void createTextField() {
            textField = new TextField(getString());
            textField.setMinWidth(this.getWidth() - this.getGraphicTextGap()* 2);
            textField.focusedProperty().addListener(new ChangeListener<Boolean>(){
                @Override
                public void changed(ObservableValue<? extends Boolean> arg0, 
                    Boolean arg1, Boolean arg2) {
                        if (!arg2) {
                            commitEdit(textField.getText());
                        }
                }
            });
        }
 
        private String getString() {
            return getItem() == null ? "" : getItem().toString();
        }
    }

}
