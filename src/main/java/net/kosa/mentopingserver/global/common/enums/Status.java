package net.kosa.mentopingserver.global.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Status {

    PENDING("대기중"),
    APPROVED("승인됨"),
    REJECTED("거절됨");

    private final String description;
}
