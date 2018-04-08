package devutility.external.email;

import java.util.Date;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.mail.DefaultAuthenticator;
import org.apache.commons.mail.Email;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.HtmlEmail;
import org.apache.commons.mail.SimpleEmail;

public class CommonsEmailHelper {
	private String host;
	private int port;
	private String userName;
	private String password;
	private boolean debug;

	public boolean isDebug() {
		return debug;
	}

	public void setDebug(boolean debug) {
		this.debug = debug;
	}

	public CommonsEmailHelper(String host, int port, String userName, String password) {
		this.host = host;
		this.port = port;
		this.userName = userName;
		this.password = password;
	}

	public CommonsEmailHelper(String host, int port) {
		this(host, port, null, null);
	}

	private void initEmail(Email email) {
		email.setHostName(host);
		email.setSmtpPort(port);
		email.setAuthenticator(defaultAuthenticator());
		email.setCharset("UTF-8");
		email.setSentDate(new Date());
		email.setDebug(debug);
	}

	private DefaultAuthenticator defaultAuthenticator() {
		if (StringUtils.isNotBlank(userName)) {
			return new DefaultAuthenticator(userName, password);
		}

		return null;
	}

	private void setEmail(Email email, EmailModel emailModel) throws EmailException {
		email.setSSLOnConnect(emailModel.isSSLOnConnect());
		email.setFrom(emailModel.getFromEmail(), emailModel.getFromName(), "utf-8");
		setToEmails(email, emailModel);
		setCopyEmails(email, emailModel);
		email.setSubject(emailModel.getSubject());
	}

	private void setToEmails(Email email, EmailModel emailModel) throws EmailException {
		if (emailModel.getToEmailsMap() != null) {
			for (Map.Entry<String, String> entry : emailModel.getToEmailsMap().entrySet()) {
				email.addTo(entry.getKey(), entry.getValue(), "utf-8");
			}

			return;
		}

		if (emailModel.getToEmails() != null) {
			for (String mail : emailModel.getToEmails()) {
				email.addTo(mail);
			}
		}
	}

	private void setCopyEmails(Email email, EmailModel emailModel) throws EmailException {
		if (emailModel.getCopyEmailsMap() != null) {
			for (Map.Entry<String, String> entry : emailModel.getCopyEmailsMap().entrySet()) {
				email.addCc(entry.getKey(), entry.getValue(), "utf-8");
			}

			return;
		}

		if (emailModel.getCopyEmails() != null) {
			for (String mail : emailModel.getCopyEmails()) {
				email.addCc(mail);
			}
		}
	}

	public void sendSimpleEmail(EmailModel emailModel) throws EmailException {
		SimpleEmail email = new SimpleEmail();
		initEmail(email);
		setEmail(email, emailModel);
		email.setMsg(emailModel.getContent());
		email.send();
	}

	public void sendHtmlEmail(EmailModel emailModel) throws EmailException {
		HtmlEmail email = new HtmlEmail();
		initEmail(email);
		setEmail(email, emailModel);
		email.setHtmlMsg(emailModel.getContent());
		email.send();
	}
}