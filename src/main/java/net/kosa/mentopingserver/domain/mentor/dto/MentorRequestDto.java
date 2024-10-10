package net.kosa.mentopingserver.domain.mentor.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.File;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MentorRequestDto {

    @NotBlank(message = "이름은 필수 항목입니다.")
    private String name;


    @NotBlank(message = "전화번호는 필수 항목입니다.")
    private String phoneNumber;


    @NotBlank(message = "전문분야 설명은 필수 항목입니다.")
    private String field;


    @NotBlank(message = "이름은 필수 항목입니다.")
    private File certification_file;
}
