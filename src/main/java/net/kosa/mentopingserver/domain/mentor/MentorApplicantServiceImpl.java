package net.kosa.mentopingserver.domain.mentor;

import lombok.RequiredArgsConstructor;
import net.kosa.mentopingserver.domain.member.MemberRepository;
import net.kosa.mentopingserver.domain.member.entity.Member;
import net.kosa.mentopingserver.domain.mentor.dto.MentorApplicantRequestDto;
import net.kosa.mentopingserver.domain.mentor.dto.MentorApplicantResponseDto;
import net.kosa.mentopingserver.domain.mentor.entity.MentorApplicant;
import net.kosa.mentopingserver.global.common.enums.Category;
import net.kosa.mentopingserver.global.common.enums.Status;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MentorApplicantServiceImpl implements MentorApplicantService {

    private final MentorApplicantRepository mentorApplicantRepository;
    private final MemberRepository memberRepository;

    // 멘토 신청 생성
    @Override
    @Transactional
    public MentorApplicantResponseDto createMentorApplication(MentorApplicantRequestDto applicantDto) {
        // 멤버를 ID로 찾음
        Member member = memberRepository.findById(applicantDto.getMemberId())
                .orElseThrow(() -> new IllegalArgumentException("Member not found with id: " + applicantDto.getMemberId()));

        // MentorApplicant 엔터티 생성
        MentorApplicant mentorApplicant = MentorApplicant.builder()
                .member(member)
                .category(Category.valueOf(applicantDto.getField())) // 전문 분야를 Category로 설정
                .file(applicantDto.getCertification_file().getName()) // 파일 이름 저장 (파일 처리 로직이 필요함)
                .status(Status.PENDING) // 신청 상태는 기본적으로 "PENDING"으로 설정
                .submittedAt(LocalDateTime.now()) // 신청 제출 시간 설정
                .build();

        // 생성된 엔터티를 데이터베이스에 저장하고, 이를 DTO로 변환해서 반환
        return toDto(mentorApplicantRepository.save(mentorApplicant));
    }


    // 모든 멘토 신청한 멤버 정보 가져오기
    @Override
    @Transactional(readOnly = true)
    public List<MentorApplicantResponseDto> getAllMentorApplications() {
        return mentorApplicantRepository.findAll().stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    // 특정 ID 의 멘토 신청한 멤버 정보 가져오기
    @Override
    @Transactional(readOnly = true)
    public Optional<MentorApplicantResponseDto> getMentorApplicationById(Long id) {
        return mentorApplicantRepository.findById(id)
                .map(this::toDto);
    }

    private MentorApplicantResponseDto toDto(MentorApplicant mentorApplicant) {
        return MentorApplicantResponseDto.builder()
                .applicationId(mentorApplicant.getId().toString()) // 엔터티의 ID 사용
                .status(mentorApplicant.getStatus().name()) // 신청 상태 변환
                .submittedAt(mentorApplicant.getSubmittedAt().toString()) // 제출 날짜 변환
                .build();
    }
}