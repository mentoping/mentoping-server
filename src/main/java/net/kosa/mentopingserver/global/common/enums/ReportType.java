package net.kosa.mentopingserver.global.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ReportType {
    QUESTION("질문", "사용자가 올린 질문"),
    ANSWER("답변", "질문에 대한 답변"),
    MENTORING("멘토링", "멘토링 세션 또는 멘토링 관련 컨텐츠"),
    CHAT("채팅", "채팅 메시지"),
    USER_PROFILE("사용자 프로필", "사용자의 프로필 정보"),
    POST("게시물", "일반 게시판의 게시물"),
    REVIEW("리뷰", "멘토링 후기 또는 평가");

    private final String korName;
    private final String description;

}
