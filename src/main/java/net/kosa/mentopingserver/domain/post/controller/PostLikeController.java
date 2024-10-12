package net.kosa.mentopingserver.domain.post.controller;

import lombok.RequiredArgsConstructor;
import net.kosa.mentopingserver.domain.post.service.PostLikeService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/like")
public class PostLikeController {

    private final PostLikeService postLikeService;

    @PostMapping("/{postId}")
    public ResponseEntity<String> toggleLike(@PathVariable Long postId,
                                             @RequestParam Long memberId) {
        boolean isLiked = postLikeService.hasUserLikedPost(postId, memberId);

        if (isLiked) {
            postLikeService.removeLike(postId, memberId);
            return ResponseEntity.ok("Like removed successfully");
        } else {
            postLikeService.addLike(postId, memberId);
            return ResponseEntity.ok("Like added successfully");
        }
    }

    @PostMapping("/batch")
    public ResponseEntity<Map<Long, Boolean>> batchToggleLike(@RequestBody List<Long> postIds,
                                                              @RequestParam Long memberId) {
        Map<Long, Boolean> result = postLikeService.batchToggleLike(postIds, memberId);
        return ResponseEntity.ok(result);
    }
}
