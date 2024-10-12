package net.kosa.mentopingserver.domain.post.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MentoringApplicationRequestDto {

    @NotBlank(message = "연락처를 입력해주세요.")
    private String contact;

    @NotBlank(message = "이메일을 입력해주세요.")
    @Email(message = "유효한 이메일 주소를 입력해주세요.")
    private String email;

    @NotBlank(message = "지원 내용을 입력해주세요.")
    private String content;
}
