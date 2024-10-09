package net.kosa.mentopingserver.domain.post;

import net.kosa.mentopingserver.domain.post.dto.QuestionRequestDto;
import net.kosa.mentopingserver.domain.post.dto.QuestionResponseDto;
import net.kosa.mentopingserver.global.common.enums.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;


public interface QuestionService {

    @Transactional(readOnly = true)
    Page<QuestionResponseDto> getAllQuestions(Pageable pageable, String keyword);

    @Transactional
    QuestionResponseDto createQuestion(QuestionRequestDto questionRequestDto, Long memberId);

    @Transactional(readOnly = true)
    QuestionResponseDto getQuestionById(Long postId);

    @Transactional
    QuestionResponseDto updateQuestion(Long postId, QuestionRequestDto questionRequestDto);

    @Transactional
    void deleteQuestion(Long postId);

    @Transactional(readOnly = true)
    Page<QuestionResponseDto> getQuestionsByMemberId(Long memberId, Pageable pageable);

    @Transactional(readOnly = true)
    public Page<QuestionResponseDto> getQuestionsByCategory(Category category, Pageable pageable, String keyword);

    @Transactional
    void markAnswerAsSelected(Long questionId, Long answerId);
}
