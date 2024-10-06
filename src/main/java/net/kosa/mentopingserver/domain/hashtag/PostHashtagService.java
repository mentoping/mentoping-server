package net.kosa.mentopingserver.domain.hashtag;

import net.kosa.mentopingserver.domain.post.entity.Post;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface PostHashtagService {
    @Transactional
    void setHashtag(Post post, List<String> hashtagList);

    List<PostHashtag> getPostHashtags(Post post);
}
