package devutility.external.service.toolkit.commons_lang3.StringUtils;

import org.apache.commons.lang3.StringUtils;

import devutility.internal.test.BaseService;

public class JoinService extends BaseService {
	@Override
	public void run() {
		String[] array = { "1", "2" };
		String str = StringUtils.join(array, ",");
		System.out.println(str);
	}
}