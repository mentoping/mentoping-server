package net.kosa.mentopingserver.domain.post;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import net.kosa.mentopingserver.domain.hashtag.QHashtag;
import net.kosa.mentopingserver.domain.hashtag.QPostHashtag;
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
    public Page<Post> findAllQuestions(Pageable pageable) {
        QPost post = QPost.post;
        List<Post> results = queryFactory
                .selectFrom(post)
                .where(post.price.isNull())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        long total = queryFactory
                .select(post.count())
                .from(post)
                .where(post.price.isNull())
                .fetchOne();

        return new PageImpl<>(results, pageable, total);
    }

    @Override
    public Page<Post> findAllMentorings(Pageable pageable) {
        QPost post = QPost.post;
        List<Post> results = queryFactory
                .selectFrom(post)
                .where(post.price.isNotNull())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        long total = queryFactory
                .select(post.count())
                .from(post)
                .where(post.price.isNotNull())
                .fetchOne();

        return new PageImpl<>(results, pageable, total);
    }

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
    public Page<Post> findQuestionsByKeywords(List<String> keywords, Pageable pageable) {
        QPost post = QPost.post;
        QPostHashtag postHashtag = QPostHashtag.postHashtag;
        QHashtag hashtag = QHashtag.hashtag;

        List<Post> results = queryFactory
                .selectFrom(post)
                .leftJoin(post.postHashtags, postHashtag)
                .leftJoin(postHashtag.hashtag, hashtag)
                .where(post.price.isNull()
                        .and(keywordsContains(keywords)))
                .distinct()
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        long total = queryFactory
                .select(post.countDistinct())
                .from(post)
                .leftJoin(post.postHashtags, postHashtag)
                .leftJoin(postHashtag.hashtag, hashtag)
                .where(post.price.isNull()
                        .and(keywordsContains(keywords)))
                .fetchOne();

        return new PageImpl<>(results, pageable, total);
    }

    @Override
    public Page<Post> findQuestionsByCategoryAndKeywords(Category category, List<String> keywords, Pageable pageable) {
        QPost post = QPost.post;
        QPostHashtag postHashtag = QPostHashtag.postHashtag;
        QHashtag hashtag = QHashtag.hashtag;

        List<Post> results = queryFactory
                .selectFrom(post)
                .leftJoin(post.postHashtags, postHashtag)
                .leftJoin(postHashtag.hashtag, hashtag)
                .where(post.category.eq(category)
                        .and(post.price.isNull())
                        .and(keywordsContains(keywords)))
                .distinct()
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        long total = queryFactory
                .select(post.countDistinct())
                .from(post)
                .leftJoin(post.postHashtags, postHashtag)
                .leftJoin(postHashtag.hashtag, hashtag)
                .where(post.category.eq(category)
                        .and(post.price.isNull())
                        .and(keywordsContains(keywords)))
                .fetchOne();

        return new PageImpl<>(results, pageable, total);
    }

    @Override
    public Page<Post> findMentoringsByKeywords(List<String> keywords, Pageable pageable) {
        QPost post = QPost.post;
        QPostHashtag postHashtag = QPostHashtag.postHashtag;
        QHashtag hashtag = QHashtag.hashtag;

        List<Post> results = queryFactory
                .selectFrom(post)
                .leftJoin(post.postHashtags, postHashtag)
                .leftJoin(postHashtag.hashtag, hashtag)
                .where(post.price.isNotNull()
                        .and(keywordsContains(keywords)))
                .distinct()
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        long total = queryFactory
                .select(post.countDistinct())
                .from(post)
                .leftJoin(post.postHashtags, postHashtag)
                .leftJoin(postHashtag.hashtag, hashtag)
                .where(post.price.isNotNull()
                        .and(keywordsContains(keywords)))
                .fetchOne();

        return new PageImpl<>(results, pageable, total);
    }

    @Override
    public Page<Post> findMentoringsByCategoryAndKeywords(Category category, List<String> keywords, Pageable pageable) {
        QPost post = QPost.post;
        QPostHashtag postHashtag = QPostHashtag.postHashtag;
        QHashtag hashtag = QHashtag.hashtag;

        List<Post> results = queryFactory
                .selectFrom(post)
                .leftJoin(post.postHashtags, postHashtag)
                .leftJoin(postHashtag.hashtag, hashtag)
                .where(post.category.eq(category)
                        .and(post.price.isNotNull())
                        .and(keywordsContains(keywords)))
                .distinct()
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        long total = queryFactory
                .select(post.countDistinct())
                .from(post)
                .leftJoin(post.postHashtags, postHashtag)
                .leftJoin(postHashtag.hashtag, hashtag)
                .where(post.category.eq(category)
                        .and(post.price.isNotNull())
                        .and(keywordsContains(keywords)))
                .fetchOne();

        return new PageImpl<>(results, pageable, total);
    }

    private BooleanExpression keywordsContains(List<String> keywords) {
        if (keywords == null || keywords.isEmpty()) {
            return null;
        }

        QPost post = QPost.post;
        QHashtag hashtag = QHashtag.hashtag;
        BooleanExpression expression = null;
        for (String keyword : keywords) {
            BooleanExpression keywordCondition = post.title.like("%" + keyword + "%")
                    .or(post.content.like("%" + keyword + "%"))
                    .or(hashtag.name.like("%" + keyword + "%"));
            expression = (expression == null) ? keywordCondition : expression.or(keywordCondition);
        }
        return expression;
    }
}
