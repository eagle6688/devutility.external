package devutility.external.test.email.commonsemail;

import java.util.Arrays;
import java.util.Map;

import org.apache.commons.mail.EmailException;

import devutility.external.email.CommonsEmailHelper;
import devutility.external.email.EmailModel;
import devutility.external.test.config.MailConfig;
import devutility.internal.test.BaseTest;
import devutility.internal.test.TestExecutor;

public class SendSimpleEmailTest extends BaseTest {
	@Override
	public void run() {
		Map<String, String> map = MailConfig.get();
		int port = Integer.valueOf(map.get("port"));
		CommonsEmailHelper commonsEmailHelper = new CommonsEmailHelper(map.get("host"), port, map.get("userName"), map.get("password"));

		EmailModel emailModel = new EmailModel();
		emailModel.setFromEmail(map.get("fromEmail"));
		emailModel.setToEmails(Arrays.asList(map.get("toEmails").split(",")));
		emailModel.setCopyEmails(Arrays.asList(map.get("copyEmails").split(",")));
		emailModel.setSubject("Test mail");
		emailModel.setContent("Hello world!");

		try {
			commonsEmailHelper.setDebug(true);
			commonsEmailHelper.sendSimpleEmail(emailModel);
		} catch (EmailException e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		TestExecutor.run(SendSimpleEmailTest.class);
	}
}