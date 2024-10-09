package net.kosa.mentopingserver.domain.post.entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import net.kosa.mentopingserver.domain.answer.Answer;
import net.kosa.mentopingserver.domain.hashtag.PostHashtag;
import net.kosa.mentopingserver.domain.member.Member;
import net.kosa.mentopingserver.global.common.entity.BaseEntity;
import net.kosa.mentopingserver.global.common.enums.Category;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Entity
@Getter
@NoArgsConstructor
@SuperBuilder(toBuilder = true)
public class Post extends BaseEntity {

    @Column(nullable = false, length = 50)
    private String title;

    @Lob
    @Column(nullable = false, columnDefinition = "TEXT")
    private String content;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Category category;

    @Column
    private Long price;

    @Column(columnDefinition = "integer default 0")
    private int likeCount = 0;

    @Column(columnDefinition = "integer default 0")
    private int answerCount = 0;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @Builder.Default
    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Answer> answers = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PostHashtag> postHashtags = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PostLikes> postLikes = new ArrayList<>();

    public boolean isSelected() {
        return Optional.ofNullable(answers) // answers가 null일 경우 빈 리스트를 사용
                .orElseGet(ArrayList::new)
                .stream()
                .anyMatch(answer -> Boolean.TRUE.equals(answer.getIsSelected()));
    }

    public void incrementLikeCount() {
        this.likeCount++;
    }

    public void decrementLikeCount() {
        if (this.likeCount > 0) {
            this.likeCount--;
        }
    }

    public void incrementAnswerCount() {
        this.answerCount++;
    }

    public void decrementAnswerCount() {
        if (this.answerCount > 0) {
            this.answerCount--;
        }
    }

}
