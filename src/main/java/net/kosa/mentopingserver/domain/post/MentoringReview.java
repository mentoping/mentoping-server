package net.kosa.mentopingserver.domain.post;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import net.kosa.mentopingserver.global.common.entity.BaseEntity;

@Entity
@Getter
@NoArgsConstructor
@SuperBuilder(toBuilder = true)
public class MentoringReview extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "mentoring_application_id")
    private MentoringApplication mentoringApplication;

    @Column(nullable = false)
    private Integer rate;

    @Lob
    @Column(columnDefinition = "TEXT")
    private String content;
}
