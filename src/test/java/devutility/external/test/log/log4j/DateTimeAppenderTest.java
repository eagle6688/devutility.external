package devutility.external.test.log.log4j;

import org.apache.log4j.Logger;

import devutility.internal.test.BaseTest;
import devutility.internal.test.TestExecutor;

public class DateTimeAppenderTest extends BaseTest {
	@Override
	public void run() {
		Logger logger = Logger.getLogger(DateTimeAppenderTest.class);
		logger.trace("trace log output!");
		logger.debug("debug log output!");
		logger.info("info log output!");
		logger.warn("warn log output!");
		logger.error("error log output!");
		logger.fatal("fatal log output!");
	}
	
	public static void main(String[] args) {
		TestExecutor.run(new DateTimeAppenderTest());
	}
}