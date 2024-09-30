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
public class Post extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Id
    @Column(nullable = false, length = 50)
    private String title;

    @Lob
    @Column(nullable = false, columnDefinition = "TEXT")
    private String content;


}
