package devutility.external.test.executors.toolkit.commons_lang3;

import devutility.external.test.service.toolkit.commons_lang3.SystemUtils.GetUserDirService;
import devutility.internal.test.ServiceExecutor;

public class SystemUtilsTest {
	public static void main(String[] args) {
		ServiceExecutor.run(new GetUserDirService());
	}
}