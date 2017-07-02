package devutility.external.packages.commonslang;

import org.apache.commons.lang3.StringUtils;

public class StringUtilsTest {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String[] array = { "1", "2" };
		String str = StringUtils.join(array, ",");
		System.out.println(str);
	}
}