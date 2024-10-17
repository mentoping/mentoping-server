package net.kosa.mentopingserver.domain.post.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import net.kosa.mentopingserver.domain.login.CustomOAuth2User;
import net.kosa.mentopingserver.domain.member.MemberService;
import net.kosa.mentopingserver.domain.member.dto.MemberDto;
import net.kosa.mentopingserver.domain.post.dto.QuestionRequestDto;
import net.kosa.mentopingserver.domain.post.dto.QuestionResponseDto;
import net.kosa.mentopingserver.domain.post.service.QuestionService;
import net.kosa.mentopingserver.global.common.enums.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/questions")
public class QuestionController {

    private final QuestionService questionService;
    private final MemberService memberService;

    @GetMapping
    public ResponseEntity<?> getAllQuestions(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size,
            @RequestParam(defaultValue = "createdAt") String sort,
            @RequestParam(defaultValue = "desc") String direction,
            @RequestParam(required = false) String keyword,
            @AuthenticationPrincipal CustomOAuth2User customOAuth2User) {

        Long memberId = null;
        if (customOAuth2User != null) {
            String oauthId = customOAuth2User.getOauthId();
            Optional<MemberDto> memberOptional = memberService.getMemberByOauthId(oauthId);
            if (memberOptional.isPresent()) {
                memberId = memberOptional.get().getId();
            }
        }

        try {
            PageRequest pageRequest = createPageRequest(page, size, sort, direction);
            String decodedKeyword = decodeKeyword(keyword);
            Page<QuestionResponseDto> questions = questionService.getAllQuestions(pageRequest, decodedKeyword, memberId);
            return ResponseEntity.ok(questions);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping
    public ResponseEntity<?> createQuestion(@Valid @RequestBody QuestionRequestDto questionRequestDto,
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
        QuestionResponseDto responseDto = questionService.createQuestion(questionRequestDto, memberId);
        return ResponseEntity.status(HttpStatus.CREATED).body(responseDto);
    }

    @GetMapping("/{postId}")
    public ResponseEntity<?> getQuestionById(@PathVariable Long postId,
                                             @AuthenticationPrincipal CustomOAuth2User customOAuth2User) {

        Long memberId = null;
        if (customOAuth2User != null) {
            String oauthId = customOAuth2User.getOauthId();
            Optional<MemberDto> memberOptional = memberService.getMemberByOauthId(oauthId);
            if (memberOptional.isPresent()) {
                memberId = memberOptional.get().getId();
            }
        }

        QuestionResponseDto responseDto = questionService.getQuestionById(postId, memberId);
        return ResponseEntity.ok(responseDto);
    }

    @GetMapping("/my-questions")
    public ResponseEntity<?> getMyQuestions(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size,
            @RequestParam(defaultValue = "createdAt") String sort,
            @RequestParam(defaultValue = "desc") String direction,
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
            Page<QuestionResponseDto> myQuestions = questionService.getQuestionsByMemberId(memberId, pageRequest, memberId);
            return ResponseEntity.ok(myQuestions);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/{postId}")
    public ResponseEntity<?> updateQuestion(@PathVariable Long postId,
                                            @Valid @RequestBody QuestionRequestDto questionRequestDto,
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
        QuestionResponseDto responseDto = questionService.updateQuestion(postId, questionRequestDto, memberId);
        return ResponseEntity.ok(responseDto);
    }

    @DeleteMapping("/{postId}")
    public ResponseEntity<?> deleteQuestion(@PathVariable Long postId,
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
        questionService.deleteQuestion(postId, memberId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/category/{category}")
    public ResponseEntity<?> getQuestionsByCategory(
            @PathVariable Category category,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size,
            @RequestParam(defaultValue = "createdAt") String sort,
            @RequestParam(defaultValue = "desc") String direction,
            @RequestParam(required = false) String keyword,
            @AuthenticationPrincipal CustomOAuth2User customOAuth2User) {

        Long memberId = null;
        if (customOAuth2User != null) {
            String oauthId = customOAuth2User.getOauthId();
            Optional<MemberDto> memberOptional = memberService.getMemberByOauthId(oauthId);
            if (memberOptional.isPresent()) {
                memberId = memberOptional.get().getId();
            }
        }

        try {
            PageRequest pageRequest = createPageRequest(page, size, sort, direction);
            String decodedKeyword = decodeKeyword(keyword);
            Page<QuestionResponseDto> questions = questionService.getQuestionsByCategory(category, pageRequest, decodedKeyword, memberId);
            return ResponseEntity.ok(questions);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/answered")
    public ResponseEntity<?> getAnsweredQuestions(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size,
            @RequestParam(defaultValue = "createdAt") String sort,
            @RequestParam(defaultValue = "desc") String direction,
            @AuthenticationPrincipal CustomOAuth2User customOAuth2User) {

        Long memberId = null;
        if (customOAuth2User != null) {
            String oauthId = customOAuth2User.getOauthId();
            Optional<MemberDto> memberOptional = memberService.getMemberByOauthId(oauthId);
            if (memberOptional.isPresent()) {
                memberId = memberOptional.get().getId();
            }
        }

        try {
            if (memberId == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User not authenticated");
            }

            PageRequest pageRequest = createPageRequest(page, size, sort, direction);
            Page<QuestionResponseDto> answeredQuestions = questionService.getAnsweredQuestionsByMemberId(memberId, pageRequest);
            return ResponseEntity.ok(answeredQuestions);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/category-counts")
    public Map<Category, Long> getQuestionCountByCategory() {
        return questionService.getQuestionCountByCategory();
    }

    private PageRequest createPageRequest(int page, int size, String sort, String direction) {
        validateSortCriteria(sort);
        Sort.Direction sortDirection = Sort.Direction.fromString(direction);
        return PageRequest.of(page, size, Sort.by(sortDirection, sort));
    }

    private void validateSortCriteria(String sort) {
        List<String> validCriteria = Arrays.asList("createdAt", "likeCount", "answerCount");
        if (!validCriteria.contains(sort)) {
            throw new IllegalArgumentException("Sort must be one of: " + String.join(", ", validCriteria));
        }
    }

    private String decodeKeyword(String keyword) {
        if (keyword == null) {
            return null;
        }
        return URLDecoder.decode(keyword, StandardCharsets.UTF_8);
    }
}
