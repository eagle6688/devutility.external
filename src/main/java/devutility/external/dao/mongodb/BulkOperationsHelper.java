package devutility.external.dao.mongodb;

import java.util.List;

import org.springframework.data.mongodb.core.BulkOperations;
import org.springframework.data.mongodb.core.BulkOperations.BulkMode;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

public class BulkOperationsHelper {
	private MongoOperations mongoOperations;

	/**
	 * BulkOperationsHelper
	 * @param mongoOperations
	 */
	public BulkOperationsHelper(MongoOperations mongoOperations) {
		this.mongoOperations = mongoOperations;
	}

	protected BulkOperations bulkOperations(BulkMode bulkMode, Class<?> clazz) {
		return mongoOperations.bulkOps(bulkMode, clazz);
	}

	protected BulkOperations bulkOperations(BulkMode bulkMode, String collection) {
		return mongoOperations.bulkOps(bulkMode, collection);
	}

	public int save(Query query, Update update, Class<?> clazz) {

		return 0;
	}

	public <T> int save(List<T> list, BulkOperations bulkOperations, Class<T> clazz) {

		return 0;
	}
}