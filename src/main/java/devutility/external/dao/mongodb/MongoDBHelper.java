package devutility.external.dao.mongodb;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang3.ArrayUtils;
import org.bson.Document;

import com.mongodb.MongoClient;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;

import devutility.internal.dao.models.DBInstance;
import devutility.internal.data.BeanHelper;
import devutility.internal.lang.reflect.GenericTypeHelper;

public class MongoDBHelper {
	// region create MongoCredential

	public static MongoCredential createMongoCredential(DBInstance dbInstance) {
		if (dbInstance.getLoginName() == null) {
			return null;
		}

		return MongoCredential.createCredential(dbInstance.getLoginName(), dbInstance.getDatabase(), dbInstance.getPassword().toCharArray());
	}

	// endregion

	// region create ServerAddress

	public static ServerAddress createServerAddress(DBInstance dbInstance) {
		return new ServerAddress(dbInstance.getHost(), dbInstance.getPort());
	}

	// endregion

	// region create MongoClient

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

		T model = cl.newInstance();

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