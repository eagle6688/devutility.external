package devutility.external.dao.mongodb;

import org.springframework.data.mongodb.core.query.Query;

public abstract class MongoSearchParam {
	protected MongoQueryBuilder mongoQueryBuilder = new MongoQueryBuilder();

	public boolean isEmpty() {
		if (mongoQueryBuilder.isEmpty()) {
			buildQuery();
		}

		return mongoQueryBuilder.isEmpty();
	}

	public Query getQuery() {
		if (mongoQueryBuilder.isEmpty()) {
			buildQuery();
		}

		return mongoQueryBuilder.getQuery();
	}

	protected void clear() {
		mongoQueryBuilder.clear();
	}

	/**
	 * buildQuery void
	 */
	protected abstract void buildQuery();
}