package net.kosa.mentopingserver.domain.post.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import net.kosa.mentopingserver.domain.member.MemberRepository;
import net.kosa.mentopingserver.domain.member.entity.Member;
import net.kosa.mentopingserver.domain.post.dto.MentoringReviewDto;
import net.kosa.mentopingserver.domain.post.entity.MentoringApplication;
import net.kosa.mentopingserver.domain.post.entity.MentoringReview;
import net.kosa.mentopingserver.domain.post.repository.MentoringApplicationRepository;
import net.kosa.mentopingserver.domain.post.repository.MentoringReviewRepository;
import net.kosa.mentopingserver.global.common.enums.Status;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Log4j2
public class MentoringReviewServiceImpl implements MentoringReviewService {

    private final MentoringReviewRepository reviewRepository;
    private final MentoringApplicationRepository applicationRepository;
    private final MemberRepository memberRepository;

    @Override
    @Transactional
    public MentoringReviewDto createReview(Long mentoringId, Long applicationId, MentoringReviewDto reviewDto, Long memberId) {
        log.info("Attempting to create review. MentoringId: {}, ApplicationId: {}, MemberId: {}", mentoringId, applicationId, memberId);

        MentoringApplication application = applicationRepository.findById(applicationId)
                .orElseThrow(() -> {
                    log.error("Application not found. ApplicationId: {}", applicationId);
                    return new EntityNotFoundException("Application not found");
                });

        log.info("Application found. PostId: {}, Status: {}", application.getPost().getId(), application.getStatus());

        // 1. Check if the application status is APPROVED
        if (application.getStatus() != Status.APPROVED) {
            log.error("Application is not approved. Status: {}", application.getStatus());
            throw new IllegalStateException("Only approved applications can leave a review");
        }

        if (!application.getPost().getId().equals(mentoringId)) {
            log.error("Application does not belong to the specified mentoring. MentoringId: {}, ApplicationPostId: {}",
                    mentoringId, application.getPost().getId());
            throw new IllegalArgumentException("Application does not belong to the specified mentoring");
        }

        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> {
                    log.error("Member not found. MemberId: {}", memberId);
                    return new EntityNotFoundException("Member not found");
                });

        if (!application.getMember().getId().equals(memberId)) {
            log.error("Only the applicant can leave a review. ApplicationMemberId: {}, RequestMemberId: {}",
                    application.getMember().getId(), memberId);
            throw new IllegalArgumentException("Only the applicant can leave a review");
        }

        // 2. Check if a review already exists for this application
        if (reviewRepository.existsByMentoringApplication(application)) {
            log.error("Review already exists for this application. ApplicationId: {}", applicationId);
            throw new IllegalStateException("A review has already been submitted for this application");
        }

        MentoringReview review = MentoringReview.builder()
                .mentoringApplication(application)
                .member(member)
                .rate(reviewDto.getRate())
                .content(reviewDto.getContent())
                .build();

        MentoringReview savedReview = reviewRepository.save(review);
        log.info("Review created successfully. ReviewId: {}", savedReview.getId());

        return convertToDto(savedReview);
    }

    @Override
    @Transactional(readOnly = true)
    public MentoringReviewDto getReview(Long reviewId) {
        MentoringReview review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid review ID"));
        return convertToDto(review);
    }

    @Override
    @Transactional
    public MentoringReviewDto updateReview(Long reviewId, MentoringReviewDto reviewDto, Long memberId) {
        MentoringReview review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new EntityNotFoundException("Review not found"));

        if (!review.getMember().getId().equals(memberId)) {
            throw new IllegalArgumentException("You don't have permission to update this review");
        }

        review = review.toBuilder()
                .rate(reviewDto.getRate())
                .content(reviewDto.getContent())
                .build();

        MentoringReview updatedReview = reviewRepository.save(review);
        return convertToDto(updatedReview);
    }

    @Override
    @Transactional
    public void deleteReview(Long reviewId, Long memberId) {
        MentoringReview review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new EntityNotFoundException("Review not found"));

        if (!review.getMember().getId().equals(memberId)) {
            throw new IllegalArgumentException("You don't have permission to delete this review");
        }

        reviewRepository.delete(review);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<MentoringReviewDto> getReviewsByMentoring(Long mentoringId, Pageable pageable) {
        Page<MentoringReview> reviews = reviewRepository.findByMentoringApplication_Post_Id(mentoringId, pageable);
        return reviews.map(this::convertToDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Double getAverageRating(Long mentoringId) {
        return reviewRepository.getAverageRatingByMentoringId(mentoringId);
    }

    @Override
    @Transactional(readOnly = true)
    public Map<Integer, Long> getRatingDistribution(Long mentoringId) {
        return reviewRepository.findByMentoringApplication_Post_Id(mentoringId)
                .stream()
                .collect(Collectors.groupingBy(MentoringReview::getRate, Collectors.counting()));
    }

    private MentoringReviewDto convertToDto(MentoringReview review) {
        return MentoringReviewDto.builder()
                .id(review.getId())
                .applicationId(review.getMentoringApplication().getId())
                .memberId(review.getMember().getId())
                .memberName(review.getMember().getName())
                .rate(review.getRate())
                .content(review.getContent())
                .createdAt(review.getCreatedAt())
                .updatedAt(review.getUpdatedAt())
                .build();
    }
}
