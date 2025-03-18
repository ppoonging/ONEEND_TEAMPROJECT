package com.springboot.biz.websocket;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Component
@RequiredArgsConstructor
public class WebSocketEventListener {

    private final SimpMessageSendingOperations messagingTemplate;
    private final Map<String, Integer> roomUserCount = new ConcurrentHashMap<>();
    private final Map<String, String> sessionRoomMap = new ConcurrentHashMap<>(); // 세션-방 매핑

    // 소켓 연결 시
    @EventListener
    public void handleWebSocketConnectListener(SessionConnectEvent event) {
        log.info("웹소켓 연결됨");
    }

    // 소켓 연결 해제 시
    @EventListener
    public void handleWebSocketDisconnectListener(SessionDisconnectEvent event) {
        StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());
        String sessionId = headerAccessor.getSessionId();
        String roomId = sessionRoomMap.get(sessionId);

        if (roomId != null) {
            roomUserCount.put(roomId, roomUserCount.getOrDefault(roomId, 1) - 1);
            log.info("방 {} 접속자 감소: {}", roomId, roomUserCount.get(roomId));

            // 접속자 정보 갱신 전송
            messagingTemplate.convertAndSend("/topic/chat/room/" + roomId, Map.of(
                    "type", "USER_COUNT",
                    "count", roomUserCount.get(roomId)
            ));

            sessionRoomMap.remove(sessionId);
        }
    }

    // 입장 처리
    public void addUserToRoom(String sessionId, String roomId) {
        sessionRoomMap.put(sessionId, roomId);
        roomUserCount.put(roomId, roomUserCount.getOrDefault(roomId, 0) + 1);
        log.info("방 {} 접속자 증가: {}", roomId, roomUserCount.get(roomId));

        // 접속자 정보 전송
        messagingTemplate.convertAndSend("/topic/chat/room/" + roomId, Map.of(
                "type", "USER_COUNT",
                "count", roomUserCount.get(roomId)
        ));
    }

    public int getUserCount(String roomId) {
        return roomUserCount.getOrDefault(roomId, 0);
    }
}
