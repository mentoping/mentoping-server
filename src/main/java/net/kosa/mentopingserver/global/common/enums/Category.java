package net.kosa.mentopingserver.global.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Category {

    ITSW("ITSW"),
    DESIGN("DESIGN"),
    MARKETING("MARKETING"),
    FINANCE("FINANCE"),
    MD("MD"),
    ADVERTISE("ADVERTISE"),
    PRODUCTION("PRODUCTION"),
    MEDIA("MEDIA"),
    ETC("ETC");

    private final String name;
}
