package devutility.external.data.json;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import devutility.internal.lang.StringHelper;

public class JsonHelper {
	public static String serialize(Object value) throws JsonProcessingException {
		ObjectMapper objectMapper = new ObjectMapper();

		try {
			return objectMapper.writeValueAsString(value);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
			throw e;
		}
	}

	public static <T> T deserialize(String value, Class<T> clazz) throws IOException {
		if (StringHelper.isNullOrEmpty(value)) {
			return null;
		}

		ObjectMapper objectMapper = new ObjectMapper();

		try {
			return objectMapper.readValue(value, clazz);
		} catch (IOException e) {
			e.printStackTrace();
			throw e;
		}
	}
}