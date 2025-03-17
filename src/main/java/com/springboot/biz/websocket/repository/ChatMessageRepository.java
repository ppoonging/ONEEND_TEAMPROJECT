package com.springboot.biz.websocket.repository;

import com.springboot.biz.websocket.domain.ChatMessage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {
    // 방 안의 메시지 시간순으로 찾기
    List<ChatMessage> findByRoomIdOrderBySendDateAsc(String roomId);
}
