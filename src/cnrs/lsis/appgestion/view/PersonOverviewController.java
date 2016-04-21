package cnrs.lsis.appgestion.view;

import javafx.beans.property.ObjectProperty;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;

import java.io.File;
import java.time.LocalDate;

import cnrs.lsis.appgestion.MainApp;
import cnrs.lsis.appgestion.model.Person;
import cnrs.lsis.appgestion.util.DateUtil;

public class PersonOverviewController {
    @FXML
    private TableView<Person> personTable;
    @FXML
    private TableColumn<Person, String> firstNameColumn;
    @FXML
    private TableColumn<Person, String> lastNameColumn;
    @FXML
    private TableColumn<Person, String> teamColumn;
    @FXML
    private TableColumn<Person, String> supervisorColumn;
    @FXML
    private TableColumn<Person, String> doctoralSchoolColumn;
    @FXML
    private TableColumn<Person, String> contractColumn;
    @FXML
    private TableColumn<Person, String> desktopColumn;
    @FXML
    private TableColumn<Person, LocalDate> entranceDateColumn;
    @FXML
    private TableColumn<Person, LocalDate> exitDateColumn;
    @FXML
    private TableColumn<Person, String> statusColumn;
    
    
    @FXML
    private Label firstNameLabel;
    @FXML
    private Label lastNameLabel;
    @FXML
    private Label streetLabel;
    @FXML
    private Label postalCodeLabel;
    @FXML
    private Label cityLabel;
    @FXML
    private Label birthdayLabel;
    @FXML
    private Label entranceLabel;
    @FXML
    private Label exitLabel;
    @FXML
    private Label desktopLabel;
    @FXML
    private ImageView photo;
    @FXML
    private Label statusLabel;
    @FXML
    private Label teamLabel;
    @FXML
    private Label emailLabel;

    // Reference to the main application.
    private MainApp mainApp;

    /**
     * The constructor.
     * The constructor is called before the initialize() method.
     */
    public PersonOverviewController() {
    }

    /**
     * Initializes the controller class. This method is automatically called
     * after the fxml file has been loaded.
     */
    @FXML
    private void initialize() {
        // Initialize the person table with the two columns.
        firstNameColumn.setCellValueFactory(cellData -> cellData.getValue().firstNameProperty());
        lastNameColumn.setCellValueFactory(cellData -> cellData.getValue().lastNameProperty());
        statusColumn.setCellValueFactory(cellData -> cellData.getValue().statusProperty());
        teamColumn.setCellValueFactory(cellData -> cellData.getValue().teamProperty());
        supervisorColumn.setCellValueFactory(cellData -> cellData.getValue().postalCodeProperty());
        doctoralSchoolColumn.setCellValueFactory(cellData -> cellData.getValue().streetProperty());
        contractColumn.setCellValueFactory(cellData -> cellData.getValue().cityProperty());
        desktopColumn.setCellValueFactory(cellData -> cellData.getValue().desktopProperty());
        entranceDateColumn.setCellValueFactory(cellData -> cellData.getValue().entranceDateProperty());
        exitDateColumn.setCellValueFactory(cellData -> cellData.getValue().exitDateProperty());
        
        // Clear person details.
        showPersonDetails(null);

        // Listen for selection changes and show the person details when changed.
        personTable.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> showPersonDetails(newValue));
        // Add event handler for double click on row
        personTable.setOnMouseClicked(new EventHandler<MouseEvent>(){
        	@Override
        	public void handle(MouseEvent event) {
        	    if (event.getClickCount()>1) {
        	    	Person selectedPerson = personTable.getSelectionModel().getSelectedItem();
        	    	if (selectedPerson != null) {
        	            boolean okClicked = mainApp.showPersonEditDialog(selectedPerson);
        	            if (okClicked) {
        	                showPersonDetails(selectedPerson);
        	            }

        	        } else {
        	            // Nothing selected.
        	            Alert alert = new Alert(AlertType.WARNING);
        	            alert.initOwner(mainApp.getPrimaryStage());
        	            alert.setTitle("No Selection");
        	            alert.setHeaderText("No Person Selected");
        	            alert.setContentText("Please select a person in the table.");
        	            
        	            alert.showAndWait();
        	        }
        	    }
        	}
        });//end event handler double click
    }

    /**
     * Is called by the main application to give a reference back to itself.
     * 
     * @param mainApp
     */
    public void setMainApp(MainApp mainApp) {
        this.mainApp = mainApp;

        // Add observable list data to the table
        personTable.setItems(mainApp.getPersonData());
    }
    
    /**
     * Fills all text fields to show details about the person.
     * If the specified person is null, all text fields are cleared.
     * 
     * @param person the person or null
     */
    private void showPersonDetails(Person person) {
        if (person != null) {
            // Fill the labels with info from the person object.
            firstNameLabel.setText(person.getFirstName());
            lastNameLabel.setText(person.getLastName());
            streetLabel.setText(person.getStreet());
            postalCodeLabel.setText(person.getPostalCode());
            cityLabel.setText(person.getCity());
            birthdayLabel.setText(DateUtil.format(person.getBirthday()));
            entranceLabel.setText(DateUtil.format(person.getEntranceDate()));
            exitLabel.setText(DateUtil.format(person.getExitDate()));
            desktopLabel.setText(person.getDesktop());
            photo.setImage(new Image(person.getPhotoPath()));
            statusLabel.setText(person.getStatus());
            teamLabel.setText(person.getTeam());
            //emailLabel.setText(person.getEmail());
        } else {
            // Person is null, remove all the text.
            firstNameLabel.setText("");
            lastNameLabel.setText("");
            streetLabel.setText("");
            postalCodeLabel.setText("");
            cityLabel.setText("");
            birthdayLabel.setText("");
            entranceLabel.setText("");
            exitLabel.setText("");
            desktopLabel.setText("");
            Image img = new Image(MainApp.class.getResourceAsStream("/resources/images/person.png"));
            photo.setImage(img);
            statusLabel.setText("");
            teamLabel.setText("");
            //emailLabel.setText("");
        }
    }
    
    /**
     * Called when the user clicks on the delete button.
     */
    @FXML
    private void handleDeletePerson() {
        int selectedIndex = personTable.getSelectionModel().getSelectedIndex();
        if (selectedIndex >= 0) {
            personTable.getItems().remove(selectedIndex);
        } else {
            // Nothing selected.
            Alert alert = new Alert(AlertType.WARNING);
            alert.initOwner(mainApp.getPrimaryStage());
            alert.setTitle("No Selection");
            alert.setHeaderText("No Person Selected");
            alert.setContentText("Please select a person in the table.");
            
            alert.showAndWait();
        }
    }
    
    /**
     * Called when the user clicks the new button. Opens a dialog to edit
     * details for a new person.
     */
    @FXML
    private void handleNewPerson() {
        Person tempPerson = new Person();
        boolean okClicked = mainApp.showPersonEditDialog(tempPerson);
        if (okClicked) {
            mainApp.getPersonData().add(tempPerson);
        }
    }

    /**
     * Called when the user clicks the edit button. Opens a dialog to edit
     * details for the selected person.
     */
    @FXML
    private void handleEditPerson() {
        Person selectedPerson = personTable.getSelectionModel().getSelectedItem();
        if (selectedPerson != null) {
            boolean okClicked = mainApp.showPersonEditDialog(selectedPerson);
            if (okClicked) {
                showPersonDetails(selectedPerson);
            }

        } else {
            // Nothing selected.
            Alert alert = new Alert(AlertType.WARNING);
            alert.initOwner(mainApp.getPrimaryStage());
            alert.setTitle("No Selection");
            alert.setHeaderText("No Person Selected");
            alert.setContentText("Please select a person in the table.");
            
            alert.showAndWait();
        }
    }
}