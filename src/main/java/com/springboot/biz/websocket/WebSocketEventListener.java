package com.springboot.biz.websocket;

import com.springboot.biz.websocket.domain.ChatMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Component
@RequiredArgsConstructor
public class WebSocketEventListener {

    private final SimpMessageSendingOperations messagingTemplate;
    private final Map<String, Integer> roomUserCount = new ConcurrentHashMap<>();
    private final Map<String, String> sessionRoomMap = new ConcurrentHashMap<>();

    @EventListener
    public void handleWebSocketConnectListener(SessionConnectEvent event) {
        log.info("✅ 웹소켓 연결됨");
    }

    @EventListener
    public void handleWebSocketDisconnectListener(SessionDisconnectEvent event) {
        StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());
        String sessionId = headerAccessor.getSessionId();
        String roomId = sessionRoomMap.get(sessionId);

        if (roomId != null) {
            roomUserCount.put(roomId, roomUserCount.getOrDefault(roomId, 1) - 1);
            sessionRoomMap.remove(sessionId);

            // 퇴장 메시지 전송
            ChatMessage leaveMessage = new ChatMessage(
                    ChatMessage.MessageType.LEAVE,
                    roomId,
                    "👋 누군가 퇴장하셨습니다.",
                    "알 수 없음"
            );

            messagingTemplate.convertAndSend("/topic/chat/room/" + roomId, leaveMessage);
            sendUserCount(roomId);
        }
    }

    public void addUserToRoom(String sessionId, String roomId, String username) {
        sessionRoomMap.put(sessionId, roomId);
        roomUserCount.put(roomId, roomUserCount.getOrDefault(roomId, 0) + 1);


        // 입장 메시지 전송
        ChatMessage joinMessage = new ChatMessage(
                ChatMessage.MessageType.ENTER,
                roomId,
                "🚀 " + username + "님이 입장하셨습니다.",
                username
        );

        messagingTemplate.convertAndSend("/topic/chat/room/" + roomId, joinMessage);
        sendUserCount(roomId);
    }

    public void removeUserFromRoom(String roomId) {
        roomUserCount.put(roomId, Math.max(0, roomUserCount.getOrDefault(roomId, 1) - 1));
        sendUserCount(roomId);
    }//ㅇㅇ

    public int getUserCount(String roomId) {
        return roomUserCount.getOrDefault(roomId, 0);
    }

    public void sendUserCount(String roomId) {
        messagingTemplate.convertAndSend("/topic/chat/room/" + roomId, Map.of(
                "type", "USER_COUNT",
                "count", roomUserCount.getOrDefault(roomId, 0)
        ));
    }
}
