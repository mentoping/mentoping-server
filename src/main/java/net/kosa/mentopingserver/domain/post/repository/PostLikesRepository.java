package net.kosa.mentopingserver.domain.post.repository;

import net.kosa.mentopingserver.domain.member.entity.Member;
import net.kosa.mentopingserver.domain.post.entity.Post;
import net.kosa.mentopingserver.domain.post.entity.PostLikes;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface PostLikesRepository extends JpaRepository<PostLikes, Long> {

    boolean existsByPostAndMember(Post post, Member member);

    Optional<PostLikes> findByPostAndMember(Post post, Member member);

    boolean existsByPostIdAndMemberId(Long postId, Long memberId);

    @Query("SELECT pl.post FROM PostLikes pl WHERE pl.member = :member AND pl.post.price IS NULL")
    List<Post> findLikedQuestionsByMember(@Param("member") Member member, Pageable pageable);

    @Query("SELECT pl.post FROM PostLikes pl WHERE pl.member = :member AND pl.post.price IS NOT NULL")
    List<Post> findLikedMentoringsByMember(@Param("member") Member member, Pageable pageable);

    @Query("SELECT pl.post.id FROM PostLikes pl WHERE pl.member.id = :memberId AND pl.post.id IN :postIds AND pl.post.price IS NULL")
    Set<Long> findLikedQuestionIdsByMemberAndPostIds(@Param("memberId") Long memberId, @Param("postIds") List<Long> postIds);

    @Query("SELECT pl.post.id FROM PostLikes pl WHERE pl.member.id = :memberId AND pl.post.id IN :postIds AND pl.post.price IS NOT NULL")
    Set<Long> findLikedMentoringIdsByMemberAndPostIds(@Param("memberId") Long memberId, @Param("postIds") List<Long> postIds);

    void deleteByPostAndMember(Post post, Member member);

    @Query("SELECT pl.post.id FROM PostLikes pl WHERE pl.member.id = :memberId AND pl.post.id IN :postIds")
    Set<Long> findExistingLikePostIds(@Param("memberId") Long memberId, @Param("postIds") Set<Long> postIds);

}
