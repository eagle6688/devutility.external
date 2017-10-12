package devutility.external.service.log.slf4j;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import devutility.internal.test.BaseService;

public class IntegrateLog4jService extends BaseService {
	@Override
	public void run() {
		Logger logger = LoggerFactory.getLogger(IntegrateLog4jService.class);
		logger.debug("debug log output!");
		logger.error("error log output!");
	}
}