package net.kosa.mentopingserver.domain.post;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import net.kosa.mentopingserver.domain.post.dto.QuestionRequestDto;
import net.kosa.mentopingserver.domain.post.dto.QuestionResponseDto;
import net.kosa.mentopingserver.global.common.enums.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
            @RequestParam(defaultValue = "desc") String direction) {

        if (!sort.equals("createdAt") && !sort.equals("likeCount")) {
            throw new IllegalArgumentException("Not 'createdAt' or 'likeCount'");
        }

        Sort.Direction sortDirection = Sort.Direction.fromString(direction);
        PageRequest pageRequest = PageRequest.of(page, size, Sort.by(sortDirection, sort));

        Page<QuestionResponseDto> questions = questionService.getAllQuestions(pageRequest);
        return ResponseEntity.ok(questions);
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
            @RequestParam(defaultValue = "desc") String direction) {

        if (!sort.equals("createdAt") && !sort.equals("likeCount")) {
            throw new IllegalArgumentException("Sort must be either 'createdAt' or 'likeCount'");
        }

        Sort.Direction sortDirection = Sort.Direction.fromString(direction);
        PageRequest pageRequest = PageRequest.of(page, size, Sort.by(sortDirection, sort));

        Page<QuestionResponseDto> questions = questionService.getQuestionsByCategory(category, pageRequest);
        return ResponseEntity.ok(questions);
    }
}
