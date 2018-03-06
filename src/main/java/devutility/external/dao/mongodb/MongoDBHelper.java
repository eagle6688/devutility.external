package devutility.external.dao.mongodb;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang3.ArrayUtils;
import org.bson.Document;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.index.Index;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

import com.mongodb.MongoClient;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;
import com.mongodb.client.result.UpdateResult;

import devutility.internal.dao.models.DbInstance;
import devutility.internal.data.BeanHelper;
import devutility.internal.lang.ClassHelper;
import devutility.internal.lang.reflect.GenericTypeHelper;

public class MongoDBHelper {
	// region create MongoCredential

	public static MongoCredential createMongoCredential(DbInstance dbInstance) {
		if (dbInstance.getLoginName() == null) {
			return null;
		}

		return MongoCredential.createCredential(dbInstance.getLoginName(), dbInstance.getDatabase(), dbInstance.getPassword().toCharArray());
	}

	// endregion

	// region create ServerAddress

	public static ServerAddress createServerAddress(DbInstance dbInstance) {
		return new ServerAddress(dbInstance.getHost(), dbInstance.getPort());
	}

	// endregion

	// region create MongoClient

	/**
	 * createMongoClient
	 * @param dbInstance
	 * @return MongoClient
	 */
	public static MongoClient createMongoClient(DbInstance dbInstance) {
		if (dbInstance == null) {
			return null;
		}

		MongoCredential mongoCredential = MongoDBHelper.createMongoCredential(dbInstance);
		ServerAddress serverAddress = MongoDBHelper.createServerAddress(dbInstance);
		return createMongoClient(serverAddress, mongoCredential);
	}

	/**
	 * createMongoClient
	 * @param serverAddress
	 * @param mongoCredential
	 * @return MongoClient
	 */
	public static MongoClient createMongoClient(ServerAddress serverAddress, MongoCredential mongoCredential) {
		if (serverAddress == null) {
			return null;
		}

		if (mongoCredential == null) {
			return new MongoClient(serverAddress);
		}

		return new MongoClient(serverAddress, Arrays.asList(mongoCredential));
	}

	// endregion

	/**
	 * createIndex 
	 * @param field
	 * @return Index
	 */
	private static Index createIndex(String field) {
		return createIndex(field, false);
	}

	/**
	 * createIndex 
	 * @param field
	 * @param uniqued
	 * @return Index
	 */
	private static Index createIndex(String field, boolean uniqued) {
		Index index = new Index().on(field, Direction.ASC);

		if (uniqued) {
			return index.unique();
		}

		return index;
	}

	/**
	 * createIndex 
	 * @param mongoOperations
	 * @param clazz
	 * @param field void
	 */
	public static <T> void createIndex(MongoOperations mongoOperations, Class<T> clazz, String field) {
		mongoOperations.indexOps(clazz).ensureIndex(createIndex(field));
	}

	/**
	 * update
	 * @param mongoOperations
	 * @param id
	 * @param setField
	 * @param setValue
	 * @param clazz
	 * @return UpdateResult
	 */
	public static UpdateResult update(MongoOperations mongoOperations, String id, String setField, Object setValue, Class<?> clazz) {
		return update(mongoOperations, "_id", id, setField, setValue, clazz);
	}

	/**
	 * update
	 * @param mongoOperations
	 * @param keyField
	 * @param keyValue
	 * @param setField
	 * @param setValue
	 * @param clazz
	 * @return UpdateResult
	 */
	public static UpdateResult update(MongoOperations mongoOperations, String keyField, Object keyValue, String setField, Object setValue, Class<?> clazz) {
		Query query = new Query();
		query.addCriteria(Criteria.where(keyField).is(keyValue));

		Update update = new Update();
		update.set(setField, setValue);

		return mongoOperations.updateMulti(query, update, clazz);
	}

	// region to bean

	public static <T> T toBean(Document document, Class<T> cl) throws Exception {
		Field[] fields = cl.getDeclaredFields();
		Method[] methods = cl.getDeclaredMethods();
		return toBean(document, fields, methods, cl);
	}

	private static <T> T toBean(Document document, Field[] fields, Method[] methods, Class<T> cl) throws Exception {
		if (document == null || fields == null || fields.length == 0 || methods == null || methods.length == 0) {
			return null;
		}

		T model = ClassHelper.newInstance(cl);

		for (Field field : fields) {
			String fieldName = field.getName();
			Object value = document.get(fieldName);

			if (value == null) {
				continue;
			}

			if (value instanceof Document) {
				value = toBean((Document) value, field.getType());
			}

			if (value instanceof ArrayList) {
				value = toBeansByList(value, GenericTypeHelper.getListClassByField(field));
			}

			for (Method method : methods) {
				if (BeanHelper.isSetter(method, fieldName)) {
					method.invoke(model, value);
				}
			}
		}

		return model;
	}

	// endregion

	// region to bean list

	public static <T> List<T> toBeans(List<Document> documents, Class<T> cl) throws Exception {
		if (documents == null || documents.isEmpty()) {
			return new ArrayList<T>();
		}

		List<T> list = new ArrayList<T>();
		Field[] fields = cl.getDeclaredFields();
		Method[] methods = cl.getDeclaredMethods();

		for (Document document : documents) {
			T model = toBean(document, fields, methods, cl);
			list.add(model);
		}

		return list;
	}

	private static <T> List<T> toBeansByList(Object value, Class<T> cl) throws Exception {
		ArrayList<?> list = (ArrayList<?>) value;

		if (list == null || list.isEmpty()) {
			return new ArrayList<T>();
		}

		if (!(list.get(0) instanceof Document)) {
			return new ArrayList<T>();

		}

		ArrayList<Document> documents = new ArrayList<>();

		list.stream().forEach(i -> {
			documents.add((Document) i);
		});

		return toBeans(documents, cl);
	}

	// endregion

	// region to document

	public static Document toDocument(Object obj, String[] excludeFields) throws Exception {
		Class<?> clazz = obj.getClass();
		Field[] fields = clazz.getFields();
		Method[] methods = clazz.getMethods();
		return toDocument(obj, fields, methods, excludeFields);
	}

	public static Document toDocument(Object obj, Field[] fields, Method[] methods, String[] excludeFields) throws Exception {
		if (obj == null || fields == null || fields.length == 0 || methods == null || methods.length == 0) {
			return null;
		}

		Document document = new Document();
		ArrayList<Field> targetFields = getTargetFields(fields, excludeFields);

		for (Field targetField : targetFields) {
			Object value = null;
			String fieldName = targetField.getName();

			for (Method method : methods) {
				if (!BeanHelper.isGetter(method, fieldName)) {
					continue;
				}

				value = method.invoke(obj);

				if (value == null) {
					document.append(fieldName, value);
					continue;
				}

				if (value instanceof ArrayList) {
					Class<?> clazz = GenericTypeHelper.getListClassByField(targetField);

					if (clazz == null) {
						continue;
					}

				}

				document.append(fieldName, value);
			}
		}

		return document;
	}

	// endregion

	private static ArrayList<Field> getTargetFields(Field[] fields, String[] excludeFields) {
		ArrayList<Field> targetFields = new ArrayList<>();

		Arrays.stream(fields).forEach(i -> {
			String fieldName = i.getName();

			if (!ArrayUtils.contains(excludeFields, fieldName)) {
				targetFields.add(i);
			}
		});

		return targetFields;
	}
}