package net.kosa.mentopingserver.domain.mentor;

import lombok.RequiredArgsConstructor;
import net.kosa.mentopingserver.domain.member.MemberRepository;
import net.kosa.mentopingserver.domain.member.entity.Member;
import net.kosa.mentopingserver.domain.mentor.dto.MentorRequestDto;
import net.kosa.mentopingserver.domain.mentor.dto.MentorResponseDto;
import net.kosa.mentopingserver.domain.mentor.entity.Mentor;
import net.kosa.mentopingserver.domain.mentor.entity.MentorApplicant;
import net.kosa.mentopingserver.global.common.enums.Category;
import net.kosa.mentopingserver.global.common.enums.Status;
import net.kosa.mentopingserver.global.exception.MemberNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MentorApplicantServiceImpl implements MentorApplicantService {

    private final MentorApplicantRepository mentorApplicantRepository;
    private final MemberRepository mentorRepository;

    @Override
    @Transactional
    public MentorApplicant createMentorApplication(Long memberId, String field, MultipartFile certificationFile) {
        Member member = mentorRepository.findById(memberId)
                .orElseThrow(() -> new MemberNotFoundException("Member not found with id: " + memberId));

        MentorApplicant mentorApplicant = MentorApplicant.builder()
                .member(member)
                .category(Category.valueOf(field))
                .file(certificationFile.getOriginalFilename()) // File handling logic should be added here
                .status(Status.PENDING)
                .submittedAt(LocalDateTime.now())
                .build();

        return mentorApplicantRepository.save(mentorApplicant);
    }


}
