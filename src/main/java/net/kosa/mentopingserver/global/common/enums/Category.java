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
    ADBERTISE("ADBERTISE"),
    PRIDUCTION("PRIDUCTION"),
    MEDIA("MEDIA"),
    ETC("ETC");

    private final String name;
}
