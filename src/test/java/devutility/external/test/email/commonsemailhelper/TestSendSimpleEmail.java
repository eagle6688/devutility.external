package devutility.external.test.email.commonsemailhelper;

import java.util.Arrays;

import org.apache.commons.mail.EmailException;

import devutility.external.email.CommonsEmailHelper;
import devutility.external.email.EmailModel;
import devutility.internal.test.BaseTest;
import devutility.internal.test.TestExecutor;

public class TestSendSimpleEmail extends BaseTest {
	@Override
	public void run() {
		CommonsEmailHelper commonsEmailHelper = new CommonsEmailHelper("smtpinternal.lenovo.com", 25);

		EmailModel emailModel = new EmailModel();
		emailModel.setContent("Hello, this is a test email!");
		emailModel.setCopyEmails(Arrays.asList("yuanwc1@lenovo.com"));
		emailModel.setFromEmail("sufb1@lenovo.com");
		emailModel.setFromName("Fangbing");
		emailModel.setSubject("Hello World!");
		emailModel.setToEmails(Arrays.asList("kongrr1@lenovo.com"));

		try {
			commonsEmailHelper.sendSimpleEmail(emailModel);
		} catch (EmailException e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		TestExecutor.run(TestSendSimpleEmail.class);
	}
}