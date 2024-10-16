package net.kosa.mentopingserver.domain.post.service;

import lombok.RequiredArgsConstructor;
import net.kosa.mentopingserver.domain.answer.AnswerService;
import net.kosa.mentopingserver.domain.answer.dto.AnswerResponseDto;
import net.kosa.mentopingserver.domain.hashtag.PostHashtagService;
import net.kosa.mentopingserver.domain.member.entity.Member;
import net.kosa.mentopingserver.domain.member.MemberRepository;
import net.kosa.mentopingserver.domain.member.dto.AuthorDto;
import net.kosa.mentopingserver.domain.post.dto.QuestionRequestDto;
import net.kosa.mentopingserver.domain.post.dto.QuestionResponseDto;
import net.kosa.mentopingserver.domain.post.entity.Post;
import net.kosa.mentopingserver.domain.post.repository.PostLikesRepository;
import net.kosa.mentopingserver.domain.post.repository.PostRepository;
import net.kosa.mentopingserver.global.common.enums.Category;
import net.kosa.mentopingserver.global.exception.MemberNotFoundException;
import net.kosa.mentopingserver.global.exception.PostNotFoundException;
import net.kosa.mentopingserver.global.exception.UnauthorizedException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class QuestionServiceImpl implements QuestionService {

    private final PostRepository postRepository;
    private final MemberRepository memberRepository;
    private final PostHashtagService postHashtagService;
    private final AnswerService answerService;
    private final PostLikesRepository postLikesRepository;
    private final PostLikeService postLikeService;

    @Override
    @Transactional(readOnly = true)
    public Page<QuestionResponseDto> getAllQuestions(Pageable pageable, String keyword, Long currentUserId) {
        Page<Post> posts;
        if (keyword != null && !keyword.trim().isEmpty()) {
            List<String> keywords = Arrays.stream(keyword.split("\\s+"))
                    .filter(k -> !k.isEmpty())
                    .collect(Collectors.toList());
            posts = !keywords.isEmpty()
                    ? postRepository.findQuestionsByKeywords(keywords, pageable)
                    : postRepository.findAllQuestions(pageable);
        } else {
            posts = postRepository.findAllQuestions(pageable);
        }

        return posts.map(post -> toQuestionResponseDto(post, false, currentUserId));
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

        return toQuestionResponseDto(savedPost, false, null);
    }

    @Override
    @Transactional(readOnly = true)
    public QuestionResponseDto getQuestionById(Long postId, Long currentUserId) {
        Post post = postRepository.findPostWithAnswersById(postId)
                .orElseThrow(() -> new PostNotFoundException("Post not found with id: " + postId));

        // post가 멘토링 게시글일 경우 예외 발생
        if (post.getPrice() != null) {
            throw new IllegalArgumentException("The post with id " + postId + " is not a question.");
        }

        return toQuestionResponseDto(post, true, currentUserId);
    }

    @Override
    @Transactional
    public QuestionResponseDto updateQuestion(Long postId, QuestionRequestDto questionRequestDto, Long memberId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new PostNotFoundException("Post not found with id: " + postId));

        if (!post.getMember().getId().equals(memberId)) {
            throw new UnauthorizedException("You don't have permission to update this question");
        }

        post = post.toBuilder()
                .title(questionRequestDto.getTitle())
                .content(questionRequestDto.getContent())
                .category(questionRequestDto.getCategory())
                .build();

        Post updatedPost = postRepository.save(post);

        if (questionRequestDto.getHashtags() != null) {
            postHashtagService.setHashtag(updatedPost, questionRequestDto.getHashtags());
        }

        return toQuestionResponseDto(updatedPost, false, null);
    }

    @Override
    @Transactional
    public void deleteQuestion(Long postId, Long memberId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new PostNotFoundException("Post not found with id: " + postId));

        // 권한 검사
        if (!post.getMember().getId().equals(memberId)) {
            throw new UnauthorizedException("You don't have permission to delete this question");
        }
        
        postRepository.delete(post);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<QuestionResponseDto> getQuestionsByMemberId(Long memberId, Pageable pageable, Long currentUserId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new MemberNotFoundException("Member not found with id: " + memberId));
        Page<Post> posts = postRepository.findByMemberAndPriceIsNull(member, pageable);

        return posts.map(post -> toQuestionResponseDto(post, false, currentUserId));
    }

    @Override
    @Transactional(readOnly = true)
    public Page<QuestionResponseDto> getQuestionsByCategory(Category category, Pageable pageable, String keyword, Long currentUserId) {
        Page<Post> posts;
        if (keyword != null && !keyword.trim().isEmpty()) {
            List<String> keywords = Arrays.stream(keyword.split("\\s+"))
                    .filter(k -> !k.isEmpty())
                    .collect(Collectors.toList());
            posts = !keywords.isEmpty()
                    ? postRepository.findQuestionsByCategoryAndKeywords(category, keywords, pageable)
                    : postRepository.findByCategoryAndPriceIsNull(category, pageable);
        } else {
            posts = postRepository.findByCategoryAndPriceIsNull(category, pageable);
        }

        return posts.map(post -> toQuestionResponseDto(post, false, currentUserId));
    }

    @Override
    @Transactional
    public void markAnswerAsSelected(Long questionId, Long answerId) {
        Post post = postRepository.findById(questionId)
                .orElseThrow(() -> new PostNotFoundException("Post not found with id: " + questionId));

        post.getAnswers().forEach(answer -> {
            answer.setIsSelected(answer.getId().equals(answerId));
        });
    }

    @Override
    @Transactional
    public Map<Category, Long> getQuestionCountByCategory() {
        // 모든 카테고리에 대해 질문 수를 조회
        List<Object[]> results = postRepository.countPostsByCategory(List.of(Category.values()));
        Map<Category, Long> categoryCounts = new HashMap<>();

        // 결과를 Category, Count로 변환하여 Map에 저장
        for (Object[] result : results) {
            Category category = (Category) result[0];
            Long count = (Long) result[1];
            categoryCounts.put(category, count);
        }

        return categoryCounts;
    }

    private QuestionResponseDto toQuestionResponseDto(Post post, boolean includeAnswers, Long currentUserId) {
        List<String> hashtags = postHashtagService.getPostHashtags(post).stream()
                .map(postHashtag -> postHashtag.getHashtag().getName())
                .distinct()
                .collect(Collectors.toList());

        List<AnswerResponseDto> answers = includeAnswers
                ? post.getAnswers().stream()
                .map(answerService::toAnswerResponseDto)
                .collect(Collectors.toList())
                : null;

        boolean isLikedByCurrentUser = false;
        if (currentUserId != null) {
            isLikedByCurrentUser = postLikeService.hasUserLikedPost(post.getId(), currentUserId);
        }

        return QuestionResponseDto.builder()
                .id(post.getId())
                .title(post.getTitle())
                .content(post.getContent())
                .author(toAuthorDto(post.getMember()))
                .createdAt(post.getCreatedAt())
                .category(post.getCategory().getName())
                .hashtags(hashtags)
                .likeCount(post.getLikeCount())
                .answerCount(post.getAnswerCount())
                .isSelected(post.isSelected())
                .isLikedByCurrentUser(isLikedByCurrentUser)
                .answers(answers)
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