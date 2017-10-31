package devutility.external.test.service.cache.redis;

import devutility.external.test.cache.redis.ProductCache;
import devutility.internal.test.BaseService;
import devutility.internal.test.ServiceExecutor;

public class JedisUsageService extends BaseService {
	@Override
	public void run() {
		ProductCache productCache = new ProductCache();
		
		for (int i = 0; i < 5; i++) {
			println(productCache.get("C-GAG001ORCL-Models"));
		}
	}

	public static void main(String[] args) {
		ServiceExecutor.run(new JedisUsageService());
	}
}