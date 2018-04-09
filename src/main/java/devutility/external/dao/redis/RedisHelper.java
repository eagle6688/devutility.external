package devutility.external.dao.redis;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import devutility.external.data.codec.ObjectCompressHelper;
import devutility.internal.base.Convertor;
import devutility.internal.dao.models.RedisInstance;
import devutility.internal.data.BeanHelper;
import devutility.internal.lang.ClassHelper;
import devutility.internal.lang.StringHelper;
import devutility.internal.lang.models.EntityField;
import redis.clients.jedis.Jedis;

public class RedisHelper {
	private RedisInstance redisInstance;
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

	// region entities

	public <T> boolean entitiesSet(String key, List<T> entities, Class<T> clazz) throws IllegalArgumentException, IllegalAccessException, IOException, InvocationTargetException {
		String[][] arrays = toArrays(entities, clazz);
		return objectSet(key, arrays);
	}

	private <T> String[][] toArrays(List<T> entities, Class<T> clazz) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException {
		List<EntityField> entityFields = ClassHelper.getEntityFields(clazz);

		if (entities.size() == 0 || entityFields.size() == 0) {
			return null;
		}

		String[][] arrays = new String[entities.size()][];

		for (int i = 0; i < entities.size(); i++) {
			T entity = entities.get(i);
			String[] array = toArray(entity, entityFields);

			if (array != null) {
				arrays[i] = array;
			}
		}

		return arrays;
	}

	private <T> String[] toArray(T entity, List<EntityField> entityFields) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException {
		if (entity == null || entityFields.size() == 0) {
			return null;
		}

		String[] array = new String[entityFields.size()];

		for (int i = 0; i < entityFields.size(); i++) {
			EntityField entityField = entityFields.get(i);
			Method method = entityField.getGetter();
			Object value = method.invoke(entity);

			if (value != null) {
				array[i] = Convertor.objectToString(value);
			}
		}

		return array;
	}

	public <T> List<T> entitiesGet(String key, Class<T> clazz) throws IOException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		String[][] arrays = objectGet(key, String[][].class);
		return toEntities(arrays, clazz);
	}

	private <T> List<T> toEntities(String[][] arrays, Class<T> clazz) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		List<T> list = new ArrayList<>();
		List<EntityField> entityFields = ClassHelper.getEntityFields(clazz);

		if (arrays == null || arrays.length == 0 || clazz == null || entityFields.size() == 0) {
			return list;
		}

		for (int i = 0; i < arrays.length; i++) {
			T entity = toEntity(arrays[i], entityFields, clazz);

			if (entity != null) {
				list.add(entity);
			}
		}

		return list;
	}

	private <T> T toEntity(String[] array, List<EntityField> entityFields, Class<T> clazz) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		if (array == null || array.length == 0 || entityFields.size() == 0) {
			return null;
		}

		T entity = ClassHelper.newInstance(clazz);

		for (int i = 0; i < entityFields.size(); i++) {
			if (array[i] == null) {
				continue;
			}

			EntityField entityField = entityFields.get(i);
			Field field = entityField.getField();
			Method setter = entityField.getSetter();
			BeanHelper.setField(setter, entity, array[i], field);
		}

		return entity;
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
			String[][] array = objectGet(originalKey, String[][].class);

			if (array == null) {
				return new ArrayList<String[]>();
			}

			return Arrays.asList(array);
		}

		for (int index = 0; index < pagesCount; index++) {
			String[][] array = pagingGetArray(originalKey, index);

			if (array != null) {
				list.addAll(Arrays.asList(array));
			}
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
		return this.redisInstance.getDBIndex();
	}

	public void setDbIndex(int dbIndex) {
		this.redisInstance.setDBIndex(dbIndex);
	}

	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	// endregion
}