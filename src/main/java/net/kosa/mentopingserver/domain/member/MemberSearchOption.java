package net.kosa.mentopingserver.domain.member;

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
public class MemberSearchOption extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false, name = "member_id")
    private Member member;

    @ElementCollection
    @CollectionTable(name = "member_search_categories", joinColumns = @JoinColumn(name = "member_search_option_id"))
    @Enumerated(EnumType.STRING)
    @Column(name = "category")
    private Set<Category> categories = new HashSet<>();

    public MemberSearchOption(Member member) {
        this.member = member;
    }

    public void addCategory(Category category) {
        this.categories.add(category);
    }

    public void removeCategory(Category category) {
        this.categories.remove(category);
    }

    public void clearCategories() {
        this.categories.clear();
    }

}
