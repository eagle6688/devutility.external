package devutility.external.dao.mongodb;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import org.bson.Document;

import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;

import devutility.internal.dao.DBInstance;
import devutility.internal.data.BeanHelper;
import devutility.internal.lang.reflect.GenericTypeHelper;

public class MongoDBHelper {
	// region create MongoCredential

	public static MongoCredential createMongoCredential(DBInstance dbInstance) {
		return MongoCredential.createCredential(dbInstance.getLoginName(), dbInstance.getDatabase(),
				dbInstance.getPassword().toCharArray());
	}

	// endregion

	// region create ServerAddress

	public static ServerAddress createServerAddress(DBInstance dbInstance) {
		return new ServerAddress(dbInstance.getHost(), dbInstance.getPort());
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
}