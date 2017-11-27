package devutility.external.test.data.json;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import devutility.external.test.model.User;
import devutility.internal.test.BaseService;
import devutility.internal.test.ServiceExecutor;

public class SerializeTest extends BaseService {
	@Override
	public void run() {
		ObjectMapper objectMapper = new ObjectMapper();

		try {
			println(objectMapper.writeValueAsString(new String[0]));
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}

		List<String> list = new ArrayList<>();
		list.add("1");
		String[] array = list.stream().filter(i -> i.equals("2")).collect(Collectors.toList()).toArray(new String[0]);

		try {
			println(objectMapper.writeValueAsString(array));
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}

		User user = new User();
		user.setCountries(new String[0]);
		
		try {
			println(objectMapper.writeValueAsString(user));
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		ServiceExecutor.run(SerializeTest.class);
	}
}