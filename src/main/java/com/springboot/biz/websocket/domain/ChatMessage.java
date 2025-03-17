package com.springboot.biz.websocket.domain;

import com.springboot.biz.user.HUser;
import jakarta.persistence.*;
import lombok.*;
import jakarta.persistence.Id;


import java.time.LocalDateTime;
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
@Entity
public class ChatMessage {
    public  enum MessageType {
        ENTER, CHAT, LEAVE, SYSTEM, ALERT
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

@Enumerated(EnumType.STRING)//ENUM 타입으로 저장
    private MessageType type; // 메시지 타입 (ENTER, CHAT, LEAVE)

    private String roomId; //채팅방 번호

    private String message; //실제 메시지

    private String sender;  // 보낸 유저 (HUser)

    private String image; // base64로 저장 (선택)



    private LocalDateTime sendDate = LocalDateTime.now(); // 보낸 시간

    /*@ManyToOne(fetch = FetchType.LAZY) // 유저 연결 (다대일 관계)
    @JoinColumn(name = "user_seq") // FK 이름 (HUser의 PK)*/

}
