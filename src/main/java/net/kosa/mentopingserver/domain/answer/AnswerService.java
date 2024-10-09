package net.kosa.mentopingserver.domain.answer;

import org.springframework.transaction.annotation.Transactional;

public interface AnswerService {
    @Transactional
    Answer addAnswer(Long postId, String content, Long memberId);

    @Transactional
    void removeAnswer(Long answerId, Long memberId);
}
