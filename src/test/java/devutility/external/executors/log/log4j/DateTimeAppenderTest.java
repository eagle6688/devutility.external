package devutility.external.executors.log.log4j;

import devutility.external.service.log.log4j.DateTimeAppenderService;
import devutility.internal.test.ServiceExecutor;

public class DateTimeAppenderTest {
	public static void main(String[] args) {
		ServiceExecutor.run(new DateTimeAppenderService());
	}
}