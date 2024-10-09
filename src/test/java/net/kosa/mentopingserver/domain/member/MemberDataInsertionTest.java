package net.kosa.mentopingserver.domain.member;

import net.kosa.mentopingserver.global.common.enums.Role;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
//@ActiveProfiles("test")
//@Transactional
public class MemberDataInsertionTest {

    @Autowired
    private MemberRepository memberRepository;

    @Test
    public void insertTestData() {
        Member member1 = Member.builder()
                .name("홍길동")
                .email("hong@example.com")
                .password("password123")
                .role(Role.ROLE_MENTEE)
                .nickname("길동이")
                .oauthId("12345")
                .build();

        Member member2 = Member.builder()
                .name("김철수")
                .email("kim@example.com")
                .password("password456")
                .role(Role.ROLE_ADMIN)
                .nickname("철수킴")
                .oauthId("67890")
                .build();

        memberRepository.saveAll(Arrays.asList(member1, member2));

        List<Member> savedMembers = memberRepository.findAll();
        assertThat(savedMembers).hasSize(2);
        assertThat(savedMembers.get(0).getName()).isEqualTo("홍길동");
        assertThat(savedMembers.get(1).getName()).isEqualTo("김철수");
    }
}