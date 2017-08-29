package devutility.external.dao.work;

import java.util.List;

import devutility.external.entity.work.DMU;

public class DMUService extends BaseWorkData {
	public DMUService() {
		setMongoDBConnectionString();
	}

	public List<DMU> getList() {
		return null;
	}
}