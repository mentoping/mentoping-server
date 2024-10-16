package net.kosa.mentopingserver.domain.post.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.kosa.mentopingserver.domain.member.dto.AuthorDto;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MentoringResponseDto {

    private Long id;
    private String title;
    private String content;
    private String thumbnailUrl;
    private String summary;
    private AuthorDto author;
    private LocalDateTime createdAt;
    private String category;
    private List<String> hashtags;
    private int likeCount;
    private boolean isActive;
    private Long price;
    private boolean isLikedByCurrentUser;
    private Double averageRating;
    private List<MentoringReviewDto> reviews;
}
