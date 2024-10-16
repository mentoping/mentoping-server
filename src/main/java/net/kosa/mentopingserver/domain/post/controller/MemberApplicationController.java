package net.kosa.mentopingserver.domain.post.controller;

import lombok.RequiredArgsConstructor;
import net.kosa.mentopingserver.domain.post.dto.MentoringApplicationResponseDto;
import net.kosa.mentopingserver.domain.post.service.MentoringApplicationService;
import net.kosa.mentopingserver.global.config.CurrentUser;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/members/{memberId}/applications")
public class MemberApplicationController {

    private final MentoringApplicationService mentoringApplicationService;

    @GetMapping
    public ResponseEntity<Page<MentoringApplicationResponseDto>> getMyApplications(
            @CurrentUser Long memberId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt") String sort,
            @RequestParam(defaultValue = "desc") String direction) {

        PageRequest pageRequest = PageRequest.of(page, size, Sort.by(Sort.Direction.fromString(direction), sort));
        Page<MentoringApplicationResponseDto> myApplications =
                mentoringApplicationService.getApplicationsByMemberId(memberId, pageRequest);

        return ResponseEntity.ok(myApplications);
    }
}
