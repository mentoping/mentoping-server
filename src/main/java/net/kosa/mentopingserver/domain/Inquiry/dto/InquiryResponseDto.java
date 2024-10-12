package net.kosa.mentopingserver.domain.Inquiry.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InquiryResponseDto {

    private Long id;
    private String userId;  // 작성자 ID
    private String userName; // 작성자 이름
    private String subject; // 문의 제목
    private String inquiryContent; // 문의 내역
    private String answerContent;  // 답변 내역
    private LocalDateTime createdAt;

}
