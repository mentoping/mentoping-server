package net.kosa.mentopingserver.domain.member;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import net.kosa.mentopingserver.domain.member.dto.ChatRoomCreationRequest;
import net.kosa.mentopingserver.domain.member.dto.MemberChatRoomDTO;
import net.kosa.mentopingserver.domain.member.entity.Member;
import net.kosa.mentopingserver.domain.member.entity.MemberChatRoom;
import net.kosa.mentopingserver.global.exception.ChatRoomNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Log4j2
public class MemberChatRoomServiceImpl implements MemberChatRoomService {

    private final MemberChatRoomRepository memberChatRoomRepository;
    private final MemberRepository memberRepository;

    @Override
    @Transactional
    public MemberChatRoomDTO createChatRoom(ChatRoomCreationRequest request) {
        Member sender = memberRepository.findById(request.getSenderId())
                .orElseThrow(() -> new RuntimeException("Sender not found"));
        Member receiver = memberRepository.findById(request.getReceiverId())
                .orElseThrow(() -> new RuntimeException("Receiver not found"));

        String chatRoomName = request.getChatRoomName() != null ? request.getChatRoomName()
                : sender.getName() + " - " + receiver.getName();

        MemberChatRoom senderChatRoom = MemberChatRoom.builder()
                .member(sender)
                .chatRoomId(request.getFirebaseChatRoomId())
                .chatRoomName(chatRoomName)
                .otherParticipantId(receiver.getId())
                .build();

        MemberChatRoom receiverChatRoom = MemberChatRoom.builder()
                .member(receiver)
                .chatRoomId(request.getFirebaseChatRoomId())
                .chatRoomName(chatRoomName)
                .otherParticipantId(sender.getId())
                .build();

        memberChatRoomRepository.save(senderChatRoom);
        memberChatRoomRepository.save(receiverChatRoom);

        return convertToDTO(senderChatRoom);
    }

    @Override
    @Transactional(readOnly = true)
    public List<MemberChatRoomDTO> getMemberChatRooms(Long memberId) {
        List<MemberChatRoom> chatRooms = memberChatRoomRepository.findByMemberId(memberId);
        return chatRooms.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void leaveChatRoom(String chatRoomId, Long memberId) {
        MemberChatRoom chatRoom = memberChatRoomRepository.findByChatRoomIdAndMemberId(chatRoomId, memberId)
                .orElseThrow(() -> {
                    log.error("Chat room not found with ID: {} for member: {}", chatRoomId, memberId);
                    return new ChatRoomNotFoundException("Chat room not found");
                });
        memberChatRoomRepository.delete(chatRoom);
    }

    @Override
    @Transactional(readOnly = true)
    public MemberChatRoomDTO getChatRoomDetails(String chatRoomId) {
        MemberChatRoom chatRoom = memberChatRoomRepository.findByChatRoomId(chatRoomId)
                .orElseThrow(() -> {
                    log.error("Chat room not found with ID: {}", chatRoomId);
                    return new ChatRoomNotFoundException("Chat room not found");
                });
        return convertToDTO(chatRoom);
    }

    @Override
    @Transactional(readOnly = true)
    public List<MemberChatRoomDTO> getMyChats(Long memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new RuntimeException("Member not found"));
        List<MemberChatRoom> myChatRooms = memberChatRoomRepository.findByMember(member);
        return myChatRooms.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    private MemberChatRoomDTO convertToDTO(MemberChatRoom chatRoom) {
        return MemberChatRoomDTO.builder()
                .id(chatRoom.getId())
                .memberId(chatRoom.getMember().getId())
                .chatRoomId(chatRoom.getChatRoomId())
                .chatRoomName(chatRoom.getChatRoomName())
                .otherParticipantId(chatRoom.getOtherParticipantId())
                .createdAt(chatRoom.getCreatedAt().toString())
                .updatedAt(chatRoom.getUpdatedAt().toString())
                .build();
    }
}