package net.kosa.mentopingserver.domain.member;

import net.kosa.mentopingserver.domain.member.dto.MemberDto;
import net.kosa.mentopingserver.domain.member.entity.Member;
import net.kosa.mentopingserver.global.common.enums.Role;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@Transactional
public class MemberServiceTest {

    @Autowired
    private MemberService memberService;

    @Autowired
    private MemberRepository memberRepository;

    private Member testMember;

    @BeforeEach
    void setUp() {
        testMember = Member.builder()
                .email("test@example.com")
                .name("Test User")
                .nickname("testuser")
                .password("password")
                .role(Role.ROLE_MENTEE)
                .build();
        memberRepository.save(testMember);
    }

    @Test
    void getAllMembers_success() {
        List<MemberDto> members = memberService.getAllMembers();
        assertThat(members).isNotEmpty();
    }

    @Test
    void getMemberByEmail_success() {
        Optional<MemberDto> member = memberService.getMemberByEmail(testMember.getEmail());
        assertThat(member).isPresent();
        assertThat(member.get().getEmail()).isEqualTo(testMember.getEmail());
    }

    @Test
    void getMemberByEmail_notFound() {
        assertThrows(IllegalArgumentException.class, () -> {
            memberService.getMemberByEmail("notfound@example.com").orElseThrow(() -> new IllegalArgumentException("해당 이메일의 회원을 찾을 수 없습니다."));
        });
    }

    @Test
    void createMember_success() {
        MemberDto memberDto = MemberDto.builder()
                .email("newuser@example.com")
                .name("New User")
                .nickname("newuser")
                .password("newpassword")
                .role("ROLE_MENTEE")
                .build();

        MemberDto createdMember = memberService.createMember(memberDto);
        assertThat(createdMember).isNotNull();
        assertThat(createdMember.getEmail()).isEqualTo(memberDto.getEmail());
    }

    @Test
    void updateMember_success() {
        MemberDto memberDetails = MemberDto.builder()
                .name("Updated Name")
                .nickname("updatednickname")
                .email("updated@example.com")
                .password("updatedpassword")
                .role("ROLE_MENTOR")
                .build();

        MemberDto updatedMember = memberService.updateMember(testMember.getId(), memberDetails);
        assertThat(updatedMember.getName()).isEqualTo("Updated Name");
        assertThat(updatedMember.getEmail()).isEqualTo("updated@example.com");
    }

    @Test
    void deleteMember_success() {
        memberService.deleteMember(testMember.getId());
        Optional<Member> deletedMember = memberRepository.findById(testMember.getId());
        assertThat(deletedMember).isNotPresent();
    }
}