package devutility.external.test.service.log.log4j;

import org.apache.log4j.Logger;

import devutility.internal.test.BaseService;
import devutility.internal.test.ServiceExecutor;

public class DateTimeAppenderService extends BaseService {
	@Override
	public void run() {
		Logger logger = Logger.getLogger(DateTimeAppenderService.class);
		logger.trace("trace log output!");
		logger.debug("debug log output!");
		logger.info("info log output!");
		logger.warn("warn log output!");
		logger.error("error log output!");
		logger.fatal("fatal log output!");
	}
	
	public static void main(String[] args) {
		ServiceExecutor.run(new DateTimeAppenderService());
	}
}