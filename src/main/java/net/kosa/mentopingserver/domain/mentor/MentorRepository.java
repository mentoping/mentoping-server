package net.kosa.mentopingserver.domain.mentor;

import net.kosa.mentopingserver.domain.mentor.entity.Mentor;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MentorRepository extends JpaRepository<Mentor, Long> {
    Optional<Mentor> findByMemberId(Long memberId);
}
