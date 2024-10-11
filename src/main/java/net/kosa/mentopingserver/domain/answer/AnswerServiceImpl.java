package net.kosa.mentopingserver.domain.answer;

import lombok.RequiredArgsConstructor;
import net.kosa.mentopingserver.domain.answer.dto.AnswerRequestDto;
import net.kosa.mentopingserver.domain.answer.dto.AnswerResponseDto;
import net.kosa.mentopingserver.domain.member.dto.AuthorDto;
import net.kosa.mentopingserver.domain.member.entity.Member;
import net.kosa.mentopingserver.domain.member.MemberRepository;
import net.kosa.mentopingserver.domain.post.repository.PostRepository;
import net.kosa.mentopingserver.domain.post.entity.Post;
import net.kosa.mentopingserver.global.exception.AnswerNotFoundException;
import net.kosa.mentopingserver.global.exception.MemberNotFoundException;
import net.kosa.mentopingserver.global.exception.PostNotFoundException;
import net.kosa.mentopingserver.global.exception.UnauthorizedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AnswerServiceImpl implements AnswerService {
    private final AnswerRepository answerRepository;
    private final PostRepository postRepository;
    private final MemberRepository memberRepository;

    @Override
    @Transactional
    public Answer addAnswer(Long postId, String content, Long memberId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new PostNotFoundException("Post not found with id: " + postId));
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new MemberNotFoundException("Member not found with id: " + memberId));

        Answer answer = Answer.builder()
                .content(content)
                .post(post)
                .member(member)
                .isSelected(false)
                .build();

        Answer savedAnswer = answerRepository.save(answer);

        post.incrementAnswerCount();

        return savedAnswer;
    }

    @Override
    @Transactional
    public Answer updateAnswer(Long answerId, String content, Long memberId) {
        Answer answer = answerRepository.findById(answerId)
                .orElseThrow(() -> new AnswerNotFoundException("Answer not found with id: " + answerId));

        if (!answer.getMember().getId().equals(memberId)) {
            throw new UnauthorizedException("You are not authorized to update this answer");
        }

        Answer updatedAnswer = answer.toBuilder()
                .content(content)
                .build();

        return answerRepository.save(updatedAnswer);
    }

    @Override
    @Transactional
    public void removeAnswer(Long answerId, Long memberId) {
        Answer answer = answerRepository.findById(answerId)
                .orElseThrow(() -> new AnswerNotFoundException("Answer not found with id: " + answerId));

        // 답변 작성자 확인
        if (!answer.getMember().getId().equals(memberId)) {
            throw new UnauthorizedException("You are not authorized to delete this answer");
        }

        Post post = answer.getPost();
        if (post == null) {
            throw new IllegalStateException("Answer is not associated with any post");
        }

        post.decrementAnswerCount();

        answerRepository.delete(answer);
    }

    @Override
    @Transactional
    public AnswerResponseDto selectAnswer(Long answerId, AnswerRequestDto requestDto, Long memberId) {
        Answer answer = answerRepository.findById(answerId)
                .orElseThrow(() -> new AnswerNotFoundException("Answer not found with id: " + answerId));

        Post post = answer.getPost();
        if (post == null) {
            throw new IllegalStateException("Answer is not associated with any post");
        }

        // 게시글 작성자만 답변을 선택할 수 있도록 확인
        if (!post.getMember().getId().equals(memberId)) {
            throw new UnauthorizedException("You are not authorized to select an answer for this post");
        }

        // 이미 선택된 답변이 있는지 확인
        if (answerRepository.existsByPostIdAndIsSelectedTrue(post.getId())) {
            throw new IllegalStateException("An answer has already been selected for this post");
        }

        // 답변 선택 및 리뷰 추가
        Answer updatedAnswer = answer.toBuilder()
                .isSelected(true)
                .selectedReview(requestDto.getReview())
                .build();

        Answer savedAnswer = answerRepository.save(updatedAnswer);

        return toAnswerResponseDto(savedAnswer);
    }

    @Override
    public AnswerResponseDto toAnswerResponseDto(Answer answer) {
        return AnswerResponseDto.builder()
                .id(answer.getId())
                .content(answer.getContent())
                .isSelected(answer.getIsSelected())
                .selectedReview(answer.getSelectedReview())
                .author(toAuthorDto(answer.getMember()))
                .postId(answer.getPost().getId())
                .createdAt(answer.getCreatedAt())
                .updatedAt(answer.getUpdatedAt())
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
