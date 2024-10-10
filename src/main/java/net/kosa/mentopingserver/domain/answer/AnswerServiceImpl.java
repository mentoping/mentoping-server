package net.kosa.mentopingserver.domain.answer;

import lombok.RequiredArgsConstructor;
import net.kosa.mentopingserver.domain.member.entity.Member;
import net.kosa.mentopingserver.domain.member.MemberRepository;
import net.kosa.mentopingserver.domain.post.PostRepository;
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
}
