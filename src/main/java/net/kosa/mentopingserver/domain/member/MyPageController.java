package net.kosa.mentopingserver.domain.member;

import lombok.RequiredArgsConstructor;
import net.kosa.mentopingserver.domain.post.dto.MentoringResponseDto;
import net.kosa.mentopingserver.domain.post.dto.QuestionResponseDto;
import net.kosa.mentopingserver.domain.post.service.PostLikeService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/mypage")
public class MyPageController {

    private final PostLikeService postLikeService;

    @GetMapping("/liked-questions")
    public ResponseEntity<Page<QuestionResponseDto>> getLikedQuestions(
            @RequestParam Long memberId,
            Pageable pageable) {
        Page<QuestionResponseDto> likedQuestions = postLikeService.getLikedQuestions(memberId, pageable);
        return ResponseEntity.ok(likedQuestions);
    }

    @GetMapping("/liked-mentorings")
    public ResponseEntity<Page<MentoringResponseDto>> getLikedMentorings(
            @RequestParam Long memberId,
            Pageable pageable) {
        Page<MentoringResponseDto> likedMentorings = postLikeService.getLikedMentorings(memberId, pageable);
        return ResponseEntity.ok(likedMentorings);
    }
}
