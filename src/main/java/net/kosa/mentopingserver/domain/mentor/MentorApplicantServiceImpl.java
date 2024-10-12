package net.kosa.mentopingserver.domain.mentor;

import lombok.RequiredArgsConstructor;
import net.kosa.mentopingserver.global.util.S3Service;  // S3Service 추가
import net.kosa.mentopingserver.domain.member.MemberRepository;
import net.kosa.mentopingserver.domain.member.entity.Member;
import net.kosa.mentopingserver.domain.mentor.dto.MentorApplicantRequestDto;
import net.kosa.mentopingserver.domain.mentor.dto.MentorApplicantResponseDto;
import net.kosa.mentopingserver.domain.mentor.entity.MentorApplicant;
import net.kosa.mentopingserver.global.common.enums.Category;
import net.kosa.mentopingserver.global.common.enums.Status;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MentorApplicantServiceImpl implements MentorApplicantService {

    private final MentorApplicantRepository mentorApplicantRepository;
    private final MemberRepository memberRepository;
    private final S3Service s3Service;  // S3Service DI 추가

    // 멘토 신청 생성
    @Override
    @Transactional
    public MentorApplicantResponseDto createMentorApplication(MentorApplicantRequestDto applicantDto) throws IOException {
        // 멤버를 ID로 찾음
        Member member = memberRepository.findById(applicantDto.getMemberId())
                .orElseThrow(() -> new IllegalArgumentException("Member not found with id: " + applicantDto.getMemberId()));

        // S3에 파일 업로드
        MultipartFile certificationFile = applicantDto.getCertification_file();
        String uploadedFileUrl = s3Service.uploadFile(certificationFile);  // S3에 파일 업로드 후 URL 반환

        // MentorApplicant 엔터티 생성
        MentorApplicant mentorApplicant = MentorApplicant.builder()
                .member(member)
                .category(Category.valueOf(applicantDto.getField())) // 전문 분야를 Category로 설정
                .file(uploadedFileUrl) // S3 파일 URL 저장
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

    // 특정 ID의 멘토 신청한 멤버 정보 가져오기
    @Override
    @Transactional(readOnly = true)
    public Optional<MentorApplicantResponseDto> getMentorApplicationById(Long memberId) {
        // memberId를 사용하여 MentorApplicant 정보 조회
        return mentorApplicantRepository.findByMember_Id(memberId)
                .map(this::toDto);  // 조회된 MentorApplicant를 MentorApplicantResponseDto로 변환
    }

    @Override
    @Transactional
    public MentorApplicantResponseDto updateMentorApplication(Long id, MentorApplicantRequestDto applicantDto) throws IOException {
        // ID로 멘토 신청 엔터티 조회
        MentorApplicant mentorApplicant = mentorApplicantRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Mentor application not found with id: " + id));

        // S3에 파일 업로드 및 URL 갱신
        MultipartFile certificationFile = applicantDto.getCertification_file();
        String uploadedFileUrl = s3Service.uploadFile(certificationFile);  // S3에 파일 업로드 후 URL 반환

        // 멘토 신청 정보 업데이트
        mentorApplicant = mentorApplicant.toBuilder()
                .category(Category.valueOf(applicantDto.getField())) // 전문 분야 업데이트
                .file(uploadedFileUrl) // S3 파일 URL 업데이트
                .status(Status.valueOf(applicantDto.getStatus())) // 상태 업데이트
                .review(applicantDto.getReview()) // 리뷰 업데이트
                .reviewedAt(LocalDateTime.now()) // 리뷰 작성 시간 업데이트
                .build();

        // 수정된 엔터티를 저장하고, DTO로 변환해서 반환
        return toDto(mentorApplicantRepository.save(mentorApplicant));
    }

    @Override
    @Transactional
    public void deleteMentorApplication(Long id) {
        // 멘토 신청 ID로 해당 엔터티가 존재하는지 확인
        MentorApplicant mentorApplicant = mentorApplicantRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Mentor application not found with id: " + id));

        // 데이터베이스에서 멘토 신청 삭제
        mentorApplicantRepository.delete(mentorApplicant);
    }

    private MentorApplicantResponseDto toDto(MentorApplicant mentorApplicant) {
        return MentorApplicantResponseDto.builder()
                .applicationId(mentorApplicant.getId().toString()) // 엔터티의 ID 사용
                .status(mentorApplicant.getStatus().name()) // 신청 상태 변환
                .submittedAt(mentorApplicant.getSubmittedAt().toString()) // 제출 날짜 변환
                .build();
    }
}
