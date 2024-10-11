package net.kosa.mentopingserver.domain.post;

import net.kosa.mentopingserver.domain.member.entity.Member;
import net.kosa.mentopingserver.domain.member.MemberRepository;
import net.kosa.mentopingserver.domain.post.dto.QuestionRequestDto;
import net.kosa.mentopingserver.domain.post.dto.QuestionResponseDto;
import net.kosa.mentopingserver.global.common.enums.Category;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
//@ActiveProfiles("test")
public class QuestionDataInsertionTest {

    @Autowired
    private QuestionService questionService;

    @Autowired
    private MemberRepository memberRepository;

    private Member testMember;

    @BeforeEach
    void setUp() {
        // MemberDataInsertionTest에서 생성된 회원 데이터를 활용
        testMember = memberRepository.findByEmail("hong@example.com")
                .orElseThrow(() -> new IllegalArgumentException("Test member not found"));
    }

    @Test
    void insertDummyQuestions() {
        // given
        for (int i = 1; i <= 20; i++) {
            QuestionRequestDto questionRequestDto = QuestionRequestDto.builder()
                    .title("Dummy Question " + i)
                    .content("Dummy Content " + i)
                    .category(Category.ITSW)
                    .build();

            questionService.createQuestion(questionRequestDto, testMember.getId());
        }

        PageRequest pageRequest = PageRequest.of(0, 10);
        Page<QuestionResponseDto> responses = questionService.getAllQuestions(pageRequest, null);

        // then
        assertThat(responses).isNotNull();
        assertThat(responses.getTotalElements()).isEqualTo(20);
        assertThat(responses.getContent().size()).isEqualTo(10);
    }

    @Test
    void testPagination() {
        // given - 이미 저장된 데이터가 있다고 가정하고 페이징 테스트
        PageRequest firstPageRequest = PageRequest.of(0, 5);
        PageRequest secondPageRequest = PageRequest.of(1, 5);

        // when
        Page<QuestionResponseDto> firstPage = questionService.getAllQuestions(firstPageRequest, null);
        Page<QuestionResponseDto> secondPage = questionService.getAllQuestions(secondPageRequest, null);

        // then
        assertThat(firstPage).isNotNull();
        assertThat(firstPage.getContent().size()).isEqualTo(5);
        assertThat(firstPage.isFirst()).isTrue();
        assertThat(firstPage.isLast()).isFalse();

        assertThat(secondPage).isNotNull();
        assertThat(secondPage.getContent().size()).isEqualTo(5);
        assertThat(secondPage.isFirst()).isFalse();
        assertThat(secondPage.isLast()).isFalse();
    }
}