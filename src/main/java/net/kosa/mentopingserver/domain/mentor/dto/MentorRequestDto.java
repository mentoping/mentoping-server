package net.kosa.mentopingserver.domain.mentor.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.kosa.mentopingserver.global.common.enums.Category;
import net.kosa.mentopingserver.global.common.enums.MentorRank;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MentorRequestDto {

    private Long memberId;
    private String introduction;              // 멘토 소개
    private Double expertise;                 // 전문성 점수
    private Category category;                // 멘토링 활동 분야
    private String contact;                   // 연락처
    private String sns;                       // SNS 정보
    private String profileImage;              // 프로필 이미지 URL}
    private MentorRank mentorRank;            // Dto에 필드 추가

}