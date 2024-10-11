package net.kosa.mentopingserver.domain.answer.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AnswerRequestDto {

    @NotBlank(message = "내용을 채워주세요.")
    private String content;

    @NotNull(message = "Post ID가 필요합니다.")
    private Long postId;

    private String review;

    private Boolean isSelected;
}


