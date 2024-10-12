package net.kosa.mentopingserver.domain.Inquiry;

import lombok.RequiredArgsConstructor;
import net.kosa.mentopingserver.domain.Inquiry.dto.InquiryRequestDto;
import net.kosa.mentopingserver.domain.Inquiry.dto.InquiryResponseDto;
import net.kosa.mentopingserver.domain.report.dto.ReportResponseDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/inquiries")
public class InquiryController {

    private final InquiryService inquiryService;

    // 문의 생성
    @PostMapping
    public ResponseEntity<InquiryResponseDto> createInquiry(@RequestBody InquiryRequestDto inquiryRequestDto) {
        InquiryResponseDto createdInquiry = inquiryService.createInquiry(inquiryRequestDto);
        return ResponseEntity.ok(createdInquiry);
    }

    // 모든 문의 조회
//    @GetMapping
//    public ResponseEntity<List<InquiryResponseDto>> getAllInquiries() {
//        List<InquiryResponseDto> inquiries = inquiryService.getAllInquiries();
//        System.out.println("문희나가요 :: " + inquiries);
//        return ResponseEntity.ok(inquiries);
//    }

    @GetMapping
    public ResponseEntity<Map<String ,Object>> getAllInquiries() {
        List<InquiryResponseDto> inquiries = inquiryService.getAllInquiries();
        System.out.println("문희나가요 :: " + inquiries);

        Map<String, Object> response = new HashMap<>();
        response.put("inquiries", inquiries);

        return ResponseEntity.ok(response);
    }


    // 특정 문의 조회
    @GetMapping("/{id}")
    public ResponseEntity<InquiryResponseDto> getInquiryById(@PathVariable Long id) {
        InquiryResponseDto inquiry = inquiryService.getInquiryById(id);
        return ResponseEntity.ok(inquiry);
    }

    // 문의 삭제
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteInquiry(@PathVariable Long id) {
        inquiryService.deleteInquiry(id);
        return ResponseEntity.noContent().build();
    }

    // 문의 답변 업데이트
    @PutMapping("/{id}/answer")
    public ResponseEntity<InquiryResponseDto> updateInquiryAnswer(@PathVariable Long id, @RequestBody Map<String, String> answerContentMap) {
        InquiryResponseDto updatedInquiry = inquiryService.updateInquiryAnswer(id, answerContentMap);
        return ResponseEntity.ok(updatedInquiry);
    }

}
