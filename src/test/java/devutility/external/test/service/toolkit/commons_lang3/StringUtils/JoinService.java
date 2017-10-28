package devutility.external.test.service.toolkit.commons_lang3.StringUtils;

import org.apache.commons.lang3.StringUtils;

import devutility.internal.test.BaseService;
import devutility.internal.test.ServiceExecutor;

public class JoinService extends BaseService {
	@Override
	public void run() {
		String[] array = { "1", "2" };
		String str = StringUtils.join(array, ",");
		System.out.println(str);
	}

	public static void main(String[] args) {
		ServiceExecutor.run(new JoinService());
	}
}