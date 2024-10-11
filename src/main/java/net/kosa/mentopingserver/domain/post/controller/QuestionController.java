package net.kosa.mentopingserver.domain.post.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import net.kosa.mentopingserver.domain.post.dto.QuestionRequestDto;
import net.kosa.mentopingserver.domain.post.dto.QuestionResponseDto;
import net.kosa.mentopingserver.domain.post.service.QuestionService;
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
@RequestMapping("/questions")
public class QuestionController {

    private final QuestionService questionService;

    @GetMapping
    public ResponseEntity<Page<QuestionResponseDto>> getAllQuestions(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size,
            @RequestParam(defaultValue = "createdAt") String sort,
            @RequestParam(defaultValue = "desc") String direction,
            @RequestParam(required = false) String keyword) {

        try {
            PageRequest pageRequest = createPageRequest(page, size, sort, direction);
            String decodedKeyword = decodeKeyword(keyword);
            Page<QuestionResponseDto> questions = questionService.getAllQuestions(pageRequest, decodedKeyword);
            return ResponseEntity.ok(questions);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping
    public ResponseEntity<QuestionResponseDto> createQuestion(@Valid @RequestBody QuestionRequestDto questionRequestDto,
                                                              @RequestParam Long memberId) {
        QuestionResponseDto responseDto = questionService.createQuestion(questionRequestDto, memberId);
        return ResponseEntity.status(HttpStatus.CREATED).body(responseDto);
    }

    @GetMapping("/{postId}")
    public ResponseEntity<QuestionResponseDto> getQuestionById(@PathVariable Long postId) {
        QuestionResponseDto responseDto = questionService.getQuestionById(postId);
        return ResponseEntity.ok(responseDto);
    }

    @PutMapping("/{postId}")
    public ResponseEntity<QuestionResponseDto> updateQuestion(@PathVariable Long postId,
                                                              @Valid @RequestBody QuestionRequestDto questionRequestDto) {
        QuestionResponseDto responseDto = questionService.updateQuestion(postId, questionRequestDto);
        return ResponseEntity.ok(responseDto);
    }

    @DeleteMapping("/{postId}")
    public ResponseEntity<Void> deleteQuestion(@PathVariable Long postId) {
        questionService.deleteQuestion(postId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/category/{category}")
    public ResponseEntity<Page<QuestionResponseDto>> getQuestionsByCategory(
            @PathVariable Category category,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size,
            @RequestParam(defaultValue = "createdAt") String sort,
            @RequestParam(defaultValue = "desc") String direction,
            @RequestParam(required = false) String keyword) {

        try {
            PageRequest pageRequest = createPageRequest(page, size, sort, direction);
            String decodedKeyword = decodeKeyword(keyword);
            Page<QuestionResponseDto> questions = questionService.getQuestionsByCategory(category, pageRequest, decodedKeyword);
            return ResponseEntity.ok(questions);
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
