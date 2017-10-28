package devutility.external.test.executors.toolkit.commons_lang3;

import devutility.external.test.service.toolkit.commons_lang3.StringUtils.IsNumericService;
import devutility.external.test.service.toolkit.commons_lang3.StringUtils.JoinService;
import devutility.internal.test.ServiceExecutor;

public class StringUtilsTest {
	public static void main(String[] args) {
		ServiceExecutor.run(new JoinService());
		ServiceExecutor.run(new IsNumericService("asd"));
		ServiceExecutor.run(new IsNumericService("123"));
	}
}