package net.kosa.mentopingserver.domain.report;

import lombok.RequiredArgsConstructor;
import net.kosa.mentopingserver.domain.report.dto.ReportRequestDto;
import net.kosa.mentopingserver.domain.report.dto.ReportResponseDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/admin/reports")
@RequiredArgsConstructor
public class ReportController {

    private final ReportService reportService;

    // 신고 생성하기
    @PostMapping
    public ResponseEntity<ReportResponseDto> createReport(@RequestBody ReportRequestDto reportRequestDto) {
        ReportResponseDto createdReport = reportService.createReport(reportRequestDto);
        return ResponseEntity.ok(createdReport);
    }

    // 모든 신고 보기
    @GetMapping
    public ResponseEntity<Map<String, Object>> getAllReports() {
        List<ReportResponseDto> reports = reportService.getAllReports();
        System.out.println("신고 보내요 :: " + reports);

        Map<String, Object> response = new HashMap<>();
        response.put("reports", reports);

        return ResponseEntity.ok(response);
    }

    // 신고 ID ( 신고테이블 ID) 특정 신고 보기
    @GetMapping("/{id}")
    public ResponseEntity<ReportResponseDto> getReportById(@PathVariable Long id) {
        ReportResponseDto report = reportService.getReportById(id);
        return ResponseEntity.ok(report);
    }

    // 신고자 ID 로 특정 신고 보기
    @PostMapping("/report")
    public ResponseEntity<List<ReportResponseDto>> getReportsByReporterId(@RequestBody ReportRequestDto reportRequestDto) {
        List<ReportResponseDto> reports = reportService.getReportsByReporterId(reportRequestDto.getReporterId());
        return ResponseEntity.ok(reports);
    }

    // 신고 삭제하기
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteReport(@PathVariable Long id) {
        reportService.deleteReport(id);
        return ResponseEntity.noContent().build();
    }
}
