package net.kosa.mentopingserver.domain.post.dto;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.kosa.mentopingserver.global.common.enums.Category;
import org.springframework.web.ErrorResponse;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MentoringRequestDto {

    @NotBlank(message = "제목은 필수 입력 항목입니다.")
    @Size(max = 255, message = "제목은 255자를 초과할 수 없습니다.")
    private String title;

    @NotBlank(message = "내용은 필수 입력 항목입니다.")
    private String content;

    @NotNull(message = "카테고리는 필수 선택 항목입니다.")
    private Category category;

    @Size(max = 3, message = "해시태그는 최대 3개까지만 입력 가능합니다.")
    private List<@Size(max = 20, message = "각 해시태그는 20자를 초과할 수 없습니다.") String> hashtags;

    @NotNull(message = "가격은 필수 입력 항목입니다.")
    @PositiveOrZero(message = "가격은 0 또는 양수이어야 합니다.")
    private Long price;

    @Nullable
    private MultipartFile thumbnailUrl;

    @NotBlank(message = "요약은 필수 입력 항목입니다.")
    @Size(max = 500, message = "요약은 500자를 초과할 수 없습니다.")
    private String summary;
}
