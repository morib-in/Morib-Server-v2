package org.morib.server.global.common;

public final class Constants {

    private Constants() {
    }

    public static final String AUTHORIZATION = "Authorization";
    public static final String BEARER = "Bearer ";
    public static final String ACCESS_TOKEN_SUBJECT = "access_token";
    public static final String REFRESH_TOKEN_SUBJECT = "refresh_token";
    public static final String ID_CLAIM = "id";
    public static final String SEND = "send";
    public static final String RECEIVE = "receive";
    public static final String ADD_FRIEND_REQUEST_MSG = " 님이 친구 요청을 보냈습니다.";
    public static final Long SSE_TIMEOUT = 180_000L;
    public static final String IS_SIGN_UP_QUERYSTRING = "?isSignUp=";
    public static final String GOOGLE_REGISTRATION_ID = "google";
    public static final String INVALID_REFRESH_TOKEN = "invalid";
    public static final String GOOGLE_REVOKE_URL = "https://oauth2.googleapis.com/revoke";
    public static final String SSE_EVENT_CONNECT = "connect";
    public static final String SSE_EVENT_COMPLETION = "completion";
    public static final String SSE_EVENT_REFRESH = "refresh";
    public static final String SSE_EVENT_USER_INFO_UPDATE = "update";

}