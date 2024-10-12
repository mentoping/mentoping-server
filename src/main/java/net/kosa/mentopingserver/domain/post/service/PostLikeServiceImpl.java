package net.kosa.mentopingserver.domain.post.service;

import lombok.RequiredArgsConstructor;
import net.kosa.mentopingserver.domain.member.entity.Member;
import net.kosa.mentopingserver.domain.member.MemberRepository;
import net.kosa.mentopingserver.domain.post.entity.Post;
import net.kosa.mentopingserver.domain.post.entity.PostLikes;
import net.kosa.mentopingserver.domain.post.repository.PostLikesRepository;
import net.kosa.mentopingserver.domain.post.repository.PostRepository;
import net.kosa.mentopingserver.global.exception.MemberNotFoundException;
import net.kosa.mentopingserver.global.exception.PostNotFoundException;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PostLikeServiceImpl implements PostLikeService {

    private final PostLikesRepository postLikesRepository;
    private final PostRepository postRepository;
    private final MemberRepository memberRepository;

    @Override
    @Transactional
    public void addLike(Long postId, Long memberId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new PostNotFoundException("Post not found with id: " + postId));
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new MemberNotFoundException("Member not found with id: " + memberId));

        if (!postLikesRepository.existsByPostAndMember(post, member)) {
            post.incrementLikeCount();
            postLikesRepository.save(PostLikes.builder().post(post).member(member).build());
        }
    }

    @Override
    @Transactional
    public void removeLike(Long postId, Long memberId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new PostNotFoundException("Post not found with id: " + postId));
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new MemberNotFoundException("Member not found with id: " + memberId));

        PostLikes postLike = postLikesRepository.findByPostAndMember(post, member)
                .orElseThrow(() -> new RuntimeException("Like not found"));

        post.decrementLikeCount();
        postLikesRepository.delete(postLike);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean hasUserLikedPost(Long postId, Long memberId) {
        return postLikesRepository.existsByPostIdAndMemberId(postId, memberId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Post> getLikedQuestionsByMember(Long memberId, Pageable pageable) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new MemberNotFoundException("Member not found with id: " + memberId));

        return postLikesRepository.findLikedQuestionsByMember(member, pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Post> getLikedMentoringsByMember(Long memberId, Pageable pageable) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new MemberNotFoundException("Member not found with id: " + memberId));

        return postLikesRepository.findLikedMentoringsByMember(member, pageable);
    }

    @Override
    @Transactional
    public Map<Long, Boolean> batchToggleLike(List<Long> postIds, Long memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new MemberNotFoundException("Member not found with id: " + memberId));

        List<Post> posts = postRepository.findAllById(postIds);
        if (posts.size() != postIds.size()) {
            throw new PostNotFoundException("One or more posts not found");
        }

        Map<Long, Integer> toggleCounts = new HashMap<>();
        for (Long postId : postIds) {
            toggleCounts.put(postId, toggleCounts.getOrDefault(postId, 0) + 1);
        }

        Set<Long> initialLikedPostIds = postLikesRepository.findExistingLikePostIds(member.getId(), new HashSet<>(postIds));

        Map<Long, Boolean> result = new HashMap<>();

        for (Post post : posts) {
            int toggleCount = toggleCounts.get(post.getId());
            boolean wasInitiallyLiked = initialLikedPostIds.contains(post.getId());
            boolean isFinallyLiked = (wasInitiallyLiked && toggleCount % 2 == 0) || (!wasInitiallyLiked && toggleCount % 2 == 1);

            if (isFinallyLiked != wasInitiallyLiked) {
                if (isFinallyLiked) {
                    postLikesRepository.save(PostLikes.builder().post(post).member(member).build());
                    post.incrementLikeCount();
                } else {
                    postLikesRepository.deleteByPostAndMember(post, member);
                    post.decrementLikeCount();
                }
            }

            result.put(post.getId(), isFinallyLiked);
        }

        postRepository.saveAll(posts);

        return result;
    }
}
