package net.kosa.mentopingserver.domain.member;

import net.kosa.mentopingserver.domain.member.entity.Member;
import net.kosa.mentopingserver.domain.member.entity.MemberChatRoom;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface MemberChatRoomRepository extends JpaRepository<MemberChatRoom, Long> {
    Optional<MemberChatRoom> findByChatRoomId(String chatRoomId);

    List<MemberChatRoom> findByMemberId(Long memberId);

    Optional<MemberChatRoom> findByChatRoomIdAndMemberId(String chatRoomId, Long memberId);

    List<MemberChatRoom> findByMember(Member member);
}