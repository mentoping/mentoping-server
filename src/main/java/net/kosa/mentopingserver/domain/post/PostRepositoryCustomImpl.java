package net.kosa.mentopingserver.domain.post;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import lombok.RequiredArgsConstructor;
import net.kosa.mentopingserver.domain.post.entity.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
public class PostRepositoryCustomImpl implements PostRepositoryCustom {

    @PersistenceContext
    private final EntityManager entityManager;

    @Override
    public Optional<Post> findPostWithAnswersById(Long postId) {
        TypedQuery<Post> query = entityManager.createQuery(
                "SELECT p FROM Post p LEFT JOIN FETCH p.answers WHERE p.id = :postId", Post.class);
        query.setParameter("postId", postId);
        List<Post> results = query.getResultList();
        return results.stream().findFirst();
    }
}
