package devutility.external.data.json;

import java.io.IOException;
import java.text.DateFormat;
import java.util.Date;

import com.fasterxml.jackson.core.JsonGenerator;

public class JsonSerializerHelper {
	public static <T> void serialize(T value, JsonGenerator jsonGenerator, DateFormat dateFormat) {
		if (value == null) {
			return;
		}

		String text = dateFormat.format((Date) value);

		try {
			jsonGenerator.writeString(text);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}