package net.kosa.mentopingserver.domain.mentor;

import lombok.RequiredArgsConstructor;
import net.kosa.mentopingserver.domain.login.CustomOAuth2User;
import net.kosa.mentopingserver.domain.member.MemberService;
import net.kosa.mentopingserver.domain.member.dto.MemberDto;
import net.kosa.mentopingserver.domain.mentor.dto.MentorApplicantRequestDto;
import net.kosa.mentopingserver.domain.mentor.dto.MentorApplicantResponseDto;
import net.kosa.mentopingserver.global.util.S3Service;  // S3Service 추가
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/mentor-applicants")
@RequiredArgsConstructor
public class MentorApplicantController {

    private final MentorApplicantService mentorApplicantService;
    private final MemberService memberService;

    // 멘토 신청 생성
    @PostMapping("/upload")
    public ResponseEntity<?> createMentorApplication(
            @AuthenticationPrincipal CustomOAuth2User customOAuth2User,
            @RequestParam("field") String field,
            @RequestParam("certification_file") MultipartFile certificationFile) throws IOException {

        if (customOAuth2User == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User not authenticated");
        }

        String oauthId = customOAuth2User.getOauthId();
        Optional<MemberDto> memberOptional = memberService.getMemberByOauthId(oauthId);

        if (memberOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Member not found");
        }

        Long memberId = memberOptional.get().getId();

        // 파일이 null이 아닌지 확인
        if (certificationFile == null || certificationFile.isEmpty()) {
            throw new IllegalArgumentException("File must not be null or empty");
        }

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

}
