package devutility.external.service.toolkit.commons_lang3.SystemUtils;

import java.io.File;

import org.apache.commons.lang3.SystemUtils;

import devutility.internal.test.BaseService;

public class GetUserDirService extends BaseService {
	@Override
	public void run() {
		File file = SystemUtils.getUserDir();
		System.out.println(file.getPath());
	}
}