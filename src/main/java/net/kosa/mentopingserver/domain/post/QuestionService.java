package net.kosa.mentopingserver.domain.post;

import net.kosa.mentopingserver.domain.post.dto.QuestionRequestDto;
import net.kosa.mentopingserver.domain.post.dto.QuestionResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;


public interface QuestionService {

    @Transactional(readOnly = true)
    Page<QuestionResponseDto> getAllQuestions(Pageable pageable);

    @Transactional
    QuestionResponseDto createQuestion(QuestionRequestDto questionRequestDto, Long memberId);

    @Transactional(readOnly = true)  // 조회만 할 때 readOnly 옵션 적용
    QuestionResponseDto getQuestionById(Long postId);
}
