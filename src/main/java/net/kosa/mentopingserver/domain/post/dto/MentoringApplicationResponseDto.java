package net.kosa.mentopingserver.domain.post.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.kosa.mentopingserver.global.common.enums.Status;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MentoringApplicationResponseDto {
    private Long id;
    private Long postId;
    private String postTitle;
    private Long memberId;
    private String memberName;
    private String contact;
    private String email;
    private String content;
    private Status status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

}
