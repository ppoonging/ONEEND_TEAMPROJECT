package com.springboot.biz.websocket.chat;

import com.springboot.biz.user.HUser;
import com.springboot.biz.user.HUserRepository;
import com.springboot.biz.websocket.domain.ChatRoom;
import com.springboot.biz.websocket.repository.ChatRoomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@Controller
@RequiredArgsConstructor
@RequestMapping("/chatroom")
public class ChatRoomController {

    private final ChatRoomRepository chatRoomRepository;
    private final HUserRepository hUserRepository;

    // 방 리스트 + 방 생성
    @GetMapping("/rooms")
    public String rooms(Model model) {
        model.addAttribute("rooms", chatRoomRepository.findAll());
        return "websocket/chat_rooms"; // 템플릿 리턴
    }

    // 방 생성 (form 방식)
    @PostMapping("/rooms")
    public String createRoom(@RequestParam String name) {
        ChatRoom chatRoom = ChatRoom.create(name);
        chatRoomRepository.save(chatRoom);
        return "redirect:/chatroom/room/" + chatRoom.getRoomId(); // 방 생성 후 해당 방의 roomid를 이용해 디테일 페이지로 바로 쏴줌
    }

    @GetMapping("/room/{roomId}")
    public String roomDetail(@PathVariable String roomId, Model model, Principal principal) {
        ChatRoom room = chatRoomRepository.findByRoomId(roomId);
        model.addAttribute("room", room);

        // 로그인 유저 정보 가져오기
        String username = principal.getName();
        HUser user = hUserRepository.findByUsername(username).orElseThrow(() -> new RuntimeException("유저 정보 없음"));
        model.addAttribute("user", user);

        return "websocket/chat_detailroom"; // 상세 템플릿
    }
}