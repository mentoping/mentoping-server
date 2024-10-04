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
public class Report extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member targetMember;

    @Column(nullable = false)
    private String type;

    @Column(nullable = false)
    private String reporterId;

    @Column(nullable = false, length = 100)
    private String reason;

    @Column(nullable = false)
    private String status;

//    @Enumerated(EnumType.STRING)
//    private ReportType type;
//
//    @Enumerated(EnumType.STRING)
//    private ReportStatus status;
}
