package devutility.external.test.config;

import devutility.external.dao.redis.RedisHelper;
import devutility.internal.dao.RedisInstanceHelper;
import devutility.internal.dao.models.RedisInstance;

public class DbConfig {
	public static final String CONFIG = "dbconfig.properties";

	private static class RedisHolder {
		private static RedisInstance redisInstance = RedisInstanceHelper.getInstance(CONFIG, "redis");
		public static RedisHelper redisHelper = new RedisHelper(redisInstance);
	}

	public static RedisHelper redisHelper() {
		return RedisHolder.redisHelper;
	}
}