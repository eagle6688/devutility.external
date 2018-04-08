package devutility.external.email;

import java.util.List;
import java.util.Map;

public class EmailModel {
	private String fromEmail;
	private String fromName;
	private List<String> toEmails;
	private Map<String, String> toEmailsMap;
	private List<String> copyEmails;
	private Map<String, String> copyEmailsMap;
	private String subject;
	private String content;
	private boolean SSLOnConnect;

	public String getFromEmail() {
		return fromEmail;
	}

	public void setFromEmail(String fromEmail) {
		this.fromEmail = fromEmail;
	}

	public String getFromName() {
		return fromName;
	}

	public void setFromName(String fromName) {
		this.fromName = fromName;
	}

	public List<String> getToEmails() {
		return toEmails;
	}

	public void setToEmails(List<String> toEmails) {
		this.toEmails = toEmails;
	}

	public Map<String, String> getToEmailsMap() {
		return toEmailsMap;
	}

	public void setToEmailsMap(Map<String, String> toEmailsMap) {
		this.toEmailsMap = toEmailsMap;
	}

	public List<String> getCopyEmails() {
		return copyEmails;
	}

	public void setCopyEmails(List<String> copyEmails) {
		this.copyEmails = copyEmails;
	}

	public Map<String, String> getCopyEmailsMap() {
		return copyEmailsMap;
	}

	public void setCopyEmailsMap(Map<String, String> copyEmailsMap) {
		this.copyEmailsMap = copyEmailsMap;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public boolean isSSLOnConnect() {
		return SSLOnConnect;
	}

	public void setSSLOnConnect(boolean sSLOnConnect) {
		SSLOnConnect = sSLOnConnect;
	}
}