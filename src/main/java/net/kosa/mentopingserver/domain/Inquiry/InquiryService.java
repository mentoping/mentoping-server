package net.kosa.mentopingserver.domain.Inquiry;

import net.kosa.mentopingserver.domain.Inquiry.dto.InquiryRequestDto;
import net.kosa.mentopingserver.domain.Inquiry.dto.InquiryResponseDto;
import net.kosa.mentopingserver.domain.Inquiry.dto.UserInquiryRequestDto;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

public interface InquiryService {

    // 문의 생성
    @Transactional
    InquiryResponseDto createInquiry(UserInquiryRequestDto inquiryRequestDto);


    // 모든 문의 조회 (페이지네이션 포함 가능)
    @Transactional
    List<InquiryResponseDto> getAllInquiries();

    // 특정 문의 조회
    @Transactional
    InquiryResponseDto getInquiryById(Long id);

    // 문의 삭제
    @Transactional
    void deleteInquiry(Long id);

    // 문의 답변 업데이트
    @Transactional
    InquiryResponseDto updateInquiryAnswer(Long id, Map<String, String> answerContentMap);

}
