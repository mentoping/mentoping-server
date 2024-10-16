package net.kosa.mentopingserver.domain.answer;

import net.kosa.mentopingserver.domain.answer.dto.AnswerRequestDto;
import net.kosa.mentopingserver.domain.answer.dto.AnswerResponseDto;
import org.springframework.transaction.annotation.Transactional;

public interface AnswerService {
    @Transactional
    Answer addAnswer(Long postId, String content, Long memberId);

    @Transactional
    Answer updateAnswer(Long answerId, String content, Long memberId);

    @Transactional
    void removeAnswer(Long answerId, Long memberId);

    @Transactional
    AnswerResponseDto selectAnswer(Long answerId, AnswerRequestDto requestDto, Long memberId);

    AnswerResponseDto toAnswerResponseDto(Answer answer);

}
