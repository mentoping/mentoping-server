package net.kosa.mentopingserver.domain.mentor.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MentorApplicantRequestDto {

    @NotBlank(message = "Member ID는 필수 항목입니다.")
    private Long memberId;

//    @NotBlank(message = "이름은 필수 항목입니다.")
//    private String name;

    @NotBlank(message = "전문분야 설명은 필수 항목입니다.")
    private String field;


    @NotBlank(message = "이름은 필수 항목입니다.")
    private MultipartFile certification_file;

    private String status; // 상태 추가

    private String review; // 리뷰 추가
}
