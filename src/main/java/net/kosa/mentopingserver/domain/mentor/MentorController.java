package net.kosa.mentopingserver.domain.mentor;

import lombok.RequiredArgsConstructor;
import net.kosa.mentopingserver.domain.mentor.dto.MentorApplicantRequestDto;
import net.kosa.mentopingserver.domain.mentor.dto.MentorApplicantResponseDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/mentor-applicants")
@RequiredArgsConstructor
public class MentorController {

    private final MentorApplicantService mentorApplicantService;

    // 멘토 신청 생성
    @PostMapping
    public ResponseEntity<MentorApplicantResponseDto> createMentorApplication(@RequestBody MentorApplicantRequestDto requestDto) {
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
