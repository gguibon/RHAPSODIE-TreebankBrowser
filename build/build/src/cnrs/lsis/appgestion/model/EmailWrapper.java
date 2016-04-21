package cnrs.lsis.appgestion.model;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Helper class to wrap a list of email configurations. This is used for saving the
 * list of emails conf to XML.
 * 
 * @author Gaël Guibon
 */
@XmlRootElement(name = "emails")
public class EmailWrapper {

    private List<Email> emails;

    @XmlElement(name = "email")
    public List<Email> getEmails() {
        return emails;
    }

    public void setEmails(List<Email> emails) {
        this.emails = emails;
    }
}