package devutility.external.test.data.codec;

import java.io.IOException;
import java.util.Date;

import devutility.external.data.codec.ObjectCompressHelper;
import devutility.external.data.json.JsonHelper;
import devutility.external.test.model.User;
import devutility.internal.test.BaseService;
import devutility.internal.test.ServiceExecutor;

public class ObjectCompressHelperTest extends BaseService {
	@Override
	public void run() {
		User user = new User();
		user.setId(1);
		user.setAge(30);
		user.setName("Aldwin");
		user.setBirthday(new Date());

		try {
			String compressedValue = ObjectCompressHelper.compress(user);
			println(compressedValue);

			User originalUser = ObjectCompressHelper.decompress(compressedValue, User.class);
			println(JsonHelper.serialize(originalUser));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		ServiceExecutor.run(ObjectCompressHelperTest.class);
	}
}