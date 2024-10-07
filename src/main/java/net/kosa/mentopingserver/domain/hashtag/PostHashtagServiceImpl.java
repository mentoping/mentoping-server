package net.kosa.mentopingserver.domain.hashtag;

import lombok.RequiredArgsConstructor;
import net.kosa.mentopingserver.domain.post.entity.Post;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.Set;
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
        Set<String> validHashtags = hashtagList.stream()
                .map(String::trim)
                .filter(s -> !s.isEmpty())
                .collect(Collectors.toSet());

        // 기존 해시태그 모두 삭제
        postHashtagRepository.deleteAll(previousHashtags);

        // 새로운 해시태그 저장
        validHashtags.forEach(hashtag -> saveHashtag(post, hashtag));
    }

    @Override
    public List<PostHashtag> getPostHashtags(Post post) {
        return postHashtagRepository.findAllByPost_Id(post.getId());
    }

    private PostHashtag saveHashtag(Post post, String hashtagStr) {
        Hashtag hashtag = hashtagService.save(hashtagStr);

        PostHashtag postHashtag = PostHashtag.builder()
                .post(post)
                .hashtag(hashtag)
                .build();
        return postHashtagRepository.save(postHashtag);
    }
}
