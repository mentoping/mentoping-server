package net.kosa.mentopingserver.domain.post.service;

import lombok.RequiredArgsConstructor;
import net.kosa.mentopingserver.domain.hashtag.PostHashtagService;
import net.kosa.mentopingserver.domain.member.dto.AuthorDto;
import net.kosa.mentopingserver.domain.post.dto.MentoringRequestDto;
import net.kosa.mentopingserver.domain.post.dto.MentoringResponseDto;
import net.kosa.mentopingserver.domain.post.entity.Post;
import net.kosa.mentopingserver.domain.member.entity.Member;
import net.kosa.mentopingserver.domain.member.MemberRepository;
import net.kosa.mentopingserver.domain.post.repository.PostRepository;
import net.kosa.mentopingserver.global.common.enums.Category;
import net.kosa.mentopingserver.global.exception.MemberNotFoundException;
import net.kosa.mentopingserver.global.exception.PostNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MentoringServiceImpl implements MentoringService {

    private final PostRepository postRepository;
    private final MemberRepository memberRepository;
    private final PostHashtagService postHashtagService;
    private final PostLikeService postLikeService;

    @Override
    @Transactional(readOnly = true)
    public Page<MentoringResponseDto> getAllMentorings(Pageable pageable, String keyword, Long currentUserId) {
        Page<Post> posts;
        if (keyword != null && !keyword.trim().isEmpty()) {
            List<String> keywords = Arrays.stream(keyword.split("\\s+"))
                    .filter(k -> !k.isEmpty())
                    .collect(Collectors.toList());
            if (!keywords.isEmpty()) {
                posts = postRepository.findMentoringsByKeywords(keywords, pageable);
            } else {
                posts = postRepository.findAllMentorings(pageable);
            }
        } else {
            posts = postRepository.findAllMentorings(pageable);
        }
        return posts.map(post -> toMentoringResponseDto(post, currentUserId));
    }

    @Override
    @Transactional
    public MentoringResponseDto createMentoring(MentoringRequestDto mentoringRequestDto, Long memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new MemberNotFoundException("Member not found with id: " + memberId));

        Post post = Post.builder()
                .title(mentoringRequestDto.getTitle())
                .content(mentoringRequestDto.getContent())
                .member(member)
                .category(mentoringRequestDto.getCategory())
                .price(mentoringRequestDto.getPrice())
                .thumbnailUrl(mentoringRequestDto.getThumbnailUrl())
                .summary(mentoringRequestDto.getSummary())
                .build();

        Post savedPost = postRepository.save(post);

        if (mentoringRequestDto.getHashtags() != null && !mentoringRequestDto.getHashtags().isEmpty()) {
            postHashtagService.setHashtag(savedPost, mentoringRequestDto.getHashtags());
        }

        return toMentoringResponseDto(savedPost, null);
    }

    @Override
    @Transactional(readOnly = true)
    public MentoringResponseDto getMentoringById(Long postId, Long currentUserId) {
        Post post = postRepository.findPostWithAnswersById(postId)
                .orElseThrow(() -> new PostNotFoundException("Post not found with id: " + postId));

        // mentoring이 질문 게시글일 경우 예외 발생
        if (post.getPrice() == null) {
            throw new IllegalArgumentException("The post with id " + postId + " is not a mentoring.");
        }

        return toMentoringResponseDto(post, currentUserId);
    }

    @Override
    @Transactional
    public MentoringResponseDto updateMentoring(Long postId, MentoringRequestDto mentoringRequestDto) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new PostNotFoundException("Post not found with id: " + postId));

        post = post.toBuilder()
                .title(mentoringRequestDto.getTitle())
                .content(mentoringRequestDto.getContent())
                .category(mentoringRequestDto.getCategory())
                .price(mentoringRequestDto.getPrice())
                .thumbnailUrl(mentoringRequestDto.getThumbnailUrl())
                .summary(mentoringRequestDto.getSummary())
                .build();

        Post updatedPost = postRepository.save(post);

        if (mentoringRequestDto.getHashtags() != null) {
            postHashtagService.setHashtag(updatedPost, mentoringRequestDto.getHashtags());
        }

        return toMentoringResponseDto(updatedPost, null);
    }

    @Override
    @Transactional
    public void deleteMentoring(Long postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new PostNotFoundException("Post not found with id: " + postId));
        postRepository.delete(post);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<MentoringResponseDto> getMentoringsByMemberId(Long memberId, Pageable pageable, Long currentUserId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new MemberNotFoundException("Member not found with id: " + memberId));
        Page<Post> posts = postRepository.findByMemberAndPriceIsNotNull(member, pageable);
        return posts.map(post -> toMentoringResponseDto(post, currentUserId));
    }

    @Override
    @Transactional(readOnly = true)
    public Page<MentoringResponseDto> getMentoringsByCategory(Category category, Pageable pageable, String keyword, Long currentUserId) {
        Page<Post> posts;
        if (keyword != null && !keyword.trim().isEmpty()) {
            List<String> keywords = Arrays.stream(keyword.split("\\s+"))
                    .filter(k -> !k.isEmpty())
                    .collect(Collectors.toList());
            if (!keywords.isEmpty()) {
                posts = postRepository.findMentoringsByCategoryAndKeywords(category, keywords, pageable);
            } else {
                posts = postRepository.findByCategoryAndPriceIsNotNull(category, pageable);
            }
        } else {
            posts = postRepository.findByCategoryAndPriceIsNotNull(category, pageable);
        }
        return posts.map(post -> toMentoringResponseDto(post, currentUserId));
    }

    private MentoringResponseDto toMentoringResponseDto(Post post, Long currentUserId) {
        List<String> hashtags = postHashtagService.getPostHashtags(post).stream()
                .map(postHashtag -> postHashtag.getHashtag().getName())
                .distinct()
                .collect(Collectors.toList());

        boolean isLikedByCurrentUser = false;
        if (currentUserId != null) {
            isLikedByCurrentUser = postLikeService.hasUserLikedPost(post.getId(), currentUserId);
        }

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
                .rating(null)
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
