package devutility.external.test.executors.data.json;

import devutility.external.test.service.data.json.JacksonService;
import devutility.internal.test.ServiceExecutor;

public class JacksonTest {
	public static void main(String[] args) {
		ServiceExecutor.run(new JacksonService());
	}
}