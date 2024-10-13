package net.kosa.mentopingserver.domain.post.service;

import net.kosa.mentopingserver.domain.post.dto.MentoringResponseDto;
import net.kosa.mentopingserver.domain.post.dto.QuestionResponseDto;
import net.kosa.mentopingserver.domain.post.entity.Post;
import org.springframework.data.domain.Page;
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
    Map<Long, Boolean> batchToggleLike(List<Long> postIds, Long memberId);

    @Transactional
    Page<QuestionResponseDto> getLikedQuestions(Long memberId, Pageable pageable);

    @Transactional
    Page<MentoringResponseDto> getLikedMentorings(Long memberId, Pageable pageable);
}
