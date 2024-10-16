package net.kosa.mentopingserver.domain.member;

import lombok.RequiredArgsConstructor;
import net.kosa.mentopingserver.domain.login.CustomOAuth2User;
import net.kosa.mentopingserver.domain.member.dto.ChatRoomCreationRequest;
import net.kosa.mentopingserver.domain.member.dto.MemberChatRoomDTO;
import net.kosa.mentopingserver.domain.member.dto.MemberDto;
import net.kosa.mentopingserver.global.config.CurrentUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/chat-rooms")
public class MemberChatRoomController {

    private final MemberChatRoomService memberChatRoomService;
    private final MemberService memberService;

    @PostMapping
    public ResponseEntity<?> createChatRoom(
            @AuthenticationPrincipal CustomOAuth2User customOAuth2User,
            @RequestBody ChatRoomCreationRequest request) {
        if (customOAuth2User == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User not authenticated");
        }

        String oauthId = customOAuth2User.getOauthId();
        Optional<MemberDto> memberOptional = memberService.getMemberByOauthId(oauthId);

        if (memberOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Member not found");
        }

        Long memberId = memberOptional.get().getId();

        request.setSenderId(memberId);
        MemberChatRoomDTO chatRoom = memberChatRoomService.createChatRoom(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(chatRoom);
    }

    @GetMapping
    public ResponseEntity<?> getMemberChatRooms(
            @AuthenticationPrincipal CustomOAuth2User customOAuth2User) {
        if (customOAuth2User == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User not authenticated");
        }

        String oauthId = customOAuth2User.getOauthId();
        Optional<MemberDto> memberOptional = memberService.getMemberByOauthId(oauthId);

        if (memberOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Member not found");
        }

        Long memberId = memberOptional.get().getId();
        List<MemberChatRoomDTO> chatRooms = memberChatRoomService.getMemberChatRooms(memberId);
        return ResponseEntity.ok(chatRooms);
    }

    @DeleteMapping("/{chatRoomId}")
    public ResponseEntity<?> leaveChatRoom(
            @PathVariable String chatRoomId,
            @AuthenticationPrincipal CustomOAuth2User customOAuth2User) {
        if (customOAuth2User == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User not authenticated");
        }

        String oauthId = customOAuth2User.getOauthId();
        Optional<MemberDto> memberOptional = memberService.getMemberByOauthId(oauthId);

        if (memberOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Member not found");
        }

        Long memberId = memberOptional.get().getId();
        memberChatRoomService.leaveChatRoom(chatRoomId, memberId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{chatRoomId}")
    public ResponseEntity<?> getChatRoomDetails(
            @PathVariable String chatRoomId,
            @AuthenticationPrincipal CustomOAuth2User customOAuth2User) {
        if (customOAuth2User == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User not authenticated");
        }

        String oauthId = customOAuth2User.getOauthId();
        Optional<MemberDto> memberOptional = memberService.getMemberByOauthId(oauthId);

        if (memberOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Member not found");
        }

        Long memberId = memberOptional.get().getId();
        MemberChatRoomDTO chatRoom = memberChatRoomService.getChatRoomDetails(chatRoomId);
        return ResponseEntity.ok(chatRoom);
    }

    @GetMapping("/my-chats")
    public ResponseEntity<?> getMyChats(
            @AuthenticationPrincipal CustomOAuth2User customOAuth2User) {
        if (customOAuth2User == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User not authenticated");
        }

        String oauthId = customOAuth2User.getOauthId();
        Optional<MemberDto> memberOptional = memberService.getMemberByOauthId(oauthId);

        if (memberOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Member not found");
        }

        Long memberId = memberOptional.get().getId();
        List<MemberChatRoomDTO> myChatRooms = memberChatRoomService.getMyChats(memberId);
        return ResponseEntity.ok(myChatRooms);
    }
}