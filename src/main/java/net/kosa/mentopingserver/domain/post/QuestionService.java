package net.kosa.mentopingserver.domain.post;

import net.kosa.mentopingserver.domain.post.dto.QuestionRequestDto;
import net.kosa.mentopingserver.domain.post.dto.QuestionResponseDto;
import net.kosa.mentopingserver.global.common.enums.SubCategory;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface QuestionService {

    @Transactional
    QuestionResponseDto createQuestion(QuestionRequestDto questionRequestDto, Long memberId);

    @Transactional(readOnly = true)  // 조회만 할 때 readOnly 옵션 적용
    QuestionResponseDto getQuestionById(Long postId);
}
