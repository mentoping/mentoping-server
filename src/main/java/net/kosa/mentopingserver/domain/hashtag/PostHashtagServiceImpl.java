package net.kosa.mentopingserver.domain.hashtag;

import lombok.RequiredArgsConstructor;
import net.kosa.mentopingserver.domain.post.entity.Post;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PostHashtagServiceImpl implements PostHashtagService {

    private final HashtagService hashtagService;
    private final PostHashtagRepository postHashtagRepository;

    @Transactional
    @Override
    public void setHashtag(Post post, List<String> hashtagList) {
        // 이전에 등록된 해시태그들 가져오기
        List<PostHashtag> previousHashtags = getPostHashtags(post);

        // 유효한 해시태그 리스트 필터링 (빈 값 제외)
        List<String> validHashtags = hashtagList.stream()
                .map(String::trim)
                .filter(s -> !s.isEmpty())
                .toList();

        // 삭제할 해시태그 목록 생성 (기존에 있는데 새로운 리스트에 없는 해시태그들)
        List<PostHashtag> needToDelete = previousHashtags.stream()
                .filter(preHashtag -> !validHashtags.contains(preHashtag.getHashtag().getName()))
                .collect(Collectors.toList());

        // 새로운 해시태그 저장
        validHashtags.forEach(hashtag -> saveHashtag(post, hashtag));

        // 더 이상 포스트와 연결되지 않은 해시태그 삭제
        postHashtagRepository.deleteAll(needToDelete);
    }

    @Override
    public List<PostHashtag> getPostHashtags(Post post) {
        return postHashtagRepository.findAllByPost_Id(post.getId());
    }

    private PostHashtag saveHashtag(Post post, String hashtagStr) {
        Hashtag hashtag = hashtagService.save(hashtagStr);

        return postHashtagRepository.findByPost_IdAndHashtag_Id(post.getId(), hashtag.getId())
                .orElseGet(() -> {
                    PostHashtag postHashtag = PostHashtag.builder()
                            .post(post)
                            .hashtag(hashtag)
                            .build();
                    return postHashtagRepository.save(postHashtag);
                });
    }
}
