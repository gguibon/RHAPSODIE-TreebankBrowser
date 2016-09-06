package cnrs.rhapsodie.treebankbrowser;

import java.awt.Desktop;
import java.awt.Toolkit;
import java.io.File;
import com.aquafx_project.AquaFx;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Properties;
import java.util.prefs.Preferences;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.value.ObservableStringValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import cnrs.rhapsodie.treebankbrowser.model.PersonListWrapper;
import cnrs.rhapsodie.treebankbrowser.model.ProjectUI;
import cnrs.rhapsodie.treebankbrowser.util.OsValidator;
import cnrs.rhapsodie.treebankbrowser.utils.JarResources;
import cnrs.rhapsodie.treebankbrowser.utils.ResourcesFromJar;
import cnrs.rhapsodie.treebankbrowser.utils.Tools;
import cnrs.rhapsodie.treebankbrowser.view.PersonEditDialogController;
import cnrs.rhapsodie.treebankbrowser.view.PersonOverviewController;
import cnrs.rhapsodie.treebankbrowser.view.RootLayoutController;
import eu.hansolo.enzo.notification.Notification.Notifier;

public class MainApp extends Application {

    private Stage primaryStage;
    private BorderPane rootLayout;
    
    /**
     * The data as an observable list of Persons.
     */
    private ObservableList<ProjectUI> personData = FXCollections.observableArrayList();
    

    /**
     * Constructor
     */
    public MainApp() {   

        // Add some sample data
        personData.add(new ProjectUI("A project", "My first project"));
        personData.add(new ProjectUI("Another Project", "My second project"));

    }

    /**
     * Returns the data as an observable list of Persons. 
     * @return
     */
    public ObservableList<ProjectUI> getPersonData() {
        return personData;
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        this.primaryStage = primaryStage;
        this.primaryStage.setTitle("AddressApp");
        
        // Set the application icon.
//        this.primaryStage.getIcons().add(new Image("file:/resources/images/address_book_32.png"));
//        this.primaryStage.getIcons().add(new Image(new Tools().accessRessourceFile("/resources/images/adress_book_32.png")));
        Image img = new Image(MainApp.class.getResourceAsStream("/resources/images/address_book_32.png"));
        this.primaryStage.getIcons().add(img);

        this.primaryStage.setMaximized(true);
        
        initRootLayout();

        showPersonOverview();
        
        primaryStage.setOnCloseRequest(e -> Platform.exit());
    }

    /**
     * Initializes the root layout and tries to load the last opened
     * person file.
     */
    public void initRootLayout() {
        try {
            // Load root layout from fxml file.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApp.class
                    .getResource("view/RootLayout.fxml"));
            rootLayout = (BorderPane) loader.load();

            // Show the scene containing the root layout.
            Scene scene = new Scene(rootLayout);
            primaryStage.setScene(scene);

            // Give the controller access to the main app.
            RootLayoutController controller = loader.getController();
            controller.setMainApp(this);

            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Try to load last opened person file.
        File file = getPersonFilePath();
        if (file != null) {
            loadPersonDataFromFile(file);
        }
        
//     // Try to load mail pref.
//        this.mail = getMailPref();
//        System.out.println(mail);

    }

    /**
     * Shows the person overview inside the root layout.
     */
    public void showPersonOverview() {
        try {
            // Load person overview.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApp.class.getResource("view/PersonOverview.fxml"));
            AnchorPane personOverview = (AnchorPane) loader.load();

            // Set person overview into the center of root layout.
            rootLayout.setCenter(personOverview);

            // Give the controller access to the main app.
            PersonOverviewController controller = loader.getController();
            controller.setMainApp(this);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    /**
     * Opens a dialog to edit details for the specified person. If the user
     * clicks OK, the changes are saved into the provided person object and true
     * is returned.
     * 
     * @param person the person object to be edited
     * @return true if the user clicked OK, false otherwise.
     */
    public boolean showPersonEditDialog(ProjectUI person) {
        try {
            // Load the fxml file and create a new stage for the popup dialog.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApp.class.getResource("view/PersonEditDialog.fxml"));
            AnchorPane page = (AnchorPane) loader.load();

            // Create the dialog Stage.
            Stage dialogStage = new Stage();
            dialogStage.setTitle("Edit Person");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(primaryStage);
            Scene scene = new Scene(page);
            dialogStage.setScene(scene);

            // Set the person into the controller.
            PersonEditDialogController controller = loader.getController();
            controller.setDialogStage(dialogStage);
            controller.setPerson(person);
            
            // Set the dialog icon.
            Image img = new Image(MainApp.class.getResourceAsStream("/resources/images/edit.png"));
            dialogStage.getIcons().add(img);
//            dialogStage.getIcons().add(new Image("file:///resources/images/edit.png"));

            // Show the dialog and wait until the user closes it
            dialogStage.showAndWait();

            return controller.isOkClicked();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    
    /**
     * Opens a dialog to edit email settings. If the user
     * clicks OK, the changes are saved into the settings object and true
     * is returned.
     * 
     * @param person the person object to be edited
     */
    public void showEmailSettings() {
        try {
        	// Load the fxml file and create a new stage for the popup.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApp.class.getResource("view/EmailSettings.fxml"));
            AnchorPane page = (AnchorPane) loader.load();
            Stage dialogStage = new Stage();
            dialogStage.setTitle("Email Settings");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(primaryStage);
            Scene scene = new Scene(page);
            dialogStage.setScene(scene);
            
            // Set the dialog icon.
            Image img = new Image(MainApp.class.getResourceAsStream("/resources/images/settings.png"));
            dialogStage.getIcons().add(img);
//            dialogStage.getIcons().add(new Image("file://resources/images/settings.png"));

           

            dialogStage.show();
        } catch (IOException e) {
            e.printStackTrace();
           
        }
    }
    
    /**
     * Returns the person file preference, i.e. the file that was last opened.
     * The preference is read from the OS specific registry. If no such
     * preference can be found, null is returned.
     * 
     * @return
     */
    public File getPersonFilePath() {
        Preferences prefs = Preferences.userNodeForPackage(MainApp.class);
        String filePath = prefs.get("filePath", null);
        if (filePath != null) {
            return new File(filePath);
        } else {
            return null;
        }
    }
    
    /**
     * Returns the person file preference, i.e. the file that was last opened.
     * The preference is read from the OS specific registry. If no such
     * preference can be found, null is returned.
     * 
     * @return
     */
    public String getMailPref() {
        Preferences prefs = Preferences.userNodeForPackage(MainApp.class);
        String mailPref = prefs.get("mailPref", null);
        if (mailPref != null) {
            return mailPref;
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
    public void setPersonFilePath(File file) {
        Preferences prefs = Preferences.userNodeForPackage(MainApp.class);
        if (file != null) {
            prefs.put("filePath", file.getPath());

            // Update the stage title.
            primaryStage.setTitle("TreebankBrowser - " + file.getName());
        } else {
            prefs.remove("filePath");

            // Update the stage title.
            primaryStage.setTitle("TreebankBrowser");
        }
    }
    
    
    /**
     * Sets the file path of the currently loaded file. The path is persisted in
     * the OS specific registry.
     * 
     * @param file the file or null to remove the path
     */
    public void setMailPref(String mail) {
        Preferences prefs = Preferences.userNodeForPackage(MainApp.class);
        if (mail != null) {
            prefs.put("mailPref", mail);
        } else {
            prefs.remove("mailPref");;
        }
    }
    
    
    /**
     * Loads person data from the specified file. The current person data will
     * be replaced.
     * 
     * @param file
     */
    public void loadPersonDataFromFile(File file) {
        try {
            JAXBContext context = JAXBContext
                    .newInstance(PersonListWrapper.class);
            Unmarshaller um = context.createUnmarshaller();

            // Reading XML from the file and unmarshalling.
            PersonListWrapper wrapper = (PersonListWrapper) um.unmarshal(file);

            personData.clear();
            personData.addAll(wrapper.getPersons());

            // Save the file path to the registry.
            setPersonFilePath(file);

        } catch (Exception e) { // catches ANY exception
        	Alert alert = new Alert(AlertType.ERROR);
        	alert.setTitle("Error");
        	alert.setHeaderText("Could not load data");
        	alert.setContentText("Could not load data from file:\n" + file.getPath());
        	
        	alert.showAndWait();
        }
    }

    /**
     * Saves the current person data to the specified file.
     * 
     * @param file
     */
    public void savePersonDataToFile(File file) {
        try {
            JAXBContext context = JAXBContext
                    .newInstance(PersonListWrapper.class);
            Marshaller m = context.createMarshaller();
            m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

            // Wrapping our person data.
            PersonListWrapper wrapper = new PersonListWrapper();
            wrapper.setPersons(personData);

            // Marshalling and saving XML to the file.
            m.marshal(wrapper, file);

            // Save the file path to the registry.
            setPersonFilePath(file);
        } catch (Exception e) { // catches ANY exception
        	Alert alert = new Alert(AlertType.ERROR);
        	alert.setTitle("Error");
        	alert.setHeaderText("Could not save data");
        	alert.setContentText("Could not save data to file:\n" + file.getPath());
        	
        	alert.showAndWait();
        }
        
        
    }

    /**
     * Returns the main stage.
     * @return
     */
    public Stage getPrimaryStage() {
        return primaryStage;
    }

    public static void main(String[] args) throws Exception {
    	
//    	String jarFile = "/home/gael/workspacefx/RHAPSODIE/build/deploy/bundles/RHAPSODIE.jar";
//    	String destDir = "/home/gael/workspacefx/RHAPSODIE/build/deploy/bundles/extracted";
//    	Tools.createDir(destDir);
//    	java.util.jar.JarFile jar = new java.util.jar.JarFile(jarFile);
//    	java.util.Enumeration enumEntries = jar.entries();
//    	while (enumEntries.hasMoreElements()) {
////    	while (enumEntries.nextElement().toString() != null){
////    		if(!enumEntries.nextElement().toString().startsWith("resources"))continue;
////	    		System.out.println(enumEntries.hasMoreElements() + "\t" +enumEntries.nextElement().toString());
//	    	    java.util.jar.JarEntry file = (java.util.jar.JarEntry) enumEntries.nextElement();
//	    	    
////	    	    if(!file.getName().startsWith("resources/interface-statique"))continue;
//	    	    System.out.println(file.getName());
//	    	    if(file.getName().equals("resources/interface-statique/samples/treesModel.html"))break;
//	    	    java.io.File f = new java.io.File(destDir + java.io.File.separator + file.getName());
//	    	    if (file.isDirectory()) { // if its a directory, create it
//	//    	    	f.getParentFile().mkdirs();
//	    	        f.mkdir();
//	    	        continue;
//	    	    }
//	    	    java.io.InputStream is = jar.getInputStream(file); // get the input stream
//	    	    java.io.FileOutputStream fos = new java.io.FileOutputStream(f);
//	    	    while (is.available() > 0) {  // write contents of 'is' to 'fos'
//	    	        fos.write(is.read());
//	    	    }
//	    	    fos.close();
//	    	    is.close();
////    		}
//    	}
    	
        launch(args);    	
    	
//		ResourcesFromJar rfj = new ResourcesFromJar();
//		rfj.get();
//		System.exit(0);
    	
    }
}