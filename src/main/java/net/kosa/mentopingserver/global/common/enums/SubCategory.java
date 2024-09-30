package net.kosa.mentopingserver.global.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum SubCategory {

    // 프로그래밍
    JAVA(MainCategory.PROGRAMMING, "자바"),
    PYTHON(MainCategory.PROGRAMMING, "파이썬"),
    JAVASCRIPT(MainCategory.PROGRAMMING, "자바스크립트"),
    MOBILE_APP_DEVELOPMENT(MainCategory.PROGRAMMING, "모바일앱개발"),
    WEB_DEVELOPMENT(MainCategory.PROGRAMMING, "웹개발"),

    // 디자인
    UI_UX(MainCategory.DESIGN, "UI/UX"),
    GRAPHIC_DESIGN(MainCategory.DESIGN, "그래픽디자인"),
    ILLUSTRATION(MainCategory.DESIGN, "일러스트레이션"),
    MOTION_GRAPHICS(MainCategory.DESIGN, "모션그래픽"),

    // 마케팅
    DIGITAL_MARKETING(MainCategory.MARKETING, "디지털마케팅"),
    CONTENT_MARKETING(MainCategory.MARKETING, "콘텐츠마케팅"),
    SEO(MainCategory.MARKETING, "검색엔진최적화"),
    SOCIAL_MEDIA_MARKETING(MainCategory.MARKETING, "소셜미디어마케팅"),

    // 비즈니스
    ENTREPRENEURSHIP(MainCategory.BUSINESS, "창업"),
    PROJECT_MANAGEMENT(MainCategory.BUSINESS, "프로젝트관리"),
    FINANCE(MainCategory.BUSINESS, "재무"),
    STRATEGY(MainCategory.BUSINESS, "전략");

    private final MainCategory mainCategory;
    private final String name;
}
