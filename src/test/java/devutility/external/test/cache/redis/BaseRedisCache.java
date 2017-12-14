package devutility.external.test.cache.redis;

import devutility.external.test.cache.BaseCache;
import devutility.external.test.config.DBConfig;
import devutility.internal.dao.models.RedisInstance;

public class BaseRedisCache extends BaseCache {
	protected RedisInstance redisInstance;

	public BaseRedisCache() {
		redisInstance = DBConfig.getRedisInstance();
	}
}