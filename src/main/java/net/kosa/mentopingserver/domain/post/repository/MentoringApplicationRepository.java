package net.kosa.mentopingserver.domain.post.repository;

import net.kosa.mentopingserver.domain.member.entity.Member;
import net.kosa.mentopingserver.domain.post.entity.MentoringApplication;
import net.kosa.mentopingserver.domain.post.entity.Post;
import net.kosa.mentopingserver.global.common.enums.Status;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MentoringApplicationRepository extends JpaRepository<MentoringApplication, Long> {
    Page<MentoringApplication> findByPostId(Long mentoringId, PageRequest pageRequest);

    Optional<MentoringApplication> findByIdAndPostId(Long applicationId, Long mentoringId);

    Page<MentoringApplication> findByMemberId(Long memberId, PageRequest pageRequest);

    Optional<MentoringApplication> findByPostAndMemberAndStatus(Post post, Member member, Status status);
}
