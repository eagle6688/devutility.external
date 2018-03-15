package devutility.external.dao.mongodb;

import java.util.Arrays;
import java.util.Collections;

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

import com.mongodb.MongoClient;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;
import com.mongodb.client.result.UpdateResult;

import devutility.internal.dao.models.DbInstance;

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
	 * createMongoDbFactory
	 * @param dbInstance Object of database
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
	 * @return Index
	 */
	private static Index createIndex(String field) {
		return createIndex(field, false);
	}

	/**
	 * createIndex 
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
	 */
	public static <T> void createIndex(MongoOperations mongoOperations, Class<T> clazz, String field) {
		mongoOperations.indexOps(clazz).ensureIndex(createIndex(field));
	}

	/**
	 * update
	 * @return UpdateResult
	 */
	public static UpdateResult update(MongoOperations mongoOperations, String id, String setField, Object setValue, Class<?> clazz) {
		return update(mongoOperations, "_id", id, setField, setValue, clazz);
	}

	/**
	 * update
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
}