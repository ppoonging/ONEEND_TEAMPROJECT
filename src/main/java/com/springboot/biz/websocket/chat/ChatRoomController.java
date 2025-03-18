package com.springboot.biz.websocket.chat;

import com.springboot.biz.user.HUser;
import com.springboot.biz.user.HUserRepository;
import com.springboot.biz.websocket.WebSocketEventListener;
import com.springboot.biz.websocket.domain.ChatRoom;
import com.springboot.biz.websocket.repository.ChatRoomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/chatroom")
public class ChatRoomController {

    private final ChatRoomRepository chatRoomRepository;
    private final HUserRepository hUserRepository;
    private final WebSocketEventListener webSocketEventListener;

    // 방 리스트 + 방 생성
    // 방 리스트 (접속자 수 포함)
    @GetMapping("/rooms")
    public String rooms(Model model) {
        List<ChatRoom> chatRooms = chatRoomRepository.findAll();

        // 각 방의 접속자 수 가져오기
        chatRooms.forEach(room -> {
            int userCount = webSocketEventListener.getUserCount(room.getRoomId()); // 현재 접속자 수 조회
            room.setUserCount(userCount); // ChatRoom 엔티티에 userCount 필드 추가 필요
        });

        model.addAttribute("rooms", chatRooms);
        return "websocket/chat_rooms"; // 템플릿 리턴
    }

    // 방 생성 (form 방식)
    @PostMapping("/rooms")
    public String createRoom(@RequestParam String name, Principal principal) {
        String username = principal.getName(); // 현재 로그인한 사용자의 이름 가져오기
        HUser owner = hUserRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("유저 정보 없음")); // 방장 정보 가져오기

        // 올바르게 매개변수를 맞춰서 호출
        ChatRoom chatRoom = ChatRoom.create(name, owner.getUsername()); // 방장 포함
        chatRoomRepository.save(chatRoom);

        return "redirect:/chatroom/room/" + chatRoom.getRoomId(); // 생성된 방으로 이동
    }

    @PostMapping("/delete/{roomId}")
    public String deleteRoom(@PathVariable String roomId, Principal principal, RedirectAttributes redirectAttributes) {
        ChatRoom room = chatRoomRepository.findByRoomId(roomId);

        // 방이 존재하고, 현재 로그인한 사용자가 방장인지 확인
        if (room != null && room.getOwner().equals(principal.getName())) {
            chatRoomRepository.delete(room);
            redirectAttributes.addFlashAttribute("successMessage", "채팅방이 삭제되었습니다.");
        } else {
            redirectAttributes.addFlashAttribute("errorMessage", "방장만 삭제할 수 있습니다.");
        }
        return "redirect:/chatroom/rooms"; // 삭제 후 채팅방 리스트로 이동
    }
    @GetMapping("/room/{roomId}")
    public String roomDetail(@PathVariable String roomId, Model model, Principal principal) {
        ChatRoom room = chatRoomRepository.findByRoomId(roomId);

        // 방이 존재하지 않으면 목록으로 리디렉션
        if (room == null) {
            return "redirect:/chatroom/rooms";
        }

        model.addAttribute("room", room);

        // 로그인한 유저 정보 가져오기
        String username = principal.getName();
        HUser user = hUserRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("유저 정보 없음"));
        model.addAttribute("user", user);

        return "websocket/chat_detailroom"; // 채팅방 상세 페이지로 이동 //수정
    }


}