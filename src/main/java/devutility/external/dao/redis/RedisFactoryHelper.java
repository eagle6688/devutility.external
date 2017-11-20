package devutility.external.dao.redis;

import devutility.internal.dao.RedisInstance;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

public class RedisFactoryHelper {
	private static volatile JedisPool jedisPool;

	public static JedisPoolConfig getJedisPoolConfig(RedisInstance redisInstance) {
		JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
		jedisPoolConfig.setMaxTotal(redisInstance.getMaxConnections());
		return jedisPoolConfig;
	}

	public static JedisPool getJedisPool(JedisPoolConfig jedisPoolConfig, RedisInstance redisInstance) {
		if (jedisPool != null) {
			return jedisPool;
		}

		synchronized (RedisFactoryHelper.class) {
			if (jedisPool == null) {
				jedisPool = new JedisPool(jedisPoolConfig, redisInstance.getHost(), redisInstance.getPort(), redisInstance.getTimeout());
			}
		}

		return jedisPool;
	}

	public static Jedis getJedis(RedisInstance redisInstance) {
		if (redisInstance == null) {
			return null;
		}

		JedisPoolConfig jedisPoolConfig = getJedisPoolConfig(redisInstance);
		JedisPool jedisPool = getJedisPool(jedisPoolConfig, redisInstance);

		if (jedisPool == null) {
			throw new NullPointerException("JedisPool instance cannot be created!");
		}

		Jedis jedis = jedisPool.getResource();

		if (jedis != null) {
			jedis.select(redisInstance.getDBIndex());
		}

		return jedis;
	}
}