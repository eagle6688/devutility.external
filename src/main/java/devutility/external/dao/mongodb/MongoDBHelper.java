package devutility.external.dao.mongodb;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.mongodb.MongoDbFactory;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.SimpleMongoDbFactory;
import org.springframework.data.mongodb.core.convert.DbRefResolver;
import org.springframework.data.mongodb.core.convert.DefaultDbRefResolver;
import org.springframework.data.mongodb.core.convert.DefaultMongoTypeMapper;
import org.springframework.data.mongodb.core.convert.MappingMongoConverter;
import org.springframework.data.mongodb.core.convert.MongoCustomConversions;
import org.springframework.data.mongodb.core.index.Index;
import org.springframework.data.mongodb.core.mapping.MongoMappingContext;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.data.util.Pair;

import com.mongodb.MongoClient;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;
import com.mongodb.client.result.UpdateResult;

import devutility.internal.dao.models.DbInstance;
import devutility.internal.lang.models.EntityField;

public class MongoDBHelper {
	/**
	 * createMongoCredential
	 * @param dbInstance Database instance
	 * @return MongoCredential
	 */
	public static MongoCredential createMongoCredential(DbInstance dbInstance) {
		if (dbInstance.getLoginName() == null) {
			return null;
		}

		return MongoCredential.createCredential(dbInstance.getLoginName(), dbInstance.getDatabase(), dbInstance.getPassword().toCharArray());
	}

	/**
	 * createServerAddress
	 * @param dbInstance Database instance
	 * @return ServerAddress
	 */
	public static ServerAddress createServerAddress(DbInstance dbInstance) {
		if (dbInstance == null) {
			return null;
		}

		return new ServerAddress(dbInstance.getHost(), dbInstance.getPort());
	}

	/**
	 * mongoClient
	 * @param dbInstance
	 * @return MongoClient
	 */
	public static MongoClient mongoClient(DbInstance dbInstance) {
		if (dbInstance == null) {
			return null;
		}

		MongoCredential mongoCredential = MongoDBHelper.createMongoCredential(dbInstance);
		ServerAddress serverAddress = MongoDBHelper.createServerAddress(dbInstance);
		return mongoClient(serverAddress, mongoCredential);
	}

	/**
	 * createMongoClient
	 * @param serverAddress Object
	 * @param mongoCredential Object
	 * @return MongoClient
	 */
	public static MongoClient mongoClient(ServerAddress serverAddress, MongoCredential mongoCredential) {
		if (serverAddress == null) {
			return null;
		}

		if (mongoCredential == null) {
			return new MongoClient(serverAddress);
		}

		return new MongoClient(serverAddress, Arrays.asList(mongoCredential));
	}

	/**
	 * mongoTemplate
	 * @param dbInstance
	 * @return MongoTemplate
	 */
	public static MongoTemplate mongoTemplate(DbInstance dbInstance) {
		if (dbInstance == null) {
			return null;
		}

		MongoDbFactory mongoDbFactory = mongoDbFactory(dbInstance);
		MappingMongoConverter mappingMongoConverter = mappingMongoConverter(dbInstance);
		return new MongoTemplate(mongoDbFactory, mappingMongoConverter);
	}

	/**
	 * mongoDbFactory
	 * @param dbInstance
	 * @return MongoDbFactory
	 */
	public static MongoDbFactory mongoDbFactory(DbInstance dbInstance) {
		if (dbInstance == null) {
			return null;
		}

		MongoClient mongoClient = mongoClient(dbInstance);
		return new SimpleMongoDbFactory(mongoClient, dbInstance.getDatabase());
	}

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

	/**
	 * mongoMappingContext
	 * @return MongoMappingContext
	 */
	public static MongoMappingContext mongoMappingContext() {
		MongoMappingContext mappingContext = new MongoMappingContext();
		return mappingContext;
	}

	/**
	 * mappingMongoConverter
	 * @param mongoDbFactory
	 * @param mongoMappingContext
	 * @return MappingMongoConverter
	 */
	public static MappingMongoConverter mappingMongoConverter(MongoDbFactory mongoDbFactory, MongoMappingContext mongoMappingContext) {
		if (mongoDbFactory == null || mongoMappingContext == null) {
			return null;
		}

		DbRefResolver dbRefResolver = new DefaultDbRefResolver(mongoDbFactory);

		MappingMongoConverter mappingMongoConverter = new MappingMongoConverter(dbRefResolver, mongoMappingContext);
		mappingMongoConverter.setCustomConversions(new MongoCustomConversions(Collections.emptyList()));
		mappingMongoConverter.setTypeMapper(new DefaultMongoTypeMapper(null));
		return mappingMongoConverter;
	}

	/**
	 * mappingMongoConverter
	 * @param dbInstance
	 * @return MappingMongoConverter
	 */
	public static MappingMongoConverter mappingMongoConverter(DbInstance dbInstance) {
		if (dbInstance == null) {
			return null;
		}

		MongoDbFactory mongoDbFactory = mongoDbFactory(dbInstance);
		MongoMappingContext mongoMappingContext = mongoMappingContext();
		return mappingMongoConverter(mongoDbFactory, mongoMappingContext);
	}

	/**
	 * isPk
	 * @param annotation object
	 * @return boolean
	 */
	public static boolean isPk(Annotation annotation) {
		return org.springframework.data.annotation.Id.class == annotation.annotationType();
	}

	/**
	 * isField
	 * @param annotation object
	 * @return boolean
	 */
	public static boolean isField(Annotation annotation) {
		return org.springframework.data.mongodb.core.mapping.Field.class.equals(annotation.annotationType());
	}

	/**
	 * getFieldName
	 * @param annotation object
	 * @return String
	 */
	public static String getFieldName(Annotation annotation) {
		org.springframework.data.mongodb.core.mapping.Field field = org.springframework.data.mongodb.core.mapping.Field.class.cast(annotation);
		return field.value();
	}

	public static <T> Pair<Query, Update> entityToUpdate(T entity, List<EntityField> entityFields) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		Query query = new Query();
		Update update = new Update();

		for (EntityField entityField : entityFields) {
			boolean hasAnnotation = false;
			Object value = entityField.getValue(entity);
			Annotation[] annotations = entityField.getField().getAnnotations();

			for (Annotation annotation : annotations) {
				if (isField(annotation)) {
					hasAnnotation = true;
					update.set(getFieldName(annotation), value);
					break;
				}

				if (isPk(annotation)) {
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
}