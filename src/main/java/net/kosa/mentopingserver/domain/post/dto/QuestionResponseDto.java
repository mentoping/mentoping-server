package net.kosa.mentopingserver.domain.post.dto;

import lombok.*;
import net.kosa.mentopingserver.domain.answer.dto.AnswerResponseDto;
import net.kosa.mentopingserver.domain.member.dto.AuthorDto;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(access = AccessLevel.PUBLIC)
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
    private List<AnswerResponseDto> answers;

}
