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

    @NotBlank(message = "Content cannot be blank")
    private String content;

    @NotNull(message = "Post ID must be provided")
    private Long postId;
}


