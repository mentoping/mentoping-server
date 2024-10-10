package net.kosa.mentopingserver.domain.post;

import net.kosa.mentopingserver.domain.member.entity.Member;
import net.kosa.mentopingserver.domain.member.MemberRepository;
import net.kosa.mentopingserver.domain.post.dto.MentoringRequestDto;
import net.kosa.mentopingserver.domain.post.dto.MentoringResponseDto;
import net.kosa.mentopingserver.global.common.enums.Category;
import net.kosa.mentopingserver.global.common.enums.Role;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@Transactional
@ActiveProfiles("test")
public class MentoringServiceTest {

    @Autowired
    private MentoringService mentoringService;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private PostRepository postRepository;

    private Member testMember;

    @BeforeEach
    void setUp() {
        testMember = Member.builder()
                .email("hong@example.com")
                .name("홍길동")
                .nickname("홍길동닉네임")
                .password("password")
                .role(Role.ROLE_MENTEE)
                .build();
        memberRepository.save(testMember);
    }

    @Test
    void createMentoring_success() {
        // given
        MentoringRequestDto requestDto = MentoringRequestDto.builder()
                .title("Mentoring Title")
                .content("Mentoring Content")
                .category(Category.ITSW)
                .build();

        // when
        MentoringResponseDto response = mentoringService.createMentoring(requestDto, testMember.getId());

        // then
        assertThat(response).isNotNull();
        assertThat(response.getTitle()).isEqualTo(requestDto.getTitle());
        assertThat(response.getContent()).isEqualTo(requestDto.getContent());
        assertThat(response.getAuthor().getId()).isEqualTo(testMember.getId());
    }

    @Test
    void updateMentoring_success() {
        // given
        MentoringRequestDto createRequest = MentoringRequestDto.builder()
                .title("Original Title")
                .content("Original Content")
                .category(Category.ITSW)
                .build();

        MentoringResponseDto createdMentoring = mentoringService.createMentoring(createRequest, testMember.getId());

        MentoringRequestDto updateRequest = MentoringRequestDto.builder()
                .title("Updated Title")
                .content("Updated Content")
                .category(Category.DESIGN)
                .build();

        // when
        MentoringResponseDto updatedResponse = mentoringService.updateMentoring(createdMentoring.getId(), updateRequest);

        // then
        assertThat(updatedResponse).isNotNull();
        assertThat(updatedResponse.getTitle()).isEqualTo("Updated Title");
        assertThat(updatedResponse.getContent()).isEqualTo("Updated Content");
        assertThat(updatedResponse.getCategory()).isEqualTo(Category.DESIGN.name());
    }

    @Test
    void deleteMentoring_success() {
        // given
        MentoringRequestDto createRequest = MentoringRequestDto.builder()
                .title("Title to Delete")
                .content("Content to Delete")
                .category(Category.ITSW)
                .build();

        MentoringResponseDto createdMentoring = mentoringService.createMentoring(createRequest, testMember.getId());

        // when
        mentoringService.deleteMentoring(createdMentoring.getId());

        // then
        assertThat(postRepository.existsById(createdMentoring.getId())).isFalse();
    }

    @Test
    void getMentoringById_success() {
        // given
        MentoringRequestDto createRequest = MentoringRequestDto.builder()
                .title("Mentoring Title")
                .content("Mentoring Content")
                .category(Category.ITSW)
                .build();

        MentoringResponseDto createdMentoring = mentoringService.createMentoring(createRequest, testMember.getId());

        // when
        MentoringResponseDto response = mentoringService.getMentoringById(createdMentoring.getId());

        // then
        assertThat(response).isNotNull();
        assertThat(response.getTitle()).isEqualTo(createdMentoring.getTitle());
        assertThat(response.getContent()).isEqualTo(createdMentoring.getContent());
    }

    @Test
    void getAllMentoring_success() {
        // given
        mentoringService.createMentoring(MentoringRequestDto.builder().title("Mentoring 1").content("Content 1").category(Category.ITSW).build(), testMember.getId());
        mentoringService.createMentoring(MentoringRequestDto.builder().title("Mentoring 2").content("Content 2").category(Category.DESIGN).build(), testMember.getId());

        PageRequest pageRequest = PageRequest.of(0, 10);

        // when
        Page<MentoringResponseDto> responses = mentoringService.getAllMentoring(pageRequest);

        // then
        assertThat(responses).isNotNull();
        assertThat(responses.getTotalElements()).isEqualTo(2);
        assertThat(responses.getContent()).extracting("title").contains("Mentoring 1", "Mentoring 2");
    }
}