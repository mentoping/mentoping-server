package net.kosa.mentopingserver.domain.post.service;

import net.kosa.mentopingserver.domain.post.dto.MentoringReviewDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

public interface MentoringReviewService {

    @Transactional
    MentoringReviewDto createReview(Long mentoringId, Long applicationId, MentoringReviewDto reviewDto, Long memberId);

    @Transactional(readOnly = true)
    MentoringReviewDto getReview(Long reviewId);

    @Transactional
    MentoringReviewDto updateReview(Long reviewId, MentoringReviewDto reviewDto, Long memberId);

    @Transactional
    void deleteReview(Long reviewId, Long memberId);

    @Transactional(readOnly = true)
    Page<MentoringReviewDto> getReviewsByMentoring(Long mentoringId, Pageable pageable);

    @Transactional(readOnly = true)
    Double getAverageRating(Long mentoringId);

    @Transactional(readOnly = true)
    Map<Integer, Long> getRatingDistribution(Long mentoringId);
}
