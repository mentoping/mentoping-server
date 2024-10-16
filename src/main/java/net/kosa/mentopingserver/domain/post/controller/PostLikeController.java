package net.kosa.mentopingserver.domain.post.controller;

import lombok.RequiredArgsConstructor;
import net.kosa.mentopingserver.domain.login.CustomOAuth2User;
import net.kosa.mentopingserver.domain.member.MemberService;
import net.kosa.mentopingserver.domain.member.dto.MemberDto;
import net.kosa.mentopingserver.domain.post.service.PostLikeService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/like")
public class PostLikeController {

    private final PostLikeService postLikeService;
    private final MemberService memberService;

    @PostMapping("/{postId}")
    public ResponseEntity<String> toggleLike(@PathVariable Long postId,
                                             @AuthenticationPrincipal CustomOAuth2User customOAuth2User) {

        if (customOAuth2User == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User not authenticated");
        }

        String oauthId = customOAuth2User.getOauthId();
        Optional<MemberDto> memberOptional = memberService.getMemberByOauthId(oauthId);

        if (memberOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Member not found");
        }

        Long memberId = memberOptional.get().getId();        boolean isLiked = postLikeService.hasUserLikedPost(postId, memberId);

        if (isLiked) {
            postLikeService.removeLike(postId, memberId);
            return ResponseEntity.ok("Like removed successfully");
        } else {
            postLikeService.addLike(postId, memberId);
            return ResponseEntity.ok("Like added successfully");
        }
    }

    @PostMapping("/batch")
    public ResponseEntity<?> batchToggleLike(@RequestBody List<Long> postIds,
                                                              @AuthenticationPrincipal CustomOAuth2User customOAuth2User) {

        if (customOAuth2User == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User not authenticated");
        }

        String oauthId = customOAuth2User.getOauthId();
        Optional<MemberDto> memberOptional = memberService.getMemberByOauthId(oauthId);

        if (memberOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Member not found");
        }

        Long memberId = memberOptional.get().getId();        Map<Long, Boolean> result = postLikeService.batchToggleLike(postIds, memberId);
        return ResponseEntity.ok(result);
    }
}
