package devutility.external.dao.mongodb;

import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import devutility.internal.lang.StringHelper;

public class MongoQueryBuilder {
	private boolean empty = true;
	private Query query = new Query();

	public void in(String field, String[] values) {
		if (StringHelper.isNullOrEmpty(field) || values == null || values.length == 0) {
			return;
		}

		setEmpty(false);
		query.addCriteria(Criteria.where(field).is(values));
	}

	public boolean isEmpty() {
		return empty;
	}

	private void setEmpty(boolean empty) {
		this.empty = empty;
	}

	public Query getQuery() {
		return query;
	}

	public void clear() {
		query = new Query();
	}
}