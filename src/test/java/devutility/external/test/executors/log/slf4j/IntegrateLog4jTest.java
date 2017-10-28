package devutility.external.test.executors.log.slf4j;

import devutility.external.test.service.log.slf4j.IntegrateLog4jService;
import devutility.internal.test.ServiceExecutor;

public class IntegrateLog4jTest {
	public static void main(String[] args) {
		ServiceExecutor.run(new IntegrateLog4jService());
	}
}