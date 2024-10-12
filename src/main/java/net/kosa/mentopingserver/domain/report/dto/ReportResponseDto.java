package net.kosa.mentopingserver.domain.report.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.kosa.mentopingserver.global.common.enums.ReportType;
import net.kosa.mentopingserver.global.common.enums.Status;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReportResponseDto {

    private Long id;
    private Long targetMemberId;    // 신고 당하는 사람
    private String reporterId;      // 신고 하는 사람
    private String reason;
    private Status status;
    private ReportType reportType;
}
