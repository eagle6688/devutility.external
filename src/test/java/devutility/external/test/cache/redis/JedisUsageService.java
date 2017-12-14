package devutility.external.test.cache.redis;

import devutility.internal.test.BaseTest;
import devutility.internal.test.TestExecutor;

public class JedisUsageService extends BaseTest {
	@Override
	public void run() {
		ProductCache productCache = new ProductCache();
		
		for (int i = 0; i < 5; i++) {
			println(productCache.get("C-GAG001ORCL-Models"));
		}
	}

	public static void main(String[] args) {
		TestExecutor.run(new JedisUsageService());
	}
}