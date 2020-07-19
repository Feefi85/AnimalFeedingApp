package ch.animal.view;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.time.LocalDate;

import ch.animal.model.Animal;

/**
 * Dialog to edit details of an animal.
 * 
 */

public class AnimalEditDialogController {
	
	ObservableList<String> bestellungnotwendigList = FXCollections
			.observableArrayList("Ja", "Nein");
	
    public ObservableList<String> getBestellungnotwendigList() {
		return bestellungnotwendigList;
	}

	public void setBestellungnotwendigList(ObservableList<String> bestellungnotwendigList) {
		this.bestellungnotwendigList = bestellungnotwendigList;
	}

	@FXML
    private TextField tierArtField;
    @FXML
    private TextField futterArtField;
    @FXML
    private TextField aktuellerBestandField;
    @FXML
    private TextField mengeneinheitField;
    @FXML
    private ComboBox<String> bestellungnotwendigBox;
    @FXML
    private DatePicker bestelldatumField;

    private Stage dialogStage;
    private Animal animal;
    private boolean okClicked = false;
    
    /**
     * Initializes the controller class. This method is automatically called
     * after the fxml file has been loaded.
     */
    @FXML
    private void initialize() {
    	
    }

    /**
     * Sets the stage of this dialog.
     * 
     * @param dialogStage
     */
    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }
    
    /**
     * Sets the animal to be edited in the dialog.
     * 
     * @param animal
     */
    public void setAnimal(Animal animal) {
        this.animal = animal;
      
        tierArtField.setText(animal.getTierArt());
        futterArtField.setText(animal.getFutterArt());
        aktuellerBestandField.setText(Double.toString(animal.getAktuellerBestand()));
        mengeneinheitField.setText(animal.getMengeneinheit());
        bestellungnotwendigBox.setItems(bestellungnotwendigList);
        bestellungnotwendigBox.setValue(animal.getBestellungnotwendig());
        bestelldatumField.setValue(animal.getBestelldatum());
        
        // no manual entry - date picker still available
        bestelldatumField.setEditable(false);
    }
    
    /**
     * Returns true if the user clicked OK, false otherwise.
     * 
     * @return
     */
    public boolean isOkClicked() {
        return okClicked;
    }
    
    /**
     * Called when the user clicks ok.
     */
    @FXML
    private void handleOk() {
        if (isInputValid()) {
            animal.setTierArt(tierArtField.getText());
            animal.setFutterArt(futterArtField.getText());
            animal.setAktuellerBestand(Double.parseDouble(aktuellerBestandField.getText()));
            animal.setMengeneinheit(mengeneinheitField.getText());
            animal.setBestellungnotwendig(bestellungnotwendigBox.getValue());
            animal.setBestelldatum(bestelldatumField.getValue());

            okClicked = true;
            dialogStage.close();
        }
    }
    
    
    /**
     * Called when the user clicks "heute".
     * Writes todays date to the Bestelldatum field
     */
    @FXML
    private void handleToday() {
    	animal.setBestelldatum(LocalDate.now());
    	bestelldatumField.setValue(animal.getBestelldatum());
    }
    
    /**
     * Called when the user clicks cancel.
     */
    @FXML
    private void handleCancel() {
        dialogStage.close();
    }
    
    /**
     * Validates the user input in the text fields.
     * 
     * @return true if the input is valid
     */
    private boolean isInputValid() {
        String errorMessage = "";

        if (tierArtField.getText() == null || tierArtField.getText().length() == 0) {
            errorMessage += "Ungültige Eingabe in Feld 'Tierart'!\n"; 
        }
        if (futterArtField.getText() == null || futterArtField.getText().length() == 0) {
            errorMessage += "Ungültige Eingabe im Feld 'Futterart'!\n"; 
        }
        if (aktuellerBestandField.getText() == null || aktuellerBestandField.getText().length() == 0) {
            errorMessage += "Ungültige Eingabe im Feld 'Aktueller Bestand'!\n"; 
        } else {
            // try to parse the aktueller bestand into an double.
            try {
                Double.parseDouble(aktuellerBestandField.getText());
            } catch (NumberFormatException e) {
                errorMessage += "Ungültige Eingabe im Feld 'Aktueller Bestand' (muss eine Zahl sein)!\n"; 
            }
        }
        
        if (mengeneinheitField.getText() == null || mengeneinheitField.getText().length() == 0) {
            errorMessage += "Ungültige Einabe im Feld 'Mengeneinheit'!\n";
        }

        if (errorMessage.length() == 0) {
            return true;
        } else {
            // Show the error message.
            Alert alert = new Alert(AlertType.ERROR);
            alert.initOwner(dialogStage);
            alert.setTitle("Fehlerhafte Eingaben");
            alert.setHeaderText("Bitte fehlerhafte Eingaben korrigieren");
            alert.setContentText(errorMessage);
            
            alert.showAndWait();
            
            return false;
        }
    }
}
