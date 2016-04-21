package cnrs.lsis.appgestion.view;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;

import cnrs.lsis.appgestion.MainApp;
import cnrs.lsis.appgestion.model.Email;
import cnrs.lsis.appgestion.model.Person;
import cnrs.lsis.appgestion.model.PersonListWrapper;
import cnrs.lsis.appgestion.util.DateUtil;
import eu.hansolo.enzo.notification.Notification.Notifier;

/**
 * Dialog to edit email settings.
 * 
 * @author Gaël Guibon
 */
public class EmailSettingsController {

	// Reference to the main application
    private MainApp mainApp;
	
    @FXML
    private TextField mailToField;
    @FXML
    private TextField mailFromField;
    @FXML
    private TextField passwordField;
    @FXML
    private TextField outSMTPField;
    @FXML
    private TextField portSMTPField;

    private Stage dialogStage;
    private Email email;
    private boolean okClicked = false;

    /**
     * Initializes the controller class. This method is automatically called
     * after the fxml file has been loaded.
     */
    @FXML
    private void initialize() {
    	
    }

    
    /**
     * Is called by the main application to give a reference back to itself.
     * 
     * @param mainApp
     */
    public void setMainApp(MainApp mainApp) {
        this.mainApp = mainApp;
    }
    
    /**
     * Sets the stage of this dialog.
     * 
     * @param dialogStage
     */
    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
        
        // Set the dialog icon.
        this.dialogStage.getIcons().add(new Image("file:resources/images/edit.png"));
    }

    /**
     * Sets the mail to be edited in the dialog.
     * 
     * @param email
     */
    public void setEmail(String mail) {
    	
        this.email = email;
//        mailToField.setText(email.getMailTo());
        mailToField.setText(mail);
//        mailFromField.setText(email.getMailFrom());
//        passwordField.setText(email.getPassword());
//        outSMTPField.setText(email.getOutSMTP());
//        portSMTPField.setText(email.getPortSMTP());
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
        	String mailFieldText = mailToField.getText();
        	Notifier.INSTANCE.notifySuccess("Success !", mailFieldText);
        	
        	mainApp.setMail(mailFieldText);

            okClicked = true;
            Notifier.INSTANCE.notifySuccess("Success !", "Changes updated !");
            dialogStage.close();
        }
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

        if (mailToField.getText() == null || mailToField.getText().length() == 0) {
            errorMessage += "No valid mail adress to send to!\n"; 
        }
//        if (mailFromField.getText() == null || mailFromField.getText().length() == 0) {
//            errorMessage += "No valid mail adress to send from!\n"; 
//        }
//        if (passwordField.getText() == null || passwordField.getText().length() == 0) {
//            errorMessage += "No valid password!\n"; 
//        }
//
//        if (outSMTPField.getText() == null || outSMTPField.getText().length() == 0) {
//            errorMessage += "No valid SMTP server!\n"; 
//        } 
//
//        if (portSMTPField.getText() == null || portSMTPField.getText().length() == 0) {
//            errorMessage += "No valid SMTP port!\n"; 
//        }

        
        if (errorMessage.length() == 0) {
            return true;
        } else {
            // Show the error message.
            Alert alert = new Alert(AlertType.ERROR);
            alert.initOwner(dialogStage);
            alert.setTitle("Invalid Fields");
            alert.setHeaderText("Please correct invalid fields");
            alert.setContentText(errorMessage);
            
            alert.showAndWait();
            
            return false;
        }
    }
}