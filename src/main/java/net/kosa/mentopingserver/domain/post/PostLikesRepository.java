package net.kosa.mentopingserver.domain.post;

import net.kosa.mentopingserver.domain.post.entity.PostLikes;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PostLikesRepository extends JpaRepository<PostLikes, Long> {

    // 특정 게시물과 회원의 좋아요 여부 확인
    Optional<PostLikes> findByPost_IdAndMember_Id(Long postId, Long memberId);

    // 게시물의 좋아요 수 확인
    int countByPost_Id(Long postId);

    // 특정 게시물과 회원의 좋아요 삭제
    void deleteByPost_IdAndMember_Id(Long postId, Long memberId);
}
