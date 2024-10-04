package net.kosa.mentopingserver.global.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum EntrySource {

    ORGANIC_SEARCH("유기적 검색", "검색 엔진을 통한 자연스러운 유입"),
    PAID_SEARCH("유료 검색", "검색 엔진 광고를 통한 유입"),
    SOCIAL_MEDIA("소셜 미디어", "모든 소셜 미디어 플랫폼을 통한 유입"),
    DIRECT("직접 방문", "URL을 직접 입력하거나 북마크를 통한 방문"),
    REFERRAL("추천", "다른 웹사이트의 링크를 통한 유입"),
    EMAIL("이메일", "모든 종류의 이메일을 통한 유입"),
    DISPLAY_ADS("디스플레이 광고", "배너 등 시각적 광고를 통한 유입"),
    AFFILIATE("제휴 마케팅", "제휴 프로그램을 통한 유입"),
    OFFLINE("오프라인", "오프라인 활동이나 이벤트를 통한 유입"),
    MOBILE("모바일", "모바일 앱이나 모바일 특화 캠페인을 통한 유입"),
    CONTENT_MARKETING("컨텐츠 마케팅", "블로그, 비디오 등 컨텐츠를 통한 유입"),
    PARTNERSHIPS("파트너십", "전략적 파트너십을 통한 유입"),
    INTERNAL("내부", "같은 웹사이트 내의 다른 페이지에서의 유입"),
    OTHER("기타", "위 카테고리에 속하지 않는 기타 유입 경로");

    private final String description;
    private final String detailedDescription;
}
