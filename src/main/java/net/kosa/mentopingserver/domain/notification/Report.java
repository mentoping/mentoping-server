package net.kosa.mentopingserver.domain.notification;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import net.kosa.mentopingserver.domain.member.Member;
import net.kosa.mentopingserver.global.common.entity.BaseEntity;
import net.kosa.mentopingserver.global.common.enums.ReportType;
import net.kosa.mentopingserver.global.common.enums.Status;

@Entity
@Getter
@NoArgsConstructor
@SuperBuilder(toBuilder = true)
public class Report extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member targetMember;

    @Column(nullable = false)
    private String reporterId;

    @Column(nullable = false, length = 100)
    private String reason;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Status status;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ReportType reportType;

}
