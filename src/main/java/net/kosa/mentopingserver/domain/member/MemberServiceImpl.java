package net.kosa.mentopingserver.domain.member;

import lombok.RequiredArgsConstructor;
import net.kosa.mentopingserver.domain.member.dto.MemberDto;
import net.kosa.mentopingserver.domain.member.entity.Member;
import net.kosa.mentopingserver.global.common.enums.Role;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {

    private final MemberRepository memberRepository;

    @Override
    public List<MemberDto> getAllMembers() {
        return memberRepository.findAll().stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<MemberDto> getMemberByEmail(String email) {
        return memberRepository.findByEmail(email)
                .map(this::toDto);
    }

    @Override
    public MemberDto createMember(MemberDto memberDto) {
        if (memberRepository.findByEmail(memberDto.getEmail()).isPresent()) {
            throw new IllegalArgumentException("이미 사용 중인 이메일입니다.");
        }
        Member member = toEntity(memberDto);
        return toDto(memberRepository.save(member));
    }

    @Override
    public MemberDto updateMember(Long memberId, MemberDto memberDto) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("해당 ID의 회원을 찾을 수 없습니다: " + memberId));

        member = member.toBuilder()
                .name(memberDto.getName())
                .password(memberDto.getPassword())  // 보안상 암호화 필요
                .nickname(memberDto.getNickname())
                .email(memberDto.getEmail())
                .profile(memberDto.getProfile())
                .content(memberDto.getContent())
                .build();

        return toDto(memberRepository.save(member));
    }

    @Override
    public void deleteMember(Long memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("해당 ID의 회원을 찾을 수 없습니다: " + memberId));
        memberRepository.delete(member);
    }

    @Override
    public Optional<MemberDto> getMemberById(Long memberId) {
        return memberRepository.findById(memberId)
                .map(this::toDto);
    }

    @Override
    @Transactional
    public Optional<MemberDto> getMemberByOauthId(String oauthId){
        return  memberRepository.findByOauthId(oauthId)
                .map(this::toDto);
    }

    private MemberDto toDto(Member member) {
        return MemberDto.builder()
                .id(member.getId())
                .oauthId(member.getOauthId())
                .role(member.getRole().name())
                .name(member.getName())
                .password(member.getPassword())  // DTO로 전달하지만 실제로는 암호화된 형태여야 함
                .email(member.getEmail())
                .nickname(member.getNickname())
                .profile(member.getProfile())
                .content(member.getContent())
                .build();
    }

    private Member toEntity(MemberDto memberDto) {
        return Member.builder()
                .id(memberDto.getId())
                .oauthId(memberDto.getOauthId())
                .role(Role.valueOf(memberDto.getRole()))
                .name(memberDto.getName())
                .password(memberDto.getPassword())  // 암호화 로직 필요
                .email(memberDto.getEmail())
                .nickname(memberDto.getNickname())
                .profile(memberDto.getProfile())
                .content(memberDto.getContent())
                .build();
    }
}