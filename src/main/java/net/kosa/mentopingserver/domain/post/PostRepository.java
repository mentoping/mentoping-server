package net.kosa.mentopingserver.domain.post;

import net.kosa.mentopingserver.domain.member.Member;
import net.kosa.mentopingserver.domain.post.entity.Post;
import net.kosa.mentopingserver.global.common.enums.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository<Post, Long>, PostRepositoryCustom {
    
    Page<Post> findByMember(Member member, Pageable pageable);

    Page<Post> findByCategory(Category category, Pageable pageable);

}
