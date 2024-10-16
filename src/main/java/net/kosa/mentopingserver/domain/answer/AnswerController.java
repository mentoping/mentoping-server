package net.kosa.mentopingserver.domain.answer;

import lombok.RequiredArgsConstructor;
import net.kosa.mentopingserver.domain.answer.dto.AnswerRequestDto;
import net.kosa.mentopingserver.domain.answer.dto.AnswerResponseDto;
import net.kosa.mentopingserver.domain.login.CustomOAuth2User;
import net.kosa.mentopingserver.domain.member.MemberService;
import net.kosa.mentopingserver.domain.member.dto.MemberDto;
import net.kosa.mentopingserver.global.exception.AnswerNotFoundException;
import net.kosa.mentopingserver.global.exception.UnauthorizedException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/answers")
@RequiredArgsConstructor
public class AnswerController {

    private final AnswerService answerService;
    private final MemberService memberService;

    @PostMapping
    public ResponseEntity<?> addAnswer(@RequestBody AnswerRequestDto requestDto,
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
        Answer answer = answerService.addAnswer(requestDto.getPostId(), requestDto.getContent(), memberId);
        AnswerResponseDto responseDto = answerService.toAnswerResponseDto(answer);
        return ResponseEntity.status(HttpStatus.CREATED).body(responseDto);
    }

    @PutMapping("/{answerId}")
    public ResponseEntity<?> updateAnswer(@PathVariable Long answerId,
                                          @RequestBody AnswerRequestDto requestDto,
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
        Answer updatedAnswer = answerService.updateAnswer(answerId, requestDto.getContent(), memberId);
        AnswerResponseDto responseDto = answerService.toAnswerResponseDto(updatedAnswer);
        return ResponseEntity.ok(responseDto);
    }

    @DeleteMapping("/{answerId}")
    public ResponseEntity<?> removeAnswer(@PathVariable Long answerId,
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
        answerService.removeAnswer(answerId, memberId);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{answerId}/selected")
    public ResponseEntity<?> selectAnswer(@PathVariable Long answerId,
                                          @RequestBody AnswerRequestDto requestDto,
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
            AnswerResponseDto selectedAnswer = answerService.selectAnswer(answerId, requestDto, memberId);
            return ResponseEntity.ok(selectedAnswer);
        } catch (AnswerNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (UnauthorizedException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        } catch (IllegalStateException e) {
            return ResponseEntity.badRequest().body(new AnswerResponseDto());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
