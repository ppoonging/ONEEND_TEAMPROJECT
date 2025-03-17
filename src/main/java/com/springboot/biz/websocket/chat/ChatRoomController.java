package com.springboot.biz.websocket.chat;

import com.springboot.biz.user.HUser;
import com.springboot.biz.user.HUserRepository;
import com.springboot.biz.websocket.domain.ChatRoom;
import com.springboot.biz.websocket.repository.ChatMessageRepository;
import com.springboot.biz.websocket.repository.ChatRoomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RequiredArgsConstructor
@Controller
@RequestMapping("/chat")
public class ChatRoomController {

    private final ChatRoomRepository chatRoomRepository;
    private final HUserRepository hUserRepository;
    private final ChatMessageRepository chatMessageRepository;

    // 방 리스트
    @GetMapping("/rooms")
    public String rooms(Model model, Principal principal) {
        model.addAttribute("rooms", chatRoomRepository.findAll());

        if (principal != null) { // 로그인 한 경우만
            String username = principal.getName();
            HUser user = hUserRepository.findByUsername(username)
                    .orElseThrow(() -> new RuntimeException("유저 정보 없음"));
            model.addAttribute("myNickname", user.getNickname()); // 닉네임 넘기기
        }

        return "websocket/chat_rooms"; // 방 리스트 html
    }

    // 방 생성
    @PostMapping("/rooms")
    public String createRoom(@RequestParam String name, Principal principal) {
        String username = principal.getName();
        HUser user = hUserRepository.findByUsername(username).orElseThrow(() -> new RuntimeException("유저 정보 없음"));
        ChatRoom chatRoom = ChatRoom.create(name, user.getNickname());
        chatRoomRepository.save(chatRoom);
        return "redirect:/chatroom/room/" + chatRoom.getRoomId();
    }

    // 방 상세
    @GetMapping("/room/{roomId}")
    public String roomDetail(@PathVariable String roomId, Model model, Principal principal) {
        ChatRoom room = chatRoomRepository.findByRoomId(roomId);
        model.addAttribute("room", room);

        String username = principal.getName();
        HUser user = hUserRepository.findByUsername(username).orElseThrow(() -> new RuntimeException("유저 정보 없음"));
        model.addAttribute("user", user);

        return "websocket/chat_detailroom";
    }

    // 방 삭제
    @PostMapping("/room/{roomId}/delete")
    public String deleteRoom(@PathVariable String roomId, Principal principal) {
        ChatRoom room = chatRoomRepository.findByRoomId(roomId);
        if (room != null) {
            String username = principal.getName();
            HUser user = hUserRepository.findByUsername(username).orElseThrow(() -> new RuntimeException("유저 정보 없음"));
            if (!room.getOwner().equals(user.getNickname())) {
                throw new RuntimeException("방 삭제 권한 없음");
            }
            chatMessageRepository.deleteByRoomId(roomId);
            chatRoomRepository.delete(room);
        }
        return "redirect:/chatroom/rooms";
    }
}
