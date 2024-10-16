package net.kosa.mentopingserver.domain.mentor;

import net.kosa.mentopingserver.domain.mentor.dto.MentorApplicantRequestDto;
import net.kosa.mentopingserver.domain.mentor.dto.MentorApplicantResponseDto;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

public interface MentorApplicantService {
    @Transactional
    MentorApplicantResponseDto createMentorApplication(MentorApplicantRequestDto applicantDto) throws IOException;

    @Transactional(readOnly = true)
    List<MentorApplicantResponseDto> getAllMentorApplications();

    @Transactional(readOnly = true)
    Optional<MentorApplicantResponseDto> getMentorApplicationById(Long id);

    @Transactional
    MentorApplicantResponseDto updateMentorApplication(Long id, MentorApplicantRequestDto applicantDto) throws IOException;

    @Transactional
    void deleteMentorApplication(Long id);
}

