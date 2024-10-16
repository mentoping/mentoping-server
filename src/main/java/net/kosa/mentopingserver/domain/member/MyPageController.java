package net.kosa.mentopingserver.domain.member;

import lombok.RequiredArgsConstructor;
import net.kosa.mentopingserver.domain.login.CustomOAuth2User;
import net.kosa.mentopingserver.domain.member.dto.MemberDto;
import net.kosa.mentopingserver.domain.post.dto.MentoringResponseDto;
import net.kosa.mentopingserver.domain.post.dto.QuestionResponseDto;
import net.kosa.mentopingserver.domain.post.service.PostLikeService;
import net.kosa.mentopingserver.global.config.CurrentUser;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/mypage")
public class MyPageController {

    private final PostLikeService postLikeService;
    private final MemberService memberService;

    @GetMapping("/liked-questions")
    public ResponseEntity<?> getLikedQuestions(
            @AuthenticationPrincipal CustomOAuth2User customOAuth2User,
            Pageable pageable) {
        if (customOAuth2User == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User not authenticated");
        }

        String oauthId = customOAuth2User.getOauthId();
        Optional<MemberDto> memberOptional = memberService.getMemberByOauthId(oauthId);

        if (memberOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Member not found");
        }

        Long memberId = memberOptional.get().getId();
        Page<QuestionResponseDto> likedQuestions = postLikeService.getLikedQuestions(memberId, pageable);
        return ResponseEntity.ok(likedQuestions);
    }

    @GetMapping("/liked-mentorings")
    public ResponseEntity<?> getLikedMentorings(
            @AuthenticationPrincipal CustomOAuth2User customOAuth2User,
            Pageable pageable) {
        if (customOAuth2User == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User not authenticated");
        }

        String oauthId = customOAuth2User.getOauthId();
        Optional<MemberDto> memberOptional = memberService.getMemberByOauthId(oauthId);

        if (memberOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Member not found");
        }

        Long memberId = memberOptional.get().getId();
        Page<MentoringResponseDto> likedMentorings = postLikeService.getLikedMentorings(memberId, pageable);
        return ResponseEntity.ok(likedMentorings);
    }
}
