package net.kosa.mentopingserver.domain.answer;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import net.kosa.mentopingserver.global.common.entity.BaseEntity;
import org.hibernate.annotations.ColumnDefault;

@Entity
@Getter
@NoArgsConstructor
@SuperBuilder(toBuilder = true)
public class Answer extends BaseEntity {

    @Lob
    @Column(nullable = false, columnDefinition = "TEXT")
    private String content;

    @ColumnDefault("false")
    private Boolean isSelected;

    @Lob
    @Column(columnDefinition = "TEXT")
    private String selectedReview;
}
