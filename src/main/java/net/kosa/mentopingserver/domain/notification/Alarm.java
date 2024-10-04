package net.kosa.mentopingserver.domain.notification;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import net.kosa.mentopingserver.domain.member.Member;
import net.kosa.mentopingserver.global.common.entity.BaseEntity;

@Entity
@Getter
@NoArgsConstructor
@SuperBuilder(toBuilder = true)
public class Alarm extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

//    @Enumerated(EnumType.STRING)
//    @Column(nullable = false, length = 50)
//    private AlarmType type;

    @Column(nullable = false, length = 500)
    private String content;

    @Column(nullable = false, columnDefinition = "BOOLEAN DEFAULT false")
    private boolean isRead;

    @Column
    private String relatedLink;
}
