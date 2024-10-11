package net.kosa.mentopingserver.domain.answer;

import lombok.RequiredArgsConstructor;
import net.kosa.mentopingserver.domain.answer.dto.AnswerRequestDto;
import net.kosa.mentopingserver.domain.answer.dto.AnswerResponseDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/answers")
@RequiredArgsConstructor
public class AnswerController {

    private final AnswerService answerService;

    @PostMapping
    public ResponseEntity<AnswerResponseDto> addAnswer(@RequestBody AnswerRequestDto requestDto,
                                                       @RequestParam Long memberId) {
        Answer answer = answerService.addAnswer(requestDto.getPostId(), requestDto.getContent(), memberId);
        AnswerResponseDto responseDto = answerService.toAnswerResponseDto(answer);
        return ResponseEntity.status(HttpStatus.CREATED).body(responseDto);
    }

    @PutMapping("/{answerId}")
    public ResponseEntity<AnswerResponseDto> updateAnswer(@PathVariable Long answerId,
                                                          @RequestBody AnswerRequestDto requestDto,
                                                          @RequestParam Long memberId) {
        Answer updatedAnswer = answerService.updateAnswer(answerId, requestDto.getContent(), memberId);
        AnswerResponseDto responseDto = answerService.toAnswerResponseDto(updatedAnswer);
        return ResponseEntity.ok(responseDto);
    }

    @DeleteMapping("/{answerId}")
    public ResponseEntity<Void> removeAnswer(@PathVariable Long answerId,
                                             @RequestParam Long memberId) {
        answerService.removeAnswer(answerId, memberId);
        return ResponseEntity.noContent().build();
    }

}
