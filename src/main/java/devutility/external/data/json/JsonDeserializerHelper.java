package devutility.external.data.json;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;

import com.fasterxml.jackson.core.JsonParser;

import devutility.internal.lang.StringHelper;

public class JsonDeserializerHelper {
	public static Object deserializeDate(JsonParser jsonParser, DateFormat dateFormat) {
		String text = null;

		try {
			text = jsonParser.getText();
		} catch (IOException e) {
			e.printStackTrace();
		}

		if (StringHelper.isNullOrEmpty(text)) {
			return null;
		}

		try {
			return dateFormat.parse(text);
		} catch (ParseException e) {
			e.printStackTrace();
		}

		return null;
	}
}