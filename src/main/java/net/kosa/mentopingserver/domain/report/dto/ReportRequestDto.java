package net.kosa.mentopingserver.domain.report.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Builder;
import net.kosa.mentopingserver.global.common.enums.ReportType;
import net.kosa.mentopingserver.global.common.enums.Status;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReportRequestDto {
    private Long targetMemberId;  // 신고 당하는 멤버의 ID
    private String reporterId;    // 신고하는 사람의 ID
    private String reason;        // 신고 이유
    private Status status;        // 신고 상태
    private ReportType reportType; // 신고 유형

    public Long reportId;
}
