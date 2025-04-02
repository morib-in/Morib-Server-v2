package org.morib.server.global.common;

public final class Constants {

    private Constants() {
    }

    public static final String AUTHORIZATION = "Authorization";
    public static final String BEARER = "Bearer ";
    public static final String ACCESS_TOKEN_SUBJECT = "accessToken";
    public static final String REFRESH_TOKEN_SUBJECT = "refreshToken";
    public static final String ID_CLAIM = "id";
    public static final String SEND = "send";
    public static final String RECEIVE = "receive";
    public static final Long SSE_TIMEOUT = 300_000L;
    public static final Long MAX_CONNECTION_TIME = 30 * 60 * 1000L;
    public static final int MAX_FAILED_ATTEMPTS = 100;
    public static final String IS_ONBOARDING_COMPLETED = "?isOnboardingCompleted=";
    public static final String GOOGLE_REGISTRATION_ID = "google";
    public static final String INVALID_REFRESH_TOKEN = "invalid";
    public static final String GOOGLE_REVOKE_URL = "https://oauth2.googleapis.com/revoke";
    public static final String SSE_EVENT_CONNECT = "connect";
    public static final String SSE_EVENT_COMPLETION = "completion";
    public static final String SSE_EVENT_REFRESH = "refresh";
    public static final String SSE_EVENT_TIME_OUT = "timeout";
    public static final String SSE_EVENT_TIMER_START = "timerStart";
    public static final String SSE_EVENT_TIMER_STOP_ACTION = "timerStopAction";
    public static final String SSE_EVENT_FRIEND_REQUEST = "friendRequest";
    public static final String SSE_EVENT_FRIEND_REQUEST_ACCEPT = "friendRequestAccept";
    public static final String SSE_EVENT_HEARTBEAT = "heartbeat";
    public static final int MAX_VISIBLE_ALLOWED_SERVICES = 5;


}