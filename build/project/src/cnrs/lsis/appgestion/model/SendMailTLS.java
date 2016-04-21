package cnrs.lsis.appgestion.model;

import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class SendMailTLS {
	
	
	public void send(String mailTo, String mailFrom, String pwd, String SMTP, String portSMTP, String msg) {

		final String username = mailFrom;
		final String password = pwd;

		Properties props = new Properties();
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.starttls.enable", "true");
		props.put("mail.smtp.host", SMTP);
		props.put("mail.smtp.port", portSMTP);

		Session session = Session.getInstance(props,
		  new javax.mail.Authenticator() {
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(username, password);
			}
		  });

		try {

			Message message = new MimeMessage(session);
			message.setFrom(new InternetAddress(mailFrom));
			message.setRecipients(Message.RecipientType.TO,
				InternetAddress.parse(mailTo));
			message.setSubject("[APP GESTION] Notification ");
			message.setText(msg);

			Transport.send(message);

			System.out.println("Done");

		} catch (MessagingException e) {
			throw new RuntimeException(e);
		}
	}
}