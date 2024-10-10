package net.kosa.mentopingserver.domain.post;

import net.kosa.mentopingserver.domain.member.Member;
import net.kosa.mentopingserver.domain.post.entity.Post;
import net.kosa.mentopingserver.global.common.enums.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository<Post, Long>, PostRepositoryCustom {
    
    Page<Post> findByMemberAndPriceIsNull(Member member, Pageable pageable);

    Page<Post> findByCategoryAndPriceIsNull(Category category, Pageable pageable);

}
