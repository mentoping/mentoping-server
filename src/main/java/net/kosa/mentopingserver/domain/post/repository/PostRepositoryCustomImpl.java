package net.kosa.mentopingserver.domain.post.repository;

import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
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
import org.springframework.data.domain.Sort;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
public class PostRepositoryCustomImpl implements PostRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public Page<Post> findAllQuestions(Pageable pageable) {
        QPost post = QPost.post;
        JPAQuery<Post> query = queryFactory
                .selectFrom(post)
                .where(post.price.isNull());

        return getPagedResult(query, pageable, post);
    }

    @Override
    public Page<Post> findAllMentorings(Pageable pageable) {
        QPost post = QPost.post;
        JPAQuery<Post> query = queryFactory
                .selectFrom(post)
                .where(post.price.isNotNull());

        return getPagedResult(query, pageable, post);
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

        JPAQuery<Post> query = queryFactory
                .selectFrom(post)
                .leftJoin(post.postHashtags, postHashtag)
                .leftJoin(postHashtag.hashtag, hashtag)
                .where(post.price.isNull()
                        .and(keywordsContains(keywords)))
                .distinct();

        return getPagedResult(query, pageable, post);
    }

    @Override
    public Page<Post> findQuestionsByCategoryAndKeywords(Category category, List<String> keywords, Pageable pageable) {
        QPost post = QPost.post;
        QPostHashtag postHashtag = QPostHashtag.postHashtag;
        QHashtag hashtag = QHashtag.hashtag;

        JPAQuery<Post> query = queryFactory
                .selectFrom(post)
                .leftJoin(post.postHashtags, postHashtag)
                .leftJoin(postHashtag.hashtag, hashtag)
                .where(post.category.eq(category)
                        .and(post.price.isNull())
                        .and(keywordsContains(keywords)))
                .distinct();

        return getPagedResult(query, pageable, post);
    }

    @Override
    public Page<Post> findMentoringsByKeywords(List<String> keywords, Pageable pageable) {
        QPost post = QPost.post;
        QPostHashtag postHashtag = QPostHashtag.postHashtag;
        QHashtag hashtag = QHashtag.hashtag;

        JPAQuery<Post> query = queryFactory
                .selectFrom(post)
                .leftJoin(post.postHashtags, postHashtag)
                .leftJoin(postHashtag.hashtag, hashtag)
                .where(post.price.isNotNull()
                        .and(keywordsContains(keywords)))
                .distinct();

        return getPagedResult(query, pageable, post);
    }

    @Override
    public Page<Post> findMentoringsByCategoryAndKeywords(Category category, List<String> keywords, Pageable pageable) {
        QPost post = QPost.post;
        QPostHashtag postHashtag = QPostHashtag.postHashtag;
        QHashtag hashtag = QHashtag.hashtag;

        JPAQuery<Post> query = queryFactory
                .selectFrom(post)
                .leftJoin(post.postHashtags, postHashtag)
                .leftJoin(postHashtag.hashtag, hashtag)
                .where(post.category.eq(category)
                        .and(post.price.isNotNull())
                        .and(keywordsContains(keywords)))
                .distinct();

        return getPagedResult(query, pageable, post);
    }

    private Page<Post> getPagedResult(JPAQuery<Post> query, Pageable pageable, QPost post) {
        List<Post> results = query
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(getOrderSpecifier(pageable, post))
                .fetch();

        long total = query.fetchCount();

        return new PageImpl<>(results, pageable, total);
    }

    private OrderSpecifier<?>[] getOrderSpecifier(Pageable pageable, QPost post) {
        List<OrderSpecifier<?>> orderSpecifiers = new ArrayList<>();
        if (pageable.getSort().isSorted()) {
            for (Sort.Order order : pageable.getSort()) {
                OrderSpecifier<?> orderSpecifier = getOrderSpecifier(order, post);
                if (orderSpecifier != null) {
                    orderSpecifiers.add(orderSpecifier);
                }
            }
        }
        if (orderSpecifiers.isEmpty()) {
            orderSpecifiers.add(post.createdAt.desc()); // 기본 정렬
        }
        return orderSpecifiers.toArray(new OrderSpecifier[0]);
    }

    private OrderSpecifier<?> getOrderSpecifier(Sort.Order order, QPost post) {
        Order direction = order.isAscending() ? Order.ASC : Order.DESC;
        switch (order.getProperty()) {
            case "createdAt":
                return new OrderSpecifier<>(direction, post.createdAt);
            case "likeCount":
                return new OrderSpecifier<>(direction, post.likeCount);
            case "answerCount":
                return new OrderSpecifier<>(direction, post.answerCount);
            // 필요한 경우 다른 정렬 기준 추가
            default:
                return null;
        }
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
