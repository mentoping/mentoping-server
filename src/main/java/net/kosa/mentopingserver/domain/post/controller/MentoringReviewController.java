package net.kosa.mentopingserver.domain.post.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import net.kosa.mentopingserver.domain.post.dto.MentoringReviewDto;
import net.kosa.mentopingserver.domain.post.service.MentoringApplicationService;
import net.kosa.mentopingserver.domain.post.service.MentoringReviewService;
import net.kosa.mentopingserver.global.config.CurrentUser;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequiredArgsConstructor
public class MentoringReviewController {

    private final MentoringReviewService mentoringReviewService;
    private final MentoringApplicationService mentoringApplicationService;

    @PostMapping("/mentorings/{mentoringId}/applications/{applicationId}/reviews")
    public ResponseEntity<MentoringReviewDto> createReview(
            @PathVariable Long mentoringId,
            @PathVariable Long applicationId,
            @Valid @RequestBody MentoringReviewDto reviewDto,
            @CurrentUser(required = false) Long memberId) {
        MentoringReviewDto createdReview = mentoringReviewService.createReview(mentoringId, applicationId, reviewDto, memberId);
        return new ResponseEntity<>(createdReview, HttpStatus.CREATED);
    }

    @GetMapping("/reviews/{reviewId}")
    public ResponseEntity<MentoringReviewDto> getReview(@PathVariable Long reviewId) {
        MentoringReviewDto review = mentoringReviewService.getReview(reviewId);
        return ResponseEntity.ok(review);
    }

    @PutMapping("/reviews/{reviewId}")
    public ResponseEntity<MentoringReviewDto> updateReview(
            @PathVariable Long reviewId,
            @Valid @RequestBody MentoringReviewDto reviewDto,
            @CurrentUser Long memberId) {
        MentoringReviewDto updatedReview = mentoringReviewService.updateReview(reviewId, reviewDto, memberId);
        return ResponseEntity.ok(updatedReview);
    }

    @DeleteMapping("/reviews/{reviewId}")
    public ResponseEntity<Void> deleteReview(
            @PathVariable Long reviewId,
            @CurrentUser Long memberId) {
        mentoringReviewService.deleteReview(reviewId, memberId);
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