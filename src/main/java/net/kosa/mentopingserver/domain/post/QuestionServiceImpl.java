package net.kosa.mentopingserver.domain.post;

import lombok.RequiredArgsConstructor;
import net.kosa.mentopingserver.domain.hashtag.PostHashtagService;
import net.kosa.mentopingserver.domain.mentor.dto.AuthorDto;
import net.kosa.mentopingserver.domain.post.dto.QuestionRequestDto;
import net.kosa.mentopingserver.domain.post.dto.QuestionResponseDto;
import net.kosa.mentopingserver.domain.post.entity.Post;
import net.kosa.mentopingserver.domain.member.Member;
import net.kosa.mentopingserver.domain.member.MemberRepository;
import net.kosa.mentopingserver.global.exception.MemberNotFoundException;
import net.kosa.mentopingserver.global.exception.PostNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
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

    private QuestionResponseDto toQuestionResponseDto(Post post) {
        return QuestionResponseDto.builder()
                .id(post.getId())
                .title(post.getTitle())
                .content(post.getContent())
                .author(toAuthorDto(post.getMember()))
                .createdAt(post.getCreatedAt())
                .category(post.getCategory().getName())
                .hashtags(Optional.ofNullable(post.getPostHashtags())
                        .orElseGet(ArrayList::new)  // postHashtags가 null일 경우 빈 리스트로 처리
                        .stream()
                        .map(postHashtag -> postHashtag.getHashtag().getName())
                        .collect(Collectors.toList()))
                .likeCount(postLikesRepository.countByPost_Id(post.getId()))
                .answerCount(Optional.ofNullable(post.getAnswers())  // null 방지
                        .orElseGet(ArrayList::new)
                        .size())  // size()를 안전하게 호출
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