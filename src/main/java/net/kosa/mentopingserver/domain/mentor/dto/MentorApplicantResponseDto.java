package net.kosa.mentopingserver.domain.mentor.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MentorApplicantResponseDto {

    private String applicationId;
    private String status;
    private String submittedAt;

}
