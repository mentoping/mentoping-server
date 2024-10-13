package net.kosa.mentopingserver.domain.post.service;

import net.kosa.mentopingserver.domain.post.dto.MentoringApplicationRequestDto;
import net.kosa.mentopingserver.domain.post.dto.MentoringApplicationResponseDto;
import net.kosa.mentopingserver.global.common.enums.Status;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

public interface MentoringApplicationService {
    MentoringApplicationResponseDto applyForMentoring(Long mentoringId, MentoringApplicationRequestDto applicationDto, Long memberId);

    Page<MentoringApplicationResponseDto> getApplicationsForMentoring(Long mentoringId, PageRequest pageRequest);

    MentoringApplicationResponseDto getApplicationById(Long mentoringId, Long applicationId);

    MentoringApplicationResponseDto updateApplication(Long mentoringId, Long applicationId, MentoringApplicationRequestDto applicationDto);

    MentoringApplicationResponseDto updateApplicationStatus(Long applicationId, Status newStatus);

    void deleteApplication(Long mentoringId, Long applicationId);

    Page<MentoringApplicationResponseDto> getApplicationsByMemberId(Long memberId, PageRequest pageRequest);

}
