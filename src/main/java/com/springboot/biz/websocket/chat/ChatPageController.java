package com.springboot.biz.websocket.chat;

import com.springboot.biz.user.HUser;
import com.springboot.biz.user.HUserRepository;
import com.springboot.biz.user.HUserSerevice;
import com.springboot.biz.websocket.repository.ChatRoomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;

@Controller
@RequestMapping("/chat")
@RequiredArgsConstructor
public class ChatPageController {

    private final ChatRoomRepository chatRoomRepository;
    private final HUserRepository hUserRepository;

    // 채팅방 리스트 페이지
    @GetMapping("/rooms")
    public String rooms(Model model) {
        model.addAttribute("rooms", chatRoomRepository.findAll());
        return "websocket/chat_rooms"; // thymeleaf 페이지로 리턴
    }

    // 채팅방 상세 페이지
    @PreAuthorize("isAuthenticated()")//수정
    @GetMapping("/room/{roomId}")
    public String room(@PathVariable String roomId, Model model, Principal principal) {
        model.addAttribute("room", chatRoomRepository.findByRoomId(roomId));//roomid 로 찾기
        // 로그인 유저 정보 가져오기
        String username = principal.getName(); // 현재 로그인된 유저명
        HUser user = hUserRepository.findByUsername(username).orElseThrow(() -> new RuntimeException("유저 정보 없음"));
        model.addAttribute("user", user); // 유저 정보 넘기기

        return "websocket/chat_detailroom"; // 내가 사용하는 상세 페이지로
    }

}
