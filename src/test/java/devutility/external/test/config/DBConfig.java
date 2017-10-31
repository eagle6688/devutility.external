package devutility.external.test.config;

import devutility.internal.dao.RedisInstance;
import devutility.internal.dao.helper.RedisInstanceHelper;

public class DBConfig {
	public static final String CONFIG = "dbconfig.properties";

	public static RedisInstance getRedisInstance() {
		return RedisInstanceHelper.getInstance(CONFIG, "redis");
	}
}