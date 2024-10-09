package net.kosa.mentopingserver.domain.post;

import net.kosa.mentopingserver.domain.member.Member;
import net.kosa.mentopingserver.domain.member.MemberRepository;
import net.kosa.mentopingserver.domain.post.dto.QuestionRequestDto;
import net.kosa.mentopingserver.domain.post.dto.QuestionResponseDto;
import net.kosa.mentopingserver.domain.answer.Answer;
import net.kosa.mentopingserver.domain.post.entity.Post;
import net.kosa.mentopingserver.global.common.enums.Role;
import net.kosa.mentopingserver.global.common.enums.Category;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
@ActiveProfiles("test")
public class QuestionServiceTest {

    @Autowired
    private QuestionService questionService;

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
    void updateQuestion_success() {
        // given
        QuestionRequestDto questionRequestDto = QuestionRequestDto.builder()
                .title("Updated Title")
                .content("Updated Content")
                .category(Category.ITSW)
                .build();

        QuestionResponseDto createdQuestion = questionService.createQuestion(
                QuestionRequestDto.builder().title("Original Title").content("Original Content").category(Category.ITSW).build(),
                testMember.getId()
        );

        // when
        QuestionResponseDto updatedQuestion = questionService.updateQuestion(createdQuestion.getId(), questionRequestDto);

        // then
        assertThat(updatedQuestion).isNotNull();
        assertThat(updatedQuestion.getTitle()).isEqualTo("Updated Title");
        assertThat(updatedQuestion.getContent()).isEqualTo("Updated Content");
    }

    @Test
    void deleteQuestion_success() {
        // given
        QuestionResponseDto createdQuestion = questionService.createQuestion(
                QuestionRequestDto.builder().title("Title to Delete").content("Content to Delete").category(Category.ITSW).build(),
                testMember.getId()
        );

        // when
        questionService.deleteQuestion(createdQuestion.getId());

        // then
        assertThat(postRepository.existsById(createdQuestion.getId())).isFalse();
    }

    @Test
    void getQuestionsByMemberId_success() {
        // given
        questionService.createQuestion(
                QuestionRequestDto.builder().title("Member Question 1").content("Content 1").category(Category.ITSW).build(),
                testMember.getId()
        );
        questionService.createQuestion(
                QuestionRequestDto.builder().title("Member Question 2").content("Content 2").category(Category.ITSW).build(),
                testMember.getId()
        );

        PageRequest pageRequest = PageRequest.of(0, 10);

        // when
        Page<QuestionResponseDto> questionsByMember = questionService.getQuestionsByMemberId(testMember.getId(), pageRequest);

        // then
        assertThat(questionsByMember).isNotNull();
        assertThat(questionsByMember.getContent()).extracting("title").contains("Member Question 1", "Member Question 2");
    }

    @Test
    void getQuestionsByCategory_success() {
        // given
        questionService.createQuestion(
                QuestionRequestDto.builder().title("Category Question 1").content("Content 1").category(Category.ITSW).build(),
                testMember.getId()
        );
        questionService.createQuestion(
                QuestionRequestDto.builder().title("Category Question 2").content("Content 2").category(Category.ITSW).build(),
                testMember.getId()
        );

        PageRequest pageRequest = PageRequest.of(0, 10);

        // when
        Page<QuestionResponseDto> questionsByCategory = questionService.getQuestionsByCategory(Category.ITSW, pageRequest, null);

        // then
        assertThat(questionsByCategory).isNotNull();
        assertThat(questionsByCategory.getContent()).extracting("title").contains("Category Question 1", "Category Question 2");
    }

    @Test
    void markAnswerAsSelected_success() {
        // given
        QuestionResponseDto createdQuestion = questionService.createQuestion(
                QuestionRequestDto.builder().title("Question with Answers").content("Content").category(Category.ITSW).build(),
                testMember.getId()
        );

        Post post = postRepository.findById(createdQuestion.getId())
                .orElseThrow(() -> new IllegalArgumentException("Post not found"));

        Answer firstAnswer = Answer.builder().content("First Answer").member(testMember).post(post).build();
        Answer secondAnswer = Answer.builder().content("Second Answer").member(testMember).post(post).build();
        post.getAnswers().add(firstAnswer);
        post.getAnswers().add(secondAnswer);
        postRepository.save(post);

        Long answerId = post.getAnswers().get(0).getId();

        // when
        questionService.markAnswerAsSelected(createdQuestion.getId(), answerId);

        // then
        post = postRepository.findById(createdQuestion.getId())
                .orElseThrow(() -> new IllegalArgumentException("Post not found"));

        assertThat(post.getAnswers().stream().anyMatch(Answer::getIsSelected)).isTrue();
        assertThat(post.getAnswers().get(0).getIsSelected()).isTrue();
        assertThat(post.getAnswers().get(1).getIsSelected()).isFalse();
    }
}