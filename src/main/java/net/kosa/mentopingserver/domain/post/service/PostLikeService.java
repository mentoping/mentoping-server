package net.kosa.mentopingserver.domain.post.service;

import org.springframework.transaction.annotation.Transactional;

public interface PostLikeService {
    @Transactional
    void addLike(Long postId, Long memberId);

    @Transactional
    void removeLike(Long postId, Long memberId);

    @Transactional(readOnly = true)
    boolean hasUserLikedPost(Long postId, Long memberId);
}
