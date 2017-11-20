package devutility.external.dao.redis;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import devutility.external.data.codec.ObjectCompressHelper;
import devutility.internal.dao.RedisInstance;
import devutility.internal.lang.StringHelper;
import redis.clients.jedis.Jedis;

public class RedisHelper {
	private RedisInstance redisInstance;
	private int dbIndex;
	private int pageSize;

	// region constructor

	public RedisHelper(RedisInstance redisInstance) {
		this.redisInstance = redisInstance;
	}

	// endregion

	// region int

	public int intGet(String key) {
		String value = stringGet(key);

		if (StringHelper.isNullOrEmpty(value)) {
			return 0;
		}

		return Integer.parseInt(value);
	}

	public boolean intSet(String key, int value) {
		return stringSet(key, String.valueOf(value));
	}

	// endregion

	// region string

	public String stringGet(String key) {
		if (StringHelper.isNullOrEmpty(key)) {
			return null;
		}

		try (Jedis jedis = RedisFactoryHelper.getJedis(redisInstance)) {
			return jedis.get(key);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public boolean stringSet(String key, String value) {
		return stringSet(key, value, 0);
	}

	public boolean stringSet(String key, String value, int expireSeconds) {
		if (StringHelper.isNullOrEmpty(key) || value == null) {
			return false;
		}

		try (Jedis jedis = RedisFactoryHelper.getJedis(redisInstance)) {
			jedis.set(key, value);

			if (expireSeconds > 0) {
				jedis.expire(key, expireSeconds);
			}
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}

		return true;
	}

	// endregion

	// region object

	public <T> T objectGet(String key, Class<T> clazz) throws IOException {
		String value = stringGet(key);

		if (StringHelper.isNullOrEmpty(value)) {
			return null;
		}

		return ObjectCompressHelper.decompress(value, clazz);
	}

	public boolean objectSet(String key, Object value) throws IOException {
		return objectSet(key, value, 0);
	}

	public boolean objectSet(String key, Object value, int expireSeconds) throws IOException {
		String compressedValue = ObjectCompressHelper.compress(value);

		if (StringHelper.isNullOrEmpty(compressedValue)) {
			return false;
		}

		return stringSet(key, compressedValue, expireSeconds);
	}

	// endregion

	// region expire

	public boolean expire(String key, int seconds) {
		if (StringHelper.isNullOrEmpty(key)) {
			return false;
		}

		try (Jedis jedis = RedisFactoryHelper.getJedis(redisInstance)) {
			return jedis.expire(key, seconds) == 1;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	// endregion

	// region remove

	public boolean remove(String key) {
		if (StringHelper.isNullOrEmpty(key)) {
			return false;
		}

		try (Jedis jedis = RedisFactoryHelper.getJedis(redisInstance)) {
			return jedis.del(key) > 0;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	// endregion

	// region paging

	public int pagingGetCount(String originalKey) {
		if (StringHelper.isNullOrEmpty(originalKey)) {
			return 0;
		}

		String key = RedisPagingHelper.getPagesCountKey(originalKey);
		return intGet(key);
	}

	public String[][] pagingGetArray(String originalKey, int pageIndex) throws IOException {
		String key = RedisPagingHelper.getPagingArrayKey(originalKey, pageIndex);
		return objectGet(key, String[][].class);
	}

	public List<String[]> pagingGetList(String originalKey) throws IOException {
		List<String[]> list = new ArrayList<>();
		int pagesCount = pagingGetCount(originalKey);

		if (pagesCount == 0) {
			return Arrays.asList(objectGet(originalKey, String[][].class));
		}

		for (int index = 0; index < pagesCount; index++) {
			list.addAll(Arrays.asList(pagingGetArray(originalKey, index)));
		}

		return list;
	}

	public boolean pagingSetCount(String originalKey, int count) {
		if (StringHelper.isNullOrEmpty(originalKey) || count < 1) {
			return false;
		}

		String key = RedisPagingHelper.getPagesCountKey(originalKey);
		return intSet(key, count);
	}

	public boolean pagingSetArray(String[][] array, String originalKey, int pageIndex) throws IOException {
		String key = RedisPagingHelper.getPagingArrayKey(originalKey, pageIndex);
		String[][] pagingArray = RedisPagingHelper.pagingArray(array, pageIndex, pageSize);
		return objectSet(key, pagingArray);
	}

	// endregion

	// region other

	public int getDbIndex() {
		return dbIndex;
	}

	public void setDbIndex(int dbIndex) {
		this.dbIndex = dbIndex;
	}

	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	// endregion
}