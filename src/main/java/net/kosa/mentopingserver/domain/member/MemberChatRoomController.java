package net.kosa.mentopingserver.domain.member;

import lombok.RequiredArgsConstructor;
import net.kosa.mentopingserver.domain.member.dto.ChatRoomCreationRequest;
import net.kosa.mentopingserver.domain.member.dto.MemberChatRoomDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/chat-rooms")
public class MemberChatRoomController {

    private final MemberChatRoomService memberChatRoomService;

    @PostMapping
    public ResponseEntity<MemberChatRoomDTO> createChatRoom(
            @RequestParam Long memberId,
            @RequestBody ChatRoomCreationRequest request) {
        request.setSenderId(memberId);
        MemberChatRoomDTO chatRoom = memberChatRoomService.createChatRoom(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(chatRoom);
    }

    @GetMapping
    public ResponseEntity<List<MemberChatRoomDTO>> getMemberChatRooms(
            @RequestParam Long memberId) {
        List<MemberChatRoomDTO> chatRooms = memberChatRoomService.getMemberChatRooms(memberId);
        return ResponseEntity.ok(chatRooms);
    }

    @DeleteMapping("/{chatRoomId}")
    public ResponseEntity<Void> leaveChatRoom(
            @PathVariable String chatRoomId,
            @RequestParam Long memberId) {
        memberChatRoomService.leaveChatRoom(chatRoomId, memberId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{chatRoomId}")
    public ResponseEntity<MemberChatRoomDTO> getChatRoomDetails(
            @PathVariable String chatRoomId,
            @RequestParam Long memberId) {
        MemberChatRoomDTO chatRoom = memberChatRoomService.getChatRoomDetails(chatRoomId);
        return ResponseEntity.ok(chatRoom);
    }

    @GetMapping("/my-chats")
    public ResponseEntity<List<MemberChatRoomDTO>> getMyChats(
            @RequestParam Long memberId) {
        List<MemberChatRoomDTO> myChatRooms = memberChatRoomService.getMyChats(memberId);
        return ResponseEntity.ok(myChatRooms);
    }
}