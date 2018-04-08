package devutility.external.dao.mongodb;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.data.mongodb.core.BulkOperations;
import org.springframework.data.mongodb.core.BulkOperations.BulkMode;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.data.util.Pair;

import com.mongodb.bulk.BulkWriteResult;

import devutility.internal.lang.ClassHelper;
import devutility.internal.lang.models.EntityField;

public class BulkOperationsHelper {
	private MongoOperations mongoOperations;

	/**
	 * BulkOperationsHelper
	 * @param mongoOperations
	 */
	public BulkOperationsHelper(MongoOperations mongoOperations) {
		this.mongoOperations = mongoOperations;
	}

	/**
	 * bulkOperations
	 * @param bulkMode
	 * @param clazz
	 * @return BulkOperations
	 */
	public BulkOperations bulkOperations(BulkMode bulkMode, Class<?> clazz) {
		return mongoOperations.bulkOps(bulkMode, clazz);
	}

	/**
	 * bulkOperations
	 * @param bulkMode
	 * @param collection
	 * @return BulkOperations
	 */
	public BulkOperations bulkOperations(BulkMode bulkMode, String collection) {
		return mongoOperations.bulkOps(bulkMode, collection);
	}

	/**
	 * entityToQueryAndUpdate
	 * @param entity
	 * @param entityFields
	 * @return Pair<Query,Update>
	 * @throws IllegalAccessException
	 * @throws IllegalArgumentException
	 * @throws InvocationTargetException
	 */
	public static <T> Pair<Query, Update> entityToQueryAndUpdate(T entity, List<EntityField> entityFields) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		Query query = new Query();
		Update update = new Update();

		for (EntityField entityField : entityFields) {
			boolean hasAnnotation = false;
			Object value = entityField.getValue(entity);
			Annotation[] annotations = entityField.getField().getAnnotations();

			for (Annotation annotation : annotations) {
				if (MongoDBHelper.isField(annotation)) {
					hasAnnotation = true;
					update.set(MongoDBHelper.getFieldName(annotation), value);
					break;
				}

				if (MongoDBHelper.isPk(annotation)) {
					query.addCriteria(Criteria.where("_id").is(value));
					update.set("_id", value);
					hasAnnotation = true;
					break;
				}
			}

			if (!hasAnnotation) {
				update.set(entityField.getField().getName(), value);
			}
		}

		return Pair.of(query, update);
	}

	/**
	 * entitiesToPairs
	 * @param list
	 * @param clazz
	 * @return List<Pair<Query,Update>>
	 * @throws IllegalAccessException
	 * @throws IllegalArgumentException
	 * @throws InvocationTargetException
	 */
	public static <T> List<Pair<Query, Update>> entitiesToPairs(List<T> list, Class<T> clazz) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		List<Pair<Query, Update>> pairs = new ArrayList<>(list.size());
		List<EntityField> entityFields = ClassHelper.getEntityFields(clazz);

		for (T entity : list) {
			pairs.add(entityToQueryAndUpdate(entity, entityFields));
		}

		return pairs;
	}

	/**
	 * entitiesToPairs
	 * @param list
	 * @param fields
	 * @param clazz
	 * @return List<Pair<Query,Update>>
	 * @throws IllegalAccessException
	 * @throws IllegalArgumentException
	 * @throws InvocationTargetException
	 */
	public static <T> List<Pair<Query, Update>> entitiesToPairs(List<T> list, List<String> fields, Class<T> clazz) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		List<Pair<Query, Update>> pairs = new ArrayList<>(list.size());
		List<EntityField> entityFields = ClassHelper.getEntityFields(clazz);

		for (T entity : list) {
			Query query = MongoDBHelper.entityToQuery(entity, fields, entityFields);
			Update update = MongoDBHelper.entityToUpdate(entity, entityFields);
			pairs.add(Pair.of(query, update));
		}

		return pairs;
	}

	/**
	 * save
	 * @param list
	 * @param clazz
	 * @return BulkWriteResult
	 * @throws IllegalAccessException
	 * @throws IllegalArgumentException
	 * @throws InvocationTargetException BulkWriteResult
	 */
	public <T> BulkWriteResult save(List<T> list, Class<T> clazz) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		BulkOperations bulkOperations = bulkOperations(BulkMode.UNORDERED, clazz);
		List<Pair<Query, Update>> pairs = entitiesToPairs(list, clazz);
		bulkOperations.upsert(pairs);
		return bulkOperations.execute();
	}

	/**
	 * save
	 * @param list
	 * @param fields
	 * @param clazz
	 * @return BulkWriteResult
	 * @throws IllegalAccessException
	 * @throws IllegalArgumentException
	 * @throws InvocationTargetException
	 */
	public <T> BulkWriteResult save(List<T> list, List<String> fields, Class<T> clazz) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		BulkOperations bulkOperations = bulkOperations(BulkMode.UNORDERED, clazz);
		List<Pair<Query, Update>> pairs = entitiesToPairs(list, fields, clazz);
		bulkOperations.upsert(pairs);
		return bulkOperations.execute();
	}
}