package net.kosa.mentopingserver.domain.mentor;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import net.kosa.mentopingserver.global.common.entity.BaseEntity;
import net.kosa.mentopingserver.global.common.enums.Category;

import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@NoArgsConstructor
@SuperBuilder(toBuilder = true)
public class MentorCategories extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "mentor_id")
    private Mentor mentor;

    @ElementCollection
    @CollectionTable(name = "mentor_activity_categories", joinColumns = @JoinColumn(name = "mentor_activity_categories_id"))
    @Enumerated(EnumType.STRING)
    @Column(name = "category")
    private Set<Category> categories = new HashSet<>();

    public MentorCategories(Mentor mentor) {
        this.mentor = mentor;
    }

    public void addCategory(Category category) {
        this.categories.add(category);
    }

    public void removeCategory(Category category) {
        this.categories.remove(category);
    }
}
