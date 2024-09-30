package net.kosa.mentopingserver.global.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Role {
    ROLE_ADMIN("관리자"), ROLE_MENTEE("멘티"), ROLE_MENTOR("멘토"), ROLE_MENTORING("멘토링");

    private final String value;
}
