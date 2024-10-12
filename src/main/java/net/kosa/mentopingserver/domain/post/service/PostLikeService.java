package net.kosa.mentopingserver.domain.post.service;

import net.kosa.mentopingserver.domain.post.entity.Post;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

public interface PostLikeService {
    @Transactional
    void addLike(Long postId, Long memberId);

    @Transactional
    void removeLike(Long postId, Long memberId);

    @Transactional(readOnly = true)
    boolean hasUserLikedPost(Long postId, Long memberId);

    @Transactional(readOnly = true)
    List<Post> getLikedQuestionsByMember(Long memberId, Pageable pageable);

    @Transactional(readOnly = true)
    List<Post> getLikedMentoringsByMember(Long memberId, Pageable pageable);

    @Transactional(readOnly = true)
    Map<Long, Boolean> batchToggleLike(List<Long> postIds, Long memberId);
}
