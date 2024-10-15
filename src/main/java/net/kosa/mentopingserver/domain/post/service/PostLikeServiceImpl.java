package net.kosa.mentopingserver.domain.post.service;

import lombok.RequiredArgsConstructor;
import net.kosa.mentopingserver.domain.hashtag.PostHashtagService;
import net.kosa.mentopingserver.domain.member.MemberRepository;
import net.kosa.mentopingserver.domain.member.dto.AuthorDto;
import net.kosa.mentopingserver.domain.member.entity.Member;
import net.kosa.mentopingserver.domain.post.dto.MentoringResponseDto;
import net.kosa.mentopingserver.domain.post.dto.MentoringReviewDto;
import net.kosa.mentopingserver.domain.post.dto.QuestionResponseDto;
import net.kosa.mentopingserver.domain.post.entity.Post;
import net.kosa.mentopingserver.domain.post.entity.PostLikes;
import net.kosa.mentopingserver.domain.post.repository.PostLikesRepository;
import net.kosa.mentopingserver.domain.post.repository.PostRepository;
import net.kosa.mentopingserver.global.exception.MemberNotFoundException;
import net.kosa.mentopingserver.global.exception.PostNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PostLikeServiceImpl implements PostLikeService {

    private final PostLikesRepository postLikesRepository;
    private final PostRepository postRepository;
    private final MemberRepository memberRepository;
    private final PostHashtagService postHashtagService;
    private final MentoringReviewService mentoringReviewService;

    @Override
    @Transactional
    public void addLike(Long postId, Long memberId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new PostNotFoundException("Post not found with id: " + postId));
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new MemberNotFoundException("Member not found with id: " + memberId));

        if (!postLikesRepository.existsByPostAndMember(post, member)) {
            post.incrementLikeCount();
            postLikesRepository.save(PostLikes.builder().post(post).member(member).build());
        }
    }

    @Override
    @Transactional
    public void removeLike(Long postId, Long memberId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new PostNotFoundException("Post not found with id: " + postId));
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new MemberNotFoundException("Member not found with id: " + memberId));

        PostLikes postLike = postLikesRepository.findByPostAndMember(post, member)
                .orElseThrow(() -> new RuntimeException("Like not found"));

        post.decrementLikeCount();
        postLikesRepository.delete(postLike);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean hasUserLikedPost(Long postId, Long memberId) {
        return postLikesRepository.existsByPostIdAndMemberId(postId, memberId);
    }

    @Override
    @Transactional
    public Page<QuestionResponseDto> getLikedQuestions(Long memberId, Pageable pageable) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new RuntimeException("Member not found"));

        Page<Post> questionPosts = postRepository.findLikedQuestionsByMember(member, pageable);

        return questionPosts.map(post ->
                toQuestionResponseDto(post, memberId)
        );
    }

    @Override
    @Transactional
    public Page<MentoringResponseDto> getLikedMentorings(Long memberId, Pageable pageable) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new RuntimeException("Member not found"));

        Page<Post> mentoringPosts = postRepository.findLikedMentoringsByMember(member, pageable);

        return mentoringPosts.map(post ->
                toMentoringResponseDto(post, memberId)
        );
    }

    @Override
    @Transactional
    public Map<Long, Boolean> batchToggleLike(List<Long> postIds, Long memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new MemberNotFoundException("Member not found with id: " + memberId));

        List<Post> posts = postRepository.findAllById(postIds);
        if (posts.size() != postIds.size()) {
            throw new PostNotFoundException("One or more posts not found");
        }

        Map<Long, Integer> toggleCounts = new HashMap<>();
        for (Long postId : postIds) {
            toggleCounts.put(postId, toggleCounts.getOrDefault(postId, 0) + 1);
        }

        Set<Long> initialLikedPostIds = postLikesRepository.findExistingLikePostIds(member.getId(), new HashSet<>(postIds));

        Map<Long, Boolean> result = new HashMap<>();

        for (Post post : posts) {
            int toggleCount = toggleCounts.get(post.getId());
            boolean wasInitiallyLiked = initialLikedPostIds.contains(post.getId());
            boolean isFinallyLiked = (wasInitiallyLiked && toggleCount % 2 == 0) || (!wasInitiallyLiked && toggleCount % 2 == 1);

            if (isFinallyLiked != wasInitiallyLiked) {
                if (isFinallyLiked) {
                    postLikesRepository.save(PostLikes.builder().post(post).member(member).build());
                    post.incrementLikeCount();
                } else {
                    postLikesRepository.deleteByPostAndMember(post, member);
                    post.decrementLikeCount();
                }
            }

            result.put(post.getId(), isFinallyLiked);
        }

        postRepository.saveAll(posts);

        return result;
    }

    private QuestionResponseDto toQuestionResponseDto(Post post, Long currentUserId) {
        List<String> hashtags = postHashtagService.getPostHashtags(post).stream()
                .map(postHashtag -> postHashtag.getHashtag().getName())
                .distinct()
                .collect(Collectors.toList());

        boolean isLikedByCurrentUser = hasUserLikedPost(post.getId(), currentUserId);

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
                .build();
    }

    private MentoringResponseDto toMentoringResponseDto(Post post, Long currentUserId) {
        List<String> hashtags = postHashtagService.getPostHashtags(post).stream()
                .map(postHashtag -> postHashtag.getHashtag().getName())
                .distinct()
                .collect(Collectors.toList());

        boolean isLikedByCurrentUser = hasUserLikedPost(post.getId(), currentUserId);

        // Fetch average rating
        Double averageRating = mentoringReviewService.getAverageRating(post.getId());

        // Fetch a preview of reviews (e.g., first 3 reviews)
        List<MentoringReviewDto> reviewsPreview = mentoringReviewService.getReviewsByMentoring(post.getId(), PageRequest.of(0, 3)).getContent();


        return MentoringResponseDto.builder()
                .id(post.getId())
                .title(post.getTitle())
                .content(post.getContent())
                .thumbnailUrl(post.getThumbnailUrl())
                .summary(post.getSummary())
                .author(toAuthorDto(post.getMember()))
                .createdAt(post.getCreatedAt())
                .category(post.getCategory().getName())
                .hashtags(hashtags)
                .likeCount(post.getLikeCount())
                .isActive(true)
                .price(post.getPrice())
                .isLikedByCurrentUser(isLikedByCurrentUser)
                .averageRating(averageRating)
                .reviews(reviewsPreview)
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
