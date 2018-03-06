package devutility.external.dao.mongodb;

import java.util.List;

import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import devutility.internal.lang.StringHelper;

public class MongoQueryBuilder {
	private boolean empty = true;
	private Query query = new Query();

	public boolean isEmpty() {
		return empty;
	}

	private void setEmpty(boolean empty) {
		this.empty = empty;
	}

	public Query getQuery() {
		return query;
	}

	public void in(String field, List<String> values) {
		if (StringHelper.isNullOrEmpty(field) || values == null || values.size() == 0) {
			return;
		}

		setEmpty(false);
		query.addCriteria(Criteria.where(field).in(values));
	}

	public void is(String field, Object value) {
		if (StringHelper.isNullOrEmpty(field) || value == null) {
			return;
		}

		setEmpty(false);
		query.addCriteria(Criteria.where(field).is(value));
	}

	public void paging(int pageIndex, int pageSize) {
		if (pageIndex < 1 || pageSize < 1) {
			return;
		}

		int skip = (pageIndex - 1) * pageSize;
		query.skip(skip);
		query.limit(pageSize);
	}

	public void top(int topCount) {
		if (topCount < 1) {
			return;
		}

		query.limit(topCount);
	}

	public void clear() {
		query = new Query();
	}
}