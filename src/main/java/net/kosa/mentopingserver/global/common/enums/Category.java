package net.kosa.mentopingserver.global.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Category {

    // 프로그래밍
    ITSW("it_sw"),
//    DESIGN("디자인"),
    DESIGN("DESIGN"),
    MARKETING("마케팅"),
    FINANCE("금융/은행/보험"),
    MD("MD/상품기획"),
    ADBERTISE("광고/홍보"),
    PRIDUCTION("생산/제조"),
    MEDIA("미디어"),
    ETC("기타");

    private final String name;
}
