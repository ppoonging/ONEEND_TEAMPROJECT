package com.springboot.biz.websocket.repository;


import com.springboot.biz.websocket.domain.ChatMessage;
import com.springboot.biz.websocket.domain.ChatRoom;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ChatRoomRepository extends JpaRepository<ChatRoom, String> {
    // 방 번호로 찾기
    ChatRoom findByRoomId(String roomId);
}