package net.kosa.mentopingserver.domain.post;

import net.kosa.mentopingserver.domain.member.Member;
import net.kosa.mentopingserver.domain.member.MemberRepository;
import net.kosa.mentopingserver.domain.post.dto.QuestionRequestDto;
import net.kosa.mentopingserver.domain.post.dto.QuestionResponseDto;
import net.kosa.mentopingserver.global.common.enums.SubCategory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
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
        QuestionRequestDto questionRequestDto1 = QuestionRequestDto.builder()
                .title("Dummy Question 1")
                .content("Dummy Content 1")
                .category(SubCategory.JAVA)
                .build();

        QuestionRequestDto questionRequestDto2 = QuestionRequestDto.builder()
                .title("Dummy Question 2")
                .content("Dummy Content 2")
                .category(SubCategory.PYTHON)
                .build();

        // when
        questionService.createQuestion(questionRequestDto1, testMember.getId());
        questionService.createQuestion(questionRequestDto2, testMember.getId());

        PageRequest pageRequest = PageRequest.of(0, 10);
        Page<QuestionResponseDto> responses = questionService.getAllQuestions(pageRequest);

        // then
        assertThat(responses).isNotNull();
        assertThat(responses.getTotalElements()).isEqualTo(2);
        assertThat(responses.getContent().get(0).getTitle()).isEqualTo("Dummy Question 1");
        assertThat(responses.getContent().get(1).getTitle()).isEqualTo("Dummy Question 2");
    }
}