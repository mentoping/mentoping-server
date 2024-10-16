package net.kosa.mentopingserver.domain.post.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import net.kosa.mentopingserver.domain.member.MemberService;
import net.kosa.mentopingserver.domain.post.dto.QuestionRequestDto;
import net.kosa.mentopingserver.domain.post.dto.QuestionResponseDto;
import net.kosa.mentopingserver.domain.post.service.QuestionService;
import net.kosa.mentopingserver.global.common.enums.Category;
import net.kosa.mentopingserver.global.config.CurrentUser;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/questions")
public class QuestionController {

    private final QuestionService questionService;
    private final MemberService memberService;

    @GetMapping
    public ResponseEntity<Page<QuestionResponseDto>> getAllQuestions(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size,
            @RequestParam(defaultValue = "createdAt") String sort,
            @RequestParam(defaultValue = "desc") String direction,
            @RequestParam(required = false) String keyword,
            @CurrentUser(required = false) Long memberId) {

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
    public ResponseEntity<QuestionResponseDto> createQuestion(@Valid @RequestBody QuestionRequestDto questionRequestDto,
                                                              @CurrentUser Long memberId) {
        QuestionResponseDto responseDto = questionService.createQuestion(questionRequestDto, memberId);
        return ResponseEntity.status(HttpStatus.CREATED).body(responseDto);
    }

    @GetMapping("/{postId}")
    public ResponseEntity<QuestionResponseDto> getQuestionById(@PathVariable Long postId,
                                                               @CurrentUser(required = false) Long memberId) {
        QuestionResponseDto responseDto = questionService.getQuestionById(postId, memberId);
        return ResponseEntity.ok(responseDto);
    }

    @PutMapping("/{postId}")
    public ResponseEntity<QuestionResponseDto> updateQuestion(@PathVariable Long postId,
                                                              @Valid @RequestBody QuestionRequestDto questionRequestDto,
                                                              @CurrentUser Long memberId) {
        QuestionResponseDto responseDto = questionService.updateQuestion(postId, questionRequestDto, memberId);
        return ResponseEntity.ok(responseDto);
    }

    @DeleteMapping("/{postId}")
    public ResponseEntity<Void> deleteQuestion(@PathVariable Long postId,
                                               @CurrentUser Long memberId) {
        questionService.deleteQuestion(postId, memberId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/category/{category}")
    public ResponseEntity<Page<QuestionResponseDto>> getQuestionsByCategory(
            @PathVariable Category category,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size,
            @RequestParam(defaultValue = "createdAt") String sort,
            @RequestParam(defaultValue = "desc") String direction,
            @RequestParam(required = false) String keyword,
            @CurrentUser Long memberId) {

        try {
            PageRequest pageRequest = createPageRequest(page, size, sort, direction);
            String decodedKeyword = decodeKeyword(keyword);
            Page<QuestionResponseDto> questions = questionService.getQuestionsByCategory(category, pageRequest, decodedKeyword, memberId);
            return ResponseEntity.ok(questions);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
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
