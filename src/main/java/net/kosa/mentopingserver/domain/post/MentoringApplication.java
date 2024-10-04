package net.kosa.mentopingserver.domain.post;

import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import net.kosa.mentopingserver.global.common.entity.BaseEntity;

@Entity
@Getter
@NoArgsConstructor
@SuperBuilder(toBuilder = true)
public class MentoringApplication extends BaseEntity {

}
