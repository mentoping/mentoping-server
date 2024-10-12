package net.kosa.mentopingserver.domain.Inquiry;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import net.kosa.mentopingserver.domain.member.entity.Member;
import net.kosa.mentopingserver.global.common.entity.BaseEntity;

@Entity
@Getter
@NoArgsConstructor
@SuperBuilder(toBuilder = true)
public class Inquiry extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @Column
    private String title;

    @Lob
    @Column(nullable = false, columnDefinition = "TEXT")
    private String inquiryContent;


    @Lob
    @Column(nullable = false, columnDefinition = "TEXT")
    private String answerContent;

}
