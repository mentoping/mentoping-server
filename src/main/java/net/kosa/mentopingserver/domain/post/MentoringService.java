package net.kosa.mentopingserver.domain.post;

import net.kosa.mentopingserver.domain.post.dto.MentoringRequestDto;
import net.kosa.mentopingserver.domain.post.dto.MentoringResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

public interface MentoringService {

    @Transactional(readOnly = true)
    Page<MentoringResponseDto> getAllMentoring(Pageable pageable);

    @Transactional
    MentoringResponseDto createMentoring(MentoringRequestDto mentoringRequestDto, Long memberId);

    @Transactional(readOnly = true)
    MentoringResponseDto getMentoringById(Long mentoringId);

    @Transactional
    MentoringResponseDto updateMentoring(Long mentoringId, MentoringRequestDto mentoringRequestDto);

    @Transactional
    void deleteMentoring(Long mentoringId);

}
