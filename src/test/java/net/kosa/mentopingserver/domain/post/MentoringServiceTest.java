package net.kosa.mentopingserver.domain.post;

import net.kosa.mentopingserver.domain.post.dto.MentoringRequestDto;
import net.kosa.mentopingserver.domain.post.dto.MentoringResponseDto;
import net.kosa.mentopingserver.domain.post.service.MentoringService;
import net.kosa.mentopingserver.global.common.enums.Category;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.stream.IntStream;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
//@Transactional
public class MentoringServiceTest {

    @Autowired
    private MentoringService mentoringService;

    @Test
    public void testCreate20Mentorings() {
        // 20개의 mentoring 게시글 생성
        List<MentoringResponseDto> createdMentorings = IntStream.range(0, 20)
                .mapToObj(i -> {
                    MentoringRequestDto requestDto = MentoringRequestDto.builder()
                            .title("Mentoring Title " + i)
                            .content("Mentoring Content " + i)
                            .category(Category.values()[i % Category.values().length])
                            .hashtags(Arrays.asList("tag1", "tag2", "tag3"))
                            .price((long) (i * 1000))
                            .thumbnailUrl(i % 2 == 0 ? "https://example.com/thumbnail" + i + ".jpg" : null)
                            .summary("Summary of mentoring " + i)
                            .build();
                    return mentoringService.createMentoring(requestDto, 5L); // memberId를 1L로 가정
                })
                .toList();

        // 생성된 mentoring 게시글 검증
        assertEquals(20, createdMentorings.size());

        createdMentorings.forEach(mentoring -> {
            assertNotNull(mentoring.getId());
            assertNotNull(mentoring.getTitle());
            assertNotNull(mentoring.getContent());
            assertNotNull(mentoring.getCategory());
            assertNotNull(mentoring.getHashtags());
            assertEquals(3, mentoring.getHashtags().size());
            assertNotNull(mentoring.getPrice());
            assertTrue(mentoring.getPrice() >= 0);
            assertNotNull(mentoring.getSummary());
            // thumbnailUrl은 null일 수 있으므로 검증하지 않습니다.
        });

        // 데이터베이스에서 모든 mentoring 게시글 조회 (페이징 없이)
        List<MentoringResponseDto> allMentorings = mentoringService.getAllMentorings(null, null).getContent();

        // 조회된 게시글 수 확인
        assertEquals(20, allMentorings.size());

        // 몇 개의 게시글을 샘플로 상세 검증
        IntStream.of(0, 10, 19).forEach(i -> {
            MentoringResponseDto mentoring = allMentorings.get(i);
            assertEquals("Mentoring Title " + i, mentoring.getTitle());
            assertEquals("Mentoring Content " + i, mentoring.getContent());
            assertEquals(Category.values()[i % Category.values().length], mentoring.getCategory());
            assertEquals(Arrays.asList("tag1", "tag2", "tag3"), mentoring.getHashtags());
            assertEquals((long) (i * 1000), mentoring.getPrice());
            assertEquals(i % 2 == 0 ? "https://example.com/thumbnail" + i + ".jpg" : null, mentoring.getThumbnailUrl());
            assertEquals("Summary of mentoring " + i, mentoring.getSummary());
        });
    }
}