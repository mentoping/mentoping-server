package net.kosa.mentopingserver.domain.post.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import net.kosa.mentopingserver.domain.login.CustomOAuth2User;
import net.kosa.mentopingserver.domain.member.MemberService;
import net.kosa.mentopingserver.domain.member.dto.MemberDto;
import net.kosa.mentopingserver.domain.post.dto.MentoringReviewDto;
import net.kosa.mentopingserver.domain.post.service.MentoringApplicationService;
import net.kosa.mentopingserver.domain.post.service.MentoringReviewService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
public class MentoringReviewController {

    private final MentoringReviewService mentoringReviewService;
    private final MentoringApplicationService mentoringApplicationService;
    private final MemberService memberService;

    @PostMapping("/mentorings/{mentoringId}/applications/{applicationId}/reviews")
    public ResponseEntity<?> createReview(
            @PathVariable Long mentoringId,
            @PathVariable Long applicationId,
            @Valid @RequestBody MentoringReviewDto reviewDto,
            @AuthenticationPrincipal CustomOAuth2User customOAuth2User) {

        if (customOAuth2User == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User not authenticated");
        }

        String oauthId = customOAuth2User.getOauthId();
        Optional<MemberDto> memberOptional = memberService.getMemberByOauthId(oauthId);

        if (memberOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Member not found");
        }

        Long memberId = memberOptional.get().getId();        MentoringReviewDto createdReview = mentoringReviewService.createReview(mentoringId, applicationId, reviewDto, memberId);
        return new ResponseEntity<>(createdReview, HttpStatus.CREATED);
    }

    @GetMapping("/reviews/{reviewId}")
    public ResponseEntity<MentoringReviewDto> getReview(@PathVariable Long reviewId) {
        MentoringReviewDto review = mentoringReviewService.getReview(reviewId);
        return ResponseEntity.ok(review);
    }

    @PutMapping("/reviews/{reviewId}")
    public ResponseEntity<?> updateReview(
            @PathVariable Long reviewId,
            @Valid @RequestBody MentoringReviewDto reviewDto,
            @AuthenticationPrincipal CustomOAuth2User customOAuth2User) {

        if (customOAuth2User == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User not authenticated");
        }

        String oauthId = customOAuth2User.getOauthId();
        Optional<MemberDto> memberOptional = memberService.getMemberByOauthId(oauthId);

        if (memberOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Member not found");
        }

        Long memberId = memberOptional.get().getId();        MentoringReviewDto updatedReview = mentoringReviewService.updateReview(reviewId, reviewDto, memberId);
        return ResponseEntity.ok(updatedReview);
    }

    @DeleteMapping("/reviews/{reviewId}")
    public ResponseEntity<?> deleteReview(
            @PathVariable Long reviewId,
            @AuthenticationPrincipal CustomOAuth2User customOAuth2User) {

        if (customOAuth2User == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User not authenticated");
        }

        String oauthId = customOAuth2User.getOauthId();
        Optional<MemberDto> memberOptional = memberService.getMemberByOauthId(oauthId);

        if (memberOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Member not found");
        }

        Long memberId = memberOptional.get().getId();        mentoringReviewService.deleteReview(reviewId, memberId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/mentorings/{mentoringId}/reviews")
    public ResponseEntity<Page<MentoringReviewDto>> getReviews(
            @PathVariable Long mentoringId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        PageRequest pageRequest = PageRequest.of(page, size);
        Page<MentoringReviewDto> reviews = mentoringReviewService.getReviewsByMentoring(mentoringId, pageRequest);
        return ResponseEntity.ok(reviews);
    }

    @GetMapping("/mentorings/{mentoringId}/reviews/average-rating")
    public ResponseEntity<Double> getAverageRating(@PathVariable Long mentoringId) {
        Double averageRating = mentoringReviewService.getAverageRating(mentoringId);
        return ResponseEntity.ok(averageRating);
    }

    @GetMapping("/mentorings/{mentoringId}/reviews/rating-distribution")
    public ResponseEntity<Map<Integer, Long>> getRatingDistribution(@PathVariable Long mentoringId) {
        Map<Integer, Long> distribution = mentoringReviewService.getRatingDistribution(mentoringId);
        return ResponseEntity.ok(distribution);
    }
}