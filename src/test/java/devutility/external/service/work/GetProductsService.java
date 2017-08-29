package devutility.external.service.work;

import org.bson.Document;
import org.bson.conversions.Bson;

import com.mongodb.MongoClient;
import com.mongodb.client.AggregateIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import static com.mongodb.client.model.Aggregates.*;
import static com.mongodb.client.model.Filters.*;
import static com.mongodb.client.model.Projections.*;

import java.util.ArrayList;

import devutility.internal.test.BaseService;

public class GetProductsService extends BaseService {
	@Override
	public void run() {
		try (MongoClient mongoClient = new MongoClient("10.122.22.94", 27017)) {
			MongoDatabase mongoDatabase = mongoClient.getDatabase("SupportDB26");

			// region get dmu

			MongoCollection<Document> dmuCollection = mongoDatabase.getCollection("GlobalAccounts");
			ArrayList<Bson> dmuSelectConditions = new ArrayList<>();
			dmuSelectConditions.add(match(eq("ID", "GAG001ORCL")));

			Bson dmuFields = fields(include("DMU"), excludeId());
			dmuSelectConditions.add(project(dmuFields));
			dmuSelectConditions.add(unwind("$DMU"));

			AggregateIterable<Document> dmuDocument = dmuCollection.aggregate(dmuSelectConditions);

			for (Document document : dmuDocument) {
				println(document.toJson());
			}

			// endregion

			// MongoCollection<Document> collection =
			// mongoDatabase.getCollection("Products");
			// Document document = collection.find(eq("", "")).first();
			//
			// String json = document.toJson();
			// println(json);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
}