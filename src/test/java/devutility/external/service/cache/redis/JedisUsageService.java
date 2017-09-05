package devutility.external.service.cache.redis;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Paths;

import devutility.internal.io.DirectoryHelper;
import devutility.internal.system.Application;
import devutility.internal.test.BaseService;
import redis.clients.jedis.Jedis;

public class JedisUsageService extends BaseService {
	private String host = null;
	private int port = 0;

	@Override
	public void run() {
		String configFile = Paths.get(DirectoryHelper.getResourcesDirectory(), "db.properties").toString();

		try {
			host = Application.getProperty(configFile, "redis.host");
			port = Application.getIntProperty(configFile, "redis.port");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		for (int i = 0; i < 50; i++) {
			println(read("Commercial-Products-GAG001ORCL"));
		}

		println(read("Commercial-Products-GAG001ORCL"));
	}

	String read(String key) {
		try (Jedis jedis = new Jedis(host, port, 1000000)) {
			jedis.select(1);
			return jedis.get(key);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
}