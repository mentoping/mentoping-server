package net.kosa.mentopingserver.domain.post.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MentoringReviewDto {

    private Long id;

    private Long applicationId;

    private Long memberId;

    private String memberName;

    @NotNull(message = "Rate is required")
    @Min(value = 1, message = "Rate must be at least 1")
    @Max(value = 5, message = "Rate must be at most 5")
    private Integer rate;

    @Size(max = 1000, message = "Content must not exceed 1000 characters")
    private String content;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}