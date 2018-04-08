package devutility.external.test.config;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import devutility.internal.util.PropertiesHelper;

public class MailConfig {
	public static final String configFile = "email.properties";

	public static Map<String, String> get() {
		Properties properties = PropertiesHelper.getProperties(configFile);

		Map<String, String> map = new HashMap<String, String>();
		map.put("host", PropertiesHelper.getProperty(properties, "host"));
		map.put("fromEmail", PropertiesHelper.getProperty(properties, "fromEmail"));
		map.put("userName", PropertiesHelper.getProperty(properties, "userName"));
		map.put("password", PropertiesHelper.getProperty(properties, "password"));
		map.put("toEmail", PropertiesHelper.getProperty(properties, "toEmail"));
		return map;
	}
}