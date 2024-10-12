package net.kosa.mentopingserver.domain.report;


import lombok.RequiredArgsConstructor;
import net.kosa.mentopingserver.domain.member.MemberRepository;
import net.kosa.mentopingserver.domain.report.Report;
import net.kosa.mentopingserver.domain.report.dto.ReportRequestDto;
import net.kosa.mentopingserver.domain.report.dto.ReportResponseDto;
import net.kosa.mentopingserver.domain.report.ReportRepository;
import net.kosa.mentopingserver.domain.report.ReportService;
import net.kosa.mentopingserver.domain.member.entity.Member;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReportServiceImpl implements ReportService {

    private final ReportRepository reportRepository;

    private final MemberRepository memberRepository;

    // 신고 생성하기
    @Override
    @Transactional
    public ReportResponseDto createReport(ReportRequestDto reportRequestDto) {
        Member targetMember = memberRepository.findById(reportRequestDto.getTargetMemberId())
                .orElseThrow(() -> new IllegalArgumentException("Target member not found with id: " + reportRequestDto.getTargetMemberId()));

        Report report = Report.builder()
                .reporterId(reportRequestDto.getReporterId())
                .targetMember(targetMember)
                .reason(reportRequestDto.getReason())
                .reportType(reportRequestDto.getReportType())
                .status(reportRequestDto.getStatus())
                .build();

        Report savedReport = reportRepository.save(report);
        return convertToResponseDto(savedReport);

    }

    // 신고 모두 보기
    @Override
    @Transactional(readOnly = true)
    public List<ReportResponseDto> getAllReports() {
        return reportRepository.findAll().stream()
                .map(this::convertToResponseDto)
                .collect(Collectors.toList());
    }

    // 신고자 ID로 특정 신고만 보기
    @Override
    @Transactional(readOnly = true)
    public ReportResponseDto getReportById(Long reportId) {
        Optional<Report> report = reportRepository.findById(reportId);
        return report.map(this::convertToResponseDto)
                .orElseThrow(() -> new IllegalArgumentException("Report not found with id: " + reportId));
    }

    // 신고 지우기
    @Override
    @Transactional
    public void deleteReport(Long reportId) {
        if (!reportRepository.existsById(reportId)) {
            throw new IllegalArgumentException("Report not found with id: " + reportId);
        }
        reportRepository.deleteById(reportId);
    }

    // 신고자 ID로 신고 조회 ( 마이 페이지에서 내가 작성한 신고 내역 조회할 때 사용)
    @Override
    @Transactional
    public List<ReportResponseDto> getReportsByReporterId(Long reporterId) {
        return reportRepository.findByReporterId(reporterId).stream()
                .map(this::convertToResponseDto)
                .collect(Collectors.toList());
    }

    private ReportResponseDto convertToResponseDto(Report report) {

        Member targetmember = memberRepository.findById(report.getTargetMember().getId())
                .orElseThrow(() -> new IllegalArgumentException("Member not found"));

        Member reportermember = memberRepository.findById(report.getReporterId())
                .orElseThrow(() -> new IllegalArgumentException("Member not found"));

        return ReportResponseDto.builder()
                .id(report.getId())
                .targetMemberId(report.getTargetMember().getId())  // 신고 당하는 사람
                .targetMemberName(targetmember.getName()) // userName 추가
                .reporterId(report.getReporterId())                // 신고 하는 사람
                .reporterName(reportermember.getName())
                .reason(report.getReason())
                .status(report.getStatus())
                .reportType(report.getReportType())
                .createdAt(report.getCreatedAt())
                .build();
    }
}