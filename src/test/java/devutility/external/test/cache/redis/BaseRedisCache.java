package devutility.external.test.cache.redis;

import devutility.external.dao.redis.RedisHelper;
import devutility.external.test.config.DbConfig;
import devutility.internal.test.BaseTest;

public abstract class BaseRedisCache extends BaseTest {
	protected RedisHelper redisHelper = DbConfig.redisHelper();
}