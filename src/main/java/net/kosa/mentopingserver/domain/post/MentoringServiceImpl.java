package net.kosa.mentopingserver.domain.post;

import lombok.RequiredArgsConstructor;
import net.kosa.mentopingserver.domain.hashtag.PostHashtagService;
import net.kosa.mentopingserver.domain.mentor.dto.AuthorDto;
import net.kosa.mentopingserver.domain.post.dto.MentoringRequestDto;
import net.kosa.mentopingserver.domain.post.dto.MentoringResponseDto;
import net.kosa.mentopingserver.domain.post.entity.Post;
import net.kosa.mentopingserver.domain.member.entity.Member;
import net.kosa.mentopingserver.domain.member.MemberRepository;
import net.kosa.mentopingserver.global.exception.MemberNotFoundException;
import net.kosa.mentopingserver.global.exception.PostNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MentoringServiceImpl implements MentoringService {

    private final PostRepository postRepository;
    private final MemberRepository memberRepository;
    private final PostHashtagService postHashtagService;

    @Override
    @Transactional(readOnly = true)
    public Page<MentoringResponseDto> getAllMentoring(Pageable pageable) {
        Page<Post> posts = postRepository.findAll(pageable);
        return posts.map(this::toMentoringResponseDto);
    }

    @Override
    @Transactional
    public MentoringResponseDto createMentoring(MentoringRequestDto mentoringRequestDto, Long memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new MemberNotFoundException("Member not found with id: " + memberId));

        Long price = mentoringRequestDto.getPrice() != null ? mentoringRequestDto.getPrice() : 0L;

        Post post = Post.builder()
                .title(mentoringRequestDto.getTitle())
                .content(mentoringRequestDto.getContent())
                .member(member)
                .category(mentoringRequestDto.getCategory())
                .price(price)
                .build();

        Post savedPost = postRepository.save(post);

        if (mentoringRequestDto.getHashtags() != null && !mentoringRequestDto.getHashtags().isEmpty()) {
            postHashtagService.setHashtag(savedPost, mentoringRequestDto.getHashtags());
        }

        return toMentoringResponseDto(savedPost);
    }

    @Override
    @Transactional(readOnly = true)
    public MentoringResponseDto getMentoringById(Long mentoringId) {
        Post post = postRepository.findById(mentoringId)
                .orElseThrow(() -> new PostNotFoundException("Mentoring not found with id: " + mentoringId));
        return toMentoringResponseDto(post);
    }

    @Override
    @Transactional
    public MentoringResponseDto updateMentoring(Long mentoringId, MentoringRequestDto mentoringRequestDto) {
        Post post = postRepository.findById(mentoringId)
                .orElseThrow(() -> new PostNotFoundException("Mentoring not found with id: " + mentoringId));

        Long price = mentoringRequestDto.getPrice() != null ? mentoringRequestDto.getPrice() : 0L;

        post = post.toBuilder()
                .title(mentoringRequestDto.getTitle())
                .content(mentoringRequestDto.getContent())
                .category(mentoringRequestDto.getCategory())
                .price(price)
                .build();

        Post updatedPost = postRepository.save(post);

        if (mentoringRequestDto.getHashtags() != null) {
            postHashtagService.setHashtag(updatedPost, mentoringRequestDto.getHashtags());
        }

        return toMentoringResponseDto(updatedPost);
    }

    @Override
    @Transactional
    public void deleteMentoring(Long mentoringId) {
        Post post = postRepository.findById(mentoringId)
                .orElseThrow(() -> new PostNotFoundException("Mentoring not found with id: " + mentoringId));
        postRepository.delete(post);
    }

    private MentoringResponseDto toMentoringResponseDto(Post post) {
        List<String> hashtags = post.getPostHashtags().stream()
                .map(postHashtag -> postHashtag.getHashtag().getName())
                .distinct()
                .collect(Collectors.toList());

        return MentoringResponseDto.builder()
                .id(post.getId())
                .title(post.getTitle())
                .content(post.getContent())
                .thumbnailUrl(null) // Thumbnail URL is not available in Post, set to null or add logic if needed
                .author(toAuthorDto(post.getMember()))
                .createdAt(post.getCreatedAt())
                .category(post.getCategory().getName())
                .hashtags(hashtags)
                .likeCount(post.getLikeCount())
                .isApplicant(false) // Logic for determining if the user is an applicant can be added here
                .price(post.getPrice() != null ? post.getPrice().intValue() : 0) // Convert Long to int for DTO
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
