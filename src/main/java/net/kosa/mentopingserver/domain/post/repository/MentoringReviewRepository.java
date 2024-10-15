package net.kosa.mentopingserver.domain.post.repository;

import net.kosa.mentopingserver.domain.post.entity.MentoringApplication;
import net.kosa.mentopingserver.domain.post.entity.MentoringReview;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MentoringReviewRepository extends JpaRepository<MentoringReview, Long> {

    @Query("SELECT AVG(r.rate) FROM MentoringReview r WHERE r.mentoringApplication.post.id = :mentoringId")
    Double getAverageRatingByMentoringId(@Param("mentoringId") Long mentoringId);

    List<MentoringReview> findByMentoringApplication_Post_Id(Long mentoringId);

    Page<MentoringReview> findByMentoringApplication_Post_Id(Long mentoringId, Pageable pageable);

    boolean existsByMentoringApplication(MentoringApplication application);
}
