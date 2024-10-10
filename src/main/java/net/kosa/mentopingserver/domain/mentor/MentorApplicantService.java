package net.kosa.mentopingserver.domain.mentor;

import net.kosa.mentopingserver.domain.mentor.dto.MentorRequestDto;
import net.kosa.mentopingserver.domain.mentor.dto.MentorResponseDto;
import net.kosa.mentopingserver.domain.mentor.entity.MentorApplicant;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;

public interface MentorApplicantService {

    @Transactional
    public MentorApplicant createMentorApplication(Long memberId, String field, MultipartFile certificationFile);


}
