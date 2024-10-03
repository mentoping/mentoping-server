package net.kosa.mentopingserver.global.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Rank {
    SEED("씨앗"),
    SPROUT("새싹"),
    STEM("줄기"),
    BRANCH("가지"),
    LEAF("잎새"),
    FLOWER("꽃"),
    FRUIT("열매");

    private final String displayName;
}
