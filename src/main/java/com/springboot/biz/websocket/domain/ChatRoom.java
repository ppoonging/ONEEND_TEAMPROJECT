package com.springboot.biz.websocket.domain;

import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Setter
@Getter
public class ChatRoom {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true, nullable = false) // UUID 값 (방 아이디)
    private String roomId; //방 번호
    @Column(nullable = false)
    private String name; // 방 이름

    // 방 생성 메서드 (랜덤 UUID로 방 ID 생성)
    public static ChatRoom create(String name) {
        ChatRoom room = new ChatRoom();
        room.roomId = UUID.randomUUID().toString();
        room.name = name;
        return room;
    }
}
