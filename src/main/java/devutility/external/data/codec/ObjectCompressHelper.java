package devutility.external.data.codec;

import java.io.IOException;

import devutility.external.data.json.JsonHelper;
import devutility.internal.data.codec.Base64Helper;
import devutility.internal.data.codec.GZipHelper;
import devutility.internal.data.codec.UTF8Helper;

public class ObjectCompressHelper {
	public static String compress(Object value) throws IOException {
		String jsonValue = JsonHelper.serialize(value);
		byte[] utf8Bytes = UTF8Helper.encode(jsonValue);
		byte[] compressedBytes = GZipHelper.compress(utf8Bytes);
		return Base64Helper.encodeToString(compressedBytes);
	}

	public static <T> T decompress(String value, Class<T> clazz) throws IOException {
		byte[] compressedBytes = Base64Helper.decodeByString(value);
		byte[] utf8Bytes = GZipHelper.deCompress(compressedBytes);
		String jsonValue = UTF8Helper.decode(utf8Bytes);
		return JsonHelper.deserialize(jsonValue, clazz);
	}
}