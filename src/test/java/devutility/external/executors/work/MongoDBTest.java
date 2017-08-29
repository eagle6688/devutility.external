package devutility.external.executors.work;

import devutility.external.service.work.GetProductsService;
import devutility.internal.test.ServiceExecutor;

public class MongoDBTest {
	public static void main(String[] args) {
		ServiceExecutor.run(new GetProductsService());
	}
}