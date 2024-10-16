package net.kosa.mentopingserver.domain.post.repository;

import net.kosa.mentopingserver.domain.member.entity.Member;
import net.kosa.mentopingserver.domain.post.entity.Post;
import net.kosa.mentopingserver.global.common.enums.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long>, PostRepositoryCustom {
    
    Page<Post> findByMemberAndPriceIsNull(Member member, Pageable pageable);

    Page<Post> findByCategoryAndPriceIsNull(Category category, Pageable pageable);

    Page<Post> findByMemberAndPriceIsNotNull(Member member, Pageable pageable);

    Page<Post> findByCategoryAndPriceIsNotNull(Category category, Pageable pageable);

    @Query("SELECT pl.post FROM PostLikes pl WHERE pl.member = :member AND pl.post.price IS NULL")
    Page<Post> findLikedQuestionsByMember(@Param("member") Member member, Pageable pageable);

    @Query("SELECT pl.post FROM PostLikes pl WHERE pl.member = :member AND pl.post.price IS NOT NULL")
    Page<Post> findLikedMentoringsByMember(@Param("member") Member member, Pageable pageable);

    @Query("SELECT p.category, COUNT(p) FROM Post p WHERE p.price IS NULL AND p.category IN :categories GROUP BY p.category")
    List<Object[]> countPostsByCategory(@Param("categories") List<Category> categories);

    @Query("SELECT p.category, COUNT(p) FROM Post p WHERE p.price IS NOT NULL AND p.category IN :categories GROUP BY p.category")
    List<Object[]> countMentoringsByCategory(@Param("categories") List<Category> categories);

    // 특정 멤버가 작성한 멘토링 (price가 null이 아닌) 포스트의 수를 모두 조회하는 메서드
    @Query("SELECT COUNT(p) FROM Post p WHERE p.member = :member AND p.price IS NOT NULL")
    Long findAllMentoringsByMember(@Param("member") Member member);


}
