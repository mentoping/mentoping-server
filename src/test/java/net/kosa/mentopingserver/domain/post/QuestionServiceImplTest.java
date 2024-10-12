package net.kosa.mentopingserver.domain.post;

import net.kosa.mentopingserver.domain.hashtag.PostHashtagService;
import net.kosa.mentopingserver.domain.member.entity.Member;
import net.kosa.mentopingserver.domain.member.MemberRepository;
import net.kosa.mentopingserver.domain.post.dto.QuestionRequestDto;
import net.kosa.mentopingserver.domain.post.dto.QuestionResponseDto;
import net.kosa.mentopingserver.domain.post.entity.Post;
import net.kosa.mentopingserver.domain.post.repository.PostRepository;
import net.kosa.mentopingserver.domain.post.service.QuestionService;
import net.kosa.mentopingserver.global.common.enums.Role;
import net.kosa.mentopingserver.global.common.enums.Category;
import net.kosa.mentopingserver.global.exception.MemberNotFoundException;
import net.kosa.mentopingserver.global.exception.PostNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@Transactional
@ActiveProfiles("test")
class QuestionServiceImplTest {

    @Autowired
    private QuestionService questionService;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private PostHashtagService postHashtagService;

    private Member testMember;

    @BeforeEach
    void setUp() {
        // 테스트에 사용할 회원을 미리 생성
        testMember = memberRepository.save(Member.builder()
                .name("Test User")
                .nickname("TestUserNick")
                .email("testuser@example.com")
                .password("password123")
                .role(Role.ROLE_MENTEE)  // role 필드를 Enum 값으로 설정
                .profile("profile_url")
                .content("Test content")
                .oauthId("3456")
                .build());
    }

    @Test
    void createQuestion_success() {
        // given
        QuestionRequestDto questionRequestDto = QuestionRequestDto.builder()
                .title("Test Title")
                .content("Test Content")
                .category(Category.ITSW)
                .build();

        // when
        QuestionResponseDto response = questionService.createQuestion(questionRequestDto, testMember.getId());

        // then
        assertThat(response).isNotNull();
        assertThat(response.getTitle()).isEqualTo(questionRequestDto.getTitle());
        assertThat(response.getContent()).isEqualTo(questionRequestDto.getContent());
        assertThat(response.getAuthor().getId()).isEqualTo(testMember.getId());

        // 저장된 Post 확인
        Optional<Post> savedPost = postRepository.findById(response.getId());
        assertThat(savedPost).isPresent();
        assertThat(savedPost.get().getTitle()).isEqualTo(questionRequestDto.getTitle());

        // 추가된 검증: postHashtags 및 answers 초기화 확인
        assertThat(savedPost.get().getPostHashtags()).isNotNull();
        assertThat(savedPost.get().getAnswers()).isNotNull();
        assertThat(savedPost.get().getPostHashtags()).isEmpty();  // 저장된 해시태그가 없으므로 empty 확인
        assertThat(savedPost.get().getAnswers()).isEmpty();  // 저장된 답변이 없으므로 empty 확인
    }

    @Test
    void createQuestion_memberNotFound() {
        // given
        QuestionRequestDto questionRequestDto = QuestionRequestDto.builder()
                .title("Test Title")
                .content("Test Content")
                .category(Category.ITSW)
                .build();

        // when, then
        assertThrows(MemberNotFoundException.class, () -> {
            questionService.createQuestion(questionRequestDto, 999L);  // 존재하지 않는 회원 ID
        });
    }

    @Test
    void getQuestionById_success() {
        // given
        Post post = Post.builder()
                .title("Sample Post")
                .content("Sample Content")
                .member(testMember)
                .category(Category.ITSW)
                .build();

        postRepository.save(post);

        // when
        QuestionResponseDto response = questionService.getQuestionById(post.getId(), 3L);

        // then
        assertThat(response).isNotNull();
        assertThat(response.getTitle()).isEqualTo(post.getTitle());
        assertThat(response.getContent()).isEqualTo(post.getContent());
        assertThat(response.getAuthor().getId()).isEqualTo(testMember.getId());

        // 추가된 검증: postHashtags 및 answers 초기화 확인
        Optional<Post> savedPost = postRepository.findById(post.getId());
        assertThat(savedPost.get().getPostHashtags()).isNotNull();
        assertThat(savedPost.get().getAnswers()).isNotNull();
        assertThat(savedPost.get().getPostHashtags()).isEmpty();  // 해시태그가 없으므로 empty
        assertThat(savedPost.get().getAnswers()).isEmpty();  // 답변이 없으므로 empty
    }

    @Test
    void getQuestionById_postNotFound() {
        // when, then
        assertThrows(PostNotFoundException.class, () -> {
            questionService.getQuestionById(999L, 3L);  // 존재하지 않는 포스트 ID
        });
    }

    @Test
    void getAllQuestions_success() {
        // given
        Post post1 = Post.builder()
                .title("Sample Post 1")
                .content("Sample Content 1")
                .member(testMember)
                .category(Category.ITSW)
                .build();

        Post post2 = Post.builder()
                .title("Sample Post 2")
                .content("Sample Content 2")
                .member(testMember)
                .category(Category.DESIGN)
                .build();

        postRepository.save(post1);
        postRepository.save(post2);

        // 페이징 요청 생성 (첫 페이지, 사이즈 10)
        PageRequest pageRequest = PageRequest.of(0, 10);

        // when
        Page<QuestionResponseDto> responses = questionService.getAllQuestions(pageRequest, null, 3L);

        // then
        assertThat(responses).isNotNull();
        assertThat(responses.getTotalElements()).isEqualTo(2);
        assertThat(responses.getContent().get(0).getTitle()).isEqualTo(post1.getTitle());
        assertThat(responses.getContent().get(1).getTitle()).isEqualTo(post2.getTitle());
    }
}