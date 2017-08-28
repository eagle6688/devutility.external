package devutility.external.executors.log.log4j.custom;

import devutility.external.service.log.log4j.DateTimeAppender.LogService;
import devutility.internal.test.ServiceExecutor;

public class DateTimeAppenderTest {
	public static void main(String[] args) {
		ServiceExecutor.run(new LogService());
	}
}