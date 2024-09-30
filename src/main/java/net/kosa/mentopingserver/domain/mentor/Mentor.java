package net.kosa.mentopingserver.domain.mentor;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import net.kosa.mentopingserver.domain.member.Member;
import net.kosa.mentopingserver.global.common.entity.BaseEntity;
import net.kosa.mentopingserver.global.common.enums.SubCategory;

@Entity
@Getter
@NoArgsConstructor
@SuperBuilder(toBuilder = true)
public class Mentor extends BaseEntity {

    @Id
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @MapsId
    @JoinColumn(name = "id")
    private Member member;

    @Column(length = 50)
    private String education;

    @Column(length = 100)
    private String career;

    @Column(length = 100)
    private String location;

    @Column(length = 20)
    private String contact;

    @Column
    private String sns;

    @Column
    private String profile;

    @Lob
    @Column(columnDefinition = "TEXT")
    private String content;

    @Enumerated(EnumType.STRING)
    private SubCategory category;

    @Column
    private Integer exp;

}
