package net.kosa.mentopingserver.domain.answer.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.kosa.mentopingserver.domain.member.dto.AuthorDto;
import net.kosa.mentopingserver.domain.member.dto.MemberDto;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AnswerResponseDto {

    private Long id;
    private String content;
    private Boolean isSelected;
    private String selectedReview;
    private AuthorDto author;
    private Long postId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
