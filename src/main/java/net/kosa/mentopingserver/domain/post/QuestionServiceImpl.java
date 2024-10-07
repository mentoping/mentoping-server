package net.kosa.mentopingserver.domain.post;

import lombok.RequiredArgsConstructor;
import net.kosa.mentopingserver.domain.hashtag.PostHashtagService;
import net.kosa.mentopingserver.domain.mentor.dto.AuthorDto;
import net.kosa.mentopingserver.domain.post.dto.QuestionRequestDto;
import net.kosa.mentopingserver.domain.post.dto.QuestionResponseDto;
import net.kosa.mentopingserver.domain.post.entity.Post;
import net.kosa.mentopingserver.domain.member.Member;
import net.kosa.mentopingserver.domain.member.MemberRepository;
import net.kosa.mentopingserver.global.common.enums.SubCategory;
import net.kosa.mentopingserver.global.exception.MemberNotFoundException;
import net.kosa.mentopingserver.global.exception.PostNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class QuestionServiceImpl implements QuestionService {

    private final PostRepository postRepository;
    private final MemberRepository memberRepository;
    private final PostLikesRepository postLikesRepository;
    private final PostHashtagService postHashtagService;

    @Override
    @Transactional(readOnly = true)
    public Page<QuestionResponseDto> getAllQuestions(Pageable pageable) {
        Page<Post> posts = postRepository.findAll(pageable);
        return posts.map(this::toQuestionResponseDto);
    }

    @Override
    @Transactional
    public QuestionResponseDto createQuestion(QuestionRequestDto questionRequestDto, Long memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new MemberNotFoundException("Member not found with id: " + memberId));

        Post post = Post.builder()
                .title(questionRequestDto.getTitle())
                .content(questionRequestDto.getContent())
                .member(member)
                .category(questionRequestDto.getCategory())
                .build();

        Post savedPost = postRepository.save(post);

        if (questionRequestDto.getHashtags() != null && !questionRequestDto.getHashtags().isEmpty()) {
            postHashtagService.setHashtag(savedPost, questionRequestDto.getHashtags());
        }

        return toQuestionResponseDto(savedPost);
    }

    @Override
    @Transactional(readOnly = true)
    public QuestionResponseDto getQuestionById(Long postId) {
        Post post = postRepository.findPostWithAnswersById(postId)
                .orElseThrow(() -> new PostNotFoundException("Post not found with id: " + postId));

        return toQuestionResponseDto(post);
    }

    @Transactional
    public QuestionResponseDto updateQuestion(Long postId, QuestionRequestDto questionRequestDto) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new PostNotFoundException("Post not found with id: " + postId));

        post = post.toBuilder()
                .title(questionRequestDto.getTitle())
                .content(questionRequestDto.getContent())
                .category(questionRequestDto.getCategory())
                .build();

        Post updatedPost = postRepository.save(post);

        if (questionRequestDto.getHashtags() != null) {
            postHashtagService.setHashtag(updatedPost, questionRequestDto.getHashtags());
        }

        return toQuestionResponseDto(updatedPost);
    }

    @Override
    @Transactional
    public void deleteQuestion(Long postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new PostNotFoundException("Post not found with id: " + postId));
        postRepository.delete(post);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<QuestionResponseDto> getQuestionsByMemberId(Long memberId, Pageable pageable) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new MemberNotFoundException("Member not found with id: " + memberId));
        Page<Post> posts = postRepository.findByMember(member, pageable);
        return posts.map(this::toQuestionResponseDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<QuestionResponseDto> getQuestionsByCategory(SubCategory category, Pageable pageable) {
        Page<Post> posts = postRepository.findByCategory(category, pageable);
        return posts.map(this::toQuestionResponseDto);
    }

    @Override
    @Transactional
    public void markAnswerAsSelected(Long questionId, Long answerId) {
        Post post = postRepository.findById(questionId)
                .orElseThrow(() -> new PostNotFoundException("Post not found with id: " + questionId));

        post.getAnswers().forEach(answer -> {
            if (answer.getId().equals(answerId)) {
                answer.setIsSelected(true);
            } else {
                answer.setIsSelected(false);
            }
        });
    }

    private QuestionResponseDto toQuestionResponseDto(Post post) {
        int answerCount = post.getAnswers() != null ? post.getAnswers().size() : 0;
        int likeCount = postLikesRepository.countByPost_Id(post.getId());

        // Fetch the latest hashtags using PostHashtagService
        List<String> hashtags = postHashtagService.getPostHashtags(post).stream()
                .map(postHashtag -> postHashtag.getHashtag().getName())
                .distinct()
                .collect(Collectors.toList());

        return QuestionResponseDto.builder()
                .id(post.getId())
                .title(post.getTitle())
                .content(post.getContent())
                .author(toAuthorDto(post.getMember()))
                .createdAt(post.getCreatedAt())
                .category(post.getCategory().getName())
                .hashtags(hashtags)
                .likeCount(likeCount)
                .answerCount(answerCount)
                .isSelected(post.isSelected())
                .build();
    }

    private AuthorDto toAuthorDto(Member member) {
        return AuthorDto.builder()
                .id(member.getId())
                .name(member.getName())
                .profileUrl(member.getProfile())
                .build();
    }
}