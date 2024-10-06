package net.kosa.mentopingserver.domain.answer;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import net.kosa.mentopingserver.domain.member.Member;
import net.kosa.mentopingserver.domain.post.entity.Post;
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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    private Post post;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    public void setPost(Post post) {
        this.post = post;
    }
}
