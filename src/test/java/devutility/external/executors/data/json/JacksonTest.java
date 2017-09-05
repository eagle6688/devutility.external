package devutility.external.executors.data.json;

import devutility.external.service.data.json.JacksonService;
import devutility.internal.test.ServiceExecutor;

public class JacksonTest {
	public static void main(String[] args) {
		ServiceExecutor.run(new JacksonService());
	}
}