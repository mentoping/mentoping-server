package net.kosa.mentopingserver.domain.Inquiry.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserInquiryRequestDto {
    private String userId;  // 작성자 ID
    private String subject; // 문의 제목
    private String inquiryContent; // 문의 내역
}
