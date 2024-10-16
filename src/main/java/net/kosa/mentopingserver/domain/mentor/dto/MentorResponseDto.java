package net.kosa.mentopingserver.domain.mentor.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.kosa.mentopingserver.domain.answer.dto.AnswerRequestDto;
import net.kosa.mentopingserver.domain.answer.dto.AnswerResponseDto;
import net.kosa.mentopingserver.global.common.enums.Category;
import net.kosa.mentopingserver.global.common.enums.MentorRank;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MentorResponseDto {

    private String name;                      // 멘토 이름
    private String introduction;              // 멘토 소개
    private String profileImage;              // 프로필 이미지 URL
    private Double expertise;                 // 전문성 점수 (exp)
    private Category category;                // 멘토링 활동 분야
    private String contact;                   // 연락처
    private String sns;                       // SNS 정보
    private MentorRank mentorRank;            // Dto에 필드 추가
//    private List<AnswerResponseDto> answer;    // 모든 답변
//    private List<AnswerResponseDto> answerSelected;  // 채택 답변
}
