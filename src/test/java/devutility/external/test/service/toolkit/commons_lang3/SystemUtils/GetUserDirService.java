package devutility.external.test.service.toolkit.commons_lang3.SystemUtils;

import java.io.File;

import org.apache.commons.lang3.SystemUtils;

import devutility.internal.test.BaseService;
import devutility.internal.test.ServiceExecutor;

public class GetUserDirService extends BaseService {
	@Override
	public void run() {
		File file = SystemUtils.getUserDir();
		System.out.println(file.getPath());
	}
	
	public static void main(String[] args) {
		ServiceExecutor.run(new GetUserDirService());
	}
}