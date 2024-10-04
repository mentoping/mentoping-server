package net.kosa.mentopingserver.domain.member;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import net.kosa.mentopingserver.global.common.entity.BaseEntity;

@Entity
@Getter
@NoArgsConstructor
@SuperBuilder(toBuilder = true)
public class MemberChatRoom extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @Column(nullable = false)
    private Integer chatRoomId;
}
