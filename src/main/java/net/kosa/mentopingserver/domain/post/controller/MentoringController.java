package net.kosa.mentopingserver.domain.post.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import net.kosa.mentopingserver.domain.login.CustomOAuth2User;
import net.kosa.mentopingserver.domain.member.MemberService;
import net.kosa.mentopingserver.domain.member.dto.MemberDto;
import net.kosa.mentopingserver.domain.post.dto.MentoringRequestDto;
import net.kosa.mentopingserver.domain.post.dto.MentoringResponseDto;
import net.kosa.mentopingserver.domain.post.service.MentoringService;
import net.kosa.mentopingserver.global.common.enums.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/mentorings")
public class MentoringController {

    private final MentoringService mentoringService;
    private final MemberService memberService;

    @GetMapping
    public ResponseEntity<?> getAllMentorings(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "8") int size,
            @RequestParam(defaultValue = "createdAt") String sort,
            @RequestParam(defaultValue = "desc") String direction,
            @RequestParam(required = false) String keyword,
            @AuthenticationPrincipal CustomOAuth2User customOAuth2User) {

        if (customOAuth2User == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User not authenticated");
        }

        String oauthId = customOAuth2User.getOauthId();
        Optional<MemberDto> memberOptional = memberService.getMemberByOauthId(oauthId);

        if (memberOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Member not found");
        }

        Long memberId = memberOptional.get().getId();
        try {
            PageRequest pageRequest = createPageRequest(page, size, sort, direction);
            String decodedKeyword = decodeKeyword(keyword);
            Page<MentoringResponseDto> mentorings = mentoringService.getAllMentorings(pageRequest, decodedKeyword, memberId);
            return ResponseEntity.ok(mentorings);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

//    @PostMapping
//    public ResponseEntity<?> createMentoring(@Valid @RequestBody MentoringRequestDto mentoringRequestDto,
//                                             @AuthenticationPrincipal CustomOAuth2User customOAuth2User) throws IOException {
//
//        if (customOAuth2User == null) {
//            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User not authenticated");
//        }
//
//        String oauthId = customOAuth2User.getOauthId();
//        Optional<MemberDto> memberOptional = memberService.getMemberByOauthId(oauthId);
//
//        if (memberOptional.isEmpty()) {
//            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Member not found");
//        }
//
//        Long memberId = memberOptional.get().getId();
//        MentoringResponseDto responseDto = mentoringService.createMentoring(mentoringRequestDto, memberId);
//        return ResponseEntity.status(HttpStatus.CREATED).body(responseDto);
//    }


    @PostMapping
    public ResponseEntity<?> createMentoring(
            @RequestParam("title") String title,
            @RequestParam("content") String content,
            @RequestParam("category") Category category,
            @RequestParam("price") Long price,
            @RequestParam("summary") String summary,
            @RequestParam(value = "thumbnailFile", required = false) MultipartFile thumbnailFile,
            @RequestParam(value = "hashtags", required = false) List<String> hashtags,
            @AuthenticationPrincipal CustomOAuth2User customOAuth2User
    ) throws IOException {

        if (customOAuth2User == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User not authenticated");
        }

        String oauthId = customOAuth2User.getOauthId();
        Optional<MemberDto> memberOptional = memberService.getMemberByOauthId(oauthId);

        if (memberOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Member not found");
        }

        Long memberId = memberOptional.get().getId();

        // MentoringRequestDto 생성
        MentoringRequestDto mentoringRequestDto = MentoringRequestDto.builder()
                .title(title)
                .content(content)
                .category(category)
                .price(price)
                .summary(summary)
                .thumbnailUrl(thumbnailFile)
                .hashtags(hashtags)
                .build();

        MentoringResponseDto responseDto = mentoringService.createMentoring(mentoringRequestDto, memberId);
        return ResponseEntity.status(HttpStatus.CREATED).body(responseDto);
    }


    @GetMapping("/{postId}")
    public ResponseEntity<?> getMentoringById(@PathVariable Long postId,
                                              @AuthenticationPrincipal CustomOAuth2User customOAuth2User) {

        if (customOAuth2User == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User not authenticated");
        }

        String oauthId = customOAuth2User.getOauthId();
        Optional<MemberDto> memberOptional = memberService.getMemberByOauthId(oauthId);

        if (memberOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Member not found");
        }

        Long memberId = memberOptional.get().getId();
        MentoringResponseDto responseDto = mentoringService.getMentoringById(postId, memberId);
        return ResponseEntity.ok(responseDto);
    }

    @PutMapping("/{postId}")
    public ResponseEntity<?> updateMentoring(@PathVariable Long postId,
                                             @Valid @RequestBody MentoringRequestDto mentoringRequestDto,
                                             @AuthenticationPrincipal CustomOAuth2User customOAuth2User) throws IOException {

        if (customOAuth2User == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User not authenticated");
        }

        String oauthId = customOAuth2User.getOauthId();
        Optional<MemberDto> memberOptional = memberService.getMemberByOauthId(oauthId);

        if (memberOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Member not found");
        }

        Long memberId = memberOptional.get().getId();
        MentoringResponseDto responseDto = mentoringService.updateMentoring(postId, mentoringRequestDto, memberId);
        return ResponseEntity.ok(responseDto);
    }

    @DeleteMapping("/{postId}")
    public ResponseEntity<?> deleteMentoring(@PathVariable Long postId,
                                             @AuthenticationPrincipal CustomOAuth2User customOAuth2User) {

        if (customOAuth2User == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User not authenticated");
        }

        String oauthId = customOAuth2User.getOauthId();
        Optional<MemberDto> memberOptional = memberService.getMemberByOauthId(oauthId);

        if (memberOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Member not found");
        }

        Long memberId = memberOptional.get().getId();
        mentoringService.deleteMentoring(postId, memberId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/category/{category}")
    public ResponseEntity<?> getMentoringsByCategory(
            @PathVariable Category category,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "8") int size,
            @RequestParam(defaultValue = "createdAt") String sort,
            @RequestParam(defaultValue = "desc") String direction,
            @RequestParam(required = false) String keyword,
            @AuthenticationPrincipal CustomOAuth2User customOAuth2User) {

        if (customOAuth2User == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User not authenticated");
        }

        String oauthId = customOAuth2User.getOauthId();
        Optional<MemberDto> memberOptional = memberService.getMemberByOauthId(oauthId);

        if (memberOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Member not found");
        }

        Long memberId = memberOptional.get().getId();
        try {
            PageRequest pageRequest = createPageRequest(page, size, sort, direction);
            String decodedKeyword = decodeKeyword(keyword);
            Page<MentoringResponseDto> mentorings = mentoringService.getMentoringsByCategory(category, pageRequest, decodedKeyword, memberId);
            return ResponseEntity.ok(mentorings);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/category-counts")
    public ResponseEntity<Map<Category, Long>> getMentoringCountByCategory() {
        Map<Category, Long> categoryCounts = mentoringService.getMentoringCountByCategory();
        return ResponseEntity.ok(categoryCounts);
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
