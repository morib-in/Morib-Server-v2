package org.morib.server.global.oauth2.converter;

public class AppleTokenHolder {

	private static final ThreadLocal<String> refreshTokenHolder = new ThreadLocal<>();

	public static void setRefreshToken(String refreshToken) {
		System.out.println("Setting refresh token: " + refreshToken);
		refreshTokenHolder.set(refreshToken);
	}

	public static String getRefreshToken() {
		System.out.println("Get refresh token: " + refreshTokenHolder.get());
		return refreshTokenHolder.get();
	}

	public static void clear() {
		refreshTokenHolder.remove();
	}
}
