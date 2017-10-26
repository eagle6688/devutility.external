package devutility.external.dao.mongodb;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import org.bson.Document;

import devutility.internal.data.BeanHelper;

public class MongoDBHelper {
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
		List<T> list = new ArrayList<T>();
		Field[] fields = cl.getDeclaredFields();
		Method[] methods = cl.getDeclaredMethods();

		for (Document document : documents) {
			T model = toBean(document, fields, methods, cl);
			list.add(model);
		}

		return list;
	}

	// endregion
}