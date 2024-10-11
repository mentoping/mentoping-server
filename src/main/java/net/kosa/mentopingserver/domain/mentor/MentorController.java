package net.kosa.mentopingserver.domain.mentor;

import lombok.RequiredArgsConstructor;
import net.kosa.mentopingserver.domain.mentor.dto.MentorApplicantRequestDto;
import net.kosa.mentopingserver.domain.mentor.dto.MentorApplicantResponseDto;
import net.kosa.mentopingserver.S3Service;  // S3Service 추가
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/mentor-applicants")
@RequiredArgsConstructor
public class MentorController {

    private final MentorApplicantService mentorApplicantService;
    private final S3Service s3Service;  // S3Service DI 추가

    // 멘토 신청 생성
    @PostMapping("/upload")
    public ResponseEntity<MentorApplicantResponseDto> createMentorApplication(
            @RequestParam("memberId") Long memberId,
            @RequestParam("field") String field,
            @RequestParam("certification_file") MultipartFile certificationFile) throws IOException {

        // S3에 파일 업로드
        String fileUrl = s3Service.uploadFile(certificationFile);

        MentorApplicantRequestDto requestDto = MentorApplicantRequestDto.builder()
                .memberId(memberId)
                .field(field)
                .certification_file(certificationFile)  // 파일을 함께 전달
                .build();

        MentorApplicantResponseDto responseDto = mentorApplicantService.createMentorApplication(requestDto);
        return ResponseEntity.ok(responseDto);
    }

    // 멘토 신청한 모든 멤버 정보 불러오기
    @GetMapping
    public ResponseEntity<List<MentorApplicantResponseDto>> getAllMentorApplications() {
        List<MentorApplicantResponseDto> responseDtos = mentorApplicantService.getAllMentorApplications();
        return ResponseEntity.ok(responseDtos);
    }

    // 멘토 신청한 특정 멤버 정보 불러오기
    @GetMapping("/{id}")
    public ResponseEntity<MentorApplicantResponseDto> getMentorApplicationById(@PathVariable Long id) {
        Optional<MentorApplicantResponseDto> responseDto = mentorApplicantService.getMentorApplicationById(id);
        return responseDto.map(ResponseEntity::ok)
                .orElseThrow(() -> new IllegalArgumentException("Mentor application not found with id: " + id));
    }

    // 멘토 신청 업데이트
    @PutMapping("/{id}")
    public ResponseEntity<MentorApplicantResponseDto> updateMentorApplication(
            @PathVariable Long id,
            @RequestParam("field") String field,
            @RequestParam("certification_file") MultipartFile certificationFile,
            @RequestParam("status") String status,
            @RequestParam("review") String review) throws IOException {

        // S3에 파일 업로드
        String fileUrl = s3Service.uploadFile(certificationFile);

        MentorApplicantRequestDto requestDto = MentorApplicantRequestDto.builder()
                .field(field)
                .certification_file(certificationFile)  // 파일을 함께 전달
                .status(status)
                .review(review)
                .build();

        MentorApplicantResponseDto responseDto = mentorApplicantService.updateMentorApplication(id, requestDto);
        return ResponseEntity.ok(responseDto);
    }

    // 멘토 신청 삭제
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMentorApplication(@PathVariable Long id) {
        mentorApplicantService.deleteMentorApplication(id);
        return ResponseEntity.noContent().build();
    }
}
