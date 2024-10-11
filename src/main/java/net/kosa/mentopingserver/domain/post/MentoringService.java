package net.kosa.mentopingserver.domain.post;

import net.kosa.mentopingserver.domain.post.dto.MentoringRequestDto;
import net.kosa.mentopingserver.domain.post.dto.MentoringResponseDto;
import net.kosa.mentopingserver.domain.post.dto.QuestionResponseDto;
import net.kosa.mentopingserver.global.common.enums.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

public interface MentoringService {

    @Transactional(readOnly = true)
    Page<MentoringResponseDto> getAllMentorings(Pageable pageable, String keyword);

    @Transactional
    MentoringResponseDto createMentoring(MentoringRequestDto mentoringRequestDto, Long memberId);

    @Transactional(readOnly = true)
    MentoringResponseDto getMentoringById(Long mentoringId);

    @Transactional
    MentoringResponseDto updateMentoring(Long mentoringId, MentoringRequestDto mentoringRequestDto);

    @Transactional
    void deleteMentoring(Long mentoringId);

    @Transactional(readOnly = true)
    Page<MentoringResponseDto> getMentoringsByMemberId(Long memberId, Pageable pageable);

    @Transactional(readOnly = true)
    Page<MentoringResponseDto> getMentoringsByCategory(Category category, Pageable pageable, String keyword);
}
