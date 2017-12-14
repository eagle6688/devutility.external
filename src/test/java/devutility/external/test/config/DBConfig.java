package devutility.external.test.config;

import devutility.internal.dao.RedisInstanceHelper;
import devutility.internal.dao.models.RedisInstance;

public class DBConfig {
	public static final String CONFIG = "dbconfig.properties";

	public static RedisInstance getRedisInstance() {
		return RedisInstanceHelper.getInstance(CONFIG, "redis");
	}
}