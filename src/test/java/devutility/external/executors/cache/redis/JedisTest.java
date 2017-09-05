package devutility.external.executors.cache.redis;

import devutility.external.service.cache.redis.JedisUsageService;
import devutility.internal.test.ServiceExecutor;

public class JedisTest {
	public static void main(String[] args) {
		ServiceExecutor.run(new JedisUsageService());
	}
}