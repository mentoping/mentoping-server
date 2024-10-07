package net.kosa.mentopingserver.domain.post;

import net.kosa.mentopingserver.domain.post.entity.Post;
import net.kosa.mentopingserver.global.common.enums.SubCategory;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class PostRepositoryCustomImplTest {

    @Autowired
    private PostRepository postRepository;

    @Test
    void testFindPostWithAnswersById() {
        // Given - 데이터 준비
        Post savedPost = postRepository.save(Post.builder()
                .title("Test Title")
                .content("Test Content")
                .category(SubCategory.JAVA)
                .build());

        // When - 메서드 호출
        Optional<Post> result = postRepository.findPostWithAnswersById(savedPost.getId());

        // Then - 검증
        assertThat(result).isPresent();
        assertThat(result.get().getTitle()).isEqualTo("Test Title");
        assertThat(result.get().getContent()).isEqualTo("Test Content");
        assertThat(result.get().getCategory()).isEqualTo(SubCategory.JAVA);
    }

}