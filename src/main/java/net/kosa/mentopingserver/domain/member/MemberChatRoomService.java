package net.kosa.mentopingserver.domain.member;

import net.kosa.mentopingserver.domain.member.dto.ChatRoomCreationRequest;
import net.kosa.mentopingserver.domain.member.dto.MemberChatRoomDTO;

import java.util.List;

public interface MemberChatRoomService {
    MemberChatRoomDTO createChatRoom(ChatRoomCreationRequest request);

    List<MemberChatRoomDTO> getMemberChatRooms(Long memberId);

    void leaveChatRoom(String chatRoomId, Long memberId);

    MemberChatRoomDTO getChatRoomDetails(String chatRoomId);

    List<MemberChatRoomDTO> getMyChats(Long memberId);

}
