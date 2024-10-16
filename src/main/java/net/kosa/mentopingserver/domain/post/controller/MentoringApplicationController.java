package net.kosa.mentopingserver.domain.post.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import net.kosa.mentopingserver.domain.login.CustomOAuth2User;
import net.kosa.mentopingserver.domain.member.MemberService;
import net.kosa.mentopingserver.domain.member.dto.MemberDto;
import net.kosa.mentopingserver.domain.post.dto.MentoringApplicationRequestDto;
import net.kosa.mentopingserver.domain.post.dto.MentoringApplicationResponseDto;
import net.kosa.mentopingserver.domain.post.service.MentoringApplicationService;
import net.kosa.mentopingserver.global.common.enums.Status;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/mentorings/{mentoringId}/applications")
public class MentoringApplicationController {

    private final MentoringApplicationService mentoringApplicationService;
    private final MemberService memberService;

    @PostMapping
    public ResponseEntity<?> applyForMentoring(
            @PathVariable Long mentoringId,
            @Valid @RequestBody MentoringApplicationRequestDto applicationDto,
            @AuthenticationPrincipal CustomOAuth2User customOAuth2User) {

        if (customOAuth2User == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User not authenticated");
        }

        String oauthId = customOAuth2User.getOauthId();
        Optional<MemberDto> memberOptional = memberService.getMemberByOauthId(oauthId);

        if (memberOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Member not found");
        }

        Long memberId = memberOptional.get().getId();        MentoringApplicationResponseDto responseDto = mentoringApplicationService.applyForMentoring(mentoringId, applicationDto, memberId);
        return ResponseEntity.status(HttpStatus.CREATED).body(responseDto);
    }

    @GetMapping
    public ResponseEntity<Page<MentoringApplicationResponseDto>> getApplicationsForMentoring(
            @PathVariable Long mentoringId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        PageRequest pageRequest = PageRequest.of(page, size);
        Page<MentoringApplicationResponseDto> applications = mentoringApplicationService.getApplicationsForMentoring(mentoringId, pageRequest);
        return ResponseEntity.ok(applications);
    }

    @GetMapping("/{applicationId}")
    public ResponseEntity<MentoringApplicationResponseDto> getApplicationById(
            @PathVariable Long mentoringId,
            @PathVariable Long applicationId) {
        MentoringApplicationResponseDto responseDto = mentoringApplicationService.getApplicationById(mentoringId, applicationId);
        return ResponseEntity.ok(responseDto);
    }

    @PutMapping("/{applicationId}")
    public ResponseEntity<MentoringApplicationResponseDto> updateApplication(
            @PathVariable Long mentoringId,
            @PathVariable Long applicationId,
            @Valid @RequestBody MentoringApplicationRequestDto applicationDto) {
        MentoringApplicationResponseDto responseDto = mentoringApplicationService.updateApplication(mentoringId, applicationId, applicationDto);
        return ResponseEntity.ok(responseDto);
    }

    @PatchMapping("/{applicationId}/status")
    public ResponseEntity<MentoringApplicationResponseDto> updateApplicationStatus(
//            @PathVariable Long mentoringId,
            @PathVariable Long applicationId,
            @RequestParam(name = "newstatus") Status newStatus) {
        MentoringApplicationResponseDto responseDto = mentoringApplicationService.updateApplicationStatus(applicationId, newStatus);
        return ResponseEntity.ok(responseDto);
    }

    @DeleteMapping("/{applicationId}")
    public ResponseEntity<Void> deleteApplication(
            @PathVariable Long mentoringId,
            @PathVariable Long applicationId) {
        mentoringApplicationService.deleteApplication(mentoringId, applicationId);
        return ResponseEntity.noContent().build();
    }

}
