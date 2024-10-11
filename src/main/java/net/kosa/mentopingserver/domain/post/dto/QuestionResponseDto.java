package net.kosa.mentopingserver.domain.post.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.kosa.mentopingserver.domain.member.dto.AuthorDto;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class QuestionResponseDto {

    private Long id;
    private String title;
    private String content;
    private AuthorDto author;
    private LocalDateTime createdAt;
    private String category;
    private List<String> hashtags;
    private int likeCount;
    private int answerCount;
    private boolean isSelected;

}
