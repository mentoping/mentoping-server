package net.kosa.mentopingserver.domain.post.repository;

import net.kosa.mentopingserver.domain.member.entity.Member;
import net.kosa.mentopingserver.domain.post.entity.MentoringApplication;
import net.kosa.mentopingserver.domain.post.entity.Post;
import net.kosa.mentopingserver.global.common.enums.Status;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface MentoringApplicationRepository extends JpaRepository<MentoringApplication, Long> {
    Page<MentoringApplication> findByPostId(Long mentoringId, PageRequest pageRequest);

    Optional<MentoringApplication> findByIdAndPostId(Long applicationId, Long mentoringId);

    Page<MentoringApplication> findByMemberId(Long memberId, PageRequest pageRequest);

    Optional<MentoringApplication> findByPostAndMemberAndStatus(Post post, Member member, Status status);

    Page<MentoringApplication> findByMemberIdAndStatus(Long memberId, Status status, Pageable pageable);

    @Query("SELECT CASE WHEN COUNT(ma) > 0 THEN true ELSE false END " +
            "FROM MentoringApplication ma " +
            "WHERE ma.post.id = :postId " +
            "AND ma.member.id = :memberId " +
            "AND ma.status = net.kosa.mentopingserver.global.common.enums.Status.PENDING")
    boolean existsByPostIdAndMemberIdAndStatusWaiting(@Param("postId") Long postId, @Param("memberId") Long memberId);

    @Query("SELECT ma.post FROM MentoringApplication ma WHERE ma.member.id = :memberId AND ma.status = :status")
    Page<Post> findApprovedAppliedMentoringsByMemberId(@Param("memberId") Long memberId, @Param("status") Status status, Pageable pageable);
}
