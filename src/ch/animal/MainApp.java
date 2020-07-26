package ch.animal;

import java.io.File;
import java.io.IOException;
import java.util.prefs.Preferences;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import ch.animal.model.AnimalListWrapper;
import ch.animal.model.Animal;
import ch.animal.view.AnimalEditDialogController;
import ch.animal.view.AnimalOverviewController;
import ch.animal.view.RootLayoutController;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
/**
 * MainApp of the Animal Feeding App
 * @author Damian Krebs  
 * @author Stephanie Gloor
 */

public class MainApp extends Application {
	
	// Instance variables
	private Stage primaryStage; 
	private BorderPane rootLayout;
	
	private boolean dataEdited = false;
	
	/**
     * The data as an observable list of Animals.
     */
	private ObservableList<Animal> animalData = FXCollections.observableArrayList();
	
    /**
     * Constructor
     */
    public MainApp() {
        // Add some sample data to not start with an empty list
        animalData.add(new Animal("Rind", "Mais"));
        animalData.add(new Animal("Kalb", "Heu"));
        animalData.add(new Animal("Pferd", "Gras"));
        animalData.add(new Animal("Pony", "Hafer"));
        animalData.add(new Animal("Schwein", "Grünfutter"));
        animalData.add(new Animal("Huhn", "Körner"));
        animalData.add(new Animal("Strauss", "Körner"));
    }
    
    /**
     * Returns the data as an observable list of Animals. 
     * @return observable list of Animals
     */
    public ObservableList<Animal> getAnimalData() {
    	return animalData;
    }
    
    // Rest of class
    
    @Override
    public void start(Stage primaryStage) {
    	this.primaryStage = primaryStage;
    	this.primaryStage.setTitle("AnimalFeedingApp");
    	
    	initRootLayout();
    	
    	showAnimalOverview();
    }

    /**
     * Initializes the root layout plus tries to load the last opened animal file.
     */
    public void initRootLayout() {
        try {
            // Load root layout from fxml file
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApp.class.getResource("view/RootLayout.fxml"));
            rootLayout = (BorderPane) loader.load();
            
            // Show the scene containing the root layout
            Scene scene = new Scene(rootLayout);
            primaryStage.setScene(scene);
            
            // Give the controller access to the mainapp
            RootLayoutController controller = loader.getController();
            controller.setMainApp(this);
            
            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
        
     // Try to load last opened animal file
        File file = getAnimalFilePath();
        if (file != null) {
            loadAnimalDataFromFile(file);
        }
    }
    
    /**
     * Shows the animal overview inside the root layout.
     */
    public void showAnimalOverview() {
        try {
            // Load animal overview
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApp.class.getResource("view/AnimalOverview.fxml"));
            AnchorPane animalOverview = (AnchorPane) loader.load();
            
            // Set animal overview into the center of root layout
            rootLayout.setCenter(animalOverview);

            // Give access to the controller to the mainapp
            AnimalOverviewController controller = loader.getController();
            controller.setMainApp(this);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    /**
     * Returns the main stage.
     * @return main stage
     */
    public Stage getPrimaryStage() {
        return primaryStage;
    }

    public static void main(String[] args) {
        launch(args);
    }
    
    /**
     * Opens a dialog to edit details for the specified animal. If the user
     * clicks OK, the changes are saved into the provided animal object and true
     * is returned.
     * 
     * @param animal the animal object to be edited
     * @return true if the user clicked OK, false otherwise.
     */
    public boolean showAnimalEditDialog(Animal animal) {
        try {
            // Load the fxml file and create a new stage for the popup dialog
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApp.class.getResource("view/AnimalEditDialog.fxml"));
            AnchorPane page = (AnchorPane) loader.load();

            // Create the dialog stage
            Stage dialogStage = new Stage();
            dialogStage.setTitle("Edit Animal");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(primaryStage);
            Scene scene = new Scene(page);
            dialogStage.setScene(scene);

            // Set the animal into the controller
            AnimalEditDialogController controller = loader.getController();
            controller.setDialogStage(dialogStage);
            controller.setAnimal(animal);

            // Show the dialog and wait until the user closes it
            dialogStage.showAndWait();

            if (controller.isOkClicked()) {
            	setEdited(true);
            	return true;
            } else {
            	return false;
            }
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    /**
     * Returns the path to the preferences file, i.e. the file that was last opened.
     * The preference is read from the OS specific registry. If no such
     * preference can be found, null is returned.
     * 
     * @return path to preferences file
     */
    public File getAnimalFilePath() {
        Preferences prefs = Preferences.userNodeForPackage(MainApp.class);
        String filePath = prefs.get("filePath", null);
        if (filePath != null) {
            return new File(filePath);
        } else {
            return null;
        }
    }
    
    /**
     * Sets the file path of the currently loaded file. The path is persisted in
     * the OS specific registry.
     * 
     * @param file the file or null to remove the path
     */
    public void setAnimalFilePath(File file) {
        Preferences prefs = Preferences.userNodeForPackage(MainApp.class);
        if (file != null) {
            prefs.put("filePath", file.getPath());

            // Update the stage title
            primaryStage.setTitle("AnimalFeedingApp - " + file.getName());
        } else {
            prefs.remove("filePath");

            // Update the stage title
            primaryStage.setTitle("AnimalFeedingApp");
        }
    }
    
    /**
     * Loads animal data from the specified file. The current animal data will
     * be replaced.
     * 
     * @param file file to open
     */
    public void loadAnimalDataFromFile(File file) {
        try {
        	if(file.exists() ) {
        		
	            JAXBContext context = JAXBContext.newInstance(AnimalListWrapper.class);
	            Unmarshaller um = context.createUnmarshaller();
	
	            // Reading XML from the file and unmarshalling
	            AnimalListWrapper wrapper = (AnimalListWrapper) um.unmarshal(file);
	
	            animalData.clear();
	            animalData.addAll(wrapper.getAnimals());
	
	            // Save the file path to the registry
	            setAnimalFilePath(file);
	            setEdited(false);
        	}

        } catch (Exception e) { 
        	// catches ANY exception
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Could not load data");
            alert.setContentText("Could not load data from file:\n" + file.getPath());

            alert.showAndWait();
        }
    }
    
    /**
     * Saves the current animal data to the specified file.
     * 
     * @param file file to save
     */
    public void saveAnimalDataToFile(File file) {
        try {
            JAXBContext context = JAXBContext.newInstance(AnimalListWrapper.class);
            Marshaller m = context.createMarshaller();
            m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

            // Wrapping our animal data
            AnimalListWrapper wrapper = new AnimalListWrapper();
            wrapper.setAnimals(animalData);

            // Marshalling and saving XML to the file
            m.marshal(wrapper, file);

            // Save the file path to the registry
            setAnimalFilePath(file);
            setEdited(false);
        } catch (Exception e) { 
        	// catches ANY exception
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Could not save data");
            alert.setContentText("Could not save data to file:\n" + file.getPath());

            alert.showAndWait();
        }
    }
    
    /**
     * Sets a flag to indicate if data was modified in memory
     * @param edited set to true if data was modified
     */
    public void setEdited(boolean edited) {
    	dataEdited = edited;
    }
    
    /**
     * Returns a flag to indicate if data was modified in memory
     * @return true if modified
     */
    public boolean getEdited() {
    	return dataEdited;
    }
}
