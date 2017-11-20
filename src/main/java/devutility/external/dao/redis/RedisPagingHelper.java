package devutility.external.dao.redis;

import java.util.Arrays;

public class RedisPagingHelper {
	public static int calculatePagesCount(int totalCount, int pageSize) {
		if (totalCount % pageSize == 0) {
			return totalCount / pageSize;
		}

		return totalCount / pageSize + 1;
	}

	public static String getPagesCountKey(String key) {
		return String.format("%s:count", key);
	}

	public static String getPagingArrayKey(String key, int pageIndex) {
		return String.format("%s:%d", key, pageIndex);
	}

	public static String[][] pagingArray(String[][] array, int pageIndex, int pageSize) {
		return (String[][]) Arrays.stream(array).skip(pageIndex * pageSize).limit(pageSize).toArray();
	}
}