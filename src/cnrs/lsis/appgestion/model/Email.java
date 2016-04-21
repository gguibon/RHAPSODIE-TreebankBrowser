package cnrs.lsis.appgestion.model;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 * Model class for an email configuration.
 *
 * @author Gaël Guibon
 */
public class Email {
	
	private final StringProperty mailTo;
	private final StringProperty mailFrom;
	private final StringProperty password;
	private final StringProperty outSMTP;
	private final StringProperty portSMTP;

    /**
     * Default constructor.
     */
    public Email() {
        this(null, null);
    }

    /**
     * Constructor with some initial data.
     * 
     * @param firstName
     * @param lastName
     */
    public Email(String mailTo, String mailFrom) {
        this.mailTo = new SimpleStringProperty(mailTo);
        this.mailFrom = new SimpleStringProperty(mailFrom);
        
        // Some initial dummy data, just for convenient testing.
        this.outSMTP = new SimpleStringProperty("SMTP server");
        this.password = new SimpleStringProperty("password of the sending email account");
        this.portSMTP = new SimpleStringProperty("port for SMTP");
        
    }

    public String getMailTo() {
        return mailTo.get();
    }

    public void setMailTo(String mail) {
        this.mailTo.set(mail);
    }

    public StringProperty mailToProperty() {
        return mailTo;
    }
    
    public String getMailFrom() {
        return mailFrom.get();
    }

    public void setMailFrom(String mail) {
        this.mailFrom.set(mail);
    }

    public StringProperty mailFromProperty() {
        return mailFrom;
    }
    
    public String getPassword() {
        return password.get();
    }

    public void setPassword(String pass) {
        this.password.set(pass);
    }

    public StringProperty passwordProperty() {
        return password;
    }
    
    public String getOutSMTP() {
        return outSMTP.get();
    }

    public void setOutSMTP(String smtp) {
        this.outSMTP.set(smtp);
    }

    public StringProperty outSMTPProperty() {
        return outSMTP;
    }
    
    public String getPortSMTP() {
        return portSMTP.get();
    }

    public void setPortSMTP(String smtpPort) {
        this.portSMTP.set(smtpPort);
    }

    public StringProperty portSMTPProperty() {
        return portSMTP;
    }
}