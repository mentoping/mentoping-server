package net.kosa.mentopingserver.domain.mentor;

import lombok.RequiredArgsConstructor;
import net.kosa.mentopingserver.domain.mentor.dto.MentorRequestDto;
import net.kosa.mentopingserver.domain.mentor.dto.MentorResponseDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/mentors")
@RequiredArgsConstructor
public class MentorController {

    private final MentorService mentorService;

    // 멘토 생성
    @PostMapping
    public ResponseEntity<MentorResponseDto> createMentor(@RequestBody MentorRequestDto mentorRequestDto) {
        MentorResponseDto mentorResponseDto = mentorService.createMentor(mentorRequestDto);
        return ResponseEntity.ok(mentorResponseDto);
    }

    // 특정 멘토 조회
    @GetMapping("/{memberId}")
    public ResponseEntity<MentorResponseDto> getMentorByMemberId(@PathVariable Long memberId) {
        MentorResponseDto mentorResponseDto = mentorService.getMentorByMemberId(memberId);
        return ResponseEntity.ok(mentorResponseDto);
    }

    // 모든 멘토 목록 조회
    @GetMapping
    public ResponseEntity<List<MentorResponseDto>> getAllMentors() {
        List<MentorResponseDto> mentorResponseDtoList = mentorService.getAllMentors();
        return ResponseEntity.ok(mentorResponseDtoList);
    }

    // 멘토 정보 업데이트
    @PutMapping("/{id}")
    public ResponseEntity<MentorResponseDto> updateMentor(@PathVariable Long id, @RequestBody MentorRequestDto mentorRequestDto) {
        MentorResponseDto updatedMentor = mentorService.updateMentor(id, mentorRequestDto);
        return ResponseEntity.ok(updatedMentor);
    }

    // 멘토 삭제
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMentor(@PathVariable Long id) {
        mentorService.deleteMentor(id);
        return ResponseEntity.noContent().build();
    }



}
