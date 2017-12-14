package devutility.external.test.log.slf4j;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import devutility.internal.test.BaseTest;
import devutility.internal.test.TestExecutor;

public class IntegrateLog4jTest extends BaseTest {
	@Override
	public void run() {
		Logger logger = LoggerFactory.getLogger(IntegrateLog4jTest.class);
		logger.debug("debug log output!");
		logger.error("error log output!");
	}
	
	public static void main(String[] args) {
		TestExecutor.run(new IntegrateLog4jTest());
	}
}