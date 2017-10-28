package devutility.external.test.executors.log.log4j;

import devutility.external.test.service.log.log4j.DateTimeAppenderService;
import devutility.internal.test.ServiceExecutor;

public class DateTimeAppenderTest {
	public static void main(String[] args) {
		ServiceExecutor.run(new DateTimeAppenderService());
	}
}