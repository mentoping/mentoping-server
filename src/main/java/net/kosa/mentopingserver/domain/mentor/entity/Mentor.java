package net.kosa.mentopingserver.domain.mentor.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import net.kosa.mentopingserver.domain.member.entity.Member;
import net.kosa.mentopingserver.global.common.entity.BaseEntity;
import net.kosa.mentopingserver.global.common.enums.MentorRank;
import net.kosa.mentopingserver.global.common.enums.Category;

@Entity
@Getter
@NoArgsConstructor
@SuperBuilder(toBuilder = true)
public class Mentor extends BaseEntity {

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @Column(length = 20)
    private String contact;

    @Column
    private String sns;

    @Lob
    @Column(columnDefinition = "TEXT")
    private String content;

    @Enumerated(EnumType.STRING)
    private Category category;

    @Column
    private Integer exp;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private MentorRank mentorRank;
}
