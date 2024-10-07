package net.kosa.mentopingserver.global.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Category {

    // 프로그래밍
    JAVA("자바"),
    PYTHON("파이썬"),
    JAVASCRIPT("자바스크립트"),
    MOBILE_APP_DEVELOPMENT("모바일앱개발"),
    WEB_DEVELOPMENT("웹개발"),

    // 디자인
    UI_UX("UI/UX"),
    GRAPHIC_DESIGN("그래픽디자인"),
    ILLUSTRATION("일러스트레이션"),
    MOTION_GRAPHICS("모션그래픽"),

    // 마케팅
    DIGITAL_MARKETING("디지털마케팅"),
    CONTENT_MARKETING("콘텐츠마케팅"),
    SEO("검색엔진최적화"),
    SOCIAL_MEDIA_MARKETING("소셜미디어마케팅"),

    // 비즈니스
    ENTREPRENEURSHIP( "창업"),
    PROJECT_MANAGEMENT("프로젝트관리"),
    FINANCE("재무"),
    STRATEGY("전략");

    private final String name;
}
