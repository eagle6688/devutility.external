package devutility.external.dao.mongodb;

import org.springframework.data.mongodb.core.query.Query;

public abstract class MongoSearchParam {
	private Boolean deleted;

	/**
	 * Page index
	 */
	private int pageIndex;

	/**
	 * Page size
	 */
	private int pageSize;

	/**
	 * Count for limit
	 */
	private int topCount;

	/**
	 * query builder
	 */
	protected MongoQueryBuilder mongoQueryBuilder = new MongoQueryBuilder();

	public MongoSearchParam() {
		deleted = false;
	}

	public boolean isDeleted() {
		return deleted;
	}

	public void setDeleted(boolean deleted) {
		this.deleted = deleted;
	}

	public int getPageIndex() {
		return pageIndex;
	}

	public void setPageIndex(int pageIndex) {
		this.pageIndex = pageIndex;
	}

	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	public int getTopCount() {
		return topCount;
	}

	public void setTopCount(int topCount) {
		this.topCount = topCount;
	}

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

		if (deleted != null) {
			mongoQueryBuilder.is("Deleted", deleted);
		}

		if (pageIndex > 0 && pageSize > 0) {
			mongoQueryBuilder.paging(pageIndex, pageSize);
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