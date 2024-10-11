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


    // 파일을 전송받기 위한 필드 (MultipartFile)
    private MultipartFile certification_file;

    // 업로드된 파일의 URL을 저장하기 위한 필드
    private String certificationFileUrl;

    private String status; // 상태 추가

    private String review; // 리뷰 추가
}
