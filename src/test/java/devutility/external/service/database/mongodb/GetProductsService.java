package devutility.external.service.database.mongodb;

import org.bson.Document;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

import devutility.internal.test.BaseService;

public class GetProductsService extends BaseService {
	@Override
	public void run() {
		try (MongoClient mongoClient = new MongoClient("mongodb://10.122.22.94:27017")) {
			MongoDatabase mongoDatabase = mongoClient.getDatabase("SupportDB26");
			MongoCollection<Document> collection = mongoDatabase.getCollection("Products");
			Document document = collection.find().first();
			String json = document.toJson();
			println(json);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}