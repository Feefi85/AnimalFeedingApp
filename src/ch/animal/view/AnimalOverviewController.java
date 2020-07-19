package ch.animal.view;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import ch.animal.MainApp;
import ch.animal.model.Animal;
import ch.animal.util.DateUtil;

public class AnimalOverviewController {
	
    @FXML
    private TableView<Animal> animalTable;
    @FXML
    private TableColumn<Animal, String> tierArtColumn;
    @FXML
    private TableColumn<Animal, String> futterArtColumn;

    @FXML
    private Label tierArtLabel;
    @FXML
    private Label futterArtLabel;
    @FXML
    private Label aktuellerBestandLabel;
    @FXML
    private Label mengeneinheitLabel;
    @FXML
    private Label bestellungnotwendigLabel;
    @FXML
    private Label bestelldatumLabel;
    
    // Reference to the main application.
    private MainApp mainApp;

    /**
     * The constructor.
     * The constructor is called before the initialize() method.
     */
    public AnimalOverviewController() {
    }
    
    /**
     * Initializes the controller class. This method is automatically called
     * after the fxml file has been loaded.
     */
    @FXML
    private void initialize() {
        // Initialize the animal table with the two columns
        tierArtColumn.setCellValueFactory(
                cellData -> cellData.getValue().tierArtProperty());
        futterArtColumn.setCellValueFactory(
                cellData -> cellData.getValue().futterArtProperty());

        // Clear animal details
        showAnimalDetails(null);

        // Listen for selection changes and show the animal details when changed
        animalTable.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> showAnimalDetails(newValue));
    }
    
    /**
     * Is called by the main application to give a reference back to itself.
     * 
     * @param mainApp
     */
    public void setMainApp(MainApp mainApp) {
    	this.mainApp = mainApp;
    	
        // Add observable list data to the table
    	animalTable.setItems(mainApp.getAnimalData());
    }
    
    /**
     * Fills all text fields to show details about the animal.
     * If the specified animal is null, all text fields are cleared.
     * 
     * @param animal the animal or null
     */
    private void showAnimalDetails(Animal animal) {
    	if (animal != null) {
            // Fill the labels with info from the animal object.
    		tierArtLabel.setText(animal.getTierArt());
    		futterArtLabel.setText(animal.getFutterArt());
    		aktuellerBestandLabel.setText(Double.toString(animal.getAktuellerBestand()));
    		mengeneinheitLabel.setText(animal.getMengeneinheit());
    		bestellungnotwendigLabel.setText(animal.getBestellungnotwendig());
    		
    		bestelldatumLabel.setText(DateUtil.format(animal.getBestelldatum()));
    		// bestelldatumLabel.setText(...);
    	} else {
    		// Animal is null, remove all the text.
    		tierArtLabel.setText("");
    		futterArtLabel.setText("");
    		aktuellerBestandLabel.setText("");
    		mengeneinheitLabel.setText("");
    		bestellungnotwendigLabel.setText("");
    	}
    }
    /**
     * Called when the user clicks on the delete button.
     */
    @FXML
    private void handleDeleteAnimal() {
    	int selectedIndex = animalTable.getSelectionModel().getSelectedIndex();
    	if (selectedIndex >= 0) {
    		animalTable.getItems().remove(selectedIndex);
    	} else {
    		// Nothing selected.
    		Alert alert = new Alert(AlertType.WARNING);
            alert.initOwner(mainApp.getPrimaryStage());
            alert.setTitle("No Selection");
            alert.setHeaderText("No Animal Selected");
            alert.setContentText("Please select an animal in the table.");

            alert.showAndWait();
    	}
    }
    
    /**
     * Called when the user clicks the new button. Opens a dialog to edit
     * details for a new animal.
     */
    @FXML
    private void handleNewAnimal() {
        Animal tempAnimal = new Animal();
        boolean okClicked = mainApp.showAnimalEditDialog(tempAnimal);
        if (okClicked) {
            mainApp.getAnimalData().add(tempAnimal);
        }
    }
    
    /**
     * Called when the user clicks the edit button. Opens a dialog to edit
     * details for the selected animal.
     */
    @FXML
    private void handleEditAnimal() {
    	Animal selectedAnimal = animalTable.getSelectionModel().getSelectedItem();
    	if (selectedAnimal != null) {
    		boolean okClicked = mainApp.showAnimalEditDialog(selectedAnimal);
    		if (okClicked) {
    			showAnimalDetails(selectedAnimal);
    		}
    		
    	} else {
    		// Nothing selected.
    		Alert alert = new Alert(AlertType.WARNING);
            alert.initOwner(mainApp.getPrimaryStage());
            alert.setTitle("No Selection");
            alert.setHeaderText("No Animal Selected");
            alert.setContentText("Please select an animal in the table.");
            
            alert.showAndWait();
    	}
    }
}
