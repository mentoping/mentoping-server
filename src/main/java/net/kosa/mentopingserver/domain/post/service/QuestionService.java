package net.kosa.mentopingserver.domain.post.service;

import net.kosa.mentopingserver.domain.post.dto.QuestionRequestDto;
import net.kosa.mentopingserver.domain.post.dto.QuestionResponseDto;
import net.kosa.mentopingserver.global.common.enums.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;


public interface QuestionService {

    @Transactional(readOnly = true)
    Page<QuestionResponseDto> getAllQuestions(Pageable pageable, String keyword, Long currentUserId);

    @Transactional
    QuestionResponseDto createQuestion(QuestionRequestDto questionRequestDto, Long memberId);

    @Transactional(readOnly = true)
    QuestionResponseDto getQuestionById(Long postId, Long currentUserId);

    @Transactional
    QuestionResponseDto updateQuestion(Long postId, QuestionRequestDto questionRequestDto, Long memberId);

    @Transactional
    void deleteQuestion(Long postId, Long memberId);

    @Transactional(readOnly = true)
    Page<QuestionResponseDto> getQuestionsByMemberId(Long memberId, Pageable pageable, Long currentUserId);

    @Transactional(readOnly = true)
    public Page<QuestionResponseDto> getQuestionsByCategory(Category category, Pageable pageable, String keyword, Long currentUserId);

    @Transactional
    void markAnswerAsSelected(Long questionId, Long answerId);

    @Transactional
    Map<Category, Long> getQuestionCountByCategory();

    Page<QuestionResponseDto> getAnsweredQuestionsByMemberId(Long memberId, PageRequest pageRequest);
}
