package devutility.external.data.json;

import java.util.Date;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import devutility.internal.text.format.DateFormatHelper;

public class DateJsonDeserializer extends JsonDeserializer<Date> {
	@Override
	public Date deserialize(JsonParser p, DeserializationContext ctxt) {
		Object value = JsonDeserializerHelper.deserializeDate(p, DateFormatHelper.getSimpleDateFormatWithStandardDateFormat());

		if (value == null) {
			return null;
		}

		return (Date) value;
	}
}