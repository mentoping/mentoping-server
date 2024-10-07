package net.kosa.mentopingserver.domain.post;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import net.kosa.mentopingserver.domain.post.dto.QuestionRequestDto;
import net.kosa.mentopingserver.domain.post.dto.QuestionResponseDto;
import net.kosa.mentopingserver.global.common.enums.SubCategory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/questions")
public class QuestionController {

    private final QuestionService questionService;

    @GetMapping
    public ResponseEntity<Page<QuestionResponseDto>> getAllQuestions(Pageable pageable) {
        Page<QuestionResponseDto> questions = questionService.getAllQuestions(pageable);
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
}
