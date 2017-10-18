package devutility.external.service.log.log4j;

import org.apache.log4j.Logger;

import devutility.internal.test.BaseService;

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
}