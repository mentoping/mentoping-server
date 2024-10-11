package net.kosa.mentopingserver.domain.post;

import net.kosa.mentopingserver.domain.post.entity.Post;
import net.kosa.mentopingserver.global.common.enums.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface PostRepositoryCustom {
    Page<Post> findAllQuestions(Pageable pageable);

    Page<Post> findAllMentorings(Pageable pageable);

    Optional<Post> findPostWithAnswersById(Long postId);

    Page<Post> findQuestionsByKeywords(List<String> keywords, Pageable pageable);

    Page<Post> findQuestionsByCategoryAndKeywords(Category category, List<String> keywords, Pageable pageable);

    Page<Post> findMentoringsByKeywords(List<String> keywords, Pageable pageable);

    Page<Post> findMentoringsByCategoryAndKeywords(Category category, List<String> keywords, Pageable pageable);
}
