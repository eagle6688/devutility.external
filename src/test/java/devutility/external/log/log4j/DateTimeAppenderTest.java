package devutility.external.log.log4j;

import org.apache.log4j.Logger;

public class DateTimeAppenderTest {

	public static Logger logger = Logger.getLogger(DateTimeAppenderTest.class);
	
	public static void main(String[] args) {
		logger.debug("debug log output!");
		logger.error("error log output!");
	}
}