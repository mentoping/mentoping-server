package net.kosa.mentopingserver.domain.post;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import net.kosa.mentopingserver.domain.post.entity.Post;
import net.kosa.mentopingserver.domain.post.entity.QPost;
import net.kosa.mentopingserver.global.common.enums.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
public class PostRepositoryCustomImpl implements PostRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public Optional<Post> findPostWithAnswersById(Long postId) {
        QPost post = QPost.post;
        return Optional.ofNullable(queryFactory
                .selectFrom(post)
                .leftJoin(post.answers).fetchJoin()
                .where(post.id.eq(postId))
                .fetchOne());
    }

    @Override
    public Page<Post> findByKeywords(List<String> keywords, Pageable pageable) {
        QPost post = QPost.post;

        List<Post> results = queryFactory
                .selectFrom(post)
                .where(keywordsContains(keywords))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        long total = queryFactory
                .select(post.count())
                .from(post)
                .where(keywordsContains(keywords))
                .fetchOne();

        return new PageImpl<>(results, pageable, total);
    }

    @Override
    public Page<Post> findByCategoryAndKeywords(Category category, List<String> keywords, Pageable pageable) {
        QPost post = QPost.post;

        List<Post> results = queryFactory
                .selectFrom(post)
                .where(post.category.eq(category)
                        .and(keywordsContains(keywords)))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        long total = queryFactory
                .select(post.count())
                .from(post)
                .where(post.category.eq(category)
                        .and(keywordsContains(keywords)))
                .fetchOne();

        return new PageImpl<>(results, pageable, total);
    }

    private BooleanExpression keywordsContains(List<String> keywords) {
        if (keywords == null || keywords.isEmpty()) {
            return null;
        }

        QPost post = QPost.post;
        BooleanExpression expression = null;
        for (String keyword : keywords) {
            BooleanExpression keywordCondition = post.title.contains(keyword)
                    .or(post.content.contains(keyword));
            expression = (expression == null) ? keywordCondition : expression.or(keywordCondition);
        }
        return expression;
    }

}
