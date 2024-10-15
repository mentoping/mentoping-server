package net.kosa.mentopingserver.domain.member;

import net.kosa.mentopingserver.domain.member.dto.MemberDto;
import net.kosa.mentopingserver.domain.member.entity.Member;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface MemberService {

    @Transactional(readOnly = true)
    List<MemberDto> getAllMembers();

    @Transactional(readOnly = true)
    Optional<MemberDto> getMemberByEmail(String email);

    @Transactional
    MemberDto createMember(MemberDto memberDto);

    @Transactional
    MemberDto updateMember(Long memberId, MemberDto memberDto);

    @Transactional
    void deleteMember(Long memberId);

    @Transactional(readOnly = true)
    Optional<MemberDto> getMemberById(Long memberId);

    @Transactional
    Optional<MemberDto> getMemberByOauthId(String oauthId);
}
