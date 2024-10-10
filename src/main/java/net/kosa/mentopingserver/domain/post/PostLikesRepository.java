package net.kosa.mentopingserver.domain.post;

import net.kosa.mentopingserver.domain.member.entity.Member;
import net.kosa.mentopingserver.domain.post.entity.Post;
import net.kosa.mentopingserver.domain.post.entity.PostLikes;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PostLikesRepository extends JpaRepository<PostLikes, Long> {

    boolean existsByPostAndMember(Post post, Member member);
    Optional<PostLikes> findByPostAndMember(Post post, Member member);
    boolean existsByPostIdAndMemberId(Long postId, Long memberId);
}
