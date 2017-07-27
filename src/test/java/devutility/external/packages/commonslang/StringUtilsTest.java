package devutility.external.packages.commonslang;

import org.apache.commons.lang3.StringUtils;

public class StringUtilsTest {

	public static void main(String[] args) {
		String[] array = { "1", "2" };
		String str = StringUtils.join(array, ",");
		System.out.println(str);

		if (StringUtils.isNumeric("asd")) {
			System.out.println("asd is numeric.");
		}
		
		if (StringUtils.isNumeric("123")) {
			System.out.println("123 is numeric.");
		}
	}
}