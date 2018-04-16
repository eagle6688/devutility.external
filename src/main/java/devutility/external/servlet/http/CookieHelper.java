package devutility.external.servlet.http;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import devutility.internal.lang.StringHelper;
import devutility.internal.net.UrlCoderHelper;

public class CookieHelper {
	// region set cookie

	public static void set(HttpServletResponse httpServletResponse, Cookie cookie) {
		if (httpServletResponse == null || cookie == null) {
			return;
		}

		httpServletResponse.addCookie(cookie);
	}

	public static void set(HttpServletResponse httpServletResponse, String name, String value, String domain, String path, int expireSeconds, boolean secure) {
		Cookie cookie = create(name, value, domain, path, expireSeconds, secure);
		set(httpServletResponse, cookie);
	}

	public static void set(HttpServletResponse httpServletResponse, String name, String value, String domain, String path, int expireSeconds) {
		Cookie cookie = create(name, value, domain, path, expireSeconds);
		set(httpServletResponse, cookie);
	}

	public static void set(HttpServletResponse httpServletResponse, String name, String value, String domain, String path) {
		Cookie cookie = create(name, value, domain, path);
		set(httpServletResponse, cookie);
	}

	public static void set(HttpServletResponse httpServletResponse, String name, String value, String domain) {
		Cookie cookie = create(name, value, domain);
		set(httpServletResponse, cookie);
	}

	public static void set(HttpServletResponse httpServletResponse, String name, String value) {
		Cookie cookie = create(name, value);
		set(httpServletResponse, cookie);
	}

	public static void set(HttpServletResponse httpServletResponse, String name, String value, int expireSeconds) {
		set(httpServletResponse, name, value, null, null, expireSeconds);
	}

	// endregion

	// region create cookie

	public static Cookie create(String name, String value, String domain, String path, int expireSeconds, boolean secure) {
		if (StringHelper.isNullOrEmpty(name) || value == null) {
			return null;
		}

		Cookie cookie = new Cookie(name, UrlCoderHelper.encode(value));

		if (!StringHelper.isNullOrEmpty(domain)) {
			cookie.setDomain(domain);
		}

		if (StringHelper.isNullOrEmpty(path)) {
			path = "/";
		}

		cookie.setPath(path);
		cookie.setMaxAge(expireSeconds);
		cookie.setSecure(secure);
		cookie.setHttpOnly(true);
		return cookie;
	}

	public static Cookie create(String name, String value, String domain, String path, int expireSeconds) {
		return create(name, value, domain, path, expireSeconds, false);
	}

	public static Cookie create(String name, String value, String domain, String path) {
		return create(name, value, domain, path, -1);
	}

	public static Cookie create(String name, String value, String domain) {
		return create(name, value, domain, "/");
	}

	public static Cookie create(String name, String value) {
		return create(name, value, null);
	}

	// endregion

	// region get cookie

	public static Cookie get(HttpServletRequest httpServletRequest, String name) {
		if (httpServletRequest == null) {
			return null;
		}

		return get(httpServletRequest.getCookies(), name);
	}

	public static Cookie get(Cookie[] cookies, String name) {
		if (cookies == null || cookies.length == 0 || StringHelper.isNullOrEmpty(name)) {
			return null;
		}

		for (Cookie cookie : cookies) {
			if (name.equals(cookie.getName())) {
				return cookie;
			}
		}

		return null;
	}

	// endregion

	// region get value

	public static String getValue(HttpServletRequest httpServletRequest, String name) {
		Cookie cookie = get(httpServletRequest, name);
		return getValue(cookie);
	}

	public static String getValue(Cookie[] cookies, String name) {
		Cookie cookie = get(cookies, name);
		return getValue(cookie);
	}

	public static String getValue(Cookie cookie) {
		if (cookie == null) {
			return null;
		}

		return UrlCoderHelper.decode(cookie.getValue());
	}

	// endregion

	// region remove

	public static boolean remove(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, String name) {
		Cookie cookie = get(httpServletRequest, name);

		if (cookie == null) {
			return false;
		}

		cookie.setMaxAge(0);
		httpServletResponse.addCookie(cookie);
		return true;
	}

	// endregion
}