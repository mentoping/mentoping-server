package net.kosa.mentopingserver.domain.mentor;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import net.kosa.mentopingserver.domain.member.Member;
import net.kosa.mentopingserver.global.common.entity.BaseEntity;
import net.kosa.mentopingserver.global.common.enums.Status;
import net.kosa.mentopingserver.global.common.enums.SubCategory;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
@SuperBuilder(toBuilder = true)
public class MentorApplicant extends BaseEntity {

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @Column(nullable = false)
    private String file;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private SubCategory category;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Status status;

    @Column(nullable = false)
    private LocalDateTime submittedAt;

    @Column
    private LocalDateTime reviewedAt;

    @Lob
    @Column(columnDefinition = "TEXT")
    private String review;
}
