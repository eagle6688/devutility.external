package devutility.external.test.service.toolkit.commons_lang3.StringUtils;

import org.apache.commons.lang3.StringUtils;

import devutility.internal.test.BaseService;

public class IsNumericService extends BaseService {
	String value;

	public IsNumericService(String value) {
		this.value = value;
	}

	@Override
	public void run() {
		if (StringUtils.isNumeric(value)) {
			System.out.println(String.format("\"%s\" is numeric!", value));
		}
		else {
			System.out.println(String.format("\"%s\" is not numeric!", value));
		}
	}
}