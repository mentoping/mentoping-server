package net.kosa.mentopingserver.member;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
public class MemberRepositoryTest {

    @Autowired
    private MemberRepository memberRepository;

    @Test
    public void testCreateMember() {
        LocalDateTime now = LocalDateTime.now();
        Member member = Member.builder()
                .name("John Doe")
                .email("john.doe@example.com")
                .createdAt(now)
                .updatedAt(now)
                .build();

        Member savedMember = memberRepository.save(member);

        assertThat(savedMember.getId()).isNotNull();
        assertThat(savedMember.getName()).isEqualTo("John Doe");
        assertThat(savedMember.getEmail()).isEqualTo("john.doe@example.com");
        assertThat(savedMember.getCreatedAt()).isEqualTo(now);
        assertThat(savedMember.getUpdatedAt()).isEqualTo(now);
    }

    @Test
    public void testUpdateMember() {
        LocalDateTime now = LocalDateTime.now();
        Member member = Member.builder()
                .name("Bob Smith")
                .email("bob.smith@example.com")
                .createdAt(now)
                .updatedAt(now)
                .build();

        Member savedMember = memberRepository.save(member);

        LocalDateTime laterTime = now.plusHours(1);
        Member updatedMember = savedMember.toBuilder()
                .name("Robert Smith")
                .email("robert.smith@example.com")
                .updatedAt(laterTime)
                .build();

        updatedMember = memberRepository.save(updatedMember);

        assertThat(updatedMember.getId()).isEqualTo(savedMember.getId());
        assertThat(updatedMember.getName()).isEqualTo("Robert Smith");
        assertThat(updatedMember.getEmail()).isEqualTo("robert.smith@example.com");
        assertThat(updatedMember.getCreatedAt()).isEqualTo(now);
        assertThat(updatedMember.getUpdatedAt()).isEqualTo(laterTime);
    }

    @Test
    public void testFindMember() {
        LocalDateTime now = LocalDateTime.now();
        Member member = Member.builder()
                .name("Jane Doe")
                .email("jane.doe@example.com")
                .createdAt(now)
                .updatedAt(now)
                .build();

        Member savedMember = memberRepository.save(member);

        Member foundMember = memberRepository.findById(savedMember.getId()).orElse(null);

        assertThat(foundMember).isNotNull();
        assertThat(foundMember.getName()).isEqualTo("Jane Doe");
        assertThat(foundMember.getEmail()).isEqualTo("jane.doe@example.com");
        assertThat(foundMember.getCreatedAt()).isEqualTo(now);
        assertThat(foundMember.getUpdatedAt()).isEqualTo(now);
    }

    @Test
    public void testDeleteMember() {
        LocalDateTime now = LocalDateTime.now();
        Member member = Member.builder()
                .name("Alice Johnson")
                .email("alice.johnson@example.com")
                .createdAt(now)
                .updatedAt(now)
                .build();

        Member savedMember = memberRepository.save(member);

        memberRepository.delete(savedMember);

        Member deletedMember = memberRepository.findById(savedMember.getId()).orElse(null);

        assertThat(deletedMember).isNull();
    }
}