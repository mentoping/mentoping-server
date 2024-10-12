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

import java.util.List;

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
}
