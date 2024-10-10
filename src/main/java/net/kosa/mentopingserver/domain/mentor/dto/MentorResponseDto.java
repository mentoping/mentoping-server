package net.kosa.mentopingserver.domain.mentor.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MentorResponseDto {
    private Long applicationId;
    private String status;
    private String submittedAt;
    private String reviewedAt;
    private String review;
    private String field;
}