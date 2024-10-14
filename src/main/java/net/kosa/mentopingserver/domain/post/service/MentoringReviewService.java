package net.kosa.mentopingserver.domain.post.service;

import net.kosa.mentopingserver.domain.post.dto.MentoringReviewDto;
import net.kosa.mentopingserver.domain.post.entity.MentoringReview;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

public interface MentoringReviewService {

    @Transactional
    public MentoringReviewDto createReview(Long mentoringId, Long applicationId, MentoringReviewDto reviewDto, Long memberId);

    @Transactional(readOnly = true)
    MentoringReviewDto getReview(Long reviewId);

    @Transactional
    public MentoringReviewDto updateReview(Long reviewId, MentoringReviewDto reviewDto, Long memberId);

    @Transactional
    public void deleteReview(Long reviewId, Long memberId);

    @Transactional(readOnly = true)
    Page<MentoringReviewDto> getReviewsByMentoring(Long mentoringId, Pageable pageable);

    @Transactional(readOnly = true)
    Double getAverageRating(Long mentoringId);

    @Transactional(readOnly = true)
    Map<Integer, Long> getRatingDistribution(Long mentoringId);
}
