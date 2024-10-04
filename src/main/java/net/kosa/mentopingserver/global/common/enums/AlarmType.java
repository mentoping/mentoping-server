package net.kosa.mentopingserver.global.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum AlarmType {

    NEW_MESSAGE("새 메시지", "새로운 메시지가 도착했습니다"),
    MENTORING_REQUEST("멘토링 요청", "새로운 멘토링 요청이 있습니다"),
    MENTORING_ACCEPTED("멘토링 수락", "멘토링 요청이 수락되었습니다"),
    MENTORING_REJECTED("멘토링 거절", "멘토링 요청이 거절되었습니다"),
    NEW_ANSWER("새 답변", "질문에 새로운 답변이 달렸습니다"),
    QUESTION_LIKED("질문 좋아요", "당신의 질문에 좋아요가 추가되었습니다"),
    ANSWER_ACCEPTED("답변 채택", "당신의 답변이 채택되었습니다"),
    SYSTEM_NOTICE("시스템 공지", "시스템 공지사항이 있습니다");

    private final String korName;
    private final String description;
}
