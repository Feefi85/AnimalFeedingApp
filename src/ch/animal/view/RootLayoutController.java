package ch.animal.view;

import java.io.File;
import java.util.Optional;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.stage.FileChooser;
import javafx.stage.WindowEvent;
import javafx.scene.control.ButtonBar;
import ch.animal.MainApp;

/**
 * The controller for the root layout. The root layout provides the basic
 * application layout containing a menu bar and space where other JavaFX
 * elements can be placed.
 * 
 */
public class RootLayoutController {
	
    // Reference to the main application
    private MainApp mainApp;

    /**
     * Is called by the main application to give a reference back to itself.
     * 
     * @param mainApp mainApp of the application
     */
    public void setMainApp(MainApp mainApp) {
        this.mainApp = mainApp;
        mainApp.getPrimaryStage().setOnCloseRequest((WindowEvent we) -> 
        {
            this.handleExit();
        });
    }
    
    /**
     * Creates an empty animalfeeding list.
     */
    @FXML
    private void handleNew() {
        mainApp.getAnimalData().clear();
        mainApp.setAnimalFilePath(null);
    }
    
    /**
     * Opens a FileChooser to let the user select an existing animalfeeding file to load.
     */
    @FXML
    private void handleOpen() {
        FileChooser fileChooser = new FileChooser();

        // Set extension filter
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter(
                "XML files (*.xml)", "*.xml");
        fileChooser.getExtensionFilters().add(extFilter);

        // Show open file dialog
        File file = fileChooser.showOpenDialog(mainApp.getPrimaryStage());

        if (file != null) {
            mainApp.loadAnimalDataFromFile(file);
        }
    }
    
    /**
     * Saves the file to the animal file that is currently open. If there is no
     * open file, the "save as" dialog is shown.
     */
    @FXML
    private void handleSave() {
        File animalFile = mainApp.getAnimalFilePath();
        if (animalFile != null) {
            mainApp.saveAnimalDataToFile(animalFile);
        } else {
            handleSaveAs();
        }
    }
    
    /**
     * Opens a FileChooser to let the user select a file to save to.
     */
    @FXML
    private void handleSaveAs() {
        FileChooser fileChooser = new FileChooser();

        // Set extension filter
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter(
                "XML files (*.xml)", "*.xml");
        fileChooser.getExtensionFilters().add(extFilter);

        // Show save file dialog
        File file = fileChooser.showSaveDialog(mainApp.getPrimaryStage());

        if (file != null) {
            // Make sure it has the correct extension
            if (!file.getPath().endsWith(".xml")) {
                file = new File(file.getPath() + ".xml");
            }
            mainApp.saveAnimalDataToFile(file);
        }
    }
    
    /**
     * Opens an about dialog of the authors.
     */
    @FXML
    private void handleAbout() {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("AnimalFeedingApp");
        alert.setHeaderText("About");
        alert.setContentText("Author: Damian Krebs & Stephanie Gloor");
        alert.showAndWait();
    }
    
    /**
     * Opens an documentation on an external hyperlink
     */
    @FXML
    private void handleDocumentation() {
    	Runtime rt = Runtime.getRuntime();
    	String url = "https://wiki.lohmueller.ch/display/AN";
    
		try {
			//checks the actual OS of the users computer and opens the standardbrowser
	    	String os = System.getProperty("os.name").toLowerCase();
	    	if (os.indexOf("win") >= 0) {
				rt.exec("rundll32 url.dll,FileProtocolHandler " + url);
	    	} else {
	    		rt.exec("open  " + url);
	    	}
    	} catch (Exception ex) {
    		ex.printStackTrace();
    	}
    }
    
    
    /**
     * Closes the application after asking if the data should be saved
     */
    @FXML
    private void handleExit() {
        File animalFile = mainApp.getAnimalFilePath();
    	if (mainApp.getEdited()) {
        	ButtonType speichern = new ButtonType("Speichern", ButtonBar.ButtonData.OK_DONE);
            ButtonType verwerfen = new ButtonType("Verwerfen", ButtonBar.ButtonData.CANCEL_CLOSE);

            Alert alert = new Alert(AlertType.CONFIRMATION,
            		"Sollen die aktuellen Daten gespeichert werden nach '" + animalFile + "'?",
            		speichern, verwerfen);            
            Optional<ButtonType> result = alert.showAndWait();
            if (result.orElse(verwerfen) == speichern) {
            	handleSave();
            }    		
    	}
    	
        System.exit(0);
    }

}
