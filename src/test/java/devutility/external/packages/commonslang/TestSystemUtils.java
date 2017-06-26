package devutility.external.packages.commonslang;

import static org.junit.Assert.*;

import java.io.File;

import org.apache.commons.lang3.SystemUtils;
import org.junit.Before;
import org.junit.Test;

public class TestSystemUtils {

	@Before
	public void setUp() throws Exception {
		File file = SystemUtils.getUserDir();
		System.out.println(file.getPath());
	}

	@Test
	public void test() {
		fail("Not yet implemented");
	}
}