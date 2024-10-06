package net.kosa.mentopingserver.domain.post;

import net.kosa.mentopingserver.domain.post.entity.Post;

import java.util.Optional;

public interface PostRepositoryCustom {
    Optional<Post> findPostWithAnswersById(Long postId);

}
