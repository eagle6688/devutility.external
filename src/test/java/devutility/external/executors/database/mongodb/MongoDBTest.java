package devutility.external.executors.database.mongodb;

import devutility.external.service.database.mongodb.GetProductsService;
import devutility.internal.test.ServiceExecutor;

public class MongoDBTest {
	public static void main(String[] args) {
		ServiceExecutor.run(new GetProductsService());
	}
}