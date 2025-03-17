package com.springboot.biz.websocket.chat;

import com.springboot.biz.user.HUser;
import com.springboot.biz.user.HUserRepository;
import com.springboot.biz.websocket.WebSocketEventListener;
import com.springboot.biz.websocket.domain.ChatMessage;
import com.springboot.biz.websocket.repository.ChatMessageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Controller;

import java.security.Principal;
import java.time.LocalDateTime;

@RequiredArgsConstructor
@Controller
public class ChatController {

    private final ChatMessageRepository chatMessageRepository;
    private final HUserRepository hUserRepository;
    private final WebSocketEventListener eventListener;
    private final SimpMessageSendingOperations messagingTemplate;

    // 채팅방 입장
    @MessageMapping("/chat/enter")
    public void enter(@Payload ChatMessage message, SimpMessageHeaderAccessor headerAccessor, Principal principal) {
        String username = principal.getName();
        HUser sender = hUserRepository.findByUsername(username).orElseThrow(() -> new RuntimeException("유저 정보 없음"));

        headerAccessor.getSessionAttributes().put("roomId", message.getRoomId());
        eventListener.addUserToRoom(headerAccessor.getSessionId(), message.getRoomId());

        message.setSender(sender.getNickname());
        message.setType(ChatMessage.MessageType.ENTER);
        message.setSendDate(LocalDateTime.now());

        messagingTemplate.convertAndSend("/topic/chat/room/" + message.getRoomId(), message);
    }

    // 채팅방 메세지
    @MessageMapping("/chat/message/{roomId}")
    public void message(@DestinationVariable String roomId, @Payload ChatMessage message, Principal principal) {
        String username = principal.getName();
        HUser sender = hUserRepository.findByUsername(username).orElseThrow(() -> new RuntimeException("유저 정보 없음"));

        message.setRoomId(roomId);
        message.setSender(sender.getNickname());
        message.setSendDate(LocalDateTime.now());
        message.setType(ChatMessage.MessageType.CHAT);

        chatMessageRepository.save(message);
        messagingTemplate.convertAndSend("/topic/chat/room/" + roomId, message);
    }

    // 채팅방 퇴장 (주소 변경!)
    @MessageMapping("/chat/leave")
    public void leave(@Payload ChatMessage message, Principal principal) {
        String username = principal.getName();
        HUser sender = hUserRepository.findByUsername(username).orElseThrow(() -> new RuntimeException("유저 정보 없음"));

        message.setSender(sender.getNickname());
        message.setType(ChatMessage.MessageType.LEAVE);
        message.setSendDate(LocalDateTime.now());

        messagingTemplate.convertAndSend("/topic/chat/room/" + message.getRoomId(), message);
    }

    // 시스템 공지
    @MessageMapping("/chat/system/notice")
    public void systemNotice(@Payload ChatMessage message) {
        message.setSender("SYSTEM");
        message.setType(ChatMessage.MessageType.SYSTEM);
        message.setSendDate(LocalDateTime.now());
        messagingTemplate.convertAndSend("/topic/chat/room/" + message.getRoomId(), message);
    }

    // 시스템 긴급 알림
    @MessageMapping("/chat/system/alert")
    public void systemAlert(@Payload ChatMessage message) {
        message.setSender("ALERT");
        message.setType(ChatMessage.MessageType.ALERT);
        message.setSendDate(LocalDateTime.now());
        messagingTemplate.convertAndSend("/topic/chat/room/" + message.getRoomId(), message);
    }
}
