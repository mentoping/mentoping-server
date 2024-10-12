package net.kosa.mentopingserver.domain.post.repository;

import net.kosa.mentopingserver.domain.post.entity.MentoringReview;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MentoringReviewRepository extends JpaRepository<MentoringReview, Long> {
}
