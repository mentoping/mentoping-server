package net.kosa.mentopingserver.domain.report;

import net.kosa.mentopingserver.domain.report.dto.ReportRequestDto;
import net.kosa.mentopingserver.domain.report.dto.ReportResponseDto;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface ReportService {

    @Transactional
    ReportResponseDto createReport(ReportRequestDto reportRequestDto);

    @Transactional(readOnly = true)
    List<ReportResponseDto> getAllReports();

    @Transactional(readOnly = true)
    ReportResponseDto getReportById(Long reportId);

    @Transactional
    void deleteReport(Long reportId);

    @Transactional
    List<ReportResponseDto> getReportsByReporterId(String reporterId);


}
