package org.morib.server.global.message;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class SseMessageBuilder {

    public String buildConnectionMessage(Long userId) {
        return "[userId : " + userId + "] 가 연결되었습니다.";
    }

    public String buildDisconnectionMessage(Long userId) {
        return "[userId : " + userId + "] 과의 연결이 종료되었습니다.";
    }

    public String buildTimeoutMessage(Long userId) {
        return "[userId : " + userId + "] 님의 연결 유지 시간이 만료되었습니다.";
    }

    public String buildFriendRequestMessage(String userName) {
        return userName + " 님이 친구 요청을 보냈습니다.";
    }

    public String buildFriendRequestAcceptMessage(String userName) {
        return userName + " 님이 친구 요청을 수락했습니다.";
    }

    public String buildHeartbeatMessage() { return "heart-beat"; }
}
