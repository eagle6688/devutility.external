package devutility.external.test.cache.redis;

import devutility.external.dao.redis.RedisHelper;
import redis.clients.jedis.Jedis;

public class ProductCache extends BaseRedisCache {
	public String get(String key) {
		try (Jedis jedis = RedisHelper.getJedis(redisInstance)) {
			return jedis.get(key);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
}