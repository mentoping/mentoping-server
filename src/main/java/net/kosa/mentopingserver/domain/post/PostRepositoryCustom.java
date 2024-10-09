package net.kosa.mentopingserver.domain.post;

import net.kosa.mentopingserver.domain.post.entity.Post;
import net.kosa.mentopingserver.global.common.enums.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface PostRepositoryCustom {
    Optional<Post> findPostWithAnswersById(Long postId);

    Page<Post> findByKeywords(List<String> keywords, Pageable pageable);

    Page<Post> findByCategoryAndKeywords(Category category, List<String> keywords, Pageable pageable);
}
