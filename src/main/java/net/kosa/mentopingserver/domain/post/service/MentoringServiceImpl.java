package net.kosa.mentopingserver.domain.post.service;

import lombok.RequiredArgsConstructor;
import net.kosa.mentopingserver.domain.hashtag.PostHashtagService;
import net.kosa.mentopingserver.domain.member.dto.AuthorDto;
import net.kosa.mentopingserver.domain.post.dto.MentoringRequestDto;
import net.kosa.mentopingserver.domain.post.dto.MentoringResponseDto;
import net.kosa.mentopingserver.domain.post.entity.Post;
import net.kosa.mentopingserver.domain.member.entity.Member;
import net.kosa.mentopingserver.domain.member.MemberRepository;
import net.kosa.mentopingserver.domain.post.repository.MentoringApplicationRepository;
import net.kosa.mentopingserver.domain.post.repository.MentoringReviewRepository;
import net.kosa.mentopingserver.domain.post.repository.PostRepository;
import net.kosa.mentopingserver.global.common.enums.Category;
import net.kosa.mentopingserver.global.common.enums.Status;
import net.kosa.mentopingserver.global.exception.MemberNotFoundException;
import net.kosa.mentopingserver.global.exception.PostNotFoundException;
import net.kosa.mentopingserver.global.exception.UnauthorizedException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import net.kosa.mentopingserver.global.util.S3Service;  // S3Service 추가

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MentoringServiceImpl implements MentoringService {

    private final PostRepository postRepository;
    private final MemberRepository memberRepository;
    private final PostHashtagService postHashtagService;
    private final PostLikeService postLikeService;
    private final MentoringReviewService mentoringReviewService;
    private final S3Service s3Service;
    private final MentoringReviewRepository mentoringReviewRepository;
    private final MentoringApplicationRepository mentoringApplicationRepository;


    @Override
    @Transactional(readOnly = true)
    public Page<MentoringResponseDto> getAllMentorings(Pageable pageable, String keyword, Long currentUserId) {
        Page<Post> posts;
        if (keyword != null && !keyword.trim().isEmpty()) {
            List<String> keywords = Arrays.stream(keyword.split("\\s+"))
                    .filter(k -> !k.isEmpty())
                    .collect(Collectors.toList());
            posts = !keywords.isEmpty()
                    ? postRepository.findMentoringsByKeywords(keywords, pageable)
                    : postRepository.findAllMentorings(pageable);
        } else {
            posts = postRepository.findAllMentorings(pageable);
        }

        return posts.map(post -> toMentoringResponseDto(post, currentUserId));

    }

    @Override
    @Transactional
    public MentoringResponseDto createMentoring(MentoringRequestDto mentoringRequestDto, Long memberId) throws IOException {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new MemberNotFoundException("Member not found with id: " + memberId));

        // S3에 파일 업로드
        String uploadedFileUrl = null;
        MultipartFile thumbnailFile = mentoringRequestDto.getThumbnailUrl();
        if (thumbnailFile != null && !thumbnailFile.isEmpty()) {
            uploadedFileUrl = s3Service.uploadFile(thumbnailFile);  // S3에 파일 업로드 후 URL 반환
        }


        Post post = Post.builder()
                .title(mentoringRequestDto.getTitle())
                .content(mentoringRequestDto.getContent())
                .member(member)
                .category(mentoringRequestDto.getCategory())
                .price(mentoringRequestDto.getPrice())
                .thumbnailUrl(uploadedFileUrl)
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

        MentoringResponseDto responseDto = toMentoringResponseDto(post, currentUserId);

        responseDto.setReviews(mentoringReviewService.getReviewsByMentoring(postId, PageRequest.of(0, 10)).getContent());

        return responseDto;
    }

    @Override
    @Transactional
    public MentoringResponseDto updateMentoring(Long postId, MentoringRequestDto mentoringRequestDto, Long memberId) throws IOException {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new PostNotFoundException("Post not found with id: " + postId));

        // 권한 검사
        if (!post.getMember().getId().equals(memberId)) {
            throw new UnauthorizedException("You don't have permission to update this mentoring post");
        }

        // S3에 파일 업로드 (썸네일 파일이 존재할 경우)
        String uploadedFileUrl = post.getThumbnailUrl(); // 기존 URL 유지
        MultipartFile thumbnailFile = mentoringRequestDto.getThumbnailUrl();
        if (thumbnailFile != null && !thumbnailFile.isEmpty()) {
            uploadedFileUrl = s3Service.uploadFile(thumbnailFile);  // S3에 파일 업로드 후 URL 반환
        }

        post = post.toBuilder()
                .title(mentoringRequestDto.getTitle())
                .content(mentoringRequestDto.getContent())
                .category(mentoringRequestDto.getCategory())
                .price(mentoringRequestDto.getPrice())
                .thumbnailUrl(uploadedFileUrl)
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
    public void deleteMentoring(Long postId, Long memberId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new PostNotFoundException("Post not found with id: " + postId));

        // 권한 검사
        if (!post.getMember().getId().equals(memberId)) {
            throw new UnauthorizedException("You don't have permission to update this mentoring post");
        }

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
    public Page<MentoringResponseDto> getApprovedAppliedMentorings(Long memberId, Pageable pageable) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new MemberNotFoundException("Member not found with id: " + memberId));

        Page<Post> approvedAppliedPosts = mentoringApplicationRepository.findApprovedAppliedMentoringsByMemberId(memberId, Status.APPROVED, pageable);
        return approvedAppliedPosts.map(post -> toMentoringResponseDto(post, memberId));
    }

    @Override
    @Transactional(readOnly = true)
    public Page<MentoringResponseDto> getMentoringsByCategory(Category category, Pageable pageable, String keyword, Long currentUserId) {
        Page<Post> posts;
        if (keyword != null && !keyword.trim().isEmpty()) {
            List<String> keywords = Arrays.stream(keyword.split("\\s+"))
                    .filter(k -> !k.isEmpty())
                    .collect(Collectors.toList());
            posts = !keywords.isEmpty()
                    ? postRepository.findMentoringsByCategoryAndKeywords(category, keywords, pageable)
                    : postRepository.findByCategoryAndPriceIsNotNull(category, pageable);
        } else {
            posts = postRepository.findByCategoryAndPriceIsNotNull(category, pageable);
        }
        return posts.map(post -> toMentoringResponseDto(post, currentUserId));
    }

    @Override
    @Transactional
    public Map<Category, Long> getMentoringCountByCategory() {
        // 모든 카테고리에 대해 멘토링 수를 조회
        List<Object[]> results = postRepository.countMentoringsByCategory(List.of(Category.values()));
        Map<Category, Long> categoryCounts = new HashMap<>();

        // 결과를 Category, Count로 변환하여 Map에 저장
        for (Object[] result : results) {
            Category category = (Category) result[0]; // 카테고리 값
            Long count = (Long) result[1];            // 멘토링 수
            categoryCounts.put(category, count);
        }

        return categoryCounts;
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

        Double averageRating = mentoringReviewRepository.getAverageRatingByMentoringId(post.getId());

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
                .averageRating(averageRating != null ? averageRating : 0.0)
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
