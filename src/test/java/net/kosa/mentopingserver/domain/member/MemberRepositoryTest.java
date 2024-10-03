package net.kosa.mentopingserver.domain.member;

import net.kosa.mentopingserver.global.common.enums.Role;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
public class MemberRepositoryTest {

    @Autowired
    private MemberRepository memberRepository;

    @AfterEach
    public void cleanup() {
        memberRepository.deleteAll();
    }

    @Test
    public void whenFindByEmail_thenReturnMember() {
        // given
        Member member = Member.builder()
                .role(Role.ROLE_MENTEE)
                .name("John Doe")
                .password("password123")
                .email("john@example.com")
                .nickname("johnd")
                .build();
        memberRepository.save(member);

        // when
        Optional<Member> found = memberRepository.findByEmail(member.getEmail());

        // then
        assertThat(found).isPresent();
        found.ifPresent(foundMember -> {
            assertThat(foundMember.getEmail()).isEqualTo(member.getEmail());
        });
    }

    @Test
    public void whenFindByNickname_thenReturnMember() {
        // given
        Member member = Member.builder()
                .role(Role.ROLE_MENTEE)
                .name("Jane Doe")
                .password("password456")
                .email("jane@example.com")
                .nickname("janed")
                .build();
        memberRepository.save(member);

        // when
        Optional<Member> found = memberRepository.findByNickname(member.getNickname());

        // then
        assertThat(found).isPresent();
        found.ifPresent(foundMember -> {
            assertThat(foundMember.getNickname()).isEqualTo(member.getNickname());
        });
    }

    @Test
    public void whenSaveMember_thenReturnMemberWithId() {
        // given
        Member member = Member.builder()
                .role(Role.ROLE_ADMIN)
                .name("Admin User")
                .password("adminPass")
                .email("admin@example.com")
                .nickname("adminUser")
                .build();

        // when
        Member savedMember = memberRepository.save(member);

        // then
        assertThat(savedMember.getId()).isNotNull();
    }

    @Test
    public void whenFindById_thenReturnMember() {
        // given
        Member member = Member.builder()
                .role(Role.ROLE_MENTEE)
                .name("Test User")
                .password("testPass")
                .email("test@example.com")
                .nickname("testUser")
                .build();
        Member savedMember = memberRepository.save(member);

        // when
        Member found = memberRepository.findById(savedMember.getId()).orElse(null);

        // then
        assertThat(found).isNotNull();
        assertThat(found.getId()).isEqualTo(savedMember.getId());
    }
}