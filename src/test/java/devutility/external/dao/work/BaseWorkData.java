package devutility.external.dao.work;

import devutility.external.dao.BaseData;

public class BaseWorkData extends BaseData {
	protected void setMongoDBConnectionString() {
		host = "10.122.22.94";
		port = 27017;
		database = "SupportDB26";
	}
}