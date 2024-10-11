package net.kosa.mentopingserver.domain.post;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import net.kosa.mentopingserver.domain.post.dto.MentoringRequestDto;
import net.kosa.mentopingserver.domain.post.dto.MentoringResponseDto;
import net.kosa.mentopingserver.global.common.enums.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;

@RestController
@RequiredArgsConstructor
@RequestMapping("/mentorings")
public class MentoringController {

    private final MentoringService mentoringService;

    @GetMapping
    public ResponseEntity<Page<MentoringResponseDto>> getAllMentorings(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size,
            @RequestParam(defaultValue = "createdAt") String sort,
            @RequestParam(defaultValue = "desc") String direction,
            @RequestParam(required = false) String keyword) {

        try {
            PageRequest pageRequest = createPageRequest(page, size, sort, direction);
            String decodedKeyword = decodeKeyword(keyword);
            Page<MentoringResponseDto> mentorings = mentoringService.getAllMentorings(pageRequest, decodedKeyword);
            return ResponseEntity.ok(mentorings);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping
    public ResponseEntity<MentoringResponseDto> createMentoring(@Valid @RequestBody MentoringRequestDto mentoringRequestDto,
                                                                @RequestParam Long memberId) {
        MentoringResponseDto responseDto = mentoringService.createMentoring(mentoringRequestDto, memberId);
        return ResponseEntity.status(HttpStatus.CREATED).body(responseDto);
    }

    @GetMapping("/{postId}")
    public ResponseEntity<MentoringResponseDto> getMentoringById(@PathVariable Long postId) {
        MentoringResponseDto responseDto = mentoringService.getMentoringById(postId);
        return ResponseEntity.ok(responseDto);
    }

    @PutMapping("/{postId}")
    public ResponseEntity<MentoringResponseDto> updateMentoring(@PathVariable Long postId,
                                                                @Valid @RequestBody MentoringRequestDto mentoringRequestDto) {
        MentoringResponseDto responseDto = mentoringService.updateMentoring(postId, mentoringRequestDto);
        return ResponseEntity.ok(responseDto);
    }

    @DeleteMapping("/{postId}")
    public ResponseEntity<Void> deleteMentoring(@PathVariable Long postId) {
        mentoringService.deleteMentoring(postId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/category/{category}")
    public ResponseEntity<Page<MentoringResponseDto>> getMentoringsByCategory(
            @PathVariable Category category,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size,
            @RequestParam(defaultValue = "createdAt") String sort,
            @RequestParam(defaultValue = "desc") String direction,
            @RequestParam(required = false) String keyword) {

        try {
            PageRequest pageRequest = createPageRequest(page, size, sort, direction);
            String decodedKeyword = decodeKeyword(keyword);
            Page<MentoringResponseDto> mentorings = mentoringService.getMentoringsByCategory(category, pageRequest, decodedKeyword);
            return ResponseEntity.ok(mentorings);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    private PageRequest createPageRequest(int page, int size, String sort, String direction) {
        validateSortCriteria(sort);
        Sort.Direction sortDirection = Sort.Direction.fromString(direction);
        return PageRequest.of(page, size, Sort.by(sortDirection, sort));
    }

    private void validateSortCriteria(String sort) {
        if (!sort.equals("createdAt") && !sort.equals("likeCount")) {
            throw new IllegalArgumentException("Sort must be either 'createdAt' or 'likeCount'");
        }
    }

    private String decodeKeyword(String keyword) {
        if (keyword == null) {
            return null;
        }
        return URLDecoder.decode(keyword, StandardCharsets.UTF_8);
    }

}
