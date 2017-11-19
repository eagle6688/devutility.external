package devutility.external.data.json;

import java.util.Date;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import devutility.internal.text.format.DateFormatHelper;

public class DateJsonSerializer extends JsonSerializer<Date> {
	@Override
	public void serialize(Date value, JsonGenerator gen, SerializerProvider serializers) {
		JsonSerializerHelper.<Date>serialize(value, gen, DateFormatHelper.StandardDate_SimpleDateFormat);
	}
}